package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.screen.stat.ScreenEditorStatUtils;
import com.miui.gallery.editor.photo.screen.text.ScreenTextBubbleAdapter;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;

/* loaded from: classes2.dex */
public class TextPopupWindow extends ToolPopupWindow {
    public OnItemClickListener mBubbleItemClickListener;
    public Context mContext;
    public SimpleRecyclerViewNoSpring mRecyclerView;
    public View mRootView;
    public ScreenTextBubbleAdapter mScreenTextBubbleAdapter;
    public Text mText;
    public TextChangeListener mTextChangeListener;

    /* loaded from: classes2.dex */
    public interface TextChangeListener {
        void onTextChange(TextConfig textConfig, int i);
    }

    public TextPopupWindow(Context context, Text text, TextChangeListener textChangeListener) {
        super(context);
        this.mBubbleItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.penengine.TextPopupWindow.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                TextData itemData = TextPopupWindow.this.mScreenTextBubbleAdapter.getItemData(i);
                TextPopupWindow.this.mScreenTextBubbleAdapter.setSelection(i);
                TextPopupWindow.this.mText.setCurrentSelectIndex(i);
                TextPopupWindow.this.mTextChangeListener.onTextChange((TextConfig) itemData, i);
                ScreenEditorStatUtils.statTextMenuItemClick(i);
                return true;
            }
        };
        this.mContext = context;
        this.mText = text;
        this.mTextChangeListener = textChangeListener;
        init();
    }

    public final void init() {
        this.mRootView = View.inflate(this.mContext, R.layout.screen_text_popwin, null);
        int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(R.dimen.screen_text_popwin_width);
        int dimensionPixelOffset2 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.screen_text_popwin_height);
        setContentView(this.mRootView);
        setWidth(dimensionPixelOffset);
        setHeight(dimensionPixelOffset2);
        initView();
    }

    public final void initView() {
        this.mRecyclerView = (SimpleRecyclerViewNoSpring) this.mRootView.findViewById(R.id.recycler_view);
        ScreenTextBubbleAdapter screenTextBubbleAdapter = new ScreenTextBubbleAdapter(this.mContext, this.mText.getTextDataList(), this.mText.getCurrentSelectIndex());
        this.mScreenTextBubbleAdapter = screenTextBubbleAdapter;
        screenTextBubbleAdapter.setOnItemClickListener(this.mBubbleItemClickListener);
        this.mRecyclerView.setAdapter(this.mScreenTextBubbleAdapter);
        this.mRecyclerView.addItemDecoration(new BlankDivider(0, 0, this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_text_popwin_item_interval), 0, 0));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mRecyclerView);
    }

    public void setText(Text text) {
        this.mText = text;
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        ScreenTextBubbleAdapter screenTextBubbleAdapter = this.mScreenTextBubbleAdapter;
        if (screenTextBubbleAdapter != null) {
            screenTextBubbleAdapter.setSelection(this.mText.getCurrentSelectIndex());
        }
        super.showAtLocation(view, i, i2, i3);
    }
}
