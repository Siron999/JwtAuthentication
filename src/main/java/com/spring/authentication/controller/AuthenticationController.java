package com.spring.authentication.controller;

import com.spring.authentication.dto.LoginRequestDTO;
import com.spring.authentication.dto.LoginResponseDTO;
import com.spring.authentication.dto.StudentDTO;
import com.spring.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<StudentDTO> registerAsStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerAsStudent(studentDTO));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDTO) {
        return ResponseEntity.ok().body(authenticationService.login(loginDTO));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<String> onlyAdmin() {
        return ResponseEntity.ok().body("Only Admin");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STUDENT')")
    @GetMapping("student")
    public ResponseEntity<String> studentAndAdmin() {
        return ResponseEntity.ok().body("Student And Admin");
    }


}
