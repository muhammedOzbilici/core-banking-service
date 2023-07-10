CREATE TABLE IF NOT EXISTS accounts
(
    id          SERIAL      NOT NULL,
    customer_id VARCHAR(80) NOT NULL,
    country     VARCHAR(120),
    version     INT  DEFAULT (0),
    updated_date   TIMESTAMP   NOT NULL DEFAULT now(),
    updated_date   TIMESTAMP   NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TYPE valid_currencies AS ENUM ('EUR','SEK','GBP','USD');
CREATE TYPE valid_directions AS INT (1,2);

CREATE TABLE IF NOT EXISTS transactions
(
    id          SERIAL           NOT NULL,
    account_id  INT              NOT NULL,
    amount      NUMERIC          NOT NULL DEFAULT 0,
    currency    valid_currencies NOT NULL,
    direction   valid_directions NOT NULL,
    description TEXT             NOT NULL,
    created_date   TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE IF NOT EXISTS balances
(
    id         SERIAL           NOT NULL,
    currency   valid_currencies NOT NULL,
    amount     NUMERIC          NOT NULL DEFAULT 0,
    account_id INT              NOT NULL,
    created_date  TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)

);