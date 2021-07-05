INSERT INTO settings (id, theme_name) VALUES
  (1, 'default'), (2, 'cosmic');

INSERT INTO admin_account (id, first_name, last_name, age, login, email, password_hash, is_deleted, settings_id) VALUES
-- Hashed "!2e4S"
  (1, 'Admin', 'Admin', 25, 'testAdmin', 'admin@admin.admin', '$2a$10$S.w55797etFyQugmxjgHAe22yd6fLcVu2ErgcjjBTjNP2zCg2.cqW', false, 1),
-- Hashed "12345"
  (2, 'User1', 'User1', 20, 'testUser1', 'user@user.user', '$2a$10$wgEjvYPY9gnw4cN/Jl6wAePzLHA8A/V23DNuRXvEkJkYDvpxNWm2O', false, 2);

INSERT INTO image (user_id, image) VALUES
-- Bytes of the default picture
(1, null),
(2, null);

INSERT INTO role (id, name, is_default) VALUES
  (1, 'USER', 1), (2, 'ADMIN', 0);

INSERT INTO administrator_roles (administrator_id, role_id) VALUES
  (1, 2), (2, 1);



