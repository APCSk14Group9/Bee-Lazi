package cs300.beelazi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

import cs300.beelazi.Model.TransferEventItem;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BeeLaziDatabase.db";
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Events Table
        db.execSQL("create table Events(ID INTEGER PRIMARY KEY, eventName VARCHAR," +
                "startHour INTEGER, startMinute INTEGER, startDay INTEGER, startMonth INTEGER, startYear INTEGER, " +
                "endHour INTEGER, endMinute INTEGER, endDay INTEGER, endMonth INTEGER, endYear INTEGER)");
    }


    public boolean insertEvents(int id, String title, TransferEventItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id+1);
        contentValues.put("eventName", title);
        contentValues.put("startHour", item.getsHour());
        contentValues.put("startMinute", item.getsMinute());
        contentValues.put("startDay", item.getsDay());
        contentValues.put("startMonth", item.getsMonth());
        contentValues.put("startYear", item.getsYear());
        contentValues.put("endHour", item.geteHour());
        contentValues.put("endMinute", item.geteMinute());
        contentValues.put("endDay", item.geteDay());
        contentValues.put("endMonth", item.geteMonth());
        contentValues.put("endYear", item.geteYear());
        db.insert("Events", null, contentValues);
        return true;
    }

    public ArrayList<Pair<String,TransferEventItem>> getAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Pair<String,TransferEventItem>> list = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM Events", null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            String title = res.getString(res.getColumnIndex("eventName"));

            int id = res.getInt(res.getColumnIndex("ID"));

            int startHour = res.getInt(res.getColumnIndex("startHour")),
                    startMinute = res.getInt(res.getColumnIndex("startMinute")),
                    startDay = res.getInt(res.getColumnIndex("startDay")),
                    startMonth = res.getInt(res.getColumnIndex("startMonth")),
                    startYear = res.getInt(res.getColumnIndex("startYear")),
                    endHour = res.getInt(res.getColumnIndex("endHour")),
                    endMinute = res.getInt(res.getColumnIndex("endMinute")),
                    endDay = res.getInt(res.getColumnIndex("endDay")),
                    endMonth = res.getInt(res.getColumnIndex("endMonth")),
                    endYear = res.getInt(res.getColumnIndex("endYear"));

            TransferEventItem item = new TransferEventItem(id, startHour, startMinute, startDay, startMonth, startYear,
                    endHour, endMinute, endDay, endMonth, endYear);

            list.add(new Pair<String, TransferEventItem>(title, item));
            res.moveToNext();
        }
        res.close();
        return list;
    }

    public int getMaxId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MAX(ID) FROM Events", null);
        c.moveToFirst();
        int ans = c.getInt(0);
        c.close();
        return ans;
    }

    public Integer deleteEvents(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Abc", String.valueOf(id));
        return db.delete("Events", "ID = ?", new String[] {Integer.toString(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteAllEvents(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Events");
    }


}
