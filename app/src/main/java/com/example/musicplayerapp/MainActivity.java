package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // هنضيف اذن الوصول الي الاغاني في الموبايل

    }
    public void runtimePermission(){

        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

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
        for (File singlefile: files ){
            // check if singlefile --> directory or not
            // check if directory hidden or not

            if(singlefile.isDirectory() && !singlefile.isHidden()){
                // if true -->  move to arraylist to check from songs in this file
                arrayList.addAll(findSong(singlefile));
            }
            else{
                // check if file we get mp3 file or wav file or not
                if(singlefile.getName().endsWith(".mp3")
                        ||singlefile.getName().endsWith(".wav")){
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
    }
}