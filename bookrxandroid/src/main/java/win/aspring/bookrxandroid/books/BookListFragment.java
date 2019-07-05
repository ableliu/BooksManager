package win.aspring.bookrxandroid.books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.source.local.BookDataSource;
import win.aspring.common.base.BaseFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends BaseFragment implements BookAdapter.OnDeleteInteractionListener {
    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isViewCreated = false;

    /**
     * Fragment对用户可见的标记
     */
    private boolean isUIVisible = false;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CATEGORY_ID = "category-id";
    private int mColumnCount = 1;
    private String mCateID;
    private OnListFragmentInteractionListener mListener;

    @Bind(R.id.list)
    protected RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    protected TextView mEmpty;

    private BookDataSource mDataSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
    }

    public static BookListFragment newInstance(int columnCount, String cid) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_CATEGORY_ID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mCateID = getArguments().getString(ARG_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        ButterKnife.bind(this, view);

        mDataSource = BookDataSource.getInstance(mActivity);

        initView(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void initView(View view) {
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
    }

    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            initData();
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    @Override
    public void initData() {
        mDataSource.getBooks(mCateID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    if (null != books && !books.isEmpty()) {
                        BookAdapter adapter = new BookAdapter(books, mListener);
                        adapter.setOnDeleteInteractionListener(BookListFragment.this);
                        recyclerView.setAdapter(adapter);
                        showEmpty(false);
                    } else {
                        showEmpty(true);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                initData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        isViewCreated = false;
        isUIVisible = false;
    }

    @Override
    public void onDeleteListener(Book item) {
        mDataSource.deleteData(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer > 0) {
                        showToast("删除成功！");
                        initData();
                    } else {
                        showErrorToast("删除失败！");
                    }
                });
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Book item);
    }

    /**
     * 展示空布局
     *
     * @param isShow 是否展示
     */
    private void showEmpty(boolean isShow) {
        if (isShow) {
            recyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
        }
    }
}
