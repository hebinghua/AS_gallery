package com.miui.mishare.app.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.mishare.IMiShareDiscoverCallback;
import com.miui.mishare.IMiShareStateListener;
import com.miui.mishare.IMiShareTaskStateListener;
import com.miui.mishare.MiShareDeviceModel;
import com.miui.mishare.MiShareTask;
import com.miui.mishare.R$bool;
import com.miui.mishare.R$dimen;
import com.miui.mishare.R$drawable;
import com.miui.mishare.R$id;
import com.miui.mishare.R$layout;
import com.miui.mishare.R$string;
import com.miui.mishare.RemoteDevice;
import com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter;
import com.miui.mishare.app.connect.MiShareGalleryConnectivity;
import com.miui.mishare.app.model2.MiShareDevice;
import com.miui.mishare.app.util.FolmeUtil;
import com.miui.mishare.app.util.MiShareFileUtil;
import com.miui.mishare.app.util.NearbyUtils;
import com.miui.mishare.app.util.PrintHelper;
import com.miui.mishare.app.view.popup.GuidePopupWindow2;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class MiShareGalleryTransferView extends LinearLayout implements View.OnClickListener, MiShareGalleryDeviceAdapter.OnDeviceClickListener, MiShareGalleryConnectivity.ServiceBindCallBack {
    public MiShareGalleryDeviceAdapter mAdapter;
    public View mBottomDivider;
    public MiShareGalleryConnectivity mConnectivity;
    public Context mContext;
    public int mCurrentDeviceStatus;
    public String mDeviceId;
    public View mDeviceLayout;
    public RecyclerView mDeviceLv;
    public IMiShareDiscoverCallback.Stub mDiscoverCallback;
    public boolean mDiscovering;
    public TextView mDiscoveringTipsTv;
    public View mDividerView;
    public List<Uri> mFiles;
    public final Handler mHandler;
    public FilesNotYetSetListener mHasNoFilesListener;
    public Intent mIntent;
    public boolean mIsPrint;
    public View.OnClickListener mMiPrintClick;
    public View mMiShareDisabled;
    public View mNearbyLayout;
    public ImageButton mPrintBtn;
    public boolean mPrintEnabled;
    public ImageButton mScreenThrow;
    public View.OnClickListener mScreenThrowClick;
    public View mSendToLayout;
    public MiShareTaskStateReceiver mShareTaskStateReceive;
    public boolean mShareToNearby;
    public boolean mShowDivider;
    public int mState;
    public IMiShareStateListener mStateListener;
    public IMiShareTaskStateListener.Stub mTaskStateListener;
    public boolean mThrowEnabled;
    public View mToDiscoverView;

    /* loaded from: classes3.dex */
    public interface FilesNotYetSetListener {
        void fileNotYetSet();
    }

    public final int getDeviceType(byte b) {
        if (b < 20 || b > 29) {
            if (b >= 10 && b <= 19) {
                return b == 11 ? 4 : 3;
            } else if (b >= 50 && b <= 59) {
                return 6;
            } else {
                if (b >= 41 && b <= 45) {
                    return 7;
                }
                if (b >= 80 && b <= 89) {
                    return 8;
                }
                if (b >= 60 && b <= 69) {
                    return 9;
                }
                if (b >= -86 && b <= -77) {
                    return 10;
                }
                if (b >= -96 && b <= -87) {
                    return b == -96 ? 12 : 11;
                } else if (b >= 90 && b <= 95) {
                    return 13;
                } else {
                    if (b >= 70 && b <= 75) {
                        return 14;
                    }
                    if (b >= 100 && b <= 109) {
                        return 15;
                    }
                    return b == 32 ? 5 : 1;
                }
            }
        }
        return 2;
    }

    public MiShareGalleryTransferView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mState = 0;
        this.mHandler = new Handler();
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(getContext()).inflate(R$layout.view_mishare_transfer_gallery, this);
        this.mConnectivity = new MiShareGalleryConnectivity(getContext());
        findViewById(R$id.tv_help).setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R$id.iv_throwing_screen);
        this.mScreenThrow = imageButton;
        imageButton.setOnClickListener(this);
        ImageButton imageButton2 = (ImageButton) findViewById(R$id.iv_printer);
        this.mPrintBtn = imageButton2;
        imageButton2.setOnClickListener(this);
        int i = 8;
        this.mPrintBtn.setVisibility(hasPrinter() ? 0 : 8);
        this.mMiShareDisabled = findViewById(R$id.rl_prompt_switch);
        this.mDividerView = findViewById(R$id.view_middle_divider);
        this.mSendToLayout = findViewById(R$id.rl_send_to);
        this.mDeviceLayout = findViewById(R$id.rl_devices);
        this.mBottomDivider = findViewById(R$id.bottom_divider);
        this.mNearbyLayout = findViewById(R$id.rl_nearby);
        FolmeUtil.handleTouchOf(getContext(), this.mPrintBtn);
        FolmeUtil.handleTouchOf(getContext(), this.mScreenThrow);
        MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter = new MiShareGalleryDeviceAdapter();
        this.mAdapter = miShareGalleryDeviceAdapter;
        miShareGalleryDeviceAdapter.registerListener(this);
        this.mDeviceLv = (RecyclerView) findViewById(R$id.lv_scanned_device);
        this.mToDiscoverView = findViewById(R$id.rl_to_discover_view);
        this.mDiscoveringTipsTv = (TextView) findViewById(R$id.tv_discover_tips);
        this.mDeviceLv.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.mDeviceLv.setAdapter(this.mAdapter);
        final int dimensionPixelOffset = getResources().getDimensionPixelOffset(R$dimen.divider_gallery_device_half);
        if (this.mDeviceLv.getItemDecorationCount() > 0) {
            this.mDeviceLv.removeItemDecorationAt(0);
        }
        this.mDeviceLv.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.1
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view, androidx.recyclerview.widget.RecyclerView recyclerView, RecyclerView.State state) {
                int i2 = dimensionPixelOffset;
                rect.right = i2;
                rect.left = i2;
            }
        });
        this.mDeviceLv.setItemAnimator(new DefaultItemAnimator());
        boolean z = getResources().getBoolean(R$bool.show_top_divider);
        this.mShowDivider = z;
        View view = this.mDividerView;
        if (z) {
            i = 0;
        }
        view.setVisibility(i);
        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R$dimen.send_to_margin_start) - ((getResources().getDimensionPixelOffset(R$dimen.size_wave) - getResources().getDimensionPixelOffset(R$dimen.size_scanned_device)) / 2);
        this.mDeviceLv.setPaddingRelative(dimensionPixelOffset2, 0, dimensionPixelOffset2, 0);
        View findViewById = findViewById(R$id.btn_enable_midrop);
        findViewById.setOnClickListener(this);
        FolmeUtil.handleTouchOf(getContext(), findViewById);
        initMiShareStatus();
        if (NearbyUtils.supportNearby(getContext())) {
            this.mNearbyLayout.setVisibility(0);
            View findViewById2 = findViewById(R$id.btn_nearby);
            findViewById2.setOnClickListener(this);
            FolmeUtil.handleTouchOf(getContext(), findViewById2);
            View view2 = this.mDividerView;
            if (view2 != null) {
                ((ViewGroup.MarginLayoutParams) view2.getLayoutParams()).topMargin = getResources().getDimensionPixelSize(R$dimen.transfer_view_middle_divider_margin_top_nearby);
            }
            ((ViewGroup.MarginLayoutParams) this.mBottomDivider.getLayoutParams()).bottomMargin = 0;
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        post(new Runnable() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.2
            @Override // java.lang.Runnable
            public void run() {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mSendToLayout.getLayoutParams();
                Resources resources = MiShareGalleryTransferView.this.getResources();
                int i = R$dimen.send_to_margin_start;
                int dimensionPixelSize = resources.getDimensionPixelSize(i);
                marginLayoutParams.setMarginStart(dimensionPixelSize);
                marginLayoutParams.setMarginEnd(dimensionPixelSize);
                MiShareGalleryTransferView.this.mSendToLayout.setLayoutParams(marginLayoutParams);
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mPrintBtn.getLayoutParams();
                marginLayoutParams2.setMarginEnd(MiShareGalleryTransferView.this.getResources().getDimensionPixelSize(R$dimen.btn_printer_margin_end));
                MiShareGalleryTransferView.this.mPrintBtn.setLayoutParams(marginLayoutParams2);
                ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mDeviceLayout.getLayoutParams();
                marginLayoutParams3.height = MiShareGalleryTransferView.this.getResources().getDimensionPixelSize(R$dimen.device_area_height);
                MiShareGalleryTransferView.this.mDeviceLayout.setLayoutParams(marginLayoutParams3);
                ViewGroup.MarginLayoutParams marginLayoutParams4 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mDeviceLv.getLayoutParams();
                marginLayoutParams4.topMargin = MiShareGalleryTransferView.this.getResources().getDimensionPixelSize(R$dimen.device_list_margin_top);
                int dimensionPixelOffset = (MiShareGalleryTransferView.this.getResources().getDimensionPixelOffset(i) - MiShareGalleryTransferView.this.getResources().getDimensionPixelOffset(R$dimen.divider_gallery_device_half)) - MiShareGalleryTransferView.this.getResources().getDimensionPixelOffset(R$dimen.device_name_margin_horizon);
                MiShareGalleryTransferView.this.mDeviceLv.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
                MiShareGalleryTransferView.this.mDeviceLv.setLayoutParams(marginLayoutParams4);
                MiShareGalleryTransferView.this.mDeviceLv.scrollToPosition(0);
                ViewGroup.MarginLayoutParams marginLayoutParams5 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mMiShareDisabled.getLayoutParams();
                marginLayoutParams5.setMarginStart(dimensionPixelSize);
                marginLayoutParams5.setMarginEnd(dimensionPixelSize);
                MiShareGalleryTransferView.this.mMiShareDisabled.setLayoutParams(marginLayoutParams5);
                ViewGroup.MarginLayoutParams marginLayoutParams6 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mBottomDivider.getLayoutParams();
                marginLayoutParams6.setMarginStart(dimensionPixelSize);
                marginLayoutParams6.setMarginEnd(dimensionPixelSize);
                MiShareGalleryTransferView.this.mBottomDivider.setLayoutParams(marginLayoutParams6);
                ViewGroup.MarginLayoutParams marginLayoutParams7 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mNearbyLayout.getLayoutParams();
                marginLayoutParams7.setMarginStart(dimensionPixelSize);
                marginLayoutParams7.setMarginEnd(dimensionPixelSize);
                MiShareGalleryTransferView.this.mNearbyLayout.setLayoutParams(marginLayoutParams7);
                MiShareGalleryTransferView miShareGalleryTransferView = MiShareGalleryTransferView.this;
                miShareGalleryTransferView.mShowDivider = miShareGalleryTransferView.getResources().getBoolean(R$bool.show_top_divider);
                if (MiShareGalleryTransferView.this.mShowDivider) {
                    if (MiShareGalleryTransferView.this.mDeviceLv.getVisibility() == 0) {
                        MiShareGalleryTransferView.this.mDividerView.setVisibility(4);
                    } else {
                        MiShareGalleryTransferView.this.mDividerView.setVisibility(0);
                    }
                }
                if (MiShareGalleryTransferView.this.mShowDivider) {
                    ViewGroup.MarginLayoutParams marginLayoutParams8 = (ViewGroup.MarginLayoutParams) MiShareGalleryTransferView.this.mDividerView.getLayoutParams();
                    marginLayoutParams8.setMarginStart(dimensionPixelSize);
                    marginLayoutParams8.setMarginEnd(dimensionPixelSize);
                    MiShareGalleryTransferView.this.mDividerView.setLayoutParams(marginLayoutParams8);
                }
                MiShareGalleryTransferView.this.requestLayout();
            }
        });
    }

    public final void registerStateReceiver() {
        this.mShareTaskStateReceive = new MiShareTaskStateReceiver(this.mAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.mishare.connectivity.TASK_STATE");
        this.mShareTaskStateReceive.onReceive(getContext(), getContext().registerReceiver(this.mShareTaskStateReceive, intentFilter));
    }

    public final void unregisterStateReceiver() {
        if (this.mShareTaskStateReceive != null) {
            getContext().unregisterReceiver(this.mShareTaskStateReceive);
        }
    }

    public final void initMiShareStatus() {
        this.mMiShareDisabled.setVisibility(0);
        this.mDeviceLv.setVisibility(8);
    }

    /* loaded from: classes3.dex */
    public static final class MiShareStateListener extends IMiShareStateListener.Stub {
        public final WeakReference<Activity> mActivity;
        public final WeakReference<MiShareGalleryTransferView> mView;

        public MiShareStateListener(Activity activity, MiShareGalleryTransferView miShareGalleryTransferView) {
            this.mActivity = new WeakReference<>(activity);
            this.mView = new WeakReference<>(miShareGalleryTransferView);
        }

        @Override // com.miui.mishare.IMiShareStateListener
        public void onStateChanged(final int i) {
            Activity activity = this.mActivity.get();
            final MiShareGalleryTransferView miShareGalleryTransferView = this.mView.get();
            if (activity == null || miShareGalleryTransferView == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.MiShareStateListener.1
                @Override // java.lang.Runnable
                public void run() {
                    MiShareGalleryTransferView miShareGalleryTransferView2 = miShareGalleryTransferView;
                    miShareGalleryTransferView2.refreshView(miShareGalleryTransferView2.mState, i);
                    miShareGalleryTransferView.mState = i;
                }
            });
        }
    }

    public final void refreshView(int i, int i2) {
        if (i <= 3 && i2 >= 4) {
            onStartDiscover();
        } else if (i <= 2 && i2 == 3) {
            startDiscoverIfNeeded();
        } else if (i2 != 1) {
        } else {
            onStopDiscover();
        }
    }

    public final void setEmptyDevice() {
        updateView(true, false);
        this.mAdapter.setInitialAddDevice();
    }

    public final void setStopDevice() {
        this.mAdapter.stopSort();
        updateView(true, true);
    }

    public final void updateView(boolean z, boolean z2) {
        if (this.mShowDivider) {
            this.mDividerView.setVisibility(0);
        }
        animView(this.mMiShareDisabled, z);
        animView(this.mToDiscoverView, z2);
        animView(this.mDiscoveringTipsTv, !z2);
        animView(this.mDeviceLv, !z);
    }

    public final void animView(View view, boolean z) {
        if (z) {
            animShow(view);
        } else {
            animHide(view);
        }
    }

    public final void animShow(final View view) {
        if (view.getVisibility() == 0) {
            return;
        }
        view.clearAnimation();
        if (view.getAnimation() != null) {
            view.getAnimation().cancel();
        }
        view.setVisibility(0);
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(100L);
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setAlpha(1.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                view.setAlpha(1.0f);
            }
        });
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        duration.start();
    }

    public final void animHide(final View view) {
        if (view.getVisibility() == 8) {
            return;
        }
        view.clearAnimation();
        if (view.getAnimation() != null) {
            view.getAnimation().cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(100L);
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setVisibility(8);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(8);
            }
        });
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        duration.start();
    }

    public final void startDiscoverIfNeeded() {
        if (!this.mDiscovering) {
            this.mDiscovering = true;
            Intent intent = this.mIntent;
            if (intent != null) {
                this.mConnectivity.startDiscoverWithIntent(this.mDiscoverCallback, intent);
            } else {
                this.mConnectivity.startDiscover(this.mDiscoverCallback);
            }
        }
    }

    public final void onStartDiscover() {
        startDiscoverIfNeeded();
        this.mConnectivity.registerTaskStateListener(this.mTaskStateListener);
        if (this.mAdapter.getItemCount() == 0) {
            setEmptyDevice();
        }
    }

    public void onStopDiscover() {
        this.mDiscovering = false;
        this.mConnectivity.unregisterTaskStateListener(this.mTaskStateListener);
        setStopDevice();
    }

    public final boolean hasPrinter() {
        Intent intent = new Intent();
        intent.setClassName("com.android.printspooler", "com.android.printspooler.ui.MiuiPrintActivity");
        PackageManager packageManager = getContext().getPackageManager();
        return packageManager != null && packageManager.queryIntentActivities(intent, 0).size() > 0;
    }

    public static boolean isServiceAvailable(Context context) {
        return MiShareGalleryConnectivity.isAvailable(context);
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public void bind() {
        Log.d("MishareGalleryTransferView", "MISHARE SDK VERSION: 1.5.2");
        this.mStateListener = new MiShareStateListener((Activity) this.mContext, this);
        this.mDiscoverCallback = new MiShareDiscoverCallback(this);
        this.mTaskStateListener = new MiShareTaskStateListener(this);
        this.mConnectivity.bind(this);
        registerStateReceiver();
    }

    public void unbind() {
        Log.d("MishareGalleryTransferView", "unbind");
        if (this.mConnectivity.checkServiceBound()) {
            this.mConnectivity.unregisterStateListener(this.mStateListener);
            this.mConnectivity.stopDiscover(this.mDiscoverCallback);
            this.mState = 0;
        }
        this.mDiscovering = false;
        this.mConnectivity.unbind();
        this.mAdapter.stopSort();
        this.mAdapter.clear();
        unregisterStateReceiver();
    }

    public void setFiles(List<Uri> list) {
        this.mFiles = list;
        if (list == null || list.size() != 1) {
            setMiPrintEnable(false);
        } else {
            setMiPrintEnable(isFileCanPrint(list.get(0)));
        }
    }

    public final void sendToDevice(MiShareDevice miShareDevice) {
        if (miShareDevice == null) {
            return;
        }
        List<Uri> list = this.mFiles;
        if ((list == null || list.isEmpty()) && this.mHasNoFilesListener != null) {
            miShareDevice.deviceStatus = 1;
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        miShareDevice.files = this.mFiles;
        MiShareTask miShareTask = new MiShareTask();
        miShareTask.device = miShareDevice.remoteDevice;
        miShareTask.clipData = getClipData(miShareDevice.files);
        miShareTask.taskId = miShareDevice.taskId;
        miShareTask.count = miShareDevice.files.size();
        this.mConnectivity.sendData(miShareTask);
        miShareDevice.deviceStatus = this.mCurrentDeviceStatus;
        this.mAdapter.notifyDataSetChanged();
    }

    public void sendFiles(List<Uri> list) {
        setFiles(list);
        if (this.mIsPrint) {
            this.mIsPrint = false;
            print();
        } else if (this.mShareToNearby) {
            this.mShareToNearby = false;
            try {
                getContext().startActivity(NearbyUtils.getShareIntent(getContext(), list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MiShareDevice deviceById = this.mAdapter.getDeviceById(this.mDeviceId);
            if (deviceById == null) {
                showToast(getContext().getResources().getString(R$string.device_offline));
            } else {
                sendToDevice(deviceById);
            }
        }
    }

    public void setFileIfNotYet(FilesNotYetSetListener filesNotYetSetListener) {
        this.mHasNoFilesListener = filesNotYetSetListener;
    }

    public void setDeliveryTitle(String str) {
        ((TextView) findViewById(R$id.tv_send_file_to)).setText(str);
    }

    public final boolean isFileCanPrint(Uri uri) {
        return MiShareFileUtil.isImageCanPrint(getContext(), uri) || MiShareFileUtil.isFilePdf(getContext(), uri);
    }

    public void setScreenThrowClickListener(View.OnClickListener onClickListener) {
        this.mScreenThrowClick = onClickListener;
    }

    public void setScreenThrowEnable(boolean z) {
        this.mThrowEnabled = z;
        this.mScreenThrow.setImageResource(z ? R$drawable.ic_cast : R$drawable.ic_cast_disabled);
    }

    public void setMiPrintEnable(boolean z) {
        this.mPrintEnabled = z;
        this.mPrintBtn.setImageResource(z ? R$drawable.ic_printer : R$drawable.ic_printer_disabled);
    }

    public void setScreenThrowHighLight(boolean z) {
        if (z) {
            this.mScreenThrow.setBackground(getContext().getResources().getDrawable(R$drawable.bg_screen_throw_hight_light));
            this.mScreenThrow.getDrawable().setTint(-1);
            return;
        }
        this.mScreenThrow.setBackground(getContext().getResources().getDrawable(R$drawable.bg_mishare_tranfer_btn));
        this.mScreenThrow.setImageResource(R$drawable.ic_cast);
    }

    public void setMiPrintClickListener(View.OnClickListener onClickListener) {
        this.mMiPrintClick = onClickListener;
    }

    @Override // com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter.OnDeviceClickListener
    public void onDeviceTaskRetry(MiShareDevice miShareDevice) {
        if (miShareDevice.isPC()) {
            miShareDevice.taskId = MiShareDevice.generatePCTaskId();
        }
        send(miShareDevice);
    }

    @Override // com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter.OnDeviceClickListener
    public void onCancelTask(MiShareDevice miShareDevice) {
        this.mConnectivity.cancel(miShareDevice);
    }

    public final void send(MiShareDevice miShareDevice) {
        if (miShareDevice == null) {
            return;
        }
        this.mShareToNearby = false;
        this.mIsPrint = false;
        this.mCurrentDeviceStatus = 5;
        this.mDeviceId = miShareDevice.deviceId;
        FilesNotYetSetListener filesNotYetSetListener = this.mHasNoFilesListener;
        if (filesNotYetSetListener == null) {
            return;
        }
        filesNotYetSetListener.fileNotYetSet();
    }

    @Override // com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter.OnDeviceClickListener
    public void onDeviceTaskStart(MiShareDevice miShareDevice) {
        send(miShareDevice);
    }

    public static ClipData getClipData(List<Uri> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        ClipData clipData = new ClipData(new ClipDescription("mishare data", new String[]{""}), new ClipData.Item(list.get(0)));
        int size = list.size();
        for (int i = 1; i < size; i++) {
            Uri uri = list.get(i);
            if (uri != null) {
                clipData.addItem(new ClipData.Item(uri));
            }
        }
        return clipData;
    }

    public final boolean isMishareTransfering() {
        return Settings.System.getInt(getContext().getContentResolver(), "mishare_in_transfering", 0) == 1;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.btn_enable_midrop) {
            startDiscoverIfNeeded();
        } else if (id == R$id.iv_throwing_screen) {
            if (isMishareTransfering()) {
                Toast.makeText(getContext(), R$string.can_not_transfer_when_screening, 0).show();
            } else if (this.mThrowEnabled) {
                View.OnClickListener onClickListener = this.mScreenThrowClick;
                if (onClickListener == null) {
                    return;
                }
                onClickListener.onClick(view);
            } else {
                showToast(getResources().getString(R$string.file_cannot_screen_throw));
            }
        } else if (id == R$id.iv_printer) {
            if (this.mPrintEnabled) {
                this.mIsPrint = true;
                FilesNotYetSetListener filesNotYetSetListener = this.mHasNoFilesListener;
                if (filesNotYetSetListener != null) {
                    filesNotYetSetListener.fileNotYetSet();
                    return;
                } else {
                    print();
                    return;
                }
            }
            showToast(getResources().getString(R$string.file_cannot_print));
        } else if (id == R$id.tv_help) {
            if (MiShareDeviceModel.supportUwb()) {
                Intent intent = new Intent("com.miui.mishare.ACTION_SHOW_FIND_DEVICE_GUIDE");
                intent.setPackage("com.miui.mishare.connectivity");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                    return;
                } else {
                    showGuide(view);
                    return;
                }
            }
            showGuide(view);
        } else if (id != R$id.btn_nearby) {
        } else {
            shareByGoogleNearby();
        }
    }

    public final void showGuide(View view) {
        GuidePopupWindow2 guidePopupWindow2 = new GuidePopupWindow2(getContext());
        if (view.getLayoutDirection() == 1) {
            guidePopupWindow2.setArrowMode(64);
        } else {
            guidePopupWindow2.setArrowMode(32);
        }
        guidePopupWindow2.setGuideText(R$string.help_content);
        guidePopupWindow2.show(view, 0, 0, false);
    }

    public final void print() {
        if (canPrint()) {
            doPrint();
            View.OnClickListener onClickListener = this.mMiPrintClick;
            if (onClickListener == null) {
                return;
            }
            onClickListener.onClick(this.mPrintBtn);
            return;
        }
        showToast(getResources().getString(R$string.file_cannot_print));
    }

    public final void showToast(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public final boolean canPrint() {
        Uri uri;
        List<Uri> list = this.mFiles;
        return (list == null || list.size() > 1 || this.mFiles.size() == 0 || (uri = this.mFiles.get(0)) == null || !MiShareFileUtil.isImageCanPrint(getContext(), uri)) ? false : true;
    }

    public final void doPrint() {
        List<Uri> list = this.mFiles;
        if (list == null || list.size() != 1) {
            return;
        }
        PrintHelper printHelper = new PrintHelper(getContext());
        printHelper.setScaleMode(1);
        Uri uri = this.mFiles.get(0);
        String fileNameFromUri = MiShareFileUtil.getFileNameFromUri(uri);
        if (!MiShareFileUtil.isImageCanPrint(getContext(), uri)) {
            return;
        }
        try {
            printHelper.printBitmap("MIUI:" + fileNameFromUri, uri, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final void shareByGoogleNearby() {
        this.mShareToNearby = true;
        FilesNotYetSetListener filesNotYetSetListener = this.mHasNoFilesListener;
        if (filesNotYetSetListener != null) {
            filesNotYetSetListener.fileNotYetSet();
        }
    }

    /* loaded from: classes3.dex */
    public static final class MiShareDiscoverCallback extends IMiShareDiscoverCallback.Stub {
        public final WeakReference<MiShareGalleryTransferView> mView;

        public MiShareDiscoverCallback(MiShareGalleryTransferView miShareGalleryTransferView) {
            this.mView = new WeakReference<>(miShareGalleryTransferView);
        }

        @Override // com.miui.mishare.IMiShareDiscoverCallback
        public void onDeviceLost(final String str) {
            final MiShareGalleryTransferView miShareGalleryTransferView = this.mView.get();
            if (miShareGalleryTransferView == null) {
                return;
            }
            miShareGalleryTransferView.mHandler.post(new Runnable() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.MiShareDiscoverCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    miShareGalleryTransferView.mAdapter.removeDevice(str);
                    if (miShareGalleryTransferView.mAdapter.devicesEmpty()) {
                        miShareGalleryTransferView.setEmptyDevice();
                    }
                }
            });
        }

        @Override // com.miui.mishare.IMiShareDiscoverCallback
        public void onDeviceUpdated(final RemoteDevice remoteDevice) throws RemoteException {
            final MiShareGalleryTransferView miShareGalleryTransferView = this.mView.get();
            if (miShareGalleryTransferView == null) {
                return;
            }
            miShareGalleryTransferView.mHandler.post(new Runnable() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.MiShareDiscoverCallback.2
                @Override // java.lang.Runnable
                public void run() {
                    RemoteDevice remoteDevice2 = remoteDevice;
                    if (remoteDevice2 == null || remoteDevice2.getExtras() == null) {
                        return;
                    }
                    miShareGalleryTransferView.mDividerView.setVisibility(miShareGalleryTransferView.mShowDivider ? 4 : 8);
                    Bundle extras = remoteDevice.getExtras();
                    extras.setClassLoader(getClass().getClassLoader());
                    MiShareDevice miShareDevice = new MiShareDevice(extras.getInt("sgnt"));
                    miShareDevice.files = miShareGalleryTransferView.mFiles;
                    RemoteDevice remoteDevice3 = remoteDevice;
                    miShareDevice.remoteDevice = remoteDevice3;
                    miShareDevice.deviceId = remoteDevice3.getDeviceId();
                    miShareDevice.deviceModelName = extras.getString("model");
                    miShareDevice.deviceType = miShareGalleryTransferView.getDeviceType(extras.getByte("mc"));
                    miShareDevice.deviceName = extras.getString("nickname");
                    miShareDevice.isUwbHit = extras.getBoolean("uwb_hit");
                    miShareDevice.uwbRank = extras.getInt("uwb_rank");
                    miShareDevice.isGlobalDevice = extras.getBoolean("global_device");
                    miShareDevice.vendorId = extras.getInt("verdor_id");
                    if (miShareDevice.isGlobalDevice) {
                        miShareDevice.deviceType = 0;
                    }
                    miShareGalleryTransferView.updateView(false, false);
                    miShareGalleryTransferView.mAdapter.addOrUpdateDevice(miShareDevice, miShareGalleryTransferView.getContext());
                }
            });
        }
    }

    /* loaded from: classes3.dex */
    public static final class MiShareTaskStateReceiver extends BroadcastReceiver {
        public final WeakReference<MiShareGalleryDeviceAdapter> adapter;

        public MiShareTaskStateReceiver(MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter) {
            this.adapter = new WeakReference<>(miShareGalleryDeviceAdapter);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "com.miui.mishare.connectivity.TASK_STATE".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("device_id");
                int intExtra = intent.getIntExtra("state", 1);
                boolean booleanExtra = intent.getBooleanExtra("showProgress", false);
                long longExtra = intent.getLongExtra("current", 0L);
                long longExtra2 = intent.getLongExtra("total", 0L);
                float f = 0.0f;
                if (longExtra > 0 && longExtra2 > 0) {
                    f = ((float) longExtra) / ((float) longExtra2);
                }
                MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter = this.adapter.get();
                if (miShareGalleryDeviceAdapter == null) {
                    return;
                }
                miShareGalleryDeviceAdapter.setDeviceState(stringExtra, intExtra, booleanExtra, f);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class MiShareTaskStateListener extends IMiShareTaskStateListener.Stub {
        public final WeakReference<MiShareGalleryTransferView> mView;

        @Override // com.miui.mishare.IMiShareTaskStateListener
        public void onTaskStateChanged(String str, int i) {
        }

        public MiShareTaskStateListener(MiShareGalleryTransferView miShareGalleryTransferView) {
            this.mView = new WeakReference<>(miShareGalleryTransferView);
        }

        @Override // com.miui.mishare.IMiShareTaskStateListener
        public void onTaskIdChanged(final MiShareTask miShareTask) {
            final MiShareGalleryTransferView miShareGalleryTransferView = this.mView.get();
            if (miShareGalleryTransferView == null) {
                return;
            }
            miShareGalleryTransferView.mHandler.post(new Runnable() { // from class: com.miui.mishare.app.view.MiShareGalleryTransferView.MiShareTaskStateListener.1
                @Override // java.lang.Runnable
                public void run() {
                    miShareGalleryTransferView.mAdapter.replaceTaskId(miShareTask);
                }
            });
        }
    }

    @Override // com.miui.mishare.app.connect.MiShareGalleryConnectivity.ServiceBindCallBack
    public void onServiceBound() {
        this.mConnectivity.registerStateListener(this.mStateListener);
        int serviceState = this.mConnectivity.getServiceState();
        refreshView(this.mState, serviceState);
        this.mState = serviceState;
    }
}
