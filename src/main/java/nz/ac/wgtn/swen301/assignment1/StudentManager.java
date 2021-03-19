package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails;
import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
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
    public static HashMap<String, Student> studentMap;
    public static HashMap<String, Degree> degreeMap;

    public StudentManager(){
        studentMap = new HashMap<String, Student>();
        degreeMap = new HashMap<String, Degree>();
        try{

            String url = "jdbc:derby:memory:studentdb;create=true";
            Connection conn = DriverManager.getConnection(url);
            String readStudents = "select * from students where id = ?";
            String readDegree = "select * from degrees where id = ?";
            studentStmt = conn.prepareStatement(readStudents);
            degreeStmt = conn.prepareStatement(readDegree);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND THE APPLICATION CAN CONNECT TO IT WITH JDBC
    static {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    // THE FOLLOWING METHODS MUST IMPLEMENTED :

    /**
     * Return a student instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student readStudent(String id) throws NoSuchRecordException {

        if(!studentMap.isEmpty()){
            if(studentMap.get(id) != null){
                return studentMap.get(id);
            }
        }

        String f_name = null;
        String s_name = null;
        String degree = null;

        try {
            studentStmt.setString(1, id);

            studentStmt.executeQuery();
            ResultSet results = studentStmt.getResultSet();

            if (results.next()) {
                f_name = results.getString(2);
                s_name = results.getString(3);
                degree = results.getString(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Student student = new Student(id, s_name, f_name, readDegree(degree));
        studentMap.put(id, student);

        return student;
    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree readDegree(String id) throws NoSuchRecordException {

        if(!degreeMap.isEmpty()){
            if(degreeMap.get(id) != null){
                return degreeMap.get(id);
            }
        }

        String name = null;

        try {

            degreeStmt.setString(1, id);

            degreeStmt.executeQuery();
            ResultSet degreeResults = degreeStmt.getResultSet();


            if (degreeResults.next()) {
                name = degreeResults.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Degree degree = new Degree(id, name);
//        degree.setName(name);
        degreeMap.put(id, degree);
        return degree;
    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_delete
     */
    public static void delete(Student student) throws NoSuchRecordException {}

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_update (followed by optional numbers if multiple tests are used)
     */
    public static void update(Student student) throws NoSuchRecordException {}


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not been used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name
     * @param firstName
     * @param degree
     * @return a freshly created student instance
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_createStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student createStudent(String name,String firstName,Degree degree) {
        return null;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> getAllStudentIds() {
        return null;
    }

    /**
     * Get all degree ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllDegreeIds (followed by optional numbers if multiple tests are used)
     */
    public static Iterable<String> getAllDegreeIds() {
        return null;
    }

    /**
     * Closes statements to release the lock on the table.
     * @throws SQLException
     */
    public static void close() throws SQLException {
        studentStmt.close();
        degreeStmt.close();
    }


}
