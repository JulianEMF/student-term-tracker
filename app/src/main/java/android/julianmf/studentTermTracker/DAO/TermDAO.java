package android.julianmf.studentTermTracker.DAO;

import android.julianmf.studentTermTracker.Entities.Term;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update (Term term);

    @Query("SELECT * FROM terms_table ORDER BY termId ASC")
    List<Term> getAllTerms();

    @Delete
    void delete (Term term);

    @Query("SELECT * FROM terms_table WHERE termID = :termId")
    Term getTermById(int termId);

    @Query("SELECT termId FROM terms_table WHERE termName = :associatedTerm")
    int getTermId(String associatedTerm);
}
