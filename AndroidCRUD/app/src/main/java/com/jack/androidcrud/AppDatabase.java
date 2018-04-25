package com.jack.androidcrud;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Jack on 4/16/2018.
 */

@Database(entities = {Record.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();
}
