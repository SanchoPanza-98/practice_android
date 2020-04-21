package com.example.beadworkhelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.beadworkhelper.database.DataBaseHelper;
import com.example.beadworkhelper.database.Work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static int REQUEST_PERMISSIONS = 1;
    DataBaseHelper dbHelper;
    SQLiteDatabase database;
    List<Work> works;
    Button next,prev;
    Button addWork;
    Button detailsWork;
    ImageView imageView2;
    TextView nameWork,stateWork;
    int showPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        addWork =findViewById(R.id.add);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        detailsWork = findViewById(R.id.details);
        nameWork = findViewById(R.id.nameView);
        stateWork = findViewById(R.id.stateView);
        imageView2 =findViewById(R.id.imageView2);

        dbHelper = new DataBaseHelper(this);
        database = dbHelper.getWritableDatabase();
        works = new ArrayList<>();
        showPosition = 0;

        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatorActivity.class);
                startActivity(intent);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(showPosition < works.size()-1){

                    showPosition++;
                    showDataBase(showPosition);
                }
                if(showPosition==works.size()-1){
                    next.setEnabled(false);
                }
                if(showPosition==1){
                    prev.setEnabled(true);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPosition > 0){
                    showPosition--;
                    showDataBase(showPosition);

                }
                if(showPosition == 0)   {
                    prev.setEnabled(false);
                }
                if(showPosition==works.size()-2){
                    next.setEnabled(true);
                }
            }
        });
        detailsWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!works.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("id",works.get(showPosition).get_id());
                    startActivity(intent);
                }
            }
        });
        permission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTableWorkFromDB();
        showDataBase(showPosition);

    }

    public void showDataBase(int position){

        if (!works.isEmpty() && position>=0 && position<=works.size()-1){
            //Toast.makeText(this,"Position "+position,Toast.LENGTH_LONG).show();
            nameWork.setText(works.get(position).getName());
            stateWork.setText(works.get(position).getState());
            final Uri imageUri = Uri.parse(works.get(position).getLinkPicture());
            //Toast.makeText(this, "URI  " + imageUri, Toast.LENGTH_LONG).show();
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView2.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void getTableWorkFromDB(){
        Cursor cursor = database.query(DataBaseHelper.TABLE_WORKS,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            showPosition = 0;
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
            int linkPictureIndex = cursor.getColumnIndex(DataBaseHelper.KEY_LINK_PICTURE);
            int linkSchemeIndex = cursor.getColumnIndex(DataBaseHelper.KEY_LINK_SCHEME);
            int notesIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NOTES);
            int stateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_STATE);
            do{
                works.add(new Work(cursor.getInt(idIndex)
                        ,cursor.getString(nameIndex)
                        ,cursor.getString(linkPictureIndex)
                        ,cursor.getString(linkSchemeIndex)
                        ,cursor.getString(notesIndex)
                        ,cursor.getString(stateIndex)));
            }while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        //dbHelper.close();
    }

    private void permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
