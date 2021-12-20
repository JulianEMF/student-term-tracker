package android.julianmf.studentTermTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.Alarms.CourseStartDateReceiver;
import android.julianmf.studentTermTracker.Alarms.CourseEndDateReceiver;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Dialogs.DateStartDialogFragment;
import android.julianmf.studentTermTracker.Dialogs.DateDialogFragment;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.Entities.Term;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private int courseId;
    private String courseTitle;
    private String courseStartDate;
    private String courseEndDate;
    private String status;
    private String courseInstructorName;
    private String courseInstructorPhone;
    private String courseInstructorEmail;
    private String notes;
    private int termId;
    private String updatedNotes;
    private EditText editTextCourseTitle;
    private EditText editTextCourseStart;
    private EditText editTextCourseEnd;
    private EditText editTextInstructorName;
    private EditText editTextInstructorPhone;
    private EditText editTextInstructorEmail;
    private EditText editTextNotes;
    private Button buttonAddAssessment;
    private Button ButtonSaveCourse;
    private String associatedTerm;
    public Course currentCourse;
    Repository repository;
    private Boolean enableDeleteButton = false;
    private Boolean enableShareButton = false;
    private Boolean enableNotifyButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());

        // Checks if it is a new course or an update
        if (getIntent().hasExtra("courseId")){
            // Enable the menu item to delete
            enableDeleteButton = true;

            // Enable the menu item to share
            enableShareButton = true;

            // Enable the menu item to notify
            enableNotifyButton = true;


            // Spinner Implementation for course Status
            Spinner spinner = findViewById(R.id.spinnerCourseStatus);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.courseStatus, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            String courseStatus = getIntent().getStringExtra("status");

            // Iterates over the spinner adapter trying to match the incoming type with an existing item
            for(int i= 0; i < spinner.getAdapter().getCount(); i++) {
                if(spinner.getAdapter().getItem(i).toString().contains(courseStatus)) {
                    spinner.setSelection(i);
                }
            }

            // Spinner implementation for associated terms
            List<Term> spinnerTerm =  repository.getAllTerms();
            ArrayAdapter<Term> adapterTerm = new ArrayAdapter<Term>(
                    this, android.R.layout.simple_spinner_item, spinnerTerm);
            adapterTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner termItems = (Spinner) findViewById(R.id.spinnerAssociatedTerm);
            termItems.setAdapter(adapterTerm);

            // Detect the assessment selection
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    status = parentView.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            termId = getIntent().getIntExtra("termID", -1);

            // Iterates over the spinner adapter trying to match the incoming type with an existing item
            for(int i= 0; i < spinnerTerm.size(); i++) {
                if(spinnerTerm.get(i).getTermId() == termId) {
                    termItems.setSelection(i);
                }
            }

            // Detect the course selection
            termItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    associatedTerm = parentView.getItemAtPosition(position).toString();
                    termId = repository.getTermId(associatedTerm);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            populateCourse();
        }else{
            // // Queries the database to find the next available ID
            courseId = findNextCourseId();

            // Spinner Implementation for course status
            Spinner spinner = findViewById(R.id.spinnerCourseStatus);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.courseStatus, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            // Spinner implementation for associated terms
            List<Term> spinnerTerm =  repository.getAllTerms();
            ArrayAdapter<Term> adapterTerm = new ArrayAdapter<Term>(
                    this, android.R.layout.simple_spinner_item, spinnerTerm);
            adapterTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner courseItems = (Spinner) findViewById(R.id.spinnerAssociatedTerm);
            courseItems.setAdapter(adapterTerm);

            // Detect the status selection
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    status = parentView.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // Detect the term selection
            courseItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    associatedTerm = parentView.getItemAtPosition(position).toString();
                    termId = repository.getTermId(associatedTerm);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // Saving functionality
            ButtonSaveCourse = findViewById(R.id.buttonSaveCourse);
            ButtonSaveCourse.setOnClickListener(view -> {
                saveCourse();
            });
        }

        // Event listener for add assessment button
        buttonAddAssessment = findViewById(R.id.buttonAddAssessment);
        buttonAddAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
            startActivity(intent);
        });
    }

    // Populates the fields with the existing data
    public void populateCourse(){
        courseId = getIntent().getIntExtra("courseId", 1);
        courseTitle =  getIntent().getStringExtra("title");
        courseStartDate = getIntent().getStringExtra("courseStartDate");
        courseEndDate = getIntent().getStringExtra("courseEndDate");
        courseInstructorName = getIntent().getStringExtra("instructorName");
        courseInstructorPhone = getIntent().getStringExtra("instructorPhone");
        courseInstructorEmail = getIntent().getStringExtra("instructorEmail");
        termId = getIntent().getIntExtra("termId", 1);
        notes = getIntent().getStringExtra("notes");

        populateAssociatedCoursesRecyclerView(courseId);

        editTextCourseTitle = findViewById(R.id.editTextCourseTitle);
        editTextCourseStart = findViewById(R.id.editTextCourseStart);
        editTextCourseEnd = findViewById(R.id.editTextCourseEnd);
        editTextInstructorName = findViewById(R.id.editTextInstructorName);
        editTextInstructorPhone = findViewById(R.id.editTextInstructorPhone);
        editTextInstructorEmail = findViewById(R.id.editTextInstructorEmail);
        editTextNotes = findViewById(R.id.editTextNotes);

        editTextCourseTitle.setText(courseTitle);
        editTextCourseStart.setText(courseStartDate);
        editTextCourseEnd.setText(courseEndDate);
        editTextInstructorName.setText(courseInstructorName);
        editTextInstructorPhone.setText(courseInstructorPhone);
        editTextInstructorEmail.setText(courseInstructorEmail);
        if(notes.length() > 0){
            editTextNotes.setText(notes);
        }else{
            editTextNotes.setText("");
        }

        ButtonSaveCourse = findViewById(R.id.buttonSaveCourse);
        ButtonSaveCourse.setText("Update");
        ButtonSaveCourse.setOnClickListener(view -> {
            updateCourse(courseId, termId);
        });
    }

    public void populateAssociatedCoursesRecyclerView(int courseId){
        // Query the assessment table in the database for the matching courseID
        // Then populates the recyclerview with the result
        repository = new Repository(getApplication());
        List<Assessment> allAssessmentsById = repository.getAllAssessmentsByID(courseId);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAssociatedAssessments);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(allAssessmentsById);
    }

    // Saves the course
    public void saveCourse(){
        editTextCourseTitle = findViewById(R.id.editTextCourseTitle);
        editTextCourseStart = findViewById(R.id.editTextCourseStart);
        editTextCourseEnd = findViewById(R.id.editTextCourseEnd);
        editTextInstructorName = findViewById(R.id.editTextInstructorName);
        editTextInstructorPhone = findViewById(R.id.editTextInstructorPhone);
        editTextInstructorEmail = findViewById(R.id.editTextInstructorEmail);
        editTextNotes = findViewById(R.id.editTextNotes);

        courseTitle = editTextCourseTitle.getText().toString();
        courseStartDate = editTextCourseStart.getText().toString();
        courseEndDate = editTextCourseEnd.getText().toString();
        courseInstructorName = editTextInstructorName.getText().toString();
        courseInstructorPhone = editTextInstructorPhone.getText().toString();
        courseInstructorEmail = editTextInstructorEmail.getText().toString();
        if(!editTextNotes.getText().toString().isEmpty()){
            notes = editTextNotes.getText().toString();
        }else{
            notes = "";
        }

        if(validateDate()) {
            Course course = new Course(courseId, courseTitle, courseStartDate, courseEndDate, status, courseInstructorName, courseInstructorPhone, courseInstructorEmail, termId, notes);
            repository.insertCourse(course);

            Intent intent = new Intent(CourseDetails.this, CoursesList.class);
            startActivity(intent);
        }
    }

    // Updates the course
    public void updateCourse(int courseId, int termId){
        editTextCourseTitle = findViewById(R.id.editTextCourseTitle);
        editTextCourseStart = findViewById(R.id.editTextCourseStart);
        editTextCourseEnd = findViewById(R.id.editTextCourseEnd);
        editTextInstructorName = findViewById(R.id.editTextInstructorName);
        editTextInstructorPhone = findViewById(R.id.editTextInstructorPhone);
        editTextInstructorEmail = findViewById(R.id.editTextInstructorEmail);
        editTextNotes = findViewById(R.id.editTextNotes);

        int updatedCourseId = courseId;
        String updatedCourseTitle = editTextCourseTitle.getText().toString();
        String updatedCourseStart = editTextCourseStart.getText().toString();
        String updatedCourseEnd = editTextCourseEnd.getText().toString();
        String updatedCourseStatus = status;
        String updatedInstructorName = editTextInstructorName.getText().toString();
        String updatedInstructorPhone = editTextInstructorPhone.getText().toString();
        String updatedInstructorEmail = editTextInstructorEmail.getText().toString();
        if(editTextNotes.getText().toString().length() > 0){
            updatedNotes = editTextNotes.getText().toString();
        }else{
            updatedNotes = "";
        }
        int updatedTermId = termId;

        if(validateDate()) {
            Course course = new Course(updatedCourseId, updatedCourseTitle, updatedCourseStart, updatedCourseEnd, updatedCourseStatus, updatedInstructorName, updatedInstructorPhone, updatedInstructorEmail, updatedTermId, updatedNotes);
            repository.updateCourse(course);

            Intent intent = new Intent(CourseDetails.this, CoursesList.class);
            startActivity(intent);
        }
    }

    // Finds the next available ID
    public int findNextCourseId() {
        int nextCourseId = 0;

        for (Course course : repository.getAllCourses()) {
            if(nextCourseId > course.getCourseID()){
                nextCourseId = course.getCourseID();
                return nextCourseId;
            }else{
                nextCourseId = course.getCourseID();
            }
        }
        return ++nextCourseId;
    }

    // Validates the input date
    public boolean validateDate(){
        editTextCourseStart = findViewById(R.id.editTextCourseStart);
        editTextCourseEnd = findViewById(R.id.editTextCourseEnd);
        String startDateText = editTextCourseStart.getText().toString();
        String endDateText = editTextCourseEnd.getText().toString();
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
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notifyCourse:
                if(validateDate()){
                    editTextCourseStart = findViewById(R.id.editTextCourseStart);
                    editTextCourseEnd = findViewById(R.id.editTextCourseEnd);
                    editTextCourseTitle = findViewById(R.id.editTextCourseTitle);
                    String startDateFromScreen = editTextCourseStart.getText().toString();
                    String endDateFromScreen = editTextCourseEnd.getText().toString();
                    String courseTitleFromScreen = editTextCourseTitle.getText().toString();
                    String myFormat = "MM/dd/yy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    Date myStartDate = null;
                    Date myEndDate = null;
                    try {
                        myStartDate = sdf.parse(startDateFromScreen);
                        myEndDate = sdf.parse(endDateFromScreen);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Long trigger = myStartDate.getTime();
                    Intent intent= new Intent(CourseDetails.this, CourseStartDateReceiver.class);
                    intent.putExtra("key","Course: " + courseTitleFromScreen + " starts today");
                    PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent,0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                    Long trigger2 = myEndDate.getTime();
                    Intent intent2= new Intent(CourseDetails.this, CourseEndDateReceiver.class);
                    intent2.putExtra("key2","Course: " + courseTitleFromScreen + " ends today");
                    PendingIntent sender2 = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent2,0);
                    AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);

                    Toast.makeText(CourseDetails.this,"A notification has been established", Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    Toast.makeText(CourseDetails.this,"Please enter a valid date", Toast.LENGTH_SHORT).show();
                }
            case R.id.shareCourse:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, notes);
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Notes for the course " + courseTitle);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.deleteCourse:
                for(Course course : repository.getAllCourses()){
                    if(course.getCourseID() == courseId)
                        currentCourse = course;
                }
                if(true) {
                    repository.deleteCourse(currentCourse);
                }
                Intent afterDeleteIntent = new Intent(CourseDetails.this, MainActivity.class);
                startActivity(afterDeleteIntent);
                Toast.makeText(CourseDetails.this,"The course has been successfully deleted", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.deleteCourse);
        MenuItem item2 = menu.findItem(R.id.shareCourse);
        MenuItem item3 = menu.findItem(R.id.notifyCourse);
        if(enableDeleteButton == true){
            item.setEnabled(true);
        }
        if(enableShareButton == true){
            item2.setEnabled(true);
        }
        if(enableNotifyButton == true){
            item3.setEnabled(true);
        }
        return true;
    }
}