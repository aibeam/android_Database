package com.example.sampledatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText dbText, tableText;
    TextView textView;

    DatabaseHelper dbHelper;
    SQLiteDatabase sqlDatabase;
    String tableName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbText = findViewById(R.id.editText);
        tableText = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);

        Button btnDB = findViewById(R.id.button);
        Button btnTable = findViewById(R.id.button2);
        Button btnSrch = findViewById(R.id.button3);

        btnDB.setOnClickListener(v->{
            createDB(dbText.getText().toString());
        });

        btnTable.setOnClickListener(v->{
            tableName = tableText.getText().toString();
            createTable(tableName);
            insertRecord();
        });

        btnSrch.setOnClickListener(v->{
            executeQuery();//결과값 반영 X
        });


    }

    private void executeQuery() {
        if (sqlDatabase == null) {
            Toast.makeText(this, "데이터베이스를 먼저 연결하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        println("executeQuery 호출됨.");
        Cursor cursor = sqlDatabase.rawQuery("select _id, name, age, mobile " +
                "from emp ", null);
        int recordCnt = cursor.getCount();
        println(recordCnt+"개");
        for (int i = 0; i < recordCnt; i++){
            cursor.moveToNext();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            String mobile = cursor.getString(3);
            println("레코드#" + i +":"+id+","+name+","+age+","+mobile);
        }
        cursor.close();
    }


    private void createDB(String name){
        println("createDatabase 호출됨.");
        dbHelper = new DatabaseHelper(this);
        sqlDatabase = dbHelper.getWritableDatabase();
        println("데이터베이스 생성함."+ name);

    }

    private void createTable(String name){
        if (sqlDatabase == null) {
            Toast.makeText(this, "데이터베이스를 먼저 만드세요", Toast.LENGTH_SHORT).show();
            return;
        }
        sqlDatabase.execSQL("create table if not exists emp " +
                " ( _id integer PRIMARY KEY autoincrement, " +
                " name text, " +
                " age integer, " +
                " mobile text)");

    }

    private void insertRecord(){
        println("insertRecord 호출됨.");
        if (sqlDatabase == null) {
            Toast.makeText(this, "데이터베이스를 먼저 만드세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tableName == null) {
            Toast.makeText(this, "테이블을 먼저 만드세요", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < 5; i++) {
            sqlDatabase.execSQL("insert into " + tableName +
                    "(name, age, mobile) " +
                    "values (" +
                    "'John" + i + "'," +
                    i + "," +
                    "'010-1111-111" + i +
                    "')");

            println("레코드 추가함.");
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
}