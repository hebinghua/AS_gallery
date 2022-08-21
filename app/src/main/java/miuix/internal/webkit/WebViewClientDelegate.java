package miuix.internal.webkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import miuix.internal.hybrid.provider.AbsWebView;
import miuix.internal.util.UrlResolverHelper;
import miuix.util.UrlResolver;

/* loaded from: classes3.dex */
public class WebViewClientDelegate {
    public DeviceAccountLogin mAccountLogin;
    public LoginState mLoginState;
    public boolean mSupportAutoLogin;
    public boolean mSupportDeepLink;

    /* loaded from: classes3.dex */
    public enum LoginState {
        LOGIN_START,
        LOGIN_INPROGRESS,
        LOGIN_FINISHED
    }

    public WebViewClientDelegate() {
        this(-1);
    }

    public WebViewClientDelegate(int i) {
        this(i, -1);
    }

    public WebViewClientDelegate(int i, int i2) {
        this.mLoginState = LoginState.LOGIN_FINISHED;
        int i3 = (i & i2) | ((~i2) & (-1));
        boolean z = false;
        this.mSupportDeepLink = (i3 & 1) != 0;
        this.mSupportAutoLogin = (i3 & 2) != 0 ? true : z;
    }

    public boolean shouldOverrideUrlLoading(AbsWebView absWebView, String str) {
        if (this.mSupportDeepLink && UrlResolverHelper.isMiUrl(str)) {
            Context context = absWebView.getContext();
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.addCategory("android.intent.category.BROWSABLE");
            ResolveInfo checkMiuiIntent = UrlResolver.checkMiuiIntent(context, packageManager, intent);
            if (checkMiuiIntent == null) {
                return false;
            }
            if (checkMiuiIntent.activityInfo == null) {
                return true;
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public void onPageStarted(AbsWebView absWebView, String str, Bitmap bitmap) {
        if (this.mSupportAutoLogin && this.mLoginState == LoginState.LOGIN_START) {
            this.mLoginState = LoginState.LOGIN_INPROGRESS;
        }
    }

    public void onPageFinished(AbsWebView absWebView, String str) {
        if (this.mSupportAutoLogin && this.mLoginState == LoginState.LOGIN_INPROGRESS) {
            this.mLoginState = LoginState.LOGIN_FINISHED;
            this.mAccountLogin.onLoginPageFinished();
        }
    }

    public static Activity getActivityContextFromView(View view) {
        Context context = ((ViewGroup) view.getRootView()).getChildAt(0).getContext();
        if (context.getClass().getName().contains("com.android.internal.policy.DecorContext")) {
            try {
                Field declaredField = context.getClass().getDeclaredField("mPhoneWindow");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(context);
                Object invoke = obj.getClass().getMethod("getContext", new Class[0]).invoke(obj, new Object[0]);
                if (invoke != null && (invoke instanceof Context)) {
                    context = (Context) invoke;
                }
            } catch (Exception e) {
                Log.e("WebViewClientDelegate", e.getMessage());
            }
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        Log.i("WebViewClientDelegate", "fail to get activity");
        return null;
    }

    public void onReceivedLoginRequest(AbsWebView absWebView, String str, String str2, String str3) {
        Activity activityContextFromView;
        if (this.mSupportAutoLogin && (activityContextFromView = getActivityContextFromView(absWebView.getRootView())) != null) {
            if (this.mAccountLogin == null) {
                this.mAccountLogin = new DefaultDeviceAccountLogin(activityContextFromView, absWebView);
            }
            if (absWebView.canGoForward()) {
                if (absWebView.canGoBack()) {
                    absWebView.goBack();
                    return;
                } else {
                    activityContextFromView.finish();
                    return;
                }
            }
            this.mLoginState = LoginState.LOGIN_START;
            absWebView.setVisibility(4);
            this.mAccountLogin.login(str, str2, str3);
        }
    }
}
