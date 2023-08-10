package com.spring.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String username;

    private String fullName;

    private String password;

    private String address;
}
