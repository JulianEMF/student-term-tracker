package android.julianmf.studentTermTracker.Database;

import android.content.Context;
import android.julianmf.studentTermTracker.DAO.AssessmentDAO;
import android.julianmf.studentTermTracker.DAO.CourseDAO;
import android.julianmf.studentTermTracker.DAO.TermDAO;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.Entities.Term;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Term.class, Course.class, Assessment.class}, version=13,exportSchema = false)
public abstract class TrackerDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO TermDAO();
    public abstract CourseDAO CourseDAO();
    public abstract AssessmentDAO AssessmentDAO();
    private static volatile TrackerDatabaseBuilder INSTANCE;

    static TrackerDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized(TrackerDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TrackerDatabaseBuilder.class, "TrackerDatabase.db").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
