package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserDto toDto();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModel();


}
