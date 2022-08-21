package com.miui.gallery.ui.photoPage.menufilter;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.StorageItem;
import com.miui.gallery.model.TrashDataItem;
import com.miui.gallery.model.UriItem;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.ui.photoPage.menufilter.config.CommonConfigFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.external.CameraFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.external.CommonExternalFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.external.FileManagerFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.external.MessageFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.external.ScreenRecordFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.internal.BackupFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.internal.CommonInternalFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.internal.RecommendFaceFilter;
import com.miui.gallery.ui.photoPage.menufilter.enter.internal.TrashUpFilter;
import com.miui.gallery.ui.photoPage.menufilter.extra.ExtraFilter;
import com.miui.gallery.ui.photoPage.menufilter.itemType.CloudFilter;
import com.miui.gallery.ui.photoPage.menufilter.itemType.MediaFilter;
import com.miui.gallery.ui.photoPage.menufilter.itemType.StorageFilter;
import com.miui.gallery.ui.photoPage.menufilter.itemType.TrashFilter;
import com.miui.gallery.ui.photoPage.menufilter.itemType.UriFilter;

/* loaded from: classes2.dex */
public class FilterFactory {

    /* renamed from: com.miui.gallery.ui.photoPage.menufilter.FilterFactory$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType;

        static {
            int[] iArr = new int[EnterTypeUtils.EnterType.values().length];
            $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType = iArr;
            try {
                iArr[EnterTypeUtils.EnterType.FROM_BACKUP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_TRASH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_CAMERA.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_RECOMMEND_FACE_PAGE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_FILE_MANAGER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_MESSAGE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_SCREEN_RECORDER.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_COMMON_EXTERNAL.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_CUSTOM_WIDGET.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_COMMON_INTERNAL.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[EnterTypeUtils.EnterType.FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public static MenuFilterController.IEnterFilter getEnterFilter(EnterTypeUtils.EnterType enterType) {
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$ui$photoPage$EnterTypeUtils$EnterType[enterType.ordinal()]) {
            case 1:
                return new BackupFilter();
            case 2:
                return new TrashUpFilter();
            case 3:
                return new CameraFilter();
            case 4:
                return new RecommendFaceFilter();
            case 5:
                return new FileManagerFilter();
            case 6:
                return new MessageFilter();
            case 7:
                return new ScreenRecordFilter();
            case 8:
            case 9:
                return new CommonExternalFilter();
            default:
                return new CommonInternalFilter();
        }
    }

    public static MenuFilterController.IItemTypeFilter getItemTypeFilter(BaseDataItem baseDataItem) {
        if (baseDataItem instanceof UriItem) {
            return new UriFilter();
        }
        if (baseDataItem instanceof StorageItem) {
            return new StorageFilter();
        }
        if (baseDataItem instanceof CloudItem) {
            return new CloudFilter();
        }
        if (baseDataItem instanceof TrashDataItem) {
            return new TrashFilter();
        }
        return new MediaFilter();
    }

    public static MenuFilterController.IExtraFilter getExtraFilter() {
        return new ExtraFilter();
    }

    public static MenuFilterController.IConfigFilter getConfigFilter() {
        return new CommonConfigFilter();
    }
}
