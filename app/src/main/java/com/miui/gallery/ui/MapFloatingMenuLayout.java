package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.ui.MapExpandFloatingMenu;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.support.cardview.CardView;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class MapFloatingMenuLayout extends FrameLayout {
    public AnimState mCollapseState;
    public CardView mContentLayout;
    public boolean mExpandEnable;
    public MapExpandFloatingMenu mExpandMenu;
    public AnimState mExpandState;
    public ViewStub mExpandViewStub;
    public boolean mIsAnimating;
    public OnMenuItemClickListener mItemClickListener;
    public ImageView mLocationBtn;
    public ImageView mShowNearbyBtn;

    /* loaded from: classes2.dex */
    public interface OnMenuItemClickListener {
        void hideNearby();

        void location();

        void showNearby();
    }

    public static /* synthetic */ void $r8$lambda$TMb_m88mHmzmfHTlobpsOC2lAzI(MapFloatingMenuLayout mapFloatingMenuLayout, View view) {
        mapFloatingMenuLayout.lambda$initView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$X33K0dYPcjB8j665AIAIFZNbleY(MapFloatingMenuLayout mapFloatingMenuLayout, View view) {
        mapFloatingMenuLayout.lambda$initView$1(view);
    }

    public MapFloatingMenuLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MapFloatingMenuLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public final void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.map_floting_menu_layout, this);
        this.mContentLayout = (CardView) findViewById(R.id.content_layout);
        this.mLocationBtn = (ImageView) findViewById(R.id.location_btn);
        this.mShowNearbyBtn = (ImageView) findViewById(R.id.show_nearby_btn);
        this.mExpandViewStub = (ViewStub) findViewById(R.id.map_expand_viewstub);
        this.mLocationBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.MapFloatingMenuLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MapFloatingMenuLayout.$r8$lambda$TMb_m88mHmzmfHTlobpsOC2lAzI(MapFloatingMenuLayout.this, view);
            }
        });
        this.mShowNearbyBtn.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.MapFloatingMenuLayout$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MapFloatingMenuLayout.$r8$lambda$X33K0dYPcjB8j665AIAIFZNbleY(MapFloatingMenuLayout.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$initView$0(View view) {
        OnMenuItemClickListener onMenuItemClickListener = this.mItemClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.location();
        }
    }

    public /* synthetic */ void lambda$initView$1(View view) {
        expand();
        OnMenuItemClickListener onMenuItemClickListener = this.mItemClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.showNearby();
        }
    }

    public final void expand() {
        setVisibility(0);
        setLayoutParams(this, -1, -1);
        animExpand();
    }

    public final void animExpand() {
        if (this.mIsAnimating) {
            return;
        }
        if (this.mExpandMenu == null) {
            initExpandView();
        }
        this.mExpandViewStub.setVisibility(0);
        if (this.mExpandState == null) {
            AnimState animState = new AnimState("expand");
            this.mExpandState = animState;
            animState.add(ViewProperty.WIDTH, getResources().getDimensionPixelSize(R.dimen.map_floating_menu_width));
            this.mExpandState.add(ViewProperty.HEIGHT, getResources().getDimensionPixelSize(R.dimen.map_expand_floating_menu_height));
        }
        FolmeUtil.animShowHide(this.mShowNearbyBtn, false, true);
        FolmeUtil.animShowHide(this.mExpandMenu, true, true);
        Folme.useAt(this.mContentLayout).state().to(this.mExpandState, new AnimConfig().addListeners(new TransitionListener() { // from class: com.miui.gallery.ui.MapFloatingMenuLayout.1
            {
                MapFloatingMenuLayout.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                MapFloatingMenuLayout.this.mIsAnimating = true;
                MapFloatingMenuLayout.this.mContentLayout.setRadius(MapFloatingMenuLayout.this.getResources().getDimension(R.dimen.map_expand_floating_menu_corner_radius));
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                MapFloatingMenuLayout.this.mIsAnimating = false;
            }
        }));
    }

    public final void collapse() {
        setVisibility(0);
        setLayoutParams(this, -1, -2);
        animCollapse();
    }

    public final void animCollapse() {
        if (this.mIsAnimating) {
            return;
        }
        if (this.mCollapseState == null) {
            AnimState animState = new AnimState("collapse");
            this.mCollapseState = animState;
            animState.add(ViewProperty.WIDTH, getResources().getDimensionPixelSize(R.dimen.map_floating_menu_width));
            this.mCollapseState.add(ViewProperty.HEIGHT, getResources().getDimensionPixelSize(R.dimen.map_floating_menu_width));
        }
        FolmeUtil.animShowHide(this.mExpandMenu, false, true);
        FolmeUtil.animShowHide(this.mShowNearbyBtn, true, true);
        Folme.useAt(this.mContentLayout).state().to(this.mCollapseState, new AnimConfig().addListeners(new TransitionListener() { // from class: com.miui.gallery.ui.MapFloatingMenuLayout.2
            {
                MapFloatingMenuLayout.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                MapFloatingMenuLayout.this.mIsAnimating = true;
                MapFloatingMenuLayout.this.mContentLayout.setRadius(MapFloatingMenuLayout.this.getResources().getDimension(R.dimen.map_floating_menu_corner_radius));
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                MapFloatingMenuLayout.this.mIsAnimating = false;
            }
        }));
    }

    public final void initExpandView() {
        this.mExpandViewStub.inflate();
        MapExpandFloatingMenu mapExpandFloatingMenu = (MapExpandFloatingMenu) findViewById(R.id.map_expand_menu_layout);
        this.mExpandMenu = mapExpandFloatingMenu;
        mapExpandFloatingMenu.setOnMenuItemClickListener(new MapExpandFloatingMenu.OnMenuItemClickListener() { // from class: com.miui.gallery.ui.MapFloatingMenuLayout.3
            {
                MapFloatingMenuLayout.this = this;
            }

            @Override // com.miui.gallery.ui.MapExpandFloatingMenu.OnMenuItemClickListener
            public void click(int i) {
                if (i == R.id.expand_location_btn) {
                    if (MapFloatingMenuLayout.this.mItemClickListener == null) {
                        return;
                    }
                    MapFloatingMenuLayout.this.mItemClickListener.location();
                } else if (i != R.id.hide_nearby_btn) {
                } else {
                    MapFloatingMenuLayout.this.collapse();
                    if (MapFloatingMenuLayout.this.mItemClickListener == null) {
                        return;
                    }
                    MapFloatingMenuLayout.this.mItemClickListener.hideNearby();
                }
            }
        });
    }

    public final void setLayoutParams(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        view.setLayoutParams(layoutParams);
    }

    public void setOnItemClickListener(OnMenuItemClickListener onMenuItemClickListener, boolean z) {
        this.mExpandEnable = z;
        this.mItemClickListener = onMenuItemClickListener;
        if (z) {
            this.mLocationBtn.setVisibility(8);
            this.mShowNearbyBtn.setVisibility(0);
            return;
        }
        this.mLocationBtn.setVisibility(0);
        this.mShowNearbyBtn.setVisibility(8);
    }

    public void setExpandEnable(boolean z) {
        this.mExpandEnable = z;
        if (z) {
            this.mLocationBtn.setVisibility(8);
            this.mShowNearbyBtn.setVisibility(0);
        }
    }
}
