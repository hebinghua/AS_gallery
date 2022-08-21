package com.miui.gallery.editor.photo.core.imports.remover2;

import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.utils.MemoryFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes2.dex */
public class Remover2NNFData implements Parcelable {
    public static final Parcelable.Creator<Remover2NNFData> CREATOR = new Parcelable.Creator<Remover2NNFData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2NNFData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Remover2NNFData mo858createFromParcel(Parcel parcel) {
            return new Remover2NNFData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Remover2NNFData[] mo859newArray(int i) {
            return new Remover2NNFData[i];
        }
    };
    public int count;
    public ParcelFileDescriptor fileDescriptor;
    public int height;
    public byte[] image;
    public int index;
    public int length;
    public MemoryFile memoryFile;
    public int rect_height;
    public int rect_width;
    public int type;
    public int width;
    public int x;
    public int y;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Remover2NNFData() {
    }

    public Remover2NNFData(Parcel parcel) {
        this.height = parcel.readInt();
        this.width = parcel.readInt();
        this.index = parcel.readInt();
        this.x = parcel.readInt();
        this.y = parcel.readInt();
        this.rect_height = parcel.readInt();
        this.rect_width = parcel.readInt();
        this.type = parcel.readInt();
        this.fileDescriptor = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.length = parcel.readInt();
        this.count = parcel.readInt();
        getDataFromParcel();
    }

    public void saveDataForParcel() {
        int length = this.image.length;
        this.count = length;
        this.length = length;
        MemoryFile createMemoryFile = MemoryFileUtils.createMemoryFile("remove", length);
        this.memoryFile = createMemoryFile;
        try {
            byte[] bArr = this.image;
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
                    this.image = bArr;
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
        parcel.writeInt(this.x);
        parcel.writeInt(this.y);
        parcel.writeInt(this.rect_height);
        parcel.writeInt(this.rect_width);
        parcel.writeInt(this.type);
        parcel.writeParcelable(this.fileDescriptor, i);
        parcel.writeInt(this.length);
        parcel.writeInt(this.count);
    }
}
