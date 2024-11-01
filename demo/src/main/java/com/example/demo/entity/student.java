package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity //để đánh dấu đây là một thực thể lưu xuống database
//Data jpa sẽ giúp làm việc với DB => nếu chưa có table Student => nó se giúp tạo table Student
public class student {
    @Id //đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tự generate ra cột này
    long id;

    boolean isDeleted = false ;

    String name;

    @Column(unique = true)
    String studentCode;

    float score;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToMany
    @JoinTable(name = "student_class",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    Set<ClassEntity> classEntities;

}
