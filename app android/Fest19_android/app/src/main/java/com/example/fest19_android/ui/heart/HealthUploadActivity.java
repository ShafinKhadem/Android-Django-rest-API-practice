package com.example.fest19_android.ui.heart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fest19_android.R;
import com.example.fest19_android.data.HealthRepository;
import com.example.fest19_android.data.model.HealthEntry;
import com.example.fest19_android.ui.login.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HealthUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }

    public void upload(View view) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        HealthEntry healthEntry = new HealthEntry(nowAsISO,
                Integer.parseInt(((EditText) findViewById(R.id.distanceTravelled)).getText().toString()),
                Integer.parseInt(((EditText) findViewById(R.id.leapCount)).getText().toString()),
                Integer.parseInt(((EditText) findViewById(R.id.heartRate)).getText().toString()));

        // didn't bother to create viewmodel for just 2 proxy methods.
        HealthRepository.getInstance(this).insert(healthEntry);
        HealthRepository.getInstance(this).upload(healthEntry);
        finish();
    }
}
