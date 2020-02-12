package com.example.fest19_android.ui.heart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fest19_android.R;
import com.example.fest19_android.data.LoginRepository;
import com.example.fest19_android.data.model.HealthEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of HealthEntries. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HealthDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HealthListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    HealthViewModel healthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(), HealthUploadActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.health_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        HealthViewModelFactory healthViewModelFactory = new HealthViewModelFactory(getApplication());
        healthViewModel = new ViewModelProvider(this, healthViewModelFactory).get(HealthViewModel.class);
        final HealthAdapter adapter = new HealthAdapter(this, mTwoPane, healthViewModel);
        RecyclerView recyclerView = findViewById(R.id.health_list);
        assert recyclerView != null;
        recyclerView.setAdapter(adapter);

        healthViewModel.getAllEntries().observe(this, new Observer<List<HealthEntry>>() {
            @Override
            public void onChanged(List<HealthEntry> healthEntries) {
                adapter.submitList(healthEntries);
            }
        });
        healthViewModel.sync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void logout(MenuItem item) {
        LoginRepository.getInstance(null).logout();
        finish();
    }

    public void sync(MenuItem item) {
        healthViewModel.sync();
    }

    public void deleteAll(MenuItem item) {
        healthViewModel.deleteAll();
    }
}
