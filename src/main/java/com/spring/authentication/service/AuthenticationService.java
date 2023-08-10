package com.spring.authentication.service;

import com.spring.authentication.dto.LoginRequestDTO;
import com.spring.authentication.dto.LoginResponseDTO;
import com.spring.authentication.dto.StudentDTO;
import com.spring.authentication.enums.RoleEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthenticationService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    StudentDTO registerAsStudent(StudentDTO studentDTO);

    void createAdmin();

    UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String username, RoleEnum role);
}
