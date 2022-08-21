package com.miui.gallery.magic.matting.preview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.widget.portrait.PortraitEditView;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import java.util.List;

/* loaded from: classes2.dex */
public interface IPreview$VP {
    void addPersonForegroundToView(MattingInvoker.SegmentResult segmentResult, Bitmap bitmap);

    void addPortraitNode(Bitmap bitmap, long j, Rect rect, int i);

    void autoFuse(List<PortraitNode> list);

    void backgroundPaintingSegment(boolean z, int i);

    void changeSticker(PortraitNode portraitNode, Bitmap bitmap);

    void faceDetect(MattingInvoker mattingInvoker, MattingInvoker.SegmentResult segmentResult);

    void firstAddNode();

    Bitmap getBackgroundBit();

    PortraitNode getCurrentPerson();

    Bitmap getOriginBitmap();

    Bitmap getPersonBitmapByNode(PortraitNode portraitNode);

    MattingInvoker.SegmentResult getSegmentResult();

    void loadPreview(Bitmap bitmap, boolean z);

    int mirrorPerson(int i);

    void onBackPressed();

    void redoNotifyCurrentBg(Bitmap bitmap);

    void save(PortraitEditView.Portrait portrait);

    boolean selectPhotos(Uri uri, Bitmap bitmap);

    void setBackground(Bitmap bitmap);

    void setStrokeLine(StrokeIconItem strokeIconItem);
}
