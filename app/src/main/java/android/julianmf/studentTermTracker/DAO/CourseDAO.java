package android.julianmf.studentTermTracker.DAO;

import android.julianmf.studentTermTracker.Entities.Course;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses_table ORDER BY courseID ASC")
    List<Course> getAllCourses();

    @Query("DELETE FROM courses_table")
    void deleteAllCourses();

    @Query("SELECT courseID FROM courses_table WHERE title = :courseTitle")
    int getCourseId(String courseTitle);

    @Query("SELECT * FROM courses_table WHERE termID = :termId")
    List<Course> getAllCoursesById(int termId);

}
