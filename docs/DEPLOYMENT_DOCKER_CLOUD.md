# Despliegue Docker/Cloud - identity-access-service

## Objetivo

El servicio debe funcionar tanto en local como en nube sin cambios manuales de código. Toda diferencia de entorno se controla por variables de entorno.

## Local

```env
SPRING_PROFILES_ACTIVE=local
IDENTITY_DB_URL=jdbc:mysql://localhost:33061/banquito_identity_access_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil
IDENTITY_DB_USER=root
IDENTITY_DB_PASSWORD=BanQuitoRoot2026!
JWT_SECRET=BanQuitoIdentityJwtSecretKeyForDevelopmentOnlyChangeMe2026
```

## Docker/Cloud

```env
SPRING_PROFILES_ACTIVE=docker
IDENTITY_DB_URL=jdbc:mysql://mysql-identity:3306/banquito_identity_access_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil
IDENTITY_DB_USER=root
IDENTITY_DB_PASSWORD=${MYSQL_ROOT_PASSWORD}
JWT_SECRET=${JWT_SECRET}
```

## Kong

Este servicio se expone por Kong mediante rutas REST/OpenAPI:

```text
/api/v1/auth/**
/api/v1/iam/**
```

Kong debe enrutar hacia:

```text
identity-access-service:8081
```

cuando todos los microservicios estén dockerizados.

## Seguridad

- `JWT_SECRET` debe ser igual en todos los microservicios que validen tokens.
- En nube, `JWT_SECRET`, passwords de base y credenciales SMTP/RabbitMQ no deben versionarse en GitHub.
- Usar `.env.example` como plantilla, no como archivo con secretos reales.
