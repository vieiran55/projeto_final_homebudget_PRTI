package br.com.homebudget.investimentos;

import br.com.homebudget.investimentos.dto.InvestimentoDTO;
import br.com.homebudget.investimentos.dto.InvestimentoInputDTO;
import br.com.homebudget.shared.mapper.UserMapperHelper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapperHelper.class)
public interface InvestimentoMapper {
    InvestimentoMapper INSTANCE = Mappers.getMapper(InvestimentoMapper.class);

    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserEntityToUserId")
    InvestimentoDTO toDto(InvestimentoEntity entity);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUserEntity")
    InvestimentoEntity toEntity(InvestimentoInputDTO inputDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(InvestimentoInputDTO inputDTO, @MappingTarget InvestimentoEntity entity);
}
