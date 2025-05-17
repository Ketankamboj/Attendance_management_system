package com.attendance.system.controller;

import com.attendance.system.model.Teacher;
import com.attendance.system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping("/login")
    public String login(Model model) {
        System.out.println("Login page accessed");
        return "login";
    }
    
    @PostMapping("/login")
    public String loginProcess(@RequestParam String email, 
                             @RequestParam String password,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        System.out.println("Login attempt: " + email);
        
        Teacher teacher = teacherService.authenticate(email, password);
        
        if (teacher != null) {
            System.out.println("Login successful for: " + teacher.getName());
            // Store teacher in session
            session.setAttribute("teacherId", teacher.getId());
            session.setAttribute("teacherName", teacher.getName());
            session.setAttribute("teacherEmail", teacher.getEmail());
            
            // Use a dedicated success page with JavaScript redirect
            return "login-success";
        } else {
            System.out.println("Login failed for: " + email);
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        System.out.println("Register page accessed");
        return "register";
    }
    
    @PostMapping("/register")
    public String registerProcess(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String department,
                                RedirectAttributes redirectAttributes) {
        
        System.out.println("Registration attempt: " + email);
        
        Teacher newTeacher = teacherService.registerTeacher(name, email, password, department);
        
        if (newTeacher != null) {
            System.out.println("Registration successful for: " + newTeacher.getName());
            // Use a dedicated success page with JavaScript redirect
            return "register-success";
        } else {
            System.out.println("Registration failed - Email already exists: " + email);
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}