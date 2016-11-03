# University information system #

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



