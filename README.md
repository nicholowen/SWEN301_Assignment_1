## SWEN301 Assignment 1 Template

a) How you checked the correctness of dependencies between the UI and the domain layer using the generated jdepend reports.

From the jdepend report, there are no dependencies listed in Cycles, therefore the UI and domain layers are running
independently from one another.

b) How to generate the standalone CLI application with mvn, and how to use
1. In terminal, change directory to Assignment1 folder
2. Command: 'mvn package'
3. Command: 'cd target'
4. Command: 'java -jar assignment1-1.0.0-shaded.jar id42' where id42 is the student id number
5. Appropriate student details will be listed in order: ID, First Name, Last Name, Degree (name)


c) Discuss (less than 100 words) whether your design is prone to memory leaks by interfering with Garbage Collection and how this has been or could be addressed.

My application design is prone to memory leaks as it is heavily reliant on static members. This is due to static members
are kept in memory for the lifetime of the application.
As the methods inside StudentManager.java are static, the fields in the class must be static therefore cannot be addressed.
I have addressed memory leak issue by closing the database connections which are open for every method call as these are
also kept in memory while the application is running.
