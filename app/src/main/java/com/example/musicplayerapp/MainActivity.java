package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicplayerapp.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ListView listView;
    String[] items;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       listView= (ListView) findViewById(R.id.listviewSong);

        runtimePermission();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
    }   // هنضيف اذن الوصول الي الاغاني في الموبايل

    public void runtimePermission(){

        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                        Log.i(TAG, "onPermissionGranted: ok" );

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }
    //find songs from external storage unite
    public ArrayList<File> findSong(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        // for loop for check for all files 1- mp3file or  2-folder
       // if(files != null) {


    //    if (files != null) {
            for (File singlefile: files ){
                // check if singlefile --> directory or not
                // check if directory hidden or not

                if(singlefile.isDirectory() && !singlefile.isHidden()){
                    // if true -->  move to arraylist to check from songs in this file
                    arrayList.addAll(findSong(singlefile));
                }
                else{
                    //
                    // check if file we get mp3 file or wav file or not
                    if(singlefile.getName().endsWith(".mp3")){
                        arrayList.add(singlefile);
                    } else if (singlefile.getName().endsWith(".wav")){
                        // if this happened we will add this file(mp3 0r wav) to arraylist
                        arrayList.add(singlefile);
                    }
                }

            }


        return arrayList;
    }

    // anther way to show music inside listview
    void displaySongs(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        // i will call items of array
        items= new String[mySongs.size()];
        // now create for loop for storing all songs in items
        for (int i=0; i<mySongs.size();i++){
            items[i]=mySongs.get(i).getName().toString().replace(".mp3" ,
                    " ").replace(".wav",""); //pass mp3 and wav with null
        }
        //ArrayAdapter<String>  myAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        //binding.listviewSong.setAdapter(myAdapter);
        //listView.setAdapter(myAdapter);
        customAdapter adapter=new customAdapter();
        listView.setAdapter(adapter);
    }
    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
         View view1=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView txtSong=view1.findViewById(R.id.textSong);
            txtSong.setSelected(true);
            txtSong.setText(items[i]);
            return view;
        }
    }
}