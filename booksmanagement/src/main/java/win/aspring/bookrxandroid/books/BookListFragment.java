package win.aspring.bookrxandroid.books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.source.BaseDataSource;
import win.aspring.bookrxandroid.data.source.local.BookDataSource;
import win.aspring.common.base.BaseFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends BaseFragment implements BookAdapter.OnDeleteInteractionListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CATEGORY_ID = "category-id";
    private int mColumnCount = 1;
    private String mCateID;
    private OnListFragmentInteractionListener mListener;

    @Bind(R.id.list)
    protected RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    protected TextView mEmpty;

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

        initView(view);
        initData();

        return view;
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

    @Override
    public void initData() {
        BookDataSource.getInstance(mContext).getBooks(mCateID, new BaseDataSource.LoadDataCallBack<Book>() {
            @Override
            public void onListLoaded(List<Book> books) {
                BookAdapter adapter = new BookAdapter(books, mListener);
                adapter.setOnDeleteInteractionListener(BookListFragment.this);
                recyclerView.setAdapter(adapter);
                showEmpty(false);
            }

            @Override
            public void onDataNotAvailable() {
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDeleteListener(Book item) {
        BookDataSource.getInstance(mContext).deleteData(item.getId(), new BaseDataSource.HandlerCallBack() {
            @Override
            public void onSuccess() {
                showInfoToast("删除成功！");
                initData();
            }

            @Override
            public void onFailure() {
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
