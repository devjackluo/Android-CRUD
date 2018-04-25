package com.jack.androidcrud;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity {

    private ListView mListView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById( R.id.mListView );

        AllRecords = recordDatabase.recordDao().getAll();
        AllRecords.observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(@Nullable final List<Record> records) {

                List<String> recordNames = new ArrayList<>();
                for(Record e : records){
                    if(e.getDescription().length() > 15) {
                        recordNames.add(e.getName() + "  --  " + e.getDescription().substring(0, 15) + "...");
                    }else {
                        recordNames.add(e.getName() + "  --  " + e.getDescription());
                    }
                }

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent showIntent = new Intent(getApplicationContext(), ShowRecordActivity.class);

                        //TODO see if correct
                        //seems alright
                        //showIntent.putExtra("ID", records.get(i).getRecordID());
                        //way better this way.
                        recordID =  records.get(i).getRecordID();

                        startActivity(showIntent);
                    }
                });

                adapter = new ArrayAdapter<String>( getApplicationContext(), R.layout.activity_listview_cell, recordNames );
                mListView.setAdapter( adapter );
                adapter.notifyDataSetChanged();
                mListView.invalidateViews();
                mListView.refreshDrawableState();

            }
        });

    }

    public void create40(View v){



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want create 40 random records?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Random rand = new Random();
                                for(int x = 0; x < 40; x++) {

                                    Record record = new Record();
                                    record.setName(randomString(5));
                                    record.setDescription(randomString(rand.nextInt(30)+3));
                                    record.setPrice(rand.nextInt(100));
                                    record.setRating(rand.nextInt(5)+1);
                                    record.setImage("");
                                    record.setDate_modified(new Date());
                                    record.setDate_created(new Date());
                                    recordDatabase.recordDao().addRecord(record);

                                }

                            }
                        }).start();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();

    }

    public void headToAdd(View v){
        Intent intent = new Intent(this, AddRecordActivity.class);
        startActivity(intent);
    }

}
