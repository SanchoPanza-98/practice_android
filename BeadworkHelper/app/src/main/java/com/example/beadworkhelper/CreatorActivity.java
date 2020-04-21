package com.example.beadworkhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.beadworkhelper.database.DataBaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreatorActivity extends Activity  {

    private ImageView imageView;
    private TextView debugText;
    private EditText nameWork, schemeLink;
    private Button selectPic,addw;
    private Spinner stateWork;
    private final int PICK_IMAGE = 1;
    private String pathToPicture;
    private int positionState;

    private String[] data = {"Ждет разработки ", "В разработке", "Готово", "Готово.Оставила себе", "Готово.Подарила"};
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added);
        debugText =findViewById(R.id.selectionPicture);
        imageView = findViewById(R.id.picture);
        nameWork = findViewById(R.id.nameWork);
        schemeLink =findViewById(R.id.enterSchemeLink);
        selectPic =findViewById(R.id.selectPicture);
        addw = findViewById(R.id.addMaterialView);
        stateWork=findViewById(R.id.selectStateWork);
        dbHelper = new DataBaseHelper(this);

        // адаптер для спинера stateWork
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateWork.setAdapter(adapter);
        stateWork.setSelection(2);
        stateWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                positionState = position;
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });
        addw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameWork.getText().toString();
                String scheme = schemeLink.getText().toString();

                debugText.setText(pathToPicture);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(DataBaseHelper.KEY_NAME, name);
                contentValues.put(DataBaseHelper.KEY_LINK_PICTURE, pathToPicture);
                contentValues.put(DataBaseHelper.KEY_LINK_SCHEME, scheme);
                contentValues.put(DataBaseHelper.KEY_NOTES, "");
                contentValues.put(DataBaseHelper.KEY_STATE, data[positionState]);


                database.insert(DataBaseHelper.TABLE_WORKS,null,contentValues);

                dbHelper.close();
                Intent intent = new Intent(CreatorActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Обрабатываем результат выбора в галерее:
    @RequiresApi(api = Build.VERSION_CODES.Q)
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
                    imageView.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}





