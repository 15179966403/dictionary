package com.hrc.administrator.application;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ParentActivity extends Activity{
    protected final String DATABASE_PATH=android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/dictionary";
    protected final String DATABASE_FILENAME="dictionary.db";
    protected SQLiteDatabase database;
    protected SQLiteDatabase opendatabase(){
        try{
            String databaseFilename=DATABASE_PATH+"/"+DATABASE_FILENAME;
            File dir=new File(DATABASE_PATH);
            if(!dir.exists()){
                dir.mkdir();
            }
            if(!(new File(databaseFilename)).exists()){
                InputStream is=getResources().openRawResource(R.raw.dictionary);
                FileOutputStream fos=new FileOutputStream(databaseFilename);
                byte[] buffer=new byte[8192];
                int count=0;
                while((count=is.read(buffer))>0){
                    fos.write(buffer,0,count);
                }
                fos.flush();
                fos.close();
                is.close();
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
