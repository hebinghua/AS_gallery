package com.miui.gallery.security;

import android.content.DialogInterface;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import miuix.pickerwidget.date.Calendar;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DataDeletedHelper {
    /* renamed from: $r8$lambda$-7uuTQJXEIIF0r6C96L-Y5AuDtQ */
    public static /* synthetic */ void m1344$r8$lambda$7uuTQJXEIIF0r6C96LY5AuDtQ(FragmentActivity fragmentActivity, DataDeletedMessage dataDeletedMessage, DialogInterface dialogInterface, int i) {
        lambda$showDataDeletedNotification$0(fragmentActivity, dataDeletedMessage, dialogInterface, i);
    }

    /* renamed from: $r8$lambda$HWC1sA2xR17-Db2dAafeOviKIpc */
    public static /* synthetic */ void m1345$r8$lambda$HWC1sA2xR17Db2dAafeOviKIpc(DataDeletedMessage dataDeletedMessage, DialogInterface dialogInterface, int i) {
        notifyServer(dataDeletedMessage, true);
    }

    public static void notifyDataDeleted(FragmentActivity fragmentActivity) {
        new NotifyTask<Void, DataDeletedMessage>(fragmentActivity) { // from class: com.miui.gallery.security.DataDeletedHelper.1
            @Override // com.miui.gallery.security.NotifyTask
            public DataDeletedMessage doProcess(Void... voidArr) {
                Set<String> deleteMessages = GalleryPreferences.RiskControl.getDeleteMessages();
                DataDeletedMessage dataDeletedMessage = null;
                if (deleteMessages != null) {
                    Iterator<String> it = deleteMessages.iterator();
                    while (it.hasNext()) {
                        String next = it.next();
                        DataDeletedMessage convert = DataDeletedMessage.convert(next, true);
                        if (convert == null || !convert.isValid()) {
                            it.remove();
                            GalleryPreferences.RiskControl.clearDelete(next);
                            dataDeletedMessage = convert;
                        } else {
                            convert.setJsonMsg(next);
                            return convert;
                        }
                    }
                    return dataDeletedMessage;
                }
                return null;
            }

            @Override // com.miui.gallery.security.NotifyTask
            public void doNotify(FragmentActivity fragmentActivity2, DataDeletedMessage dataDeletedMessage) {
                if (dataDeletedMessage != null) {
                    DataDeletedHelper.showDataDeletedNotification(fragmentActivity2, dataDeletedMessage);
                }
            }
        }.start(new Void[0]);
    }

    public static void showDataDeletedNotification(final FragmentActivity fragmentActivity, final DataDeletedMessage dataDeletedMessage) {
        if (fragmentActivity.getSupportFragmentManager().findFragmentByTag("RiskControl_Delete") != null) {
            DefaultLogger.d("RiskControl_Delete", "has notification already");
            return;
        }
        GalleryPreferences.RiskControl.clearDelete(dataDeletedMessage.getJsonMsg());
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Calendar calendar = new Calendar();
        calendar.setTimeInMillis(dataDeletedMessage.getDeleteTime());
        calendar.format(StaticContext.sGetAndroidContext(), sb, fragmentActivity.getString(R.string.risk_control_deleted_date));
        calendar.format(StaticContext.sGetAndroidContext(), sb2, fragmentActivity.getString(R.string.risk_control_deleted_time));
        new AlertDialogFragment.Builder().setTitle(fragmentActivity.getString(R.string.risk_control_deleted_title)).setMessage(fragmentActivity.getString(R.string.risk_control_deleted_message, new Object[]{sb, sb2, dataDeletedMessage.getDeviceName()})).setCancelable(false).setNegativeButton(fragmentActivity.getString(R.string.not_is), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.security.DataDeletedHelper$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DataDeletedHelper.m1344$r8$lambda$7uuTQJXEIIF0r6C96LY5AuDtQ(FragmentActivity.this, dataDeletedMessage, dialogInterface, i);
            }
        }).setPositiveButton(fragmentActivity.getString(R.string.is), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.security.DataDeletedHelper$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DataDeletedHelper.m1345$r8$lambda$HWC1sA2xR17Db2dAafeOviKIpc(DataDeletedMessage.this, dialogInterface, i);
            }
        }).create().showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "RiskControl_Delete");
    }

    public static /* synthetic */ void lambda$showDataDeletedNotification$0(FragmentActivity fragmentActivity, DataDeletedMessage dataDeletedMessage, DialogInterface dialogInterface, int i) {
        IntentUtil.gotoWindControlManagement(fragmentActivity, dataDeletedMessage.getDeleteInfoJson());
        notifyServer(dataDeletedMessage, false);
    }

    public static void stat(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("is_true", String.valueOf(z));
        StatHelper.recordCountEvent("risk_controller", "risk_show_warning", hashMap);
    }

    public static void notifyServer(DataDeletedMessage dataDeletedMessage, boolean z) {
        stat(z);
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            DefaultLogger.e("RiskControl_Delete", "connecting to network was disallowed");
        } else {
            new BaseGalleryRequest(1002, HostManager.RiskControl.getConfirmUrl()) { // from class: com.miui.gallery.security.DataDeletedHelper.2
                @Override // com.miui.gallery.net.BaseGalleryRequest
                public void onRequestSuccess(JSONObject jSONObject) throws Exception {
                    super.onRequestSuccess(jSONObject);
                    DefaultLogger.d("RiskControl_Delete", "notify server successfully");
                }

                @Override // com.miui.gallery.net.BaseGalleryRequest, com.miui.gallery.net.json.BaseJsonRequest, com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
                public void onRequestError(ErrorCode errorCode, String str, Object obj) {
                    super.onRequestError(errorCode, str, obj);
                    DefaultLogger.d("RiskControl_Delete", "notify server failed, %s", obj);
                }
            }.addParam("devId", dataDeletedMessage.getDeviceId()).addParam("expected", String.valueOf(z)).addParam(nexExportFormat.TAG_FORMAT_TAG, dataDeletedMessage.getTag()).execute();
        }
    }
}
