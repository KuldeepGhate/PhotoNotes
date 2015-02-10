    package com.example.kuldeep.photonotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddPhoto extends Activity {


    static int TAKE_PICTURE = 1;
    static int ACTION_TAKE_PICTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    private File imageFile;
    DatabaseAdapter Helper;
    EditText editCaption;
    String caption;
    String path;
    Bitmap bitMap;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        editCaption = (EditText) findViewById(R.id.editCaption);

        Helper = new DatabaseAdapter(this);
        final Button capture = (Button) findViewById(R.id.capture);
        imageView = (ImageView) findViewById(R.id.image);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               process(v);
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption = editCaption.getText().toString();
                if(caption.isEmpty())
                {
                    editCaption.setError("Please enter the caption!");
                }
                else if(path == null){
                    AlertDialog alertDialog = new AlertDialog.Builder(AddPhoto.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please Capture an Image!");

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
                else{
                    long id = Helper.insertData(path,caption);
                    finish();
                }

            }
        });

    }


        public void process(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                imageFile = null;
                try{
                    imageFile = createFile();
                }catch (IOException e){
                    //ERROR OCCURRED
                }
                if(imageFile!=null){
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(imageFile));
                    startActivityForResult(intent, 0);
                }
            }


        }

        public File createFile() throws IOException{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getStorageDir("Photos");
            File image = File.createTempFile(imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );
            return image;
        }

        public File getStorageDir(String albumName){
            File file = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    albumName);
            if (!file.mkdirs()) {
                Log.e("", "Directory not created");
            }
            return file;
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        path = imageFile.getAbsolutePath().toString();
                    }

                    else{
                        Toast.makeText(this,"Not saved",Toast.LENGTH_LONG).show();
                    }
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this,"Not saved",Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }
    public void viewDetails(View view){
        String data = Helper.getAllData();
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.uninstall) {
            Intent uninstall = new Intent(Intent.ACTION_DELETE);
            uninstall.setData(Uri.parse("package:" + "com.example.kuldeep.photonotes"));
            startActivity(uninstall);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
