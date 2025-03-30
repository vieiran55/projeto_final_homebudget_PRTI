package br.com.homebudget.shared.mapper;

import br.com.homebudget.users.UserEntity;
import org.mapstruct.Named;

public class UserMapperHelper {
    @Named("mapUserIdToUserEntity")
    public static UserEntity mapUserIdToUserEntity(Long userId) {
        if (userId == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userId);
        return user;
    }

    @Named("mapUserEntityToUserId")
    public static Long mapUserEntityToUserId(UserEntity user) {
        return (user != null) ? user.getId() : null;
    }
}
