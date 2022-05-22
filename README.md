## Customer management written in Java (example of using JdbcTemplate & NamedParameterJdbcTemplate)

Methods:
- getting all customers from the database.
- getting a customer by id from the database.
- adding a new customer to the database.
- updating customer data by id in the database.
- deleting a customer by id. When a customer is deleted, it remains in the database with the "deleted" status and will not be taken into account when using any methods in the future.

The application can be run in two profiles: default and dev.

- default profile: 
  - Connection to a production database. Used JdbcTemplate. Messages to the console are output in the default format.

- dev profile: 
  - Connecting to the dev database. Used NamedParameterJdbcTemplate. Messages to the console are output in json format.

Creating a table in the database is written in the file table_in_database.txt

The microservice can be tested using the Postman application.
