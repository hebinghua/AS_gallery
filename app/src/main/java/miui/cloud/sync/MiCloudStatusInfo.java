package miui.cloud.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.xiaomi.micloudsdk.cloudinfo.utils.CloudInfoUtils;
import java.util.ArrayList;
import java.util.Map;
import miui.accounts.ExtraAccountManager;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class MiCloudStatusInfo {
    public QuotaInfo mQuotaInfo;
    public String mUserId;
    public boolean mVipEnable;

    public MiCloudStatusInfo(String str) {
        this.mUserId = str;
    }

    public void parseQuotaString(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e("MiCloudStatusInfo", "parseQuotaString() quota is empty.");
            this.mQuotaInfo = null;
            return;
        }
        try {
            this.mQuotaInfo = CloudInfoUtils.getQuotaInfo(this, new JSONObject(str));
        } catch (JSONException unused) {
            Log.e("MiCloudStatusInfo", "catch JSONException in parseQuotaString()");
            this.mQuotaInfo = null;
        }
    }

    public void parseMap(Map map) {
        Object obj = map.get("quota");
        if (obj instanceof Map) {
            this.mQuotaInfo = mapToQuotaInfo((Map) obj);
        }
        Object obj2 = map.get("VIPAvailable");
        if (obj2 instanceof Boolean) {
            this.mVipEnable = ((Boolean) obj2).booleanValue();
        }
    }

    public QuotaInfo getQuotaInfo() {
        return this.mQuotaInfo;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final miui.cloud.sync.MiCloudStatusInfo.QuotaInfo mapToQuotaInfo(java.util.Map r20) {
        /*
            r19 = this;
            r0 = r20
            java.lang.String r1 = "total"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.Long
            r3 = 0
            if (r2 == 0) goto L16
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            r7 = r1
            goto L17
        L16:
            r7 = r3
        L17:
            java.lang.String r1 = "used"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.Integer
            if (r2 == 0) goto L2a
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            long r1 = (long) r1
        L28:
            r9 = r1
            goto L36
        L2a:
            boolean r2 = r1 instanceof java.lang.Long
            if (r2 == 0) goto L35
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            goto L28
        L35:
            r9 = r3
        L36:
            java.lang.String r1 = "warn"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.String
            java.lang.String r5 = ""
            if (r2 == 0) goto L46
            java.lang.String r1 = (java.lang.String) r1
            r11 = r1
            goto L47
        L46:
            r11 = r5
        L47:
            java.lang.String r1 = "yearlyPackageType"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.String
            if (r2 == 0) goto L55
            java.lang.String r1 = (java.lang.String) r1
            r12 = r1
            goto L56
        L55:
            r12 = r5
        L56:
            java.lang.String r1 = "yearlyPackageSize"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.Long
            if (r2 == 0) goto L68
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            r13 = r1
            goto L69
        L68:
            r13 = r3
        L69:
            java.lang.String r1 = "yearlyPackageCreateTime"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.Long
            if (r2 == 0) goto L7b
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            r15 = r1
            goto L7c
        L7b:
            r15 = r3
        L7c:
            java.lang.String r1 = "yearlyPackageExpireTime"
            java.lang.Object r1 = r0.get(r1)
            boolean r2 = r1 instanceof java.lang.Long
            if (r2 == 0) goto L8c
            java.lang.Long r1 = (java.lang.Long) r1
            long r3 = r1.longValue()
        L8c:
            r17 = r3
            miui.cloud.sync.MiCloudStatusInfo$QuotaInfo r1 = new miui.cloud.sync.MiCloudStatusInfo$QuotaInfo
            r5 = r1
            r6 = r19
            r5.<init>(r7, r9, r11, r12, r13, r15, r17)
            java.lang.String r2 = "items"
            java.lang.Object r0 = r0.get(r2)
            boolean r2 = r0 instanceof java.util.Map
            if (r2 == 0) goto Lc6
            java.util.Map r0 = (java.util.Map) r0
            java.util.Set r2 = r0.keySet()
            java.util.Iterator r2 = r2.iterator()
        Laa:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto Lc6
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.Object r4 = r0.get(r3)
            java.util.Map r4 = (java.util.Map) r4
            r5 = r19
            miui.cloud.sync.MiCloudStatusInfo$ItemInfo r3 = r5.mapToItemInfo(r3, r4)
            r1.addItemInfo(r3)
            goto Laa
        Lc6:
            r5 = r19
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.cloud.sync.MiCloudStatusInfo.mapToQuotaInfo(java.util.Map):miui.cloud.sync.MiCloudStatusInfo$QuotaInfo");
    }

    public final ItemInfo mapToItemInfo(String str, Map map) {
        long longValue;
        Object obj = map.get("localized_name");
        String str2 = obj instanceof String ? (String) obj : "";
        Object obj2 = map.get("used");
        if (obj2 instanceof Integer) {
            longValue = ((Integer) obj2).intValue();
        } else {
            longValue = obj2 instanceof Long ? ((Long) obj2).longValue() : 0L;
        }
        return new ItemInfo(str, str2, longValue);
    }

    public static MiCloudStatusInfo fromUserData(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account xiaomiAccount = ExtraAccountManager.getXiaomiAccount(context);
        if (xiaomiAccount == null) {
            return null;
        }
        String userData = accountManager.getUserData(xiaomiAccount, "extra_micloud_status_info_quota");
        MiCloudStatusInfo miCloudStatusInfo = new MiCloudStatusInfo(xiaomiAccount.name);
        miCloudStatusInfo.parseQuotaString(userData);
        QuotaInfo quotaInfo = miCloudStatusInfo.getQuotaInfo();
        if (quotaInfo == null || quotaInfo.getWarn() == null) {
            Log.w("MiCloudStatusInfo", "deserialize failed");
            accountManager.setUserData(xiaomiAccount, "extra_micloud_status_info_quota", "");
        }
        return miCloudStatusInfo;
    }

    /* loaded from: classes3.dex */
    public class QuotaInfo {
        public ArrayList<ItemInfo> mItemInfoList = new ArrayList<>();
        public long mTotal;
        public long mUsed;
        public String mWarn;
        public long mYearlyPackageCreateTime;
        public long mYearlyPackageExpireTime;
        public long mYearlyPackageSize;
        public String mYearlyPackageType;

        public QuotaInfo(long j, long j2, String str, String str2, long j3, long j4, long j5) {
            this.mTotal = j;
            this.mUsed = j2;
            this.mWarn = str;
            this.mYearlyPackageType = str2;
            this.mYearlyPackageSize = j3;
            this.mYearlyPackageCreateTime = j4;
            this.mYearlyPackageExpireTime = j5;
        }

        public void addItemInfo(ItemInfo itemInfo) {
            this.mItemInfoList.add(itemInfo);
        }

        public long getTotal() {
            return this.mTotal;
        }

        public long getUsed() {
            return this.mUsed;
        }

        public String getWarn() {
            return this.mWarn;
        }

        public String getYearlyPackageType() {
            return this.mYearlyPackageType;
        }

        public boolean isSpaceFull() {
            return "full".equals(getWarn());
        }

        public boolean isSpaceLowPercent() {
            return "low_percent".equals(getWarn());
        }

        public String toString() {
            return "QuotaInfo{mTotal=" + this.mTotal + ", mUsed=" + this.mUsed + ", mWarn='" + this.mWarn + CoreConstants.SINGLE_QUOTE_CHAR + ", mYearlyPackageType='" + this.mYearlyPackageType + CoreConstants.SINGLE_QUOTE_CHAR + ", mYearlyPackageSize=" + this.mYearlyPackageSize + ", mYearlyPackageCreateTime=" + this.mYearlyPackageCreateTime + ", mYearlyPackageExpireTime=" + this.mYearlyPackageExpireTime + ", mItemInfoList=" + this.mItemInfoList + '}';
        }
    }

    /* loaded from: classes3.dex */
    public class ItemInfo {
        public String mLocalizedName;
        public String mName;
        public long mUsed;

        public ItemInfo(String str, String str2, long j) {
            this.mName = str;
            this.mLocalizedName = str2;
            this.mUsed = j;
        }

        public String toString() {
            return "ItemInfo{mName=" + this.mName + ", mLocalizedName=" + this.mLocalizedName + ", mUsed='" + this.mUsed + '}';
        }
    }
}
