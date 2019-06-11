package win.aspring.bookrxandroid.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 图书实体类
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 13:18
 */
public class Book implements Parcelable {
    /**
     * 自增ID
     */
    private int id;
    /**
     * 分类ID
     */
    private String category_id;
    /**
     * 图书编号
     */
    private String book_code;
    /**
     * 图书名称
     */
    private String book_name;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 作者
     */
    private String author;
    /**
     * 在馆状态
     */
    private int state_library;
    /**
     * 借阅人
     */
    private String borrow_people;
    /**
     * 借阅人联系方式
     */
    private String borrow_phone;
    /**
     * 借阅时间
     */
    private String borrow_time;
    /**
     * 归还时间
     */
    private String back_time;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 更新时间
     */
    private String update_time;

    public Book(@NonNull String category_id, @NonNull String book_code,
                @NonNull String book_name, String publisher, String author,
                int state_library, String borrow_people, String borrow_phone,
                String borrow_time, String back_time, String create_time, String update_time) {
        this.category_id = category_id;
        this.book_code = book_code;
        this.book_name = book_name;
        this.publisher = publisher;
        this.author = author;
        this.state_library = state_library;
        this.borrow_people = borrow_people;
        this.borrow_phone = borrow_phone;
        this.borrow_time = borrow_time;
        this.back_time = back_time;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public Book(int id, @NonNull String category_id, @NonNull String book_code,
                @NonNull String book_name, String publisher, String author,
                int state_library, String borrow_people, String borrow_phone,
                String borrow_time, String back_time, String create_time, String update_time) {
        this.id = id;
        this.category_id = category_id;
        this.book_code = book_code;
        this.book_name = book_name;
        this.publisher = publisher;
        this.author = author;
        this.state_library = state_library;
        this.borrow_people = borrow_people;
        this.borrow_phone = borrow_phone;
        this.borrow_time = borrow_time;
        this.back_time = back_time;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBook_code() {
        return book_code;
    }

    public void setBook_code(String book_code) {
        this.book_code = book_code;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getState_library() {
        return state_library;
    }

    public void setState_library(int state_library) {
        this.state_library = state_library;
    }

    public String getBorrow_people() {
        return borrow_people;
    }

    public void setBorrow_people(String borrow_people) {
        this.borrow_people = borrow_people;
    }

    public String getBorrow_phone() {
        return borrow_phone;
    }

    public void setBorrow_phone(String borrow_phone) {
        this.borrow_phone = borrow_phone;
    }

    public String getBorrow_time() {
        return borrow_time;
    }

    public void setBorrow_time(String borrow_time) {
        this.borrow_time = borrow_time;
    }

    public String getBack_time() {
        return back_time;
    }

    public void setBack_time(String back_time) {
        this.back_time = back_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.category_id);
        dest.writeString(this.book_code);
        dest.writeString(this.book_name);
        dest.writeString(this.publisher);
        dest.writeString(this.author);
        dest.writeInt(this.state_library);
        dest.writeString(this.borrow_people);
        dest.writeString(this.borrow_phone);
        dest.writeString(this.borrow_time);
        dest.writeString(this.back_time);
        dest.writeString(this.create_time);
        dest.writeString(this.update_time);
    }

    protected Book(Parcel in) {
        this.id = in.readInt();
        this.category_id = in.readString();
        this.book_code = in.readString();
        this.book_name = in.readString();
        this.publisher = in.readString();
        this.author = in.readString();
        this.state_library = in.readInt();
        this.borrow_people = in.readString();
        this.borrow_phone = in.readString();
        this.borrow_time = in.readString();
        this.back_time = in.readString();
        this.create_time = in.readString();
        this.update_time = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
