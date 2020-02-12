package com.example.fest19_android.ui.heart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HealthViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public HealthViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HealthViewModel(application);
    }
}
