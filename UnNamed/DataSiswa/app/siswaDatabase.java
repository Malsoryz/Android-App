import android.content.Context;
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
