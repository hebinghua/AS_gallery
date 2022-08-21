package com.miui.gallery.editor.photo.core.imports.text.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class AutoLineLayout {
    public Callbacks mCallbacks;
    public boolean mIsSingleVerticalText;
    public boolean mIsStroke;
    public int mMaxLines;
    public String mOriginText;
    public final TextPaint mPaint;
    public TextPaint mStrokePaint;
    public String mText;
    public Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
    public float mLineHeight = 0.0f;
    public RectF mBound = new RectF();
    public MultipleLineText mMultipleLineText = new MultipleLineText(null);
    public TextAlignment mTextAlignment = TextAlignment.LEFT;
    public float mLineHeightOffset = 0.0f;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        Shader getShader(float f);
    }

    /* loaded from: classes2.dex */
    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public AutoLineLayout() {
        TextPaint textPaint = new TextPaint(1);
        this.mPaint = textPaint;
        textPaint.setColor(-1);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate((-this.mBound.width()) / 2.0f, (-this.mBound.height()) / 2.0f);
        float f = 0.0f;
        for (SingleLineText singleLineText : this.mMultipleLineText.mSingleLineTextList) {
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[this.mTextAlignment.ordinal()];
            if (i == 1) {
                f = (this.mMultipleLineText.mWidth - singleLineText.mWidth) / 2.0f;
            } else if (i == 2) {
                f = this.mMultipleLineText.mWidth - singleLineText.mWidth;
            } else if (i == 3) {
                f = 0.0f;
            }
            canvas.translate(f, 0.0f);
            singleLineText.draw(canvas);
            canvas.translate(-f, 0.0f);
            canvas.translate(0.0f, this.mLineHeight);
        }
        canvas.restore();
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment;

        static {
            int[] iArr = new int[TextAlignment.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment = iArr;
            try {
                iArr[TextAlignment.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[TextAlignment.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$utils$AutoLineLayout$TextAlignment[TextAlignment.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public class SingleLineText {
        public StringBuilder mBuilder;
        public float mWidth;

        public SingleLineText() {
            this.mBuilder = new StringBuilder("");
            this.mWidth = 0.0f;
        }

        public /* synthetic */ SingleLineText(AutoLineLayout autoLineLayout, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void count() {
            this.mWidth = AutoLineLayout.this.mPaint.measureText(this.mBuilder.toString());
        }

        public void draw(Canvas canvas) {
            if (AutoLineLayout.this.mIsStroke) {
                AutoLineLayout.this.mStrokePaint.setTextSize(AutoLineLayout.this.getPaint().getTextSize());
                StringBuilder sb = this.mBuilder;
                canvas.drawText(sb, 0, sb.length(), 0.0f, -AutoLineLayout.this.mFontMetricsInt.ascent, AutoLineLayout.this.mStrokePaint);
            }
            if (Objects.nonNull(AutoLineLayout.this.mCallbacks)) {
                AutoLineLayout.this.mPaint.setShader(AutoLineLayout.this.mCallbacks.getShader(this.mWidth));
            }
            StringBuilder sb2 = this.mBuilder;
            canvas.drawText(sb2, 0, sb2.length(), 0.0f, -AutoLineLayout.this.mFontMetricsInt.ascent, AutoLineLayout.this.mPaint);
        }
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    /* loaded from: classes2.dex */
    public static class MultipleLineText {
        public float mHeight;
        public String mLongestText;
        public List<SingleLineText> mSingleLineTextList;
        public float mWidth;

        public MultipleLineText() {
            this.mSingleLineTextList = new ArrayList();
            this.mLongestText = "";
            this.mHeight = 0.0f;
            this.mWidth = 0.0f;
        }

        public /* synthetic */ MultipleLineText(AnonymousClass1 anonymousClass1) {
            this();
        }

        public final void reset() {
            this.mSingleLineTextList.clear();
            this.mHeight = 0.0f;
            this.mWidth = 0.0f;
        }
    }

    public void countText() {
        cutText();
        refreshRect();
    }

    public void countText(float f, float f2) {
        MultipleLineText multipleLineText;
        this.mPaint.getFontMetricsInt(this.mFontMetricsInt);
        this.mLineHeight = getLineHeight();
        this.mMultipleLineText.reset();
        if (f == 0.0f || f2 == 0.0f) {
            return;
        }
        if (f < 0.0f) {
            f = Float.MAX_VALUE;
        }
        int i = f2 < 0.0f ? Integer.MAX_VALUE : (int) (f2 / this.mLineHeight);
        if (i < 1) {
            i = 1;
        }
        int i2 = this.mMaxLines;
        if (i2 > 0 && i > i2) {
            i = i2;
        }
        List list = this.mMultipleLineText.mSingleLineTextList;
        list.add(new SingleLineText(this, null));
        int i3 = 0;
        while (i3 < this.mText.length() && list.size() <= i) {
            String str = this.mText;
            int indexOf = TextUtils.indexOf((CharSequence) str, '\n', i3, str.length());
            SingleLineText singleLineText = (SingleLineText) list.get(list.size() - 1);
            StringBuilder sb = singleLineText.mBuilder;
            String str2 = this.mText;
            sb.append(str2.substring(i3, indexOf == -1 ? str2.length() : indexOf));
            singleLineText.count();
            if (singleLineText.mWidth > f) {
                list.remove(list.size() - 1);
                list.addAll(cutSingleLineTextByWidth(singleLineText, f, i - list.size()));
            }
            if (indexOf == -1) {
                break;
            }
            list.add(new SingleLineText(this, null));
            i3 = indexOf + 1;
        }
        while (list.size() > i) {
            list.remove(this.mMultipleLineText.mSingleLineTextList.size() - 1);
        }
        for (SingleLineText singleLineText2 : this.mMultipleLineText.mSingleLineTextList) {
            if (singleLineText2.mWidth > this.mMultipleLineText.mWidth) {
                this.mMultipleLineText.mWidth = singleLineText2.mWidth;
            }
        }
        this.mMultipleLineText.mHeight = this.mLineHeight * multipleLineText.mSingleLineTextList.size();
        refreshRect();
    }

    public TextPaint getStrokePaint() {
        if (this.mStrokePaint == null) {
            this.mStrokePaint = new TextPaint(1);
        }
        return this.mStrokePaint;
    }

    public final void refreshRect() {
        float f = this.mMultipleLineText.mWidth / 2.0f;
        float f2 = this.mMultipleLineText.mHeight / 2.0f;
        this.mLineHeight = getLineHeight();
        this.mBound.set(-f, -f2, f, f2);
    }

    public void countTextArea(float f, float f2, float f3, float f4, float f5) {
        DefaultLogger.d("AutoLineLayout", "--- begin count text in a area ---");
        this.mMultipleLineText.reset();
        this.mBound.setEmpty();
        if (TextUtils.isEmpty(this.mText)) {
            return;
        }
        cutText();
        long currentTimeMillis = System.currentTimeMillis();
        findSuitableTextSize(f, f2, f3, f4, f5);
        countText(f, f2);
        findSuitableTextSize(f, f2, f3, f4, f5);
        long currentTimeMillis2 = System.currentTimeMillis();
        DefaultLogger.d("AutoLineLayout", "text size coast time：%d", Long.valueOf(currentTimeMillis2 - currentTimeMillis));
        countText(f, f2);
        DefaultLogger.d("AutoLineLayout", "text resize coast time：%d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
        DefaultLogger.d("AutoLineLayout", "--- end count text in a area ---");
    }

    public final void findSuitableTextSize(float f, float f2, float f3, float f4, float f5) {
        float textSize = this.mPaint.getTextSize();
        float f6 = this.mMultipleLineText.mHeight;
        float f7 = this.mMultipleLineText.mWidth;
        float f8 = textSize;
        while (f7 < f && f6 < f2 && f8 <= f4) {
            f8 += f5;
            this.mPaint.setTextSize(f8);
            f6 = this.mPaint.getFontSpacing() * this.mMultipleLineText.mSingleLineTextList.size();
            f7 = this.mMultipleLineText.mWidth * (f8 / textSize);
        }
        while (true) {
            if ((f7 > f || f6 > f2) && f8 >= f3) {
                f8 -= f5;
                this.mPaint.setTextSize(f8);
                f6 = this.mPaint.getFontSpacing() * this.mMultipleLineText.mSingleLineTextList.size();
                f7 = this.mMultipleLineText.mWidth * (f8 / textSize);
            }
        }
        float measureText = this.mPaint.measureText(this.mMultipleLineText.mLongestText);
        while (measureText < f && f6 < f2 && f8 <= f4) {
            f8 += f5;
            this.mPaint.setTextSize(f8);
            f6 = this.mMultipleLineText.mSingleLineTextList.size() * this.mPaint.getFontSpacing();
            measureText = this.mPaint.measureText(this.mMultipleLineText.mLongestText);
        }
        while (true) {
            if ((measureText > f || f6 > f2) && f8 >= f3) {
                f8 -= f5;
                this.mPaint.setTextSize(f8);
                f6 = this.mPaint.getFontSpacing() * this.mMultipleLineText.mSingleLineTextList.size();
                measureText = this.mPaint.measureText(this.mMultipleLineText.mLongestText);
            }
        }
        this.mMultipleLineText.mWidth = measureText;
        this.mMultipleLineText.mHeight = f6;
        refreshRect();
    }

    public final List<SingleLineText> cutSingleLineTextByWidth(SingleLineText singleLineText, float f, int i) {
        ArrayList arrayList = new ArrayList();
        if (i <= 0) {
            return arrayList;
        }
        char[] charArray = singleLineText.mBuilder.toString().toCharArray();
        SingleLineText singleLineText2 = new SingleLineText(this, null);
        int i2 = 0;
        int i3 = 0;
        while (i2 <= charArray.length) {
            int i4 = i2 - i3;
            if (this.mPaint.measureText(charArray, i3, i4) > f) {
                i2--;
                if (i2 == i3) {
                    i2++;
                }
                singleLineText2.mBuilder.append(charArray, i3, i2 - i3);
                singleLineText2.count();
                arrayList.add(singleLineText2);
                if (arrayList.size() >= i) {
                    return arrayList;
                }
                singleLineText2 = new SingleLineText(this, null);
                i3 = i2;
            } else if (i2 == charArray.length) {
                singleLineText2.mBuilder.append(charArray, i3, i4);
                singleLineText2.count();
                arrayList.add(singleLineText2);
                if (arrayList.size() >= i) {
                    return arrayList;
                }
            } else {
                continue;
            }
            i2++;
        }
        return arrayList;
    }

    public final void cutText() {
        long currentTimeMillis = System.currentTimeMillis();
        this.mPaint.getFontMetricsInt(this.mFontMetricsInt);
        this.mLineHeight = getLineHeight();
        this.mMultipleLineText.reset();
        if (TextUtils.isEmpty(this.mText)) {
            return;
        }
        List list = this.mMultipleLineText.mSingleLineTextList;
        list.add(new SingleLineText(this, null));
        int i = 0;
        while (i < this.mText.length()) {
            String str = this.mText;
            int indexOf = TextUtils.indexOf((CharSequence) str, '\n', i, str.length());
            SingleLineText singleLineText = (SingleLineText) list.get(list.size() - 1);
            StringBuilder sb = singleLineText.mBuilder;
            String str2 = this.mText;
            sb.append(str2.substring(i, indexOf == -1 ? str2.length() : indexOf));
            singleLineText.count();
            if (singleLineText.mWidth > this.mMultipleLineText.mWidth) {
                this.mMultipleLineText.mWidth = singleLineText.mWidth;
                this.mMultipleLineText.mLongestText = singleLineText.mBuilder.toString();
            }
            if (indexOf == -1) {
                break;
            }
            list.add(new SingleLineText(this, null));
            i = indexOf + 1;
        }
        MultipleLineText multipleLineText = this.mMultipleLineText;
        multipleLineText.mHeight = multipleLineText.mSingleLineTextList.size() * this.mLineHeight;
        DefaultLogger.d("AutoLineLayout", "cut text coast time： %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public float getLineHeight() {
        return this.mPaint.getFontSpacing() + this.mLineHeightOffset;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setText(String str) {
        this.mOriginText = str;
        this.mText = str;
        if (this.mIsSingleVerticalText) {
            this.mText = getSingleLineText(str);
        }
    }

    public void getTextRect(RectF rectF) {
        rectF.set(this.mBound);
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.mTextAlignment = textAlignment;
    }

    public TextAlignment getTextAlignment() {
        return this.mTextAlignment;
    }

    public void setLetterSpace(float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mPaint.setLetterSpacing(f);
        }
    }

    public String getText() {
        return this.mOriginText;
    }

    public void setLineHeightOffset(float f) {
        this.mLineHeightOffset = f;
    }

    public void setSingleVerticalText(boolean z) {
        this.mIsSingleVerticalText = z;
    }

    public static String getSingleLineText(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt != '\n') {
                sb.append(charAt);
                if (i < str.length() - 1) {
                    sb.append('\n');
                }
            }
        }
        return sb.toString();
    }

    public void setMaxLines(int i) {
        this.mMaxLines = i;
    }

    public boolean isStroke() {
        return this.mIsStroke;
    }

    public void setIsStroke(boolean z) {
        this.mIsStroke = z;
    }
}
