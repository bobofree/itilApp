package DataBaseHelper;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "baseinfo.db";//数据库名称
    private static final int SCHEMA_VERSION = 2;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    public DBHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建的是一个个人信息表  如果存在不必再执行
        db.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, password TEXT, " +
                "type TEXT, " +
                "phone TEXT, " +
                "state TEXT, " +
                "remember TEXT DEFAULT \'no\'," +
                "logintime TIMESTAMP default (datetime(\'now\', \'localtime\')));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            //db.execSQL("ALTER TABLE restaurants ADD phone TEXT;");
        }
    }

    public Cursor getAll(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf = new StringBuilder("SELECT id, name, password, type, phone, state, remember,logintime FROM user");

        if (where != null && "" != orderBy) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy != null && "" != orderBy) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }

        return (getReadableDatabase().rawQuery(buf.toString(), null));
    }

    public Cursor getById(String id) {//根据点击事件获取id,查询数据库
        String[] args = {id};

        return (getReadableDatabase()
                .rawQuery("SELECT id, name, password, type, phone, state, remember,logintime FROM user WHERE id=?",
                        args));
    }

    public void insert(String name, String password, String type, String phone, String state, String remember) {
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("password", password);
        cv.put("type", type);
        cv.put("phone", phone);
        cv.put("state", state);
        cv.put("remember", remember);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put("logintime", format.format(new Date()));

        getWritableDatabase().insert("user", "name", cv);
    }

    public void update(String id, String name, String password, String type, String phone, String state, String remember) {
        ContentValues cv = new ContentValues();
        String[] args = {id};

        cv.put("name", name);
        cv.put("password", password);
        cv.put("type", type);
        cv.put("phone", phone);
        cv.put("state", state);
        cv.put("remember", remember);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put("logintime", format.format(new Date()));

        getWritableDatabase().update("user", cv, "id=?",
                args);
    }

    public void update(String id, String field, String val) {
        ContentValues cv = new ContentValues();
        String[] args = {id};

        cv.put(field, val);

        getWritableDatabase().update("user", cv, "id=?",
                args);
    }

    public String getPhone(Cursor c) {
        return (c.getString(4));
    }
}