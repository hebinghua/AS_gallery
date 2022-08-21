package com.miui.mishare.app.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.mishare.R$color;
import com.miui.mishare.R$drawable;
import com.miui.mishare.R$id;
import com.miui.mishare.R$layout;
import com.miui.mishare.R$string;
import com.miui.mishare.app.model2.MiShareDevice;
import com.miui.mishare.app.util.FolmeUtil;

/* loaded from: classes3.dex */
public class MiShareGalleryDeviceView extends LinearLayout {
    public boolean isGlobal;
    public int mCurrentStatus;
    public View mDevice;
    public ImageView mDeviceGlobal;
    public View mDeviceIcon;
    public View mDeviceIconInner;
    public TextView mDeviceModelNameTv;
    public TextView mDeviceNameTv;
    public ImageView mDeviceType;
    public ImageView mLogoIv;
    public View mLogoView;
    public NoticeView mNoticeView;
    public CircleProgressBar mProgressBar;
    public ImageView mStatusIv;
    public boolean showProgress;

    public MiShareGalleryDeviceView(Context context) {
        super(context);
        initView();
    }

    public final void initView() {
        LayoutInflater.from(getContext()).inflate(R$layout.view_mishare_gallery_device_view, this);
        this.mDevice = findViewById(R$id.rl_device);
        this.mDeviceIcon = findViewById(R$id.rl_device_icon);
        this.mDeviceIconInner = findViewById(R$id.ll_device_area);
        this.mDeviceNameTv = (TextView) findViewById(R$id.tv_device_name);
        this.mDeviceModelNameTv = (TextView) findViewById(R$id.tv_device_model_name);
        this.mStatusIv = (ImageView) findViewById(R$id.iv_device_status);
        this.mDeviceType = (ImageView) findViewById(R$id.iv_device_type);
        this.mDeviceGlobal = (ImageView) findViewById(R$id.iv_global_device);
        this.mLogoIv = (ImageView) findViewById(R$id.iv_logo);
        this.mLogoView = findViewById(R$id.rl_logo);
        this.mProgressBar = (CircleProgressBar) findViewById(R$id.progress);
        this.mNoticeView = (NoticeView) findViewById(R$id.device_notice);
        FolmeUtil.handleTouchNoDim(this.mDevice);
    }

    public View getIconView() {
        return this.mDevice;
    }

