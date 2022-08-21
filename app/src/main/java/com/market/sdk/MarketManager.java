package com.market.sdk;

import android.app.Application;
import android.content.Context;
import com.market.sdk.utils.AppGlobal;
import miuix.os.Build;

/* loaded from: classes.dex */
public class MarketManager {
    public static final String MARKET_PACKAGE_NAME = initMarketPackageName();
    public static volatile MarketManager sManager;
    public final String DETAIL_CLASS_NAME = "com.xiaomi.market.ui.AppDetailActivity";
    public final String MARKET_SERVICE_CLASS_NAME = "com.xiaomi.market.data.MarketService";
    public final String MARKET_USER_AGREEMENT_CLASS = "com.xiaomi.market.ui.UserAgreementActivity";
    public Context mContext;

    public MarketManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static MarketManager getManager() {
        if (sManager == null) {
            synchronized (MarketManager.class) {
                if (sManager == null) {
                    sManager = new MarketManager(AppGlobal.getContext());
                }
            }
        }
        return sManager;
    }

    public static String initMarketPackageName() {
        try {
            return Build.IS_INTERNATIONAL_BUILD ? "com.xiaomi.discover" : "com.xiaomi.market";
        } catch (Throwable unused) {
            return "com.xiaomi.market";
        }
    }

    public boolean hasFeature(MarketFeatures marketFeatures) {
        return marketFeatures.isSupported();
    }

    public FloatCardManager getFloatCardManager() {
        return FloatCardManager.get((Application) this.mContext.getApplicationContext());
    }
}
