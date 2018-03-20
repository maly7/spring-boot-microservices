ALTER TABLE user_account
  ADD CONSTRAINT UC_email UNIQUE (email);
