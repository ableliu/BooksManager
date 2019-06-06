package win.aspring.bookrxandroid.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

/**
 * Main entry point for accessing data.
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-21 14:04
 */
public interface BaseDataSource<T> {

    Observable<List<T>> getList();

    Observable<T> getData(@NonNull int id);

    Observable<Long> saveData(@NonNull T t);

    Observable<Integer> deleteData(@NonNull int id);

    Observable<Integer> deleteAll();
}
