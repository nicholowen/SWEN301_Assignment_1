package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails;
import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * A student managers providing basic CRUD operations for instances of Student, and a read operation for instances of Degree.
 * @author jens dietrich
 */
public class StudentManager {

    public static PreparedStatement studentStmt;
    public static PreparedStatement degreeStmt;
    public static PreparedStatement deleteStmt;
    public static PreparedStatement selectStudentIdsStmt;
    public static PreparedStatement selectDegreeIdsStmt;
    public static PreparedStatement updateStmt;
    public static PreparedStatement createStmt;

    //cache maps to store already read Student and Degree objects
    public static HashMap<String, Student> studentMap;
    public static HashMap<String, Degree> degreeMap;
    public static int nextid = 9999;

    public static String url = "jdbc:derby:memory:studentdb;create=true";

    public static String readStudents = "select * from students where id = ?";
    public static String readDegree = "select * from degrees where id = ?";
    public static String deleteStudent = "delete from students where id = ?";
    public static String selectStudent = "select id from students";
    public static String selectDegrees = "select id from degrees";
    public static String updateStudent = "update students set first_name = ?, name = ?, degree = ? where id = ?";
    public static String createStudent = "insert into students(id, first_name, name, degree) values (?, ?, ?, ?)";

    public StudentManager(){
    }


    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND THE APPLICATION CAN CONNECT TO IT WITH JDBC
    static {
        degreeMap = new HashMap<String, Degree>();
        studentMap = new HashMap<String, Student>();
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    // THE FOLLOWING METHODS MUST IMPLEMENTED :

    /**
     * Return a student instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id Student id
     * @return Student Object
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student readStudent(String id) throws NoSuchRecordException, SQLException {
        String st_id = null;
        String f_name = null;
        String s_name = null;
        String degree = null;

        if(studentMap.containsKey(id)){
            return studentMap.get(id);
        }

        try {
            Connection conn = DriverManager.getConnection(url);
            studentStmt = conn.prepareStatement(readStudents);
            studentStmt.setString(1, id);

            studentStmt.executeQuery();
            ResultSet results = studentStmt.getResultSet();

            if (results.next()) {

                st_id = results.getString(1);
                f_name = results.getString(2);
                s_name = results.getString(3);
                degree = results.getString(4);
            }
            results.close();
            conn.close();

            if(st_id != null){
                Student student = new Student(id, s_name, f_name, readDegree(degree));
                studentMap.put(id, student);
                return student;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        throw new NoSuchRecordException();


    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id Degree id
     * @return Degree object
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree readDegree(String id) throws NoSuchRecordException, SQLException {

        String name = null;

        if(degreeMap.containsKey(id)){
            return degreeMap.get(id);
        }

        try {
            Connection conn = DriverManager.getConnection(url);
            degreeStmt = conn.prepareStatement(readDegree);

            degreeStmt.setString(1, id);

            degreeStmt.executeQuery();
            ResultSet degreeResults = degreeStmt.getResultSet();

            if (degreeResults.next()) {
                name = degreeResults.getString(2);
            }

            degreeResults.close();
            conn.close();

            Degree degree = new Degree(id, name);
            degreeMap.put(id, degree);
            return degree;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NoSuchRecordException();

    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student Student Object
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_delete
     */
    public static void delete(Student student) throws NoSuchRecordException {
        int rowCount = 0;
        try {
            Connection conn = DriverManager.getConnection(url);
            deleteStmt = conn.prepareStatement(deleteStudent);

            deleteStmt.setString(1, student.getId());
            rowCount = deleteStmt.executeUpdate();
            studentMap.remove(student.getId());
            System.out.println("deleted row: " + rowCount);

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(rowCount == 0){
            throw new NoSuchRecordException();
        }

    }

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student Student Object
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_update (followed by optional numbers if multiple tests are used)
     */
    public static void update(Student student) throws NoSuchRecordException, SQLException {

        Student check = readStudent(student.getId());
        studentMap.remove(check.getId());

        try{
            Connection conn = DriverManager.getConnection(url);
            updateStmt = conn.prepareStatement(updateStudent);
            updateStmt.setString(1, student.getName());
            updateStmt.setString(2, student.getFirstName());
            updateStmt.setString(3, student.getDegree().getId());
            updateStmt.setString(4, student.getId());

            updateStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new NoSuchRecordException();

    }


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not been used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name - last name of the student
     * @param firstName - first name of the student
     * @param degree - a degree object
     * @return a freshly created student instance
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_createStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student createStudent(String name,String firstName,Degree degree) throws SQLException {

        Student student = null;
        String id;
        try {
            Connection conn = DriverManager.getConnection(url);
            createStmt = conn.prepareStatement(createStudent);
            nextid++;
            id = "id" + nextid;
            student = new Student(id, name, firstName, degree);
            System.out.println(id);
            createStmt.setString(1, id);
            createStmt.setString(2, firstName);
            createStmt.setString(3, name);
            createStmt.setString(4, degree.getId());

            createStmt.executeUpdate();

            conn.close();

            return student;

        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return Collection containing all student ids
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> getAllStudentIds() throws SQLException {

        Collection<String> ids = new ArrayList<>();

        try{
            Connection conn = DriverManager.getConnection(url);
            selectStudentIdsStmt = conn.prepareStatement(selectStudent);
            selectStudentIdsStmt.executeQuery();
            ResultSet s = selectStudentIdsStmt.getResultSet();

            while(s.next()){
                ids.add(s.getString(1));
            }
            s.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }

    /**
     * Get all degree ids currently being used in the database.
     * @return An iterable collection containing all degree ids
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllDegreeIds (followed by optional numbers if multiple tests are used)
     */
    public static Iterable<String> getAllDegreeIds() throws SQLException {
        ArrayList<String> ids = new ArrayList<>();

        try{
            Connection conn = DriverManager.getConnection(url);
            selectDegreeIdsStmt = conn.prepareStatement(selectDegrees);

            selectDegreeIdsStmt.executeQuery();
            ResultSet s = selectDegreeIdsStmt.getResultSet();

            while(s.next()){
                ids.add(s.getString(1));
            }
            s.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }


}
