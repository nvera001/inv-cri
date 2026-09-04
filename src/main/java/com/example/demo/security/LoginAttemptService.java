package com.example.demo.security;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Protección simple contra fuerza bruta en el login: cuenta intentos
 * fallidos por username en memoria y bloquea temporalmente al que se
 * pasa de MAX_INTENTOS. Vive solo en memoria (se resetea si se reinicia
 * la app) y no es apta para múltiples instancias del server, pero
 * alcanza y sobra para el tamaño de este proyecto.
 */
@Component
public class LoginAttemptService {

    private static final int MAX_INTENTOS = 5;
    private static final long BLOQUEO_MINUTOS = 15;

    private static class Intento {
        int fallos = 0;
        Instant bloqueadoHasta = null;
    }

    private final ConcurrentHashMap<String, Intento> intentosPorUsuario = new ConcurrentHashMap<>();

    public boolean estaBloqueado(String username) {
        Intento intento = intentosPorUsuario.get(clave(username));
        if (intento == null || intento.bloqueadoHasta == null) {
            return false;
        }
        if (Instant.now().isAfter(intento.bloqueadoHasta)) {
            // Ya pasó el tiempo de bloqueo: reseteamos para que arranque de cero.
            intentosPorUsuario.remove(clave(username));
            return false;
        }
        return true;
    }

    public void registrarFallo(String username) {
        Intento intento = intentosPorUsuario.computeIfAbsent(clave(username), k -> new Intento());
        intento.fallos++;
        if (intento.fallos >= MAX_INTENTOS) {
            intento.bloqueadoHasta = Instant.now().plusSeconds(BLOQUEO_MINUTOS * 60);
        }
    }

    public void registrarExito(String username) {
        intentosPorUsuario.remove(clave(username));
    }

    private String clave(String username) {
        return username == null ? "" : username.toLowerCase();
    }
}
