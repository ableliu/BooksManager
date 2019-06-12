package win.aspring.bookrxandroid.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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
     */
    @Override
    public Observable<List<Category>> getList() {
        return Observable.fromCallable(() -> {
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

            return cate;
        });
    }

    /**
     * 根据ID获取分类
     *
     * @param id
     */
    @Override
    public Observable<Category> getData(final int id) {
        return Observable.fromCallable(() -> {
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
                int cId = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
                String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

                cat = new Category(cId, categoryId, cname, desc, createTime, updateTime);
            }

            if (c != null) {
                c.close();
            }

            db.close();

            return cat;
        });
    }

    /**
     * 保存分类
     *
     * @param category 分
     */
    @Override
    public Observable<Long> saveData(@NonNull final Category category) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AppConfig.CATEGORY_ID, category.getCategory_id());
            values.put(AppConfig.CNAME, category.getCname());
            values.put(AppConfig.CREATE_TIME, category.getCreate_time());
            values.put(AppConfig.UPDATE_TIME, category.getUpdate_time());

            long l = db.insert(AppConfig.TABLE_CATEGORY, null, values);

            db.close();

            return l;
        });
    }

    /**
     * 根据ID删除分类
     *
     * @param id ID
     */
    @Override
    public Observable<Integer> deleteData(final int id) {
        return Observable.fromCallable(() -> {
            int i = 0;
            SQLiteDatabase db = mHelper.getWritableDatabase();
            Category cate = getCategory(id);
            if (cate != null) {
                List<Book> books = BookDataSource.getInstance(context).getBookOfCategory(cate.getCategory_id());
                if (books != null && !books.isEmpty()) {
                    i = -1;
                }
            } else {
                String selection = AppConfig.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};

                i = db.delete(AppConfig.TABLE_CATEGORY, selection, selectionArgs);

                db.close();
            }
            return i;
        });
    }

    /**
     * 删除所有分类
     */
    @Override
    public Observable<Integer> deleteAll() {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            int i = db.delete(AppConfig.TABLE_CATEGORY, null, null);

            db.close();

            return i;
        });
    }

    /**
     * 根据ID获取分类
     *
     * @param id
     */
    private Category getCategory(int id) {
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

        return cat;
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
            int cId = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
            String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
            String cname = c.getString(c.getColumnIndexOrThrow(AppConfig.CNAME));
            String desc = c.getString(c.getColumnIndexOrThrow(AppConfig.DESC));
            String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
            String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));

            cat = new Category(cId, categoryId, cname, desc, createTime, updateTime);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        return cat;
    }
}
