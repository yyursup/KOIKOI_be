package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.student;
import com.example.demo.model.StudentRequest;
import com.example.demo.repository.StudentRepository;
import jakarta.security.auth.message.AuthException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //đánh dấu đây là 1 lớp để xử l logic
public class StudentService {
    //xử lí những logic liên quan tới student

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    AuthenticationService authenticationService;

    public student createNewStudent(StudentRequest studentRequest) {
        //add student vào database bằng repository
        try {
            student student = modelMapper.map(studentRequest, student.class);

            // xac dinh account nao tao cai student nay
            // thong qua dc filter
            // luu lai dc cai account yeu cau tao student
            Account accountRequest = authenticationService.getCurrentAccount();
            student.setAccount(accountRequest);

            student newStudent = studentRepository.save(student);
            return newStudent;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new student");
        }
    }

    public List<student> getAllStudent() {
        List<student> students = studentRepository.findStudentsByIsDeletedFalse();
        return students;
    }

    public student updateStudent(student student, long studentId) {
        // bước 1: Tìm tới student có id như fe cung cấp
        student oldStudent = studentRepository.findStudentById(studentId);

        if(oldStudent == null) {
            throw new RuntimeException("Student not found");
        }
        // student đã tồn tại
        oldStudent.setStudentCode(student.getStudentCode());
        oldStudent.setName(student.getName());
        oldStudent.setScore(student.getScore());
        return studentRepository.save(oldStudent);
    }

    public student DeleteStudent(long studentId) {
        student oldStudent = studentRepository.findStudentById(studentId);

        if(oldStudent == null) {
            throw new RuntimeException("Student not found");
        }

        oldStudent.setDeleted(true);
        return studentRepository.save(oldStudent);

    }
}
