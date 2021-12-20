package android.julianmf.studentTermTracker.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "terms_table")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termName;
    private String startDate;
    private String endDate;

    public Term(int termId, String termName, String startDate, String endDate) {
        this.termId = termId;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString(){
        return termName;
    }
}
