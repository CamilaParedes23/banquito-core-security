package com.banquito.platform.identity.api.dto.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String username,
        @Email String email,
        String telefonoMovil,
        @NotBlank String password,
        @NotNull String actorType,
        String externalReferenceUuid,
        String referenceType,
        Boolean requirePasswordChange
) {}
