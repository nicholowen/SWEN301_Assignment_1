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
            String deleteStudent = "delete from students where id = ?";
            String selectStudent = "select id from students";
            String selectDegrees = "select id from degrees";
            String updateStudent = "update students set first_name = ?, name = ?, degree = ? where id = ?";
            studentStmt = conn.prepareStatement(readStudents);
            degreeStmt = conn.prepareStatement(readDegree);
            deleteStmt = conn.prepareStatement(deleteStudent);
            selectStudentIdsStmt = conn.prepareStatement(selectStudent);
            selectDegreeIdsStmt = conn.prepareStatement(selectDegrees);
            updateStmt = conn.prepareStatement(updateStudent);


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

//        if(!studentMap.isEmpty()){
//            if(studentMap.containsKey(id)){
//                return studentMap.get(id);
//            }
//        }

        String st_id = null;
        String f_name = null;
        String s_name = null;
        String degree = null;

        try {
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
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree readDegree(String id) throws NoSuchRecordException {

//        if(!degreeMap.isEmpty()){
//            if(degreeMap.get(id) != null){
//                return degreeMap.get(id);
//            }
//        }

        String name = null;

        try {

            degreeStmt.setString(1, id);

            degreeStmt.executeQuery();
            ResultSet degreeResults = degreeStmt.getResultSet();


            if (degreeResults.next()) {
                name = degreeResults.getString(2);
            }

            degreeResults.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Degree degree = new Degree(id, name);
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
    public static void delete(Student student) throws NoSuchRecordException {
        try {
            deleteStmt.setString(1, student.getId());
            int rowCount = deleteStmt.executeUpdate();
            studentMap.remove(student.getId());
            System.out.println("deleted row: " + rowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
    public static void update(Student student) throws NoSuchRecordException, SQLException {

        Student check = readStudent(student.getId());

        try{
            if(check != null){
                updateStmt.setString(1, student.getName());
                System.out.println("setting last name - " + student.getName());
                updateStmt.setString(2, student.getFirstName());
                System.out.println("setting first name - " + student.getFirstName());
                updateStmt.setString(3, student.getDegree().getId());
                System.out.println("setting degree - " + student.getDegree().getId());
                updateStmt.setString(4, student.getId());
                System.out.println("for id: " + student.getId());

                updateStmt.executeUpdate();
//                if(studentMap.containsKey(student.getId())){
//                    studentMap.
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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

       //get last row, add 1 to degree number
        //create new student, then add to db

        return null;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> getAllStudentIds() {
        //to test, put them all in a hashset, then get the number of students current students and check vs set
        ArrayList<String> ids = new ArrayList<>();

        try{
            selectStudentIdsStmt.executeQuery();
            ResultSet s = selectStudentIdsStmt.getResultSet();

            int count = 0;
            while(s.next()){
                ids.add(s.getString(1));
                count++;
            }
            s.close();

//            System.out.println(count);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }

    /**
     * Get all degree ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllDegreeIds (followed by optional numbers if multiple tests are used)
     */
    public static Iterable<String> getAllDegreeIds() {
        //same as find all studentIDs
        ArrayList<String> ids = new ArrayList<>();

        try{
            selectDegreeIdsStmt.executeQuery();
            ResultSet s = selectDegreeIdsStmt.getResultSet();

            int count = 0;
            while(s.next()){
                ids.add(s.getString(1));
                count++;
            }
            s.close();

//            System.out.println(count);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }


}
