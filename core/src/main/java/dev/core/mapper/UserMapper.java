package dev.core.mapper;

import dev.core.domain.User;
import dev.core.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);
}
