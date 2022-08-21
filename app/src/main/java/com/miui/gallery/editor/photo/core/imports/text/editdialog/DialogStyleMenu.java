package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.HostAbility;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.textstyle.TextEditStyleAdapter;
import com.miui.gallery.editor.photo.core.imports.text.textstyle.TextEditStyleItemDecoration;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.photo.widgets.seekbar.CircleDrawable;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;

/* loaded from: classes2.dex */
public class DialogStyleMenu extends DialogSubMenu<TextEditDialog.ConfigChangeListener, DialogStatusData> {
    public Context mContext;
    public HostAbility mHostAbility;
    public TextView mProgressTv;
    public RecyclerView mRecyclerView;
    public RelativeLayout.LayoutParams mRelativeLayoutParams;
    public TextView mTextEditDialogTabTitle;
    public TextEditStyleAdapter mTextEditStyleAdapter;
    public TextEditStyleItemDecoration mTextEditStyleItemDecoration;
    public BubbleSeekBar mTransparentBubbleSeekBar;
    public ViewGroup mWholeView;

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public /* bridge */ /* synthetic */ void setChecked(boolean z) {
        super.setChecked(z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DialogStyleMenu(Context context, ViewGroup viewGroup, TextEditDialog.ConfigChangeListener configChangeListener, HostAbility hostAbility) {
        super(context, viewGroup, R.string.text_edit_dialog_style, R.drawable.text_edit_dialog_tab_icon_style);
        this.mContext = context;
        this.mListener = configChangeListener;
        this.mHostAbility = hostAbility;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public ViewGroup initSubMenuView(Context context, ViewGroup viewGroup) {
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_edit_dialog_options_style_panel, viewGroup, false);
        this.mWholeView = viewGroup2;
        this.mProgressTv = (TextView) viewGroup2.findViewById(R.id.tv_progress);
        this.mTextEditDialogTabTitle = (TextView) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_style_transparent_title);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) this.mWholeView.findViewById(R.id.bubbleSeekBar);
        this.mTransparentBubbleSeekBar = bubbleSeekBar;
        bubbleSeekBar.setHideBubble(true);
        this.mTransparentBubbleSeekBar.setProgressListener(new TransparentBsbProgressListener());
        RecyclerView recyclerView = (RecyclerView) this.mWholeView.findViewById(R.id.recyclerView);
        this.mRecyclerView = recyclerView;
        recyclerView.setItemViewCacheSize(0);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(context, context.getResources().getInteger(R.integer.text_dialog_style_span_count)));
        TextEditStyleAdapter textEditStyleAdapter = new TextEditStyleAdapter(context);
        this.mTextEditStyleAdapter = textEditStyleAdapter;
        textEditStyleAdapter.setOnItemClickListener(new StyleItemSelectChangeListener());
        this.mRecyclerView.setAdapter(this.mTextEditStyleAdapter);
        TextEditStyleItemDecoration textEditStyleItemDecoration = new TextEditStyleItemDecoration(context.getResources().getInteger(R.integer.text_dialog_style_span_count), context.getResources().getDimensionPixelSize(R.dimen.text_edit_style_adapter_item_spaces), context.getResources().getDimensionPixelSize(R.dimen.text_edit_style_adapter_item_column_spaces));
        this.mTextEditStyleItemDecoration = textEditStyleItemDecoration;
        this.mRecyclerView.addItemDecoration(textEditStyleItemDecoration);
        initView(context);
        return this.mWholeView;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void OnConfigurationChanged() {
        super.OnConfigurationChanged();
        this.mRecyclerView.removeAllViews();
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mContext.getResources().getInteger(R.integer.text_dialog_style_span_count));
        this.mTextEditStyleItemDecoration.updateItemDecoration(this.mContext.getResources().getInteger(R.integer.text_dialog_style_span_count), this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_style_adapter_item_column_spaces), this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_style_adapter_item_spaces));
        this.mRecyclerView.invalidateItemDecorations();
        ScreenAdaptationHelper.updateBSBWidth(this.mContext, this.mTransparentBubbleSeekBar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mTextEditDialogTabTitle.getLayoutParams();
        this.mRelativeLayoutParams = layoutParams;
        layoutParams.setMarginEnd(this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_dialog_tab_style_transparent_title_margin_right));
        this.mTextEditDialogTabTitle.setLayoutParams(this.mRelativeLayoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mProgressTv.getLayoutParams();
        this.mRelativeLayoutParams = layoutParams2;
        layoutParams2.setMarginStart(this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_dialog_options_style_panel_rl_seek_bar_margin_right));
        this.mProgressTv.setLayoutParams(this.mRelativeLayoutParams);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void initializeData(DialogStatusData dialogStatusData) {
        BubbleSeekBar bubbleSeekBar = this.mTransparentBubbleSeekBar;
        bubbleSeekBar.setCurrentProgress((int) Math.abs((dialogStatusData.transparentProgress * 100.0f) - bubbleSeekBar.getMaxProgress()));
        TextView textView = this.mProgressTv;
        textView.setText(((int) this.mTransparentBubbleSeekBar.getCurrentProgress()) + "%");
        this.mTextEditStyleAdapter.setDialogStatusData(dialogStatusData);
        this.mTextEditStyleAdapter.setHostAbility(this.mHostAbility);
    }

    public final void initView(Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.custom_seekbar_thumb_size);
        CircleDrawable circleDrawable = new CircleDrawable(context.getResources().getDimensionPixelSize(R.dimen.editor_seek_bar_progress_thumb_start), context.getResources());
        circleDrawable.setIntrinsicWidth(dimensionPixelSize);
        circleDrawable.setIntrinsicHeight(dimensionPixelSize);
    }

    public final void itemClickHandle(View view, int i) {
        if (i == 0) {
            if (this.mListener == 0) {
                return;
            }
            this.mTextEditStyleAdapter.getInitializeData().textBold = true ^ this.mTextEditStyleAdapter.getInitializeData().textBold;
            ((TextEditDialog.ConfigChangeListener) this.mListener).onBoldChange(this.mTextEditStyleAdapter.getInitializeData().textBold);
        } else if (i == 1) {
            if (this.mListener == 0 || this.mTextEditStyleAdapter.getInitializeData().isSubstrate) {
                return;
            }
            this.mTextEditStyleAdapter.getInitializeData().isStroke = true ^ this.mTextEditStyleAdapter.getInitializeData().isStroke;
            ((TextEditDialog.ConfigChangeListener) this.mListener).onStrokeChange(this.mTextEditStyleAdapter.getInitializeData().isStroke);
        } else if (i == 2) {
            if (this.mListener == 0) {
                return;
            }
            this.mTextEditStyleAdapter.getInitializeData().textShadow = true ^ this.mTextEditStyleAdapter.getInitializeData().textShadow;
            ((TextEditDialog.ConfigChangeListener) this.mListener).onShadowChange(this.mTextEditStyleAdapter.getInitializeData().textShadow);
        } else if (i == 3) {
            DialogStatusData initializeData = this.mTextEditStyleAdapter.getInitializeData();
            AutoLineLayout.TextAlignment textAlignment = AutoLineLayout.TextAlignment.LEFT;
            initializeData.textAlignment = textAlignment;
            configTextToAlign(textAlignment);
        } else if (i == 4) {
            DialogStatusData initializeData2 = this.mTextEditStyleAdapter.getInitializeData();
            AutoLineLayout.TextAlignment textAlignment2 = AutoLineLayout.TextAlignment.CENTER;
            initializeData2.textAlignment = textAlignment2;
            configTextToAlign(textAlignment2);
        } else if (i != 5) {
        } else {
            DialogStatusData initializeData3 = this.mTextEditStyleAdapter.getInitializeData();
            AutoLineLayout.TextAlignment textAlignment3 = AutoLineLayout.TextAlignment.RIGHT;
            initializeData3.textAlignment = textAlignment3;
            configTextToAlign(textAlignment3);
        }
    }

    public final void configTextToAlign(AutoLineLayout.TextAlignment textAlignment) {
        T t = this.mListener;
        if (t != 0) {
            ((TextEditDialog.ConfigChangeListener) t).onTextAlignChange(textAlignment);
        }
    }

    /* loaded from: classes2.dex */
    public class StyleItemSelectChangeListener implements OnItemClickListener {
        public StyleItemSelectChangeListener() {
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            DialogStyleMenu.this.itemClickHandle(view, i);
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public class TransparentBsbProgressListener implements BubbleSeekBar.ProgressListener {
        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
        }

        public TransparentBsbProgressListener() {
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
            TextView textView = DialogStyleMenu.this.mProgressTv;
            textView.setText(i + "%");
            if (DialogStyleMenu.this.mListener != 0) {
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                ((TextEditDialog.ConfigChangeListener) DialogStyleMenu.this.mListener).onTransparentChange(Math.abs(i - DialogStyleMenu.this.mTransparentBubbleSeekBar.getMaxProgress()));
            }
        }
    }
}
