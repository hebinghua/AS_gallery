package com.miui.gallery.cloud.baby;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

/* loaded from: classes.dex */
public final class BabyInfo implements Parcelable {
    public static final Parcelable.Creator<BabyInfo> CREATOR = new Parcelable.Creator<BabyInfo>() { // from class: com.miui.gallery.cloud.baby.BabyInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BabyInfo mo691createFromParcel(Parcel parcel) {
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            String readString5 = parcel.readString();
            String readString6 = parcel.readString();
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            return new BabyInfo(readString, readString2, readString3, readString4, readString5, readString6, z, parcel.readInt());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BabyInfo[] mo692newArray(int i) {
            return new BabyInfo[i];
        }
    };
    public static final int[] sDaysInMon = {31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30};
    @SerializedName("autoUpdate")
    public boolean mAutoupdate;
    @SerializedName("birthday")
    public String mBirthday;
    @SerializedName("localFlag")
    public int mLocalFlag;
    @SerializedName("name")
    public String mNickName;
    @SerializedName("peopleId")
    public String mPeopleId;
    @SerializedName("relation")
    public String mRelation;
    @SerializedName("relationText")
    public String mRelationText;
    @SerializedName("gender")
    public String mSex;
    @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
    public String type = "baby";

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BabyInfo(String str, String str2, String str3, String str4, String str5, String str6, boolean z, int i) {
        this.mNickName = str;
        this.mBirthday = str2;
        this.mSex = str3;
        this.mRelation = str4;
        this.mRelationText = str5;
        this.mPeopleId = str6;
        this.mAutoupdate = z;
        this.mLocalFlag = i;
    }

    public static BabyInfo fromJSON(String str) {
        if (!TextUtils.isEmpty(str)) {
            return (BabyInfo) GsonUtils.fromJson(str, (Class<Object>) BabyInfo.class);
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BabyInfo)) {
            return false;
        }
        BabyInfo babyInfo = (BabyInfo) obj;
        return TextUtils.equals(this.mNickName, babyInfo.mNickName) && TextUtils.equals(this.mBirthday, babyInfo.mBirthday) && TextUtils.equals(this.mSex, babyInfo.mSex) && TextUtils.equals(this.mRelation, babyInfo.mRelation) && TextUtils.equals(this.mRelationText, babyInfo.mRelationText) && TextUtils.equals(this.mPeopleId, babyInfo.mPeopleId) && this.mAutoupdate == babyInfo.mAutoupdate && this.mLocalFlag == babyInfo.mLocalFlag;
    }

    public int hashCode() {
        return Objects.hash(this.mNickName, this.mBirthday, this.mSex, this.mRelation, this.mRelationText, this.mPeopleId, Boolean.valueOf(this.mAutoupdate), Integer.valueOf(this.mLocalFlag));
    }

    public String toJSON() {
        return GsonUtils.toJson(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mNickName);
        parcel.writeString(this.mBirthday);
        parcel.writeString(this.mSex);
        parcel.writeString(this.mRelation);
        parcel.writeString(this.mRelationText);
        parcel.writeString(this.mPeopleId);
        parcel.writeInt(this.mAutoupdate ? 1 : 0);
        parcel.writeInt(this.mLocalFlag);
    }

    public static int[] splitBirthDay(BabyInfo babyInfo) {
        if (babyInfo == null || TextUtils.isEmpty(babyInfo.mBirthday)) {
            return null;
        }
        String[] split = babyInfo.mBirthday.split("-");
        if (split != null && split.length == 3) {
            try {
                return new int[]{Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), Integer.valueOf(split[2]).intValue()};
            } catch (NumberFormatException e) {
                e.printStackTrace();
                DefaultLogger.e("BabyInfo", "illegal birthday: " + babyInfo.mBirthday);
                return null;
            }
        }
        DefaultLogger.e("BabyInfo", "illegal birthday: " + babyInfo.mBirthday);
        return null;
    }

    public static int[] getAge(long j, int i, int i2, int i3) {
        int i4;
        int[] currentYearMonthDay = getCurrentYearMonthDay(j);
        int i5 = currentYearMonthDay[0];
        int i6 = currentYearMonthDay[1];
        int i7 = currentYearMonthDay[2];
        int i8 = i7 - i3;
        int i9 = -1;
        if (i8 < 0) {
            int i10 = i6 - 1;
            i8 = (i7 + sDaysInMon[(i10 + 12) % 12]) - i3;
            if (i5 % 4 == 0 && i10 == 2) {
                i8++;
            }
            i4 = -1;
        } else {
            i4 = 0;
        }
        int i11 = i4 + (i6 - i2);
        if (i11 < 0) {
            i11 += 12;
        } else {
            i9 = 0;
        }
        return new int[]{i9 + (i5 - i), i11, i8};
    }

    public static int[] getCurrentYearMonthDay(long j) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(j);
        return new int[]{calendar.get(1), calendar.get(2) + 1, calendar.get(5)};
    }
}
