package com.example.beadworkhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.beadworkhelper.database.DataBaseHelper;
import com.example.beadworkhelper.database.Material;
import com.example.beadworkhelper.database.Work;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends Activity {


    private EditText nameWork,schemeLink,nameMaterial,description,quantity,price;
    private Button selectPicture,addMaterial,updateOk;
    private Spinner stateWork;
    private ListView materials;
    private ImageView picture;

    private final int PICK_IMAGE = 1;
    private String[] data = {"Ждет разработки ", "В разработке", "Готово", "Готово.Оставила себе", "Готово.Подарила"};
    private String pathToPicture;
    private int positionState;
    private int idWork;
    private Work work;
    private ArrayList<String> materialsAdapter;


    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        Intent intent = getIntent();
        idWork = intent.getIntExtra("id",0);
        dbHelper = new DataBaseHelper(this);
        database = dbHelper.getWritableDatabase();

        nameWork = findViewById(R.id.nameWork);
        schemeLink = findViewById(R.id.enterSchemeLink);
        nameMaterial = findViewById(R.id.nameMaterial);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        selectPicture = findViewById(R.id.selectPicture);
        addMaterial = findViewById(R.id.addMaterial);
        updateOk = findViewById(R.id.updateOk);
        stateWork = findViewById(R.id.selectStateWork);
        materials = findViewById(R.id.materials);
        picture = findViewById(R.id.picture);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateWork.setAdapter(adapter);

        materialsAdapter = new ArrayList<>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> adapterMat = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, materialsAdapter);
        // Привяжем массив через адаптер к ListView
        materials.setAdapter(adapterMat);

        work = getWorkById(idWork);
        showDetailsWork(work);

        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMaterial();
            }
        });
        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });
        updateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTableWorks(idWork);
                Intent intent1 = new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
        stateWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                positionState = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void showDetailsWork(Work work){
        if(work!=null) {
            nameWork.setText(work.getName());
            schemeLink.setText(work.getLinkScheme());
            positionState = getPositionByName(work.getState());
            stateWork.setSelection(positionState);
            final Uri imageUri = Uri.parse(work.getLinkPicture());
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                picture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPositionByName(String name){
        for (int i = 0; i<data.length;i++) {
            if(data[i].equals(name)){
                return i;
            }
        }
        return -1;
    }


    public void insertMaterial(){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.MATERIAL_KEY_NAME, nameMaterial.getText().toString());
        contentValues.put(DataBaseHelper.MATERIAL_KEY_DESCRIPTION, description.getText().toString());
        contentValues.put(DataBaseHelper.MATERIAL_KEY_QUANTITY, Integer.parseInt(quantity.getText().toString()));
        contentValues.put(DataBaseHelper.MATERIAL_KEY_PRICE,Integer.parseInt(price.getText().toString()) );
        contentValues.put(DataBaseHelper.MATERIAL_KEY_WORK_ID, idWork);

        database.insert(DataBaseHelper.TABLE_MATERIALS,null,contentValues);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    pathToPicture = imageUri.toString();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    picture.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateTableWorks(int id){
        String state = data[positionState];
        database.execSQL("update "+DataBaseHelper.TABLE_WORKS+" set "
                + DataBaseHelper.KEY_NAME + " = '" + nameWork.getText().toString()+ "', "
                + DataBaseHelper.KEY_LINK_PICTURE + " = '" + pathToPicture+ "', "
                + DataBaseHelper.KEY_LINK_SCHEME + " = '" + schemeLink.getText().toString()+ "', "
                + DataBaseHelper.KEY_STATE + " = '" + state+ "' "
                +" where "
                + DataBaseHelper.KEY_ID + " = "+id);

        dbHelper.close();

    }
}
