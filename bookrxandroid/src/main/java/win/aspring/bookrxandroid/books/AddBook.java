package win.aspring.bookrxandroid.books;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Locale;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.local.BookDataSource;
import win.aspring.bookrxandroid.data.source.local.CategoryDataSource;
import win.aspring.common.util.StringUtils;
import win.aspring.common.widget.CustomToast;

public class AddBook extends AppCompatActivity {
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.book_category)
    protected Spinner mCategory;
    @Bind(R.id.book_code)
    protected TextInputLayout mCode;
    @Bind(R.id.book_name)
    protected TextInputLayout mName;
    @Bind(R.id.book_publisher)
    protected TextInputLayout mPublisher;
    @Bind(R.id.book_author)
    protected TextInputLayout mAuthor;

    private String mCurrentCid = String.valueOf(1);
    private int mCurrentPosition = 0;

    private Context mContext;

    private boolean isEdit;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_act);

        mContext = this;
        ButterKnife.bind(this);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            mBook = getIntent().getParcelableExtra("book");
            mToolbar.setTitle("修改图书信息");
        } else {
            mToolbar.setTitle("添加图书");
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_back);

        initView();
        initData();
    }

    private void initView() {
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_category) {
                addBook();
            }
            return true;
        });
        Objects.requireNonNull(mCode.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mCode.setErrorEnabled(false);
            }
        });
        Objects.requireNonNull(mName.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mName.setErrorEnabled(false);
            }
        });
        Objects.requireNonNull(mPublisher.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPublisher.setErrorEnabled(false);
            }
        });
        Objects.requireNonNull(mAuthor.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mAuthor.setErrorEnabled(false);
            }
        });
    }

    private void initData() {
        CategoryDataSource.getInstance(this).getList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    if (categories != null && !categories.isEmpty()) {
                        mCategory.setAdapter(new SimpleAdapter(mContext, categories));
                        if (isEdit) {
                            for (int i = 0, len = categories.size(); i < len; i++) {
                                if (mBook.getCategory_id().equals(categories.get(i).getCategory_id())) {
                                    mCurrentPosition = i;
                                    mCurrentCid = mBook.getCategory_id();
                                    break;
                                }
                            }
                        }
                        mCategory.setSelection(mCurrentPosition, true);
                        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Category cate = (Category) parent.getItemAtPosition(position);
                                mCurrentPosition = position;
                                mCurrentCid = cate.getCategory_id();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });

        if (isEdit) {
            mCurrentCid = mBook.getCategory_id();
            String temp = "XJXY" + mBook.getCategory_id();
            String code = mBook.getBook_code().substring(temp.length());
            mCode.getEditText().setText(String.valueOf(Integer.parseInt(code)));
            mName.getEditText().setText(mBook.getBook_name());
            mPublisher.getEditText().setText(mBook.getPublisher());
            mAuthor.getEditText().setText(mBook.getAuthor());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void addBook() {
        String strCode = mCode.getEditText().getText().toString();
        String name = mName.getEditText().getText().toString();
        String publisher = mPublisher.getEditText().getText().toString();
        String author = mAuthor.getEditText().getText().toString();

        if (mCurrentCid.equals(String.valueOf(0))) {
            CustomToast.info(mContext, "请选择图书分类！");
            return;
        }
        if (StringUtils.isEmpty(strCode)) {
            mCode.setError("图书序号不能为空！");
            return;
        }

        if (strCode.length() < 3) {
            strCode = String.format(Locale.getDefault(), "%03d", Integer.parseInt(strCode));
        }

        String code = "XJXY" + mCurrentCid + strCode;

        BookDataSource dataSource = BookDataSource.getInstance(this);
        if (!isEdit) {
            dataSource.getData(code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(book -> {
                        if (null != book) {
                            mCode.setError("图书序号不可用！");
                            return;
                        }
                    });
        }
        if (StringUtils.isEmpty(name)) {
            mName.setError("图书名称不能为空！");
            return;
        }
        if (StringUtils.isEmpty(publisher)) {
            mPublisher.setError("出版社不能为空！");
            return;
        }
        if (StringUtils.isEmpty(author)) {
            mAuthor.setError("图书作者不能为空！");
            return;
        }

        mCode.setErrorEnabled(false);
        mName.setErrorEnabled(false);
        mPublisher.setErrorEnabled(false);
        mAuthor.setErrorEnabled(false);

        if (isEdit) {
            mBook.setCategory_id(mCurrentCid);
            mBook.setBook_code(code);
            mBook.setBook_name(name);
            mBook.setPublisher(publisher);
            mBook.setAuthor(author);
            dataSource.updateBook(mBook.getId(), mCurrentCid, code, name, publisher, author)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (integer > 0) {
                            CustomToast.info(mContext, "修改成功！");
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            CustomToast.error(mContext, "对不起，修改失败！");
                        }
                    });
        } else {
            Book book = new Book(mCurrentCid, code, name, publisher, author, 1, "", "", "", "",
                    StringUtils.getCurTimeStr(), StringUtils.getCurTimeStr());
            dataSource.saveData(book)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (aLong > 0) {
                            CustomToast.info(mContext, "添加成功！");
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            CustomToast.error(mContext, "对不起，添加失败！");
                        }
                    });
        }
    }
}
