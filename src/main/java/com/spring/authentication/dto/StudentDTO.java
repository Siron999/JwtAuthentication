package com.spring.authentication.dto;

import com.spring.authentication.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentDTO extends UserDTO {
    private Long studentId;
    private final RoleEnum role = RoleEnum.ROLE_STUDENT;
}
