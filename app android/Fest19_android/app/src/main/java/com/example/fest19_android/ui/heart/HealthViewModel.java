package com.example.fest19_android.ui.heart;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fest19_android.data.HealthRepository;
import com.example.fest19_android.data.model.HealthEntry;

import java.util.List;

public class HealthViewModel extends ViewModel {
    private HealthRepository healthRepository;
    private LiveData<List<HealthEntry>> allEntries;

    public HealthViewModel(Application application) {
        healthRepository = HealthRepository.getInstance(application);
        allEntries = healthRepository.getAllEntries();
    }

    void sync() {
        healthRepository.fetchAll();
        healthRepository.uploadAll();
    }

    void uploadAll() {
        healthRepository.uploadAll();
    }

    LiveData<Boolean> delete(HealthEntry healthEntry) {
        return healthRepository.deleteFromServer(healthEntry);
    }

    void deleteAll() {
        healthRepository.deleteAllFromServer();
    }

    LiveData<List<HealthEntry>> getAllEntries() {
        return allEntries;
    }
}
