CREATE TABLE user_account (
  id           VARCHAR(255) NOT NULL,
  password     VARCHAR(255) NOT NULL,
  email        VARCHAR(255) NOT NULL,
  created_date DATETIME     NOT NULL,
  verified     BOOLEAN      NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id)
);
