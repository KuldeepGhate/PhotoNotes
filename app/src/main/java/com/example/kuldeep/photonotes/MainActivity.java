package com.example.kuldeep.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    DatabaseAdapter Helper;
    private SQLiteDatabase dataBase;

    ArrayList<String> arr = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        query();

        list = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ViewPhoto.class);
                intent.putExtra("num",position);
                startActivity(intent);
            }
        });
    }
    private void query(){
        dataBase = new VivzHelper(this).getWritableDatabase();
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;
        String[] resultColumns = {VivzHelper.UID,VivzHelper.NAME,VivzHelper.PATH};
        Cursor cursor = dataBase.query(VivzHelper.TABLE_NAME, resultColumns, null, null, null, null, null);

        while (cursor.moveToNext()){
            int uid = cursor.getInt(cursor.getColumnIndex(VivzHelper.UID));
            String name = cursor.getString(1);
            if(!arr.contains(name)){
                arr.add(name);
            }
        }
    }

    @Override
    protected void onResume() {
        query();
        list = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        list.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            final Intent intent = new Intent(MainActivity.this, AddPhoto.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.uninstall){
            Intent uninstall = new Intent(Intent.ACTION_DELETE);
            uninstall.setData(Uri.parse("package:" + "com.example.kuldeep.photonotes"));
            startActivity(uninstall);
        }
        return super.onOptionsItemSelected(item);
    }
}
