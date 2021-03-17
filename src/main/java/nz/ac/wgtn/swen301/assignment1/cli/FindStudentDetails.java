package nz.ac.wgtn.swen301.assignment1.cli;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FindStudentDetails {

    // THE FOLLOWING METHOD MUST IMPLEMENTED
    /**
     * Executable: the user will provide a student id as single argument, and if the details are found,
     * the respective details are printed to the console.
     * E.g. a user could invoke this by running "java -cp <someclasspath> nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails id42"
     * @param arg
     */

    public static Statement stmt = null;

    public static void main (String[] arg) throws SQLException, NoSuchRecordException {

//        try{
//
//            String url = "jdbc:derby:memory:studentdb;create=true";
//            Connection conn = DriverManager.getConnection(url);
//            System.out.println("got it");
//            stmt = conn.createStatement();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        StudentManager sm = new StudentManager();
        sm.readStudent("id42");
        sm.readDegree("id42");

    }
}
