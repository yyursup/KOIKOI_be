package com.example.demo.repository;

import com.example.demo.entity.student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<student, Long> {
    //Tìm 1 thằng student bằng id của nó
    //findStudentById(long id) find + Student + By + Id(long id)
    student findStudentById(long id);

    //lấy danh sách những thằng student mà biến isDeleted = false;
    List<student> findStudentsByIsDeletedFalse();
}
