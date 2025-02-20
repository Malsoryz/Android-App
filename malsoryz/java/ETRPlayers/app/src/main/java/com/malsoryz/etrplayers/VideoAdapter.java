package com.malsoryz.etrplayers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends CursorAdapter {

    private TextView viewTitle, viewCreator;
    private ImageView viewThumbnail;
    public VideoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_video_items, parent, false);
    }

    private void init(View view) {
        viewThumbnail = view.findViewById(R.id.thumbnail);
        viewTitle = view.findViewById(R.id.videoTitle);
        viewCreator = view.findViewById(R.id.videoCreator);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        init(view);
        byte[] byteThumbnail = cursor.getBlob(cursor.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_THUMBNAIL));
        viewThumbnail.setImageBitmap(BitmapFactory.decodeByteArray(byteThumbnail, 0, byteThumbnail.length));
        viewTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_TITLE)));
        viewCreator.setText(cursor.getString(cursor.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_CREATOR)));
    }
}
