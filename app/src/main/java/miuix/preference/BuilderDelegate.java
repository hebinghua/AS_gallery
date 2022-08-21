package miuix.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListAdapter;
import androidx.appcompat.app.AlertDialog;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes3.dex */
public class BuilderDelegate extends AlertDialog.Builder {
    public AlertDialog.Builder mMiuixBuilder;

    public BuilderDelegate(Context context, AlertDialog.Builder builder) {
        this(context, 0, builder);
    }

    public BuilderDelegate(Context context, int i, AlertDialog.Builder builder) {
        super(context, i);
        this.mMiuixBuilder = builder;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setTitle(CharSequence charSequence) {
        this.mMiuixBuilder.setTitle(charSequence);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setCustomTitle(View view) {
        this.mMiuixBuilder.setCustomTitle(view);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setMessage(CharSequence charSequence) {
        this.mMiuixBuilder.setMessage(charSequence);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setIcon(Drawable drawable) {
        this.mMiuixBuilder.setIcon(drawable);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mMiuixBuilder.setPositiveButton(charSequence, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mMiuixBuilder.setNegativeButton(charSequence, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.mMiuixBuilder.setOnKeyListener(onKeyListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
        this.mMiuixBuilder.setAdapter(listAdapter, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
        this.mMiuixBuilder.setMultiChoiceItems(charSequenceArr, zArr, onMultiChoiceClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, DialogInterface.OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(charSequenceArr, i, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setSingleChoiceItems(ListAdapter listAdapter, int i, DialogInterface.OnClickListener onClickListener) {
        this.mMiuixBuilder.setSingleChoiceItems(listAdapter, i, onClickListener);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.Builder
    public AlertDialog.Builder setView(View view) {
        this.mMiuixBuilder.setView(view);
        return this;
    }
}
