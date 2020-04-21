package com.example.beadworkhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.beadworkhelper.database.DataBaseHelper;

public class SchemeViewerActivity extends Activity {

    private WebView schemeViewer;
    private EditText editText;
    private Button save;

    private int idWork;
    private String link,notes;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheme_view);

        Intent intent = getIntent();
        idWork = intent.getIntExtra("id",0);
        link = intent.getStringExtra("link");
        notes = intent.getStringExtra("notes");
        dbHelper = new DataBaseHelper(this);
        database = dbHelper.getWritableDatabase();


        schemeViewer = findViewById(R.id.schemeView);
        editText = findViewById(R.id.schemeNotes);
        save = findViewById(R.id.saveNotes);


        editText.setText(notes);
        schemeViewer.loadUrl(link);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTableWorks(idWork,editText.getText().toString());
                Intent intent1 = new Intent(SchemeViewerActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    public void updateTableWorks(int id,String notes){
        database.execSQL("update "+DataBaseHelper.TABLE_WORKS+" set "
                + DataBaseHelper.KEY_NOTES + " = '" + notes + "'" +" where "
                + DataBaseHelper.KEY_ID + " = "+id);

        dbHelper.close();

    }
}
