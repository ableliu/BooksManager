package win.aspring.bookrxandroid.category;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Category;
import win.aspring.bookrxandroid.data.source.local.CategoryDataSource;
import win.aspring.common.util.StringUtils;
import win.aspring.common.widget.CustomToast;

public class AddCategory extends AppCompatActivity {
    private Context mContext;

    @Bind(R.id.category_id)
    protected TextInputLayout idWrapper;
    @Bind(R.id.category_name)
    protected TextInputLayout nameWrapper;
    @Bind(R.id.category_desc)
    protected TextInputLayout descWrapper;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category_act);

        mContext = this;

        ButterKnife.bind(this);

        mToolbar.setTitle("添加分类");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_back);

        initView();
    }

    public void initView() {
        Objects.requireNonNull(idWrapper.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                idWrapper.setErrorEnabled(false);
            }
        });

        Objects.requireNonNull(nameWrapper.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                nameWrapper.setErrorEnabled(false);
            }
        });
        Objects.requireNonNull(descWrapper.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                descWrapper.setErrorEnabled(false);
            }
        });

        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_category) {
                addCategory();
            }
            return true;
        });
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

    private void addCategory() {
        String id = idWrapper.getEditText().getText().toString();
        String name = nameWrapper.getEditText().getText().toString();
        String desc = descWrapper.getEditText().getText().toString();
        if (StringUtils.isEmpty(id)) {
            idWrapper.setError("分类编号不能为空！");
            return;
        }

        CategoryDataSource dataSource = CategoryDataSource.getInstance(mContext);

        if (null != dataSource.getData(id)) {
            idWrapper.setError("分类编号不可用！");
            return;
        }

        if (StringUtils.isEmpty(name)) {
            nameWrapper.setError("分类名称不能为空！");
            return;
        }

        Category cate = new Category(id, name, desc, StringUtils.getCurTimeStr(), StringUtils.getCurTimeStr());
        dataSource.saveData(cate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong > 0) {
                        CustomToast.normal(mContext, "添加成功！");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        CustomToast.error(mContext, "对不起，添加失败！");
                    }
                });
    }
}
