package com.miui.gallery.hybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.miui.gallery.R;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.hybrid.GalleryHybridFragment;
import com.miui.gallery.hybrid.HybridLoadingProgressView;
import com.miui.gallery.hybrid.hybridclient.HybridClient;
import com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase;
import com.miui.gallery.hybrid.pulltorefresh.PullToRefreshWebView;
import com.miui.gallery.request.HostManager;
import com.miui.gallery.ui.BaseFragment;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.webkit.WebViewClient;

/* loaded from: classes2.dex */
public class GalleryHybridFragment extends BaseFragment implements HybridLoadingProgressView.HybridOnRefreshListener {
    public boolean mDisableNavigationBarStrategy;
    public HybridClient mHybridClient;
    public HybridViewEventListener mHybridViewEventListener;
    public HybridLoadingProgressView mLoadingProgressView;
    public HybridLoadingProgressView.HybridLoadingState mLoadingState;
    public boolean mNetworkConnected;
    public NetworkConnectivityChangedReceiver mNetworkConnectivityReceiver;
    public PullToRefreshWebView mPullToRefreshWebView;
    public WebView mWebview;

    /* loaded from: classes2.dex */
    public interface HybridViewEventListener {
        void onReceivedTitle(String str);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "hybrid";
    }

    @Override // com.miui.gallery.hybrid.HybridLoadingProgressView.HybridOnRefreshListener
    public void onRefresh() {
        reload();
    }

    public void setHybridClient(HybridClient hybridClient) {
        this.mHybridClient = hybridClient;
        hybridClient.bindWebViewFragment(this);
        configureWebView();
    }

    public void setHybridViewEventListener(HybridViewEventListener hybridViewEventListener) {
        this.mHybridViewEventListener = hybridViewEventListener;
    }

