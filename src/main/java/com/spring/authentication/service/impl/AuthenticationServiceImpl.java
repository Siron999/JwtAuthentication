package com.spring.authentication.service.impl;

import com.spring.authentication.dto.*;
import com.spring.authentication.entity.Admin;
import com.spring.authentication.entity.Student;
import com.spring.authentication.enums.RoleEnum;
import com.spring.authentication.repository.AdminRepository;
import com.spring.authentication.repository.StudentRepository;
import com.spring.authentication.service.AuthenticationService;
import com.spring.authentication.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO;
        if (loginRequestDTO.getRole().equals(RoleEnum.ROLE_ADMIN)) {
            loginResponseDTO = this.loginAsAdmin(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        } else {
            loginResponseDTO = this.loginAsStudent(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        }
        return loginResponseDTO;
    }

    private LoginResponseDTO loginAsAdmin(String username, String password) {
        AdminDTO adminDTO = this.getAdminByUsername(username);
        return this.authenticate(adminDTO, password, adminDTO.getRole());
    }

    private LoginResponseDTO loginAsStudent(String username, String password) {
        StudentDTO studentDTO = this.getStudentByUsername(username);
        return this.authenticate(studentDTO, password, studentDTO.getRole());
    }

    private LoginResponseDTO authenticate(UserDTO userDTO, String rawPassword, RoleEnum role) throws UsernameNotFoundException {
        this.checkPassword(rawPassword, userDTO.getPassword());
        List<SimpleGrantedAuthority> authorities = this.addAuthority(role);
        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword(),
                authorities);
        String accessToken = this.jwtUtil.generateToken(
                usernamePasswordAuthenticationToken.getName(),
                this.generateRoles(usernamePasswordAuthenticationToken.getAuthorities())
        );
        userDTO.setPassword(null);
        return new LoginResponseDTO(accessToken, userDTO);
    }

    private List<String> generateRoles(Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = new ArrayList<>();
        authorities.forEach(grantedAuthority -> roles.add(grantedAuthority.toString()));
        return roles;
    }


    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new BadCredentialsException("Password Incorrect");
    }

    @Override
    public StudentDTO registerAsStudent(StudentDTO studentDTO) {
        Student student = this.modelMapper.map(studentDTO, Student.class);
        if (this.userNameExists(student.getUsername())) {
            log.error("Username {} already taken", student.getUsername());
            throw new RuntimeException("Username already taken");
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        this.studentRepository.save(student);
        student.setPassword(null);
        return this.modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public void createAdmin() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(this.passwordEncoder.encode("password123"));
        admin.setAddress("Jawalakhel");
        admin.setFullName("Siron Shakya");
        this.adminRepository.save(admin);
    }

    @Override
    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String username, RoleEnum role) {
        if (role.equals(RoleEnum.ROLE_STUDENT)) {
            StudentDTO studentDTO = this.getStudentByUsername(username);
            List<SimpleGrantedAuthority> authorities = this.addAuthority(studentDTO.getRole());
            return new UsernamePasswordAuthenticationToken(studentDTO.getUsername(), studentDTO.getPassword(),
                    authorities);
        } else {
            AdminDTO adminDTO = this.getAdminByUsername(username);
            List<SimpleGrantedAuthority> authorities = this.addAuthority(adminDTO.getRole());
            return new UsernamePasswordAuthenticationToken(adminDTO.getUsername(), adminDTO.getPassword(),
                    authorities);
        }
    }

    private List<SimpleGrantedAuthority> addAuthority(RoleEnum roleEnum) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (Objects.nonNull(roleEnum)) {
            authorities.add(new SimpleGrantedAuthority(roleEnum.toString()));
        }
        return authorities;
    }

    private AdminDTO getAdminByUsername(String username) {
        Admin admin = this.adminRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return modelMapper.map(admin, AdminDTO.class);
    }

    private StudentDTO getStudentByUsername(String username) {
        Student student = this.studentRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return modelMapper.map(student, StudentDTO.class);
    }

    private Boolean userNameExists(String username) {
        Optional<Admin> savedAdmin = this.adminRepository.findByUsername(username);
        Optional<Student> savedStudent = this.studentRepository.findByUsername(username);
        return savedAdmin.isPresent() || savedStudent.isPresent();
    }
}
