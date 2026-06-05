package com.banquito.platform.identity.domain.repository;

import com.banquito.platform.identity.domain.enums.EstadoUsuarioIdentidadEnum;
import com.banquito.platform.identity.domain.model.UsuarioIdentidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioIdentidadRepository extends JpaRepository<UsuarioIdentidad, Long> {
    Optional<UsuarioIdentidad> findByUsername(String username);
    Optional<UsuarioIdentidad> findByUuidUsuario(String uuidUsuario);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByEstado(EstadoUsuarioIdentidadEnum estado);
}
