package com.example.demo.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Usuario;
import com.example.demo.model.enums.Rol;
import com.example.demo.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste("admin", "admin123", Rol.ADMIN);
        crearSiNoExiste("analista", "analista123", Rol.ANALISTA);
    }

    // A diferencia de la version anterior (que solo creaba usuarios si la
    // tabla estaba vacia), esta version chequea usuario por usuario. Asi,
    // como ya existe "admin" de la entrega pasada, no lo toca ni lo
    // duplica, pero SI crea "analista" que todavia no existe.
    private void crearSiNoExiste(String username, String passwordPlano, Rol rol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(passwordPlano));
            usuario.setRol(rol);
            usuarioRepository.save(usuario);
            System.out.println("Usuario '" + username + "' creado por defecto (rol " + rol + "). Cambiar la contraseña cuanto antes.");
        }
    }
}
