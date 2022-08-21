package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.widgets.seekbar.CircleDrawable;
import com.miui.gallery.editor.photo.widgets.seekbar.ColorGradientDrawable;
import com.miui.gallery.editor.photo.widgets.seekbar.ColorPicker;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.seekbar.BubbleIndicator;

/* loaded from: classes2.dex */
public class DialogStyleMenuOld extends DialogSubMenuOld<TextEditDialog.ConfigChangeListener, DialogStatusData> implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public TextView mAlignButton;
    public final Drawable mAlignCenterDrawable;
    public final String mAlignCenterString;
    public int mAlignIndex;
    public final Drawable mAlignLeftDrawable;
    public final String mAlignLeftString;
    public final Drawable mAlignRightDrawable;
    public final String mAlignRightString;
    public TextView mBoldButton;
    public ColorPicker mColorPickSeekBar;
    public TextView mShadowButton;
    public SeekBar mTransparentSeekBar;
    public ViewGroup mWholeView;

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DialogStyleMenuOld(Context context, ViewGroup viewGroup, TextEditDialog.ConfigChangeListener configChangeListener) {
        super(context, viewGroup, R.string.text_edit_dialog_style, R.drawable.text_edit_dialog_tab_icon_style_old);
        this.mAlignIndex = 0;
        this.mListener = configChangeListener;
        this.mAlignLeftDrawable = context.getResources().getDrawable(R.drawable.text_edit_dialog_tab_icon_align_left);
        this.mAlignCenterDrawable = context.getResources().getDrawable(R.drawable.text_edit_dialog_tab_icon_align_center);
        this.mAlignRightDrawable = context.getResources().getDrawable(R.drawable.text_edit_dialog_tab_icon_align_right);
        this.mAlignLeftString = context.getString(R.string.text_edit_dialog_style_align_left);
        this.mAlignCenterString = context.getString(R.string.text_edit_dialog_style_align_center);
        this.mAlignRightString = context.getString(R.string.text_edit_dialog_style_align_right);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenuOld
    public ViewGroup initSubMenuView(Context context, ViewGroup viewGroup) {
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_edit_dialog_options_style_panel_old, viewGroup, false);
        this.mWholeView = viewGroup2;
        this.mColorPickSeekBar = (ColorPicker) viewGroup2.findViewById(R.id.text_edit_dialog_tab_style_color_picker);
        this.mTransparentSeekBar = (SeekBar) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_style_transparent);
        this.mShadowButton = (TextView) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_style_shadow);
        this.mAlignButton = (TextView) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_style_align);
        this.mBoldButton = (TextView) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_style_bold);
        initView(context);
        return this.mWholeView;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenuOld
    public void initializeData(DialogStatusData dialogStatusData) {
        this.mColorPickSeekBar.setProgress(this.mColorPickSeekBar.findProgressByColor(dialogStatusData.color));
        int max = this.mTransparentSeekBar.getMax();
        this.mTransparentSeekBar.setProgress(max - ((int) (max * dialogStatusData.transparentProgress)));
        this.mBoldButton.setSelected(dialogStatusData.textBold);
        this.mShadowButton.setSelected(dialogStatusData.textShadow);
        this.mShadowButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogStyleMenuOld.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                DefaultLogger.d("DialogStyleMenu", "onLayoutChange");
                DialogStyleMenuOld.this.mShadowButton.removeOnLayoutChangeListener(this);
                DialogStyleMenuOld.this.mShadowButton.refreshDrawableState();
            }
        });
        setAlignButton(dialogStatusData.textAlignment);
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogStyleMenuOld$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment;

        static {
            int[] iArr = new int[AutoLineLayout.TextAlignment.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment = iArr;
            try {
                iArr[AutoLineLayout.TextAlignment.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[AutoLineLayout.TextAlignment.CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[AutoLineLayout.TextAlignment.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public final void setAlignButton(AutoLineLayout.TextAlignment textAlignment) {
        Drawable drawable;
        String str;
        int i = AnonymousClass2.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[textAlignment.ordinal()];
        if (i == 1) {
            drawable = this.mAlignLeftDrawable;
            str = this.mAlignLeftString;
        } else if (i == 2) {
            drawable = this.mAlignCenterDrawable;
            str = this.mAlignCenterString;
        } else if (i != 3) {
            str = null;
            drawable = null;
        } else {
            str = this.mAlignRightString;
            drawable = this.mAlignRightDrawable;
        }
        this.mAlignButton.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        this.mAlignButton.setText(str);
        this.mAlignIndex = textAlignment.ordinal();
    }

    public final void initView(Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.custom_seekbar_thumb_size);
        this.mColorPickSeekBar.setProgressDrawable(new ColorGradientDrawable(ColorPicker.COLORS));
        ColorPicker colorPicker = this.mColorPickSeekBar;
        colorPicker.setThumbColor(colorPicker.getColor());
        CircleDrawable circleDrawable = new CircleDrawable(context.getResources().getDimensionPixelSize(R.dimen.editor_seek_bar_progress_thumb_start), context.getResources());
        circleDrawable.setIntrinsicWidth(dimensionPixelSize);
        circleDrawable.setIntrinsicHeight(dimensionPixelSize);
        this.mTransparentSeekBar.setThumb(circleDrawable);
        if (this.mTransparentSeekBar.getLayerType() == 0) {
            this.mTransparentSeekBar.setLayerType(2, null);
        }
        setViewClickListener(this.mShadowButton, this.mAlignButton, this.mBoldButton);
        this.mTransparentSeekBar.setOnSeekBarChangeListener(this);
        this.mColorPickSeekBar.setOnSeekBarChangeListener(new BubbleIndicator(View.inflate(context, R.layout.doodle_color_indicator, null), context.getResources().getDimensionPixelSize(R.dimen.photo_editor_bubble_indicator_offset), new BubbleCallback(), new ColorPickerChangeListener()));
    }

    /* loaded from: classes2.dex */
    public class BubbleCallback implements BubbleIndicator.Callback<View> {
        public BubbleCallback() {
        }

        @Override // com.miui.gallery.widget.seekbar.BubbleIndicator.Callback
        public void updateProgress(View view, int i) {
            DefaultLogger.d("DialogStyleMenu", "updateProgress progress: %d color: %s", Integer.valueOf(i), Integer.toHexString(DialogStyleMenuOld.this.mColorPickSeekBar.getColor()));
            ((GradientDrawable) view.getBackground()).setColor(DialogStyleMenuOld.this.mColorPickSeekBar.getColor());
        }
    }

    /* loaded from: classes2.dex */
    public class ColorPickerChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public ColorPickerChangeListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            DialogStyleMenuOld dialogStyleMenuOld = DialogStyleMenuOld.this;
            if (dialogStyleMenuOld.mListener != 0) {
                dialogStyleMenuOld.mColorPickSeekBar.setThumbColor(DialogStyleMenuOld.this.mColorPickSeekBar.getColor());
                DialogStyleMenuOld dialogStyleMenuOld2 = DialogStyleMenuOld.this;
                ((TextEditDialog.ConfigChangeListener) dialogStyleMenuOld2.mListener).onColorChange(dialogStyleMenuOld2.mColorPickSeekBar.getColor());
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            DialogStyleMenuOld dialogStyleMenuOld = DialogStyleMenuOld.this;
            if (dialogStyleMenuOld.mListener != 0) {
                dialogStyleMenuOld.mColorPickSeekBar.setThumbColor(DialogStyleMenuOld.this.mColorPickSeekBar.getColor());
                DialogStyleMenuOld dialogStyleMenuOld2 = DialogStyleMenuOld.this;
                ((TextEditDialog.ConfigChangeListener) dialogStyleMenuOld2.mListener).onColorChange(dialogStyleMenuOld2.mColorPickSeekBar.getColor());
            }
        }
    }

    public final void setViewClickListener(View... viewArr) {
        for (View view : viewArr) {
            view.setOnClickListener(this);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_edit_dialog_tab_style_align /* 2131363536 */:
                AutoLineLayout.TextAlignment[] values = AutoLineLayout.TextAlignment.values();
                int i = this.mAlignIndex + 1;
                this.mAlignIndex = i;
                int length = i % values.length;
                this.mAlignIndex = length;
                AutoLineLayout.TextAlignment textAlignment = values[length];
                setAlignButton(textAlignment);
                T t = this.mListener;
                if (t == 0) {
                    return;
                }
                ((TextEditDialog.ConfigChangeListener) t).onTextAlignChange(textAlignment);
                return;
            case R.id.text_edit_dialog_tab_style_bold /* 2131363537 */:
                boolean isSelected = this.mBoldButton.isSelected();
                this.mBoldButton.setSelected(!isSelected);
                T t2 = this.mListener;
                if (t2 == 0) {
                    return;
                }
                ((TextEditDialog.ConfigChangeListener) t2).onBoldChange(!isSelected);
                return;
            case R.id.text_edit_dialog_tab_style_color_picker /* 2131363538 */:
            case R.id.text_edit_dialog_tab_style_color_picker_title /* 2131363539 */:
            default:
                return;
            case R.id.text_edit_dialog_tab_style_shadow /* 2131363540 */:
                boolean isSelected2 = this.mShadowButton.isSelected();
                this.mShadowButton.setSelected(!isSelected2);
                T t3 = this.mListener;
                if (t3 == 0) {
                    return;
                }
                ((TextEditDialog.ConfigChangeListener) t3).onShadowChange(!isSelected2);
                return;
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        T t;
        if (seekBar.getId() == R.id.text_edit_dialog_tab_style_transparent && (t = this.mListener) != 0) {
            ((TextEditDialog.ConfigChangeListener) t).onTransparentChange(seekBar.getMax() - i);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        T t;
        if (seekBar.getId() == R.id.text_edit_dialog_tab_style_transparent && (t = this.mListener) != 0) {
            ((TextEditDialog.ConfigChangeListener) t).onTransparentChange(seekBar.getMax() - seekBar.getProgress());
        }
    }
}
