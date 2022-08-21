package com.miui.gallery.movie.ui.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;
import com.miui.gallery.movie.ui.adapter.EditAdapter;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import java.util.Collections;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class EditAdapter extends BaseAdapter<ImageEntity> {
    public static final ImageEntity ITEM_ADD = new ImageEntity("ITEM_ADD", null);
    public boolean isShook;
    public ItemTouchHelper.Callback mCallback;
    public OnActionListener mOnActionListener;
    public final RequestOptions mRequestOptions;

    /* loaded from: classes2.dex */
    public interface OnActionListener {
        void onMove(int i, int i2);
    }

    public EditAdapter(Context context) {
        super(context);
        this.mCallback = new ItemTouchHelper.Callback() { // from class: com.miui.gallery.movie.ui.adapter.EditAdapter.1
            public int mFromPosition;
            public boolean mLastActive;
            public boolean mMove;
            public int mToPosition;
            public final Interpolator sDragViewScrollCapInterpolator = new Interpolator() { // from class: com.miui.gallery.movie.ui.adapter.EditAdapter.1.1
                @Override // android.animation.TimeInterpolator
                public float getInterpolation(float f) {
                    float f2 = f - 1.0f;
                    return (f2 * f2 * f2 * f2 * f2) + 1.0f;
                }

                {
                    AnonymousClass1.this = this;
                }
            };
            public final Interpolator sDragScrollInterpolator = new Interpolator() { // from class: com.miui.gallery.movie.ui.adapter.EditAdapter.1.2
                @Override // android.animation.TimeInterpolator
                public float getInterpolation(float f) {
                    return f * f * f;
                }

                {
                    AnonymousClass1.this = this;
                }
            };

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            }

            {
                EditAdapter.this = this;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                EditAdapter.this.isShook = false;
                return true;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
                super.onSelectedChanged(viewHolder, i);
                if (i != 0) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    if (EditAdapter.this.isAddItem(adapterPosition)) {
                        return;
                    }
                    EditAdapter.this.doScaleAnimal(viewHolder.itemView, true);
                    this.mMove = true;
                    this.mFromPosition = adapterPosition;
                    this.mToPosition = adapterPosition;
                } else if (!this.mMove || this.mFromPosition == this.mToPosition || EditAdapter.this.mOnActionListener == null) {
                } else {
                    EditAdapter.this.mOnActionListener.onMove(this.mFromPosition, this.mToPosition);
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
                super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
                if (i == 2 && this.mLastActive) {
                    EditAdapter.this.doScaleAnimal(viewHolder.itemView, true);
                }
                if (this.mLastActive && !z) {
                    EditAdapter.this.doScaleAnimal(viewHolder.itemView, false);
                }
                this.mLastActive = z;
                EditAdapter.this.setSelectedItemPosition(-1);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                EditAdapter.this.doScaleAnimal(viewHolder.itemView, false);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (EditAdapter.this.isAddItem(viewHolder.getAdapterPosition())) {
                    return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
                }
                return ItemTouchHelper.Callback.makeMovementFlags(15, 0);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int i, int i2, int i3, long j) {
                float f = 1.0f;
                int signum = (int) (((int) Math.signum(i2)) * 40 * this.sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (Math.abs(i2) * 1.0f) / i)));
                if (j <= 500) {
                    f = ((float) j) / 500.0f;
                }
                int interpolation = (int) (signum * this.sDragScrollInterpolator.getInterpolation(f));
                return interpolation == 0 ? i2 > 0 ? 1 : -1 : interpolation;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                EditAdapter.this.doScaleAnimal(viewHolder.itemView, true);
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                if (EditAdapter.this.isAddItem(adapterPosition2)) {
                    adapterPosition2--;
                }
                this.mToPosition = adapterPosition2;
                if (adapterPosition == adapterPosition2) {
                    return true;
                }
                if (adapterPosition < adapterPosition2) {
                    int i = adapterPosition;
                    while (i < adapterPosition2) {
                        int i2 = i + 1;
                        Collections.swap(EditAdapter.this.getList(), i, i2);
                        EditAdapter.this.swapSelectItem(i, i2);
                        i = i2;
                    }
                } else {
                    for (int i3 = adapterPosition; i3 > adapterPosition2; i3--) {
                        int i4 = i3 - 1;
                        Collections.swap(EditAdapter.this.getList(), i3, i4);
                        EditAdapter.this.swapSelectItem(i3, i4);
                    }
                }
                LinearMotorHelper.performHapticFeedback(recyclerView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                EditAdapter.this.notifyItemMoved(adapterPosition, adapterPosition2);
                return true;
            }
        };
        this.mSelectedItemPosition = -1;
        this.mRequestOptions = RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo978skipMemoryCache(false).mo964lock();
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public int getLayoutId(int i) {
        return R$layout.movie_layout_edit_item;
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public BaseAdapter.BaseHolder<ImageEntity> getHolder(View view) {
        return new EditHolder(view);
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.mList == null) {
            return 0;
        }
        return super.getItemCount() + 1;
    }

    public int getListSize() {
        List<T> list = this.mList;
        if (list == 0) {
            return 0;
        }
        return list.size();
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    /* renamed from: getItemData */
    public ImageEntity mo1153getItemData(int i) {
        if (i < 0) {
            return null;
        }
        if (i >= this.mList.size()) {
            return ITEM_ADD;
        }
        return (ImageEntity) super.mo1153getItemData(i);
    }

    public boolean isAddItem(int i) {
        ImageEntity mo1153getItemData = mo1153getItemData(i);
        return mo1153getItemData != null && TextUtils.equals(mo1153getItemData.image, "ITEM_ADD");
    }

    /* loaded from: classes2.dex */
    public class EditHolder extends BaseAdapter.BaseHolder {
        public ImageView mImageView;
        public ImageView mImageViewAdd;
        public int mTargetSize;

        public static /* synthetic */ void $r8$lambda$A9ycbCD7OBJH4Ii5ZziJ0cxMr0I(EditHolder editHolder, int i, View view) {
            editHolder.lambda$bindView$0(i, view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EditHolder(View view) {
            super(view);
            EditAdapter.this = r7;
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mImageView = (ImageView) view.findViewById(R$id.item_iv);
            this.mImageViewAdd = (ImageView) view.findViewById(R$id.item_iv_add);
            FolmeUtil.setCustomTouchAnim(this.mImageViewAdd, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, null, true);
            this.mTargetSize = (int) this.mImageView.getResources().getDimension(R$dimen.photo_movie_edit_edit_item_width);
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.BaseHolder
        public void bindView(Object obj, final int i) {
            if (EditAdapter.this.isAddItem(i)) {
                this.mImageViewAdd.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.adapter.EditAdapter$EditHolder$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EditAdapter.EditHolder.$r8$lambda$A9ycbCD7OBJH4Ii5ZziJ0cxMr0I(EditAdapter.EditHolder.this, i, view);
                    }
                });
                this.mImageView.setVisibility(8);
                this.mImageViewAdd.setVisibility(0);
                return;
            }
            if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                this.itemView.setHapticFeedbackEnabled(false);
            } else {
                this.itemView.setHapticFeedbackEnabled(true);
            }
            this.mImageView.setVisibility(0);
            this.mImageViewAdd.setVisibility(8);
            Glide.with(this.itemView).mo985asBitmap().mo963load(EditAdapter.this.mo1153getItemData(i).image).mo946apply((BaseRequestOptions<?>) EditAdapter.this.mRequestOptions).mo970override(this.mTargetSize).into(this.mImageView);
        }

        public /* synthetic */ void lambda$bindView$0(int i, View view) {
            BaseAdapter.ItemSelectChangeListener itemSelectChangeListener = EditAdapter.this.getItemSelectChangeListener();
            if (itemSelectChangeListener != null) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_LIGHT);
                itemSelectChangeListener.onItemSelect((miuix.recyclerview.widget.RecyclerView) this.itemView.getParent(), null, i, false);
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
        if (!isAddItem(i)) {
            baseHolder.itemView.setContentDescription(this.mContext.getResources().getString(R$string.movie_edit_photo));
        }
    }
}
