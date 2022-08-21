package com.miui.gallery.magic.matting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.matting.bean.MattingItem;
import com.miui.gallery.magic.matting.doodle.DoodleFragment;
import com.miui.gallery.magic.matting.menu.MattingMenuFragment;
import com.miui.gallery.magic.matting.preview.PreviewFragment;
import com.miui.gallery.magic.ui.ConfirmDialog;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicMainHandler;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes2.dex */
public class MattingActivity extends BaseFragmentActivity {
    public MattingInvoker invoker;
    public DoodleFragment mDoodleFragment;
    public MattingMenuFragment mMenuFragment;
    public PreviewFragment mPreviewFragment;
    public int[] mWH;
    public boolean isImageBeingEdited = false;
    public LinkedList<MattingItem> mUndoList = new LinkedList<>();
    public Stack<MattingItem> mRedoList = new Stack<>();

    public static /* synthetic */ void $r8$lambda$4NPmO3Ttgp5RjlKM7IImKLZF01g(MattingActivity mattingActivity) {
        mattingActivity.lambda$showRedoUndo$1();
    }

    public static /* synthetic */ void $r8$lambda$H_Dfru1Vrtl4zyA1hGbTWj0f81A(MattingActivity mattingActivity) {
        mattingActivity.lambda$onDestroy$2();
    }

    public static /* synthetic */ void $r8$lambda$bqeSwftft0XeNc3ikpAnHYaksqw(MattingActivity mattingActivity) {
        mattingActivity.lambda$removeLoadingDialog$0();
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        MagicSampler.getInstance().recordCategory("matting", "enter");
        Uri data = getIntent().getData();
        if (data != null && !ImageFormatUtils.isSupportImageFormat(data)) {
            ToastUtils.makeText(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
            finish();
        } else if (data != null && MagicFileUtil.checkMaxPX(this, data)) {
            ToastUtils.makeText(this, getResources().getString(R$string.magic_max_px));
            finish();
        } else {
            this.mPreviewFragment = new PreviewFragment();
            this.mMenuFragment = new MattingMenuFragment();
            this.mDoodleFragment = new DoodleFragment();
            initInvoker();
            this.mPreviewFragment.setData(null, this.invoker, null);
            addPreview(this.mPreviewFragment);
            addMenu(this.mMenuFragment);
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public void removeLoadingDialog() {
        super.removeLoadingDialog();
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.magic.matting.MattingActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MattingActivity.$r8$lambda$bqeSwftft0XeNc3ikpAnHYaksqw(MattingActivity.this);
            }
        });
    }

    public /* synthetic */ void lambda$removeLoadingDialog$0() {
        PreviewFragment previewFragment = this.mPreviewFragment;
        if (previewFragment != null) {
            previewFragment.playDownloadMediaEditorAppAnimation();
        }
    }

    public final void initInvoker() {
        if (this.invoker == null) {
            this.invoker = new MattingInvoker();
        }
        MattingItem.clearCatch(this);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public Object event(int i, Object obj) {
        if (i == 1) {
            this.mPreviewFragment.getContract().setBackground((Bitmap) obj);
            return null;
        } else if (i == 2) {
            this.mWH = (int[]) obj;
            return null;
        } else if (i == 4) {
            this.mPreviewFragment.getContract().setStrokeLine((StrokeIconItem) obj);
            return null;
        } else if (i == 6) {
            return this.mWH;
        } else {
            if (i == 7) {
                return this.mMenuFragment.getContract().getBackgroundItem();
            }
            if (i != 8) {
                return null;
            }
            this.isImageBeingEdited = true;
            return null;
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void openDoodleFragment(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, boolean z, int i) {
        if (z) {
            this.isImageBeingEdited = true;
            this.mUndoList.clear();
            showPreviewFragment(this.mDoodleFragment);
            this.mDoodleFragment.setData(bitmap, segmentResult, this.invoker, i, this.mRedoList);
            findViewById(R$id.magic_menu_container).setVisibility(8);
        }
    }

    public void closeDoodleFragment(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, int i2, Bitmap bitmap2) {
        this.mPreviewFragment.setData(bitmap, this.invoker, segmentResult);
        showPreviewFragment(this.mPreviewFragment);
        findViewById(R$id.magic_menu_container).setVisibility(0);
        if (i == -1) {
            return;
        }
        if (i == 3) {
            MattingItem peek = this.mRedoList.peek();
            this.mPreviewFragment.refreshNewMattingItem(peek);
            this.mPreviewFragment.checkoutPerson(i2);
            this.mPreviewFragment.checkIsChangeBackground(bitmap2, getBgByMattingItem(peek.getBackgroundItem().getBackground()));
        } else if (i == 5) {
            this.mPreviewFragment.refreshNewMattingItem(this.mRedoList.peek());
            this.mPreviewFragment.removeIndex(this.mRedoList.peek().getmPersonIndex());
        }
        showRedoUndo();
    }

    public void addRedoList(List<PortraitNode> list, MattingInvoker.SegmentResult segmentResult, int i, int... iArr) {
        this.mUndoList.clear();
        int size = this.mRedoList.size();
        if (this.mRedoList.size() >= 16) {
            this.mRedoList.remove(0);
        }
        String str = null;
        if (size > 0) {
            str = this.mRedoList.peek().getSegmentId();
        }
        this.mRedoList.push(new MattingItem(this, segmentResult, str, list, this.mMenuFragment.getContract().getBackgroundItem(), i, iArr));
        showRedoUndo();
    }

    public final void showRedoUndo() {
        MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.MattingActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MattingActivity.$r8$lambda$4NPmO3Ttgp5RjlKM7IImKLZF01g(MattingActivity.this);
            }
        });
        boolean z = false;
        this.mPreviewFragment.showRedo(this.mRedoList.size() > 1, this.mRedoList.size() <= 1 && this.mUndoList.size() < 1);
        PreviewFragment previewFragment = this.mPreviewFragment;
        boolean z2 = this.mUndoList.size() >= 1;
        if (this.mRedoList.size() <= 1 && this.mUndoList.size() < 1) {
            z = true;
        }
        previewFragment.showUndo(z2, z);
    }

