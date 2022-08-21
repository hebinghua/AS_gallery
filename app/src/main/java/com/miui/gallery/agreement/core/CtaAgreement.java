package com.miui.gallery.agreement.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import com.miui.gallery.util.BaseBuildUtil;
import java.util.Locale;

/* loaded from: classes.dex */
public class CtaAgreement {

    /* loaded from: classes.dex */
    public static class Licence {
        public static String URL_BD_MAP_PRIVACY_POLICY = "https://privacy.mi.com/miuigallery-share/zh_CN/";
        public static String URL_MIUI_PRIVACY_POLICY = "https://privacy.mi.com/all";
        public static String URL_MIUI_USER_AGREEMENT = "http://www.miui.com/res/doc/eula.html";

        public static Intent getPrivacyIntent() {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(getPrivacyUrl()));
            return intent;
        }

        public static Intent getMapPrivacyIntent() {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(getMapPrivacyUrl()));
            return intent;
        }

        public static String getUserAgreementUrl() {
            return String.format(Locale.US, "%s?region=%s&lang=%s", URL_MIUI_USER_AGREEMENT, BaseBuildUtil.getRegion(), Locale.getDefault().toString());
        }

        public static String getPrivacyUrl() {
            return String.format(Locale.US, "%s/%s_%s", URL_MIUI_PRIVACY_POLICY, Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        }

        public static String getMapPrivacyUrl() {
            return URL_BD_MAP_PRIVACY_POLICY;
        }
    }

    public static CharSequence buildUserNotice(Context context, int i) {
        return Html.fromHtml(context.getResources().getString(i, Licence.getUserAgreementUrl(), Licence.getPrivacyUrl()));
    }

    public static CharSequence buildMapPrivacyPolicy(Context context, int i) {
        return Html.fromHtml(context.getResources().getString(i, Licence.getMapPrivacyUrl()));
    }
}
