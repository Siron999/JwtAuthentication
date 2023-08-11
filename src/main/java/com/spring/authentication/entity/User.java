package com.spring.authentication.entity;

import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Data
public class User {

    private String username;

    private String fullName;

    private String password;

    private String address;
}
