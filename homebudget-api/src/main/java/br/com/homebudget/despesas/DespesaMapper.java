package br.com.homebudget.despesas;

import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.despesas.dto.DespesaInputDTO;
import br.com.homebudget.shared.mapper.UserMapperHelper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapperHelper.class)
public interface DespesaMapper {
    DespesaMapper INSTANCE = Mappers.getMapper(DespesaMapper.class);

    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserEntityToUserId")
    DespesaDTO toDto(DespesaEntity entity);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUserEntity")
    DespesaEntity toEntity(DespesaInputDTO inputDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DespesaInputDTO inputDTO, @MappingTarget DespesaEntity entity);
}
