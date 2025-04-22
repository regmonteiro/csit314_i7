Requirements:
- Java 17 or above
- Maven (https://maven.apache.org/download.cgi)
- XAMPP (https://www.apachefriends.org/index.html)
- git bash (https://git-scm.com/downloads)
-----------------------------------------------------------
Database Setup
1. Launch XAMPP and start MySQL
3. Open phpMyAdmin http://localhost/phpmyadmin/
4. Create a database named "i7_db"
5. Click the Import tab
6. Choose the file: "i7_db_user_accounts.sql"
7. Click "Go"
-----------------------------------------------------------
Application Setup
1. If application.properties does not exist in the resources folder, create application.properties file in the resources folder
2. Paste this code inside the file: 
		spring.datasource.url=jdbc:mysql://localhost:3306/i7_db
		spring.datasource.username=root
		spring.datasource.password=1234
		spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
		spring.jpa.hibernate.ddl-auto=none
3. Gop to C:\xampp\phpMyAdmin\config.inc.php
4. Make sure the passwords are the same in both config.inc.php and application.properties
-----------------------------------------------------------
Running the Application in localhost
1. Open Git Bash inside project root folder
2. Run this command to start application:
	 ./mvnw spring-boot:run
3. Once it builds successfully, open browser and go to:
	 http://localhost:8080/
