package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentRequest {
    @NotBlank(message = "Name can not blank!")
    String name;

    @NotBlank(message = "studentCode can not blank!")
    // SE000001
    @Pattern(regexp = "SE\\d{6}")
    @Column(unique = true)
    String studentCode;

    @Min(value = 0, message = "Score must be at last 0")
    @Max(value = 10, message = "Score must not be more than 10!")
    float score;


}
