package com.jack.androidcrud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRecordActivity extends BaseActivity {

    private EditText edtAddName;
    private EditText edtAddDescription;
    private EditText edtAddPrice;
    private EditText edtAddRating;
    //private EditText edtAddImage;
    private ImageView addImageView;
    private String imagePath = "";
    private String oldPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        this.edtAddName = findViewById(R.id.edtAddName);
        this.edtAddDescription  = findViewById(R.id.edtAddDescription);
        this.edtAddPrice  = findViewById(R.id.edtAddPrice);
        this.edtAddRating = findViewById(R.id.edtAddRating);
        //this.edtAddImage = findViewById(R.id.edtAddImage);
        this.addImageView = findViewById(R.id.addImageView);

        //addImageView.setImageResource(R.drawable.clickme);
        addImageView.setClickable(true);
            addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toastIt("clicked image");
                openCameraIntentToFile();
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
        oldPath = imagePath;
        imagePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK ){

            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            addImageView.setImageBitmap(bitmap);

        }

        if(bitmap == null){
            imagePath = oldPath;
        }

    }




    public void saveEventOnClick(View v){

        if(this.edtAddName.getText().toString().equals("")
                || this.edtAddDescription.getText().toString().equals("")
                || this.edtAddPrice.getText().toString().equals("")
                || this.edtAddRating.getText().toString().equals("")
                || this.imagePath.equals("")){
            toastIt("PLEASE CHECK YOUR INFO");
            return;
        }

        final String recordName = this.edtAddName.getText().toString();
        final String recordDescription = this.edtAddDescription.getText().toString();
        final double recordPrice = Double.parseDouble(this.edtAddPrice.getText().toString());
        final double recordRating = Double.parseDouble(this.edtAddRating.getText().toString());
        final String recordImage = this.imagePath;

        if(recordRating < 1 || recordRating > 5){
            toastIt("RATING INVALID");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                Record record = new Record();
                record.setName(recordName);
                record.setDescription(recordDescription);
                record.setPrice(recordPrice);
                record.setRating(recordRating);
                record.setImage(recordImage);
                record.setDate_modified(new Date());
                record.setDate_created(new Date());
                recordDatabase.recordDao().addRecord(record);

                Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(showIntent);

            }
        }).start();

    }

}
