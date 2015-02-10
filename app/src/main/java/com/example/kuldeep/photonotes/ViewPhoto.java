package com.example.kuldeep.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;


public class ViewPhoto extends Activity {

    TextView textView;
    ImageView imageView;
    String name,path;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        textView = (TextView) findViewById(R.id.caption);
        imageView = (ImageView) findViewById(R.id.imageView);
        pos = getIntent().getExtras().getInt("num");
        query();
        textView.setText(""+name);
        File img = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;  // Experiment with different sizes
        Bitmap b = BitmapFactory.decodeFile(img.getAbsolutePath());
        imageView.setImageBitmap(b);
    }

    private void query(){
        SQLiteDatabase database = new VivzHelper(this).getWritableDatabase();
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {VivzHelper.UID,VivzHelper.NAME, VivzHelper.PATH};
        Cursor cursor = database.query(VivzHelper.TABLE_NAME,resultColumns,where,whereArgs,groupBy,having,order);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(VivzHelper.UID));

            if(pos+1==id){
                name = cursor.getString(1);
                path=  cursor.getString(2);
                break;
            }
            Log.d("PHOTOS", String.format("%s,%s", id, name));

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_photo, menu);
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
