package win.aspring.bookrxandroid.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.local.CategoryDataSource;
import win.aspring.common.base.BaseApplication;
import win.aspring.common.base.BaseFragment;
import win.aspring.common.widget.CustomToast;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CategoryFragment extends BaseFragment implements CategoryAdapter.OnDeleteInteractionListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    @Bind(R.id.list)
    protected RecyclerView recyclerView;
    @Bind(R.id.btn_add)
    protected FloatingActionButton mFloatButton;
    @Bind(R.id.empty_view)
    protected TextView mEmpty;

    private CategoryDataSource mDataSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(int columnCount) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, view);

        mDataSource = CategoryDataSource.getInstance(mContext);

        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mFloatButton.setImageResource(R.drawable.ic_add);
        mFloatButton.setOnClickListener(v -> {
            Intent i = new Intent(mContext, AddCategory.class);
            startActivityForResult(i, 100);
        });
    }

    @Override
    public void initData() {
        showWaitDialog();
        mDataSource.getList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (categories != null && !categories.isEmpty()) {
                        CategoryAdapter mAdapter = new CategoryAdapter(categories, mListener);
                        mAdapter.setOnDeleteInteractionListener(CategoryFragment.this);
                        recyclerView.setAdapter(mAdapter);
                        hideWaitDialog();
                        showEmpty(false);
                    } else {
                        hideWaitDialog();
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
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    }

    @Override
    public void onDeleteListener(Category item) {
        mDataSource.deleteData(item.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer > 0) {
                        CustomToast.info(mContext, "删除成功！");
                        initData();
                    } else {
                        CustomToast.error(mContext, "删除失败！");
                    }
                });
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Category item);
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
