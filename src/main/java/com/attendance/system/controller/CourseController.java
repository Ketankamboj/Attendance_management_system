package com.attendance.system.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.attendance.system.model.Course;
import com.attendance.system.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public String listCourses(HttpSession session, Model model) {
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        model.addAttribute("courses", courses);
        
        return "courses";
    }
    
    @GetMapping("/create")
    public String createCourseForm() {
        return "create-course";
    }
    
    @PostMapping("/create")
    public String createCourse(@RequestParam String name,
                             @RequestParam String courseCode,
                             @RequestParam String schedule,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        Course newCourse = courseService.createCourse(name, courseCode, schedule, teacherId);
        
        redirectAttributes.addFlashAttribute("success", "Course created successfully");
        return "redirect:/courses";
    }
    
    @GetMapping("/{id}")
    public String viewCourse(@PathVariable String id, Model model, HttpSession session) {
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        Course course = courseService.findById(id);
        
        if (course == null || !course.getTeacherId().equals(teacherId)) {
            return "redirect:/courses";
        }
        
        model.addAttribute("course", course);
        return "view-course";
    }
    
    @GetMapping("/{id}/edit")
    public String editCourseForm(@PathVariable String id, Model model, HttpSession session) {
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        Course course = courseService.findById(id);
        
        if (course == null || !course.getTeacherId().equals(teacherId)) {
            return "redirect:/courses";
        }
        
        model.addAttribute("course", course);
        return "edit-course";
    }
    
    @PostMapping("/{id}/edit")
    public String updateCourse(@PathVariable String id,
                             @RequestParam String name,
                             @RequestParam String courseCode,
                             @RequestParam String schedule,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        Course course = courseService.findById(id);
        
        if (course == null || !course.getTeacherId().equals(teacherId)) {
            return "redirect:/courses";
        }
        
        courseService.updateCourse(id, name, courseCode, schedule);
        
        redirectAttributes.addFlashAttribute("success", "Course updated successfully");
        return "redirect:/courses";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable String id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        String teacherId = (String) session.getAttribute("teacherId");
        
        if (teacherId == null) {
            return "redirect:/login";
        }
        
        Course course = courseService.findById(id);
        
        if (course == null || !course.getTeacherId().equals(teacherId)) {
            return "redirect:/courses";
        }
        
        courseService.deleteCourse(id);
        
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully");
        return "redirect:/courses";
    }
}