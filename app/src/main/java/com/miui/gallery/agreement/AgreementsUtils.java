package com.miui.gallery.agreement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.agreement.cn.CustomCTANetworkAgreementInjector;
import com.miui.gallery.agreement.cn.NetworkAgreementFragment;
import com.miui.gallery.agreement.cn.SystemCTAAgreement;
import com.miui.gallery.agreement.core.Agreement;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.agreement.korea.AgreementDialogFragment;
import com.miui.gallery.permission.R$string;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class AgreementsUtils {
    public static void showUserAgreements(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        showUserAgreements(fragmentActivity, false, onAgreementInvokedListener);
    }

    public static void showUserAgreements(final FragmentActivity fragmentActivity, boolean z, final OnAgreementInvokedListener onAgreementInvokedListener) {
        if (isKoreaRegion()) {
            if (!BaseGalleryPreferences.Agreement.isRequiredAgreementsAllowed()) {
                AgreementDialogFragment.newInstance(getAgreements(fragmentActivity)).invoke(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.agreement.AgreementsUtils.1
                    @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                    public void onAgreementInvoked(boolean z2) {
                        BaseGalleryPreferences.Agreement.setRequiredAgreementsAllowed(z2);
                        if (z2) {
                            BaseGalleryPreferences.CTA.setCanConnectNetwork(true);
                        } else {
                            FragmentActivity.this.finish();
                        }
                        OnAgreementInvokedListener onAgreementInvokedListener2 = onAgreementInvokedListener;
                        if (onAgreementInvokedListener2 != null) {
                            onAgreementInvokedListener2.onAgreementInvoked(z2);
                        }
                    }
                });
            } else if (onAgreementInvokedListener == null) {
            } else {
                onAgreementInvokedListener.onAgreementInvoked(true);
            }
        } else if (!BaseBuildUtil.isInternational()) {
            showNetworkingAgreement(fragmentActivity, z, onAgreementInvokedListener);
        } else {
            BaseGalleryPreferences.CTA.setCanConnectNetwork(true);
            if (onAgreementInvokedListener == null) {
                return;
            }
            onAgreementInvokedListener.onAgreementInvoked(true);
        }
    }

    public static void showNetworkingAgreement(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        showNetworkingAgreement(fragmentActivity, false, onAgreementInvokedListener);
    }

    public static void showNetworkingAgreement(FragmentActivity fragmentActivity, boolean z, OnAgreementInvokedListener onAgreementInvokedListener) {
        BaseGalleryPreferences.CTA.setHasShownNetworkingAgreements(true);
        if (SystemCTAAgreement.SUPPORT_SYSTEM_CTA.get(fragmentActivity).booleanValue()) {
            CustomCTANetworkAgreementInjector customCTANetworkAgreementInjector = new CustomCTANetworkAgreementInjector();
            customCTANetworkAgreementInjector.setCancelable(false);
            customCTANetworkAgreementInjector.invoke(fragmentActivity, new OnNetworkInvokedWrapper(onAgreementInvokedListener));
            return;
        }
        new NetworkAgreementFragment().invoke(fragmentActivity, new OnNetworkInvokedWrapper(onAgreementInvokedListener));
    }

    public static boolean isNetworkingAgreementAccepted() {
        if (!BaseBuildUtil.isInternational()) {
            return BaseGalleryPreferences.CTA.canConnectNetwork();
        }
        if (isKoreaRegion()) {
            if (BaseGalleryPreferences.Agreement.isRequiredAgreementsAllowed()) {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    BaseGalleryPreferences.CTA.setCanConnectNetwork(true);
                }
                return true;
            }
            return BaseGalleryPreferences.CTA.canConnectNetwork();
        }
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            BaseGalleryPreferences.CTA.setCanConnectNetwork(true);
        }
        return true;
    }

    public static void viewAgreement(Context context, Agreement agreement) {
        if (agreement == null || TextUtils.isEmpty(agreement.mLink)) {
            DefaultLogger.w("AgreementsUtils", "agreement can't view");
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(agreement.mLink));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            DefaultLogger.e("AgreementsUtils", e);
        }
    }

    public static ArrayList<Agreement> getAgreements(Context context) {
        ArrayList<Agreement> arrayList = new ArrayList<>();
        Agreement agreement = new Agreement(context.getResources().getString(R$string.user_agreement2), CtaAgreement.Licence.getUserAgreementUrl(), true);
        Agreement agreement2 = new Agreement(context.getResources().getString(R$string.user_agreement4), CtaAgreement.Licence.getPrivacyUrl(), true);
        arrayList.add(agreement);
        arrayList.add(agreement2);
        return arrayList;
    }

    public static boolean isKoreaRegion() {
        return "KR".equalsIgnoreCase(BaseBuildUtil.getRegion());
    }
}
