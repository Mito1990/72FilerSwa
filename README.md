A small file sharing application with user login that use JWT-Token for Authentication.
It's a simple application that can create, edit, delete text files and share them with other users.

Frontend: ReactJs
Backend:  Springboot
DB:       MySQL

The Project is dockerozed and can be pulled via this docker compose file.

docker-compose-file:
----------------------------------------------------------------------------------------------------------------------------

version: '3.8'

services:

  MySqlDB:
    image: mysql:latest
    container_name: MySqlDB
    restart: always
    environment:
      DATABASE_PORT: 3306
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: filesharing
      MYSQL_USER: user
      MYSQL_PASSWORD: root

  reactjs:
    image: mito1990/filesharingsystem:reactjs
    container_name: frontend
    build:
      context: ./Frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      - NODE_ENV=development

  springboot:
    image: mito1990/filesharingsystem:springboot
    container_name: backend
    build:
      context: ./Backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      - MySqlDB
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://MySqlDB:3306/filesharing
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: root

----------------------------------------------------------------------------------------------------------------------------
