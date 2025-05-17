package com.attendance.system.controller;

import com.attendance.system.model.AttendanceRecord;
import com.attendance.system.util.LocationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AttendanceController {
    
    // In a real application, these would be stored in a database
    // For demo purposes, we'll use in-memory storage
    private final Map<String, Map<String, Object>> activeAttendanceSessions = new HashMap<>();
    private final List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    
    /**
     * Display the form for creating a new attendance session
     */
    @GetMapping("/create-session")
    public String createSessionForm(Model model) {
        model.addAttribute("pageTitle", "Create Attendance Session");
        return "create-session";
    }
    
    /**
     * Handle the submission of a new attendance session
     */
    @PostMapping("/create-session")
    public String createSession(@RequestParam String courseName,
                              @RequestParam Double latitude,
                              @RequestParam Double longitude,
                              @RequestParam Integer radius,
                              @RequestParam Integer duration,
                              Model model) {
        
        // Generate a unique token for this session
        String token = "ABC" + System.currentTimeMillis();
        
        // Store session details
        Map<String, Object> sessionDetails = new HashMap<>();
        sessionDetails.put("courseName", courseName);
        sessionDetails.put("latitude", latitude);
        sessionDetails.put("longitude", longitude);
        sessionDetails.put("radius", radius);
        sessionDetails.put("createdAt", LocalDateTime.now());
        sessionDetails.put("expiresAt", LocalDateTime.now().plusMinutes(duration));
        
        activeAttendanceSessions.put(token, sessionDetails);
        
        // Generate attendance URL
        String attendanceUrl = "/mark-attendance/" + token;
        
        model.addAttribute("pageTitle", "Session Created");
        model.addAttribute("token", token);
        model.addAttribute("attendanceUrl", attendanceUrl);
        model.addAttribute("sessionDetails", sessionDetails);
        
        return "session-created";
    }
    
    /**
     * Display the attendance marking form for students
     */
    @GetMapping("/mark-attendance/{token}")
    public String markAttendanceForm(@PathVariable String token, Model model) {
        // Check if the session exists and is still valid
        if (!activeAttendanceSessions.containsKey(token)) {
            model.addAttribute("error", "Invalid or expired attendance session");
            return "attendance-error";
        }
        
        Map<String, Object> sessionDetails = activeAttendanceSessions.get(token);
        LocalDateTime expiresAt = (LocalDateTime) sessionDetails.get("expiresAt");
        
        if (LocalDateTime.now().isAfter(expiresAt)) {
            model.addAttribute("error", "Attendance session has expired");
            return "attendance-error";
        }
        
        model.addAttribute("token", token);
        model.addAttribute("courseName", sessionDetails.get("courseName"));
        model.addAttribute("expiresAt", expiresAt);
        
        return "mark-attendance";
    }
    
    /**
     * Process attendance submission
     */
    @PostMapping("/mark-attendance/{token}")
    @ResponseBody
    public Map<String, Object> submitAttendance(@PathVariable String token,
                                               @RequestParam String name,
                                               @RequestParam String rollNumber,
                                               @RequestParam String section,
                                               @RequestParam Double latitude,
                                               @RequestParam Double longitude) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Check if the session exists and is still valid
        if (!activeAttendanceSessions.containsKey(token)) {
            response.put("status", "error");
            response.put("message", "Invalid or expired attendance session");
            return response;
        }
        
        Map<String, Object> sessionDetails = activeAttendanceSessions.get(token);
        LocalDateTime expiresAt = (LocalDateTime) sessionDetails.get("expiresAt");
        
        if (LocalDateTime.now().isAfter(expiresAt)) {
            response.put("status", "error");
            response.put("message", "Attendance session has expired");
            return response;
        }
        
        // Check if the student is within the allowed radius
        Double sessionLat = (Double) sessionDetails.get("latitude");
        Double sessionLon = (Double) sessionDetails.get("longitude");
        Integer allowedRadius = (Integer) sessionDetails.get("radius");
        
        boolean isWithinRadius = LocationUtils.isWithinRadius(
            sessionLat, sessionLon, latitude, longitude, allowedRadius);
        
        AttendanceRecord.AttendanceStatus status = isWithinRadius ? 
            AttendanceRecord.AttendanceStatus.PRESENT : AttendanceRecord.AttendanceStatus.ABSENT;
        
        // Create and store attendance record
        AttendanceRecord record = new AttendanceRecord(
            name, rollNumber, section, latitude, longitude, LocalDateTime.now(), status);
        
        attendanceRecords.add(record);
        
        // Prepare response
        response.put("status", "success");
        if (isWithinRadius) {
            response.put("message", "You have been marked as present for " + sessionDetails.get("courseName"));
        } else {
            response.put("message", "You have been marked as absent because you are not within the required location");
            double distance = LocationUtils.calculateDistance(sessionLat, sessionLon, latitude, longitude);
            response.put("distance", Math.round(distance));
            response.put("allowedRadius", allowedRadius);
        }
        
        return response;
    }
    
    /**
     * View attendance records for a session
     */
    @GetMapping("/view-records/{token}")
    public String viewRecords(@PathVariable String token, Model model) {
        // Check if the session exists
        if (!activeAttendanceSessions.containsKey(token)) {
            model.addAttribute("error", "Invalid attendance session");
            return "attendance-error";
        }
        
        Map<String, Object> sessionDetails = activeAttendanceSessions.get(token);
        
        // Filter records for this session
        List<AttendanceRecord> sessionRecords = new ArrayList<>();
        for (AttendanceRecord record : attendanceRecords) {
            if (record.getStudentRollNumber() != null && record.getStudentSection() != null) {
                sessionRecords.add(record);
            }
        }
        
        model.addAttribute("sessionDetails", sessionDetails);
        model.addAttribute("records", sessionRecords);
        
        return "view-records";
    }
}