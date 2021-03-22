package test.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.*;


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
//        new StudentManager();
        Student student = new StudentManager().readStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }

    @Test
    public void test_readStudent1() throws Exception {
        new StudentManager();
        Student student = StudentManager.readStudent("id42");
        assertEquals("Sue", student.getFirstName());
    }

    @Test
    public void test_readStudent2() throws Exception {
//        new StudentManager();
        Student student = StudentManager.readStudent("id6");
        assertEquals("Tom", student.getFirstName());
    }

    @Test(expected = NoSuchRecordException.class)
    public void test_readStudent3() throws NoSuchRecordException {
        try{
            new StudentManager().readStudent("id93478293");
        } catch (Exception e) {
            System.out.println("Exception thrown: "+e);
            throw new NoSuchRecordException();
        }
    }

    // READ DEGREE

    @Test
    public void test_readDegree1() throws Exception {
        Degree degree = new StudentManager().readDegree("deg4");
        assertEquals("BSc Mathematics", degree.getName());
    }

    @Test
    public void test_readDegree2() throws Exception {
//        new StudentManager();
        Degree degree = new StudentManager().readDegree("deg9");
        assertEquals("BCom Marketing", degree.getName());
    }

    @Test
    public void test_readDegree3() throws Exception {
//        new StudentManager();
        Student student = new StudentManager().readStudent("id9");
        Degree degree = student.getDegree();
        assertEquals("BCom Marketing", degree.getName());
    }

    //TEST PERFORMANCE
    @Test
    public void testPerformance() throws  Exception {

        Random random = new Random();
//        new StudentManager();
//
        long startTime = System.nanoTime();

        for(int i = 0; i < 1000; i++){
            int r = random.nextInt(10000);
            StudentManager.readStudent("id" + r);
        }

        double elapsed = ((double)System.nanoTime() - (double)startTime)/1000000000;
        System.out.println("Time elapsed = " + elapsed + " seconds");
        assertTrue(elapsed <= 1.00);
    }

    //DELETE
    @Test
    public void deleteTest_1() throws Exception {
        new StudentManager();
        Student student1 = StudentManager.readStudent("id3");

        StudentManager.delete(student1);

        try {
            StudentManager.readStudent("id3");
        } catch (NoSuchRecordException e) {
            System.out.println("no such record");
        }

    }

    @Test
    public void test_getAllStudentIds() throws Exception {
        new StudentManager();
        Collection<String> ids = StudentManager.getAllStudentIds();
        assertTrue(ids.size() == 10000);
    }

    @Test
    public void test_getAllDegreeIds() throws Exception {
        new StudentManager();
        Iterable<String> ids = StudentManager.getAllDegreeIds();
        int counter = 0;
        for(String i : ids){
            counter++;
        }
        assertTrue(counter == 10);
    }

    @Test
    public void test_updateStudent() {
        new StudentManager();
        try {
            Student student = StudentManager.readStudent("id0");

            Student n_student = new Student("id0", "Jamie", "Lok", StudentManager.readDegree("deg7"));
            StudentManager.update(n_student);

            Student updated = StudentManager.readStudent("id0");
            if(student.getId().equals(updated.getId())){
                assertFalse(updated.getFirstName().equals(student.getFirstName()));
            }


        } catch (NoSuchRecordException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_createStudent() {
        String name = null;
        try {
            Degree degree = StudentManager.readDegree("deg0");

             StudentManager.createStudent("Chan", "Maggie", degree);
             name = StudentManager.readStudent("id10000").getFirstName();

        } catch (NoSuchRecordException | SQLException e) {
            e.printStackTrace();
        }

        assertTrue(name.equals("Maggie"));
    }



}
