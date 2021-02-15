package com.ichangemycity.ichangemycommunity.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ichangemycity.ichangemycommunity.R;
import com.ichangemycity.ichangemycommunity.utils.LogUtils;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG;
    private ProgressDialog mProgressDialog;
    protected Toolbar mToolbar;


    protected abstract int getContentViewId();

    protected abstract void onCreateActivity(Bundle bundle);

    public BaseActivity() {
        this.TAG = getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.debug(this.TAG, "* onCreateActivity");
        setContentView(getContentViewId());
        ButterKnife.bind((Activity) this);
        //TODO enable this for toolbar support
//        this.mToolbar = (Toolbar) ButterKnife.findById((Activity) this, (int) R.id.toolbar);
//        if (this.mToolbar != null) {
//            setSupportActionBar(this.mToolbar);
//        }
        onCreateActivity(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.debug(this.TAG, "* onResumeActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.debug(this.TAG, "* onDestroyActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.debug(this.TAG, "* onPause");
        hideSoftKeyboard();
    }

    public void showToast(String text) {
        if (text != null) {
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            LogUtils.debug(this.TAG, "toast: " + text);
        }
    }

    public void showToast(int res) {
        showToast(getString(res));
    }

    protected void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (!(this instanceof MainActivity) && id == android.R.id.home) {
//            onBackPressed();
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void showProgressDialog(String s) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setCanceledOnTouchOutside(false);
        }
        this.mProgressDialog.setMessage(s);
        this.mProgressDialog.show();
    }

    protected void showFragment(Fragment fragment, int containerViewId, boolean isFirstFragmentInContainer) {

        if (isFirstFragmentInContainer) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment, null);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            String fragmentTag = fragment.getClass().getSimpleName();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    protected void showProgressDialog(int res) {
        showProgressDialog(getString(res));
    }

    protected Boolean getProgressDialogState() {
        boolean z = this.mProgressDialog != null && this.mProgressDialog.isShowing();
        return Boolean.valueOf(z);
    }

    protected void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }


    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showInfoDialog(String title, String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", listener).show();
    }

    public void showProgressDialog() {
        mProgressDialog = ProgressDialog.show(this, "Please wait ...", "Loading...", true);
        mProgressDialog.setCancelable(false);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    public void toogleView(View toShow, View toHide) {
        toShow.setVisibility(View.VISIBLE);
        toHide.setVisibility(View.GONE);

    }

}
