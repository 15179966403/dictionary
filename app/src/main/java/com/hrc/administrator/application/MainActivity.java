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

public class MainActivity extends ParentActivity implements View.OnClickListener,TextWatcher{
    private AutoCompleteTextView actvWord;
    private Button btnSelectWord;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=opendatabase();
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
}
