package DownloadVideo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DownloadDBHelper extends SQLiteOpenHelper {
    public DownloadDBHelper(Context context) {
        super(context, "download.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, " + "start_pos integer, end_pos integer, compelete_size integer,url varchar(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
