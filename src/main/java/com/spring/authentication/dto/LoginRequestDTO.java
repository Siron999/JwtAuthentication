package com.spring.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.authentication.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequestDTO {
    private String username;
    private String password;
    private RoleEnum role;
}
