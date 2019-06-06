package win.aspring.bookrxandroid.books;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.local.CategoryDataSource;
import win.aspring.common.base.BaseFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class BookFragment extends BaseFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    @Bind(R.id.view_pager)
    protected ViewPager mPager;
    @Bind(R.id.pager_tabs)
    protected PagerTabStrip mTabStrip;
    @Bind(R.id.btn_add)
    protected FloatingActionButton mFloatButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
    }

    public static BookFragment newInstance(int columnCount) {
        BookFragment fragment = new BookFragment();
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
        View view = inflater.inflate(R.layout.fragment_book_tab, container, false);

        ButterKnife.bind(this, view);

        initView(view);
        initData();

        return view;
    }

    @Override
    public void initView(View view) {
        //设置pagerTabStrip
        mTabStrip.setTabIndicatorColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mTabStrip.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mTabStrip.setClickable(true);
        mTabStrip.setTextSpacing(40);
        mTabStrip.setBackgroundColor(Color.WHITE);
        mTabStrip.setDrawFullUnderline(true);

        mFloatButton.setImageResource(R.drawable.ic_add);
        mFloatButton.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddBook.class);
            startActivityForResult(i, 100);
        });
    }

    @Override
    public void initData() {
        showWaitDialog();

        CategoryDataSource.getInstance(mContext).getList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (categories != null && !categories.isEmpty()) {
                        mPager.setAdapter(new BookListAdapter(getChildFragmentManager(), categories));
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class BookListAdapter extends FragmentPagerAdapter {
        private List<Category> mList;

        BookListAdapter(FragmentManager fm, List<Category> categories) {
            super(fm);
            this.mList = categories;
        }

        @Override
        public Fragment getItem(int position) {
            Category category = mList.get(position);
            return BookListFragment.newInstance(mColumnCount, category.getCategory_id());
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mList.get(position).getCname();
        }
    }
}
