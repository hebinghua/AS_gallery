package com.miui.mishare;

import android.content.ClipData;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes3.dex */
public class MiShareTask implements Parcelable {
    public static final Parcelable.Creator<MiShareTask> CREATOR = new Parcelable.Creator<MiShareTask>() { // from class: com.miui.mishare.MiShareTask.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MiShareTask mo1839createFromParcel(Parcel parcel) {
            return new MiShareTask(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MiShareTask[] mo1840newArray(int i) {
            return new MiShareTask[i];
        }
    };
    public ClipData clipData;
    public int count;
    public RemoteDevice device;
    public int deviceX;
    public int deviceY;
    public String mimeType;
    public boolean send;
    public String taskId;
    public int tbHeight;
    public int tbWidth;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MiShareTask() {
    }

    public MiShareTask(Parcel parcel) {
        this.send = parcel.readByte() != 0;
        this.taskId = parcel.readString();
        this.count = parcel.readInt();
        this.device = (RemoteDevice) parcel.readParcelable(RemoteDevice.class.getClassLoader());
        this.deviceX = parcel.readInt();
        this.deviceY = parcel.readInt();
        this.clipData = (ClipData) parcel.readParcelable(ClipData.class.getClassLoader());
        this.mimeType = parcel.readString();
        this.tbWidth = parcel.readInt();
        this.tbHeight = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.send ? (byte) 1 : (byte) 0);
        parcel.writeString(this.taskId);
        parcel.writeInt(this.count);
        parcel.writeParcelable(this.device, i);
        parcel.writeInt(this.deviceX);
        parcel.writeInt(this.deviceY);
        parcel.writeParcelable(this.clipData, i);
        parcel.writeString(this.mimeType);
        parcel.writeInt(this.tbWidth);
        parcel.writeInt(this.tbHeight);
    }

    public int hashCode() {
        return Objects.hashCode(this.taskId);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MiShareTask) {
            return Objects.equals(this.taskId, ((MiShareTask) obj).taskId);
        }
        return super.equals(obj);
    }
}
