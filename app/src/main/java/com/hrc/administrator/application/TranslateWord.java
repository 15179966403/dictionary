package com.hrc.administrator.application;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/25.
 */

public class TranslateWord extends ParentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView=(TextView)getLayoutInflater().inflate(R.layout.word_list_item,null);
        textView.setTextColor(Color.WHITE);
        if(getIntent().getData()!=null){
            String word=getIntent().getData().getHost();
            String sql="select chinese from t_words where english=?";
            database=opendatabase();
            Cursor cursor=database.rawQuery(sql,new String[]{word});
            String result="未找到该单词";
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                result=cursor.getString(cursor.getColumnIndex("chinese"));
            }
            textView.setText(result);
        }
        setContentView(textView);
    }
}
