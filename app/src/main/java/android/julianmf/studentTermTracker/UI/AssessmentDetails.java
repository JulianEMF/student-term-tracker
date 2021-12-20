package android.julianmf.studentTermTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.Alarms.AssessmentEndDateReceiver;
import android.julianmf.studentTermTracker.Alarms.AssessmentStartDateReceiver;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Dialogs.DateStartDialogFragment;
import android.julianmf.studentTermTracker.Dialogs.DateDialogFragment;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.Entities.Course;
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

public class AssessmentDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int assessmentId;
    private String assessmentTitle;
    private String assessmentStartDate;
    private String assessmentEndDate;
    private String typeOfAssessment;
    private int courseId;
    private EditText editTextAssessmentTitle;
    private EditText editTextAssessmentStart;
    private EditText editTextAssessmentEnd;
    private Button ButtonSaveAssessment;
    private Assessment currentAssessment;
    public String associatedCourse;
    private EditText editTextAssessmentStartDate;
    private EditText editTextAssessmentEndDate;
    Repository repository;
    private Boolean enableDeleteButton = false;
    private Boolean enableNotifyButton = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());

        // Checks if it is a new assessment or an update
        if (getIntent().hasExtra("assessmentId")){

            // Enable the menu item to delete
            enableDeleteButton = true;

            // Enable the menu item to notify
            enableNotifyButton = true;

            // Spinner Implementation for type of assessment
            Spinner spinner = findViewById(R.id.spinnerAssessmentType);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessmentTypes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            String type = getIntent().getStringExtra("assessmentType");

            // Iterates over the spinner adapter trying to match the incoming type with an existing item
            for(int i= 0; i < spinner.getAdapter().getCount(); i++) {
                if(spinner.getAdapter().getItem(i).toString().contains(type)) {
                    spinner.setSelection(i);
                }
            }

            // Spinner implementation for associated courses
            List<Course> spinnerCourse =  repository.getAllCourses();
            ArrayAdapter<Course> adapterCourse = new ArrayAdapter<Course>(
                    this, android.R.layout.simple_spinner_item, spinnerCourse);
            adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner courseItems = (Spinner) findViewById(R.id.spinnerAssociatedCourse);
            courseItems.setAdapter(adapterCourse);
            courseId = getIntent().getIntExtra("courseId", -1);

            // Iterates over the spinner adapter trying to match the incoming type with an existing item
            for(int i= 0; i < spinnerCourse.size(); i++) {
                if(spinnerCourse.get(i).getCourseID() == courseId) {
                    courseItems.setSelection(i);
                }
            }

            // Detect the assessment selection
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    typeOfAssessment = parentView.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // Detect the course selection
            courseItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    associatedCourse = parentView.getItemAtPosition(position).toString();
                    courseId = repository.getCourseId(associatedCourse);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // On to populate assessment method
            populateAssessment();
        }else{
            // Queries the database to find the next available ID
            assessmentId = findNextAssessmentId();

            // Spinner Implementation for type of assessment
            Spinner spinner = findViewById(R.id.spinnerAssessmentType);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessmentTypes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            // Spinner implementation for associated courses
            List<Course> spinnerCourse =  repository.getAllCourses();
            ArrayAdapter<Course> adapterCourse = new ArrayAdapter<Course>(
                    this, android.R.layout.simple_spinner_item, spinnerCourse);
            adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner courseItems = (Spinner) findViewById(R.id.spinnerAssociatedCourse);
            courseItems.setAdapter(adapterCourse);

            // Detect the assessment selection
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    typeOfAssessment = parentView.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // Detect the course selection
            courseItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    associatedCourse = parentView.getItemAtPosition(position).toString();
                    courseId = repository.getCourseId(associatedCourse);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            // Saving functionality
            ButtonSaveAssessment = findViewById(R.id.buttonSaveAssessment);
            ButtonSaveAssessment.setOnClickListener(view -> {
                saveAssessment();
            });
        }
    }

    // Parses through the fields gathering data, creates an entity Assessment and uses the repository to insert it in the database
    public void saveAssessment() {
        editTextAssessmentTitle = findViewById(R.id.editTextAssessmentTitle);
        editTextAssessmentStart = findViewById(R.id.editTextAssessmentStart);
        editTextAssessmentEnd = findViewById(R.id.editTextAssessmentEnd);

        String assessmentType = typeOfAssessment;
        String assessmentTitle = editTextAssessmentTitle.getText().toString();
        String assessmentStartDate = editTextAssessmentStart.getText().toString();
        String assessmentEndDate = editTextAssessmentEnd.getText().toString();

        // If the date input is valid
        if(validateDate()){
            Assessment assessment = new Assessment(assessmentId, assessmentType, assessmentTitle, assessmentStartDate, assessmentEndDate, courseId);
            repository.insertAssessment(assessment);

            Intent intent = new Intent(AssessmentDetails.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Populates the fields with the existing data
    public void populateAssessment(){
        assessmentId = getIntent().getIntExtra("assessmentId", 1);
        assessmentTitle =  getIntent().getStringExtra("assessmentTitle");
        assessmentStartDate = getIntent().getStringExtra("assessmentStartDate");
        assessmentEndDate = getIntent().getStringExtra("assessmentEndDate");
        courseId = getIntent().getIntExtra("termId", 1);

        editTextAssessmentTitle = findViewById(R.id.editTextAssessmentTitle);
        editTextAssessmentStart = findViewById(R.id.editTextAssessmentStart);
        editTextAssessmentEnd = findViewById(R.id.editTextAssessmentEnd);

        editTextAssessmentTitle.setText(assessmentTitle);
        editTextAssessmentStart.setText(assessmentStartDate);
        editTextAssessmentEnd.setText(assessmentEndDate);

        ButtonSaveAssessment = findViewById(R.id.buttonSaveAssessment);
        ButtonSaveAssessment.setText("Update");
        ButtonSaveAssessment.setOnClickListener(view -> {
            updateAssessment(assessmentId, courseId);
        });
    }

    // Parses through the fields gathering data, creates an entity Assessment and uses the repository to update it in the database
    public void updateAssessment(int assessmentId, int courseId){
        editTextAssessmentTitle = findViewById(R.id.editTextAssessmentTitle);
        editTextAssessmentStart = findViewById(R.id.editTextAssessmentStart);
        editTextAssessmentEnd = findViewById(R.id.editTextAssessmentEnd);

        int updatedAssessmentId = assessmentId;
        String updatedAssessmentTitle = editTextAssessmentTitle.getText().toString();
        String updatedAssessmentStart = editTextAssessmentStart.getText().toString();
        String updatedAssessmentEnd = editTextAssessmentEnd.getText().toString();
        String updatedAssessmentType = typeOfAssessment;
        int updatedCourseId = courseId;

        // If the input date is valid
        if(validateDate()) {
            Assessment assessment = new Assessment(updatedAssessmentId, updatedAssessmentType, updatedAssessmentTitle, updatedAssessmentStart, updatedAssessmentEnd, updatedCourseId);
            repository.updateAssessment(assessment);

            Intent intent = new Intent(AssessmentDetails.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Finds the next available ID
    public int findNextAssessmentId() {
        int nextAssessmentId = 0;

        for (Assessment assessment : repository.getAllAssessments()) {
            if(nextAssessmentId > assessment.getAssessmentID()){
                nextAssessmentId = assessment.getAssessmentID();
                return nextAssessmentId;
            }else{
                nextAssessmentId = assessment.getAssessmentID();
            }
        }
        return ++nextAssessmentId;
    }

    // Validates that the dates are valid
    public boolean validateDate(){
        editTextAssessmentStart = findViewById(R.id.editTextAssessmentStart);
        editTextAssessmentEnd = findViewById(R.id.editTextAssessmentEnd);
        String startDateText = editTextAssessmentStart.getText().toString();
        String endDateText = editTextAssessmentEnd.getText().toString();
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
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notifyAssessment:
                if(validateDate()){
                    editTextAssessmentStartDate = findViewById(R.id.editTextAssessmentStart);
                    editTextAssessmentEndDate = findViewById(R.id.editTextAssessmentEnd);
                    String startDateFromScreen = editTextAssessmentStartDate.getText().toString();
                    String endDateFromScreen = editTextAssessmentEndDate.getText().toString();
                    String assessmentTitle = editTextAssessmentTitle.getText().toString();
                    String myFormat = "MM/dd/yy";
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
                    Intent intent= new Intent(AssessmentDetails.this, AssessmentStartDateReceiver.class);
                    intent.putExtra("key","The assessment " + assessmentTitle + " begins today");
                    PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent,0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

                    Long trigger2 = myEndDate.getTime();
                    Intent intent2 = new Intent(AssessmentDetails.this, AssessmentEndDateReceiver.class);
                    intent2.putExtra("key2","The assessment " + assessmentTitle + " is due today");
                    PendingIntent sender2 = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent2,0);
                    AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);

                    Toast.makeText(AssessmentDetails.this,"A notification has been established", Toast.LENGTH_SHORT).show();
                    return true;
                }else{
                    Toast.makeText(AssessmentDetails.this,"Please enter a valid date", Toast.LENGTH_SHORT).show();
                }
            case R.id.deleteAssessment:
                for(Assessment assessment : repository.getAllAssessments()){
                    if(assessment.getAssessmentID() == 1)
                        currentAssessment = assessment;
                }
                if(true) {
                    repository.deleteAssessment(currentAssessment);
                    Intent afterDeleteIntent = new Intent(AssessmentDetails.this, MainActivity.class);
                    startActivity(afterDeleteIntent);
                    Toast.makeText(AssessmentDetails.this,"The assessment has been successfully deleted", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    // Enables the delete menu item
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.deleteAssessment);
        MenuItem item2 = menu.findItem(R.id.notifyAssessment);
        if(enableDeleteButton == true){
            item.setEnabled(true);
        }
        if(enableNotifyButton == true){
            item2.setEnabled(true);
        }
        return true;
    }
}