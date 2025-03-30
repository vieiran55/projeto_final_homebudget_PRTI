package br.com.homebudget.shared.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            // 🔥 Log do token recebido
            System.out.println("Token recebido: " + token);

            String username = jwtService.extractUsername(token);

            // 🔥 Log do usuário extraído do token
            System.out.println("Usuário extraído do token: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 🔥 Log de verificação do usuário no banco
                System.out.println("Usuário carregado do banco: " + userDetails.getUsername());
                System.out.println("Permissões: " + userDetails.getAuthorities());

                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("Token inválido ou expirado para o usuário: " + username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
                    return;
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("Erro ao processar o token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro na autenticação do token.");
        }
    }

}
