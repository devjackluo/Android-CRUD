package com.jack.androidcrud;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowRecordActivity extends BaseActivity {

    private TextView txtName, txtDescription, txtRating, txtPrice, txtImage;
    private ImageView showImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtRating = findViewById(R.id.txtRating);
        txtPrice = findViewById(R.id.txtPrice);
        //txtImage = findViewById(R.id.txtImage);
        showImageView = findViewById(R.id.showImageView);

        LiveData<Record> record = recordDatabase.recordDao().findByRecordNum((int)recordID);

        record.observe(this, new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {

                currentRecord = record;

                if (record != null) {
                    txtName.setText(record.getName());
                    txtDescription.setText(record.getDescription());
                    txtPrice.setText(Double.toString(record.getPrice()));
                    txtRating.setText(Double.toString(record.getRating()));
                    //txtImage.setText(record.getImage());
                    Bitmap bitmap = BitmapFactory.decodeFile(record.getImage());

                    if(bitmap != null){
                        showImageView.setImageBitmap(bitmap);
                    }else {
                        showImageView.setImageResource(R.drawable.notfound);
                    }

                }
            }
        });

    }

    public void editRecord(View v){

        Intent intent = new Intent(getApplicationContext(), EditRecordActivity.class);
        startActivity(intent);

    }


    public void deleteRecord(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this record?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recordDatabase.recordDao().deleteRecord(currentRecord);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
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

}
