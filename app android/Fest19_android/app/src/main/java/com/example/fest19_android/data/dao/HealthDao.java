package com.example.fest19_android.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fest19_android.data.model.HealthEntry;

import java.util.List;

@Dao
public interface HealthDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HealthEntry... healthEntries);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<HealthEntry> healthEntries);

    @Update
    void update(HealthEntry... healthEntries);

    @Update
    void update(List<HealthEntry> healthEntries);

    @Delete
    void delete(HealthEntry... healthEntries);

    @Delete
    void delete(List<HealthEntry> healthEntries);

    @Query("select * from userHealthTable order by recordingTime desc")
    LiveData<List<HealthEntry>> getAllEntries();

    @Query("select * from userHealthTable where entryId = :id")
    HealthEntry getItemByid(String id);

    @Query("delete from userHealthTable")
    void deleteAll();
}
