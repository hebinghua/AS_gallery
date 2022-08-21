package com.miui.gallery.magic.matting.bean;

import android.app.Activity;
import android.text.TextUtils;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.matting.MattingActivity;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MattingItem {
    public BackgroundItem background;
    public int[] mOperator;
    public int mPersonIndex;
    public List<PortraitNode> mPortraitNodeList = new ArrayList(20);
    public String segmentId;

    public static void clearCatch(MattingActivity mattingActivity) {
        File[] listFiles = new File(mattingActivity.getFilesDir().getPath()).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].toString().endsWith(".segment_cache") && listFiles[i].delete()) {
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("MattingItem clearCatch： " + listFiles[i]);
            }
        }
    }

    public int[] getOperator() {
        return this.mOperator;
    }

    public void setOperator(int... iArr) {
        this.mOperator = iArr;
    }

    public MattingItem(Activity activity, MattingInvoker.SegmentResult segmentResult, String str, List<PortraitNode> list, BackgroundItem backgroundItem, int i, int[] iArr) {
        this.mPersonIndex = i;
        this.mOperator = iArr;
        for (PortraitNode portraitNode : list) {
            this.mPortraitNodeList.add(portraitNode.cloneNode());
        }
        this.background = backgroundItem;
        for (int i2 : iArr) {
            if (i2 == 2 || i2 == 4 || i2 == 5) {
                checkSegmentIdFile(activity, segmentResult, str, i2);
            } else {
                this.segmentId = str;
            }
        }
    }

    public final void checkSegmentIdFile(Activity activity, MattingInvoker.SegmentResult segmentResult, String str, int i) {
        if (TextUtils.isEmpty(str) || i == 2 || i == 5) {
            saveSegmentIdToFile(segmentResult, activity);
        }
    }

    public MattingItem(String str, List<PortraitNode> list, BackgroundItem backgroundItem, int[] iArr) {
        this.mOperator = iArr;
        for (PortraitNode portraitNode : list) {
            this.mPortraitNodeList.add(portraitNode.cloneNode());
        }
        this.background = backgroundItem;
        this.segmentId = str;
    }

    public String getSegmentId() {
        return this.segmentId;
    }

    public BackgroundItem getBackgroundItem() {
        return this.background;
    }

    public List<PortraitNode> getPortraitNodeList() {
        return this.mPortraitNodeList;
    }

    public void saveSegmentIdToFile(MattingInvoker.SegmentResult segmentResult, Activity activity) {
        this.segmentId = "segment_cache_" + System.currentTimeMillis() + ".segment_cache";
        if (segmentResult != null) {
            MagicLog.INSTANCE.startLog("matting_segmentFile", "保存缓存文件");
            try {
                segmentResult.saveToFile(activity, this.segmentId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("segmentResult.saveToFile:  " + this.segmentId);
            MagicLog.INSTANCE.endLog("matting_segmentFile", "保存缓存文件");
        }
    }

    public MattingItem cloneItem() {
        MattingItem mattingItem = new MattingItem(this.segmentId, getPortraitNodeList(), new BackgroundItem(getBackgroundItem().getBackgroundIndex(), getBackgroundItem().getBackground(), getBackgroundItem().getOriginUri()), getOperator());
        mattingItem.setmPersonIndex(this.mPersonIndex);
        return mattingItem;
    }

    public boolean isChangeSegment(MattingItem mattingItem) {
        if (mattingItem == null) {
            return true;
        }
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("matting_segmentFile cur: " + this.segmentId);
        MagicLog magicLog2 = MagicLog.INSTANCE;
        magicLog2.showLog("matting_segmentFile pre: " + mattingItem.segmentId);
        return !this.segmentId.equals(mattingItem.segmentId);
    }

    public int getmPersonIndex() {
        return this.mPersonIndex;
    }

    public void setmPersonIndex(int i) {
        this.mPersonIndex = i;
    }

    public PortraitNode getCurrentNode() {
        List<PortraitNode> list = this.mPortraitNodeList;
        if (list == null) {
            return null;
        }
        for (PortraitNode portraitNode : list) {
            if (portraitNode.getPersonIndex() == this.mPersonIndex) {
                return portraitNode;
            }
        }
        return null;
    }
}
