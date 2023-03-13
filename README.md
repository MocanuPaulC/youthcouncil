# Youth council platform

### Team 4

#### Members

- Peter Buschenreiter
- Paul Mocanu
- Thomas Ellmenreich
- Samantha Ngong
- Joseph Quansah

### Profiles

- dev -> uses an h2 filebased database
- prod -> uses a postgres database

### Seeded users

| Username  | Password  | Role          |
|-----------|-----------|---------------|
| gadmin    | gadmin    | GENERAL_ADMIN |
| cadmin    | cadmin    | COUNCIL_ADMIN |
| moderator | moderator | MODERATOR     |
| member1   | member1   | MEMBER        |
| member2   | member2   | MEMBER        |

### How to run

- dev: `./gradlew bootRun --args='--spring.profiles.active=dev'`
- prod: `./gradlew bootRun --args='--spring.profiles.active=prod --POSTGRES_PASSWORD=yourpassword'` (
  replace `yourpassword` with the password you set for the postgres user)
