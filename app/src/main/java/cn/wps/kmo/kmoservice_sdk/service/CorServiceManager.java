package cn.wps.kmo.kmoservice_sdk.service;

import android.content.Context;
import android.os.Bundle;
import cn.wps.kmo.kmoservice_sdk.common.TaskData;
import cn.wps.kmo.kmoservice_sdk.common.TaskResult;
import cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper;
import cn.wps.kmo.kmoservice_sdk.utils.HandlerUtil;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import com.xiaomi.milab.videosdk.message.MsgType;

/* loaded from: classes.dex */
public class CorServiceManager {
    public String appType;
    public Context mApplicationContext;
    public CorServiceHelper mCorServiceHelper;
    public boolean mIsInstalled;
    public boolean mIsSupport;
    public String taskName;

    public void init(Context context, String str, String str2) {
        Context applicationContext = context.getApplicationContext();
        this.mApplicationContext = applicationContext;
        this.appType = str;
        this.taskName = str2;
        this.mIsInstalled = isInstalled(applicationContext);
        this.mIsSupport = isSupport(this.mApplicationContext);
    }

    public final boolean isInstalled(Context context) {
        return SdkUtils.isInstalled(context, this.appType);
    }

    public final boolean isSupport(Context context) {
        return SdkUtils.isSupport(context, this.appType);
    }

    public CorServiceHelper getBindServiceHelper() {
        if (this.mCorServiceHelper == null) {
            this.mCorServiceHelper = new CorServiceHelper(this.appType, this.taskName);
        }
        return this.mCorServiceHelper;
    }

    public TaskResult checkAppInvalid() {
        Bundle bundle = new Bundle();
        if (this.mApplicationContext == null) {
            bundle.putString("MSG", "没有初始化，需先执行init()");
            return new TaskResult(MsgType.XMSEXPORT, bundle);
        } else if (!this.mIsInstalled) {
            bundle.putString("MSG", "用户没有安装WPS");
            return new TaskResult(MsgType.XMSTRANSCODE, bundle);
        } else if (!this.mIsSupport) {
            bundle.putString("MSG", "用户安装的WPS不支持pdf转换功能");
            return new TaskResult(MsgType.XMSAUDIOEXTRACT, bundle);
        } else {
            bundle.putString("MSG", "WPS正常，可以开始与wps建立连接");
            return new TaskResult(MsgType.XMSCONTEXT, bundle);
        }
    }

    public void requestPermission(final CorServiceHelper.OnPermissionListener onPermissionListener) {
        if (HandlerUtil.isOnUiThread()) {
            getBindServiceHelper().requestPermission(this.mApplicationContext, onPermissionListener);
        } else {
            HandlerUtil.runOnUiThread(new Runnable() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceManager.1
                @Override // java.lang.Runnable
                public void run() {
                    CorServiceManager.this.getBindServiceHelper().requestPermission(CorServiceManager.this.mApplicationContext, onPermissionListener);
                }
            });
        }
    }

    public void tryToBindService() {
        if (this.mApplicationContext != null) {
            getBindServiceHelper().tryToBindService(this.mApplicationContext, this.appType, this.taskName);
        }
    }

    public void startTask(final TaskData taskData) {
        if (HandlerUtil.isOnUiThread()) {
            startTaskInner(taskData);
        } else {
            HandlerUtil.runOnUiThread(new Runnable() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceManager.2
                @Override // java.lang.Runnable
                public void run() {
                    CorServiceManager.this.startTaskInner(taskData);
                }
            });
        }
    }

    public void startTaskInner(final TaskData taskData) {
        getBindServiceHelper().tryToBindService(this.mApplicationContext, new CorServiceHelper.OnServiceBindListener() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceManager.3
            @Override // cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.OnServiceBindListener
            public void bindCallback(boolean z) {
                if (!z) {
                    Bundle bundle = new Bundle();
                    bundle.putString("MSG", "与wps连接失败");
                    SdkUtils.respCallback(taskData.mCallback, new TaskResult(MsgType.XMSTIMELINE, bundle));
                    return;
                }
                CorServiceManager.this.getBindServiceHelper().callWPSPDFConverter(taskData);
            }
        }, taskData);
    }

    public void dispose(String str, boolean z) {
        if (z) {
            try {
                if (this.mApplicationContext != null && isBindSuccess()) {
                    getBindServiceHelper().unBindService(this.mApplicationContext);
                }
                this.mCorServiceHelper = null;
            } catch (Throwable unused) {
                return;
            }
        }
        CorTaskManager.getInstance().destoryTask(str);
        CorTaskManager.getInstance().removeTask(str);
    }

    public boolean isBindSuccess() {
        return getBindServiceHelper().mIsBindSuccess;
    }
}
