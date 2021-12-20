package android.julianmf.studentTermTracker.DAO;

import android.julianmf.studentTermTracker.Entities.Assessment;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessments_table ORDER BY assessmentID ASC")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessments_table WHERE courseID = :courseID")
    List<Assessment> getAllAssessmentsById(int courseID);

}