    public void setDeviceType(int i) {
        switch (i) {
            case 0:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_global);
                this.mProgressBar.setProgressColor(R$color.bg_progress_global);
                return;
            case 1:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_xiaomi);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_mi);
                this.mProgressBar.setProgressColor(R$color.bg_device_xiaomi);
                return;
            case 2:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_vivo);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_vivo);
                this.mProgressBar.setProgressColor(R$color.bg_device_vivo);
                return;
            case 3:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_oppo);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_oppo);
                this.mProgressBar.setProgressColor(R$color.bg_device_oppo);
                return;
            case 4:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_realme);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_realme);
                this.mProgressBar.setProgressColor(R$color.bg_device_realme);
                return;
            case 5:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_blackshark);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_blackshark);
                this.mProgressBar.setProgressColor(R$color.bg_device_blackshark);
                return;
            case 6:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_meizu);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_meizu);
                this.mProgressBar.setProgressColor(R$color.bg_device_meizu);
                return;
            case 7:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_oneplus);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_oneplus);
                this.mProgressBar.setProgressColor(R$color.bg_device_oneplus);
                return;
            case 8:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_zte);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_zte);
                this.mProgressBar.setProgressColor(R$color.progress_color_zte);
                return;
            case 9:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_nubia);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_nubia);
                this.mProgressBar.setProgressColor(R$color.progress_color_nubia);
                return;
            case 10:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_hisense);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_hisense);
                this.mProgressBar.setProgressColor(R$color.progress_color_hisense);
                return;
            case 11:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_asus);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_asus);
                this.mProgressBar.setProgressColor(R$color.progress_color_asus);
                return;
            case 12:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_rog);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_rog);
                this.mProgressBar.setProgressColor(R$color.progress_color_rog);
                return;
            case 13:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_smartisan);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_smartisan);
                this.mProgressBar.setProgressColor(R$color.progress_color_smartisan);
                return;
            case 14:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_samsung);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_samsung);
                this.mProgressBar.setProgressColor(R$color.bg_device_samsung);
                return;
            case 15:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_lenovo);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_lenovo);
                this.mProgressBar.setProgressColor(R$color.bg_device_lenovo);
                return;
            default:
                this.mDeviceIcon.setBackgroundResource(R$drawable.bg_scanned_device_xiaomi);
                this.mLogoIv.setImageResource(R$drawable.icon_logo_mi);
                this.mProgressBar.setProgressColor(R$color.progress_color_xiaomi);
                return;
        }
    }

    public final void setDeviceStatusName(int i, String str, String str2, boolean z, int i2) {
        String string;
        if (i == 2) {
            animShow(this.mDeviceModelNameTv);
            this.mDeviceNameTv.setVisibility(0);
            this.mDeviceModelNameTv.setText(getResources().getString(R$string.device_status_sending));
            this.mDeviceModelNameTv.setTextColor(getResources().getColor(R$color.textcolor_status_sending));
        } else if (i == 3) {
            animShow(this.mDeviceModelNameTv);
            this.mDeviceNameTv.setVisibility(0);
            this.mDeviceModelNameTv.setText(getResources().getString(R$string.device_status_failed));
            this.mDeviceModelNameTv.setTextColor(getResources().getColor(R$color.textcolor_status_failed));
        } else if (i == 4) {
            animShow(this.mDeviceModelNameTv);
            this.mDeviceNameTv.setVisibility(0);
            this.mDeviceModelNameTv.setText(getResources().getString(R$string.device_status_success));
            this.mDeviceModelNameTv.setTextColor(getResources().getColor(R$color.textcolor_status_success));
        } else if (i == 5) {
            animShow(this.mDeviceModelNameTv);
            this.mDeviceNameTv.setVisibility(0);
            this.mDeviceModelNameTv.setText(getResources().getString(R$string.device_status_waiting));
            this.mDeviceModelNameTv.setTextColor(getResources().getColor(R$color.textcolor_status_waiting));
        } else {
            animShow(this.mDeviceModelNameTv);
            if (z) {
                string = str2;
            } else if (i2 == 34) {
                string = getResources().getString(R$string.pc_xiaomi_device_model_name);
            } else if (i2 == 35) {
                string = getResources().getString(R$string.pc_redmi_device_model_name);
            } else {
                string = getResources().getString(R$string.pc_device_model_name);
            }
            this.mDeviceModelNameTv.setText(string);
            this.mDeviceModelNameTv.setTextColor(getResources().getColor(R$color.textcolor_status_default));
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && str2.contains(str)) {
                this.mDeviceModelNameTv.setVisibility(8);
                this.mDeviceNameTv.setMaxLines(2);
                return;
            }
            this.mDeviceModelNameTv.setVisibility(0);
            this.mDeviceNameTv.setMaxLines(1);
        }
    }

    public void setDeviceStyle(int i) {
        if (i == 2) {
            this.mDeviceType.setImageResource(R$drawable.ic_device_pc);
        } else if (i == 3) {
            this.mDeviceType.setImageResource(R$drawable.ic_device_pad);
        } else if (i == 4) {
            this.mDeviceGlobal.setImageResource(R$drawable.ic_global_device_pc);
        } else if (i == 5) {
            this.mDeviceGlobal.setImageResource(R$drawable.ic_global_device_pad);
        } else if (i == 6) {
            this.mDeviceGlobal.setImageResource(R$drawable.ic_global_device_phone);
        } else {
            this.mDeviceType.setImageResource(R$drawable.ic_device_phone);
        }
    }

    public void setDeviceName(String str, boolean z) {
        TextView textView = this.mDeviceNameTv;
        if (z) {
            str = getResources().getString(R$string.device_name_with_ellipsize, str);
        }
        textView.setText(str);
    }

    public void setGlobalDeviceName(int i) {
        if (i == 4) {
            this.mDeviceNameTv.setText(getResources().getString(R$string.global_pc));
        } else if (i == 5) {
            this.mDeviceNameTv.setText(getResources().getString(R$string.global_pad));
        } else {
            this.mDeviceNameTv.setText(getResources().getString(R$string.global_phone));
        }
    }

    public void setIsGloabal(boolean z) {
        this.isGlobal = z;
    }

    public final void animShow(View view) {
        if (view.getVisibility() == 0 && view.getAlpha() == 1.0f) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = (ViewPropertyAnimator) view.getTag(view.getId());
        float alpha = view.getAlpha();
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (view.getVisibility() == 8) {
            view.setVisibility(0);
            alpha = 0.0f;
            view.setAlpha(0.0f);
        }
        ViewPropertyAnimator duration = view.animate().alpha(1.0f).setDuration((int) ((1.0f - alpha) * 500.0f));
        view.setTag(view.getId(), duration);
        duration.start();
    }

    public final void animHide(View view) {
        if (view.getVisibility() == 8) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = (ViewPropertyAnimator) view.getTag(view.getId());
        float alpha = view.getAlpha();
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        ViewPropertyAnimator duration = view.animate().alpha(0.0f).setDuration((int) (alpha * 500.0f));
        view.setTag(view.getId(), duration);
        duration.start();
    }

    /* loaded from: classes3.dex */
    public static final class AnimationCallback extends Animatable2.AnimationCallback {
        public AnimationCallback() {
        }

        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).start();
            }
        }
    }

    public final void setDeviceSending() {
        stopAnimator();
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R$drawable.avd_sending_animation);
        this.mStatusIv.setVisibility(0);
        this.mStatusIv.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.registerAnimationCallback(new AnimationCallback());
        animatedVectorDrawable.start();
    }

    public final void setDeviceWaiting() {
        stopAnimator();
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) getResources().getDrawable(R$drawable.avd_loading_animation);
        this.mStatusIv.setVisibility(0);
        this.mStatusIv.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.registerAnimationCallback(new AnimationCallback());
        animatedVectorDrawable.start();
    }

    public final void stopAnimStatus() {
        this.mStatusIv.setVisibility(8);
        stopAnimator();
    }

    public final void stopAnimator() {
        Drawable drawable = this.mStatusIv.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            if (!animatedVectorDrawable.isRunning()) {
                return;
            }
            animatedVectorDrawable.clearAnimationCallbacks();
            animatedVectorDrawable.stop();
        }
    }

    public final void showWaitingStatus() {
        this.mDeviceGlobal.setVisibility(8);
        this.mDeviceType.setVisibility(8);
        this.mLogoView.setVisibility(8);
        setDeviceWaiting();
    }

    public final void showSendingStatus() {
        if (this.showProgress) {
            stopAnimator();
            this.mStatusIv.setVisibility(8);
            if (this.isGlobal) {
                this.mDeviceGlobal.setVisibility(0);
            } else {
                this.mLogoView.setVisibility(0);
                this.mDeviceType.setVisibility(0);
            }
            this.mProgressBar.setProgressPercent(0.0f);
            this.mProgressBar.setVisibility(0);
            return;
        }
        this.mDeviceGlobal.setVisibility(8);
        this.mDeviceType.setVisibility(8);
        animHide(this.mLogoView);
        setDeviceSending();
    }

    public final void showFailureStatus() {
        if (this.showProgress) {
            this.mProgressBar.setVisibility(8);
        }
        this.mDeviceGlobal.setVisibility(8);
        this.mDeviceType.setVisibility(8);
        this.mLogoView.setVisibility(8);
        this.mStatusIv.setVisibility(0);
        DeviceStatusTransition deviceStatusTransition = new DeviceStatusTransition();
        deviceStatusTransition.addTarget(this.mStatusIv);
        TransitionManager.beginDelayedTransition((ViewGroup) this.mDeviceIcon, deviceStatusTransition);
        this.mStatusIv.setImageResource(R$drawable.ic_device_retry);
    }

    public final void showSuccessStatus() {
        if (this.showProgress) {
            this.mStatusIv.setVisibility(0);
            this.mProgressBar.setVisibility(8);
        }
        this.mDeviceGlobal.setVisibility(8);
        this.mDeviceType.setVisibility(8);
        this.mLogoView.setVisibility(8);
        this.mStatusIv.setVisibility(0);
        DeviceStatusTransition deviceStatusTransition = new DeviceStatusTransition();
        deviceStatusTransition.addTarget(this.mStatusIv);
        TransitionManager.beginDelayedTransition((ViewGroup) this.mDeviceIcon, deviceStatusTransition);
        this.mStatusIv.setImageResource(R$drawable.ic_device_success);
    }

    public final void showIDLEStatus() {
        stopAnimStatus();
        this.mStatusIv.setVisibility(8);
        if (this.isGlobal) {
            this.mLogoView.setVisibility(8);
            this.mDeviceType.setVisibility(8);
            animShow(this.mDeviceGlobal);
        } else {
            this.mDeviceGlobal.setVisibility(8);
            animShow(this.mDeviceType);
            animShow(this.mLogoView);
        }
        this.mProgressBar.setVisibility(8);
    }

    public void setDeviceStatus(MiShareDevice miShareDevice) {
        int i = miShareDevice.deviceStatus;
        this.showProgress = miShareDevice.showProgress;
        setDeviceStatusName(i, miShareDevice.deviceName, miShareDevice.deviceModelName, !miShareDevice.isPC(), miShareDevice.vendorId);
        if (this.mCurrentStatus != i || i == 1) {
            this.mCurrentStatus = i;
            updateView();
        }
    }

    public void updateView() {
        int i = this.mCurrentStatus;
        if (i == 1) {
            showIDLEStatus();
        } else if (i == 2) {
            showSendingStatus();
        } else if (i == 3) {
            showFailureStatus();
        } else if (i == 4) {
            showSuccessStatus();
        } else if (i != 5) {
        } else {
            showWaitingStatus();
        }
    }

    public void setProgressPercent(float f) {
        this.mProgressBar.setProgressPercent(f);
    }

    public void setIsUwbDevice(boolean z) {
        if (z) {
            Drawable drawable = getResources().getDrawable(R$drawable.ic_uwb);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            this.mDeviceNameTv.setCompoundDrawables(drawable, null, null, null);
            this.mDeviceNameTv.setCompoundDrawablePadding(7);
            this.mDeviceModelNameTv.setVisibility(0);
            this.mDeviceNameTv.setMaxLines(1);
            return;
        }
        this.mDeviceNameTv.setCompoundDrawables(null, null, null, null);
        this.mDeviceNameTv.setCompoundDrawablePadding(0);
    }

    public void toggleNotice(boolean z) {
        if (z) {
            this.mNoticeView.start();
        } else {
            this.mNoticeView.stop();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateView();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimStatus();
        getHandler().removeCallbacksAndMessages(null);
    }

    /* loaded from: classes3.dex */
    public static class DeviceStatusTransition extends Transition {
        public DeviceStatusTransition() {
        }

        @Override // android.transition.Transition
        public void captureStartValues(TransitionValues transitionValues) {
            transitionValues.values.put("device_status_drawable", ((ImageView) transitionValues.view).getDrawable());
        }

        @Override // android.transition.Transition
        public void captureEndValues(TransitionValues transitionValues) {
            transitionValues.values.put("device_status_drawable", ((ImageView) transitionValues.view).getDrawable());
        }

        @Override // android.transition.Transition
        public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
            final ImageView imageView = (ImageView) transitionValues2.view;
            final Drawable drawable = (Drawable) transitionValues.values.get("device_status_drawable");
            final Drawable drawable2 = (Drawable) transitionValues2.values.get("device_status_drawable");
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(500L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.mishare.app.view.MiShareGalleryDeviceView.DeviceStatusTransition.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (floatValue <= 0.5f) {
                        imageView.setImageDrawable(drawable);
                        imageView.setAlpha((0.5f - floatValue) / 0.5f);
                        return;
                    }
                    imageView.setImageDrawable(drawable2);
                    imageView.setAlpha((floatValue - 0.5f) / 0.5f);
                }
            });
            return ofFloat;
        }
    }
}
