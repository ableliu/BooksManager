package win.aspring.bookrxandroid.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import win.aspring.bookrxandroid.AppConfig;

/**
 * 数据库帮助类
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 13:29
 */
public class LocalDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "book.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CATEGORY = "CREATE TABLE " + AppConfig.TABLE_CATEGORY + " (" +
            AppConfig.ID + BOOLEAN_TYPE + " PRIMARY KEY," +
            AppConfig.CATEGORY_ID + TEXT_TYPE + COMMA_SEP +
            AppConfig.CNAME + TEXT_TYPE + COMMA_SEP +
            AppConfig.DESC + TEXT_TYPE + COMMA_SEP +
            AppConfig.CREATE_TIME + TEXT_TYPE + COMMA_SEP +
            AppConfig.UPDATE_TIME + TEXT_TYPE +
            ")";

    private static final String SQL_CREATE_BOOK = "CREATE TABLE " + AppConfig.TABLE_BOOK + " (" +
            AppConfig.ID + BOOLEAN_TYPE + " PRIMARY KEY," +
            AppConfig.CATEGORY_ID + TEXT_TYPE + COMMA_SEP +
            AppConfig.BOOK_CODE + TEXT_TYPE + COMMA_SEP +
            AppConfig.BOOK_NAME + TEXT_TYPE + COMMA_SEP +
            AppConfig.PUBLISHER + TEXT_TYPE + COMMA_SEP +
            AppConfig.AUTHOR + TEXT_TYPE + COMMA_SEP +
            AppConfig.STATE_LIBRARY + BOOLEAN_TYPE + COMMA_SEP +
            AppConfig.BORROW_PEOPLE + TEXT_TYPE + COMMA_SEP +
            AppConfig.BORROW_PHONE + TEXT_TYPE + COMMA_SEP +
            AppConfig.BORROW_TIME + TEXT_TYPE + COMMA_SEP +
            AppConfig.BACK_TIME + TEXT_TYPE + COMMA_SEP +
            AppConfig.CREATE_TIME + TEXT_TYPE + COMMA_SEP +
            AppConfig.UPDATE_TIME + TEXT_TYPE +
            ")";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORY);
        db.execSQL(SQL_CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
