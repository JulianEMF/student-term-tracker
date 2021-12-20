package android.julianmf.studentTermTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.Entities.Term;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To Terms List
        Button termsButton = findViewById(R.id.button_terms);
        termsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TermsList.class);
            startActivity(intent);
        });

        // To Courses List
        Button coursesButton = findViewById(R.id.button_courses);
        coursesButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CoursesList.class);
            startActivity(intent);
        });

        // To Assessments List
        Button assessmentsButton = findViewById(R.id.button_assessments);
        assessmentsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AssessmentsList.class);
            startActivity(intent);
        });
    }
}