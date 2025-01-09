package dev.api.dto;

import lombok.Data;

@Data
public class MyUserDetailsDTO {
    private Long id;
    private String username;
    private String roleName;
}
