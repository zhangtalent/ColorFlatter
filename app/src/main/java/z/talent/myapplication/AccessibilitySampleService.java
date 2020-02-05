package z.talent.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_TEXT;

public class AccessibilitySampleService extends AccessibilityService {
    // 1、创建OkHttpClient对象
    OkHttpClient okHttpClient = new OkHttpClient();
    String data = "";
    String username = "";
    //static volatile String userNAME = "镂空";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = event.getPackageName().toString();
        int eventType = event.getEventType();
        // AccessibilityOperator封装了辅助功能的界面查找与模拟点击事件等操作
        Log.d("NAME", Contrast.username+"----");


        if(pkgName.equals("z.talent.myapplication")){
            AccessibilityNodeInfo root = getRootInActiveWindow();
            List<AccessibilityNodeInfo> tvs = root.findAccessibilityNodeInfosByViewId("z.talent.myapplication:id/ed");
            List<AccessibilityNodeInfo> bts = root.findAccessibilityNodeInfosByViewId("z.talent.myapplication:id/bt");
            if(eventType == TYPE_VIEW_CLICKED){
                if(tvs.size()>0) username = tvs.get(0).getText().toString();
                Log.d("NAME", username+"----");
            }
        }

        if(pkgName.equals("com.tencent.mm")){
            AccessibilityNodeInfo root = getRootInActiveWindow();

            List<AccessibilityNodeInfo> nodeInfos = root.findAccessibilityNodeInfosByText(username);

            if(nodeInfos.size()>0){
                List<AccessibilityNodeInfo> tvs = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aqe");

                try {
                    getData();
                    Log.d("data",data);
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, data+"\n\n来自张天才的彩虹屁-机器人");
                    if(!data.equals(""))tvs.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    List<AccessibilityNodeInfo> SendNodes = root.findAccessibilityNodeInfosByText("发送");
                    if (SendNodes != null) {
                        for (int i = 0; i < SendNodes.size(); i++) {
                            SendNodes.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    public void getData() {
        // 2、创建Request对象
        Request request = new Request.Builder().url("https://chp.shadiao.app/api.php?from=sunbelife").build();
        // 3、通过okHttpClient的newCall方法获得一个Call对象
        Call call = okHttpClient.newCall(request);
        // 4、请求
        // 同步请求
        //Response response = call.execute();
        // 异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 子线程
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 子线程
                data = response.body().string();
            }
        });
    }
}
