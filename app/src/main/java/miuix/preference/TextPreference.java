package miuix.preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/* loaded from: classes3.dex */
public class TextPreference extends Preference {
    public CharSequence mText;
    public TextProvider mTextProvider;
    public int mTextRes;

    /* loaded from: classes3.dex */
    public interface TextProvider<T extends TextPreference> {
        CharSequence provideText(T t);
    }

    public TextPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TextPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.textPreferenceStyle);
    }

    public TextPreference(Context context) {
        this(context, null);
    }

    public final TextProvider getTextProvider() {
        return this.mTextProvider;
    }

    public void setText(String str) {
        if (getTextProvider() != null) {
            throw new IllegalStateException("Preference already has a TextProvider set.");
        }
        if (TextUtils.equals(str, this.mText)) {
            return;
        }
        this.mTextRes = 0;
        this.mText = str;
        notifyChanged();
    }

    public void setText(int i) {
        setText(getContext().getString(i));
        this.mTextRes = i;
    }

    public CharSequence getText() {
        if (getTextProvider() != null) {
            return getTextProvider().provideText(this);
        }
        return this.mText;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R$id.text_right);
        if (textView != null) {
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                textView.setText(text);
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
        }
    }
}
