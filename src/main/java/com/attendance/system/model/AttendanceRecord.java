package com.attendance.system.model;

import java.time.LocalDateTime;

public class AttendanceRecord {
    
    private Long id;
    private String studentName;
    private String studentRollNumber;
    private String studentSection;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private AttendanceStatus status;
    
    public enum AttendanceStatus {
        PRESENT, ABSENT
    }
    
    // Constructors
    public AttendanceRecord() {
    }
    
    public AttendanceRecord(String studentName, String studentRollNumber, String studentSection,
                           Double latitude, Double longitude, LocalDateTime timestamp, AttendanceStatus status) {
        this.studentName = studentName;
        this.studentRollNumber = studentRollNumber;
        this.studentSection = studentSection;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getStudentRollNumber() {
        return studentRollNumber;
    }
    
    public void setStudentRollNumber(String studentRollNumber) {
        this.studentRollNumber = studentRollNumber;
    }
    
    public String getStudentSection() {
        return studentSection;
    }
    
    public void setStudentSection(String studentSection) {
        this.studentSection = studentSection;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public AttendanceStatus getStatus() {
        return status;
    }
    
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
