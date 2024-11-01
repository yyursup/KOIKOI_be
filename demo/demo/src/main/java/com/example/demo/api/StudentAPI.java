package com.example.demo.api;

import com.example.demo.entity.student;
import com.example.demo.model.StudentRequest;
import com.example.demo.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*") //cho phép tất cả truy cập
@RequestMapping("/api/student")
@SecurityRequirement(name = "api")
@PreAuthorize("hasAuthority('TEACHER')")
public class StudentAPI {

    // Them 1 sv moi
    // /api/student => POST

    @Autowired
    StudentService studentService;
    @PostMapping

    public ResponseEntity createStudent(@Valid @RequestBody StudentRequest student) {
        //nhờ service tạo mới 1 account
        student newStudent = studentService.createNewStudent(student);
        //return về cho fe
        return ResponseEntity.ok(newStudent);
    }

    // lay ds sv hien tai
    @GetMapping
    public ResponseEntity getAllStudents() {
        List<student> students = studentService.getAllStudent();
        return ResponseEntity.ok(students);
    }

    @PutMapping("{id}")
    public ResponseEntity updateStudent(@Valid @RequestBody student student, @PathVariable Long id) {
        student newStudent = studentService.updateStudent(student, id);
        return ResponseEntity.ok(newStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        student newStudent = studentService.DeleteStudent(id);
        return ResponseEntity.ok(newStudent);
    }

}
