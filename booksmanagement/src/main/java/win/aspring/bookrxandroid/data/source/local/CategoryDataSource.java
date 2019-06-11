package win.aspring.bookrxandroid.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import win.aspring.bookrxandroid.AppConfig;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.BaseDataSource;

/**
 * 图书分类
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 16:25
 */
public class CategoryDataSource implements BaseDataSource<Category> {
    private Context context;
    private static CategoryDataSource INSTANCE;

    private LocalDbHelper mHelper;

    private CategoryDataSource(@NonNull Context context) {
        this.context = context;
        mHelper = new LocalDbHelper(context);
    }

    public static CategoryDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CategoryDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * 获取所有分类列表
     *
     * @param callBack
     */
    @Override
    public void getList(@NonNull LoadDataCallBack<Category> callBack) {
        List<Category> cate = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.CNAME,
                AppConfig.DESC,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        Cursor c = db.query(AppConfig.TABLE_CATEGORY, columns, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
                String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

                Category cat = new Category(id, categoryId, cname, desc, createTime, updateTime);
                cate.add(cat);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (cate.isEmpty()) {
            callBack.onDataNotAvailable();
        } else {
            callBack.onListLoaded(cate);
        }
    }

    /**
     * 根据ID获取分类
     *
     * @param id
     * @param callback
     */
    @Override
    public void getData(int id, @NonNull GetDataCallBack<Category> callback) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.CNAME,
                AppConfig.DESC,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        String selection = AppConfig.ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c = db.query(AppConfig.TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null);

        Category cat = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
            String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
            String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
            String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
            String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
            String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

            cat = new Category(Id, categoryId, cname, desc, createTime, updateTime);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (cat == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(cat);
        }
    }

    /**
     * 获取分类
     *
     * @param id
     * @return
     */
    private Category getData(int id) {
        Category cate = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.CNAME,
                AppConfig.DESC,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        String selection = AppConfig.ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c = db.query(AppConfig.TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
            String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
            String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
            String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
            String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
            String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

            cate = new Category(Id, categoryId, cname, desc, createTime, updateTime);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return cate;
    }

    /**
     * 保存分类
     *
     * @param category 分类
     * @param callBack
     */
    @Override
    public void saveData(@NonNull Category category, @NonNull HandlerCallBack callBack) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppConfig.CATEGORY_ID, category.getCategory_id());
        values.put(AppConfig.CNAME, category.getCname());
        values.put(AppConfig.CREATE_TIME, category.getCreate_time());
        values.put(AppConfig.UPDATE_TIME, category.getUpdate_time());

        long l = db.insert(AppConfig.TABLE_CATEGORY, null, values);

        if (l > 0) {
            callBack.onSuccess();
        } else {
            callBack.onFailure();
        }

        db.close();
    }

    /**
     * 根据ID删除分类
     *
     * @param id       ID
     * @param callBack
     */
    @Override
    public void deleteData(int id, @NonNull HandlerCallBack callBack) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String selection = AppConfig.ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Category cate = getData(id);
        if (cate != null) {
            List<Book> books = BookDataSource.getInstance(context).getBooks(cate.getCategory_id());
            if (books == null && books.isEmpty()) {
                int i = db.delete(AppConfig.TABLE_CATEGORY, selection, selectionArgs);
                if (i > 0) {
                    callBack.onSuccess();
                } else {
                    callBack.onFailure();
                }
            } else {
                callBack.onFailure();
            }
        }

        db.close();
    }

    /**
     * 删除所有分类
     *
     * @param callBack
     */
    @Override
    public void deleteAll(@NonNull HandlerCallBack callBack) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int i = db.delete(AppConfig.TABLE_CATEGORY, null, null);

        if (i > 0) {
            callBack.onSuccess();
        } else {
            callBack.onFailure();
        }

        db.close();
    }

    /**
     * 根据ID获取分类
     *
     * @param cid
     */
    public Category getData(@NonNull String cid) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.CNAME,
                AppConfig.DESC,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        String selection = AppConfig.CATEGORY_ID + "=?";
        String[] selectionArgs = {cid};

        Cursor c = db.query(AppConfig.TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null);

        Category cat = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int Id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
            String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
            String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
            String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
            String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
            String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

            cat = new Category(Id, categoryId, cname, desc, createTime, updateTime);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return cat;
    }
}
