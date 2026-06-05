# Estándar de codificación Banco BanQuito V2

## Paquetes

- Core: `com.banquito.core.<contexto>`
- Plataforma transversal: `com.banquito.platform.<contexto>`
- Switch: evitar `switch`; usar `com.banquito.payments` o `com.banquito.switching`

## Lombok

Permitido:

- `@Getter`
- `@Setter`
- `@RequiredArgsConstructor` en servicios/controladores si aplica
- `@Getter` en enums con valor interno

No permitido en entidades JPA:

- `@Data`
- `@EqualsAndHashCode`

## JPA

Obligatorio:

- `@Entity`
- `@Table(name = "...")`
- `@Column(name = "...", length = ..., nullable = ...)` en todos los atributos persistentes
- `@Id`
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` para PK autoincremental
- Constructores: vacío y por ID
- `equals()` y `hashCode()` manual por ID
- `toString()` manual y seguro

## Enums

- Toda columna con `CHECK` debe mapearse a enum Java.
- Usar `@Enumerated(EnumType.STRING)`.
- Sufijo al final: `EstadoCuentaEnum`, `TipoActorEnum`, etc.
- No reutilizar enums de ciclos de vida distintos.

## Relaciones

- Dentro del mismo microservicio: relaciones de hijo a padre, preferiblemente `ManyToOne(fetch = LAZY)`.
- No relaciones bidireccionales salvo caso justificado.
- Entre microservicios: no JPA ni FK; usar UUID, códigos y contratos REST/gRPC.

## Datos financieros

- Montos: `BigDecimal`, no `double` ni `float`.
- Fechas: `LocalDate`, `LocalDateTime`, `LocalTime`, no `@Temporal`.
