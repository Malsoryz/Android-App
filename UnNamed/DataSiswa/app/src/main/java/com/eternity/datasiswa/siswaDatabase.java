package com.eternity.datasiswa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class siswaDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "siswa.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_MAIN_TABLE =
            "CREATE TABLE siswa("+
                    "siswaId INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "nama VARCHAR(100), "+
                    "nilaiHarian INTEGER, "+
                    "nilaiTugas INTEGER, "+
                    "nilaiUlangan INTEGER)";

    public siswaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addSiswa(String nama, int harian, int tugas, int akhir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues siswa = new ContentValues();
        siswa.put("nama",nama);
        siswa.put("nilaiHarian",harian);
        siswa.put("nilaiTugas",tugas);
        siswa.put("nilaiUlangan",akhir);
        db.insert("siswa",null,siswa);
        db.close();
    }

    public void deleteSiswa(int siswaID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("siswa","siswaId = ?",new String[]{String.valueOf(siswaID)});
    }

    public void updateSiswa(String nama, int harian, int tugas, int ulangan, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updates =new ContentValues();
        updates.put("nama",nama);
        updates.put("nilaiHarian",harian);
        updates.put("nilaiTugas",tugas);
        updates.put("nilaiUlangan",ulangan);
        db.update("siswa",updates,"siswaId = ?",new String[]{String.valueOf(id)});
    }

    public Cursor selectSiswa() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT siswaId as _id, nama, nilaiHarian, nilaiTugas, nilaiUlangan FROM siswa",null);
    }

    public boolean isSiswaEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM siswa",null);
        boolean isEmpty = false;
        if (cursor != null) {
            cursor.moveToFirst();
            isEmpty = cursor.getInt(0) == 0;
            cursor.close();
        }
        return isEmpty;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS siswa");
        onCreate(db);
    }
}
