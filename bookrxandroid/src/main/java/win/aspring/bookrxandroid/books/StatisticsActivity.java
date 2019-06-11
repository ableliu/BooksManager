package win.aspring.bookrxandroid.books;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import win.aspring.bookrxandroid.BuildConfig;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.bookdetail.BookDetailActivity;
import win.aspring.bookrxandroid.category.CategoryFragment;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.Category;

public class StatisticsActivity extends AppCompatActivity implements CategoryFragment.OnListFragmentInteractionListener,
        BookListFragment.OnListFragmentInteractionListener {
    @Bind(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager manager;

    private Context mContext;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mContext = this;

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        ButterKnife.bind(this);

        mToolbar.setTitle("图书管理");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initView();
    }


    public void initView() {
        manager = getSupportFragmentManager();
        // Set up the navigation drawer.
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimary);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.sure_text, R.string.cancel_text);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        BookFragment book = BookFragment.newInstance(1);
        manager.beginTransaction().replace(R.id.contentFrame, book, "book").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = manager.findFragmentById(R.id.contentFrame);
            switch (menuItem.getItemId()) {
                case R.id.list_navigation_menu_item:
                    if (fragment instanceof CategoryFragment) {

                    } else {
                        mToolbar.setTitle("分类管理");
                        CategoryFragment category = CategoryFragment.newInstance(1);
                        manager.beginTransaction().replace(R.id.contentFrame, category, "category").commit();
                    }
                    break;
                case R.id.statistics_navigation_menu_item:
                    if (fragment instanceof BookFragment) {

                    } else {
                        mToolbar.setTitle("图书管理");
                        BookFragment book = BookFragment.newInstance(1);
                        manager.beginTransaction().replace(R.id.contentFrame, book, "book").commit();
                    }
                    break;
                default:
                    break;
            }
            // Close the navigation drawer when an item is selected.
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                BookFragment fragment = (BookFragment) manager.findFragmentByTag("book");
                assert fragment != null;
                fragment.initData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onListFragmentInteraction(Category item) {

    }

    @Override
    public void onListFragmentInteraction(Book item) {
        Intent i = new Intent(mContext, BookDetailActivity.class);
        i.putExtra("book", item);
        startActivityForResult(i, 200);
    }
}
