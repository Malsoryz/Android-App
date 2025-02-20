package com.malsoryz.etrplayers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends CursorAdapter {

    public HistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_history_items, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView viewThumbnail = view.findViewById(R.id.thumbnail);
        TextView viewTitle = view.findViewById(R.id.videoTitle);
        TextView viewLastPlayed = view.findViewById(R.id.videoLastPlayed);

        byte[] byteThumbnail = cursor.getBlob(cursor.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_THUMBNAIL));
        String displayLastPlayed = "Last played: ";
        long microSeconds = cursor.getLong(cursor.getColumnIndexOrThrow(ETRDatabase.HISTORY_COLUMN_LAST_PLAY_AT));
        long totalSeconds = microSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        @SuppressLint("DefaultLocale") String formatedLastPlayedTime = String.format("%02d:%02d", minutes, seconds);

        viewThumbnail.setImageBitmap(BitmapFactory.decodeByteArray(byteThumbnail, 0, byteThumbnail.length));
        viewTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_TITLE)));
        viewLastPlayed.setText(displayLastPlayed + formatedLastPlayedTime);
    }
}
