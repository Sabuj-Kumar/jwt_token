package com.springJWTProject.springSecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

	@GetMapping("only")
	public ResponseEntity<String> sayHello() {
		System.out.println("admin method call succesfully");
		return ResponseEntity.ok("i am admin");
	}
}
