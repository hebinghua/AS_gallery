package com.miui.gallery.ui.photoPage;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Locale;

/* loaded from: classes2.dex */
public class EnterTypeUtils {

    /* loaded from: classes2.dex */
    public enum EnterType implements Parcelable {
        FROM_NO_CARE("FROM_NO_CARE", -1, -1),
        FROM_COMMON_INTERNAL("FROM_COMMON_INTERNAL", 0, -1),
        FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM("FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM", 0, -1),
        FROM_BACKUP("FROM_BACKUP", 0, -1),
        FROM_RECOMMEND_FACE_PAGE("FROM_RECOMMEND_FACE_PAGE", 0, -1),
        FROM_TRASH("FROM_TRASH", 0, -1),
        FROM_COMMON_EXTERNAL("FROM_COMMON_EXTERNAL", 1, -2),
        FROM_CAMERA("FROM_CAMERA", 1, 0),
        FROM_CUSTOM_WIDGET("FROM_CUSTOM_WIDGET", 1, 5),
        FROM_FILE_MANAGER("FROM_FILE_MANAGER", 1, 1),
        FROM_MESSAGE("FROM_MESSAGE", 1, 2),
        FROM_SCREEN_RECORDER("FROM_SCREEN_RECORDER", 1, 3),
        FROM_NOTE("FROM_NOTE", 1, 4);
        
        public static final Parcelable.Creator<EnterType> CREATOR = new Parcelable.Creator<EnterType>() { // from class: com.miui.gallery.ui.photoPage.EnterTypeUtils.EnterType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public EnterType mo1604createFromParcel(Parcel parcel) {
                return getEnterType(parcel);
            }

            public final EnterType getEnterType(Parcel parcel) {
                if (parcel == null) {
                    return EnterType.FROM_NO_CARE;
                }
                String readString = parcel.readString();
                if (readString == null || TextUtils.isEmpty(readString)) {
                    return EnterType.FROM_NO_CARE;
                }
                String upperCase = readString.toUpperCase(Locale.ENGLISH);
                upperCase.hashCode();
                char c = 65535;
                switch (upperCase.hashCode()) {
                    case -1922309319:
                        if (upperCase.equals("FROM_RECOMMEND_FACE_PAGE")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1398305945:
                        if (upperCase.equals("FROM_NOTE")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1218840494:
                        if (upperCase.equals("FROM_MESSAGE")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -725216260:
                        if (upperCase.equals("FROM_COMMON_INTERNAL")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -392198589:
                        if (upperCase.equals("FROM_TRASH")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 195773431:
                        if (upperCase.equals("FROM_BACKUP")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 224694618:
                        if (upperCase.equals("FROM_CAMERA")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 601931477:
                        if (upperCase.equals("FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 612835484:
                        if (upperCase.equals("FROM_SCREEN_RECORDER")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 1178579210:
                        if (upperCase.equals("FROM_COMMON_EXTERNAL")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case 1902416447:
                        if (upperCase.equals("FROM_FILE_MANAGER")) {
                            c = '\n';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        return EnterType.FROM_RECOMMEND_FACE_PAGE;
                    case 1:
                        return EnterType.FROM_NOTE;
                    case 2:
                        return EnterType.FROM_MESSAGE;
                    case 3:
                        return EnterType.FROM_COMMON_INTERNAL;
                    case 4:
                        return EnterType.FROM_TRASH;
                    case 5:
                        return EnterType.FROM_BACKUP;
                    case 6:
                        return EnterType.FROM_CAMERA;
                    case 7:
                        return EnterType.FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM;
                    case '\b':
                        return EnterType.FROM_SCREEN_RECORDER;
                    case '\t':
                        return EnterType.FROM_COMMON_EXTERNAL;
                    case '\n':
                        return EnterType.FROM_FILE_MANAGER;
                    default:
                        return EnterType.FROM_NO_CARE;
                }
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public EnterType[] mo1605newArray(int i) {
                return new EnterType[i];
            }
        };
        private final int mLauncherType;
        private final String mName;
        private final int mReligionType;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        EnterType(String str, int i, int i2) {
            this.mReligionType = i;
            this.mName = str;
            this.mLauncherType = i2;
        }

        public boolean isFromInternal() {
            return this.mReligionType == 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mName);
        }
    }

    public static EnterType getEnterFrom(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        return (EnterType) bundle.getParcelable("photo_page_enter_type");
    }

    public static boolean isFromCamera(Bundle bundle) {
        return getEnterFrom(bundle) == EnterType.FROM_CAMERA;
    }

    public static boolean isFromCamera(EnterType enterType) {
        return enterType == EnterType.FROM_CAMERA;
    }

    public static boolean isFromCustomWidget(EnterType enterType) {
        return enterType == EnterType.FROM_CUSTOM_WIDGET;
    }

    public static boolean isUsingCameraAnim(EnterType enterType) {
        return enterType == EnterType.FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM;
    }

    public static boolean isFromRecommendFacePage(EnterType enterType) {
        return enterType == EnterType.FROM_RECOMMEND_FACE_PAGE;
    }

    public static boolean isFromTrash(EnterType enterType) {
        return enterType == EnterType.FROM_TRASH;
    }

    public static boolean isFromFileManager(EnterType enterType) {
        return enterType == EnterType.FROM_FILE_MANAGER;
    }

    public static boolean isFromScreenRecorder(EnterType enterType) {
        return enterType == EnterType.FROM_SCREEN_RECORDER;
    }

    public static boolean isFromInternal(EnterType enterType) {
        return enterType != null && enterType.isFromInternal();
    }

    public static boolean isFromNote(EnterType enterType) {
        return enterType == EnterType.FROM_NOTE;
    }
}
