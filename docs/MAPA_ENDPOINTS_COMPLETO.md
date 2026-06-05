# Mapa completo de endpoints REST/gRPC previstos para Banco BanQuito V2

Este mapa consolida los endpoints necesarios para Core, plataforma transversal y conexión con Switch. No todos se implementan en este primer microservicio; sirven como guía para el desarrollo completo.

## Identity Access Service

### REST

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `POST /api/v1/auth/introspect`
- `POST /api/v1/auth/client-token`
- `POST /api/v1/iam/users`
- `GET /api/v1/iam/users/{userUuid}`
- `PATCH /api/v1/iam/users/{userUuid}/status`
- `POST /api/v1/iam/users/{userUuid}/roles`
- `DELETE /api/v1/iam/users/{userUuid}/roles/{roleCode}`
- `POST /api/v1/iam/roles`
- `POST /api/v1/iam/permissions`
- `POST /api/v1/iam/roles/{roleCode}/permissions`
- `DELETE /api/v1/iam/roles/{roleCode}/permissions/{permissionCode}`
- `POST /api/v1/iam/api-clients`
- `POST /api/v1/iam/api-clients/{clientId}/scopes`
- `POST /api/v1/iam/sessions/{sessionUuid}/revoke`

### gRPC

- `IntrospectToken`
- `AuthorizeScope`
- `ValidateServiceClient`

## Core Customer Service

### REST

- `POST /api/v1/customers/natural-persons`
- `POST /api/v1/customers/legal-persons`
- `GET /api/v1/customers/{customerUuid}`
- `GET /api/v1/customers/by-identification/{identification}`
- `PATCH /api/v1/customers/{customerUuid}`
- `PATCH /api/v1/customers/{customerUuid}/status`
- `PATCH /api/v1/customers/{customerUuid}/mass-payments-status`
- `POST /api/v1/customers/{customerUuid}/relationships`
- `GET /api/v1/customers/{customerUuid}/relationships`

### gRPC

- `GetCustomerByUuid`
- `GetCustomerBasicByIdentification`
- `ValidateCustomerStatus`
- `ValidateMassPaymentsEnabled`
- `GetCustomerRelationships`

## Core Admin Service

### REST

- `GET /api/v1/admin/branches`
- `POST /api/v1/admin/branches`
- `PATCH /api/v1/admin/branches/{branchCode}`
- `GET /api/v1/admin/parameters/{code}`
- `POST /api/v1/admin/parameters`
- `PATCH /api/v1/admin/parameters/{code}`
- `GET /api/v1/admin/holidays`
- `POST /api/v1/admin/holidays`
- `PATCH /api/v1/admin/holidays/{date}`
- `GET /api/v1/admin/operational-windows`
- `POST /api/v1/admin/operational-windows`
- `PATCH /api/v1/admin/operational-windows/{code}`
- `GET /api/v1/admin/financial-institutions`
- `POST /api/v1/admin/financial-institutions`
- `PATCH /api/v1/admin/financial-institutions/{routingCode}`
- `GET /api/v1/admin/account-subtypes`
- `GET /api/v1/admin/transaction-subtypes`

### gRPC

- `GetParameterByCode`
- `GetIvaRate`
- `GetOperationalWindowByChannel`
- `GetAccountSubtype`
- `GetTransactionSubtype`
- `GetInstitutionByRoutingCode`

## Core Accounting Service

### REST

- `GET /api/v1/accounting/chart-of-accounts`
- `GET /api/v1/accounting/journal-entries/{journalEntryUuid}`
- `GET /api/v1/accounting/accounting-dates/current`
- `POST /api/v1/accounting/accounting-dates/{date}/open`
- `POST /api/v1/accounting/accounting-dates/{date}/close`
- `POST /api/v1/accounting/eod/run`
- `GET /api/v1/accounting/eod/{eodUuid}`
- `GET /api/v1/accounting/eod/{eodUuid}/status`
- `GET /api/v1/accounting/trial-balances/{accountingDate}`

### gRPC

- `CreateJournalEntry`
- `CreateReversalJournalEntry`
- `ValidateJournalEntry`
- `GetCurrentAccountingDate`
- `GetInstitutionalAccountByFunctionalCode`
- `RunEod`
- `GetTrialBalance`

## Core Account Service

### REST

- `POST /api/v1/accounts`
- `GET /api/v1/accounts/{accountNumber}`
- `GET /api/v1/accounts/{accountNumber}/owner`
- `GET /api/v1/accounts/{accountNumber}/balance`
- `GET /api/v1/accounts/{accountNumber}/transactions`
- `PATCH /api/v1/accounts/{accountNumber}/status`
- `PATCH /api/v1/accounts/{accountNumber}/payment-settings`
- `POST /api/v1/accounts/{accountNumber}/blocks`
- `PATCH /api/v1/accounts/{accountNumber}/blocks/{blockUuid}/release`
- `POST /api/v1/teller/deposits`
- `POST /api/v1/teller/withdrawals`
- `POST /api/v1/accounts/transfers/p2p`
- `POST /api/v1/accounts/transactions/{transactionUuid}/reverse`
- `POST /api/v1/switch-core/payment-reservations`
- `POST /api/v1/switch-core/payment-reservations/{reservationUuid}/consume`
- `POST /api/v1/switch-core/payment-reservations/{reservationUuid}/release`
- `POST /api/v1/switch-core/payment-reservations/{reservationUuid}/reverse`
- `POST /api/v1/switch-core/payment-reservations/{reservationUuid}/service-fee-charge`
- `POST /api/v1/switch-core/payment-reservations/{reservationUuid}/close`

### gRPC

- `ValidateAccount`
- `GetAccountBalance`
- `GetAccountOwner`
- `ValidateAccountOwnership`
- `SetFavoritePaymentAccount`
- `CreatePaymentReservation`
- `ConsumePaymentReservation`
- `ReleasePaymentReservation`
- `ReversePaymentReservation`
- `ClosePaymentReservation`
- `ChargeServiceFee`

## Document Service

### REST

- `POST /api/v1/documents`
- `GET /api/v1/documents/{documentUuid}`
- `GET /api/v1/documents/{documentUuid}/download`
- `GET /api/v1/documents?context={context}&type={type}`

### gRPC

- `RegisterDocument`
- `RegisterDocumentVersion`
- `AttachDocumentToBusinessReference`
- `GetDocumentMetadata`

## Notification Service

### REST

- `POST /api/v1/notifications/templates`
- `GET /api/v1/notifications/templates`
- `PATCH /api/v1/notifications/templates/{templateCode}`
- `POST /api/v1/notifications/requests`
- `GET /api/v1/notifications/{notificationUuid}`
- `GET /api/v1/notifications/requests/{notificationUuid}/attempts`
- `POST /api/v1/notifications/test`

### gRPC

- `SendNotification`
- `SendTemplatedNotification`
- `RegisterNotificationEvent`
- `GetNotificationStatus`
