package com.miui.gallery.vlog.base.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$integer;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.interfaces.RecyclerItemClickListener;
import com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter;
import com.miui.gallery.vlog.clip.sort.ItemTouchHelperCallback;
import com.miui.gallery.vlog.clip.sort.RecyclerViewAdapter;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class EditTimelineSortView extends RelativeLayout implements View.OnClickListener {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public RecyclerViewAdapter mAdapter;
    public ImageView mCancelView;
    public Context mContext;
    public IVideoClip mCurrentClip;
    public ItemTouchHelperCallback mItemTouchHelperCallback;
    public ImageView mOkView;
    public RecyclerItemClickListener.OnItemClickListener mOnItemClickListener;
    public List<IVideoClip> mOriginClips;
    public SimpleRecyclerView mRecyclerView;
    public SortCallback mSortCallback;
    public IVideoClip mTempCurrentClip;
    public List<IVideoClip> mTempIVideoClips;

    /* loaded from: classes2.dex */
    public static abstract class SortCallback implements ItemTouchHelperAdapter {
        @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
        public void onChildDraw(RecyclerView.ViewHolder viewHolder, boolean z) {
        }

        public abstract void onExit(boolean z, int i);

        public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);

        @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
        public void onMove(int i, int i2) {
        }

        @Override // com.miui.gallery.vlog.clip.sort.ItemTouchHelperAdapter
        public abstract void onMoveFinished(RecyclerView.ViewHolder viewHolder, int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getView() {
        return this;
    }

    public EditTimelineSortView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOriginClips = new ArrayList();
        this.mTempIVideoClips = new ArrayList();
        this.mOnItemClickListener = new RecyclerItemClickListener.OnItemClickListener() { // from class: com.miui.gallery.vlog.base.widget.EditTimelineSortView.3
            @Override // com.miui.gallery.vlog.base.interfaces.RecyclerItemClickListener.OnItemClickListener
            public void onLongClick(View view, int i) {
            }

            @Override // com.miui.gallery.vlog.base.interfaces.RecyclerItemClickListener.OnItemClickListener
            public void onScroll(View view, int i) {
            }

            @Override // com.miui.gallery.vlog.base.interfaces.RecyclerItemClickListener.OnItemClickListener
            public void onItemClick(View view, int i) {
                if (EditTimelineSortView.this.mAdapter != null) {
                    EditTimelineSortView.this.mRecyclerView.smoothScrollToPosition(i);
                    EditTimelineSortView.this.mAdapter.setSelectedIndex(i);
                    RecyclerViewAdapter.ViewHolder viewHolder = (RecyclerViewAdapter.ViewHolder) EditTimelineSortView.this.mRecyclerView.getChildViewHolder(view);
                    if (EditTimelineSortView.this.mSortCallback == null) {
                        return;
                    }
                    EditTimelineSortView.this.mSortCallback.onItemClick(viewHolder);
                }
            }
        };
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        RelativeLayout.inflate(context, R$layout.vlog_clip_edit_sort_view_layout, this);
        this.mCancelView = (ImageView) findViewById(R$id.cancel);
        ImageView imageView = (ImageView) findViewById(R$id.ok);
        this.mOkView = imageView;
        imageView.setOnClickListener(this);
        this.mCancelView.setOnClickListener(this);
        FolmeUtilsEditor.animButton(this.mOkView);
        FolmeUtilsEditor.animButton(this.mCancelView);
        this.mRecyclerView = (SimpleRecyclerView) findViewById(R$id.sortRecyclerView);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this.mContext);
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(this.mContext));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context);
        this.mAdapter = recyclerViewAdapter;
        this.mRecyclerView.setAdapter(recyclerViewAdapter);
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.mContext, this.mOnItemClickListener));
        this.mItemTouchHelperCallback = new ItemTouchHelperCallback(this.mAdapter, true, false);
        Resources resources = getResources();
        int i = R$dimen.vlog_sort_item_gap;
        int dimensionPixelSize = resources.getDimensionPixelSize(i);
        this.mRecyclerView.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(i), 0, 0));
        initAnimatorData();
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mRecyclerView, this.mItemTouchHelperCallback);
    }

    public final void initAnimatorData() {
        Resources resources = VlogUtils.getGalleryApp().getResources();
        if (sAnimOffset == 0) {
            sAnimOffset = resources.getDimensionPixelSize(R$dimen.vlog_enter_sub_editor_main_menu_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = resources.getInteger(R$integer.vlog_sub_editor_sub_menu_appear_delay);
        }
    }

    public void setOriginClips(List<IVideoClip> list) {
        this.mOriginClips = list;
        this.mAdapter.updateData(list);
        this.mTempIVideoClips.clear();
        this.mTempIVideoClips.addAll(list);
        this.mAdapter.notifyDataSetChanged();
    }

    public void setCurrentClip(IVideoClip iVideoClip) {
        this.mCurrentClip = iVideoClip;
        this.mTempCurrentClip = iVideoClip;
        RecyclerViewAdapter recyclerViewAdapter = this.mAdapter;
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.setSelectedIndex(this.mOriginClips.indexOf(iVideoClip));
        }
    }

    public List<IVideoClip> getTempIVideoClips() {
        return this.mTempIVideoClips;
    }

    public void buildSortAnimator(boolean z) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            if (getView() != null) {
                getView().setVisibility(4);
            }
            objectAnimator.setStartDelay(sAnimAppearDelay);
            objectAnimator.setDuration(sAnimAppearDuration);
            objectAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.vlog.base.widget.EditTimelineSortView.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    VlogUtils.showViews(EditTimelineSortView.this.getView());
                }
            });
        } else {
            PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setValues(ofFloat);
            objectAnimator.setDuration(sAnimDisappearDuration);
            objectAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.vlog.base.widget.EditTimelineSortView.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (EditTimelineSortView.this.mAdapter != null) {
                        EditTimelineSortView.this.mAdapter.notifyDataSetChanged();
                    }
                }
            });
            VlogUtils.hideViews(getView());
        }
        objectAnimator.setTarget(getView());
        objectAnimator.start();
    }

    public void setSortCallback(SortCallback sortCallback) {
        RecyclerViewAdapter recyclerViewAdapter = this.mAdapter;
        if (recyclerViewAdapter != null) {
            this.mSortCallback = sortCallback;
            recyclerViewAdapter.setSortCallback(sortCallback);
        }
    }

    public void setVideoFrameLoader(VideoFrameLoader videoFrameLoader) {
        this.mAdapter.setVideoFrameLoader(videoFrameLoader);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mOkView) {
            if (this.mAdapter.isDragging()) {
                return;
            }
            setCurrentClip(this.mAdapter.getSelectedItem());
            exit(true);
        } else if (view != this.mCancelView || this.mAdapter.isDragging()) {
        } else {
            exit(false);
        }
    }

    public void exit(boolean z) {
        buildSortAnimator(false);
        SortCallback sortCallback = this.mSortCallback;
        if (sortCallback != null) {
            sortCallback.onExit(z, z ? this.mAdapter.getSelectedIndex() : this.mTempIVideoClips.indexOf(this.mTempCurrentClip));
        }
    }
}
