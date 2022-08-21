package miuix.internal.hybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.xiaomi.stat.b.h;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import miuix.hybrid.Callback;
import miuix.hybrid.HybridChromeClient;
import miuix.hybrid.HybridFeature;
import miuix.hybrid.HybridSettings;
import miuix.hybrid.HybridView;
import miuix.hybrid.HybridViewClient;
import miuix.hybrid.LifecycleListener;
import miuix.hybrid.NativeInterface;
import miuix.hybrid.PageContext;
import miuix.hybrid.Request;
import miuix.hybrid.Response;

/* loaded from: classes3.dex */
public class HybridManager {
    public static ExecutorService sPool = Executors.newCachedThreadPool();
    public static String sUserAgent;
    public Activity mActivity;
    public boolean mDetached;
    public FeatureManager mFM;
    public NativeInterface mNativeInterface;
    public PermissionManager mPM;
    public PageContext mPageContext;
    public HybridView mView;
    public Set<LifecycleListener> mListeners = new CopyOnWriteArraySet();
    public ConcurrentHashMap<String, Request> mReqMap = new ConcurrentHashMap<>();

    public HybridManager(Activity activity, HybridView hybridView) {
        this.mActivity = activity;
        this.mView = hybridView;
    }

    public void init(int i, String str) {
        this.mNativeInterface = new NativeInterface(this);
        Config loadConfig = loadConfig(i);
        config(loadConfig, false);
        initView();
        if (str == null && !TextUtils.isEmpty(loadConfig.getContent())) {
            str = resolveUri(loadConfig.getContent());
        }
        if (str != null) {
            this.mView.loadUrl(str);
        }
    }

    public void clear() {
        this.mReqMap.clear();
    }

    public final Config loadConfig(int i) {
        XmlConfigParser createFromResId;
        try {
            if (i == 0) {
                createFromResId = XmlConfigParser.create(this.mActivity);
            } else {
                createFromResId = XmlConfigParser.createFromResId(this.mActivity, i);
            }
            return createFromResId.parse(null);
        } catch (HybridException e) {
            throw new RuntimeException("cannot load config: " + e.getMessage());
        }
    }

    public final String config(Config config, boolean z) {
        if (z) {
            SecurityManager securityManager = new SecurityManager(config, this.mActivity.getApplicationContext());
            if (securityManager.isExpired() || !securityManager.isValidSignature()) {
                return new Response(202).toString();
            }
        }
        this.mFM = new FeatureManager(config, this.mActivity.getClassLoader());
        this.mPM = new PermissionManager(config);
        return new Response(0).toString();
    }

    public String config(String str) {
        try {
            return config(JsonConfigParser.createFromString(str).parse(null), true);
        } catch (HybridException e) {
            return new Response(201, e.getMessage()).toString();
        }
    }

    public void setPageContext(PageContext pageContext) {
        this.mPageContext = pageContext;
    }