    public void setDisableNavigationBarStrategy(boolean z) {
        this.mDisableNavigationBarStrategy = z;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mDisableNavigationBarStrategy) {
            requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        }
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.hybrid_view, viewGroup, false);
        PullToRefreshWebView pullToRefreshWebView = (PullToRefreshWebView) inflate.findViewById(R.id.hybrid_view);
        this.mPullToRefreshWebView = pullToRefreshWebView;
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment.1
            {
                GalleryHybridFragment.this = this;
            }

            @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.OnRefreshListener
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                GalleryHybridFragment.this.mWebview.reload();
            }
        });
        this.mWebview = this.mPullToRefreshWebView.getRefreshableView();
        HybridLoadingProgressView hybridLoadingProgressView = (HybridLoadingProgressView) inflate.findViewById(R.id.loading_view);
        this.mLoadingProgressView = hybridLoadingProgressView;
        hybridLoadingProgressView.onInit(false, false, this);
        return inflate;
    }

    public void load() {
        HybridClient hybridClient = this.mHybridClient;
        if (hybridClient == null) {
            DefaultLogger.e("GalleryHybridFragment", "HybridClient is null");
        } else {
            hybridClient.getActualPath(new HybridClient.ActualPathCallback() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment.2
                {
                    GalleryHybridFragment.this = this;
                }

                @Override // com.miui.gallery.hybrid.hybridclient.HybridClient.ActualPathCallback
                public void onGetActualPath(String str) {
                    if (TextUtils.isEmpty(str)) {
                        DefaultLogger.e("GalleryHybridFragment", "The url should not be null, load nothing");
                    } else {
                        GalleryHybridFragment.this.loadUrl(str);
                    }
                }
            });
        }
    }

    public void loadUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("GalleryHybridFragment", "The url should not be null, load nothing");
            return;
        }
        initLoadingState();
        this.mWebview.loadUrl(str);
    }

    public final void initLoadingState() {
        this.mLoadingProgressView.setIndeterminate(false);
        this.mLoadingProgressView.setProgress(10);
        this.mLoadingProgressView.onStartLoading(false);
        this.mLoadingState = HybridLoadingProgressView.HybridLoadingState.OK;
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        registerConnectivityReceiver();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        unregisterConnectivityReceiver();
        super.onDetach();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mWebview.onPause();
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mNetworkConnected = BaseNetworkUtils.isNetworkConnected();
        this.mWebview.onResume();
    }

    @SuppressLint({"JavascriptInterface"})
    public final void configureWebView() {
        WebView webView;
        HybridClient hybridClient = this.mHybridClient;
        if (hybridClient == null || (webView = this.mWebview) == null) {
            return;
        }
        hybridClient.onStartConfigWebView(webView);
        this.mHybridClient.onConfigWebSettings(this.mWebview.getSettings());
        this.mWebview.setWebViewClient(this.mHybridClient.getWebViewClient(new GalleryHybridWebViewClient()));
        this.mWebview.setWebChromeClient(this.mHybridClient.getWebChromeClient(new GalleryHybridChromeClient()));
        this.mWebview.setDownloadListener(this.mHybridClient.getDownloadListener(null));
        List<HybridClient.JsInterfacePair> javascriptInterfaces = this.mHybridClient.getJavascriptInterfaces();
        if (BaseMiscUtil.isValid(javascriptInterfaces)) {
            for (HybridClient.JsInterfacePair jsInterfacePair : javascriptInterfaces) {
                this.mWebview.addJavascriptInterface(jsInterfacePair.obj, jsInterfacePair.name);
            }
        }
        if (!this.mHybridClient.isSupportPullToRefresh()) {
            this.mPullToRefreshWebView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
        this.mHybridClient.onPostConfigWebView(this.mWebview);
        this.mWebview.requestFocus();
    }

    /* loaded from: classes2.dex */
    public class GalleryHybridWebViewClient extends WebViewClient {
        public GalleryHybridWebViewClient() {
            GalleryHybridFragment.this = r1;
        }

        @Override // miuix.webkit.WebViewClient, android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "shouldOverrideUrlLoading: already detached, do nothing");
                return false;
            }
            GalleryHybridFragment.this.mLoadingState = HybridLoadingProgressView.HybridLoadingState.OK;
            GalleryHybridFragment.this.mLoadingProgressView.setProgress(10);
            GalleryHybridFragment.this.mLoadingProgressView.onStartLoading(false);
            if (HostManager.isGalleryUrl(str)) {
                webView.loadUrl(str);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, str);
        }

        @Override // miuix.webkit.WebViewClient, android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        @Override // miuix.webkit.WebViewClient, android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onPageFinished: already detached, do nothing");
                return;
            }
            super.onPageFinished(webView, str);
            GalleryHybridFragment.this.mPullToRefreshWebView.onRefreshComplete();
            if (GalleryHybridFragment.this.mLoadingState != HybridLoadingProgressView.HybridLoadingState.OK) {
                GalleryHybridFragment.this.mLoadingProgressView.onError(false, GalleryHybridFragment.this.mLoadingState, null);
                GalleryHybridFragment.this.mPullToRefreshWebView.setVisibility(8);
                return;
            }
            GalleryHybridFragment.this.mLoadingProgressView.onStopLoading(false);
            GalleryHybridFragment.this.mLoadingProgressView.setVisibility(8);
            GalleryHybridFragment.this.mPullToRefreshWebView.setVisibility(0);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onReceivedError: already detached, do nothing");
                return;
            }
            super.onReceivedError(webView, i, str, str2);
            GalleryHybridFragment.this.mPullToRefreshWebView.onRefreshComplete();
            if (BaseNetworkUtils.isNetworkConnected()) {
                GalleryHybridFragment.this.mLoadingState = HybridLoadingProgressView.HybridLoadingState.SERVICE_ERROR;
                return;
            }
            GalleryHybridFragment.this.mLoadingState = HybridLoadingProgressView.HybridLoadingState.NETWORK_ERROR;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.cancel();
            DefaultLogger.e("GalleryHybridFragment", "ssl error %s", sslError);
        }
    }

    /* loaded from: classes2.dex */
    public class GalleryHybridChromeClient extends WebChromeClient {
        public static /* synthetic */ void $r8$lambda$SwpNkUHF4TlNvVEDCXPuehb6OJc(JsResult jsResult, DialogInterface dialogInterface, int i) {
            jsResult.cancel();
        }

        public static /* synthetic */ void $r8$lambda$WwguusfpBRgyGW638thuOjPBKE0(JsResult jsResult, DialogInterface dialogInterface, int i) {
            jsResult.confirm();
        }

        /* renamed from: $r8$lambda$ZrFataGDgdg5sYgvOfbJW-D9_7Q */
        public static /* synthetic */ void m996$r8$lambda$ZrFataGDgdg5sYgvOfbJWD9_7Q(JsResult jsResult, DialogInterface dialogInterface) {
            jsResult.cancel();
        }

        @Override // android.webkit.WebChromeClient
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return true;
        }

        public GalleryHybridChromeClient() {
            GalleryHybridFragment.this = r1;
        }

        @Override // android.webkit.WebChromeClient
        public void onReceivedTitle(WebView webView, String str) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onReceivedTitle: already detached, do nothing");
                return;
            }
            super.onReceivedTitle(webView, str);
            if (GalleryHybridFragment.this.mHybridViewEventListener == null) {
                return;
            }
            GalleryHybridFragment.this.mHybridViewEventListener.onReceivedTitle(str);
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            callback.invoke(str, true, false);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onJsConfirm: already detached, do nothing");
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(GalleryHybridFragment.this.mActivity);
            String title = webView.getTitle();
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(str2);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment$GalleryHybridChromeClient$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    GalleryHybridFragment.GalleryHybridChromeClient.$r8$lambda$WwguusfpBRgyGW638thuOjPBKE0(jsResult, dialogInterface, i);
                }
            });
            builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment$GalleryHybridChromeClient$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    GalleryHybridFragment.GalleryHybridChromeClient.$r8$lambda$SwpNkUHF4TlNvVEDCXPuehb6OJc(jsResult, dialogInterface, i);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment$GalleryHybridChromeClient$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    GalleryHybridFragment.GalleryHybridChromeClient.m996$r8$lambda$ZrFataGDgdg5sYgvOfbJWD9_7Q(jsResult, dialogInterface);
                }
            });
            builder.show();
            return true;
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onJsAlert: already detached, do nothing");
                return false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(GalleryHybridFragment.this.mActivity);
            String title = webView.getTitle();
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.setMessage(str2);
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment.GalleryHybridChromeClient.1
                {
                    GalleryHybridChromeClient.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    jsResult.confirm();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.hybrid.GalleryHybridFragment.GalleryHybridChromeClient.2
                {
                    GalleryHybridChromeClient.this = this;
                }

                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    jsResult.cancel();
                }
            });
            builder.show();
            return true;
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onJsPrompt: already detached, do nothing");
                return false;
            } else if (!"MiuiGallery-finish-queuing".equals(str2)) {
                return false;
            } else {
                GalleryHybridFragment.this.mActivity.setResult(-1, null);
                GalleryHybridFragment.this.mActivity.finish();
                jsPromptResult.confirm();
                return true;
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int i) {
            DefaultLogger.d("GalleryHybridFragment", "onProgressChanged: " + i);
            if (GalleryHybridFragment.this.mActivity == null) {
                DefaultLogger.d("GalleryHybridFragment", "onProgressChanged: already detached, do nothing");
                return;
            }
            super.onProgressChanged(webView, i);
            if (GalleryHybridFragment.this.mLoadingProgressView == null || i - GalleryHybridFragment.this.mLoadingProgressView.getProgress() <= 0 || i < 0 || i > 100) {
                return;
            }
            GalleryHybridFragment.this.mLoadingProgressView.setProgress(i);
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        WebView webView = this.mWebview;
        if (webView != null) {
            ((ViewGroup) webView.getParent()).removeView(this.mWebview);
            this.mWebview.destroy();
            this.mWebview = null;
        }
        super.onDestroy();
    }

    public boolean onBackPressed() {
        WebBackForwardList copyBackForwardList;
        int stepsToGoBack;
        int currentIndex;
        HybridViewEventListener hybridViewEventListener;
        if (!this.mWebview.canGoBack() || (stepsToGoBack = stepsToGoBack()) > (currentIndex = (copyBackForwardList = this.mWebview.copyBackForwardList()).getCurrentIndex())) {
            return false;
        }
        String title = copyBackForwardList.getItemAtIndex(currentIndex - stepsToGoBack).getTitle();
        if (!TextUtils.isEmpty(title) && (hybridViewEventListener = this.mHybridViewEventListener) != null) {
            hybridViewEventListener.onReceivedTitle(title);
        }
        this.mWebview.goBackOrForward(-stepsToGoBack);
        return true;
    }

    public final int stepsToGoBack() {
        WebBackForwardList copyBackForwardList = this.mWebview.copyBackForwardList();
        int currentIndex = copyBackForwardList.getCurrentIndex();
        int i = 1;
        for (int i2 = 0; i2 <= currentIndex; i2++) {
            if (TextUtils.equals(this.mWebview.getUrl(), copyBackForwardList.getItemAtIndex(currentIndex - i2).getUrl())) {
                break;
            }
            i++;
        }
        return i;
    }

    public final void registerConnectivityReceiver() {
        DefaultLogger.d("GalleryHybridFragment", "Register network connectivity changed receiver");
        if (this.mNetworkConnectivityReceiver == null) {
            this.mNetworkConnectivityReceiver = new NetworkConnectivityChangedReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mActivity.registerReceiver(this.mNetworkConnectivityReceiver, intentFilter);
    }

    public final void unregisterConnectivityReceiver() {
        DefaultLogger.d("GalleryHybridFragment", "Unregister network connectivity changed receiver");
        this.mActivity.unregisterReceiver(this.mNetworkConnectivityReceiver);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        DefaultLogger.d("GalleryHybridFragment", "onActivityResult");
        this.mHybridClient.onActivityResult(i, i2, intent);
    }

    /* loaded from: classes2.dex */
    public class NetworkConnectivityChangedReceiver extends BroadcastReceiver {
        public NetworkConnectivityChangedReceiver() {
            GalleryHybridFragment.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean isNetworkConnected = BaseNetworkUtils.isNetworkConnected();
            if (!GalleryHybridFragment.this.mNetworkConnected && isNetworkConnected) {
                GalleryHybridFragment.this.reload();
            }
            GalleryHybridFragment.this.mNetworkConnected = isNetworkConnected;
        }
    }

    public final void reload() {
        if (!TextUtils.isEmpty(this.mWebview.getUrl())) {
            loadUrl(this.mWebview.getUrl());
        }
    }
}
