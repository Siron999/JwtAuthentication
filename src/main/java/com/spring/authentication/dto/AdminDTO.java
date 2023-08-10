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
public class AdminDTO extends UserDTO {
    private Long adminId;
    private final RoleEnum role = RoleEnum.ROLE_ADMIN;
}
