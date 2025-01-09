package dev.core.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private RoleDTO role;
}
