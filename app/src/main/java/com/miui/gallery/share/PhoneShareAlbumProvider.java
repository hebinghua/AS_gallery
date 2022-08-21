package com.miui.gallery.share;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.share.AlbumShareOperations;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.ListDialogFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class PhoneShareAlbumProvider {
    public static PhoneShareAlbumProvider sInstance;
    public int[] addSharerByWhich;

    public PhoneShareAlbumProvider() {
        initDialogItems();
    }

    public final void initDialogItems() {
        if (BaseBuildUtil.isPad()) {
            this.addSharerByWhich = new int[]{2};
        } else {
            this.addSharerByWhich = new int[]{1, 2};
        }
    }

    public static synchronized PhoneShareAlbumProvider getInstance() {
        PhoneShareAlbumProvider phoneShareAlbumProvider;
        synchronized (PhoneShareAlbumProvider.class) {
            if (sInstance == null) {
                sInstance = new PhoneShareAlbumProvider();
            }
            phoneShareAlbumProvider = sInstance;
        }
        return phoneShareAlbumProvider;
    }

    public int getAddSharerDialogItemsLength() {
        int[] iArr = this.addSharerByWhich;
        if (iArr == null) {
            return 0;
        }
        return iArr.length;
    }

    public void showAddSharerDialog(AppCompatActivity appCompatActivity, DialogInterface.OnClickListener onClickListener) {
        ListDialogFragment listDialogFragment = new ListDialogFragment();
        listDialogFragment.setTitle(appCompatActivity.getResources().getString(R.string.add_sharer));
        listDialogFragment.setDisplayItems(getShareItemTexts(appCompatActivity));
        listDialogFragment.setItemClickListener(onClickListener);
        listDialogFragment.showAllowingStateLoss(appCompatActivity.getSupportFragmentManager(), "ListDialogFragment");
    }

    public final String[] getShareItemTexts(Context context) {
        if (context == null) {
            return null;
        }
        int length = this.addSharerByWhich.length;
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            int i2 = this.addSharerByWhich[i];
            if (i2 == 1) {
                strArr[i] = context.getString(R.string.by_sms);
            } else if (i2 == 2) {
                strArr[i] = context.getString(R.string.by_others);
            } else {
                DefaultLogger.e("PhoneShareAlbumProvider", "not handled operation type" + this.addSharerByWhich[i]);
            }
        }
        return strArr;
    }

    public void shareOperation(AppCompatActivity appCompatActivity, int i, AlbumShareOperations.OutgoingInvitation outgoingInvitation, String str) {
        int[] iArr = this.addSharerByWhich;
        if (i >= iArr.length) {
            return;
        }
        int i2 = iArr[i];
        if (i2 == 1) {
            shareBySms(outgoingInvitation.toMessage(), appCompatActivity);
            recordAddSharerCountEvent("sms");
        } else if (i2 == 2) {
            shareByOther(appCompatActivity, str, outgoingInvitation);
            recordAddSharerCountEvent("other");
        } else {
            throw new UnsupportedOperationException("Bad category, which=" + i);
        }
    }

    public final void recordAddSharerCountEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("album", "add_sharer", hashMap);
    }

    public void shareBySms(String str, AppCompatActivity appCompatActivity) {
        if (TextUtils.isEmpty(str)) {
            UIHelper.toast((int) R.string.request_sms_url_failed);
            return;
        }
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse("smsto:"));
        intent.putExtra("sms_body", str);
        try {
            appCompatActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DefaultLogger.e("PhoneShareAlbumProvider", "sms intent not resolved");
            DefaultLogger.e("PhoneShareAlbumProvider", e);
        }
    }

    public void shareByOther(AppCompatActivity appCompatActivity, String str, AlbumShareOperations.OutgoingInvitation outgoingInvitation) {
        appCompatActivity.startActivity(getShareUrlIntent(appCompatActivity, str, outgoingInvitation.getDescription(), outgoingInvitation.getUrl(), false));
    }

    public Intent getShareUrlIntent(AppCompatActivity appCompatActivity, String str, String str2, String str3, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        String string = appCompatActivity.getString(R.string.quotation, new Object[]{str});
        String string2 = appCompatActivity.getString(R.string.share_public_url_text, new Object[]{string, str3});
        if (!TextUtils.isEmpty(str3)) {
            str2 = str3;
        }
        String string3 = appCompatActivity.getString(R.string.share_public_url_text, new Object[]{string, "<a href=\"" + str3 + "\">" + str2 + "</a>"});
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body> <font size=12>");
        sb.append(string3);
        sb.append("</font></body></html>");
        String sb2 = sb.toString();
        intent.putExtra("android.intent.extra.TEXT", string2);
        intent.putExtra("android.intent.extra.HTML_TEXT", sb2);
        intent.setType("text/*");
        return Intent.createChooser(intent, appCompatActivity.getString(R.string.send_public_url));
    }

    public void showKickUserDialog(AppCompatActivity appCompatActivity, DialogInterface.OnClickListener onClickListener) {
        Resources resources = appCompatActivity.getResources();
        new AlertDialogFragment.Builder().setTitle(resources.getString(R.string.kick_owner_user_title)).setCancelable(true).setNegativeButton(resources.getString(R.string.cancel), null).setPositiveButton(resources.getString(R.string.kick_owner_user_positive_button_text), onClickListener).create().showAllowingStateLoss(appCompatActivity.getSupportFragmentManager(), "KickOwnerUser");
    }
}
