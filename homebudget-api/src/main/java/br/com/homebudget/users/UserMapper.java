package br.com.homebudget.users;

import br.com.homebudget.users.dto.UserDTO;
import br.com.homebudget.users.dto.UserInputDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(UserEntity entity);

    UserEntity toEntity(UserInputDTO inputDTO);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserInputDTO dto, @MappingTarget UserEntity entity);
}
