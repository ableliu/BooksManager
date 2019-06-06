package win.aspring.bookrxandroid.data.source;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Main entry point for accessing data.
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 14:04
 */
public interface BaseDataSource<T> {

    interface LoadDataCallBack<T> {
        /**
         * 获取所有的数据
         *
         * @param ts 列表
         */
        void onListLoaded(List<T> ts);

        /**
         * 数据不存在
         */
        void onDataNotAvailable();
    }

    interface GetDataCallBack<T> {
        /**
         * 当数据加载完成
         *
         * @param t 对象
         */
        void onDataLoaded(T t);

        /**
         * 当数据不存在
         */
        void onDataNotAvailable();
    }

    interface HandlerCallBack {
        /**
         * 操作成功
         */
        void onSuccess();

        /**
         * 操作失败
         */
        void onFailure();
    }

    /**
     * 获取列表
     *
     * @param callBack 回调
     */
    void getList(@NonNull LoadDataCallBack<T> callBack);

    /**
     * 根据ID获取数据
     *
     * @param id       Id
     * @param callback 回调
     */
    void getData(@NonNull int id, @NonNull GetDataCallBack<T> callback);

    /**
     * 保存数据
     *
     * @param t        对象
     * @param callBack 回调
     */
    void saveData(@NonNull T t, @NonNull HandlerCallBack callBack);

    /**
     * 删除数据
     *
     * @param id       ID
     * @param callBack 回调
     */
    void deleteData(@NonNull int id, @NonNull HandlerCallBack callBack);

    /**
     * 删除所有数据
     *
     * @param callBack 回调
     */
    void deleteAll(@NonNull HandlerCallBack callBack);
}
