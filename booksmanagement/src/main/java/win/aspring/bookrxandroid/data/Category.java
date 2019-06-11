package win.aspring.bookrxandroid.data;

import android.support.annotation.NonNull;

/**
 * 图书分类实体类
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 13:10
 */
public class Category {
    /**
     * 自增ID
     */
    private int id;
    /**
     * 分类ID
     */
    private String category_id;
    /**
     * 分类名称
     */
    private String cname;
    /**
     * 描述
     */
    private String desc;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 更新时间
     */
    private String update_time;

    public Category(@NonNull String category_id, @NonNull String cname, String desc,
                    String create_time, String update_time) {
        this.category_id = category_id;
        this.cname = cname;
        this.desc = desc;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public Category(int id, @NonNull String category_id, @NonNull String cname, String desc,
                    String create_time, String update_time) {
        this.id = id;
        this.category_id = category_id;
        this.cname = cname;
        this.desc = desc;
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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
