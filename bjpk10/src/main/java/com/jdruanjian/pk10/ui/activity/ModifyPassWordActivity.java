package com.jdruanjian.pk10.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jdruanjian.pk10.BaseActivity;
import com.jdruanjian.pk10.Constants;
import com.jdruanjian.pk10.MainActivity;
import com.jdruanjian.pk10.R;
import com.jdruanjian.pk10.model.entity.RegisterBean;
import com.jdruanjian.pk10.utils.NetworkUtils;
import com.jdruanjian.pk10.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Longlong on 2017/12/21.
 */

@SuppressLint("Registered")
public class ModifyPassWordActivity extends BaseActivity {


    @BindView(R.id.et_oldpassword)
    EditText etOldpassword;
    @BindView(R.id.et_newpassword)
    EditText etNewpassword;
    @BindView(R.id.et_ensurepwd)
    EditText etEnsurepwd;
    @BindView(R.id.btn_ensure)
    Button btnEnsure;
    private RegisterBean model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        ButterKnife.bind(this);
        setBar();
        showBackBtn();

    }

    @OnClick(R.id.btn_ensure)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(etNewpassword.getText().toString()) &&
                etNewpassword.getText().toString().equals(etEnsurepwd.getText().toString())) {
            doRequest();
        } else {
            Toast.makeText(getApplication(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        }

    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        //    System.out.println("QQQQQQQQ-----=====" + collegeName);
        OkGo.post(Constants.LOGIN_MODIFY)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("appId", "1")
                .params("pwd", etOldpassword.getText().toString())
                .params("newPwd",etNewpassword.getText().toString())
                .params("userId", PrefUtils.getString(getApplication(),"uid",null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), RegisterBean.class);
                        if (model.status.equals("0")) {
                            finish();
                            Toast.makeText(getApplication(), model.data, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        Toast.makeText(getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
