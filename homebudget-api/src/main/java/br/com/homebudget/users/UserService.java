package br.com.homebudget.users;

import br.com.homebudget.shared.exceptions.ConflictException;
import br.com.homebudget.shared.exceptions.NotFoundException;
import br.com.homebudget.users.dto.UserDTO;
import br.com.homebudget.users.dto.UserInputDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserDTO> getAll(String name, Pageable pageable) {
        Specification<UserEntity> spec = Specification.where(UserSpecifications.byName(name));

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toDTO);
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario Não Encontrado"));
    }

    @Transactional
    public UserEntity create(@Valid UserInputDTO userInputDTO) {
        boolean exists = userRepository.existsByEmail(userInputDTO.email());
        if (exists) {
            throw new ConflictException("Já existe um usuario com este email");
        }

        UserEntity userEntity = UserMapper.INSTANCE.toEntity(userInputDTO);
        userEntity.setPassword(passwordEncoder.encode(userInputDTO.password()));

        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity update(Long id, UserInputDTO userInputDTO) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado para o ID:" + id));

        boolean exists = userRepository.existsByEmail(userInputDTO.email());
        if (exists) {
            throw new ConflictException("Já existe um usuario com este email");
        }

        userMapper.updateEntityFromDto(userInputDTO, entity);
        return userRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado."));
        userRepository.deleteById(entity.getId());
    }

}
