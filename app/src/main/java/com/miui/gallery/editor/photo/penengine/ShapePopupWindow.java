package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.screen.doodle.ScreenDoodleAdapter;
import com.miui.gallery.editor.photo.screen.stat.ScreenEditorStatUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.util.List;

/* loaded from: classes2.dex */
public class ShapePopupWindow extends ToolPopupWindow {
    public ScreenDoodleAdapter mAdapter;
    public Context mContext;
    public List<DoodleData> mDoodleData;
    public SimpleRecyclerViewNoSpring mRecyclerView;
    public View mRootView;
    public Shape mShape;
    public ShapeChangeListener mShapeChangeListener;

    /* loaded from: classes2.dex */
    public interface ShapeChangeListener {
        void onShapeChange(DoodleData doodleData, int i);
    }

    /* renamed from: $r8$lambda$X3n-HvUoZU9_4lL6zrS47RJhfBE */
    public static /* synthetic */ boolean m917$r8$lambda$X3nHvUoZU9_4lL6zrS47RJhfBE(ShapePopupWindow shapePopupWindow, RecyclerView recyclerView, View view, int i) {
        return shapePopupWindow.lambda$initView$0(recyclerView, view, i);
    }

    public ShapePopupWindow(Context context, Shape shape, ShapeChangeListener shapeChangeListener) {
        super(context);
        this.mContext = context;
        this.mShape = shape;
        this.mShapeChangeListener = shapeChangeListener;
        init();
    }

    public final void init() {
        this.mRootView = View.inflate(this.mContext, R.layout.screen_shape_popwin, null);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_shape_popwin_width);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_shape_popwin_height);
        setContentView(this.mRootView);
        setWidth(dimensionPixelSize);
        setHeight(dimensionPixelSize2);
        initView();
    }

    public final void initView() {
        this.mRecyclerView = (SimpleRecyclerViewNoSpring) this.mRootView.findViewById(R.id.recycler_view);
        List<DoodleData> doodleDataList = this.mShape.getDoodleDataList();
        this.mDoodleData = doodleDataList;
        ScreenDoodleAdapter screenDoodleAdapter = new ScreenDoodleAdapter(doodleDataList, this.mShape.getCurrentSelectIndex());
        this.mAdapter = screenDoodleAdapter;
        screenDoodleAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.penengine.ShapePopupWindow$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                return ShapePopupWindow.m917$r8$lambda$X3nHvUoZU9_4lL6zrS47RJhfBE(ShapePopupWindow.this, recyclerView, view, i);
            }
        });
        this.mRecyclerView.addItemDecoration(new BlankDivider(0, 0, (int) this.mContext.getResources().getDimension(R.dimen.screen_shape_popwin_item_margin), 0, 0));
        this.mRecyclerView.setAdapter(this.mAdapter);
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mRecyclerView);
    }

    public /* synthetic */ boolean lambda$initView$0(RecyclerView recyclerView, View view, int i) {
        this.mAdapter.setSelection(i);
        this.mShape.setCurrentSelectIndex(i);
        onShapeChange(this.mDoodleData.get(i), i);
        ScreenEditorStatUtils.statDoodleMenuItemClick(i);
        return false;
    }

    public final void onShapeChange(DoodleData doodleData, int i) {
        ShapeChangeListener shapeChangeListener = this.mShapeChangeListener;
        if (shapeChangeListener != null) {
            shapeChangeListener.onShapeChange(doodleData, i);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        ScreenDoodleAdapter screenDoodleAdapter = this.mAdapter;
        if (screenDoodleAdapter != null) {
            screenDoodleAdapter.setSelection(this.mShape.getCurrentSelectIndex());
        }
        super.showAtLocation(view, i, i2, i3);
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
    }
}
