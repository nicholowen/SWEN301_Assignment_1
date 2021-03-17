package test.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TestStudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND IN ITS INITIAL STATE BEFORE EACH TEST RUNS
    @Before
    public  void init () {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    @Test
    public void dummyTest() throws Exception {
        Student student = new StudentManager().readStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }

    @Test
    public void readStudentTest_1() throws Exception {
        Student student = new StudentManager().readStudent("id42");
        assertEquals("Sue", student.getFirstName());
    }

    @Test
    public void readStudentTest_2() throws Exception {
        Student student = new StudentManager().readStudent("id6");
        assertEquals("Tom", student.getFirstName());
    }

    @Test
    public void readStudentTest_3() throws Exception {
        Student student = new StudentManager().readStudent("id7");
        assertEquals("Ramirez", student.getName());
    }

    // READ DEGREE

    @Test
    public void readDegreeTest_1() throws Exception {
        Degree degree = new StudentManager().readDegree("deg4");
        assertEquals("BSc Mathematics", degree.getName());
    }

    @Test
    public void readDegreeTest_2() throws Exception {
        Degree degree = new StudentManager().readDegree("deg9");
        assertEquals("BCom Marketing", degree.getName());
    }

}
