# Cambios aplicados en revisión de Identity Access

## Cambios técnicos

- Parametrización completa de `application.yml` y `application-docker.yml` mediante variables de entorno.
- Dockerfile cloud-ready con Java 21, usuario no root, `JAVA_OPTS`, perfil docker y healthcheck.
- `.env.example`, `.gitignore` y `.dockerignore` incorporados.
- Manejo homogéneo de errores:
  - `BusinessException` para errores de negocio.
  - `GlobalExceptionHandler` ampliado para JSON inválido, validaciones, integridad, recursos inexistentes y errores generales.
  - `RestAuthenticationEntryPoint` para 401 estructurado.
  - `RestAccessDeniedHandler` para 403 estructurado.
- Logout estricto con errores claros cuando no se envía token/sesión o cuando la sesión no existe/no está activa.
- Documentación de despliegue Docker/Cloud.
- Copia del modelo físico SQL en `docs/database/00_identity_access_db.sql`.

## Cobertura funcional actual

- Login, refresh, logout.
- Introspección de token.
- Token técnico para API clients.
- Gestión de usuarios, roles, permisos, scopes y sesiones.
- Auditoría de seguridad.
- Outbox de seguridad.
- Contrato gRPC base para introspección/autorización técnica.

## Pendientes posteriores recomendados

- Integrar recuperación de contraseña con `notification-service` y SMTP/Mailpit real.
- Integrar eventos de seguridad vía RabbitMQ: `identity.password.changed`, `identity.user.blocked`, `identity.login.suspicious`.
- Evaluar MFA funcional si el docente lo exige en frontend.
