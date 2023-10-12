# Staff management

<!-- TOC -->
* [Staff management](#staff-management)
  * [Packaging as jar](#packaging-as-jar)
  * [Using Docker](#using-docker)
  * [Что сделано](#что-сделано)
    * [Data model](#data-model)
  * [Не сделано](#не-сделано)
    * [Pageable Employees UI](#pageable-employees-ui)
    * [Projects management](#projects-management)
    * [Security](#security)
<!-- TOC -->

## Packaging as jar

To build the final jar of the books application, run:

```
mvn clean package
```

## Using Docker

To start the books application with postgresql database in a docker container, run:

```
docker compose up -d
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

To stop it, run:

```
docker compose stop
```

To stop it and remove the container, run:

```
docker compose down
```

Тo start a postgresql database only in a docker container, run:

```
docker compose -f postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker compose -f postgresql.yml down
```

## Что сделано

UI для управления сотрудниками: [http://localhost:8080](http://localhost:8080)

REST DATA API:  http://localhost:8080/api/profile
Для REST DATA сделан отдельный набор DTO в пакете `ru.otus.coursework.staff.repository.dto`.
UI через [HAL Explorer](http://localhost:8080/api)
Доступ только с ролью ADMIN. (не сделано)

### Data model

| Table       | Description                                                                                                                                                                                                                                                                                                                                      |
|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| DEPARTMENTS | Departments table that shows details of departments where employees work.<br/>Has foreign key `manager_id` to employees table.<br/>EMPLOYEES have a foreign key to this table.                                                                                                                                                                   |
| JOBS        | Hold the different names of job roles within the company.<br/>EMPLOYEES have a foreign key to this table.                                                                                                                                                                                                                                        |
| EMPLOYEES   | Еmployee personnel information for the company.<br/>Table has a self referencing foreign key `manager_id`; has same domain as manager_id in departments table.<br/>JOB_HISTORY have a foreign key to this table.                                                                                                                                 |
| JOB_HISTORY | Table that stores job history of the employees. If an employee changes departments within the job or changes jobs within the department, new rows get inserted into this table with old job information of the employee.<br/>Contains a complex unique key: `employee_id + start_date`. References with jobs, employees, and departments tables. |

## Не сделано

### Pageable Employees UI

### Projects management

Many-to-many relationship between Projects and Employees with different Roles.

### Security

| User       | Password | Role        |
|------------|:--------:|:------------|
| admin      | password | EDIT, ADMIN |
| user       | password | VIEW        |
| accountant | password | VIEW, FIN   |
| boss       | password | EDIT, FIN   |
| hr         | password | not enabled |

Hide/show salary information depending on FIN role.
