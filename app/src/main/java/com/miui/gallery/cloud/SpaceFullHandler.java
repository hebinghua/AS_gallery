package com.miui.gallery.cloud;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import com.miui.account.AccountHelper;
import com.miui.gallery.util.Utils;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import miui.cloud.sync.MiCloudStatusInfo;

/* loaded from: classes.dex */
public class SpaceFullHandler {
    public static OwnerSpaceFullListener sOwnerSpaceFullListener;
    public static SharerSpaceFullListener sSharerSpaceFullListener;
    public static HashMap<String, Boolean> sSpaceFullMap = new HashMap<>();

    /* loaded from: classes.dex */
    public interface SpaceFullListener {
        void handleSpaceFullError(RequestCloudItem requestCloudItem);

        void handleSpaceNotFull(RequestCloudItem requestCloudItem);

        boolean isSpaceFull(RequestCloudItem requestCloudItem);
    }

    public static OwnerSpaceFullListener getOwnerSpaceFullListener() {
        if (sOwnerSpaceFullListener == null) {
            sOwnerSpaceFullListener = new OwnerSpaceFullListener();
        }
        return sOwnerSpaceFullListener;
    }

    public static SharerSpaceFullListener getSharerSpaceFullListener() {
        if (sSharerSpaceFullListener == null) {
            sSharerSpaceFullListener = new SharerSpaceFullListener();
        }
        return sSharerSpaceFullListener;
    }

    /* loaded from: classes.dex */
    public static class OwnerSpaceFullListener implements SpaceFullListener {
        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public void handleSpaceFullError(RequestCloudItem requestCloudItem) {
            SpaceFullHandler.setOwnerSpaceFull();
        }

        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public void handleSpaceNotFull(RequestCloudItem requestCloudItem) {
            SpaceFullHandler.removeOwnerSpaceFull();
        }

        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public boolean isSpaceFull(RequestCloudItem requestCloudItem) {
            return SpaceFullHandler.isOwnerSpaceFull();
        }
    }

    /* loaded from: classes.dex */
    public static class SharerSpaceFullListener implements SpaceFullListener {
        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public void handleSpaceFullError(RequestCloudItem requestCloudItem) {
            SpaceFullHandler.setSharerSpaceFull(requestCloudItem.dbCloud.getAlbumId());
        }

        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public void handleSpaceNotFull(RequestCloudItem requestCloudItem) {
            SpaceFullHandler.removeSharerSpaceFull(requestCloudItem.dbCloud.getAlbumId());
        }

        @Override // com.miui.gallery.cloud.SpaceFullHandler.SpaceFullListener
        public boolean isSpaceFull(RequestCloudItem requestCloudItem) {
            return SpaceFullHandler.isSharerSpaceFull(requestCloudItem.dbCloud.getAlbumId());
        }
    }

    public static synchronized boolean isOwnerSpaceFull() {
        synchronized (SpaceFullHandler.class) {
            long sGetCloudGallerySpaceFullTime = Preference.sGetCloudGallerySpaceFullTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - sGetCloudGallerySpaceFullTime < 3600000 && currentTimeMillis >= sGetCloudGallerySpaceFullTime) {
                return Preference.sGetCloudGallerySpaceFull();
            }
            return false;
        }
    }

    public static synchronized void setOwnerSpaceFull() {
        synchronized (SpaceFullHandler.class) {
            String className = new Throwable().getStackTrace()[1].getClassName();
            DefaultLogger.d("SpaceFullHandler", className + " set owner space full.");
            Preference.sSetCloudGallerySpaceFull(true);
            GalleryMiCloudUtil.handleSpaceFullOrNot(true);
        }
    }

    public static synchronized void removeOwnerSpaceFull() {
        synchronized (SpaceFullHandler.class) {
            if (Preference.sGetCloudGallerySpaceFull()) {
                GalleryMiCloudUtil.handleSpaceFullOrNot(false);
            }
            Preference.sSetCloudGallerySpaceFull(false);
        }
    }

    public static synchronized boolean isSharerSpaceFull(String str) {
        synchronized (SpaceFullHandler.class) {
            Boolean bool = sSpaceFullMap.get(str);
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        }
    }

    public static synchronized void setSharerSpaceFull(String str) {
        synchronized (SpaceFullHandler.class) {
            DefaultLogger.d("SpaceFullHandler", "%s, set shareAlbum: %s space full.", new Throwable().getStackTrace()[1].getClassName(), Utils.desensitizeShareAlbumId(str));
            sSpaceFullMap.put(str, Boolean.TRUE);
        }
    }

    public static synchronized void removeSharerSpaceFull(String str) {
        synchronized (SpaceFullHandler.class) {
            DefaultLogger.d("SpaceFullHandler", "%s, remove shareAlbum: %s space full.", new Throwable().getStackTrace()[1].getClassName(), Utils.desensitizeShareAlbumId(str));
            sSpaceFullMap.put(str, Boolean.FALSE);
        }
    }

    public static synchronized void handleSpaceFullIfNeeded(Context context) {
        synchronized (SpaceFullHandler.class) {
            if (!isOwnerSpaceFull()) {
                return;
            }
            Account xiaomiAccount = AccountHelper.getXiaomiAccount(context);
            if (xiaomiAccount == null) {
                return;
            }
            handleSpaceFull(context, xiaomiAccount);
        }
    }

    public static void handleSpaceFull(Context context, Account account) {
        try {
            String userData = ((AccountManager) context.getSystemService("account")).getUserData(account, "extra_micloud_status_info_quota");
            if (TextUtils.isEmpty(userData)) {
                return;
            }
            MiCloudStatusInfo miCloudStatusInfo = new MiCloudStatusInfo(account.name);
            miCloudStatusInfo.parseQuotaString(userData);
            MiCloudStatusInfo.QuotaInfo quotaInfo = miCloudStatusInfo.getQuotaInfo();
            if (quotaInfo == null) {
                return;
            }
            if (quotaInfo.getTotal() - quotaInfo.getUsed() >= 20971520) {
                removeOwnerSpaceFull();
                DefaultLogger.d("SpaceFullHandler", "removeOwnerSpaceFull");
            }
            DefaultLogger.i("SpaceFullHandler", "Quota info, yearPkgType: %s, total: %d, used: %d", quotaInfo.getYearlyPackageType(), Long.valueOf(quotaInfo.getTotal()), Long.valueOf(quotaInfo.getUsed()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
