package com.miui.gallery.magic.matting.doodle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.magic.BlendConfig;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.matting.MattingActivity;
import com.miui.gallery.magic.matting.bean.MattingItem;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.widget.DoodleView;
import com.miui.gallery.magic.widget.MagicBubbleSeekBar;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import miuix.animation.listener.TransitionListener;

/* loaded from: classes2.dex */
public class DoodleFragment extends BaseFragment {
    public static int INIT_CURRENT_PROGRESS = 40;
    public AnimParams downParams;
    public FrameLayout mBanner;
    public DoodleView mDoodle;
    public View mDoodleBody;
    public ImageView mDoodleImage;
    public MattingInvoker mInvoker;
    public Bitmap mOriginBitmap;
    public View mPaint;
    public ImageView mPaintImg;
    public TextView mPaintText;
    public int mPersonIndex = 0;
    public Stack<MattingItem> mRedoList;
    public MattingActivity mRootActivity;
    public View mRubber;
    public ImageView mRubberImg;
    public TextView mRubberText;
    public MagicBubbleSeekBar mSeekBar;
    public MattingInvoker.SegmentResult mSegmentResult;
    public View magicRedo;
    public View magicUndo;
    public View rootView;
    public ImageView tvCancel;
    public ImageView tvOk;
    public AnimParams upParams;

    public static /* synthetic */ void $r8$lambda$OLs3Zz8dNSt13LYS99EOdowNnPQ(DoodleFragment doodleFragment, long j, Rect rect, Bitmap bitmap) {
        doodleFragment.lambda$onClick$1(j, rect, bitmap);
    }

    public static /* synthetic */ void $r8$lambda$c8FJbvBNbuxtMB4AxZJKvIJJ19s(DoodleFragment doodleFragment, boolean z, boolean z2) {
        doodleFragment.lambda$initData$0(z, z2);
    }

