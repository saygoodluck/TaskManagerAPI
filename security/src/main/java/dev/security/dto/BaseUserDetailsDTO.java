package dev.security.dto;

import lombok.Data;

@Data
public class BaseUserDetailsDTO {

    private Long id;

    private String username;

    private RoleDTO roleName;
}
