package com.example.fest19_android.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "userHealthTable")
public class HealthEntry {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryId")
    public String id;
    @SerializedName("recordTime")
    public String recordingTime;
    public int distanceTravelled, leapCount, heartRate;

    public HealthEntry() {
        id = UUID.randomUUID().toString();
    }

    public HealthEntry(String recordingTime, int distanceTravelled, int leapCount, int heartRate) {
        id = UUID.randomUUID().toString();
        this.recordingTime = recordingTime;
        this.distanceTravelled = distanceTravelled;
        this.leapCount = leapCount;
        this.heartRate = heartRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthEntry that = (HealthEntry) o;
        return id.equals(that.id) &&
                distanceTravelled == that.distanceTravelled &&
                leapCount == that.leapCount &&
                heartRate == that.heartRate &&
                Objects.equals(recordingTime, that.recordingTime);
    }

    @NonNull
    @Override
    public String toString() {
        return "HealthEntry{" +
                "recordingTime='" + recordingTime + '\'' +
                ", id=" + id +
                ", distanceTravelled=" + distanceTravelled +
                ", leapCount=" + leapCount +
                ", heartRate=" + heartRate +
                '}';
    }
}
