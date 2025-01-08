package dev.core.mapper;

import dev.core.domain.Task;
import dev.core.dto.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDto(Task task);
}
