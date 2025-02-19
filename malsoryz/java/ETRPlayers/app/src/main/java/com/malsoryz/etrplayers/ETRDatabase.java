package com.malsoryz.etrplayers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ETRDatabase extends SQLiteOpenHelper {

    public static final String VIDEO_TABLE_NAME = "video";
    public static final String VIDEO_COLUMN_ID = "_id";
    public static final String VIDEO_COLUMN_TITLE = "title";
    public static final String VIDEO_COLUMN_CREATOR = "creator";
    public static final String VIDEO_COLUMN_DESC = "videodesc";
    public static final String VIDEO_COLUMN_PATH = "path";
    public static final String VIDEO_COLUMN_THUMBNAIL = "thumbnail";
    public static final String[] VIDEO_COLUMN_ALL = {
            VIDEO_COLUMN_ID,
            VIDEO_COLUMN_TITLE,
            VIDEO_COLUMN_CREATOR,
            VIDEO_COLUMN_DESC,
            VIDEO_COLUMN_PATH,
            VIDEO_COLUMN_THUMBNAIL
    };

    public static final String HISTORY_TABLE_NAME = "history";
    public static final String HISTORY_COLUMN_ID = "_id";
    public static final String HISTORY_COLUMN_LAST_PLAY_AT = "lastPlayed";
    public static final String[] HISTORY_COLUMN_ALL = {
            HISTORY_COLUMN_ID,
            HISTORY_COLUMN_LAST_PLAY_AT
    };

    private static final String CREATE_VIDEO_TABLE =
            "CREATE TABLE " + VIDEO_TABLE_NAME + "(" +
            VIDEO_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            VIDEO_COLUMN_TITLE + " VARCHAR(255), " +
            VIDEO_COLUMN_CREATOR + " VARCHAR(255), " +
            VIDEO_COLUMN_DESC + " TEXT, " +
            VIDEO_COLUMN_PATH + " INTEGER, " +
            VIDEO_COLUMN_THUMBNAIL + " BLOB);";

    private static final String CREATE_HISTORY_TABLE =
            "CREATE TABLE " + HISTORY_TABLE_NAME + "(" +
            HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY, "+
            HISTORY_COLUMN_LAST_PLAY_AT + " INTEGER);";

    public ETRDatabase(@Nullable Context context) {
        super(context, "etrpdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VIDEO_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public void addVideo(Integer id, String title, String creator, String videoDesc, Integer path, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        byte[] thumbnail = bitmapToByteArray(bitmap);

        ContentValues values = new ContentValues();
        values.put(VIDEO_COLUMN_ID, id);
        values.put(VIDEO_COLUMN_TITLE, title);
        values.put(VIDEO_COLUMN_CREATOR, creator);
        values.put(VIDEO_COLUMN_DESC, videoDesc);
        values.put(VIDEO_COLUMN_PATH, path);
        values.put(VIDEO_COLUMN_THUMBNAIL, thumbnail);
        db.insert(VIDEO_TABLE_NAME, null, values);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Cursor getVideoList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + VIDEO_TABLE_NAME, null);
    }

    public Cursor getVideoListWithExclude(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + VIDEO_TABLE_NAME + " WHERE " + VIDEO_COLUMN_ID + " != " + id, null);
    }

    public void addHistory(Integer videoId, long lastPlayed) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(HISTORY_COLUMN_ID, videoId);
        values.put(HISTORY_COLUMN_LAST_PLAY_AT, lastPlayed);
        db.insertWithOnConflict(HISTORY_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HISTORY_TABLE_NAME);
    }

    public Cursor getHistoryList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "SELECT " +
                        HISTORY_TABLE_NAME + ".*, " +
                        VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_TITLE + ", " +
                        VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_CREATOR + ", " +
                        VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_DESC + ", " +
                        VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_PATH + ", " +
                        VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_THUMBNAIL +
                        " FROM " + HISTORY_TABLE_NAME + ", " + VIDEO_TABLE_NAME +
                        " WHERE " + VIDEO_TABLE_NAME + "." + VIDEO_COLUMN_ID + " = " + HISTORY_TABLE_NAME + "." + HISTORY_COLUMN_ID + " ;";
        return db.rawQuery(query, null);
    }

    public void recreateDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        db.execSQL(CREATE_VIDEO_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
        db.close();
    }

    public static Bitmap getFrameFromVideo(Context context, int resId, long timeUs) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            Uri videoUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
            retriever.setDataSource(context, videoUri);
            return retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (IllegalArgumentException | SecurityException e) {
            throw new RuntimeException(e);
        } finally {
            retriever.release();
        }
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
