import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class siswaAdapter extends CursorAdapter {
    public siswaAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.siswa_listview_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }
}
