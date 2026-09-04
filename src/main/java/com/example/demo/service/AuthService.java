package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.exception.CredencialesInvalidasException;
import com.example.demo.exception.DemasiadosIntentosException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.request.CambiarPasswordRequest;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.LoginAttemptService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String MENSAJE_ERROR_LOGIN = "Usuario o contraseña incorrectos";
    private static final String MENSAJE_BLOQUEO =
            "Demasiados intentos fallidos. Probá de nuevo en unos minutos.";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.username();

        // Si ya se pasó de intentos fallidos, ni siquiera miramos la
        // contraseña: cortamos acá para no seguir dándole feedback a
        // quien esté probando contraseñas por fuerza bruta.
        if (loginAttemptService.estaBloqueado(username)) {
            throw new DemasiadosIntentosException(MENSAJE_BLOQUEO);
        }

        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            loginAttemptService.registrarFallo(username);
            throw new CredencialesInvalidasException(MENSAJE_ERROR_LOGIN);
        }

        loginAttemptService.registrarExito(username);
        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol().name());
        return new LoginResponse(token, usuario.getUsername(), usuario.getRol().name());
    }

    // El usuario ya está autenticado (llegó con un JWT válido) y cambia
    // su PROPIA contraseña. Igual pedimos la contraseña actual para
    // confirmar que es realmente él quien la está cambiando (por si el
    // token quedó abierto en otra máquina, por ejemplo).
    public void cambiarPassword(String username, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        if (!passwordEncoder.matches(request.passwordActual(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.passwordNueva()));
        usuarioRepository.save(usuario);
    }
}
