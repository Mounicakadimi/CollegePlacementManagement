COLLEGE PLACEMENT MANAGEMENT SYSTEM

PROJECT OVERVIEW

A Java-based application for organizing college campus placement
activities. The project includes modules for student management, company
management, job drives, job applications, placement results, placement
statistics, and database connectivity.

FEATURES

1.  STUDENT MANAGEMENT

-   Add, view, search, update, and delete student records.
-   Store student details such as ID, name, branch, email, and CGPA.
-   Check eligibility based on CGPA requirements.

2.  COMPANY MANAGEMENT

-   Add, view, search, update, and delete company records.
-   Maintain company and recruitment information.

3.  JOB DRIVE MANAGEMENT

-   Add, view, search, update, and delete job drives.
-   Store job role, location, package, and minimum CGPA.
-   Find students who meet a job drive’s minimum CGPA requirement.

4.  JOB APPLICATION MANAGEMENT

-   Manage student applications for job drives.
-   Associate applications with students and jobs.

5.  PLACEMENT RESULTS

-   Manage application outcomes, such as selected, rejected, pending, or
    withdrawn, where implemented.

6.  PLACEMENT STATISTICS

-   Provide a foundation for summarizing placement and application
    information according to the implemented features.

7.  DATABASE CONNECTIVITY

-   Includes DBConnection.java and TestDatabaseConnection.java.
-   MySQL/JDBC features require the database, credentials, and JDBC
    driver to be configured correctly.

TECHNOLOGIES

-   Java
-   Object-Oriented Programming (OOP)
-   Collections such as ArrayList, where used
-   CRUD operations
-   Exception handling and input validation
-   JDBC and MySQL, if configured
-   Visual Studio Code
-   Git and GitHub

PROJECT STRUCTURE

CollegePlacementManagement/ Main.java Student.java StudentService.java
Company.java CompanyService.java Job.java JobService.java
JobApplication.java JobApplicationService.java
PlacementResultService.java PlacementStatisticsService.java
DBConnection.java TestDatabaseConnection.java lib/

The folder may contain additional files. Java .class files are compiled
outputs and usually do not need to be committed to GitHub.

REQUIREMENTS

-   Java Development Kit (JDK)
-   Visual Studio Code or another Java-compatible IDE
-   MySQL and a compatible JDBC driver, if database functionality is
    enabled

HOW TO RUN

1.  Clone or download the repository.
2.  Open the project folder in Visual Studio Code.
3.  If using MySQL, configure the database settings in DBConnection.java
    and ensure the JDBC driver is available.
4.  Compile and run the application with the required classpath and
    dependencies.

For a project without external dependencies, the general commands are:

    javac *.java
    java Main

If the project uses a JDBC driver or other libraries, configure the
classpath before compiling and running. On Windows, the classpath
separator is a semicolon.

PURPOSE

The goal of this project is to reduce manual effort in organizing
student records, company information, recruitment drives, eligibility
checks, applications, and placement outcomes.

POSSIBLE FUTURE ENHANCEMENTS

-   Web interface using HTML, CSS, JavaScript, and Bootstrap
-   Persistent database storage
-   Authentication and role-based access
-   Advanced search and filtering
-   Downloadable placement reports
-   Email notifications
-   Additional validation and security

AUTHOR

Developed as an educational Java project demonstrating a College
Placement Management System.

LICENSE

No license has been specified. Add a license if you intend to publish or
distribute this project under specific terms.
