# Secure Chat Application

A secure messaging application with encryption, user authentication, and a chat interface.

---

## Database Setup

### 1. Run the Database Schema
Execute the **SQL schema** to set up the database:

**File Location:**  
```
src/main/resources/schema.sql
```

### 2. Configure the Database
Edit the **database configuration file** to match your MySQL credentials:

**File Location:**  
```
src/main/resources/db_config.properties
```

**Modify the following fields:**
```properties
db.url=jdbc:mysql://localhost:3306/SecureChat
db.user=root
db.password=your_database_password  # Change this to your actual MySQL password
```

## Compile & Package the Application

Before running the application, compile the project:
1.	Navigate to the computer_security_project directory:
```
``cd computer_security_project``
```
2.	Clean and compile the project:
```
mvn clean compile
```
3.	Package the application:
```
mvn package
```

## Run the Application
### 1. Start the Chat Server

**Run the server using:**
```
mvn exec:java -Dexec.mainClass="com.computer_security_project.server.ChatServer"
```
### 2. Start the Chat Client

**Run the client using:**
```
mvn exec:java -Dexec.mainClass="com.computer_security_project.client.ChatClient"
```
You can open multiple clients to chat with different users

## Fix Port Errors

If you encounter port conflicts, ensure that all instances use the same port number.

**Files to Update:**
1. src/main/java/com/computer_security_project/client/ui/ChatUI.java
2. src/main/java/com/computer_security_project/server/ChatServer.java

**Modify the SERVER_PORT to the same value in all files:**
```
private static final int SERVER_PORT = 5050;  // Ensure this matches in all files
```
