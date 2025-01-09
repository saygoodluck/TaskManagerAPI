package dev.core.mapper;

import dev.core.domain.Role;
import dev.core.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDto(Role role);
}
