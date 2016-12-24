package com.hrc.administrator.application;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    private final String DATABASE_PATH=android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/dictionary";
    private AutoCompleteTextView actvWord;
    private Button btnSelectWord;
    private final String DATABASE_FILENAME="dictionary.db";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=openDatabase();
        btnSelectWord=(Button)findViewById(R.id.btn);
        actvWord=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        btnSelectWord.setOnClickListener(this);
        actvWord.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Cursor cursor=database.rawQuery("select english as _id from t_words where english like ?",new String[]{s.toString()+"%"});
        DictionaryAdapter dictionaryAdapter=new DictionaryAdapter(this,cursor,true);
        actvWord.setAdapter(dictionaryAdapter);
    }

    @Override
    public void onClick(View v) {
        String sql="select chinese from t_words where english=?";
        Cursor cursor=database.rawQuery(sql,new String[]{actvWord.getText().toString()});
        String result="未找到该单词";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            result=cursor.getString(cursor.getColumnIndex("chinese"));
        }
        new AlertDialog.Builder(this).setTitle("查询结果").setMessage(result).setPositiveButton("关闭",null).show();
    }

    public class DictionaryAdapter extends CursorAdapter{
        private LayoutInflater inflater;

        @Override
        public CharSequence convertToString(Cursor cursor) {
            return cursor==null?"":cursor.getString(cursor.getColumnIndex("_id"));
        }

        public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public void setView(View view,Cursor cursor){
            TextView tv=(TextView)view;
            tv.setText(cursor.getString(cursor.getColumnIndex("_id")));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view=inflater.inflate(R.layout.word_list_item,null);
            setView(view,cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            setView(view,cursor);
        }
    }

    private SQLiteDatabase openDatabase(){
        try{
            String databaseFilename=DATABASE_PATH+"/"+DATABASE_FILENAME;
            File dir=new File(DATABASE_PATH);
            if(!dir.exists()){
                Log.d("openDatabse","第一个if语句");
                dir.mkdir();
            }
            if(!(new File(databaseFilename)).exists()){
                Log.d("openDatabase","第二个if语句1");
                InputStream is=getResources().openRawResource(R.raw.dictionary);
                Log.d("openDatabase","databaseFilename:"+databaseFilename);
                Log.d("openDatabase","是否存在文件："+(new File(databaseFilename)).exists());
                FileOutputStream fos=new FileOutputStream(databaseFilename);
                Log.d("openDatabase","第二个if语句3");
                byte[] buffer=new byte[8192];
                int count=0;
                while((count=is.read(buffer))>0){
                    fos.write(buffer,0,count);
                }
                Log.d("openDatabase","第二个if语句4");
                fos.flush();
                fos.close();
                is.close();
                Log.d("openDatabase","数据全部载入");
            }
            SQLiteDatabase database= SQLiteDatabase.openOrCreateDatabase(databaseFilename,null);
            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
