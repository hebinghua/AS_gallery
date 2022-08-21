package com.miui.gallery.storage.strategies;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.exceptions.StorageUnsupportedOperationException;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public interface IStoragePermissionStrategy {

    /* loaded from: classes2.dex */
    public enum Permission {
        QUERY,
        INSERT,
        DELETE,
        UPDATE,
        APPEND,
        QUERY_DIRECTORY,
        INSERT_DIRECTORY,
        DELETE_DIRECTORY,
        UPDATE_DIRECTORY
    }

    default PermissionResult checkPermission(String str, Permission permission) {
        throw new StorageUnsupportedOperationException();
    }

    default List<PermissionResult> checkPermission(List<String> list, Permission permission) {
        throw new StorageUnsupportedOperationException();
    }

    default void requestPermission(FragmentActivity fragmentActivity, String str, Permission... permissionArr) {
        throw new StorageUnsupportedOperationException();
    }

    default void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, Permission... permissionArr) {
        throw new StorageUnsupportedOperationException();
    }

    default void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent) {
        throw new StorageUnsupportedOperationException();
    }

    default void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri) {
        throw new StorageUnsupportedOperationException();
    }

    default void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        throw new StorageUnsupportedOperationException();
    }

    default void registerPermissionObserver(ContentObserver contentObserver) {
        throw new StorageUnsupportedOperationException();
    }

    /* loaded from: classes2.dex */
    public static class PermissionResult implements Parcelable {
        public static final Parcelable.Creator<PermissionResult> CREATOR = new Parcelable.Creator<PermissionResult>() { // from class: com.miui.gallery.storage.strategies.IStoragePermissionStrategy.PermissionResult.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public PermissionResult mo1409createFromParcel(Parcel parcel) {
                return new PermissionResult(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public PermissionResult[] mo1410newArray(int i) {
                return new PermissionResult[i];
            }
        };
        public boolean applicable;
        public boolean granted;
        public String path;
        public Permission type;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public PermissionResult(String str, Permission permission) {
            this.granted = false;
            this.applicable = false;
            this.path = str;
            this.type = permission;
        }

        public PermissionResult(Parcel parcel) {
            boolean z = false;
            this.granted = false;
            this.applicable = false;
            this.path = parcel.readString();
            this.type = Permission.values()[parcel.readInt()];
            this.granted = parcel.readByte() != 0;
            this.applicable = parcel.readByte() != 0 ? true : z;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.path);
            parcel.writeInt(this.type.ordinal());
            parcel.writeByte(this.granted ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.applicable ? (byte) 1 : (byte) 0);
        }

        public String toString() {
            return String.format("PermissionResult - path: [%s], type: [%s], granted: [%b], applicable: [%b]", this.path, this.type, Boolean.valueOf(this.granted), Boolean.valueOf(this.applicable));
        }
    }
}