    public /* synthetic */ void lambda$showRedoUndo$1() {
        this.mPreviewFragment.setLastAnim(this.mRedoList.size(), this.mUndoList.size());
    }

    public void redo() {
        if (this.mRedoList.size() > 1) {
            MattingItem pop = this.mRedoList.pop();
            this.mUndoList.add(pop);
            refreshRedo(pop, this.mRedoList.peek());
        } else if (this.mUndoList.size() <= 0 || this.mRedoList.size() <= 0) {
            return;
        } else {
            refreshRedo(this.mUndoList.getLast(), this.mRedoList.peek());
        }
        showRedoUndo();
    }

    public final void refreshRedo(MattingItem mattingItem, MattingItem mattingItem2) {
        int[] operator;
        for (int i : mattingItem.getOperator()) {
            if (i != 0) {
                if (i == 1) {
                    this.mMenuFragment.getContract().setSelectBackgroundIndex(mattingItem2.getBackgroundItem().getBackgroundIndex());
                    this.mPreviewFragment.changeBackGround(getBgByMattingItem(mattingItem2.getBackgroundItem().getBackground()));
                } else if (i == 2) {
                    this.mPreviewFragment.removeNodeByPersonIndex(mattingItem.getmPersonIndex());
                } else {
                    if (i != 3) {
                        if (i != 4) {
                            if (i != 5) {
                            }
                        }
                    }
                    this.mPreviewFragment.onChangeSegmentResult(mattingItem2.getSegmentId());
                    this.mPreviewFragment.refreshNewMattingItem(mattingItem2);
                }
            }
            this.mPreviewFragment.refreshNewMattingItem(mattingItem2);
        }
    }

    public Bitmap getBgByMattingItem(String str) {
        if (TextUtils.isEmpty(str) || str.equals("first_bbg")) {
            return this.mPreviewFragment.getContract().getBackgroundBit();
        }
        return this.mMenuFragment.getContract().getBackgroundBitmap(str);
    }

    public void undo() {
        if (this.mUndoList.size() > 0) {
            this.mRedoList.push(this.mUndoList.getLast());
            this.mUndoList.removeLast();
            refreshUndo(this.mRedoList.peek(), this.mRedoList.peek());
        }
    }

    public final void refreshUndo(MattingItem mattingItem, MattingItem mattingItem2) {
        int[] operator;
        for (int i : mattingItem.getOperator()) {
            if (i == 0) {
                this.mPreviewFragment.refreshNewMattingItem(mattingItem);
            } else if (i == 1) {
                this.mMenuFragment.getContract().setSelectBackgroundIndex(mattingItem.getBackgroundItem().getBackgroundIndex());
                this.mPreviewFragment.changeBackGround(getBgByMattingItem(mattingItem.getBackgroundItem().getBackground()));
            } else if (i == 2) {
                if (mattingItem.isChangeSegment(mattingItem2)) {
                    this.mPreviewFragment.onChangeSegmentResult(mattingItem.getSegmentId());
                }
                PortraitNode currentNode = mattingItem.getCurrentNode();
                currentNode.setUpdate(false);
                this.mPreviewFragment.addNodeToView(currentNode);
            } else if (i == 3 || i == 5) {
                this.mPreviewFragment.onChangeSegmentResult(mattingItem.getSegmentId());
                this.mPreviewFragment.refreshNewMattingItem(mattingItem);
            }
        }
        showRedoUndo();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.mRedoList.clear();
        this.mUndoList.clear();
        PreviewFragment previewFragment = this.mPreviewFragment;
        if (previewFragment != null) {
            previewFragment.clear();
        }
        if (this.invoker != null) {
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.MattingActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MattingActivity.$r8$lambda$H_Dfru1Vrtl4zyA1hGbTWj0f81A(MattingActivity.this);
                }
            });
        }
        super.onDestroy();
        MagicThreadHandler.removeCallbacksAndMessages();
    }

    public /* synthetic */ void lambda$onDestroy$2() {
        this.invoker.destoryModel();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.isImageBeingEdited) {
            MagicSampler.getInstance().recordCategory("matting", "cancel");
            finish();
            return;
        }
        ConfirmDialog.showConfirmDialog(getSupportFragmentManager(), getStringById(R$string.magic_edit_cancel), getStringById(R$string.magic_edit_dsc), getStringById(R$string.magic_cancel), getStringById(R$string.magic_ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.magic.matting.MattingActivity.1
            @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialogFragment) {
            }

            {
                MattingActivity.this = this;
            }

            @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialogFragment) {
                MagicSampler.getInstance().recordCategory("matting", "cancel");
                MattingActivity.this.isImageBeingEdited = false;
                MattingActivity.this.finish();
            }
        });
    }

    public String getStringById(int i) {
        return getResources().getString(i);
    }
}
