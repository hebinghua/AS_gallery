package com.miui.gallery.ui.album.main.base.config;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.RichTipDialogFragment;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ViewUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.lang.ref.WeakReference;
import miuix.animation.Folme;
import miuix.animation.IFolme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public abstract class BaseAlbumPageStyle extends BaseIconStyle {
    public Interpolator mAnimInterpolator;
    public int mCreateAlbumButtonAlphaDuration;
    public int mCurrentSystemLongPressedTimeout;
    public WeakDisappearListener mDisappearListener;
    public int mDragItemReturnToSourcePositionAnimDuration;
    public int mEnterDragPressTimeout;
    public float mSearchBarDisableAlpha;
    public float mSearchBarEnableAlpha;

    public boolean isGroupHeader(long j) {
        return j == 2131362609 || j == 2131362616 || j == 2131362613 || j == 2131362624 || j == 2131362621;
    }

    public BaseAlbumPageStyle() {
        initResource();
    }

    @Override // com.miui.gallery.ui.album.main.base.config.BaseIconStyle
    public void initResource() {
        super.initResource();
        this.mCurrentSystemLongPressedTimeout = ViewUtils.getRealLongPressedTimeout();
        this.mDragItemReturnToSourcePositionAnimDuration = ResourceUtils.getInt(R.integer.album_drag_item_return_to_source_position_anim_duration);
        this.mEnterDragPressTimeout = ResourceUtils.getInt(R.integer.album_enter_drag_timeout) + this.mCurrentSystemLongPressedTimeout;
        this.mCreateAlbumButtonAlphaDuration = ResourceUtils.getInt(R.integer.widget_alpha_anim_enter_duration);
        this.mAnimInterpolator = new DecelerateInterpolator();
        this.mSearchBarEnableAlpha = ResourceUtils.getInt(R.integer.search_bar_exit_chose_mode_alpha) / 100.0f;
        this.mSearchBarDisableAlpha = ResourceUtils.getInt(R.integer.search_bar_start_chose_mode_alpha) / 100.0f;
    }

    public int getEnterDragPressTimeout() {
        return this.mEnterDragPressTimeout;
    }

    public int getDragItemReturnToSourcePositionAnimDuration() {
        return this.mDragItemReturnToSourcePositionAnimDuration;
    }

    public void startAppearAnim(View view) {
        startAppearAnim(view, -1.0f, -1.0f);
    }

    public void startAppearAnim(View view, float f, float f2) {
        DefaultLogger.d("BaseAlbumPageStyle", "AppearAnim => view = %s", view == null ? "null" : view.getClass().getSimpleName());
        if (view == null) {
            return;
        }
        Folme.clean(view);
        WeakDisappearListener weakDisappearListener = this.mDisappearListener;
        if (weakDisappearListener != null) {
            weakDisappearListener.setEnable(false);
        }
        view.setVisibility(0);
        view.setEnabled(true);
        AnimConfig animConfig = new AnimConfig();
        animConfig.setEase(EaseManager.getStyle(-2, 0.99f, 0.2f));
        AnimState animState = new AnimState("from");
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        int i = (f > (-1.0f) ? 1 : (f == (-1.0f) ? 0 : -1));
        AnimState add = animState.add(viewProperty, i == 0 ? view.getScaleX() : f);
        ViewProperty viewProperty2 = ViewProperty.SCALE_Y;
        if (i == 0) {
            f = view.getScaleY();
        }
        AnimState add2 = add.add(viewProperty2, f);
        ViewProperty viewProperty3 = ViewProperty.ALPHA;
        if (f2 == -1.0f) {
            f2 = view.getAlpha();
        }
        AnimState add3 = add2.add(viewProperty3, f2);
        AnimState add4 = new AnimState("to").add(viewProperty, 1.0d).add(viewProperty2, 1.0d).add(viewProperty3, 1.0d);
        IFolme useAt = Folme.useAt(view);
        useAt.state().setTo(add3).to(add4, animConfig);
        useAt.touch().setTint(0).handleTouchOf(view, new AnimConfig[0]);
    }

    public void startDisappearAnim(View view) {
        DefaultLogger.d("BaseAlbumPageStyle", "DisappearAnim => view = %s", view == null ? "null" : view.getClass().getSimpleName());
        if (view == null) {
            return;
        }
        Folme.clean(view);
        view.setEnabled(false);
        AnimConfig animConfig = new AnimConfig();
        if (this.mDisappearListener == null) {
            this.mDisappearListener = new WeakDisappearListener();
        }
        this.mDisappearListener.setView(view);
        this.mDisappearListener.setEnable(true);
        animConfig.addListeners(this.mDisappearListener);
        animConfig.setEase(EaseManager.getStyle(-2, 0.99f, 0.2f));
        AnimState animState = new AnimState("from");
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        AnimState add = animState.add(viewProperty, view.getScaleX());
        ViewProperty viewProperty2 = ViewProperty.SCALE_Y;
        AnimState add2 = add.add(viewProperty2, view.getScaleY());
        ViewProperty viewProperty3 = ViewProperty.ALPHA;
        Folme.useAt(view).state().setTo(add2.add(viewProperty3, view.getAlpha())).to(new AnimState("to").add(viewProperty, 0.8500000238418579d).add(viewProperty2, 0.8500000238418579d).add(viewProperty3, SearchStatUtils.POW), animConfig);
    }

    /* loaded from: classes2.dex */
    public static class WeakDisappearListener extends TransitionListener {
        public boolean mEnable;
        public WeakReference<View> mWeakView;

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            WeakReference<View> weakReference = this.mWeakView;
            if (weakReference == null || weakReference.get() == null || !this.mEnable) {
                return;
            }
            this.mWeakView.get().setVisibility(8);
        }

        public void setView(View view) {
            if (view != null) {
                this.mWeakView = new WeakReference<>(view);
            }
        }

        public void setEnable(boolean z) {
            this.mEnable = z;
        }
    }

    public void processDisappearState(View view, float f, int i) {
        if (view == null) {
            return;
        }
        AnimConfig animConfig = new AnimConfig();
        animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
        AnimState animState = new AnimState("from");
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        AnimState add = animState.add(viewProperty, view.getScaleX());
        ViewProperty viewProperty2 = ViewProperty.SCALE_Y;
        AnimState add2 = add.add(viewProperty2, view.getScaleY()).add(ViewProperty.ALPHA, view.getAlpha());
        AnimState animState2 = new AnimState("to");
        if (i == 1) {
            double d = ((1.0f - f) * 0.14999998f) + 0.85f;
            animState2.add(viewProperty, d);
            animState2.add(viewProperty2, d);
        } else if (i == 0) {
            double d2 = (f * 0.14999998f) + 0.85f;
            animState2.add(viewProperty, d2);
            animState2.add(viewProperty2, d2);
        }
        Folme.useAt(view).state().setTo(add2).to(animState2, animConfig);
    }

    public boolean isGroupHeader(View view, RecyclerView recyclerView) {
        return isGroupHeader(view.getId());
    }

    public boolean isMediaTypeItem(View view, RecyclerView recyclerView) {
        return view.getId() == R.id.album_media_group_item_main;
    }

    public boolean isAlbumToolItem(View view, RecyclerView recyclerView) {
        return view.getId() == R.id.album_tool_item_main;
    }

    public boolean isAlbumGroupHeader(View view, RecyclerView recyclerView) {
        return view.getId() == R.id.item_user_create_album_tip || view.getId() == R.id.item_third_album_tip;
    }

    public int getAlbumGroupState(View view) {
        Integer num = (Integer) view.getTag(R.id.tag_album_group_title_state);
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    public Interpolator getMoveAnimInterpolator() {
        return this.mAnimInterpolator;
    }

    public Interpolator getRemoveAnimInterpolator() {
        return this.mAnimInterpolator;
    }

    public Interpolator getAddAnimInterpolator() {
        return this.mAnimInterpolator;
    }

    public Interpolator getChangeAnimInterpolator() {
        return this.mAnimInterpolator;
    }

    public GalleryDialogFragment getDragTipViewIfNeed() {
        if (AlbumPageConfig.getAlbumTabConfig().isNeedShowDragTip()) {
            RichTipDialogFragment newInstance = RichTipDialogFragment.newInstance(R.raw.drag_view, ResourceUtils.getString(R.string.drag_view_tip_title), ResourceUtils.getString(R.string.drag_view_tip_sub_title), null, ResourceUtils.getString(R.string.alert_secret_album_enter), null);
            AlbumConfigSharedPreferences.getInstance().putBoolean(GalleryPreferences.PrefKeys.IS_FIRST_SHOW_DRAG_TIP_VIEW, false);
            return newInstance;
        }
        return null;
    }
}
