package android.julianmf.studentTermTracker.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

public class CoursesList extends AppCompatActivity {

    private Repository repository;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());
        List<Course> allCourses = repository.getAllCourses();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCourses);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(allCourses);

        FloatingActionButton addCourse = findViewById(R.id.fab_add_course);
        addCourse.setOnClickListener(view -> {
            onToAddCourse(view);
        });

    }

    // Redirects to add course activity
    public void onToAddCourse(View view) {
        Intent intent = new Intent(CoursesList.this, CourseDetails.class);
        startActivity(intent);
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
                List<Course> allCourses = repository.getAllCourses();
                final CourseAdapter courseAdapter = new CourseAdapter(this);
                RecyclerView recyclerView = findViewById(R.id.recyclerViewCourses);
                recyclerView.setAdapter(courseAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                courseAdapter.setCourses(allCourses);
        }
        return super.onOptionsItemSelected(item);
    }

}
