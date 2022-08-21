package com.miui.gallery.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class ReverseGeocoder {
    public ConnectivityManager mConnectivityManager;
    public Context mContext;
    public Geocoder mGeocoder;

    public ReverseGeocoder(Context context) {
        this.mContext = context;
        this.mGeocoder = new Geocoder(this.mContext);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public Address lookupAddress(double d, double d2, boolean z, ThreadPool.JobContext jobContext) {
        Address address;
        DataOutputStream dataOutputStream;
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        DataOutputStream dataOutputStream2 = null;
        if (BaseGalleryPreferences.CTA.canConnectNetwork() && activeNetworkInfo != null) {
            try {
                if (activeNetworkInfo.isConnected()) {
                    try {
                    } catch (Exception e) {
                        e = e;
                        address = null;
                    }
                    if (jobContext.isCancelled()) {
                        BaseMiscUtil.closeSilently(null);
                        return null;
                    }
                    List<Address> fromLocation = this.mGeocoder.getFromLocation(d, d2, 1);
                    if (jobContext.isCancelled()) {
                        BaseMiscUtil.closeSilently(null);
                        return null;
                    }
                    if (!fromLocation.isEmpty()) {
                        address = fromLocation.get(0);
                        try {
                            dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
                        } catch (Exception e2) {
                            e = e2;
                        }
                        try {
                            Locale locale = address.getLocale();
                            writeUTF(dataOutputStream, locale.getLanguage());
                            writeUTF(dataOutputStream, locale.getCountry());
                            writeUTF(dataOutputStream, locale.getVariant());
                            writeUTF(dataOutputStream, address.getThoroughfare());
                            int maxAddressLineIndex = address.getMaxAddressLineIndex();
                            dataOutputStream.writeInt(maxAddressLineIndex);
                            for (int i = 0; i < maxAddressLineIndex; i++) {
                                if (jobContext.isCancelled()) {
                                    BaseMiscUtil.closeSilently(dataOutputStream);
                                    return null;
                                }
                                writeUTF(dataOutputStream, address.getAddressLine(i));
                            }
                            writeUTF(dataOutputStream, address.getFeatureName());
                            writeUTF(dataOutputStream, address.getLocality());
                            writeUTF(dataOutputStream, address.getAdminArea());
                            writeUTF(dataOutputStream, address.getSubAdminArea());
                            writeUTF(dataOutputStream, address.getCountryName());
                            writeUTF(dataOutputStream, address.getCountryCode());
                            writeUTF(dataOutputStream, address.getPostalCode());
                            writeUTF(dataOutputStream, address.getPhone());
                            writeUTF(dataOutputStream, address.getUrl());
                            dataOutputStream.flush();
                            dataOutputStream2 = dataOutputStream;
                        } catch (Exception e3) {
                            e = e3;
                            dataOutputStream2 = dataOutputStream;
                            DefaultLogger.w("ReverseGeocoder", e);
                            BaseMiscUtil.closeSilently(dataOutputStream2);
                            return address;
                        } catch (Throwable th) {
                            th = th;
                            dataOutputStream2 = dataOutputStream;
                            BaseMiscUtil.closeSilently(dataOutputStream2);
                            throw th;
                        }
                    } else {
                        address = null;
                    }
                    BaseMiscUtil.closeSilently(dataOutputStream2);
                    return address;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return null;
    }

    public static final void writeUTF(DataOutputStream dataOutputStream, String str) throws IOException {
        if (str == null) {
            dataOutputStream.writeUTF("");
        } else {
            dataOutputStream.writeUTF(str);
        }
    }

    public Address lookupAddress(double d, double d2, boolean z) {
        return lookupAddress(d, d2, z, new ThreadPool.JobContext() { // from class: com.miui.gallery.data.ReverseGeocoder.1
            @Override // com.miui.gallery.concurrent.ThreadPool.JobContext
            public boolean isCancelled() {
                return false;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.JobContext
            public void setCancelListener(ThreadPool.CancelListener cancelListener) {
            }
        });
    }
}
