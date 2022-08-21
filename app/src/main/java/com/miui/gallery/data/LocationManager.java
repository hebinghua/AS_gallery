package com.miui.gallery.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import miuix.core.util.Pools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LocationManager {
    public static final String ALIAS_EXIF_GPS_VALID;
    public static final String ALIAS_LOCATION_INFO_INVALID;
    public static final HashMap<String, String> INVALID_NAME;
    public static final String LOCATION_INFO_SELECTION;
    public static LocationManager mInstance;
    public static String sRegion;
    public static final Locale FIXED_ADDRESS_LOCALE = Locale.UK;
    public static final String[] FILTER_FOR_TW = {"台灣", "台湾", "台湾省", "臺灣省", "Taiwan"};
    public static final String[] LOCATION_INFO_PROJECTION = {j.c, "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "location", "address", "extraGPS", "serverId"};
    public AddressCache mAddressCache = new AddressCache();
    public final String SEPARATOR = GalleryApp.sGetAndroidContext().getString(R.string.name_split);
    public final String CONNECTOR = GalleryApp.sGetAndroidContext().getString(R.string.and);
    public Map<String, String> mMediaGpsInfoStage = new ConcurrentHashMap();

    /* renamed from: $r8$lambda$BAsT2UK-WZXGLxpQ_hXZvHk9BUo */
    public static /* synthetic */ CloudItem m728$r8$lambda$BAsT2UKWZXGLxpQ_hXZvHk9BUo(LocationManager locationManager, Cursor cursor) {
        return locationManager.lambda$loadLocation$0(cursor);
    }

    public static int ensureAreaLevel(int i) {
        if (i == 0) {
            return 6;
        }
        if (i < 15) {
            return 4;
        }
        return i < 60 ? 3 : 0;
    }

    public static boolean isLatLngValid(double d, double d2) {
        return d >= -90.0d && d <= 90.0d && d2 >= -180.0d && d2 <= 180.0d;
    }

    public static boolean isSyncable() {
        return true;
    }

    public static String obtainString(String str, String str2) {
        return str == null ? str2 : str;
    }

    static {
        String format = String.format("((%s NOT NULL AND %s NOT NULL) or %s NOT NULL)", "exifGPSLatitude", "exifGPSLongitude", "extraGPS");
        ALIAS_EXIF_GPS_VALID = format;
        String format2 = String.format("(%s IS NULL OR %s IS NULL)", "address", "location");
        ALIAS_LOCATION_INFO_INVALID = format2;
        LOCATION_INFO_SELECTION = format + " AND " + format2 + " AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
        sRegion = null;
        HashMap<String, String> hashMap = new HashMap<>();
        INVALID_NAME = hashMap;
        hashMap.put("奇纳", "中国");
    }

    public static synchronized LocationManager getInstance() {
        LocationManager locationManager;
        synchronized (LocationManager.class) {
            if (mInstance == null) {
                mInstance = new LocationManager();
            }
            locationManager = mInstance;
        }
        return locationManager;
    }

    /* JADX WARN: Code restructure failed: missing block: B:91:0x00ad, code lost:
        com.miui.gallery.util.logger.DefaultLogger.d("LocationManager", "Network is not available");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void loadAllLocation() {
        /*
            Method dump skipped, instructions count: 285
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.data.LocationManager.loadAllLocation():void");
    }

    public void loadLocationAsync(final long j) {
        ThreadManager.getRequestPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.data.LocationManager.2
            {
                LocationManager.this = this;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                LocationManager.this.loadLocation(j);
                return null;
            }
        });
    }

    public boolean loadLocation(long j) {
        boolean z = false;
        if (!Geocoder.isPresent()) {
            DefaultLogger.w("LocationManager", "loadLocation, geocoder is not present.");
            return false;
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("LocationManager", "CTA is not available");
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("LocationManager", "Network is not available");
            return false;
        } else {
            Uri uri = GalleryContract.Cloud.CLOUD_URI;
            String[] strArr = LOCATION_INFO_PROJECTION;
            CloudItem cloudItem = (CloudItem) SafeDBUtil.safeQuery(sGetAndroidContext, uri, strArr, "_id=? AND " + LOCATION_INFO_SELECTION, new String[]{String.valueOf(j)}, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.data.LocationManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public final Object mo1808handle(Cursor cursor) {
                    return LocationManager.m728$r8$lambda$BAsT2UKWZXGLxpQ_hXZvHk9BUo(LocationManager.this, cursor);
                }
            });
            if (cloudItem != null) {
                try {
                    z = loadLocationForItem(sGetAndroidContext, cloudItem, true, null);
                } catch (Exception e) {
                    DefaultLogger.e("LocationManager", "loadLocation occur exception:" + e.getMessage());
                }
            }
            DefaultLogger.d("LocationManager", "loadLocation success %s", Boolean.valueOf(z));
            return z;
        }
    }

    public /* synthetic */ CloudItem lambda$loadLocation$0(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        return parseCursor(cursor);
    }

    public Address queryCachedAddress(double d, double d2) {
        CacheItem cacheItem = this.mAddressCache.get(Locale.getDefault(), d, d2);
        if (cacheItem != null) {
            return cacheItem.address;
        }
        return null;
    }

    public void appendDefaultLocationValues(ContentValues contentValues) {
        double d;
        ExtraGps parseGpsString;
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        String asString = contentValues.getAsString("exifGPSLatitude");
        String asString2 = contentValues.getAsString("exifGPSLatitudeRef");
        String asString3 = contentValues.getAsString("exifGPSLongitude");
        String asString4 = contentValues.getAsString("exifGPSLongitudeRef");
        String asString5 = contentValues.getAsString("extraGPS");
        boolean isEmpty = TextUtils.isEmpty(asString);
        boolean z = true;
        double d2 = SearchStatUtils.POW;
        if (!isEmpty && !TextUtils.isEmpty(asString3)) {
            d2 = LocationUtil.convertRationalLatLonToDouble(asString, asString2);
            d = LocationUtil.convertRationalLatLonToDouble(asString3, asString4);
        } else if (TextUtils.isEmpty(asString5) || (parseGpsString = parseGpsString(asString5)) == null) {
            z = false;
            d = 0.0d;
        } else {
            d2 = parseGpsString.lat;
            d = parseGpsString.lng;
        }
        if (z) {
            String decode = CitySearcher.getInstance().decode(new Coordinate(d2, d));
            if (!LocationUtil.isLocationValidate(decode)) {
                return;
            }
            contentValues.put("location", LocationUtil.getCityNameFromRes(sGetAndroidContext, decode));
        }
    }

    public void recordMediaLocation(String str, Intent intent) {
        if (TextUtils.isEmpty(str) || intent == null) {
            DefaultLogger.e("LocationManager", "recordMediaLocation path or intent null");
            return;
        }
        double doubleExtra = intent.getDoubleExtra("extra_cache_location_latitude", -1.0d);
        double doubleExtra2 = intent.getDoubleExtra("extra_cache_location_longitude", -1.0d);
        boolean z = (doubleExtra == -1.0d || doubleExtra2 == -1.0d) ? false : true;
        if (intent.hasExtra("extra_location_option")) {
            boolean booleanExtra = intent.getBooleanExtra("extra_location_option", true);
            int i = z ? booleanExtra ? z ? 3 : 2 : 1 : 0;
            HashMap hashMap = new HashMap();
            hashMap.put("result", String.valueOf(i));
            SamplingStatHelper.recordCountEvent("location_manager", "location_manager_gps_status", hashMap);
            DefaultLogger.d("LocationManager", "record location| no location: %s, cache Location not null: %s, option: %s, result: %s", Boolean.valueOf(z), Boolean.valueOf(z), Boolean.valueOf(booleanExtra), Integer.valueOf(i));
        }
        if (!z) {
            DefaultLogger.e("LocationManager", "hasCacheLocation:false");
            return;
        }
        long elapsedRealtimeNanos = (SystemClock.elapsedRealtimeNanos() - intent.getLongExtra("extra_cache_location_elapsedrealtimenanos", 0L)) / 60000000000L;
        HashMap hashMap2 = new HashMap();
        hashMap2.put("elapse_time", String.valueOf(elapsedRealtimeNanos));
        SamplingStatHelper.recordCountEvent("location_manager", "location_manager_gps_delay_min", hashMap2);
        if (elapsedRealtimeNanos < 0 || elapsedRealtimeNanos > 60) {
            DefaultLogger.d("LocationManager", "record location out of date %s min", Long.valueOf(elapsedRealtimeNanos));
            return;
        }
        DefaultLogger.d("LocationManager", "record location| delay %s min", Long.valueOf(elapsedRealtimeNanos));
        this.mMediaGpsInfoStage.put(str, formatExtraGpsString(doubleExtra, doubleExtra2, elapsedRealtimeNanos));
    }

    public void appendExtraGpsInfo(String str, ContentValues contentValues) {
        String remove = this.mMediaGpsInfoStage.remove(str);
        if (remove == null) {
            return;
        }
        String asString = contentValues.getAsString("exifGPSLatitude");
        String asString2 = contentValues.getAsString("exifGPSLongitude");
        if (!TextUtils.isEmpty(asString) && !TextUtils.isEmpty(asString2)) {
            return;
        }
        contentValues.put("extraGPS", remove);
    }

    public String subToCity(String str) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String[] segmentLocation = segmentLocation(str);
        if (segmentLocation != null) {
            if (segmentLocation.length == 1) {
                acquire.append("中国");
                acquire.append(segmentLocation[0]);
            } else {
                int min = Math.min(3, segmentLocation.length);
                for (int i = 0; i < min; i++) {
                    acquire.append(segmentLocation[i]);
                }
            }
        }
        String sb = acquire.length() == 0 ? null : acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public String generateTitleLine(String str) {
        if (!isValidLocation(str)) {
            return str;
        }
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        return generateTitleLine(arrayList);
    }

    public static boolean isValidLocation(String str) {
        return !TextUtils.isEmpty(str) && str.contains(h.g);
    }

    public String generateTitleLine(List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        int size = list.size();
        String[][] strArr = new String[size];
        int i = 0;
        boolean z = false;
        int i2 = 0;
        while (true) {
            boolean z2 = true;
            if (i >= size) {
                break;
            }
            String[] segmentLocation = segmentLocation(list.get(i));
            if (segmentLocation != null) {
                int i3 = i2 + 1;
                strArr[i2] = segmentLocation;
                if (segmentLocation.length <= 1) {
                    z2 = false;
                }
                z |= z2;
                i2 = i3;
            }
            i++;
        }
        String str = "";
        if (!z) {
            int i4 = 0;
            for (int i5 = 0; i5 < i2; i5++) {
                String str2 = strArr[i5][0];
                if (LocationUtil.isLocationValidate(str2) && !str.contains(str2)) {
                    if (str.length() > 0) {
                        str = str + this.SEPARATOR;
                    }
                    str = str + str2;
                    i4++;
                    if (i4 == 2) {
                        return str;
                    }
                }
            }
            return str;
        }
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList = new ArrayList(i2);
        int i6 = 0;
        for (int i7 = 0; i7 < 6; i7++) {
            getNamesOfArea(strArr, i2, i7, arrayList);
            if (arrayList.size() > 1) {
                if (sb.length() > 0) {
                    sb.append("-");
                }
                sb.append(formatAreaNames(arrayList));
                return sb.toString();
            }
            if (arrayList.size() == 1) {
                String str3 = arrayList.get(0);
                if (!str.equals(str3)) {
                    if ((i7 != 0 || !"中国".equals(str3)) && !filterName(str3)) {
                        if (sb.length() > 0) {
                            sb.append(this.SEPARATOR);
                        }
                        sb.append(str3);
                        i6++;
                        if (i6 == 2) {
                            return sb.toString();
                        }
                    }
                    str = str3;
                } else {
                    continue;
                }
            }
        }
        return sb.toString();
    }

    public String generateTitleLineForCnMonth(List<String> list) {
        String str;
        long currentTimeMillis = System.currentTimeMillis();
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        int size = list.size();
        String[][] strArr = new String[size];
        int i = 0;
        boolean z = false;
        int i2 = 0;
        while (true) {
            boolean z2 = true;
            if (i >= size) {
                break;
            }
            String[] segmentLocation = segmentLocation(list.get(i));
            if (segmentLocation != null) {
                int i3 = i2 + 1;
                strArr[i2] = segmentLocation;
                if (segmentLocation.length <= 1) {
                    z2 = false;
                }
                z |= z2;
                i2 = i3;
            }
            i++;
        }
        if (!z) {
            String str2 = "";
            int i4 = 0;
            for (int i5 = 0; i5 < i2; i5++) {
                String str3 = strArr[i5][0];
                if (LocationUtil.isLocationValidate(str3) && !str2.contains(str3)) {
                    if (str2.length() > 0) {
                        str2 = str2 + this.SEPARATOR;
                    }
                    str2 = str2 + str3;
                    i4++;
                    if (i4 == 3) {
                        return str2;
                    }
                }
            }
            return str2;
        }
        HashMap hashMap = new HashMap();
        for (int i6 = 0; i6 < i2; i6++) {
            int i7 = 0;
            while (true) {
                if (i7 >= strArr[i6].length) {
                    str = null;
                    break;
                } else if (strArr[i6][i7].contains("市")) {
                    str = strArr[i6][i7];
                    break;
                } else {
                    i7++;
                }
            }
            if ((str == null || str.isEmpty()) && strArr[i6].length > 0) {
                str = strArr[i6][0];
            }
            if (str != null && !str.isEmpty()) {
                Integer num = (Integer) hashMap.get(str);
                if (num == null) {
                    num = 0;
                }
                hashMap.put(str, Integer.valueOf(num.intValue() + 1));
            }
        }
        PriorityQueue priorityQueue = new PriorityQueue(LocationManager$$ExternalSyntheticLambda1.INSTANCE);
        priorityQueue.addAll(hashMap.entrySet());
        int min = Math.min(priorityQueue.size(), 3);
        ArrayList arrayList = new ArrayList(min);
        for (int i8 = 0; i8 < min; i8++) {
            arrayList.add((String) ((Map.Entry) priorityQueue.poll()).getKey());
        }
        DefaultLogger.i("LocationManager", "generateTitleLineForCnMonth duration=%s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (arrayList.size() == 1) {
            return arrayList.get(0);
        }
        return formatAreaNames(arrayList);
    }

    public static /* synthetic */ int lambda$generateTitleLineForCnMonth$1(Map.Entry entry, Map.Entry entry2) {
        return ((Integer) entry2.getValue()).intValue() - ((Integer) entry.getValue()).intValue();
    }

    public static boolean filterName(String str) {
        if ("TW".equalsIgnoreCase(getRegion()) || BaseBuildUtil.isRomBuildRegionTW()) {
            for (String str2 : FILTER_FOR_TW) {
                if (str2.equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getRegion() {
        if (sRegion == null) {
            sRegion = BaseBuildUtil.getRegion();
        }
        return sRegion;
    }

    public static String formatAddress(JSONArray jSONArray, String str) {
        if (jSONArray != null && jSONArray.length() > 0) {
            try {
                Locale locale = Locale.getDefault();
                JSONObject jSONObject = null;
                for (int length = jSONArray.length() - 1; length >= 0; length--) {
                    jSONObject = jSONArray.getJSONObject(length);
                    if (LocaleParser.toString(locale).equals(AddressParser.getLocaleStringFromJson(jSONObject))) {
                        break;
                    }
                }
                return formatAddress(AddressParser.fromJson(jSONObject), getAddressTargetLevel(str));
            } catch (JSONException e) {
                DefaultLogger.e("LocationManager", "formatAddress error.", e);
            }
        }
        return null;
    }

    public static String formatAddress(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return formatAddress(AddressParser.fromJson(str), getAddressTargetLevel(str2));
        } catch (JSONException e) {
            DefaultLogger.e("LocationManager", "formatAddress error.", e);
            return null;
        }
    }

    public static int getAddressTargetLevel(String str) {
        if (!TextUtils.isEmpty(str)) {
            ExtraGps parseGpsString = parseGpsString(str);
            if (parseGpsString == null) {
                return 0;
            }
            return ensureAreaLevel(parseGpsString.delayTime);
        }
        return 6;
    }

    public static void getNamesOfArea(String[][] strArr, int i, int i2, List<String> list) {
        list.clear();
        for (int i3 = 0; i3 < i; i3++) {
            if (strArr[i3].length == 6) {
                String str = strArr[i3][i2];
                if (!TextUtils.isEmpty(str) && !list.contains(str)) {
                    list.add(str);
                }
            }
        }
    }

    public final String formatAreaNames(List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        int size = list.size();
        int i = 0;
        if (size == 1) {
            return list.get(0);
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i2 = size - 2;
            if (i < i2) {
                sb.append(list.get(i));
                sb.append(this.SEPARATOR);
                i++;
            } else {
                sb.append(list.get(i2));
                sb.append(this.CONNECTOR);
                sb.append(list.get(size - 1));
                return sb.toString();
            }
        }
    }

    public static String[] segmentLocation(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.split(h.g, -1);
        if (split.length == 1 || split.length == 6) {
            return split;
        }
        DefaultLogger.e("LocationManager", "the length of location is not correct. %s", Integer.valueOf(split.length));
        return null;
    }

    public static String formatAddress(Address address, int i) {
        if (address == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(obtainArea(address, 0, i));
        sb.append(h.g);
        sb.append(obtainArea(address, 1, i));
        sb.append(h.g);
        sb.append(obtainArea(address, 2, i));
        sb.append(h.g);
        sb.append(obtainArea(address, 3, i));
        sb.append(h.g);
        sb.append(obtainArea(address, 4, i));
        sb.append(h.g);
        String obtainArea = obtainArea(address, 5, i);
        sb.append(obtainArea);
        String obtainArea2 = obtainArea(address, 6, i);
        if (!TextUtils.equals(obtainArea, obtainArea2)) {
            sb.append(obtainArea2);
        }
        return sb.toString();
    }

    public static String obtainArea(Address address, int i, int i2) {
        return i <= i2 ? obtainString(getArea(address, i)) : "";
    }

    public static String obtainString(String str) {
        return obtainString(str, "");
    }

    public static String getArea(Address address, int i) {
        switch (i) {
            case 0:
                return address.getCountryName();
            case 1:
                return address.getAdminArea();
            case 2:
                return address.getSubAdminArea();
            case 3:
                return address.getLocality();
            case 4:
                return address.getSubLocality();
            case 5:
                return address.getThoroughfare();
            case 6:
                return address.getSubThoroughfare();
            default:
                return null;
        }
    }

    public final boolean isNetworkAvailable() {
        return BaseNetworkUtils.isNetworkConnected() && !BaseNetworkUtils.isActiveNetworkMetered();
    }

    public static String formatExifGpsString(String str, String str2, String str3, String str4) {
        return (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) ? "" : String.format("%s,%s", Double.valueOf(LocationUtil.convertRationalLatLonToDouble(str, str3)), Double.valueOf(LocationUtil.convertRationalLatLonToDouble(str2, str4)));
    }

    public static String formatExtraGpsString(double d, double d2, long j) {
        return String.format("%s,%s,%s", Double.valueOf(d), Double.valueOf(d2), Long.valueOf(j));
    }

    public static ExtraGps parseGpsString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        int length = split.length;
        if (length != 2 && length != 3) {
            return null;
        }
        ExtraGps extraGps = new ExtraGps();
        try {
            extraGps.lat = Float.valueOf(split[0]).floatValue();
            extraGps.lng = Float.valueOf(split[1]).floatValue();
            if (length > 2) {
                extraGps.delayTime = Integer.valueOf(split[2]).intValue();
            }
            return extraGps;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void recordStatistics(Statistics statistics) {
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, String.valueOf(statistics.count));
        hashMap.put("error", String.valueOf(statistics.error));
        hashMap.put("cost_time", String.valueOf(statistics.getAverageCost()));
        hashMap.put("total_time", String.valueOf(statistics.getTotalTime()));
        hashMap.put("percentage", String.valueOf(statistics.getCacheHitPercent()));
        hashMap.put("average", String.valueOf(statistics.getAvgCacheDistance()));
        hashMap.put("invalid", String.valueOf(statistics.latLngInvalid));
        hashMap.put("null", String.valueOf(statistics.addressNull));
        hashMap.put("fail_count", String.valueOf(statistics.saveFail));
        DefaultLogger.d("LocationManager", "loadLocations: %s", hashMap.toString());
        SamplingStatHelper.recordCountEvent("location_manager", "location_manager_load_stat", hashMap);
    }

    public final void checkVersionAndUpgrade() {
        int locationDetailsVersion = GalleryPreferences.LocationManager.getLocationDetailsVersion(0);
        if (locationDetailsVersion > 0 && locationDetailsVersion < 3 && isSyncable()) {
            DefaultLogger.d("LocationManager", "On clear old addresses due to location manager upgrade");
            ContentValues contentValues = new ContentValues();
            contentValues.putNull("address");
            GalleryUtils.safeUpdate(GalleryContract.Cloud.CLOUD_URI, contentValues, null, null);
        }
        if (locationDetailsVersion != 3) {
            GalleryPreferences.LocationManager.setLocationDetailsVersion(3);
        }
    }

    public final boolean loadLocationForItem(Context context, CloudItem cloudItem, boolean z, Statistics statistics) throws Exception {
        String format;
        boolean z2;
        Statistics statistics2 = statistics == null ? new Statistics() : statistics;
        boolean z3 = false;
        if (!isLatLngValid(cloudItem.lat, cloudItem.lng)) {
            DefaultLogger.w("LocationManager", "Lat or lng not valid.");
            statistics2.latLngInvalid++;
            return false;
        }
        List<Address> addressList = getAddressList(context, cloudItem, z, statistics2);
        if (!BaseMiscUtil.isValid(addressList)) {
            return false;
        }
        String formatAddress = formatAddress(addressList.get(0), ensureAreaLevel(cloudItem.delayTime));
        String json = AddressParser.toJson(addressList);
        if (TextUtils.isEmpty(cloudItem.serverId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("location", formatAddress);
            contentValues.put("address", json);
            if (SafeDBUtil.safeUpdate(context, GalleryContract.Cloud.CLOUD_URI, contentValues, "_id=?", new String[]{String.valueOf(cloudItem.id)}) > 0) {
                z3 = true;
            }
            if (!z3) {
                DefaultLogger.w("LocationManager", "Save location info failed.");
                statistics2.saveFail++;
            }
            return z3;
        }
        if (z) {
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("location", formatAddress);
            contentValues2.put("address", json);
            z2 = SafeDBUtil.safeUpdate(context, GalleryContract.Cloud.CLOUD_URI, contentValues2, "_id=?", new String[]{String.valueOf(cloudItem.id)}) > 0;
            format = "";
        } else {
            format = String.format("%s=%s, %s=%s,", "location", DatabaseUtils.sqlEscapeString(formatAddress), "address", DatabaseUtils.sqlEscapeString(json));
            z2 = true;
        }
        if (!z2) {
            DefaultLogger.w("LocationManager", "Save location info failed.");
            statistics2.saveFail++;
            return false;
        }
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(70);
        boolean safeExec = GalleryUtils.safeExec(String.format("update %s set %s %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%s", "cloud", format, "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, Long.valueOf(cloudItem.id)));
        if (!safeExec) {
            DefaultLogger.w("LocationManager", "Save location info and update edited column failed.");
            statistics2.saveFail++;
        }
        return safeExec;
    }

    public final List<Address> getAddressList(Context context, CloudItem cloudItem, boolean z, Statistics statistics) throws Exception {
        Locale locale = Locale.getDefault();
        Address address = getAddress(context, locale, cloudItem, z, statistics);
        if (address == null) {
            DefaultLogger.w("LocationManager", "default address not valid.");
            statistics.addressNull++;
            return null;
        }
        if (Rom.IS_INTERNATIONAL) {
            Locale locale2 = FIXED_ADDRESS_LOCALE;
            if (!locale.equals(locale2)) {
                Address address2 = getAddress(context, locale2, cloudItem, z, statistics);
                if (address2 == null) {
                    DefaultLogger.w("LocationManager", "fixed address not valid.");
                    statistics.addressNull++;
                    return null;
                }
                return Arrays.asList(address, address2);
            }
        }
        return Collections.singletonList(address);
    }

    public final Address getAddress(Context context, Locale locale, CloudItem cloudItem, boolean z, Statistics statistics) throws Exception {
        Address address;
        CacheItem cacheItem = this.mAddressCache.get(locale, cloudItem.lat, cloudItem.lng);
        if (cacheItem != null) {
            address = cacheItem.address;
            statistics.cacheHit++;
            statistics.cacheDistance += cacheItem.distance;
            if (z) {
                DefaultLogger.d("LocationManager", "cache hit.");
            }
        } else {
            address = null;
        }
        if (address == null) {
            Geocoder geocoder = new Geocoder(context, locale);
            long currentTimeMillis = System.currentTimeMillis();
            List<Address> fromLocation = geocoder.getFromLocation(cloudItem.lat, cloudItem.lng, 1);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            statistics.cost += currentTimeMillis2;
            if (z) {
                DefaultLogger.d("LocationManager", "get location cost: %s", Long.valueOf(currentTimeMillis2));
            }
            if (!BaseMiscUtil.isValid(fromLocation)) {
                return address;
            }
            Address fixAddressFault = fixAddressFault(fromLocation.get(0));
            this.mAddressCache.put(locale, cloudItem.lat, cloudItem.lng, fixAddressFault);
            return fixAddressFault;
        }
        return address;
    }

    public static Address fixAddressFault(Address address) {
        if (address != null) {
            String str = INVALID_NAME.get(address.getCountryName());
            if (str != null) {
                address.setCountryName(str);
            }
            String thoroughfare = address.getThoroughfare();
            if (thoroughfare != null && thoroughfare.indexOf(h.g) > 0) {
                address.setThoroughfare(thoroughfare.replaceAll(h.g, ""));
            }
            String subThoroughfare = address.getSubThoroughfare();
            if (subThoroughfare != null && subThoroughfare.indexOf(h.g) > 0) {
                address.setSubThoroughfare(subThoroughfare.replaceAll(h.g, ""));
            }
        }
        return address;
    }

    public final CloudItem parseCursor(Cursor cursor) {
        CloudItem cloudItem = new CloudItem();
        cloudItem.id = cursor.getInt(0);
        cloudItem.location = cursor.getString(5);
        cloudItem.address = cursor.getString(6);
        String string = cursor.getString(1);
        String string2 = cursor.getString(2);
        String string3 = cursor.getString(3);
        String string4 = cursor.getString(4);
        String string5 = cursor.getString(7);
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string3)) {
            cloudItem.lat = LocationUtil.convertRationalLatLonToDouble(string, string2);
            cloudItem.lng = LocationUtil.convertRationalLatLonToDouble(string3, string4);
        } else {
            ExtraGps parseGpsString = parseGpsString(string5);
            if (parseGpsString != null) {
                cloudItem.lat = parseGpsString.lat;
                cloudItem.lng = parseGpsString.lng;
                cloudItem.delayTime = parseGpsString.delayTime;
            }
        }
        cloudItem.serverId = cursor.getString(8);
        return cloudItem;
    }

    /* loaded from: classes.dex */
    public static class CloudItem {
        public String address;
        public int delayTime;
        public long id;
        public double lat;
        public double lng;
        public String location;
        public String serverId;

        public CloudItem() {
        }
    }

    /* loaded from: classes.dex */
    public static class ExtraGps {
        public int delayTime;
        public float lat;
        public float lng;

        public ExtraGps() {
        }
    }

    /* loaded from: classes.dex */
    public static class AddressParser {
        public static String toJson(List<Address> list) throws JSONException {
            if (!BaseMiscUtil.isValid(list)) {
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            for (Address address : list) {
                jSONArray.put(toJson(address));
            }
            return jSONArray.toString();
        }

        public static JSONObject toJson(Address address) throws JSONException {
            JSONArray jSONArray = null;
            if (address == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("locale", LocaleParser.toString(address.getLocale()));
            jSONObject.putOpt("countryName", address.getCountryName());
            jSONObject.putOpt("admin", address.getAdminArea());
            jSONObject.putOpt("subAdmin", address.getSubAdminArea());
            jSONObject.putOpt("locality", address.getLocality());
            jSONObject.putOpt("subLocality", address.getSubLocality());
            jSONObject.putOpt("thoroughfare", address.getThoroughfare());
            jSONObject.putOpt("subThoroughfare", address.getSubThoroughfare());
            int min = Math.min(address.getMaxAddressLineIndex() + 1, 5);
            for (int i = 0; i < min; i++) {
                if (jSONArray == null) {
                    jSONArray = new JSONArray();
                }
                jSONArray.put(address.getAddressLine(i));
            }
            jSONObject.putOpt("addressLines", jSONArray);
            return jSONObject;
        }

        public static Address fromJson(String str) throws JSONException {
            if (str == null) {
                return null;
            }
            try {
                return fromJson(new JSONObject(str));
            } catch (JSONException unused) {
                return fromJson(new JSONArray(str).getJSONObject(0));
            }
        }

        public static Address fromJson(JSONObject jSONObject) throws JSONException {
            if (jSONObject == null) {
                return null;
            }
            Address address = new Address(getLocaleFromJson(jSONObject));
            address.setCountryName(jSONObject.optString("countryName"));
            address.setAdminArea(jSONObject.optString("admin"));
            address.setSubAdminArea(jSONObject.optString("subAdmin"));
            address.setLocality(jSONObject.optString("locality"));
            address.setSubLocality(jSONObject.optString("subLocality"));
            address.setThoroughfare(jSONObject.optString("thoroughfare"));
            address.setSubThoroughfare(jSONObject.optString("subThoroughfare"));
            JSONArray optJSONArray = jSONObject.optJSONArray("addressLines");
            if (optJSONArray != null) {
                int length = optJSONArray.length();
                for (int i = 0; i < length; i++) {
                    address.setAddressLine(i, optJSONArray.optString(i));
                }
            }
            return address;
        }

        public static String getLocaleStringFromJson(JSONObject jSONObject) {
            return jSONObject.optString("locale");
        }

        public static Locale getLocaleFromJson(JSONObject jSONObject) {
            if (LocaleParser.fromString(getLocaleStringFromJson(jSONObject)) == null) {
                return Locale.getDefault();
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class LocaleParser {
        public static Locale fromString(String str) {
            String str2 = null;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            String[] split = str.split("_");
            if (split.length <= 0 || split[0] == null) {
                return null;
            }
            String str3 = split[0];
            String str4 = split.length > 1 ? split[1] : null;
            if (split.length > 2) {
                str2 = split[2];
            }
            if (str4 == null) {
                return new Locale(str3);
            }
            if (str2 == null) {
                return new Locale(str3, str4);
            }
            return new Locale(str3, str4, str2);
        }

        public static String toString(Locale locale) {
            if (locale == null) {
                return null;
            }
            String language = locale.getLanguage();
            String country = locale.getCountry();
            String variant = locale.getVariant();
            StringBuilder sb = new StringBuilder();
            sb.append(language);
            if (!TextUtils.isEmpty(country)) {
                sb.append("_");
                sb.append(country);
                if (!TextUtils.isEmpty(variant)) {
                    sb.append("_");
                    sb.append(variant);
                }
            }
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    public static class AddressCache extends ArrayList<CacheItem> {
        private AddressCache() {
        }

        public synchronized void put(Locale locale, double d, double d2, Address address) {
            if (address == null) {
                return;
            }
            CacheItem cacheItem = new CacheItem();
            cacheItem.locale = locale;
            cacheItem.lat = d;
            cacheItem.lng = d2;
            cacheItem.address = address;
            add(cacheItem);
        }

        public synchronized CacheItem get(Locale locale, double d, double d2) {
            Locale locale2;
            int size = size();
            for (int i = 0; i < size; i++) {
                CacheItem cacheItem = get(i);
                if (locale == null || (locale2 = cacheItem.locale) == null || locale.equals(locale2)) {
                    double distance = distance(cacheItem.lat, cacheItem.lng, d, d2);
                    if (distance < 50.0d) {
                        cacheItem.distance = (int) distance;
                        return cacheItem;
                    }
                }
            }
            return null;
        }

        public final double distance(double d, double d2, double d3, double d4) {
            double d5 = d * 0.017453292519943295d;
            double d6 = d3 * 0.017453292519943295d;
            return Math.acos((Math.sin(d5) * Math.sin(d6)) + (Math.cos(d5) * Math.cos(d6) * Math.cos((d4 * 0.017453292519943295d) - (d2 * 0.017453292519943295d)))) * 6371.0d * 1000.0d;
        }
    }

    /* loaded from: classes.dex */
    public static class CacheItem {
        public Address address;
        public int distance;
        public double lat;
        public double lng;
        public Locale locale;

        public CacheItem() {
        }
    }

    /* loaded from: classes.dex */
    public static class Statistics {
        public int addressNull;
        public int cacheDistance;
        public int cacheHit;
        public long cost;
        public int count;
        public int error;
        public int latLngInvalid;
        public int saveFail;
        public long startTime;

        public Statistics() {
            this.count = 0;
            this.cacheHit = 0;
            this.cost = 0L;
            this.error = 0;
            this.latLngInvalid = 0;
            this.addressNull = 0;
            this.saveFail = 0;
            this.startTime = 0L;
        }

        public long getAverageCost() {
            int i = (this.count - this.cacheHit) - this.error;
            if (i > 0) {
                long j = this.cost;
                if (j > 0) {
                    return j / i;
                }
            }
            return 0L;
        }

        public int getCacheHitPercent() {
            int i;
            int i2 = this.count;
            if (i2 <= 0 || (i = this.cacheHit) > i2) {
                return 0;
            }
            return (int) ((i * 100.0f) / i2);
        }

        public void start() {
            this.startTime = System.currentTimeMillis();
        }

        public int getTotalTime() {
            if (this.startTime > 0) {
                return (int) ((System.currentTimeMillis() - this.startTime) / 1000);
            }
            return 0;
        }

        public int getAvgCacheDistance() {
            int i;
            int i2 = this.cacheDistance;
            if (i2 <= 0 || (i = this.cacheHit) <= 0) {
                return 0;
            }
            return i2 / i;
        }
    }
}
