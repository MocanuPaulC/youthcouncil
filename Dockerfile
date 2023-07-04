FROM postgres:15.2-alpine

ENV POSTGRES_PASSWORD=12345

# Allow connections from outside the container
RUN echo "host all all 127.0.0.1/32 md5" >> /usr/local/share/postgresql/pg_hba.conf
RUN echo "host all all ::1/128      md5" >> /usr/local/share/postgresql/pg_hba.conf
RUN echo "listen_addresses='*'" >> /usr/local/share/postgresql/postgresql.conf

# Create a file to set up the schema and user
RUN mkdir -p /docker-entryp.oint-initdb.d
RUN touch /docker-entrypoint-initdb.d/init.sql

# grant full access to 'multitenancy' database to user 'spring'
RUN echo "CREATE USER spring WITH PASSWORD 'spring';" >> /docker-entrypoint-initdb.d/init.sql
RUN echo "CREATE DATABASE YouthCouncil OWNER spring;" >> /docker-entrypoint-initdb.d/init.sql
RUN echo "GRANT ALL PRIVILEGES ON DATABASE YouthCouncil TO spring;" >> /docker-entrypoint-initdb.d/init.sql
