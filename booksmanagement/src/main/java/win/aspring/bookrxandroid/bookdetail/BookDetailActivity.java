package win.aspring.bookrxandroid.bookdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.books.AddBook;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.BaseDataSource;
import win.aspring.bookrxandroid.data.source.local.BookDataSource;
import win.aspring.bookrxandroid.data.source.local.CategoryDataSource;
import win.aspring.common.util.StringUtils;
import win.aspring.common.widget.CustomToast;

public class BookDetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.fab)
    protected FloatingActionButton mFloatButton;
    @Bind(R.id.toolbar_layout)
    protected CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.book_category)
    protected TextView mCategory;
    @Bind(R.id.book_code)
    protected TextView mCode;
    @Bind(R.id.book_name)
    protected TextView mName;
    @Bind(R.id.book_publisher)
    protected TextView mPublisher;
    @Bind(R.id.book_author)
    protected TextView mAuthor;
    @Bind(R.id.book_state)
    protected TextView mState;
    @Bind(R.id.borrow_state)
    protected View mBorrow;
    @Bind(R.id.borrow_people)
    protected TextView mPeople;
    @Bind(R.id.borrow_phone)
    protected TextView mPhone;
    @Bind(R.id.borrow_time)
    protected TextView mBorrowTime;
    @Bind(R.id.book_backtime)
    protected TextView mBackTime;
    @Bind(R.id.book_createtime)
    protected TextView mCreateTime;
    @Bind(R.id.book_updatetime)
    protected TextView mUpdateTime;

    private Book mBook;

    private Context mContext;

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mBook = getIntent().getParcelableExtra("book");
        mContext = this;

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_back);

        initView();
        initData();
    }

    private void initView() {
        mToolbarLayout.setTitle(mBook.getBook_name());

        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_lend:
                    lendOut();
                    return true;
                case R.id.action_rebates:
                    onReturn();
                    return true;
                default:
            }
            return false;
        });

        mFloatButton.setOnClickListener(view -> {
            Intent i = new Intent(mContext, AddBook.class);
            i.putExtra("isEdit", true);
            i.putExtra("book", mBook);
            startActivityForResult(i, 200);
        });
    }

    private void initData() {
        String cid = mBook.getCategory_id();
        Category cate = CategoryDataSource.getInstance(this).getData(cid);
        mCategory.setText(cate.getCname());
        mCode.setText(mBook.getBook_code());
        mName.setText(mBook.getBook_name());
        mPublisher.setText(mBook.getPublisher());
        mAuthor.setText(mBook.getAuthor());

        int state = mBook.getState_library();
        if (state == 1) {
            mState.setText("在馆");
            mBorrow.setVisibility(View.GONE);
        } else {
            mState.setText("借出");
            mBorrow.setVisibility(View.GONE);
            mBorrow.setVisibility(View.VISIBLE);
            mPeople.setText(mBook.getBorrow_people());
            mPhone.setText(mBook.getBorrow_phone());
            mBorrowTime.setText(mBook.getBorrow_time());
        }
        mBackTime.setText(mBook.getBack_time());
        mCreateTime.setText(mBook.getCreate_time());
        mUpdateTime.setText(mBook.getUpdate_time());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                setResult(RESULT_OK);
                initData();
                initMenu();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        initMenu();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initMenu() {
        int state = mBook.getState_library();
        if (state == 1) {
            mMenu.getItem(0).setVisible(true);
            mMenu.getItem(0).setEnabled(true);
            mMenu.getItem(1).setVisible(false);
            mMenu.getItem(1).setEnabled(false);
        } else {
            mMenu.getItem(0).setVisible(false);
            mMenu.getItem(0).setEnabled(false);
            mMenu.getItem(1).setVisible(true);
            mMenu.getItem(1).setEnabled(true);
        }
    }

    /**
     * 借出图书
     */
    private void lendOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = getLayoutInflater().inflate(R.layout.dialog_lend, null);
        builder.setView(view);
        builder.setCancelable(false);
        final Dialog dialog = builder.create();
        dialog.show();

        final TextInputLayout mPeople = view.findViewById(R.id.borrow_people);
        final TextInputLayout mPhone = view.findViewById(R.id.borrow_phone);
        final TextView mCancel = view.findViewById(R.id.dialog_cancel);
        final TextView mSure = view.findViewById(R.id.dialog_sure);

        Objects.requireNonNull(mPeople.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPeople.setErrorEnabled(false);
            }
        });
        Objects.requireNonNull(mPhone.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPhone.setErrorEnabled(false);
            }
        });
        mCancel.setOnClickListener(v -> dialog.dismiss());
        mSure.setOnClickListener(v -> {
            mCancel.setEnabled(false);
            mSure.setEnabled(false);

            String name = mPeople.getEditText().getText().toString();
            String phone = mPhone.getEditText().getText().toString();
            if (StringUtils.isEmpty(name)) {
                mCancel.setEnabled(true);
                mSure.setEnabled(true);
                mPeople.setError("借阅人姓名不能为空！");
                return;
            }
            if (StringUtils.isEmpty(phone)) {
                mCancel.setEnabled(true);
                mSure.setEnabled(true);
                mPhone.setError("借阅人电话不能为空！");
                return;
            }
            if (!StringUtils.isPhone(phone)) {
                mCancel.setEnabled(true);
                mSure.setEnabled(true);
                mPhone.setError("借阅人电话格式不正确！");
                return;
            }

            BookDataSource.getInstance(getBaseContext())
                    .lentOut(mBook.getId(), name, phone, new BaseDataSource.HandlerCallBack() {
                        @Override
                        public void onSuccess() {
                            mCancel.setEnabled(true);
                            mSure.setEnabled(true);
                            CustomToast.info(mContext, "借阅成功！");
                            mBook = BookDataSource.getInstance(getBaseContext()).getData(mBook.getBook_code());
                            initData();
                            initMenu();
                            setResult(RESULT_OK);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure() {
                            mCancel.setEnabled(true);
                            mSure.setEnabled(true);
                            CustomToast.error(mContext, "借阅失败！");
                        }
                    });
        });
    }

    /**
     * 返还图书
     */
    private void onReturn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = getLayoutInflater().inflate(R.layout.dialog_return, null);
        builder.setView(view);
        builder.setCancelable(true);
        final Dialog dialog = builder.create();
        dialog.show();

        final TextView mMsg = view.findViewById(R.id.dialog_message);
        final TextView mCancel = view.findViewById(R.id.dialog_cancel);
        final TextView mSure = view.findViewById(R.id.dialog_sure);
        mMsg.setText("是否确认图书\"" + mBook.getBook_name() + "\"入库？");
        mCancel.setOnClickListener(v -> dialog.dismiss());
        mSure.setOnClickListener(v -> {
            mCancel.setEnabled(false);
            mSure.setEnabled(false);

            BookDataSource.getInstance(getBaseContext()).backBook(mBook.getId(), new BaseDataSource.HandlerCallBack() {
                @Override
                public void onSuccess() {
                    mCancel.setEnabled(true);
                    mSure.setEnabled(true);

                    CustomToast.info(mContext, "入库成功！");
                    mBook = BookDataSource.getInstance(getBaseContext()).getData(mBook.getBook_code());
                    initData();
                    initMenu();
                    setResult(RESULT_OK);
                    dialog.dismiss();
                }

                @Override
                public void onFailure() {
                    mCancel.setEnabled(true);
                    mSure.setEnabled(true);

                    CustomToast.error(mContext, "入库失败！");
                }
            });
        });
    }
}