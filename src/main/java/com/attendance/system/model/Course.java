package com.attendance.system.model;

public class Course {
    
    private String id;
    private String name;
    private String courseCode;
    private String schedule;
    private String teacherId;
    
    // Constructors
    public Course() {
    }
    
    public Course(String name, String courseCode, String schedule, String teacherId) {
        this.name = name;
        this.courseCode = courseCode;
        this.schedule = schedule;
        this.teacherId = teacherId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
