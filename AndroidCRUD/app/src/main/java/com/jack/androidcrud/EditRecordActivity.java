package com.jack.androidcrud;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditRecordActivity extends BaseActivity {

    private EditText edtEditName;
    private EditText edtEditDescription;
    private EditText edtEditPrice;
    private EditText edtEditRating;
    //private EditText edtEditImage;
    private ImageView editImageView;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        this.edtEditName = findViewById(R.id.edtEditName);
        this.edtEditDescription  = findViewById(R.id.edtEditDescription);
        this.edtEditPrice  = findViewById(R.id.edtEditPrice);
        this.edtEditRating = findViewById(R.id.edtEditRating);
        //this.edtEditImage = findViewById(R.id.edtEditImage);
        this.editImageView = findViewById(R.id.editImageView);

        editImageView.setClickable(true);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toastIt("clicked image");
                openCameraIntentToFile();
            }
        });


        LiveData<Record> record = recordDatabase.recordDao().findByRecordNum((int)recordID);

        record.observe(this, new Observer<Record>() {
            @Override
            public void onChanged(@Nullable Record record) {

                currentRecord = record;

                if(record != null) {
                    edtEditName.setText(record.getName());
                    edtEditDescription.setText(record.getDescription());
                    edtEditPrice.setText(Double.toString(record.getPrice()));
                    edtEditRating.setText(Double.toString(record.getRating()));
                    //edtEditImage.setText(record.getImage());

                    imagePath = record.getImage();
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                    if(bitmap != null){
                        editImageView.setImageBitmap(bitmap);
                    }else {
                        editImageView.setImageResource(R.drawable.clickme);
                    }


                }
            }
        });

    }


    private void openCameraIntentToFile(){

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null ){

            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){
                toastIt("File Error");
            }

            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.jack.androidcrud.fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }

        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK ){

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            editImageView.setImageBitmap(bitmap);

        }

    }



    public void saveChanges(View v){

        if(this.edtEditName.getText().toString().equals("")
                || this.edtEditDescription.getText().toString().equals("")
                || this.edtEditPrice.getText().toString().equals("")
                || this.edtEditRating.getText().toString().equals("")
                || this.imagePath.equals("")){
            toastIt("PLEASE CHECK YOUR INFO");
            return;
        }

        final String recordName = this.edtEditName.getText().toString();
        final String recordDescription = this.edtEditDescription.getText().toString();
        final double recordPrice = Double.parseDouble(this.edtEditPrice.getText().toString());
        final double recordRating = Double.parseDouble(this.edtEditRating.getText().toString());
        final String recordImage = this.imagePath;


        if(recordRating < 1 || recordRating > 5){
            toastIt("RATING INVALID");
            return;
        }




        new Thread(new Runnable() {
            @Override
            public void run() {

                Record temp = currentRecord;
                temp.setName(recordName);
                temp.setDescription(recordDescription);
                temp.setPrice(recordPrice);
                temp.setRating(recordRating);

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if(bitmap != null){
                    temp.setImage(imagePath);
                }

                temp.setDate_modified(new Date());

                recordDatabase.recordDao().updateRecord(temp);

                Intent showIntent = new Intent(getApplicationContext(), ShowRecordActivity.class);
                startActivity(showIntent);

            }
        }).start();

    }

}
