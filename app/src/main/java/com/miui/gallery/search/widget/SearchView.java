package com.miui.gallery.search.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import miuix.androidbasewidget.widget.ClearableEditText;

/* loaded from: classes2.dex */
public class SearchView extends LinearLayout implements View.OnClickListener {
    public View mCancelBtn;
    public RelativeLayout mContainer;
    public ClearableEditText mEditText;
    public SearchViewListener mSearchViewListener;
    public View mVoiceButton;
    public boolean mVoiceButtonEnabled;

    /* loaded from: classes2.dex */
    public interface SearchViewListener {
        void onCancelSearch(View view);

        void onFocusChanged(View view, boolean z);

        void onQueryTextChanged(View view, String str);

        void onQueryTextSubmit(View view, String str);

        void onStartVoiceAssistant(View view);
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mVoiceButtonEnabled = false;
        LayoutInflater.from(context).inflate(R.layout.search_view_layout, (ViewGroup) this, true);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        this.mContainer = (RelativeLayout) findViewById(R.id.search_bar_container);
        ClearableEditText clearableEditText = (ClearableEditText) findViewById(R.id.search_view_input);
        this.mEditText = clearableEditText;
        clearableEditText.setContentDescription(getResources().getString(R.string.talkback_input_search_hint));
        this.mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.miui.gallery.search.widget.SearchView.1
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (SearchView.this.mSearchViewListener != null) {
                    SearchView.this.mSearchViewListener.onFocusChanged(SearchView.this, z);
                }
            }
        });
        this.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.miui.gallery.search.widget.SearchView.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                if (i2 == 0 || i2 == 3) {
                    if (!TextUtils.isEmpty(SearchView.this.getQueryText())) {
                        if (SearchView.this.mSearchViewListener == null) {
                            return false;
                        }
                        SearchViewListener searchViewListener = SearchView.this.mSearchViewListener;
                        SearchView searchView = SearchView.this;
                        searchViewListener.onQueryTextSubmit(searchView, searchView.getQueryText());
                        return true;
                    }
                    ToastUtils.makeText(SearchView.this.getContext(), (int) R.string.empty_query_text_msg);
                    return false;
                }
                return false;
            }
        });
        this.mEditText.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.search.widget.SearchView.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (SearchView.this.mSearchViewListener != null) {
                    SearchViewListener searchViewListener = SearchView.this.mSearchViewListener;
                    SearchView searchView = SearchView.this;
                    searchViewListener.onQueryTextChanged(searchView, searchView.getQueryText());
                }
                SearchView.this.updateVoiceButtonState();
            }
        });
        View findViewById = findViewById(R.id.search_btn_cancel);
        this.mCancelBtn = findViewById;
        findViewById.setOnClickListener(this);
        View findViewById2 = findViewById(R.id.search_btn_voice);
        this.mVoiceButton = findViewById2;
        findViewById2.setOnClickListener(this);
        updateVoiceButtonState();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == null || this.mSearchViewListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.search_btn_cancel /* 2131363290 */:
                this.mSearchViewListener.onCancelSearch(this);
                return;
            case R.id.search_btn_voice /* 2131363291 */:
                this.mSearchViewListener.onStartVoiceAssistant(this);
                return;
            default:
                return;
        }
    }

    public void setSearchViewListener(SearchViewListener searchViewListener) {
        this.mSearchViewListener = searchViewListener;
    }

    public void setCursorVisible(boolean z) {
        this.mEditText.setCursorVisible(z);
    }

    public void setHint(String str) {
        this.mEditText.setHint(str);
    }

    public void setQueryText(String str) {
        this.mEditText.setText(str);
    }

    public String getQueryText() {
        return this.mEditText.mo49getText().toString();
    }

    public void selectAll(boolean z) {
        if (z) {
            this.mEditText.selectAll();
        } else {
            this.mEditText.setSelection(getQueryText() != null ? getQueryText().length() : 0);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void clearFocus() {
        super.clearFocus();
        hideInputMethod();
        this.mEditText.clearFocus();
    }

    public boolean showInputMethod() {
        InputMethodManager inputMethodManager = getInputMethodManager();
        if (inputMethodManager != null) {
            this.mEditText.requestFocus();
            return inputMethodManager.showSoftInput(this.mEditText, 0);
        }
        return false;
    }

    public boolean hideInputMethod() {
        InputMethodManager inputMethodManager = getInputMethodManager();
        return inputMethodManager != null && inputMethodManager.hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
    }

    public void setVoiceButtonEnabled(boolean z) {
        this.mVoiceButtonEnabled = z;
        updateVoiceButtonState();
    }

    public final void updateVoiceButtonState() {
        if (this.mVoiceButtonEnabled && TextUtils.isEmpty(this.mEditText.mo49getText().toString())) {
            this.mVoiceButton.setVisibility(0);
        } else {
            this.mVoiceButton.setVisibility(8);
        }
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext().getSystemService("input_method");
    }

    public void config() {
        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.search_bar_margin_start));
        layoutParams.setMarginEnd(getResources().getDimensionPixelOffset(R.dimen.search_bar_margin_end));
        this.mContainer.setLayoutParams(layoutParams);
        this.mEditText.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelOffset(R.dimen.search_bar_height)));
    }
}
