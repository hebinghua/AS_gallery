package com.miui.gallery.model;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.util.BaseFileMimeUtil;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ImageLoadParams.kt */
/* loaded from: classes2.dex */
public final class ImageLoadParams implements Parcelable {
    public static final CREATOR CREATOR = new CREATOR(null);
    public long createTime;
    public long fileLength;
    public int imageHeight;
    public int imageWidth;
    public boolean isFromCamera;
    public boolean isFromFace;
    public long key;
    public String location;
    public String mimeType;
    public String path;
    public int pos;
    public RectF regionRectF;
    public RequestOptions requestOptions;
    public byte[] secretKey;
    public Size targetSize;

    public /* synthetic */ ImageLoadParams(long j, String str, Size size, RectF rectF, int i, String str2, byte[] bArr, boolean z, boolean z2, long j2, long j3, String str3, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, str, size, rectF, i, str2, bArr, z, z2, j2, j3, str3, i2, i3);
    }

    public /* synthetic */ ImageLoadParams(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ImageLoadParams(long j, String str, Size size, RectF rectF, int i, String str2, byte[] bArr, boolean z, boolean z2, long j2, long j3, String str3, int i2, int i3) {
        this.key = j;
        this.path = str;
        this.targetSize = size;
        this.regionRectF = rectF;
        this.pos = i;
        this.mimeType = str2;
        this.secretKey = bArr;
        this.isFromFace = z;
        this.isFromCamera = z2;
        this.fileLength = j2;
        this.createTime = j3;
        this.location = str3;
        this.imageWidth = i2;
        this.imageHeight = i3;
    }

    public final long getKey() {
        return this.key;
    }

    public final String getPath() {
        return this.path;
    }

    public final Size getTargetSize() {
        return this.targetSize;
    }

    public final RectF getRegionRectF() {
        return this.regionRectF;
    }

    public final int getPos() {
        return this.pos;
    }

    public final String getMimeType() {
        return this.mimeType;
    }

    public final byte[] getSecretKey() {
        return this.secretKey;
    }

    public final boolean isFromFace() {
        return this.isFromFace;
    }

    public final boolean isFromCamera() {
        return this.isFromCamera;
    }

    public final long getFileLength() {
        return this.fileLength;
    }

    public final long getCreateTime() {
        return this.createTime;
    }

    public final String getLocation() {
        return this.location;
    }

    public final int getImageWidth() {
        return this.imageWidth;
    }

    public final int getImageHeight() {
        return this.imageHeight;
    }

    public final RequestOptions getRequestOptions() {
        return this.requestOptions;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public ImageLoadParams(android.os.Parcel r21) {
        /*
            r20 = this;
            r0 = r21
            long r2 = r21.readLong()
            java.lang.String r4 = r21.readString()
            java.lang.String r1 = r21.readString()
            android.util.Size r5 = android.util.Size.parseSize(r1)
            java.lang.Class<android.graphics.RectF> r1 = android.graphics.RectF.class
            java.lang.ClassLoader r1 = r1.getClassLoader()
            android.os.Parcelable r1 = r0.readParcelable(r1)
            r6 = r1
            android.graphics.RectF r6 = (android.graphics.RectF) r6
            int r7 = r21.readInt()
            java.lang.String r8 = r21.readString()
            int r1 = r21.readInt()
            if (r1 <= 0) goto L33
            byte[] r1 = new byte[r1]
            r0.readByteArray(r1)
            goto L34
        L33:
            r1 = 0
        L34:
            r9 = r1
            int r1 = r21.readInt()
            r10 = 0
            r11 = 1
            if (r1 != r11) goto L40
            r19 = r11
            goto L42
        L40:
            r19 = r10
        L42:
            int r1 = r21.readInt()
            if (r1 != r11) goto L49
            goto L4a
        L49:
            r11 = r10
        L4a:
            long r12 = r21.readLong()
            long r14 = r21.readLong()
            java.lang.String r16 = r21.readString()
            int r17 = r21.readInt()
            int r18 = r21.readInt()
            r1 = r20
            r10 = r19
            r1.<init>(r2, r4, r5, r6, r7, r8, r9, r10, r11, r12, r14, r16, r17, r18)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.ImageLoadParams.<init>(android.os.Parcel):void");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        Intrinsics.checkNotNullParameter(dest, "dest");
        dest.writeLong(this.key);
        dest.writeString(this.path);
        dest.writeString(String.valueOf(this.targetSize));
        dest.writeParcelable(this.regionRectF, i);
        dest.writeInt(this.pos);
        dest.writeString(this.mimeType);
        byte[] bArr = this.secretKey;
        dest.writeInt(bArr == null ? 0 : bArr.length);
        byte[] bArr2 = this.secretKey;
        if (bArr2 != null) {
            dest.writeByteArray(bArr2);
        }
        dest.writeInt(this.isFromFace ? 1 : 0);
        dest.writeInt(this.isFromCamera ? 1 : 0);
        dest.writeLong(this.fileLength);
        dest.writeLong(this.createTime);
        dest.writeString(this.location);
        dest.writeInt(this.imageWidth);
        dest.writeInt(this.imageHeight);
    }

    public final void updatePosition(int i) {
        this.pos = i;
    }

    public final boolean isVideo() {
        return BaseFileMimeUtil.isVideoFromMimeType(this.mimeType);
    }

    public final boolean isGif() {
        return BaseFileMimeUtil.isGifFromMimeType(this.mimeType);
    }

    public final boolean match(BaseDataItem baseDataItem, int i) {
        if (baseDataItem == null) {
            if (i == this.pos) {
                return true;
            }
        } else if (this.key == baseDataItem.getKey()) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ImageLoadParams) && this.key == ((ImageLoadParams) obj).key;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.key));
    }

    public String toString() {
        return "ImageLoadParams{key=" + this.key + ", path=" + ((Object) this.path) + ", targetSize=" + this.targetSize + ", regionRectF=" + this.regionRectF + ", pos=" + this.pos + ", mimeType=" + ((Object) this.mimeType) + ", secretKey=" + this.secretKey + ", isFromFace=" + this.isFromFace + ", isFromCamera=" + this.isFromCamera + ", fileLength=" + this.fileLength + ", createTime=" + this.createTime + ", location=" + ((Object) this.location) + ", imageWidth=" + this.imageWidth + ", imageHeight=" + this.imageHeight + ", }";
    }

    /* compiled from: ImageLoadParams.kt */
    /* loaded from: classes2.dex */
    public static final class Builder {
        public long createTime;
        public String filePath;
        public boolean fromCamera;
        public boolean fromFace;
        public int initPosition;
        public long key;
        public String location;
        public String mimeType;
        public RectF regionRect;
        public RequestOptions requestOptions;
        public byte[] secretKey;
        public Size targetSize;
        public long fileLength = -1;
        public int imageWidth = -1;
        public int imageHeight = -1;

        /* renamed from: setKey  reason: collision with other method in class */
        public final /* synthetic */ void m1104setKey(long j) {
            this.key = j;
        }

        /* renamed from: setFilePath  reason: collision with other method in class */
        public final /* synthetic */ void m1100setFilePath(String str) {
            this.filePath = str;
        }

        /* renamed from: setTargetSize  reason: collision with other method in class */
        public final /* synthetic */ void m1110setTargetSize(Size size) {
            this.targetSize = size;
        }

        /* renamed from: setRegionRect  reason: collision with other method in class */
        public final /* synthetic */ void m1107setRegionRect(RectF rectF) {
            this.regionRect = rectF;
        }

        /* renamed from: setInitPosition  reason: collision with other method in class */
        public final /* synthetic */ void m1103setInitPosition(int i) {
            this.initPosition = i;
        }

        /* renamed from: setMimeType  reason: collision with other method in class */
        public final /* synthetic */ void m1106setMimeType(String str) {
            this.mimeType = str;
        }

        /* renamed from: setSecretKey  reason: collision with other method in class */
        public final /* synthetic */ void m1109setSecretKey(byte[] bArr) {
            this.secretKey = bArr;
        }

        public final /* synthetic */ void setFromFace(boolean z) {
            this.fromFace = z;
        }

        public final /* synthetic */ void setFromCamera(boolean z) {
            this.fromCamera = z;
        }

        /* renamed from: setFileLength  reason: collision with other method in class */
        public final /* synthetic */ void m1099setFileLength(long j) {
            this.fileLength = j;
        }

        /* renamed from: setCreateTime  reason: collision with other method in class */
        public final /* synthetic */ void m1098setCreateTime(long j) {
            this.createTime = j;
        }

        /* renamed from: setLocation  reason: collision with other method in class */
        public final /* synthetic */ void m1105setLocation(String str) {
            this.location = str;
        }

        /* renamed from: setImageWidth  reason: collision with other method in class */
        public final /* synthetic */ void m1102setImageWidth(int i) {
            this.imageWidth = i;
        }

        /* renamed from: setImageHeight  reason: collision with other method in class */
        public final /* synthetic */ void m1101setImageHeight(int i) {
            this.imageHeight = i;
        }

        public final RequestOptions getRequestOptions() {
            return this.requestOptions;
        }

        /* renamed from: setRequestOptions  reason: collision with other method in class */
        public final /* synthetic */ void m1108setRequestOptions(RequestOptions requestOptions) {
            this.requestOptions = requestOptions;
        }

        public final Builder cloneFrom(ImageLoadParams params) {
            Intrinsics.checkNotNullParameter(params, "params");
            this.key = params.getKey();
            this.filePath = params.getPath();
            this.targetSize = params.getTargetSize();
            this.regionRect = params.getRegionRectF();
            this.initPosition = params.getPos();
            this.mimeType = params.getMimeType();
            this.secretKey = params.getSecretKey();
            this.fromFace = params.isFromFace();
            this.fromCamera = params.isFromCamera();
            this.fileLength = params.getFileLength();
            this.createTime = params.getCreateTime();
            this.location = params.getLocation();
            this.imageWidth = params.getImageWidth();
            this.imageHeight = params.getImageHeight();
            this.requestOptions = params.getRequestOptions();
            return this;
        }

        public final Builder setKey(long j) {
            m1104setKey(j);
            return this;
        }

        public final Builder setFilePath(String str) {
            m1100setFilePath(str);
            return this;
        }

        public final Builder setTargetSize(Size size) {
            m1110setTargetSize(size);
            return this;
        }

        public final Builder setRegionRect(RectF rectF) {
            m1107setRegionRect(rectF);
            return this;
        }

        public final Builder setInitPosition(int i) {
            m1103setInitPosition(i);
            return this;
        }

        public final Builder setMimeType(String str) {
            m1106setMimeType(str);
            return this;
        }

        public final Builder setSecretKey(byte[] bArr) {
            m1109setSecretKey(bArr);
            return this;
        }

        public final Builder fromFace(boolean z) {
            setFromFace(z);
            return this;
        }

        public final Builder fromCamera(boolean z) {
            setFromCamera(z);
            return this;
        }

        public final Builder setFileLength(long j) {
            m1099setFileLength(j);
            return this;
        }

        public final Builder setCreateTime(long j) {
            m1098setCreateTime(j);
            return this;
        }

        public final Builder setLocation(String str) {
            m1105setLocation(str);
            return this;
        }

        public final Builder setImageWidth(int i) {
            m1102setImageWidth(i);
            return this;
        }

        public final Builder setImageHeight(int i) {
            m1101setImageHeight(i);
            return this;
        }

        public final Builder setRequestOptions(RequestOptions requestOptions) {
            m1108setRequestOptions(requestOptions);
            return this;
        }

        public final ImageLoadParams build() {
            if (this.targetSize == null) {
                this.targetSize = Config$ThumbConfig.get().sMicroTargetSize;
            }
            ImageLoadParams imageLoadParams = new ImageLoadParams(this.key, this.filePath, this.targetSize, this.regionRect, this.initPosition, this.mimeType, this.secretKey, this.fromFace, this.fromCamera, this.fileLength, this.createTime, this.location, this.imageWidth, this.imageHeight, null);
            imageLoadParams.requestOptions = getRequestOptions();
            return imageLoadParams;
        }
    }

    /* compiled from: ImageLoadParams.kt */
    /* loaded from: classes2.dex */
    public static final class CREATOR implements Parcelable.Creator<ImageLoadParams> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public CREATOR() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ImageLoadParams mo1111createFromParcel(Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new ImageLoadParams(parcel, null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ImageLoadParams[] mo1112newArray(int i) {
            return new ImageLoadParams[i];
        }
    }
}
