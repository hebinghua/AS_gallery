package com.miui.gallery.editor.photo.core.imports.remover;

import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.utils.MemoryFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes2.dex */
public class RemoverNNFData implements Parcelable {
    public static final Parcelable.Creator<RemoverNNFData> CREATOR = new Parcelable.Creator<RemoverNNFData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverNNFData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RemoverNNFData mo847createFromParcel(Parcel parcel) {
            return new RemoverNNFData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RemoverNNFData[] mo848newArray(int i) {
            return new RemoverNNFData[i];
        }
    };
    public int count;
    public ParcelFileDescriptor fileDescriptor;
    public int height;
    public int index;
    public int length;
    public MemoryFile memoryFile;
    public byte[] nnf;
    public int width;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public RemoverNNFData() {
    }

    public RemoverNNFData(Parcel parcel) {
        this.height = parcel.readInt();
        this.width = parcel.readInt();
        this.index = parcel.readInt();
        this.fileDescriptor = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.length = parcel.readInt();
        this.count = parcel.readInt();
        getDataFromParcel();
    }

    public void saveDataForParcel() {
        int length = this.nnf.length;
        this.count = length;
        this.length = length;
        MemoryFile createMemoryFile = MemoryFileUtils.createMemoryFile("remove", length);
        this.memoryFile = createMemoryFile;
        try {
            byte[] bArr = this.nnf;
            createMemoryFile.writeBytes(bArr, 0, 0, bArr.length);
            this.memoryFile.allowPurging(false);
            this.fileDescriptor = MemoryFileUtils.getParcelFileDescriptor(this.memoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromParcel() {
        FileInputStream inputStream = MemoryFileUtils.getInputStream(this.fileDescriptor);
        try {
            try {
                try {
                    int i = this.count;
                    byte[] bArr = new byte[i];
                    this.nnf = bArr;
                    inputStream.read(bArr, 0, i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            BaseMiscUtil.closeSilently(inputStream);
        }
    }

    public void releaseMemoryFile() {
        MemoryFile memoryFile = this.memoryFile;
        if (memoryFile != null) {
            memoryFile.close();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        saveDataForParcel();
        parcel.writeInt(this.height);
        parcel.writeInt(this.width);
        parcel.writeInt(this.index);
        parcel.writeParcelable(this.fileDescriptor, i);
        parcel.writeInt(this.length);
        parcel.writeInt(this.count);
    }
}
