package com.miui.gallery.storage.exceptions;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.ExpectedException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/* loaded from: classes2.dex */
public class StoragePermissionMissingException extends ExpectedException implements Parcelable {
    public static final Parcelable.Creator<StoragePermissionMissingException> CREATOR = new Parcelable.Creator<StoragePermissionMissingException>() { // from class: com.miui.gallery.storage.exceptions.StoragePermissionMissingException.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public StoragePermissionMissingException mo1407createFromParcel(Parcel parcel) {
            return new StoragePermissionMissingException(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public StoragePermissionMissingException[] mo1408newArray(int i) {
            return new StoragePermissionMissingException[i];
        }
    };
    private final List<IStoragePermissionStrategy.PermissionResult> mPermissionResultList;

    /* renamed from: $r8$lambda$AE6SkdJGdd-_sevG5cHh46mEdr0 */
    public static /* synthetic */ List m1406$r8$lambda$AE6SkdJGdd_sevG5cHh46mEdr0(Parcel parcel) {
        return lambda$new$0(parcel);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public StoragePermissionMissingException(final Parcel parcel) {
        this((List) new Supplier() { // from class: com.miui.gallery.storage.exceptions.StoragePermissionMissingException$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return StoragePermissionMissingException.m1406$r8$lambda$AE6SkdJGdd_sevG5cHh46mEdr0(parcel);
            }
        }.get());
    }

    public static /* synthetic */ List lambda$new$0(Parcel parcel) {
        LinkedList linkedList = new LinkedList();
        parcel.readTypedList(linkedList, IStoragePermissionStrategy.PermissionResult.CREATOR);
        return linkedList;
    }

    public StoragePermissionMissingException(List<IStoragePermissionStrategy.PermissionResult> list) {
        Objects.requireNonNull(list);
        this.mPermissionResultList = list;
    }

    public List<IStoragePermissionStrategy.PermissionResult> getPermissionResultList() {
        return this.mPermissionResultList;
    }

    public void offer(FragmentActivity fragmentActivity) {
        for (IStoragePermissionStrategy.PermissionResult permissionResult : getPermissionResultList()) {
            if (!permissionResult.granted && permissionResult.applicable) {
                StorageSolutionProvider.get().requestPermission(fragmentActivity, permissionResult.path, permissionResult.type);
            }
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.mPermissionResultList);
    }
}
