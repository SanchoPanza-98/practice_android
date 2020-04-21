package com.example.beadworkhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beadworkhelper.database.DataBaseHelper;
import com.example.beadworkhelper.database.Material;
import com.example.beadworkhelper.database.Work;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    private Work work;
    private ArrayList<String> materialsAdapter;


    private Button update,open;
    private TextView name,state;
    private ImageView picture;
    private ListView materials;

    private int idWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Intent intent = getIntent();
        idWork = intent.getIntExtra("id",0);
        dbHelper = new DataBaseHelper(this);
        database = dbHelper.getWritableDatabase();

        update = findViewById(R.id.updateInfoAboutWork);
        open = findViewById(R.id.openScheme);
        state = findViewById(R.id.detailStateWork);
        name = findViewById(R.id.detailNameWork);
        picture =  findViewById(R.id.detailPictureWork);
        materials = findViewById(R.id.listMaterials);

        materialsAdapter = new ArrayList<>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, materialsAdapter);
        // Привяжем массив через адаптер к ListView
        materials.setAdapter(adapter);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(work!=null){
                    Intent intent = new Intent(DetailsActivity.this, SchemeViewerActivity.class);
                    intent.putExtra("id",work.get_id());
                    intent.putExtra("link",work.getLinkScheme());
                    intent.putExtra("notes",work.getNotes());
                    startActivity(intent);
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(work!=null){
                    Intent intent = new Intent(DetailsActivity.this, UpdateActivity.class);
                    intent.putExtra("id",work.get_id());
                    startActivity(intent);
                }
            }
        });
        work = getWorkById(idWork);
        showDetailsWork(work);
    }


    public void showDetailsWork(Work work){
        if(work!=null) {
            name.setText(work.getName());
            state.setText(work.getState());
            final Uri imageUri = Uri.parse(work.getLinkPicture());
            //Toast.makeText(this, "URI  " + imageUri, Toast.LENGTH_LONG).show();
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                picture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    public Work getWorkById(int id){
        String selection = "_id = ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        String selection2 = "work_id = ?";
        Cursor cursor = database.query(DataBaseHelper.TABLE_WORKS, null, selection, selectionArgs, null, null, null);
        Cursor cursor2 = database.query(DataBaseHelper.TABLE_MATERIALS,null,selection2,selectionArgs,null,null,null);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
            int linkPictureIndex = cursor.getColumnIndex(DataBaseHelper.KEY_LINK_PICTURE);
            int linkSchemeIndex = cursor.getColumnIndex(DataBaseHelper.KEY_LINK_SCHEME);
            int notesIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NOTES);
            int stateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_STATE);
            List<Material> materials = new ArrayList<>();

            int idMaterialIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_ID);
            int nameMaterialIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_NAME);
            int descriptionIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_DESCRIPTION);
            int quantityIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_QUANTITY);
            int priceIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_PRICE);
            int workIdIndex = cursor2.getColumnIndex(DataBaseHelper.MATERIAL_KEY_WORK_ID);
            while (cursor2.moveToNext()){
                Material material =new Material(cursor2.getInt(idMaterialIndex)
                        ,cursor2.getString(nameMaterialIndex)
                        ,cursor2.getString(descriptionIndex)
                        ,cursor2.getInt(quantityIndex)
                        ,cursor2.getInt(priceIndex)
                        ,cursor2.getInt(workIdIndex));
                materials.add(material);
                materialsAdapter.add(""+material.getName()+" "+material.getDescription()+
                        " "+material.getQuantity()+" "+material.getPrice()+"\n");
            }
            Work result = new Work(cursor.getInt(idIndex)
                    ,cursor.getString(nameIndex)
                    ,cursor.getString(linkPictureIndex)
                    ,cursor.getString(linkSchemeIndex)
                    ,cursor.getString(notesIndex)
                    ,cursor.getString(stateIndex)
                    ,materials);
            cursor.close();
            cursor2.close();
            return result;
        }
        cursor.close();
        cursor2.close();
        return null;
    }
}
