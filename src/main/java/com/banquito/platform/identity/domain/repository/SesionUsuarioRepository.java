package com.banquito.platform.identity.domain.repository;

import com.banquito.platform.identity.domain.enums.EstadoSesionEnum;
import com.banquito.platform.identity.domain.model.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Long> {
    Optional<SesionUsuario> findByUuidSesion(String uuidSesion);
    Optional<SesionUsuario> findByAccessTokenJti(String accessTokenJti);
    Optional<SesionUsuario> findByRefreshTokenHashAndEstado(String refreshTokenHash, EstadoSesionEnum estado);
}
