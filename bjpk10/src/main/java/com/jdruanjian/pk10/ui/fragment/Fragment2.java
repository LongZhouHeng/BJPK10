package com.jdruanjian.pk10.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jdruanjian.pk10.BaseFragment;
import com.jdruanjian.pk10.Constants;
import com.jdruanjian.pk10.R;
import com.jdruanjian.pk10.adapter.Random1ResultAdapter;
import com.jdruanjian.pk10.adapter.RandomResultAdapter;
import com.jdruanjian.pk10.model.basicmodel.NewPeriodModel;
import com.jdruanjian.pk10.model.basicmodel.QueryResultModel;
import com.jdruanjian.pk10.model.entity.NewPeriodBean;
import com.jdruanjian.pk10.model.entity.RegisterBean;
import com.jdruanjian.pk10.model.entity.ResultListBean;
import com.jdruanjian.pk10.model.net.BasicResponse;
import com.jdruanjian.pk10.model.net.NewPeroidAPI;
import com.jdruanjian.pk10.utils.NetworkUtils;
import com.jdruanjian.pk10.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Longlong on 2017/12/19.
 */

public class Fragment2 extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.gr_listlot)
    GridView grListlot;
    @BindView(R.id.tv_peroid_1)
    TextView tvPeroid1;
    @BindView(R.id.tv_plan)
    TextView tvPlan;
    @BindView(R.id.peroid_number)
    TextView peroidNumber;
    @BindView(R.id.tv_five_1)
    TextView tvFive1;
    @BindView(R.id.tv_five_2)
    TextView tvFive2;
    @BindView(R.id.tv_five_3)
    TextView tvFive3;
    @BindView(R.id.tv_five_4)
    TextView tvFive4;
    @BindView(R.id.tv_five_5)
    TextView tvFive5;
    @BindView(R.id.tv_five_6)
    TextView tvFive6;
    @BindView(R.id.tv_five_7)
    TextView tvFive7;
    @BindView(R.id.tv_five_8)
    TextView tvFive8;
    @BindView(R.id.tv_five_9)
    TextView tvFive9;
    @BindView(R.id.tv_five_10)
    TextView tvFive10;
    @BindView(R.id.next_time)
    Chronometer nextTime;
    private QueryResultModel model;
    private NewPeriodModel newPeriodModel;
    private ArrayList<NewPeriodBean> newPeriodList;
    //实例化的数组或者集合
    private ArrayList<ResultListBean> resulrlist;
    private RandomResultAdapter mAdapter;
    private Dialog dialog;
    private Random1ResultAdapter mAdapter1;
    private long hours, minute, second, seconds;
    private RegisterBean nextTimeModel;
    private Timer timer;
    private String uid;
    private String planId;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater myInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = myInflater.inflate(R.layout.fragment_2, container, false);
        unbinder = ButterKnife.bind(this, layout);
        setBar(layout);
        setTitlebar(layout, "必中PK10");
        uid = PrefUtils.getString(getActivity().getApplication(), "uid", null);
        tvPlan.setText("A");
        tvPeroid1.setText("3");
        resulrlist = new ArrayList<ResultListBean>();
        mAdapter = new RandomResultAdapter(getActivity(), resulrlist);
        grListlot.setAdapter(mAdapter);
        grListlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                planId = resulrlist.get(position).planId;
                getDetail();
            }
        });
        doRequest();
        getNewPeriod();
        getNextTime();
        timer = new Timer();
        timer.schedule(task, 1000, 1000);

        return layout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            doRequest();
            getNewPeriod();
            getNextTime();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                seconds--;
                //   int temp = seconds%3600;
                hours = seconds / 3600;
                minute = seconds % 3600 / 60;
                second = seconds % 60;
              /*  if(seconds>3600){
                    hours= seconds/3600;
                    if(temp!=0){
                        if(temp>60){
                            minute = temp/60;
                            if(temp%60!=0){
                                second = temp%60;
                            }
                        }else{
                            second = temp;
                        }
                    }
                }else{
                    minute = seconds/60;
                    if(seconds%60!=0){
                        second = seconds%60;
                    }
                }*/
                if (seconds <= 0) {
                    nextTime.setText("00" + " : " + "00" + " : " + "00");
                    getNewPeriod();
                    getNextTime();
                } else {
                    nextTime.setText(String.format(Locale.CHINA, "%02d : %02d : %02d", hours, minute, second));

                }

            }
            super.handleMessage(msg);
        }
    };
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
            System.out.println("SSSSSSSS" + message);
        }
    };

    public void getNextTime() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        //    System.out.println("QQQQQQQQ-----=====" + collegeName);
        OkGo.post(Constants.NEXT_TIME)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("lotteryName", "pk10")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        nextTimeModel = JSON.parseObject(s.getBytes(), RegisterBean.class);
                        if (nextTimeModel.status.equals("0")) {
                            seconds = Long.parseLong(nextTimeModel.data);

                        } else {
                            Toast.makeText(getActivity().getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        Toast.makeText(getActivity().getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getNewPeriod() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }

        NewPeroidAPI api = new NewPeroidAPI(getActivity(), "type_pk10", new BasicResponse.RequestListener() {
            @Override
            public void onComplete(BasicResponse response) {
                if (response.error == BasicResponse.SUCCESS) {
                    newPeriodModel = (NewPeriodModel) response.model;
                    if (newPeriodModel.msgContext.equals("datasSuc")) {
                        peroidNumber.setText(newPeriodModel.datas.get(0).period);
                        String NewNumber = newPeriodModel.datas.get(0).number;
                        String[] num = NewNumber.split(",");
                        tvFive1.setText(num[0]);
                        tvFive2.setText(num[1]);
                        tvFive3.setText(num[2]);
                        tvFive4.setText(num[3]);
                        tvFive5.setText(num[4]);
                        tvFive6.setText(num[5]);
                        tvFive7.setText(num[6]);
                        tvFive8.setText(num[7]);
                        tvFive9.setText(num[8]);
                        tvFive10.setText(num[9]);

                    } else {
                        Toast.makeText(getActivity().getApplication(), newPeriodModel.msg, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    newPeriodModel = (NewPeriodModel) response.model;
                }
            }
        });
        //         api.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
        api.executeRequest(0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        timer.cancel();
        timer = null;
        task.cancel();
        task = null;
    }

    @OnClick({R.id.tv_plans, R.id.tv_peroid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_plans:
                getPlan();
                break;
            case R.id.tv_peroid:
                getPeroid();
                break;
        }
    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        //    System.out.println("QQQQQQQQ-----=====" + collegeName);
        OkGo.post(Constants.QUERY_RESULT)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), QueryResultModel.class);
                        if (model.status.equals("0")) {
                            resulrlist.clear();
                            resulrlist.addAll((ArrayList<ResultListBean>) model.data);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity().getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        Toast.makeText(getActivity().getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getPlan() {
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choice_plan, null);
        TextView tv_plan_a = (TextView) localView.findViewById(R.id.tv_plan_a);
        TextView tv_plan_b = (TextView) localView.findViewById(R.id.tv_plan_b);
        TextView tv_plan_c = (TextView) localView.findViewById(R.id.tv_plan_c);
        TextView tv_plan_d = (TextView) localView.findViewById(R.id.tv_plan_d);
        TextView tv_plan_e = (TextView) localView.findViewById(R.id.tv_plan_e);
        TextView tv_plan_f = (TextView) localView.findViewById(R.id.tv_plan_f);
        TextView tv_plan_g = (TextView) localView.findViewById(R.id.tv_plan_g);
        TextView tv_plan_h = (TextView) localView.findViewById(R.id.tv_plan_h);
        TextView tv_plan_i = (TextView) localView.findViewById(R.id.tv_plan_i);
        TextView tv_plan_j = (TextView) localView.findViewById(R.id.tv_plan_j);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_plan_a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("A");
                dialog.dismiss();
            }
        });
        tv_plan_b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("B");
                dialog.dismiss();
            }
        });
        tv_plan_c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("C");
                dialog.dismiss();
            }
        });
        tv_plan_d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("D");
                dialog.dismiss();
            }
        });
        tv_plan_e.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("E");
                dialog.dismiss();
            }
        });
        tv_plan_f.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("F");
                dialog.dismiss();
            }
        });
        tv_plan_g.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("G");
                dialog.dismiss();
            }
        });
        tv_plan_h.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("H");
                dialog.dismiss();
            }
        });
        tv_plan_i.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("I");
                dialog.dismiss();
            }
        });
        tv_plan_j.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPlan.setText("J");
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void getPeroid() {
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choice_peroid, null);
        TextView tv_plan_1 = (TextView) localView.findViewById(R.id.tv_plan_1);
        TextView tv_plan_2 = (TextView) localView.findViewById(R.id.tv_plan_2);
        TextView tv_plan_3 = (TextView) localView.findViewById(R.id.tv_plan_3);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_plan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPeroid1.setText("1");
                dialog.dismiss();
            }
        });
        tv_plan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPeroid1.setText("2 ");
                dialog.dismiss();
            }
        });
        tv_plan_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tvPeroid1.setText("3");
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void getDetail() {

        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_lot_detail, null);
        RelativeLayout tv_cancel = (RelativeLayout) localView.findViewById(R.id.tv_cancel);
        WebView lv = (WebView) localView.findViewById(R.id.lv);
        lv.loadUrl("http://www.bzjihua.com/app/index.html?planInfoId=" + planId + "&planNum=" + tvPlan.getText().toString() +
                "&phase=" + tvPeroid1.getText().toString() + "&uid=" + uid + "&lotteryName" + "pk10");
        WebSettings webSettings = lv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        lv.addJavascriptInterface(new AndroidJsInterface(getActivity()), "android");//注意：这里一定
        lv.setWebViewClient(new myWebViewClient());
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        // 设置全屏
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

    }

    private class AndroidJsInterface {
        private Context mContext;

        private AndroidJsInterface(Context activity) {
            this.mContext = activity;
        }

        @JavascriptInterface//系统sdk 版本在v4.2以上时，必须加这个注解（安全性）
        public void fun1(String str, String str1, String str2) {
           /* //点击H5的某个区域，实现APP的跳转（退出WebpageActivity）
            if (PrefUtils.getString(getActivity().getApplication(), "uid", null) != null) {
                Intent intent = new Intent(getActivity(), Pay_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", str);//lottery_name
                bundle.putString("title", str1);//幸运飞艇
                bundle.putString("title1", str2);//一期计划
                bundle.putString("intent", "intent1");
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,4);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }*/

        }

        @JavascriptInterface//系统sdk 版本在v4.2以上时，必须加这个注解（安全性）
        public void all_lots(String str1, String str2) {
            //点击H5的某个区域，实现APP的跳转（退出WebpageActivity）
           /* if (PrefUtils.getString(getActivity().getApplication(), "uid", null) != null) {
                Intent intent_all = new Intent(getActivity(), Pay_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("alllots_1",str1);//所有彩种
                bundle.putString("allplans_1",str2);//全部计划
                bundle.putString("intent","all_1");
                intent_all.putExtras(bundle);
                getActivity().startActivityForResult(intent_all,4);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }*/

        }
    }
}
