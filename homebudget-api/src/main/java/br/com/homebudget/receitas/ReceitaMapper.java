package br.com.homebudget.receitas;

import br.com.homebudget.receitas.dto.ReceitaDTO;
import br.com.homebudget.receitas.dto.ReceitaInputDTO;
import br.com.homebudget.shared.mapper.UserMapperHelper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapperHelper.class)
public interface ReceitaMapper {
    ReceitaMapper INSTANCE = Mappers.getMapper(ReceitaMapper.class);

    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserEntityToUserId")
    ReceitaDTO toDto(ReceitaEntity entity);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUserEntity")
    ReceitaEntity toEntity(ReceitaInputDTO inputDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ReceitaInputDTO inputDTO, @MappingTarget ReceitaEntity entity);
}
