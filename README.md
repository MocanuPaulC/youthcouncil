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

| Username   | Password   | Role          |
|------------|------------|---------------|
| gadmin     | gadmin     | GENERAL_ADMIN |
| cadmin     | cadmin     | COUNCIL_ADMIN |
| moderator  | moderator  | MODERATOR     |
| member1    | member1    | MEMBER        |
| member2    | member2    | MEMBER        |
| moderator1 | moderator1 | MODERATOR     |

### How to run


## How to run the project
- Docker and java should be installed on the running system
- first step is to run  `npm install`

  From the project root, create the docker image. It can be created from the `Dockerfile`:

- For linux:

```bash
docker build -t "youthcouncil:Dockerfile" .
docker create --name youthcouncil -p 5420:5432 youthcouncil:Dockerfile
docker container start youthcouncil
docker container start youthcouncil
docker update --restart unless-stopped youthcouncil
```

- For windows:

```shell
docker update --restart unless-stopped youthcouncil
docker container start youthcouncil
docker container start youthcouncil
docker create --name youthcouncil -p 5430:5432 youthcouncil:Dockerfile
docker build -t "youthcouncil:Dockerfile" .
```

- test all at once: `./gradlew test`
- prod: `./gradlew bootRun --args='--spring.profiles.active=postgres'`

