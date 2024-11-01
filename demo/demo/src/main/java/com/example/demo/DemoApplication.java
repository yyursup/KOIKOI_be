package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Student API", version = "1.0", description = "Information"))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class DemoApplication {

	// Quan ly sinh vien
	// SSinh vien (name, studentCode, score)
	// Lay ra danh sach tat ca sinh vien
	// Them 1 sv moi vao ds
	// Update score cua sv
	// Delete 1 sv nao do

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// Restfull API
	// cach dat ten api ( /api/ )

	// Them 1 sv moi
	// /api/student   => POST: Tao 1 thang student moi
	// /api/student/studentID   => PUT: update thong tin cua thang hoc sinh
	// /api/student   => GET: lay tat ca student
	// /api/student/1   => GET: lay thong tin cua 1 thang cu the
	// /api/student/1   => DELETE: lay thong tin cua thang can delete


	// METHOD:
	/*
	*	POST => create
	*	PUT => update
	* 	DELETE => delete
	* 	GET => get
	*
	* */
}
