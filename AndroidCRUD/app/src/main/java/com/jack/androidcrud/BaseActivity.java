package com.jack.androidcrud;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseActivity extends AppCompatActivity {

    public static LiveData<List<Record>> AllRecords;
    public static AppDatabase recordDatabase;
    public static long recordID = -1;
    public static Record currentRecord;
    public static final int REQUEST_CAPTURE_IMAGE = 42;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(recordDatabase == null) {
            recordDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "events.db").fallbackToDestructiveMigration().build();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        //Inflate menu for menu passed
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        Intent intent;

        switch(item.getItemId()){
            case R.id.menuAdd:
                intent = new Intent(this, AddRecordActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuViewAll:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return true;
    }

    public static String randomString(int length){
        final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder b =  new StringBuilder();
        Random rand = new Random();
        for(int q = 0; q < length; q++){
            b.append(upper.charAt(rand.nextInt(52)));
        }
        return b.toString();
    }

    public void toastIt(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
