package com.market.sdk;

import android.util.Log;
import miui.os.Build;

/* loaded from: classes.dex */
public enum MarketFeatures {
    INSTALL_LOCAL_APK(1914330, -1, 1914531, -1),
    DESK_RECOMMEND_V2(1914331, 1914331, -1, 1914312),
    DESK_RECOMMEND_V3(1914341, 1914341, -1, -1),
    DESK_FOLDER_CATEGORY_NAME(1914451, 1914451, 1914360, -1),
    DISCOVER_METERED_UPDATE_CONFIRM(-1, -1, -1, 1914380),
    FLOAT_CARD(1914651, 1914651, -1, -1);
    
    private final int mDiscoverSupportVersion;
    private final int mMarketSupportVersion;
    private final int mMipicksSupportVersion;
    private final int mPadSupportVersion;

    MarketFeatures(int i, int i2, int i3, int i4) {
        this.mMarketSupportVersion = i;
        this.mMipicksSupportVersion = i3;
        this.mPadSupportVersion = i2;
        this.mDiscoverSupportVersion = i4;
    }

    public boolean isSupported() {
        try {
            if (Build.IS_INTERNATIONAL_BUILD) {
                if (!isSupportedBy(MarketType.MIPICKS) && !isSupportedBy(MarketType.DISCOVER)) {
                    return false;
                }
                return true;
            } else if (Build.IS_MIPAD) {
                return isSupportedBy(MarketType.MARKET_PAD);
            } else {
                return isSupportedBy(MarketType.MARKET_PHONE);
            }
        } catch (Throwable th) {
            Log.d("MarketManager", th.toString());
            return isSupportedBy(MarketType.MARKET_PHONE);
        }
    }

    public boolean isSupportedBy(MarketType marketType) {
        int i;
        if (!marketType.isEnabled()) {
            return false;
        }
        int versionCode = marketType.getVersionCode();
        int i2 = AnonymousClass1.$SwitchMap$com$market$sdk$MarketType[marketType.ordinal()];
        if (i2 == 1) {
            i = this.mMarketSupportVersion;
        } else if (i2 == 2) {
            i = this.mPadSupportVersion;
        } else if (i2 == 3) {
            i = this.mMipicksSupportVersion;
        } else if (i2 != 4) {
            return false;
        } else {
            i = this.mDiscoverSupportVersion;
        }
        return i != -1 && versionCode >= i;
    }

    /* renamed from: com.market.sdk.MarketFeatures$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$market$sdk$MarketType;

        static {
            int[] iArr = new int[MarketType.values().length];
            $SwitchMap$com$market$sdk$MarketType = iArr;
            try {
                iArr[MarketType.MARKET_PHONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$market$sdk$MarketType[MarketType.MARKET_PAD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$market$sdk$MarketType[MarketType.MIPICKS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$market$sdk$MarketType[MarketType.DISCOVER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }
}
