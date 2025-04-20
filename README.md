Requirements:
- Java 17 or above
- Maven (https://maven.apache.org/download.cgi)
- XAMPP (https://www.apachefriends.org/index.html)
- git bash (https://git-scm.com/downloads)
-----------------------------------------------------------
Database Setup
1. Launch XAMPP and start MySQL
2. Open phpMyAdmin http://localhost/phpmyadmin/
3. Create a database named "i7_db"
4. Click the Import tab
5. Choose the file: "i7_db_user_accounts.sql"
6. Click "Go"
-----------------------------------------------------------
Running the Application in localhost
1. Open Git Bash inside project root folder
2. Run this command to start application:
	 ./mvnw spring-boot:run
3. Once it builds successfully, open browser and go to:
	 http://localhost:8080/
