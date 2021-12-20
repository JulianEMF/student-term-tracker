package android.julianmf.studentTermTracker.Database;

import android.app.Application;
import android.julianmf.studentTermTracker.DAO.AssessmentDAO;
import android.julianmf.studentTermTracker.DAO.CourseDAO;
import android.julianmf.studentTermTracker.DAO.TermDAO;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.Entities.Term;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private TermDAO mTermDao;
    private CourseDAO mCourseDao;
    private AssessmentDAO mAssessmentDao;
    private List<Term>mAllTerms;
    private List<Course>mAllCourses;
    private List<Assessment>mAllAssessments;
    private Term term;
    private int courseId;
    private int termId;
    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        TrackerDatabaseBuilder db = TrackerDatabaseBuilder.getDatabase(application);
        mTermDao = db.TermDAO();
        mCourseDao = db.CourseDAO();
        mAssessmentDao = db.AssessmentDAO();
    }

    // Terms
    // Get all Terms
    public List<Term>getAllTerms(){
        databaseExecutor.execute(()->{
            mAllTerms = mTermDao.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    // Insert Term
    public void insertTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDao.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Update Term
    public void updateTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDao.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Delete term
    public void deleteTerm(Term term){
        databaseExecutor.execute(()->{
            mTermDao.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Get Term by term ID
    public Term getTermById(int termId){
        databaseExecutor.execute(()->{
            term = mTermDao.getTermById(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return term;
    }

    // Get course ID querying by name
    public int getTermId(String associatedTerm){
        databaseExecutor.execute(()->{
            termId = mTermDao.getTermId(associatedTerm);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return termId;
    }


    // Courses
    // Get all courses
    public List<Course>getAllCourses(){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDao.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    // Get course ID
    public int getCourseId(String courseTitle){
        databaseExecutor.execute(()->{
            courseId = mCourseDao.getCourseId(courseTitle);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return courseId;
    }

    // Insert course
    public void insertCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDao.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Update course
    public void updateCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDao.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Delete course
    public void deleteCourse(Course course){
        databaseExecutor.execute(()->{
            mCourseDao.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Get courses by termID
    public List<Course>getAllCoursesById(int termId){
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDao.getAllCoursesById(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }


    // Assessment
    // Get all assessments
    public List<Assessment>getAllAssessments(){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDao.getAllAssessments();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    // Get assessments by ID
    public List<Assessment>getAllAssessmentsByID(int courseId){
        databaseExecutor.execute(()->{
            mAllAssessments = mAssessmentDao.getAllAssessmentsById(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    // Insert assessment
    public void insertAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDao.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Update assessment
    public void updateAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDao.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Delete assessment
    public void deleteAssessment(Assessment assessment){
        databaseExecutor.execute(()->{
            mAssessmentDao.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