    public static /* synthetic */ void $r8$lambda$z8GI7HCwiqIluF7crlxUAAnjlZw(DoodleFragment doodleFragment, long j) {
        doodleFragment.lambda$onClick$2(j);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public BasePresenter getPresenterInstance() {
        return null;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public Object mo1066initContract() {
        return null;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        this.rootView = inflate;
        return inflate;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_matting_doodle;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        this.mRootActivity = (MattingActivity) getActivity();
        this.mSeekBar.setProgressListener(new MagicBubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.magic.matting.doodle.DoodleFragment.1
            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
            }

            {
                DoodleFragment.this = this;
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onProgressChanged(MagicBubbleSeekBar magicBubbleSeekBar, int i) {
                float side = DoodleFragment.this.getSide(i);
                DoodleFragment.this.mDoodleBody.setVisibility(0);
                ViewGroup.LayoutParams layoutParams = DoodleFragment.this.mDoodleImage.getLayoutParams();
                int i2 = (int) side;
                layoutParams.height = i2;
                layoutParams.width = i2;
                DoodleFragment.this.mDoodleImage.setLayoutParams(layoutParams);
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
                DoodleFragment.this.mDoodleBody.setVisibility(8);
                DoodleFragment.this.mDoodle.setStrokeWidth(DoodleFragment.this.getPainDiam((int) magicBubbleSeekBar.getCurrentProgress()));
            }
        });
        this.mDoodle.setImageBitmap(this.mOriginBitmap);
        this.mDoodle.setSegment(this.mSegmentResult);
        this.mDoodle.setPersonIndex(this.mPersonIndex);
        this.mDoodle.setPathChangeListener(new DoodleView.PathChangeListener() { // from class: com.miui.gallery.magic.matting.doodle.DoodleFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.magic.widget.DoodleView.PathChangeListener
            public final void change(boolean z, boolean z2) {
                DoodleFragment.$r8$lambda$c8FJbvBNbuxtMB4AxZJKvIJJ19s(DoodleFragment.this, z, z2);
            }
        });
        showReUndoBtn();
        this.mDoodle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.magic.matting.doodle.DoodleFragment.2
            {
                DoodleFragment.this = this;
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                int width = DoodleFragment.this.mDoodle.getWidth();
                int height = DoodleFragment.this.mDoodle.getHeight();
                int width2 = DoodleFragment.this.mOriginBitmap.getWidth();
                int height2 = DoodleFragment.this.mOriginBitmap.getHeight();
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("MagicLogger DoodleFragment", "addOnGlobalLayoutListener  \n Doodle.getWidth() " + width + "  mDoodle.getHeight() " + height + "  \nmOriginBitmap.getWidth() " + width2 + "  mOriginBitmap.getHeight() " + height2 + "");
                float f = ((float) width) / ((float) width2);
                float f2 = ((float) height) / ((float) height2);
                if (f <= f2) {
                    DoodleFragment.this.mDoodle.setDefaultScale(f);
                } else {
                    DoodleFragment.this.mDoodle.setDefaultScale(f2);
                }
            }
        });
    }

    public /* synthetic */ void lambda$initData$0(boolean z, boolean z2) {
        showReUndoBtn();
    }

    public final float getSide(int i) {
        float dimension = getResources().getDimension(R$dimen.magic_seek_bar_inner_min);
        return dimension + (((getResources().getDimension(R$dimen.magic_seek_bar_inner_max) - dimension) * i) / 100.0f);
    }

    public final float getPainDiam(int i) {
        return getSide(i) - (getResources().getDimension(R$dimen.magic_seek_bar_inner_border) * 2.0f);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public View findViewById(int i) {
        return this.rootView.findViewById(i);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mSeekBar = (MagicBubbleSeekBar) findViewById(R$id.magic_video_audio_seek);
        this.mBanner = (FrameLayout) findViewById(R$id.banner);
        this.mDoodle = (DoodleView) findViewById(R$id.magic_doodle);
        int i = R$id.magic_matting_paint;
        this.mPaint = findViewById(i);
        this.mPaintImg = (ImageView) findViewById(R$id.magic_matting_paint_img);
        this.mPaintText = (TextView) findViewById(R$id.magic_matting_paint_text);
        int i2 = R$id.magic_matting_rubber;
        this.mRubber = findViewById(i2);
        this.mRubberImg = (ImageView) findViewById(R$id.magic_matting_rubber_img);
        ImageView imageView = (ImageView) findViewById(R$id.magic_doodle_c_image);
        this.mDoodleImage = imageView;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) getSide(INIT_CURRENT_PROGRESS);
        layoutParams.width = (int) getSide(INIT_CURRENT_PROGRESS);
        this.mDoodleImage.setLayoutParams(layoutParams);
        this.mDoodle.setStrokeWidth(getPainDiam(INIT_CURRENT_PROGRESS));
        this.mDoodleBody = findViewById(R$id.magic_doodle_c_body);
        this.mRubberText = (TextView) findViewById(R$id.magic_matting_rubber_text);
        this.magicRedo = findViewById(R$id.magic_redo);
        this.magicUndo = findViewById(R$id.magic_undo);
        this.tvCancel = (ImageView) findViewById(R$id.magic_cancel);
        this.tvOk = (ImageView) findViewById(R$id.magic_ok);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        this.downParams = build;
        FolmeUtil.setCustomTouchAnim(this.tvCancel, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.tvOk, this.downParams, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.magicRedo, this.downParams, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.magicUndo, this.downParams, null, null, null, true);
        FolmeUtil.setDefaultTouchAnim(findViewById(i), new TransitionListener(), true);
        FolmeUtil.setDefaultTouchAnim(findViewById(i2), new TransitionListener(), true);
        if (SystemUiUtil.isWaterFallScreen()) {
            FrameLayout frameLayout = this.mBanner;
            Resources resources = getContext().getResources();
            int i3 = R$dimen.magic_px_65;
            frameLayout.setPadding((int) resources.getDimension(i3), (int) getContext().getResources().getDimension(R$dimen.magic_px_36), (int) getContext().getResources().getDimension(i3), 0);
            return;
        }
        this.mBanner.setPadding(0, (int) getContext().getResources().getDimension(R$dimen.magic_px_36), 0, 0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_cancel) {
            this.mRootActivity.closeDoodleFragment(this.mOriginBitmap, this.mSegmentResult, -1, -1, null);
        } else if (id == R$id.magic_ok) {
            final long currentTimeMillis = System.currentTimeMillis();
            this.mRootActivity.showLoading();
            new Thread(new Runnable() { // from class: com.miui.gallery.magic.matting.doodle.DoodleFragment$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DoodleFragment.$r8$lambda$z8GI7HCwiqIluF7crlxUAAnjlZw(DoodleFragment.this, currentTimeMillis);
                }
            }).start();
        } else if (id == R$id.magic_matting_paint) {
            MagicSampler.getInstance().recordCategory("matting", "paint");
            selectButton(1);
        } else if (id == R$id.magic_matting_rubber) {
            MagicSampler.getInstance().recordCategory("matting", "eraser");
            selectButton(2);
        } else if (id == R$id.magic_undo) {
            this.mDoodle.undo();
            showReUndoBtn();
        } else if (id != R$id.magic_redo) {
        } else {
            this.mDoodle.redo();
            showReUndoBtn();
        }
    }

    public /* synthetic */ void lambda$onClick$2(final long j) {
        Bitmap apply = this.mDoodle.apply();
        final Rect personRect = this.mSegmentResult.getPersonRect(this.mPersonIndex);
        this.mSegmentResult.resetMaskByBitmap(this.mPersonIndex, apply, 0, true);
        apply.recycle();
        this.mSegmentResult.clearContour();
        final Bitmap copy = this.mOriginBitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.mInvoker.inPainting(copy, this.mSegmentResult);
        this.mRootActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.matting.doodle.DoodleFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DoodleFragment.$r8$lambda$OLs3Zz8dNSt13LYS99EOdowNnPQ(DoodleFragment.this, j, personRect, copy);
            }
        });
    }

    public /* synthetic */ void lambda$onClick$1(long j, Rect rect, Bitmap bitmap) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "时间" + currentTimeMillis);
        MagicSampler.getInstance().recordCategory("matting", "save_time", hashMap);
        int execChangeSegmentPerson = execChangeSegmentPerson(rect, 3);
        selectButton(1);
        this.mRootActivity.removeLoadingDialog();
        this.mRootActivity.closeDoodleFragment(this.mOriginBitmap, this.mSegmentResult, execChangeSegmentPerson, this.mPersonIndex, bitmap);
    }

    public final int execChangeSegmentPerson(Rect rect, int i) {
        Rect personRect;
        MattingItem cloneItem = this.mRedoList.peek().cloneItem();
        List<PortraitNode> portraitNodeList = cloneItem.getPortraitNodeList();
        int size = portraitNodeList.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            PortraitNode portraitNode = portraitNodeList.get(size);
            int personIndex = portraitNode.getPersonIndex();
            int i2 = this.mPersonIndex;
            if (personIndex == i2) {
                if (this.mSegmentResult.isEmpty(i2)) {
                    i = 5;
                    this.mRedoList.peek().setmPersonIndex(this.mPersonIndex);
                }
                MattingInvoker mattingInvoker = this.mInvoker;
                Bitmap bitmap = this.mOriginBitmap;
                Bitmap halfBlending = mattingInvoker.halfBlending(bitmap, bitmap, this.mSegmentResult, this.mPersonIndex, new BlendConfig());
                int i3 = this.mSegmentResult.getPersonRect(this.mPersonIndex).top - rect.top;
                portraitNode.mImageBounds = new RectF(0.0f, 0.0f, halfBlending.getWidth(), halfBlending.getHeight());
                portraitNode.mMatrix.postTranslate(personRect.left - rect.left, i3);
                portraitNode.mMatrix.mapRect(portraitNode.mDrawBounds, portraitNode.mImageBounds);
                MagicLog.INSTANCE.showLog("DoodleFragment: mPersonIndex: " + this.mPersonIndex);
                portraitNode.setPersonBitmap(halfBlending);
            } else {
                size--;
            }
        }
        if (i == 3) {
            cloneItem.saveSegmentIdToFile(this.mSegmentResult, this.mRootActivity);
            cloneItem.setOperator(i);
            this.mRedoList.push(cloneItem);
        }
        return i;
    }

    public final void showReUndoBtn() {
        this.upParams = new AnimParams.Builder().setAlpha(0.3f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        AnimParams build = new AnimParams.Builder().setAlpha(0.3f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        boolean z = true;
        if (this.mDoodle.getRedoListSize() <= 1) {
            FolmeUtil.setCustomTouchAnim(this.magicRedo, build, this.upParams, null, null, true);
        } else {
            FolmeUtil.setCustomTouchAnim(this.magicRedo, this.downParams, null, null, null, true);
        }
        if (this.mDoodle.getZoomListSize() <= 1) {
            FolmeUtil.setCustomTouchAnim(this.magicUndo, build, this.upParams, null, null, true);
        } else {
            FolmeUtil.setCustomTouchAnim(this.magicUndo, this.downParams, null, null, null, true);
        }
        showRedo(this.mDoodle.getShowRedo(), !this.mDoodle.getShowRedo() && !this.mDoodle.getShowUndo());
        boolean showUndo = this.mDoodle.getShowUndo();
        if (this.mDoodle.getShowRedo() || this.mDoodle.getShowUndo()) {
            z = false;
        }
        showUndo(showUndo, z);
    }

    public final void selectButton(int i) {
        this.mDoodle.setRubber(i);
        if (i == 1) {
            this.mRubber.setBackgroundResource(R$drawable.magic_idp_make_item_btn_checked);
            this.mRubberText.setTextColor(-1);
            this.mRubberImg.setImageResource(R$drawable.magic_matting_rubber_light);
            this.mPaint.setBackgroundResource(R$drawable.magic_idp_make_item_btn_unchecked);
            this.mPaintText.setTextColor(-16777216);
            this.mPaintImg.setImageResource(R$drawable.magic_matting_paint_black);
            return;
        }
        this.mRubber.setBackgroundResource(R$drawable.magic_idp_make_item_btn_unchecked);
        this.mRubberText.setTextColor(-16777216);
        this.mRubberImg.setImageResource(R$drawable.magic_matting_rubber_black);
        this.mPaint.setBackgroundResource(R$drawable.magic_idp_make_item_btn_checked);
        this.mPaintText.setTextColor(-1);
        this.mPaintImg.setImageResource(R$drawable.magic_matting_paint_light);
    }

    public void showRedo(boolean z, boolean z2) {
        if (!z2) {
            this.magicRedo.setVisibility(0);
        }
        if (z) {
            this.magicRedo.setAlpha(1.0f);
        } else {
            this.magicRedo.setAlpha(0.3f);
        }
    }

    public void showUndo(boolean z, boolean z2) {
        if (!z2) {
            this.magicUndo.setVisibility(0);
        }
        if (z) {
            this.magicUndo.setAlpha(1.0f);
        } else {
            this.magicUndo.setAlpha(0.3f);
        }
    }

    public void setData(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, MattingInvoker mattingInvoker, int i, Stack<MattingItem> stack) {
        this.mPersonIndex = i;
        this.mOriginBitmap = bitmap;
        this.mSegmentResult = segmentResult;
        this.mInvoker = mattingInvoker;
        this.mRedoList = stack;
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            this.mDoodle.setSegment(this.mSegmentResult);
            this.mDoodle.setPersonIndex(this.mPersonIndex);
            selectButton(1);
            this.mDoodle.showMask();
            this.mSeekBar.setCurrentProgress(40.0f);
            this.mDoodle.setStrokeWidth(getPainDiam(40));
            showReUndoBtn();
        }
    }
}
