package com.sbiitju.smartremainder.SqliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CourseDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="database";
    public static final int VERSON=1;
    public static final String TABLE_NAME="t";
    public static final String CID="tableid";
    public static final String ID="tablename";
    public static final String SHOW="select * from "+TABLE_NAME;
    public static final String CREATE_TABLE="create table "+TABLE_NAME+"("+CID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ID+" text);";


    private Context context;
    public CourseDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSON);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Toast.makeText(context,"OnCreate is called",Toast.LENGTH_LONG);

            db.execSQL(CREATE_TABLE);

        } catch (Exception e){
            Toast.makeText(context,"Exception : "+e,Toast.LENGTH_LONG);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        try{
            Toast.makeText(context,"OnUpgrade is called",Toast.LENGTH_LONG);

            db.execSQL("drop table if exists "+TABLE_NAME);
            onCreate(db);

        }catch (Exception e){

            Toast.makeText(context,"Exception : "+e,Toast.LENGTH_LONG);

        }

    }

    public long insert(String a) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID,a);
        long rowid=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowid;
    }
    public Cursor show(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
    public boolean update(int id,String name){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CID,id);
        contentValues.put(ID,name);
        sqLiteDatabase.update(TABLE_NAME,contentValues,CID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public Integer delet(String deletid) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        // return sqLiteDatabase.delete(TABLE_NAME, CID+" = ?",new String[]{ deletid });
        return sqLiteDatabase.delete(TABLE_NAME,CID + " = ? ",new String[]{ deletid });
    }
    public  Cursor search(String text){
        SQLiteDatabase database=this.getReadableDatabase();
        String query="Select * from "+TABLE_NAME+" WHERE "+ID+" lIKE '%"+text+"%'";
        Cursor cursor=database.rawQuery(query,null);
        return cursor;
    }
}
