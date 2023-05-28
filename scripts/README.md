# Deployment

## Responsibilities

- Creating CI/CD: Peter Buschenreiter
- Reviewing CI/CD: Paul Mocanu
- Reviewing documentation: Thomas Ellmenreich

## Gitlab CI

### Stages

- build
- test
- deploy
- destroy

### Jobs

#### Build

##### generate_certs

Generate a self-signed certificate for the domain name of the application using keytool, moving the resulting
keystore.p12 file into `src/main/resources/keystore` and then generating an artifact for that directory.
This job is only run on the main branch.

##### build

Build the application using gradle, generating an artifact for the resulting jar file.
This job is dependent on the `generate_certs` job.
This job is only run on the main branch.

#### Test

##### test

Run `gradle check` to run all tests and generate a test report. Those test results are then published as an artifact.

##### semgrep-sast

By including a public gitlab ci file made available by gitlab, we can run static application security testing.

#### Deploy

##### deploy

Using the `google/cloud-sdk` docker image for this job, we don't have to install gcloud ourselves.
This job is dependent on the `build` and `test` job.
This job is only run on the main branch.

```yaml
- gcloud auth activate-service-account --key-file "$GOOGLE_SERVICE_ACCOUNT_FILE"
- cat $GOOGLE_SERVICE_ACCOUNT_FILE > key.json
- cat $JAVA_GOOGLE_SERVICE_ACCOUNT_FILE > java-key.json
- cat $env > .env
```

We first authenticate with the service account file. Then we copy the service account file into the root directory of
the gitlab-runner, which we need in order to make operations on compute and sql instances. We also copy the service
account file for the java application, which we later pass into the application as an environment variable. Finally, we
copy the environment variables file into the root directory so that we can pass it into the application as well. All
those variables are GitLab CI variables.

Then the [`setup_instance`](setup_instance.sh) script is run. More detail on that script can be found in
the [scripts](#setupinstancesh) section. We also pass the postgres password and the duckdns token to the script.

##### deploy-app-engine

Not going into much detail here, but this job deploys the application to the app engine.
This job is dependent on the `build` and `test` job.
This job is only run on the main branch.
This is a manual job.

#### Destroy

##### destroy

This job is dependent on the `deploy` job.
This is a manual job.
It runs the [`destroy`](destroy.sh) script, which is detailed in the [scripts](#destroysh) section.
This job is only run on the main branch.

## Scripts

### setup_instance.sh

1. Define some variables that we will use later on in the script.
2. Authenticate with the service account file.
3. Set the current gcloud project to the project defined in `$project`. This is important because otherwise it would
   be a gitlab project.
4. Check if the sql instance exists and if not we create it. Then we check if it is running and if not we start
   it.
5. Check if the firewall rules for the `$tags` exist and if not we create them.
6. Check if the compute instance exists and if so, we destroy it. This is important in case changes are made to
   the startup script.
7. In the startup script, install `openjdk-17-jdk`, allow port 80, 443 and 22 for tcp and enable the firewall.
   Copy the contents of [`youthcouncil.service`](youthcouncil.service) into `/etc/systemd/system/youthcouncil.service`,
   reload the systemctl daemon and update duckdns with the ip of the compute instance. See more in
   the [service](#youthcouncilservice) section.
8. The next steps are most of the time not ready yet, so wait for them to be ready.
9. When the instance is ready to receive ssh connections, copy the service account file for the java application
   into the instance, the environment variables file as well as the build directory from the artifact from
   the [build](#build-1) job.
10. Update the sql instance's authorized networks to allow the compute instance to connect to it.
11. Finally, wait for Java and systemctl to be ready and start the `youthcouncil` service.

### destroy.sh

1. Define necessary variables.
2. Authenticate with the service account file.
3. Set the current gcloud project to the project defined in `$project`.
4. Delete the compute instance.
5. Stop the sql instance. (don't delete it because we want to keep the data, as well as the ip address)

## Service

### youthcouncil.service

1. Setting the description of the service.
2. Setting the user to run the service as root.
3. Setting the working directory to the `/root` directory.
4. Setting the command to run the application. (here it's important that the path to the executable is absolute to not
   be ambiguous)
5. Setting it to automatically restart the service if it fails.
6. Setting the EnvironmentFile to the environment variables file. (this path has to be absolute as well)
7. Setting an extra environment variable for the service account file for the java application. (this path has to be
   absolute as well)

## Service accounts

### GitLab CI

This service account is used by the gitlab-runner to authenticate with the Google Cloud Platform. It is used to create
and manage resources. It has the following roles:

- Compute Admin
- Cloud SQL Admin
- Owner

### Java application

This service account is used by the java application to authenticate with the Google Cloud Platform. It is used to read
and write to the Cloud SQL instance. It has the following roles:

- Cloud SQL Client
- Storage Object Viewer (buckets - in progress)
- Storage Object Creator (buckets - in progress)

## Application

Tying it all together is the [application-prod.yaml](../src/main/resources/application-prod.yaml) file. This file is
used to configure the application when it is deployed. Relevant properties are:

```yaml
spring:
    cloud:
        gcp:
            project-id: ${PROJECT_ID} # the project id
            sql:
                enabled: true
                instance-connection-name: ${SQL_INSTANCE_NAME} # format= project:region:sql-instance-name
                database-name: ${POSTGRES_DB} # the database name
                credentials:
                    location: file:${JAVA_GOOGLE_SERVICE_ACCOUNT_FILE} # the service account file for the java application
    jpa:
        database: postgresql
        hibernate:
            ddl-auto: update # update the database schema when changes are made
        defer-datasource-initialization: true
    datasource:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://34.76.189.78:5432/YouthCouncil # the ip address of the sql instance
        username: postgres
        password: ${POSTGRES_PASSWORD} # the password for the postgres user
    devtools:
        add-properties: false
server:
    port: 443 # the port to run the application on, default port for https
    servlet:
        context-path: /
    ssl:
        enabled: true # enable ssl, disables http
        key-alias: tomcat # the alias of the key in the keystore
        key-store: classpath:keystore/keystore.p12 # the keystore that was generated in the `generate_certs` job
        key-store-type: PKCS12 # the type of the keystore
        key-store-password: ${POSTGRES_PASSWORD} # the password of the keystore, for simplicity we use the postgres password
        key-password: ${POSTGRES_PASSWORD} # the password of the key, for simplicity we use the postgres password
```