    public final void initView() {
        initSettings(this.mView.getSettings());
        this.mView.setHybridViewClient(new HybridViewClient());
        this.mView.setHybridChromeClient(new HybridChromeClient());
        this.mView.addJavascriptInterface(new JsInterface(this), "MiuiJsBridge");
        this.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: miuix.internal.hybrid.HybridManager.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                HybridManager.this.mDetached = false;
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                HybridManager.this.mDetached = true;
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public final void initSettings(HybridSettings hybridSettings) {
        hybridSettings.setJavaScriptEnabled(true);
        hybridSettings.setUserAgentString(getUserAgent(hybridSettings.getUserAgentString()));
    }

    public final String getUserAgent(String str) {
        if (sUserAgent == null) {
            StringBuilder sb = new StringBuilder(str);
            sb.append(" XiaoMi/HybridView/");
            Activity activity = this.mActivity;
            sb.append(getPackageInfo(activity, activity.getApplication().getPackageName()).versionName);
            sb.append(" ");
            sb.append(this.mActivity.getPackageName());
            sb.append(h.g);
            Activity activity2 = this.mActivity;
            sb.append(getPackageInfo(activity2, activity2.getPackageName()).versionName);
            sUserAgent = sb.toString();
        }
        return sUserAgent;
    }

    public static PackageInfo getPackageInfo(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final String resolveUri(String str) {
        if (Pattern.compile("^[a-z-]+://").matcher(str).find()) {
            return str;
        }
        if (str.charAt(0) == '/') {
            str = str.substring(1);
        }
        return "file:///android_asset/hybrid/" + str;
    }

    public final HybridFeature buildFeature(String str) throws HybridException {
        if (!this.mPM.isValid(this.mPageContext.getUrl())) {
            throw new HybridException(203, "feature not permitted: " + str);
        }
        return this.mFM.lookupFeature(str);
    }

    public final Request buildRequest(String str, String str2, String str3) {
        Request request = new Request();
        request.setAction(str2);
        request.setRawParams(str3);
        request.setPageContext(this.mPageContext);
        request.setView(this.mView);
        request.setNativeInterface(this.mNativeInterface);
        return request;
    }

    public String lookup(String str, String str2) {
        try {
            if (buildFeature(str).getInvocationMode(buildRequest(str, str2, null)) == null) {
                return new Response((int) Response.CODE_ACTION_ERROR, "action not supported: " + str2).toString();
            }
            return new Response(0).toString();
        } catch (HybridException e) {
            return e.getResponse().toString();
        }
    }

    public String invoke(String str, String str2, String str3, String str4) {
        try {
            HybridFeature buildFeature = buildFeature(str);
            Request buildRequest = buildRequest(str, str2, str3);
            HybridFeature.Mode invocationMode = buildFeature.getInvocationMode(buildRequest);
            if (invocationMode == HybridFeature.Mode.SYNC) {
                callback(new Response(1), this.mPageContext, str4);
                return buildFeature.invoke(buildRequest).toString();
            }
            String key = getKey(str, str2, str3, str4);
            this.mReqMap.put(key, buildRequest);
            if (invocationMode == HybridFeature.Mode.ASYNC) {
                sPool.execute(new AsyncInvocation(this, buildFeature, key, str4));
                return new Response(2).toString();
            }
            buildRequest.setCallback(new Callback(this, this.mPageContext, str4));
            sPool.execute(new AsyncInvocation(this, buildFeature, key, str4));
            return new Response(3).toString();
        } catch (HybridException e) {
            Response response = e.getResponse();
            callback(response, this.mPageContext, str4);
            return response.toString();
        }
    }

    public final String getKey(String str, String str2, String str3, String str4) {
        return str + str2 + str3 + str4;
    }

    public boolean isDetached() {
        return this.mDetached;
    }

    /* loaded from: classes3.dex */
    public static class AsyncInvocation implements Runnable {
        public HybridFeature mFeature;
        public WeakReference<HybridManager> mHybridManager;
        public String mJsCallback;
        public String mRequestKey;

        public AsyncInvocation(HybridManager hybridManager, HybridFeature hybridFeature, String str, String str2) {
            this.mHybridManager = new WeakReference<>(hybridManager);
            this.mFeature = hybridFeature;
            this.mRequestKey = str;
            this.mJsCallback = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Request request;
            HybridManager hybridManager = this.mHybridManager.get();
            if (hybridManager != null && (request = (Request) hybridManager.mReqMap.remove(this.mRequestKey)) != null && !hybridManager.getActivity().isFinishing() && !hybridManager.getActivity().isDestroyed()) {
                Response invoke = this.mFeature.invoke(request);
                if (this.mFeature.getInvocationMode(request) != HybridFeature.Mode.ASYNC) {
                    return;
                }
                hybridManager.callback(invoke, request.getPageContext(), this.mJsCallback);
            }
        }
    }

    public void callback(Response response, PageContext pageContext, String str) {
        if (response == null || TextUtils.isEmpty(str) || !pageContext.equals(this.mPageContext) || this.mDetached || this.mActivity.isFinishing()) {
            return;
        }
        if (Log.isLoggable("hybrid", 3)) {
            Log.d("hybrid", "non-blocking response is " + response.toString());
        }
        this.mActivity.runOnUiThread(new JsInvocation(response, str));
    }

    /* loaded from: classes3.dex */
    public class JsInvocation implements Runnable {
        public String mJsCallback;
        public Response mResponse;

        public JsInvocation(Response response, String str) {
            this.mResponse = response;
            this.mJsCallback = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            String buildCallbackJavascript = HybridManager.this.buildCallbackJavascript(this.mResponse, this.mJsCallback);
            HybridView hybridView = HybridManager.this.mView;
            hybridView.loadUrl("javascript:" + buildCallbackJavascript);
        }
    }

    public final String buildCallbackJavascript(Response response, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str + "('" + response.toString().replace("\\", "\\\\").replace("'", "\\'") + "');";
    }

    public Activity getActivity() {
        return this.mActivity;
    }

    public void addLifecycleListener(LifecycleListener lifecycleListener) {
        this.mListeners.add(lifecycleListener);
    }

    public void removeLifecycleListener(LifecycleListener lifecycleListener) {
        this.mListeners.remove(lifecycleListener);
    }

    public void onPageChange() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onPageChange();
        }
    }

    public void onStart() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onStart();
        }
    }

    public void onResume() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onResume();
        }
    }

    public void onPause() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onPause();
        }
    }

    public void onStop() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onStop();
        }
    }

    public void onDestroy() {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onDestroy();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        for (LifecycleListener lifecycleListener : this.mListeners) {
            lifecycleListener.onActivityResult(i, i2, intent);
        }
    }
}
