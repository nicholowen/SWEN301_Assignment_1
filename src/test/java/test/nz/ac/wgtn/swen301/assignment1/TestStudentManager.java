package test.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

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
    public void test_readStudent1() throws Exception {
        Student student = new StudentManager().readStudent("id42");
        assertEquals("Sue", student.getFirstName());
    }

    @Test
    public void test_readStudent2() throws Exception {
        Student student = new StudentManager().readStudent("id6");
        assertEquals("Tom", student.getFirstName());
    }

    @Test
    public void test_readStudent3() throws Exception {
        Student student = StudentManager.readStudent("id7");
        assertEquals("Ramirez", student.getName());
    }

    // READ DEGREE

    @Test
    public void test_readDegree1() throws Exception {
        Degree degree = StudentManager.readDegree("deg4");
        assertEquals("BSc Mathematics", degree.getName());
    }

    @Test
    public void test_readDegree2() throws Exception {
        Degree degree = StudentManager.readDegree("deg9");
        assertEquals("BCom Marketing", degree.getName());
    }

    @Test
    public void test_readDegree3() throws Exception {
        new StudentManager();
        Student student = StudentManager.readStudent("id9");
        Degree degree = student.getDegree();
        assertEquals("BCom Marketing", degree.getName());
    }

    //TEST PERFORMANCE
    @Test
    public void testPerformance() throws  Exception {

        Random random = new Random();
        new StudentManager();

        long startTime = System.nanoTime();

        for(int i = 0; i < 1000; i++){
            int r = random.nextInt(10000);
            StudentManager.readStudent("id" + r);
        }

        double elapsed = ((double)System.nanoTime() - (double)startTime)/1000000000;
        System.out.println(elapsed);

    }

    //DELETE
    @Test
    public void deleteTest_1() throws Exception {

    }

}
