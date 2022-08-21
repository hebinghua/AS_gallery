package com.xiaomi.micloudsdk.stat;

import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;
import java.net.SocketTimeoutException;
import org.apache.http.conn.ConnectTimeoutException;

/* loaded from: classes3.dex */
public class NetFailedStatParam implements Parcelable {
    public static final Parcelable.Creator<NetFailedStatParam> CREATOR = new Parcelable.Creator<NetFailedStatParam>() { // from class: com.xiaomi.micloudsdk.stat.NetFailedStatParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public NetFailedStatParam mo1873createFromParcel(Parcel parcel) {
            return new NetFailedStatParam(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public NetFailedStatParam[] mo1874newArray(int i) {
            return new NetFailedStatParam[i];
        }
    };
    public final String exceptionName;
    public final long requestStartTime;
    public final int resultType;
    public final int retryCount;
    public final long timeCost;
    public final String url;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public NetFailedStatParam(String str, long j, long j2, Throwable th, int i) {
        this.url = str;
        this.requestStartTime = j;
        this.timeCost = j2;
        this.exceptionName = th.getClass().getSimpleName();
        this.resultType = getResultType(th);
        this.retryCount = i;
    }

    public NetFailedStatParam(Parcel parcel) {
        this.url = parcel.readString();
        this.requestStartTime = parcel.readLong();
        this.timeCost = parcel.readLong();
        this.exceptionName = parcel.readString();
        this.resultType = parcel.readInt();
        this.retryCount = parcel.readInt();
    }

    public final int getResultType(Throwable th) {
        return ((th instanceof ConnectTimeoutException) || (th instanceof SocketTimeoutException)) ? 2 : 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeLong(this.requestStartTime);
        parcel.writeLong(this.timeCost);
        parcel.writeString(this.exceptionName);
        parcel.writeInt(this.resultType);
        parcel.writeInt(this.retryCount);
    }

    public String toString() {
        return "NetFailedStatParam{url='" + this.url + CoreConstants.SINGLE_QUOTE_CHAR + ", requestStartTime=" + this.requestStartTime + ", timeCost=" + this.timeCost + ", exceptionName='" + this.exceptionName + CoreConstants.SINGLE_QUOTE_CHAR + ", resultType=" + this.resultType + ", retryCount=" + this.retryCount + '}';
    }
}
