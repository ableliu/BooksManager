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
import win.aspring.bookrxandroid.data.source.BaseDataSource;
import win.aspring.common.util.StringUtils;

/**
 * 图书
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 14:19
 */
public class BookDataSource implements BaseDataSource<Book> {

    private static BookDataSource INSTANCE;

    private LocalDbHelper mHelper;

    private BookDataSource(@NonNull Context context) {
        mHelper = new LocalDbHelper(context);
    }

    public static BookDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BookDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Book>> getList() {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getReadableDatabase();

            String[] columns = {
                    AppConfig.ID,
                    AppConfig.CATEGORY_ID,
                    AppConfig.BOOK_CODE,
                    AppConfig.BOOK_NAME,
                    AppConfig.PUBLISHER,
                    AppConfig.AUTHOR,
                    AppConfig.STATE_LIBRARY,
                    AppConfig.BORROW_PEOPLE,
                    AppConfig.BORROW_PHONE,
                    AppConfig.BORROW_TIME,
                    AppConfig.BACK_TIME,
                    AppConfig.CREATE_TIME,
                    AppConfig.UPDATE_TIME
            };

            Cursor c = db.query(AppConfig.TABLE_BOOK, columns, null, null, null, null, null);

            List<Book> books = new ArrayList<>();

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    int id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                    String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                    String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                    String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                    String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                    String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                    int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                    String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                    String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                    String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                    String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                    String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                    String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                    Book book = new Book(id, categoryId, bookCode, bookName, publisher, author, state, people,
                            phone, borrowTime, backTime, createTime, updateTime);
                    books.add(book);
                }
            }

            if (c != null) {
                c.close();
            }

            db.close();
            return books;
        });
    }

    @Override
    public Observable<Book> getData(final int id) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getReadableDatabase();

            String[] columns = {
                    AppConfig.ID,
                    AppConfig.CATEGORY_ID,
                    AppConfig.BOOK_CODE,
                    AppConfig.BOOK_NAME,
                    AppConfig.PUBLISHER,
                    AppConfig.AUTHOR,
                    AppConfig.STATE_LIBRARY,
                    AppConfig.BORROW_PEOPLE,
                    AppConfig.BORROW_PHONE,
                    AppConfig.BORROW_TIME,
                    AppConfig.BACK_TIME,
                    AppConfig.CREATE_TIME,
                    AppConfig.UPDATE_TIME
            };

            String selection = AppConfig.ID + "=?";
            String[] selectionArgs = {String.valueOf(id)};

            Cursor c = db.query(AppConfig.TABLE_BOOK, columns, selection, selectionArgs, null, null, null);

            Book book = null;
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int ID = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                book = new Book(ID, categoryId, bookCode, bookName, publisher, author, state, people,
                        phone, borrowTime, backTime, createTime, updateTime);
            }
            if (c != null) {
                c.close();
            }

            db.close();

            return book;
        });
    }

    @Override
    public Observable<Long> saveData(@NonNull final Book book) {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AppConfig.CATEGORY_ID, book.getCategory_id());
            values.put(AppConfig.BOOK_CODE, book.getBook_code());
            values.put(AppConfig.BOOK_NAME, book.getBook_name());
            values.put(AppConfig.PUBLISHER, book.getPublisher());
            values.put(AppConfig.AUTHOR, book.getAuthor());
            values.put(AppConfig.STATE_LIBRARY, book.getState_library());
            values.put(AppConfig.BORROW_PEOPLE, book.getBorrow_people());
            values.put(AppConfig.BORROW_PHONE, book.getBorrow_phone());
            values.put(AppConfig.BORROW_TIME, book.getBorrow_time());
            values.put(AppConfig.BACK_TIME, book.getBack_time());
            values.put(AppConfig.CREATE_TIME, book.getCreate_time());
            values.put(AppConfig.UPDATE_TIME, book.getUpdate_time());

            long i = db.insert(AppConfig.TABLE_BOOK, null, values);

            db.close();

            return i;
        });
    }

    @Override
    public Observable<Integer> deleteData(final int id) {
        return Observable.fromCallable(() -> {

            SQLiteDatabase db = mHelper.getWritableDatabase();

            String selection = AppConfig.ID + "=?";
            String[] selectionArgs = {String.valueOf(id)};

            int i = db.delete(AppConfig.TABLE_BOOK, selection, selectionArgs);

            db.close();

            return i;
        });
    }

    @Override
    public Observable<Integer> deleteAll() {
        return Observable.fromCallable(() -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            int i = db.delete(AppConfig.TABLE_BOOK, null, null);

            db.close();

            return i;
        });
    }

    /**
     * 通过书名查询图书
     *
     * @param name
     */
    public void getList(@NonNull String name) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.BOOK_CODE,
                AppConfig.BOOK_NAME,
                AppConfig.PUBLISHER,
                AppConfig.AUTHOR,
                AppConfig.STATE_LIBRARY,
                AppConfig.BORROW_PEOPLE,
                AppConfig.BORROW_PHONE,
                AppConfig.BORROW_TIME,
                AppConfig.BACK_TIME,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        String selection = AppConfig.BOOK_NAME + "=?";
        String[] selectionArgs = {name};

        Cursor c = db.query(AppConfig.TABLE_BOOK, columns, selection, selectionArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                Book book = new Book(id, categoryId, bookCode, bookName, publisher, author, state, people,
                        phone, borrowTime, backTime, createTime, updateTime);
                books.add(book);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
    }


    /**
     * 根据图书分类ID获取图书
     *
     * @param cateId
     */
    public List<Book> getBookOfCategory(@NonNull final String cateId) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] columns = {
                AppConfig.ID,
                AppConfig.CATEGORY_ID,
                AppConfig.BOOK_CODE,
                AppConfig.BOOK_NAME,
                AppConfig.PUBLISHER,
                AppConfig.AUTHOR,
                AppConfig.STATE_LIBRARY,
                AppConfig.BORROW_PEOPLE,
                AppConfig.BORROW_PHONE,
                AppConfig.BORROW_TIME,
                AppConfig.BACK_TIME,
                AppConfig.CREATE_TIME,
                AppConfig.UPDATE_TIME
        };

        String selection = AppConfig.CATEGORY_ID + "=?";
        String[] selectionArgs = {cateId};

        Cursor c = db.query(AppConfig.TABLE_BOOK, columns, selection, selectionArgs, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                Book book = new Book(id, categoryId, bookCode, bookName, publisher, author, state, people,
                        phone, borrowTime, backTime, createTime, updateTime);
                books.add(book);
            }
        }

        if (c != null) {
            c.close();
        }

        db.close();

        return books;
    }


    /**
     * 根据图书分类ID获取图书
     *
     * @param cateId 分类ID
     */
    public Observable<List<Book>> getBooks(@NonNull final String cateId) {
        return Observable.create(emitter -> {
            List<Book> books = new ArrayList<>();
            SQLiteDatabase db = mHelper.getReadableDatabase();

            String[] columns = {
                    AppConfig.ID,
                    AppConfig.CATEGORY_ID,
                    AppConfig.BOOK_CODE,
                    AppConfig.BOOK_NAME,
                    AppConfig.PUBLISHER,
                    AppConfig.AUTHOR,
                    AppConfig.STATE_LIBRARY,
                    AppConfig.BORROW_PEOPLE,
                    AppConfig.BORROW_PHONE,
                    AppConfig.BORROW_TIME,
                    AppConfig.BACK_TIME,
                    AppConfig.CREATE_TIME,
                    AppConfig.UPDATE_TIME
            };

            String selection = AppConfig.CATEGORY_ID + "=?";
            String[] selectionArgs = {cateId};

            Cursor c = db.query(AppConfig.TABLE_BOOK, columns, selection, selectionArgs, null, null, null);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    int id = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                    String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                    String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                    String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                    String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                    String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                    int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                    String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                    String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                    String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                    String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                    String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                    String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                    Book book = new Book(id, categoryId, bookCode, bookName, publisher, author, state, people,
                            phone, borrowTime, backTime, createTime, updateTime);
                    books.add(book);
                }
            }

            if (c != null) {
                c.close();
            }

            db.close();

            emitter.onNext(books);
        });
    }

    /**
     * 根据图书编号获取图书
     *
     * @param code
     */
    public Observable<Book> getData(@NonNull final String code) {
        return Observable.create(emitter -> {
            SQLiteDatabase db = mHelper.getReadableDatabase();

            String[] columns = {
                    AppConfig.ID,
                    AppConfig.CATEGORY_ID,
                    AppConfig.BOOK_CODE,
                    AppConfig.BOOK_NAME,
                    AppConfig.PUBLISHER,
                    AppConfig.AUTHOR,
                    AppConfig.STATE_LIBRARY,
                    AppConfig.BORROW_PEOPLE,
                    AppConfig.BORROW_PHONE,
                    AppConfig.BORROW_TIME,
                    AppConfig.BACK_TIME,
                    AppConfig.CREATE_TIME,
                    AppConfig.UPDATE_TIME
            };

            String selection = AppConfig.BOOK_CODE + "=?";
            String[] selectionArgs = {code};

            Cursor c = db.query(AppConfig.TABLE_BOOK, columns, selection, selectionArgs, null, null, null);

            Book book = null;
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int ID = c.getInt(c.getColumnIndexOrThrow(AppConfig.ID));
                String categoryId = c.getString(c.getColumnIndexOrThrow(AppConfig.CATEGORY_ID));
                String bookCode = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_CODE));
                String bookName = c.getString(c.getColumnIndexOrThrow(AppConfig.BOOK_NAME));
                String publisher = c.getString(c.getColumnIndexOrThrow(AppConfig.PUBLISHER));
                String author = c.getString(c.getColumnIndexOrThrow(AppConfig.AUTHOR));
                int state = c.getInt(c.getColumnIndexOrThrow(AppConfig.STATE_LIBRARY));
                String people = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PEOPLE));
                String phone = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_PHONE));
                String borrowTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BORROW_TIME));
                String backTime = c.getString(c.getColumnIndexOrThrow(AppConfig.BACK_TIME));
                String createTime = c.getString(c.getColumnIndexOrThrow(AppConfig.CREATE_TIME));
                String updateTime = c.getString(c.getColumnIndexOrThrow(AppConfig.UPDATE_TIME));
                book = new Book(ID, categoryId, bookCode, bookName, publisher, author, state, people,
                        phone, borrowTime, backTime, createTime, updateTime);
            }

            if (c != null) {
                c.close();
            }

            db.close();

            if (null != book) {
                emitter.onNext(book);
            } else {
                emitter.onError(new NullPointerException("Book is Null"));
            }
        });
    }


    /**
     * 更新图书
     *
     * @param id         ID
     * @param categoryId 分类
     * @param bookName   图书名称
     * @param publisher  出版社
     * @param author     作者
     */
    public Observable<Integer> updateBook(final int id, final String categoryId, final String code,
                                          final String bookName, final String publisher, final String author) {
        return Observable.create(emitter -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AppConfig.CATEGORY_ID, categoryId);
            values.put(AppConfig.BOOK_CODE, code);
            values.put(AppConfig.BOOK_NAME, bookName);
            values.put(AppConfig.PUBLISHER, publisher);
            values.put(AppConfig.AUTHOR, author);
            values.put(AppConfig.UPDATE_TIME, StringUtils.getCurTimeStr());

            String whereClause = AppConfig.ID + "=" + id;

            int i = db.update(AppConfig.TABLE_BOOK, values, whereClause, null);

            db.close();
            emitter.onNext(i);
        });
    }

    /**
     * 借出图书
     *
     * @param id ID
     */
    public Observable<Integer> lentOut(final int id, @NonNull final String name, final String phone) {
        return Observable.create(emitter -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AppConfig.STATE_LIBRARY, 0);
            values.put(AppConfig.BORROW_PEOPLE, name);
            values.put(AppConfig.BORROW_PHONE, phone);
            values.put(AppConfig.BORROW_TIME, StringUtils.getCurTimeStr());

            String whereClause = AppConfig.ID + "=?";
            String[] whereArgs = {String.valueOf(id)};

            int i = db.update(AppConfig.TABLE_BOOK, values, whereClause, whereArgs);

            db.close();
            emitter.onNext(i);
        });
    }

    /**
     * 返还图书
     *
     * @param id ID
     */
    public Observable<Integer> backBook(final int id) {
        return Observable.create(emitter -> {
            SQLiteDatabase db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AppConfig.STATE_LIBRARY, 1);
            values.put(AppConfig.BORROW_PEOPLE, "");
            values.put(AppConfig.BORROW_PHONE, "");
            values.put(AppConfig.BORROW_TIME, "");
            values.put(AppConfig.BACK_TIME, StringUtils.getCurTimeStr());

            String whereClause = AppConfig.ID + "=?";
            String[] whereArgs = {String.valueOf(id)};

            int i = db.update(AppConfig.TABLE_BOOK, values, whereClause, whereArgs);

            db.close();
            emitter.onNext(i);
        });
    }
}
