package com.jdruanjian.pk10.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.allenliu.versionchecklib.core.AVersionService;
import com.jdruanjian.pk10.Constants;
import com.jdruanjian.pk10.R;
import com.jdruanjian.pk10.model.basicmodel.OrderPriceModel;
import com.jdruanjian.pk10.utils.NetworkUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

@SuppressLint("Registered")
public class DemoService extends AVersionService {

    public DemoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        Log.e("DemoService", response);
        //可以在判断版本之后在设置是否强制更新或者VersionParams
        //eg
        // versionParams.isForceUpdate=true;
        showVersionDialog("http://120.77.84.221:19303/PK10SSCZS.APK", "检测到新版本","");
//        or
//        showVersionDialog("http://down1.uc.cn/down2/zxl107821.uc/miaokun1/UCBrowser_V11.5.8.945_android_pf145_bi800_(Build170627172528).apk", "检测到新版本", getString(R.string.updatecontent));

    }


}
