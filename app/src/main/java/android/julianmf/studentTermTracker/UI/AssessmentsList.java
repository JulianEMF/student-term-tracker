package android.julianmf.studentTermTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

public class AssessmentsList extends AppCompatActivity {

    private Repository repository;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());
        List<Assessment> allAssessments = repository.getAllAssessments();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAssessments);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(allAssessments);

        FloatingActionButton addAssessment = findViewById(R.id.fab_add_assessment);
        addAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentsList.this, AssessmentDetails.class);
            startActivity(intent);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.refresh:
                repository = new Repository(getApplication());
                List<Assessment> allAssessments = repository.getAllAssessments();
                final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
                RecyclerView recyclerView = findViewById(R.id.recyclerViewAssessments);
                recyclerView.setAdapter(assessmentAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                assessmentAdapter.setAssessments(allAssessments);
        }
        return super.onOptionsItemSelected(item);
    }
}