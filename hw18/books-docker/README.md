# Books. Docker version 
<!-- TOC -->
* [Packaging as jar](#packaging-as-jar)
* [Using Docker](#using-docker)
* [Users](#users)
<!-- TOC -->
## Packaging as jar
To build the final jar of the books application, run:
```
mvn clean package
```
## Using Docker
To start the books application with postgresql database in a docker container, run:
```
docker-compose up -d
```
Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

To stop it, run:
```
docker-compose stop
```
To stop it and remove the container, run:
```
docker-compose down
```

## Users
| User      | Password | Role          |
|-----------|:--------:|:--------------|
| admin     | password | EDITOR, ADMIN |
| adult     | password | USER          |
| child     | password | USER, CHILD   |
| librarian | password | EDITOR        |
| guest     | password | not enabled   |
