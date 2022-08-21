package com.miui.gallery.gallerywidget.ui.editor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Property;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;
import com.miui.gallery.movie.ui.view.MovieRoundImageView;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import java.util.ArrayList;
import java.util.Collections;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class WidgetEditorAdapter extends BaseAdapter<ImageEntity> {
    public final long DELETE_IMAGE_ALPHA_DURATION;
    public final String TAG;
    public boolean isShook;
    public final ItemTouchHelper.Callback mCallback;
    public boolean mIsEditorMode;
    public OnActionListener mOnActionListener;
    public final RecyclerView mRecyclerView;
    public final RequestOptions mRequestOptions;

    /* loaded from: classes2.dex */
    public interface OnActionListener {
        void onDelete(int i);

        void onMove(int i, int i2);
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public int getLayoutId(int i) {
        return R.layout.widget_editor_item;
    }

    public void release() {
    }

    public WidgetEditorAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.TAG = "WidgetEditorAdapter";
        this.DELETE_IMAGE_ALPHA_DURATION = 200L;
        this.mIsEditorMode = true;
        this.mCallback = new ItemTouchHelper.Callback() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.2
            public int mFromPosition;
            public boolean mLastActive;
            public boolean mMove;
            public int mToPosition;
            public final Interpolator sDragViewScrollCapInterpolator = new Interpolator() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.2.1
                @Override // android.animation.TimeInterpolator
                public float getInterpolation(float f) {
                    float f2 = f - 1.0f;
                    return (f2 * f2 * f2 * f2 * f2) + 1.0f;
                }
            };
            public final Interpolator sDragScrollInterpolator = new Interpolator() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.2.2
                @Override // android.animation.TimeInterpolator
                public float getInterpolation(float f) {
                    return f * f * f;
                }
            };

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                WidgetEditorAdapter.this.isShook = false;
                return WidgetEditorAdapter.this.mIsEditorMode;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
                super.onSelectedChanged(viewHolder, i);
                if (i == 0) {
                    WidgetEditorAdapter.this.updateHolder(true);
                    if (!this.mMove || this.mFromPosition == this.mToPosition || WidgetEditorAdapter.this.mOnActionListener == null) {
                        return;
                    }
                    WidgetEditorAdapter.this.mOnActionListener.onMove(this.mFromPosition, this.mToPosition);
                    return;
                }
                int adapterPosition = viewHolder.getAdapterPosition();
                WidgetEditorAdapter.this.doScaleAnimal(viewHolder.itemView, true);
                WidgetEditorAdapter.this.updateHolder(false);
                this.mMove = true;
                this.mFromPosition = adapterPosition;
                this.mToPosition = adapterPosition;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView2, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
                super.onChildDraw(canvas, recyclerView2, viewHolder, f, f2, i, z);
                if (i == 2 && this.mLastActive) {
                    WidgetEditorAdapter.this.doScaleAnimal(viewHolder.itemView, true);
                }
                this.mLastActive = z;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void clearView(RecyclerView recyclerView2, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView2, viewHolder);
                WidgetEditorAdapter.this.doScaleAnimal(viewHolder.itemView, false);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public int getMovementFlags(RecyclerView recyclerView2, RecyclerView.ViewHolder viewHolder) {
                viewHolder.getAdapterPosition();
                return ItemTouchHelper.Callback.makeMovementFlags(12, 0);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public int interpolateOutOfBoundsScroll(RecyclerView recyclerView2, int i, int i2, int i3, long j) {
                float f = 1.0f;
                int signum = (int) (((int) Math.signum(i2)) * 40 * this.sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (Math.abs(i2) * 1.0f) / i)));
                if (j <= 500) {
                    f = ((float) j) / 500.0f;
                }
                int interpolation = (int) (signum * this.sDragScrollInterpolator.getInterpolation(f));
                return interpolation == 0 ? i2 > 0 ? 1 : -1 : interpolation;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean onMove(RecyclerView recyclerView2, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                this.mToPosition = adapterPosition2;
                if (adapterPosition == adapterPosition2) {
                    return false;
                }
                if (adapterPosition < adapterPosition2) {
                    int i = adapterPosition;
                    while (i < adapterPosition2) {
                        int i2 = i + 1;
                        Collections.swap(WidgetEditorAdapter.this.getList(), i, i2);
                        WidgetEditorAdapter.this.swapSelectItem(i, i2);
                        i = i2;
                    }
                } else {
                    for (int i3 = adapterPosition; i3 > adapterPosition2; i3--) {
                        int i4 = i3 - 1;
                        Collections.swap(WidgetEditorAdapter.this.getList(), i3, i4);
                        WidgetEditorAdapter.this.swapSelectItem(i3, i4);
                    }
                }
                LinearMotorHelper.performHapticFeedback(recyclerView2, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                WidgetEditorAdapter.this.notifyItemMoved(adapterPosition, adapterPosition2);
                return true;
            }
        };
        this.mRecyclerView = recyclerView;
        this.mSelectedItemPosition = -1;
        this.mRequestOptions = RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo978skipMemoryCache(false).mo964lock();
    }

    public final void updateHolder(final boolean z) {
        this.mIsEditorMode = z;
        ArrayList<EditHolder> arrayList = new ArrayList();
        for (int i = 0; i < getItemCount(); i++) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.mRecyclerView.findViewHolderForAdapterPosition(i);
            if (findViewHolderForAdapterPosition != null) {
                arrayList.add((EditHolder) findViewHolderForAdapterPosition);
            }
        }
        for (final EditHolder editHolder : arrayList) {
            float f = 0.0f;
            editHolder.mDeleteView.setAlpha(z ? 0.0f : 1.0f);
            editHolder.mDeleteView.setVisibility(0);
            ViewPropertyAnimator animate = editHolder.mDeleteView.animate();
            if (z) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(200L).setListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    editHolder.mDeleteView.setVisibility(z ? 0 : 4);
                    editHolder.mDeleteView.setAlpha(1.0f);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    editHolder.mDeleteView.setVisibility(z ? 0 : 4);
                    editHolder.mDeleteView.setAlpha(1.0f);
                }
            }).start();
        }
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    /* renamed from: getHolder */
    public BaseAdapter.BaseHolder<ImageEntity> getHolder2(View view) {
        return new EditHolder(view);
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.mList == null) {
            return 0;
        }
        return super.getItemCount();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    /* renamed from: getItemData */
    public ImageEntity mo1153getItemData(int i) {
        if (i >= 0 && i < this.mList.size()) {
            return (ImageEntity) super.mo1153getItemData(i);
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class EditHolder extends BaseAdapter.BaseHolder<ImageEntity> {
        public View.OnClickListener mDeleteClick;
        public ImageView mDeleteImage;
        public View mDeleteView;
        public MovieRoundImageView mImageView;
        public int mTargetSize;

        public EditHolder(View view) {
            super(view);
            this.mDeleteClick = new View.OnClickListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.EditHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (WidgetEditorAdapter.this.mOnActionListener != null) {
                        WidgetEditorAdapter.this.mOnActionListener.onDelete(EditHolder.this.getAdapterPosition());
                    }
                }
            };
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mImageView = (MovieRoundImageView) view.findViewById(R.id.item_iv);
            this.mDeleteImage = (ImageView) view.findViewById(R.id.image_delete);
            this.mDeleteView = view.findViewById(R.id.layout_delete);
            this.mTargetSize = (int) this.mImageView.getResources().getDimension(R.dimen.gallery_widget_editor_recycler_item_width);
            this.mImageView.setCorner((int) this.mImageView.getResources().getDimension(R.dimen.gallery_widget_editor_recycler_item_corner));
            this.mDeleteImage.setOnClickListener(this.mDeleteClick);
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.BaseHolder
        public void bindView(ImageEntity imageEntity, int i) {
            this.itemView.setHapticFeedbackEnabled(!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue());
            if (WidgetEditorAdapter.this.mIsEditorMode) {
                this.mDeleteView.setVisibility(0);
            } else {
                this.mDeleteView.setVisibility(4);
            }
            if (imageEntity != null) {
                Glide.with(this.itemView).mo985asBitmap().mo963load(imageEntity.getPicPath()).mo946apply((BaseRequestOptions<?>) WidgetEditorAdapter.this.mRequestOptions).mo970override(this.mTargetSize).into(this.mImageView);
            }
        }
    }

    public ItemTouchHelper.Callback getCallBack() {
        return this.mCallback;
    }

    public final void swapSelectItem(int i, int i2) {
        int i3 = this.mSelectedItemPosition;
        if (i3 == i) {
            this.mSelectedItemPosition = i2;
        } else if (i3 != i2) {
        } else {
            this.mSelectedItemPosition = i;
        }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.mOnActionListener = onActionListener;
    }

    public final void doScaleAnimal(View view, boolean z) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        Property property = View.SCALE_X;
        float[] fArr = new float[2];
        fArr[0] = view.getScaleX();
        float f = 1.2f;
        fArr[1] = z ? 1.2f : 1.0f;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(property, fArr);
        Property property2 = View.SCALE_Y;
        float[] fArr2 = new float[2];
        fArr2[0] = view.getScaleY();
        if (!z) {
            f = 1.0f;
        }
        fArr2[1] = f;
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(property2, fArr2);
        objectAnimator.setTarget(view);
        objectAnimator.setValues(ofFloat, ofFloat2);
        objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
        objectAnimator.setDuration(200L);
        objectAnimator.start();
        if (!this.isShook) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_PICK_UP);
            this.isShook = true;
        }
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseAdapter.BaseHolder<ImageEntity> baseHolder, int i) {
        super.onBindViewHolder((BaseAdapter.BaseHolder) baseHolder, i);
        baseHolder.itemView.setContentDescription(this.mContext.getResources().getString(R.string.movie_edit_photo));
    }

    public void setEditorMode(boolean z) {
        this.mIsEditorMode = z;
    }
}
