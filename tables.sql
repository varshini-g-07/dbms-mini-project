-- Table 1: Students (Strong Entity)
CREATE TABLE IF NOT EXISTS Students (
    Student_ID INT PRIMARY KEY AUTO_INCREMENT,
    First_Name VARCHAR(50) NOT NULL,
    Last_Name VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE,
	Password VARCHAR(50) NOT NULL
);

-- Table 2: Courses (Strong Entity)
CREATE TABLE IF NOT EXISTS Courses (
    Course_ID VARCHAR(10) PRIMARY KEY,
    Partner VARCHAR(100) NOT NULL,
    Course_Name VARCHAR(100) NOT NULL,
    Rating VARCHAR(5),
    Certificate_Type VARCHAR(100),
    Duration VARCHAR(100)
);

-- Table 3: Enrollments (Associative Entity/Linking Table)
CREATE TABLE IF NOT EXISTS Enrollments (
    Enrollment_ID INT PRIMARY KEY AUTO_INCREMENT,
    Student_ID INT NOT NULL,
    Course_ID VARCHAR(10) NOT NULL,
    Semester VARCHAR(10) NOT NULL,
    Year YEAR NOT NULL,
    Grade VARCHAR(5),
    Enrollment_Date DATE,
    
    -- Foreign Key Constraints
    FOREIGN KEY (Student_ID) REFERENCES Students(Student_ID),
    FOREIGN KEY (Course_ID) REFERENCES Courses(Course_ID)
);

CREATE TABLE IF NOT EXISTS Admins (
	Email VARCHAR(100) PRIMARY KEY,
    Password VARCHAR(50) NOT NULL
);
