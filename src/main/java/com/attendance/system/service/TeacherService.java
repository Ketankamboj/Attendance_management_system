package com.attendance.system.service;

import com.attendance.system.model.Teacher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TeacherService {
    
    // In-memory storage for teachers
    private final Map<String, Teacher> teachers = new HashMap<>();
    
    @PostConstruct
    public void init() {
        // Add a default teacher for testing
        registerTeacher("Demo Teacher", "teacher@example.com", "password", "Computer Science");
        System.out.println("Added default teacher: teacher@example.com / password");
    }
    
    /**
     * Register a new teacher
     */
    public Teacher registerTeacher(String name, String email, String password, String department) {
        // Check if email already exists
        if (findByEmail(email) != null) {
            return null; // Email already exists
        }
        
        Teacher teacher = new Teacher(name, email, password, department);
        String id = UUID.randomUUID().toString();
        teacher.setId(id);
        
        teachers.put(id, teacher);
        return teacher;
    }
    
    /**
     * Find a teacher by their email
     */
    public Teacher findByEmail(String email) {
        return teachers.values().stream()
                .filter(t -> t.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find a teacher by their ID
     */
    public Teacher findById(String id) {
        return teachers.get(id);
    }
    
    /**
     * Authenticate a teacher
     */
    public Teacher authenticate(String email, String password) {
        Teacher teacher = findByEmail(email);
        if (teacher != null && teacher.getPassword().equals(password)) {
            return teacher;
        }
        return null;
    }
    
    /**
     * Get all teachers
     */
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers.values());
    }
}
