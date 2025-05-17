package com.attendance.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.attendance.system.model.Course;

@Service
public class CourseService {
    
    // In-memory storage for courses
    private final Map<String, Course> courses = new HashMap<>();
    
    /**
     * Create a new course
     */
    public Course createCourse(String name, String courseCode, String schedule, String teacherId) {
        Course course = new Course(name, courseCode, schedule, teacherId);
        String id = UUID.randomUUID().toString();
        course.setId(id);
        
        courses.put(id, course);
        return course;
    }
    
    /**
     * Find a course by ID
     */
    public Course findById(String id) {
        return courses.get(id);
    }
    
    /**
     * Get all courses for a teacher
     */
    public List<Course> getCoursesByTeacherId(String teacherId) {
        return courses.values().stream()
                .filter(course -> course.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Update a course
     */
    public Course updateCourse(String id, String name, String courseCode, String schedule) {
        Course course = findById(id);
        if (course != null) {
            course.setName(name);
            course.setCourseCode(courseCode);
            course.setSchedule(schedule);
            return course;
        }
        return null;
    }
    
    /**
     * Delete a course
     */
    public boolean deleteCourse(String id) {
        if (courses.containsKey(id)) {
            courses.remove(id);
            return true;
        }
        return false;
    }
}
