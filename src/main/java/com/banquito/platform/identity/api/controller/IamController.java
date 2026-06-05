package com.banquito.platform.identity.api.controller;

import com.banquito.platform.identity.api.dto.api.*;
import com.banquito.platform.identity.application.service.IamManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/iam")
public class IamController {
    private final IamManagementService iamService;

    public IamController(IamManagementService iamService) {
        this.iamService = iamService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public UserDetailResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return iamService.createUser(request);
    }

    @GetMapping("/users/{userUuid}")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or authentication.name == #userUuid")
    public UserDetailResponse getUser(@PathVariable String userUuid) {
        return iamService.getUser(userUuid);
    }

    @PatchMapping("/users/{userUuid}/status")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public UserDetailResponse changeUserStatus(@PathVariable String userUuid, @Valid @RequestBody ChangeUserStatusRequest request) {
        return iamService.changeUserStatus(userUuid, request);
    }

    @PostMapping("/users/{userUuid}/roles")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse assignRole(@PathVariable String userUuid, @Valid @RequestBody AssignRoleRequest request) {
        return iamService.assignRole(userUuid, request);
    }

    @DeleteMapping("/users/{userUuid}/roles/{roleCode}")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse revokeRole(@PathVariable String userUuid, @PathVariable String roleCode) {
        return iamService.revokeRole(userUuid, roleCode);
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse createRole(@Valid @RequestBody CreateRoleRequest request) {
        return iamService.createRole(request);
    }

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        return iamService.createPermission(request);
    }

    @PostMapping("/roles/{roleCode}/permissions")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse assignPermission(@PathVariable String roleCode, @Valid @RequestBody AssignPermissionRequest request) {
        return iamService.assignPermission(roleCode, request);
    }

    @DeleteMapping("/roles/{roleCode}/permissions/{permissionCode}")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse revokePermission(@PathVariable String roleCode, @PathVariable String permissionCode) {
        return iamService.revokePermission(roleCode, permissionCode);
    }

    @PostMapping("/api-clients")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public ApiClientResponse createApiClient(@Valid @RequestBody CreateApiClientRequest request) {
        return iamService.createApiClient(request);
    }

    @PostMapping("/api-clients/{clientId}/scopes")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public ApiClientResponse assignClientScope(@PathVariable String clientId, @Valid @RequestBody AssignClientScopeRequest request) {
        return iamService.assignClientScope(clientId, request);
    }

    @PostMapping("/sessions/{sessionUuid}/revoke")
    @PreAuthorize("hasAuthority('SCOPE_auth.user.manage') or hasRole('ADMIN_SEGURIDAD')")
    public GenericResponse revokeSession(@PathVariable String sessionUuid) {
        return iamService.revokeSession(sessionUuid);
    }
}
