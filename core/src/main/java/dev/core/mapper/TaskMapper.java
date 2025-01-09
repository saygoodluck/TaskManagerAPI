package dev.core.mapper;

import dev.core.domain.Task;
import dev.core.dto.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    TaskDTO toDto(Task task);
}
