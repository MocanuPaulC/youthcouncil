INSERT INTO users (email, first_name, last_name, password, postcode, user_role, username)
VALUES ('admin@localhost', 'admin', 'admin', '$2a$10$2n9H5HUuGpJLL9H0dauGNeb7Al4qsJD3iA2Ua7P81M9xsbP9s/BBW', '12345',
        'GENERAL_ADMIN', 'admin');

INSERT INTO youthcouncil (council_name, municipality_name, is_after_election)
VALUES ('Youth Council of Antwerp', 'Antwerp', false);
