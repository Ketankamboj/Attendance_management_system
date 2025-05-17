package com.attendance.system.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.attendance.system.model.Course;
import com.attendance.system.model.Teacher;
import com.attendance.system.service.CourseService;
import com.attendance.system.service.TeacherService;

@Controller
public class HomeController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Attendance Management System");
        model.addAttribute("welcomeMessage", "Welcome to the Attendance Management System");
        return "home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        // Add teacher information to the model
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("teacherName", session.getAttribute("teacherName"));
        model.addAttribute("teacherEmail", session.getAttribute("teacherEmail"));
        
        // Get courses for the teacher
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        
        return "dashboard";
    }
    
    @GetMapping("/directDashboard")
    public String directDashboard(HttpSession session) {
        // Get default teacher for testing
        Teacher teacher = teacherService.findByEmail("teacher@example.com");
        
        if (teacher != null) {
            // Set session attributes manually
            session.setAttribute("teacherId", teacher.getId());
            session.setAttribute("teacherName", teacher.getName());
            session.setAttribute("teacherEmail", teacher.getEmail());
            
            System.out.println("Direct dashboard access granted for: " + teacher.getName());
            return "redirect:/dashboard";
        } else {
            System.out.println("Default teacher not found for direct access");
            return "redirect:/login";
        }
    }
}
