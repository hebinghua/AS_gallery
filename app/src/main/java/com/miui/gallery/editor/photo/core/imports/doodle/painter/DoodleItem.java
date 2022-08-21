package com.miui.gallery.editor.photo.core.imports.doodle.painter;

import android.content.res.Resources;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.path.DoodlePathNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.path.ScreenDoodlePathNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleCircularNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleRectangleNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleArrowNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleLineNode;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public enum DoodleItem {
    PATH(R.drawable.doodle_path_icon_n, R.drawable.doodle_path_icon_n, R.string.photo_editor_talkback_doodle_path, 1),
    LINE(R.drawable.doodle_line_icon_n, R.drawable.doodle_line_icon_n, R.string.photo_editor_talkback_doodle_line, 1),
    RECTANGLE(R.drawable.doodle_rectangle_icon_n, R.drawable.doodle_rectangle_icon_n, R.string.photo_editor_talkback_doodle_rectangle, 1),
    CIRCULAR(R.drawable.doodle_circular_icon_n, R.drawable.doodle_circular_icon_n, R.string.photo_editor_talkback_doodle_circular, 1),
    ARROW(R.drawable.doodle_arrow_icon_n, R.drawable.doodle_arrow_icon_n, R.string.photo_editor_talkback_doodle_arrow, 1),
    SCREEN_PATH(R.drawable.doodle_path_icon_n, R.drawable.doodle_path_icon_n, R.string.photo_editor_talkback_doodle_path, 0, false),
    SCREEN_LINE(R.drawable.screen_shape_line_normal, R.drawable.screen_shape_line_select, R.string.photo_editor_talkback_doodle_line, 0),
    SCREEN_RECTANGLE(R.drawable.screen_shape_rectangle_normal, R.drawable.screen_shape_rectangle_select, R.string.photo_editor_talkback_doodle_rectangle, 0),
    SCREEN_CIRCULAR(R.drawable.screen_shape_circular_normal, R.drawable.screen_shape_circular_select, R.string.photo_editor_talkback_doodle_circular, 0),
    SCREEN_ARROW(R.drawable.screen_shape_arrow_normal, R.drawable.screen_shape_arrow_select, R.string.photo_editor_talkback_doodle_arrow, 0);
    
    public final boolean isShow;
    public final int normal;
    public final int selected;
    public final int talkback;
    public final int type;

    DoodleItem(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, true);
    }

    DoodleItem(int i, int i2, int i3, int i4, boolean z) {
        this.normal = i;
        this.selected = i2;
        this.talkback = i3;
        this.type = i4;
        this.isShow = z;
    }

    public boolean isScreenData() {
        return this.type == 0;
    }

    public static List<DoodleItem> getShapes() {
        DoodleItem[] values;
        ArrayList arrayList = new ArrayList();
        for (DoodleItem doodleItem : values()) {
            if (doodleItem.isScreenData() && doodleItem.isShow) {
                arrayList.add(doodleItem);
            }
        }
        return arrayList;
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem;

        static {
            int[] iArr = new int[DoodleItem.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem = iArr;
            try {
                iArr[DoodleItem.SCREEN_PATH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.PATH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.LINE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.SCREEN_LINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.RECTANGLE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.SCREEN_RECTANGLE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.CIRCULAR.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.SCREEN_CIRCULAR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.ARROW.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[DoodleItem.SCREEN_ARROW.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public DoodleNode getDoodleDrawable(Resources resources) {
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$painter$DoodleItem[ordinal()]) {
            case 1:
                return new ScreenDoodlePathNode(resources);
            case 2:
                return new DoodlePathNode(resources);
            case 3:
            case 4:
                return new DoodleLineNode(resources);
            case 5:
            case 6:
                return new DoodleRectangleNode(resources);
            case 7:
            case 8:
                return new DoodleCircularNode(resources);
            case 9:
            case 10:
                return new DoodleArrowNode(resources);
            default:
                return null;
        }
    }
}
