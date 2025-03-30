package br.com.homebudget.auth;

import br.com.homebudget.auth.dto.LoginRequestDTO;
import br.com.homebudget.auth.dto.LoginResponseDTO;
import br.com.homebudget.shared.configuration.security.JwtService;
import br.com.homebudget.users.UserEntity;
import br.com.homebudget.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO authRequest) {

        UserEntity user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean senhaCorreta = passwordEncoder.matches(authRequest.password(), user.getPassword());

        if (!senhaCorreta) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(jwtToken));
    }

}
