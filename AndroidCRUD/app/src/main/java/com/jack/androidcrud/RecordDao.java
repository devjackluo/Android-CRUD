package com.jack.androidcrud;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by Jack on 4/16/2018.
 */


@Dao
public interface RecordDao {

    @Query("SELECT * FROM records")
    LiveData<List<Record>> getAll();

    //WHERE tablename = :passed_items
    @Query("SELECT * FROM records WHERE name=:record_name LIMIT 1")
    Record findByName(String record_name);

    @Query("SELECT * FROM records WHERE recordID=:recordID LIMIT 1")
    LiveData<Record> findByRecordNum(int recordID);

    @Insert
    void addRecord(Record event);

    @Update
    void updateRecord(Record event);

    @Delete
    void deleteRecord(Record event);

}
