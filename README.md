# University information system #

This application was developed during the course Advanced Software Engineering (ASE) at the Vienna University of Technology (TU Wien) in the winter term 2016.

This tool is a university information system that assist students in selecting their courses in a smarter way.

## Main features ##
* Admins manage everything including users, study plans and subjects
* Students manage their courses and study plans
* Students can generate their certificates as a pdf
* Students get recommendations about courses
* Lecturers create courses and assign grades to students

## Some screenshots ##
![login view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/login.png)
![courses view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/courses.png)
![course-details view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/course-details.png)
![recommendations view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/recommendations.png)
![studyplans view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/studyplans.png)
![certificate view](https://github.com/university-information-system/uis/raw/master/assets/screenshots/certificate.png)

## Setup and running ##

### 1. Installing postgres and configuring it in spring (optional as an embedded db is used for the moment) ###
Make sure you have installed postgres, set a password and created a new database.
Then you can configure your spring boot application:

```
#configure postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/<your_db_name>
spring.datasource.username=<your_postgres_username>
spring.datasource.password=<your_postgres_password>
spring.datasource.driver-class-name=org.postgresql.Driver
#create tables if they don't exist
spring.jpa.hibernate.hbm2ddl.auto=create
```

### 2. Building the app ###

```
cd src/main/resources/static
npm install
./node_modules/.bin/webpack
cd -
mvn clean install
```

### 3. Running the app ###

```
mvn spring-boot:run
```

## Automatic building and reloading (IntelliJ IDEA) ##
To enable this, make sure you have enabled "Make project automatically" in your compiler settings.
Now type Shift+Command+A and enter "Registry" in the search box that appears. Enable "compiler.automake.allow.when.app.running".

Now every time you modify classes, html etc. you will see the changes immediately in your browser after doing a refresh.
To make your browser autorefresh every time you change smth in your code, you can install livereload.

For the frontend static content, use the webpack watcher to automatically recompile es6 scripts and scss files:
```
cd src/main/resources/static
./node_modules/.bin/webpack --watch
```



