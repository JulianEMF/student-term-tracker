package android.julianmf.studentTermTracker.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.julianmf.studentTermTracker.Database.Repository;
import android.julianmf.studentTermTracker.Entities.Term;
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

public class TermsList extends AppCompatActivity {

    private Repository repository;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        repository = new Repository(getApplication());
        List<Term> allTerms = repository.getAllTerms();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTerms);
        final TermAdapter termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);

        FloatingActionButton addTerm = findViewById(R.id.fab_add_term);
        addTerm.setOnClickListener(view -> {
            onToAddTerm(view);
        });

    }

    // Redirects to add term activity
    public void onToAddTerm(View view) {
        Intent intent = new Intent(TermsList.this, TermDetails.class);
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
                List<Term> allTerms = repository.getAllTerms();
                final TermAdapter termAdapter = new TermAdapter(this);
                RecyclerView recyclerView = findViewById(R.id.recyclerViewTerms);
                recyclerView.setAdapter(termAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                termAdapter.setTerms(allTerms);
        }
        return super.onOptionsItemSelected(item);
    }

}
