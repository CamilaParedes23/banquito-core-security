package com.banquito.platform.identity.application.service;

import com.banquito.platform.identity.api.dto.api.*;
import com.banquito.platform.identity.domain.enums.*;
import com.banquito.platform.identity.domain.model.*;
import com.banquito.platform.identity.domain.repository.*;
import com.banquito.platform.identity.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IamManagementService {
    private final UsuarioIdentidadRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final ApiClientRepository apiClientRepository;
    private final ApiClientScopeRepository apiClientScopeRepository;
    private final SesionUsuarioRepository sesionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionQueryService permissionQueryService;

    public IamManagementService(UsuarioIdentidadRepository usuarioRepository,
                                RolRepository rolRepository,
                                PermisoRepository permisoRepository,
                                UsuarioRolRepository usuarioRolRepository,
                                RolPermisoRepository rolPermisoRepository,
                                ApiClientRepository apiClientRepository,
                                ApiClientScopeRepository apiClientScopeRepository,
                                SesionUsuarioRepository sesionRepository,
                                PasswordEncoder passwordEncoder,
                                PermissionQueryService permissionQueryService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolPermisoRepository = rolPermisoRepository;
        this.apiClientRepository = apiClientRepository;
        this.apiClientScopeRepository = apiClientScopeRepository;
        this.sesionRepository = sesionRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionQueryService = permissionQueryService;
    }

    @Transactional
    public UserDetailResponse createUser(CreateUserRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BusinessException("IAM_USERNAME_EXISTS", "El nombre de usuario ya existe", HttpStatus.CONFLICT);
        }
        if (request.email() != null && usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("IAM_EMAIL_EXISTS", "El correo ya se encuentra registrado", HttpStatus.CONFLICT);
        }
        UsuarioIdentidad usuario = new UsuarioIdentidad();
        usuario.setUuidUsuario(UUID.randomUUID().toString());
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setTelefonoMovil(request.telefonoMovil());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setPasswordAlgorithm("BCrypt");
        usuario.setPasswordUpdatedAt(LocalDateTime.now());
        usuario.setRequiereCambioPassword(Boolean.TRUE.equals(request.requirePasswordChange()));
        usuario.setEmailVerificado(false);
        usuario.setTipoActor(TipoActorEnum.valueOf(request.actorType()));
        usuario.setEstado(EstadoUsuarioIdentidadEnum.ACTIVO);
        usuario.setIntentosFallidos(0);
        usuario.setUuidReferenciaExterna(request.externalReferenceUuid());
        usuario.setReferenciaTipo(request.referenceType());
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuario.setVersion(0);
        usuarioRepository.save(usuario);
        return toUserDetail(usuario);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUser(String userUuid) {
        UsuarioIdentidad usuario = usuarioRepository.findByUuidUsuario(userUuid)
                .orElseThrow(() -> new BusinessException("IAM_USER_NOT_FOUND", "Usuario no encontrado", HttpStatus.NOT_FOUND));
        return toUserDetail(usuario);
    }

    @Transactional
    public UserDetailResponse changeUserStatus(String userUuid, ChangeUserStatusRequest request) {
        UsuarioIdentidad usuario = usuarioRepository.findByUuidUsuario(userUuid)
                .orElseThrow(() -> new BusinessException("IAM_USER_NOT_FOUND", "Usuario no encontrado", HttpStatus.NOT_FOUND));
        usuario.setEstado(EstadoUsuarioIdentidadEnum.valueOf(request.status()));
        usuarioRepository.save(usuario);
        return toUserDetail(usuario);
    }

    @Transactional
    public GenericResponse assignRole(String userUuid, AssignRoleRequest request) {
        UsuarioIdentidad usuario = usuarioRepository.findByUuidUsuario(userUuid)
                .orElseThrow(() -> new BusinessException("IAM_USER_NOT_FOUND", "Usuario no encontrado", HttpStatus.NOT_FOUND));
        Rol rol = rolRepository.findByCodigo(request.roleCode())
                .orElseThrow(() -> new BusinessException("IAM_ROLE_NOT_FOUND", "Rol no encontrado", HttpStatus.NOT_FOUND));
        usuarioRolRepository.findByUsuarioAndRolCodigo(usuario, request.roleCode()).ifPresentOrElse(ur -> {
            ur.setEstado(EstadoAsignacionRolEnum.ACTIVO);
            ur.setFechaRevocacion(null);
            usuarioRolRepository.save(ur);
        }, () -> usuarioRolRepository.save(UsuarioRol.crear(usuario, rol, request.assignedByUuid())));
        return new GenericResponse("OK", "Rol asignado correctamente");
    }

    @Transactional
    public GenericResponse revokeRole(String userUuid, String roleCode) {
        UsuarioIdentidad usuario = usuarioRepository.findByUuidUsuario(userUuid)
                .orElseThrow(() -> new BusinessException("IAM_USER_NOT_FOUND", "Usuario no encontrado", HttpStatus.NOT_FOUND));
        UsuarioRol usuarioRol = usuarioRolRepository.findByUsuarioAndRolCodigo(usuario, roleCode)
                .orElseThrow(() -> new BusinessException("IAM_ROLE_ASSIGNMENT_NOT_FOUND", "Asignación de rol no encontrada", HttpStatus.NOT_FOUND));
        usuarioRol.revocar();
        usuarioRolRepository.save(usuarioRol);
        return new GenericResponse("OK", "Rol revocado correctamente");
    }

    @Transactional
    public GenericResponse createRole(CreateRoleRequest request) {
        if (rolRepository.existsByCodigo(request.code())) {
            throw new BusinessException("IAM_ROLE_EXISTS", "El rol ya existe", HttpStatus.CONFLICT);
        }
        Rol rol = new Rol();
        rol.setCodigo(request.code());
        rol.setNombre(request.name());
        rol.setTipoRol(TipoRolEnum.valueOf(request.roleType()));
        rol.setDescripcion(request.description());
        rol.setEstado(EstadoGeneralEnum.ACTIVO);
        rol.setFechaCreacion(LocalDateTime.now());
        rol.setFechaActualizacion(LocalDateTime.now());
        rol.setVersion(0);
        rolRepository.save(rol);
        return new GenericResponse("OK", "Rol creado correctamente");
    }

    @Transactional
    public GenericResponse createPermission(CreatePermissionRequest request) {
        if (permisoRepository.existsByCodigo(request.code())) {
            throw new BusinessException("IAM_PERMISSION_EXISTS", "El permiso ya existe", HttpStatus.CONFLICT);
        }
        Permiso permiso = new Permiso();
        permiso.setCodigo(request.code());
        permiso.setNombre(request.name());
        permiso.setModulo(request.module());
        permiso.setAccion(request.action());
        permiso.setRecurso(request.resource());
        permiso.setEstado(EstadoGeneralEnum.ACTIVO);
        permiso.setFechaCreacion(LocalDateTime.now());
        permiso.setFechaActualizacion(LocalDateTime.now());
        permiso.setVersion(0);
        permisoRepository.save(permiso);
        return new GenericResponse("OK", "Permiso creado correctamente");
    }

    @Transactional
    public GenericResponse assignPermission(String roleCode, AssignPermissionRequest request) {
        Rol rol = rolRepository.findByCodigo(roleCode)
                .orElseThrow(() -> new BusinessException("IAM_ROLE_NOT_FOUND", "Rol no encontrado", HttpStatus.NOT_FOUND));
        Permiso permiso = permisoRepository.findByCodigo(request.permissionCode())
                .orElseThrow(() -> new BusinessException("IAM_PERMISSION_NOT_FOUND", "Permiso no encontrado", HttpStatus.NOT_FOUND));
        rolPermisoRepository.findByRolAndPermisoCodigo(rol, request.permissionCode()).ifPresentOrElse(rp -> {
            rp.setEstado(EstadoGeneralEnum.ACTIVO);
            rolPermisoRepository.save(rp);
        }, () -> rolPermisoRepository.save(RolPermiso.crear(rol, permiso)));
        return new GenericResponse("OK", "Permiso asignado correctamente");
    }

    @Transactional
    public GenericResponse revokePermission(String roleCode, String permissionCode) {
        Rol rol = rolRepository.findByCodigo(roleCode)
                .orElseThrow(() -> new BusinessException("IAM_ROLE_NOT_FOUND", "Rol no encontrado", HttpStatus.NOT_FOUND));
        RolPermiso rolPermiso = rolPermisoRepository.findByRolAndPermisoCodigo(rol, permissionCode)
                .orElseThrow(() -> new BusinessException("IAM_ROLE_PERMISSION_NOT_FOUND", "Relación rol-permiso no encontrada", HttpStatus.NOT_FOUND));
        rolPermiso.setEstado(EstadoGeneralEnum.INACTIVO);
        rolPermisoRepository.save(rolPermiso);
        return new GenericResponse("OK", "Permiso revocado correctamente");
    }

    @Transactional
    public ApiClientResponse createApiClient(CreateApiClientRequest request) {
        if (apiClientRepository.existsByClientId(request.clientId())) {
            throw new BusinessException("IAM_CLIENT_EXISTS", "El API client ya existe", HttpStatus.CONFLICT);
        }
        ApiClient client = new ApiClient();
        client.setClientId(request.clientId());
        client.setClientSecretHash(passwordEncoder.encode(request.clientSecret()));
        client.setNombre(request.name());
        client.setServicioOrigen(request.originService());
        client.setTipoCliente(TipoApiClientEnum.valueOf(request.clientType()));
        client.setEstado(EstadoApiClientEnum.ACTIVO);
        client.setFechaCreacion(LocalDateTime.now());
        client.setFechaActualizacion(LocalDateTime.now());
        client.setVersion(0);
        apiClientRepository.save(client);
        return toApiClientResponse(client);
    }

    @Transactional
    public ApiClientResponse assignClientScope(String clientId, AssignClientScopeRequest request) {
        ApiClient client = apiClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new BusinessException("IAM_CLIENT_NOT_FOUND", "API client no encontrado", HttpStatus.NOT_FOUND));
        apiClientScopeRepository.findByApiClientAndScope(client, request.scope()).ifPresentOrElse(scope -> {
            scope.setEstado(EstadoGeneralEnum.ACTIVO);
            scope.setDescripcion(request.description());
            apiClientScopeRepository.save(scope);
        }, () -> {
            ApiClientScope scope = new ApiClientScope();
            scope.setApiClient(client);
            scope.setScope(request.scope());
            scope.setDescripcion(request.description());
            scope.setEstado(EstadoGeneralEnum.ACTIVO);
            scope.setFechaCreacion(LocalDateTime.now());
            apiClientScopeRepository.save(scope);
        });
        return toApiClientResponse(client);
    }

    @Transactional
    public GenericResponse revokeSession(String sessionUuid) {
        SesionUsuario sesion = sesionRepository.findByUuidSesion(sessionUuid)
                .orElseThrow(() -> new BusinessException("IAM_SESSION_NOT_FOUND", "Sesión no encontrada", HttpStatus.NOT_FOUND));
        sesion.revocar("Revocación administrativa");
        sesionRepository.save(sesion);
        return new GenericResponse("OK", "Sesión revocada correctamente");
    }

    private UserDetailResponse toUserDetail(UsuarioIdentidad usuario) {
        return new UserDetailResponse(
                usuario.getUuidUsuario(), usuario.getUsername(), usuario.getEmail(),
                usuario.getTipoActor().getValue(), usuario.getEstado().getValue(),
                usuario.getUuidReferenciaExterna(), usuario.getReferenciaTipo(), usuario.getUltimoLogin(),
                permissionQueryService.rolesActivos(usuario), permissionQueryService.permisosActivos(usuario)
        );
    }

    private ApiClientResponse toApiClientResponse(ApiClient client) {
        List<String> scopes = apiClientScopeRepository.findByApiClientAndEstado(client, EstadoGeneralEnum.ACTIVO)
                .stream().map(ApiClientScope::getScope).toList();
        return new ApiClientResponse(client.getClientId(), client.getNombre(), client.getServicioOrigen(), client.getTipoCliente().getValue(), client.getEstado().getValue(), scopes);
    }
}
