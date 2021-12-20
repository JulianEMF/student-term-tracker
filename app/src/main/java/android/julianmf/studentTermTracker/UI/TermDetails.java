package android.julianmf.studentTermTracker.UI;

import android.content.Intent;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Dialogs.DateStartDialogFragment;
import android.julianmf.studentTermTracker.Dialogs.DateDialogFragment;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.Entities.Term;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TermDetails extends AppCompatActivity {

    private int termId;
    String termName;
    String termStartDate;
    String termEndDate;
    public EditText editTextTermTitle;
    public EditText editTextTermStart;
    public EditText editTextTermEnd;
    public Button ButtonSaveTerm;
    Repository repository;
    private Button buttonAddCourse;
    private Boolean enableDeleteButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());

        // Checks if it is a new term or an update
        if (getIntent().hasExtra("termId")) {
            // Enable the menu item to delete
            enableDeleteButton = true;

            populateTerm();
        } else {
            termId = findNextTermId();

            // Saving functionality
            ButtonSaveTerm = findViewById(R.id.buttonSaveTerm);
            ButtonSaveTerm.setOnClickListener(view -> {
                saveTerm();
            });
        }

        // On to add course
        buttonAddCourse = findViewById(R.id.buttonAddCourse);
        buttonAddCourse.setOnClickListener(view -> {
            Intent intent = new Intent(TermDetails.this, CourseDetails.class);
            startActivity(intent);
        });
    }

    // Populates the fields with the existing data
    public void populateTerm() {
        termId = getIntent().getIntExtra("termId", -1);
        termName = getIntent().getStringExtra("termName");
        termStartDate = getIntent().getStringExtra("termStartDate");
        termEndDate = getIntent().getStringExtra("termEndDate");

        populateAssociatedTermsRecyclerView(termId);

        editTextTermTitle = findViewById(R.id.editTextTermTitle);
        editTextTermStart = findViewById(R.id.editTextTermStart);
        editTextTermEnd = findViewById(R.id.editTextTermEnd);

        editTextTermTitle.setText(termName);
        editTextTermStart.setText(termStartDate);
        editTextTermEnd.setText(termEndDate);

        ButtonSaveTerm = findViewById(R.id.buttonSaveTerm);
        ButtonSaveTerm.setText("Update");
        ButtonSaveTerm.setOnClickListener(view -> {
            updateTerm();
        });
    }

    // Populates the recycler view
    public void populateAssociatedTermsRecyclerView(int termId){
        // Query the course table in the database for the matching courseID
        // Then populates the recyclerview with the result
        List<Course> allCoursesById = repository.getAllCoursesById(termId);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAssociatedCourses);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(allCoursesById);
    }

    // Saves the term
    public void saveTerm() {
        editTextTermTitle = findViewById(R.id.editTextTermTitle);
        editTextTermStart = findViewById(R.id.editTextTermStart);
        editTextTermEnd = findViewById(R.id.editTextTermEnd);

        String termTitle = editTextTermTitle.getText().toString();
        String termStart = editTextTermStart.getText().toString();
        String termEnd = editTextTermEnd.getText().toString();

        if(validateDate()) {
            Term term = new Term(++termId, termTitle, termStart, termEnd);
            repository.insertTerm(term);

            Intent intent = new Intent(TermDetails.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Updates the term
    public void updateTerm() {
        editTextTermTitle = findViewById(R.id.editTextTermTitle);
        editTextTermStart = findViewById(R.id.editTextTermStart);
        editTextTermEnd = findViewById(R.id.editTextTermEnd);

        int updatedTermId = termId;
        String updatedTermTitle = editTextTermTitle.getText().toString();
        String updatedTermStart = editTextTermStart.getText().toString();
        String updatedTermEnd = editTextTermEnd.getText().toString();

        if(validateDate()) {
            Term term = new Term(updatedTermId, updatedTermTitle, updatedTermStart, updatedTermEnd);
            repository.updateTerm(term);

            Intent intent = new Intent(TermDetails.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Finds the next available ID
    public int findNextTermId() {
        int nextTermId = 0;

        for (Term term : repository.getAllTerms()) {
            if(nextTermId > term.getTermId()){
                nextTermId = term.getTermId();
                return nextTermId;
            }else{
                nextTermId = term.getTermId();
            }
        }
        return ++nextTermId;
    }

    // Validates the input date
    public boolean validateDate(){
        editTextTermStart = findViewById(R.id.editTextTermStart);
        editTextTermEnd = findViewById(R.id.editTextTermEnd);
        String startDateText = editTextTermStart.getText().toString();
        String endDateText = editTextTermEnd.getText().toString();
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Date startDate = null;
        Date endDate = null;
        try {
            if(validDateNumbers(startDateText, endDateText) == false){
                DialogFragment newFragment = new DateDialogFragment();
                newFragment.show(getSupportFragmentManager(), "wrongInput");
                return false;
            }
            startDate = formatter.parse(startDateText);
            endDate = formatter.parse(endDateText);

            if(startDate.after(endDate)){
                DialogFragment newFragment = new DateStartDialogFragment();
                newFragment.show(getSupportFragmentManager(), "wrongDate");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DialogFragment newFragment = new DateDialogFragment();
            newFragment.show(getSupportFragmentManager(), "wrongInput");
        }
        return false;
    }

    public boolean validDateNumbers(String startDateText, String endDateText){
        int monthStart = Integer.parseInt(startDateText.substring(0, 2));
        int dayStart = Integer.parseInt(startDateText.substring(3, 5));
        int yearStart = Integer.parseInt(startDateText.substring(6));

        int monthEnd = Integer.parseInt(endDateText.substring(0, 2));
        int dayEnd = Integer.parseInt(endDateText.substring(3, 5));
        int yearEnd = Integer.parseInt(endDateText.substring(6));

        if(
                !((((dayStart > 0) && (dayStart < 32)) && ((monthStart > 0) && (monthStart < 13)) && (yearStart > 999)) ||
                        (((dayEnd > 0) && (dayEnd < 32)) && ((monthEnd > 0) && (monthEnd < 13)) && (yearEnd > 999)))
        ){
            return false;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteTerm:
                for(Course course : repository.getAllCourses()){
                    if(course.getTermID() == termId){
                        Toast.makeText(TermDetails.this,"Please remove the associated courses before deleting this term", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                for(Term term : repository.getAllTerms()){
                    if(term.getTermId() == termId) {
                        repository.deleteTerm(term);
                        Intent afterDeleteIntent = new Intent(TermDetails.this, MainActivity.class);
                        startActivity(afterDeleteIntent);
                        Toast.makeText(TermDetails.this,"The term has been successfully deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        return super.onOptionsItemSelected(item);
        }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.deleteTerm);
        if(enableDeleteButton == true){
            item.setEnabled(true);
        }
        return true;
    }
}

