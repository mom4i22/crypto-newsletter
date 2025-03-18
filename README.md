# Getting Started

### How to run the application locally
Make sure the configured **server.port** property in application.properties
is not being used by any other resource on your machine.

There are 2 fields in application.properties:

**spring.mail.username**

**spring.mail.password**

Both the username and password need to be valid existing credentials 
because they are used as the sender's info.

**Example:**

spring.mail.username=some_email@gmail.com
spring.mail.password=your_real_password
