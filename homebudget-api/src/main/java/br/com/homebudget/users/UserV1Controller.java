package br.com.homebudget.users;

import br.com.homebudget.auth.AuthService;
import br.com.homebudget.shared.dto.MetaResponse;
import br.com.homebudget.shared.response.PagedResponse;
import br.com.homebudget.users.dto.UserDTO;
import br.com.homebudget.users.dto.UserInputDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserV1Controller {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;

    @GetMapping("/user")
    public ResponseEntity<PagedResponse<UserDTO>> index(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserDTO> userDTOPage = userService.getAll(name, pageable);

        PagedResponse<UserDTO> response = new PagedResponse<>(
                userDTOPage.getContent(),
                new MetaResponse(
                        (int) userDTOPage.getTotalElements(),
                        userDTOPage.getTotalPages(),
                        userDTOPage.getNumber() + 1,
                        userDTOPage.getSize()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserEntity entity = userService.getById(id);
        UserDTO userDTO = userMapper.toDTO(entity);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserInputDTO userInputDTO) {
        UserEntity entity = userService.create(userInputDTO);
        UserDTO userDTO = userMapper.toDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserInputDTO userInputDTO) {
        UserEntity entity = userService.update(id, userInputDTO);
        UserDTO updateUser = userMapper.toDTO(entity);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getUserInfo() {
        Long userId = authService.getAuthenticatedUserId();
        UserEntity entity = userService.getById(userId);
        return ResponseEntity.ok(userMapper.toDTO(entity));
    }
}
