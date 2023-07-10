# Core Banking Service

This service is responsible for creating bank accounts with allowed currencies (EUR,SEK,GBP,USD) and their balances.
User can make a transaction for deposit or withdraw (Direction **IN** or **OUT**) and gather those transactions with account id. 
User also can gather account info with account id. While creating account, must be aware of that customer id and account id is unique
per customer.

Service is using Postgres as database and RabbitMq as queue. Every account events will be fired to queue named **bank-queue**.

User can dynamically change allowed currencies (defined in _.properties_ files).

## How to deploy

```bash
docker compose up
```

### OpenApi page

Go to [OpenAPI](http://localhost:8085/api-docs)