package cn.wps.kmo.kmoservice_sdk.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import ch.qos.logback.classic.Level;
import cn.wps.kmo.kmoservice_sdk.common.TaskData;
import cn.wps.kmo.kmoservice_sdk.common.TaskResult;
import cn.wps.kmo.kmoservice_sdk.utils.CommonUtils;
import cn.wps.kmo.kmoservice_sdk.utils.HandlerUtil;
import cn.wps.kmo.kmoservice_sdk.utils.KmoInfoConstant;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import cn.wps.kmo.kmoservice_sdk.utils.ThreadExecutorControl;
import cn.wps.kmo.kmoservice_sdk.utils.TypeUtils;
import cn.wps.moffice.service.exposed.QueryBinderService;
import cn.wps.moffice.service.exposed.permission.RequestPermissionCallback;
import cn.wps.moffice.service.exposed.permission.RequestPermissionService;
import com.xiaomi.milab.videosdk.message.MsgType;
import com.xiaomi.stat.d;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class CorServiceHelper {
    public String appType;
    public Context mContext;
    public boolean mIsBindSuccess;
    public boolean mIsBinding;
    public boolean mIsPermissionGranted;
    public OnPermissionListener mOnPermissionListener;
    public QueryBinderService mQueryBinderService;
    public RequestPermissionService mRequestPermissionService;
    public ThreadExecutorControl mThreadExecutorControl;
    public String taskName;
    public final int MAX_CONNECT_TIME = Level.INFO_INT;
    public List<TaskData> mWaitingTaskLists = new ArrayList();
    public ConcurrentHashMap<String, IBinder> cacheIBinders = new ConcurrentHashMap<>();
    public Runnable exceConnectRunnable = new Runnable() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.1
        @Override // java.lang.Runnable
        public void run() {
            CorServiceHelper corServiceHelper = CorServiceHelper.this;
            if (!corServiceHelper.mIsBindSuccess) {
                try {
                    corServiceHelper.mContext.unbindService(CorServiceHelper.this.taskServiceConnect);
                } catch (Exception unused) {
                }
                CorServiceHelper.this.mIsBinding = false;
            }
        }
    };
    public ServiceConnection taskServiceConnect = new ServiceConnection() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                CorServiceHelper corServiceHelper = CorServiceHelper.this;
                corServiceHelper.mIsBindSuccess = true;
                corServiceHelper.mIsBinding = false;
                corServiceHelper.mQueryBinderService = QueryBinderService.Stub.asInterface(iBinder);
                CorServiceHelper corServiceHelper2 = CorServiceHelper.this;
                corServiceHelper2.mRequestPermissionService = RequestPermissionService.Stub.asInterface(corServiceHelper2.queryBinder(TypeUtils.REQUEST_PERMISSION));
                CorServiceHelper.this.mRequestPermissionService.requestPermisson(new RequestPermissionCallback.Stub() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.2.1
                    @Override // cn.wps.moffice.service.exposed.permission.RequestPermissionCallback
                    public void callback(boolean z) {
                        CorServiceHelper corServiceHelper3 = CorServiceHelper.this;
                        corServiceHelper3.mIsPermissionGranted = z;
                        if (corServiceHelper3.mOnPermissionListener != null) {
                            CorServiceHelper.this.mOnPermissionListener.requestPermission(z);
                        }
                        if (z) {
                            CorServiceHelper corServiceHelper4 = CorServiceHelper.this;
                            corServiceHelper4.processWaitingList(corServiceHelper4.isBindSuccess());
                        }
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            CorTaskManager.getInstance().destoryTask(CorServiceHelper.this.taskName);
            CorTaskManager.getInstance().removeTask(CorServiceHelper.this.taskName);
            CorServiceFac.getInstance().removeCorServiceManager(CorServiceHelper.this.taskName, CorServiceHelper.this.appType);
            CorServiceHelper corServiceHelper = CorServiceHelper.this;
            corServiceHelper.mQueryBinderService = null;
            corServiceHelper.mIsBindSuccess = false;
            corServiceHelper.mIsBinding = false;
            corServiceHelper.mIsPermissionGranted = false;
            corServiceHelper.mRequestPermissionService = null;
            corServiceHelper.cacheIBinders.clear();
            CorServiceHelper corServiceHelper2 = CorServiceHelper.this;
            corServiceHelper2.processWaitingList(corServiceHelper2.isBindSuccess());
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            CorTaskManager.getInstance().destoryTask(CorServiceHelper.this.taskName);
            CorTaskManager.getInstance().removeTask(CorServiceHelper.this.taskName);
            CorServiceFac.getInstance().removeCorServiceManager(CorServiceHelper.this.taskName, CorServiceHelper.this.appType);
            CorServiceHelper corServiceHelper = CorServiceHelper.this;
            corServiceHelper.mQueryBinderService = null;
            corServiceHelper.mIsBindSuccess = false;
            corServiceHelper.mIsBinding = false;
            corServiceHelper.mIsPermissionGranted = false;
            corServiceHelper.mRequestPermissionService = null;
            corServiceHelper.cacheIBinders.clear();
        }
    };

    /* loaded from: classes.dex */
    public interface OnPermissionListener {
        void requestPermission(boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnServiceBindListener {
        void bindCallback(boolean z);
    }

    public CorServiceHelper(String str, String str2) {
        this.appType = str;
        this.taskName = str2;
    }

    public void bindService(Context context) {
        this.mContext = context;
        String wpsLiteAction = KmoInfoConstant.getWpsLiteAction(this.appType, this.taskName);
        String serviceWpsLite = KmoInfoConstant.getServiceWpsLite(this.appType, this.taskName);
        String packageWpsName = KmoInfoConstant.getPackageWpsName(this.appType);
        if (TextUtils.isEmpty(wpsLiteAction) || TextUtils.isEmpty(serviceWpsLite)) {
            return;
        }
        Intent intent = new Intent(wpsLiteAction);
        intent.setPackage(packageWpsName);
        intent.setFlags(268435456);
        intent.setClassName(packageWpsName, serviceWpsLite);
        context.bindService(intent, this.taskServiceConnect, 1);
        HandlerUtil.runOnUIThreadDelay(this.exceConnectRunnable, 20000L);
    }

    public boolean isBindSuccess() {
        return this.mIsBindSuccess && this.mQueryBinderService != null;
    }

    public void checkBind(Context context) {
        if (!this.mIsBinding) {
            bindService(context);
            this.mIsBinding = true;
        }
    }

    public void tryToBindService(Context context, String str, String str2) {
        if (!isBindSuccess()) {
            checkBind(context);
        }
    }

    public void tryToBindService(Context context, OnServiceBindListener onServiceBindListener, TaskData taskData) {
        if (!isBindSuccess() || !this.mIsPermissionGranted) {
            checkBind(context);
            this.mWaitingTaskLists.add(taskData);
        } else if (onServiceBindListener == null) {
        } else {
            onServiceBindListener.bindCallback(isBindSuccess());
        }
    }

    public void requestPermission(Context context, final OnPermissionListener onPermissionListener) {
        RequestPermissionService requestPermissionService;
        boolean z;
        boolean z2 = this.mIsBindSuccess;
        if (z2 && (z = this.mIsPermissionGranted)) {
            if (onPermissionListener == null) {
                return;
            }
            onPermissionListener.requestPermission(z);
        } else if (!z2) {
            this.mOnPermissionListener = onPermissionListener;
            checkBind(context);
        } else if (this.mIsPermissionGranted || (requestPermissionService = this.mRequestPermissionService) == null) {
        } else {
            try {
                requestPermissionService.requestPermisson(new RequestPermissionCallback.Stub() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.3
                    @Override // cn.wps.moffice.service.exposed.permission.RequestPermissionCallback
                    public void callback(boolean z3) throws RemoteException {
                        CorServiceHelper.this.mIsPermissionGranted = z3;
                        OnPermissionListener onPermissionListener2 = onPermissionListener;
                        if (onPermissionListener2 != null) {
                            onPermissionListener2.requestPermission(z3);
                        }
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void processWaitingList(boolean z) {
        try {
            if (this.mWaitingTaskLists == null) {
                return;
            }
            for (int i = 0; i < this.mWaitingTaskLists.size(); i++) {
                TaskData taskData = this.mWaitingTaskLists.get(i);
                if (taskData != null) {
                    if (z) {
                        if (this.mIsPermissionGranted) {
                            callWPSPDFConverter(taskData);
                        } else if (taskData.mCallback != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("MSG", "获取读写权限失败");
                            SdkUtils.respCallback(taskData.mCallback, new TaskResult(10006, bundle));
                        }
                    } else if (taskData.mCallback != null) {
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("MSG", "与wps连接失败");
                        SdkUtils.respCallback(taskData.mCallback, new TaskResult(MsgType.XMSTIMELINE, bundle2));
                    }
                }
            }
            this.mWaitingTaskLists.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callWPSPDFConverter(final TaskData taskData) {
        if (!CorTaskManager.getInstance().getCorTask(taskData.taskName).prepareTask(taskData)) {
            getTaskExecutor().submit(new Runnable() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.4
                @Override // java.lang.Runnable
                public void run() {
                    CorServiceHelper.this.doWPSPDFConverter(taskData);
                }
            });
        }
    }

    public void doWPSPDFConverter(final TaskData taskData) {
        if (taskData == null) {
            return;
        }
        try {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                CorTaskManager.getInstance().getCorTask(taskData.taskName).startTask(queryBinder(taskData.taskName), taskData);
                long currentTimeMillis2 = System.currentTimeMillis();
                CommonUtils.log("wps process document cost time: " + (currentTimeMillis2 - currentTimeMillis) + d.H);
            } catch (Throwable unused) {
                HandlerUtil.runOnUiThread(new Runnable() { // from class: cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.5
                    @Override // java.lang.Runnable
                    public void run() {
                        if (taskData != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("MSG", "wps在文档转换时发生异常");
                            SdkUtils.respCallback(taskData.mCallback, new TaskResult(10007, bundle));
                        }
                    }
                });
            }
        } finally {
            CorTaskManager.getInstance().getCorTask(taskData.taskName).taskFinish(taskData);
        }
    }

    public ExecutorService getTaskExecutor() {
        if (this.mThreadExecutorControl == null) {
            this.mThreadExecutorControl = new ThreadExecutorControl();
        }
        return this.mThreadExecutorControl.getCorTaskExecutor();
    }

    public void unBindService(Context context) {
        try {
            if (this.mIsBindSuccess || this.mQueryBinderService != null) {
                this.mIsBindSuccess = false;
                context.unbindService(this.taskServiceConnect);
            }
            this.mThreadExecutorControl = null;
            this.mIsBinding = false;
            this.cacheIBinders.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(String str) {
        IBinder iBinder;
        IBinder iBinder2 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (this.cacheIBinders.containsKey(str) && (iBinder = this.cacheIBinders.get(str)) != null) {
            return iBinder;
        }
        try {
            QueryBinderService queryBinderService = this.mQueryBinderService;
            if (queryBinderService == null) {
                return null;
            }
            iBinder2 = queryBinderService.queryBinder(str);
            this.cacheIBinders.put(str, iBinder2);
            return iBinder2;
        } catch (RemoteException unused) {
            return iBinder2;
        }
    }
}
