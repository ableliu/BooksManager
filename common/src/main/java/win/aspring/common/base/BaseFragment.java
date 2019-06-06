package win.aspring.common.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import win.aspring.common.R;
import win.aspring.common.widget.CustomToast;
import win.aspring.common.widget.dialog.DialogControl;

/**
 * 碎片基类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @date 2014年9月25日 上午11:18:46
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    /**
     * 上下文
     */
    protected Context mContext;
    protected Activity mActivity;

    protected LayoutInflater mInflater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    public abstract void initView(View view);

    public abstract void initData();

    @Override
    public void onClick(View v) {

    }

    /**
     * 展示Toast
     *
     * @param message 展示信息
     */
    public void showToast(String message) {
        CustomToast.normal(mContext, message);
    }

    /**
     * 展示提醒Toast
     *
     * @param message 展示信息
     */
    public void showInfoToast(String message) {
        CustomToast.info(mContext, message);
    }

    /**
     * 展示错误Toast
     *
     * @param message 展示信息
     */
    public void showErrorToast(String message) {
        CustomToast.error(mContext, message);
    }
}
