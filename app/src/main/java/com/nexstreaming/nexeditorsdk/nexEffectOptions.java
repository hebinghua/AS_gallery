package com.nexstreaming.nexeditorsdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes3.dex */
public final class nexEffectOptions implements Serializable {
    private static final long serialVersionUID = 1;
    public String mEffectID;
    public int mEffectType;
    private boolean updated;
    private List<TextOpt> mTextOptions = new ArrayList();
    private List<TextOpt> m_externalView_texts = null;
    private List<ColorOpt> mColorOptions = new ArrayList();
    private List<ColorOpt> m_externalView_colors = null;
    private List<SelectOpt> mSelectOptions = new ArrayList();
    private List<SelectOpt> m_externalView_selects = null;
    private List<RangeOpt> mRangeOptions = new ArrayList();
    private List<RangeOpt> m_externalView_ranges = null;
    private List<SwitchOpt> mSwitchOptions = new ArrayList();
    private List<SwitchOpt> m_externalView_switch = null;
    private Set<String> mOptionIds = new HashSet();

    private nexEffectOptions() {
    }

    public String getEffectID() {
        return this.mEffectID;
    }

    public nexEffectOptions(String str, int i) {
        this.mEffectID = str;
        this.mEffectType = i;
    }

    public void setEffectType(int i) {
        this.mEffectType = i;
    }

    public List<TextOpt> getTextOptions() {
        if (this.m_externalView_texts == null) {
            this.m_externalView_texts = Collections.unmodifiableList(this.mTextOptions);
        }
        return this.m_externalView_texts;
    }

    public List<ColorOpt> getColorOptions() {
        if (this.m_externalView_colors == null) {
            this.m_externalView_colors = Collections.unmodifiableList(this.mColorOptions);
        }
        return this.m_externalView_colors;
    }

    public List<SelectOpt> getSelectOptions() {
        if (this.m_externalView_selects == null) {
            this.m_externalView_selects = Collections.unmodifiableList(this.mSelectOptions);
        }
        return this.m_externalView_selects;
    }

    public List<RangeOpt> getRangeOptions() {
        if (this.m_externalView_ranges == null) {
            this.m_externalView_ranges = Collections.unmodifiableList(this.mRangeOptions);
        }
        return this.m_externalView_ranges;
    }

    public List<SwitchOpt> getSwitchOptions() {
        if (this.m_externalView_switch == null) {
            this.m_externalView_switch = Collections.unmodifiableList(this.mSwitchOptions);
        }
        return this.m_externalView_switch;
    }

    public String[] getOptionIds() {
        Set<String> set = this.mOptionIds;
        return (String[]) set.toArray(new String[set.size()]);
    }

    public Option getOptionById(String str) {
        for (TextOpt textOpt : this.mTextOptions) {
            if (textOpt.getId().compareTo(str) == 0) {
                return textOpt;
            }
        }
        for (ColorOpt colorOpt : this.mColorOptions) {
            if (colorOpt.getId().compareTo(str) == 0) {
                return colorOpt;
            }
        }
        for (SelectOpt selectOpt : this.mSelectOptions) {
            if (selectOpt.getId().compareTo(str) == 0) {
                return selectOpt;
            }
        }
        for (RangeOpt rangeOpt : this.mRangeOptions) {
            if (rangeOpt.getId().compareTo(str) == 0) {
                return rangeOpt;
            }
        }
        for (SwitchOpt switchOpt : this.mSwitchOptions) {
            if (switchOpt.getId().compareTo(str) == 0) {
                return switchOpt;
            }
        }
        return null;
    }

    public void setDefaultValue() {
        List<ColorOpt> list = this.mColorOptions;
        if (list != null) {
            for (ColorOpt colorOpt : list) {
                colorOpt.argb_color = colorOpt.default_argb_color;
            }
        }
        List<SelectOpt> list2 = this.mSelectOptions;
        if (list2 != null) {
            for (SelectOpt selectOpt : list2) {
                selectOpt.setSelectIndex(selectOpt.default_select_index);
            }
        }
        List<RangeOpt> list3 = this.mRangeOptions;
        if (list3 != null) {
            for (RangeOpt rangeOpt : list3) {
                rangeOpt.setValue(rangeOpt.default_value);
            }
        }
        List<SwitchOpt> list4 = this.mSwitchOptions;
        if (list4 != null) {
            for (SwitchOpt switchOpt : list4) {
                switchOpt.setValue(switchOpt.default_on);
            }
        }
    }

    public void addTextOpt(String str, String str2, int i) {
        this.mOptionIds.add(str);
        this.mTextOptions.add(new TextOpt(str, str2, i));
    }

    public void addColorOpt(String str, String str2, String str3) {
        this.mOptionIds.add(str);
        this.mColorOptions.add(new ColorOpt(str, str2, str3));
    }

    public void addSelectOpt(String str, String str2, String[] strArr, String[] strArr2, int i) {
        this.mOptionIds.add(str);
        this.mSelectOptions.add(new SelectOpt(str, str2, strArr, strArr2, i));
    }

    public void addRangeOpt(String str, String str2, int i, int i2, int i3) {
        this.mOptionIds.add(str);
        this.mRangeOptions.add(new RangeOpt(str, str2, i, i2, i3));
    }

    public void addSwitchOpt(String str, String str2, boolean z) {
        this.mOptionIds.add(str);
        this.mSwitchOptions.add(new SwitchOpt(str, str2, z));
    }

    public int getTextFieldCount() {
        List<TextOpt> list = this.mTextOptions;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /* loaded from: classes3.dex */
    public class Option implements Serializable {
        private static final long serialVersionUID = 1;
        private String mId;
        private String mLabel;

        private Option() {
        }

        public Option(String str, String str2) {
            this.mLabel = str2;
            this.mId = str;
        }

        public String getId() {
            return this.mId;
        }

        public String getLabel() {
            return this.mLabel;
        }
    }

    /* loaded from: classes3.dex */
    public class TextOpt extends Option {
        private static final long serialVersionUID = 1;
        private boolean isMultiLines;
        private String mTitle;

        private TextOpt() {
            super();
        }

        public TextOpt(String str, String str2, int i) {
            super(str, str2);
            if (i == 0) {
                this.isMultiLines = false;
            } else {
                this.isMultiLines = true;
            }
        }

        public void setText(String str) {
            this.mTitle = str;
            nexEffectOptions.this.updated = true;
        }

        public String getText() {
            return this.mTitle;
        }
    }

    /* loaded from: classes3.dex */
    public class ColorOpt extends Option {
        public int argb_color;
        public int default_argb_color;

        private ColorOpt() {
            super();
        }

        public ColorOpt(String str, String str2, String str3) {
            super(str, str2);
            int a = com.nexstreaming.app.common.util.c.a(str3);
            this.argb_color = a;
            this.default_argb_color = a;
        }

        public int getARGBformat() {
            return this.argb_color;
        }

        public void setARGBColor(int i) {
            if (this.argb_color != i) {
                nexEffectOptions.this.updated = true;
            }
            this.argb_color = i;
        }
    }

    /* loaded from: classes3.dex */
    public class SelectOpt extends Option {
        private static final long serialVersionUID = 1;
        public int default_select_index;
        private String[] mItems;
        private String[] mValues;
        public int select_index;

        private SelectOpt() {
            super();
        }

        public SelectOpt(String str, String str2, String[] strArr, String[] strArr2, int i) {
            super(str, str2);
            this.mItems = strArr;
            this.mValues = strArr2;
            this.select_index = i;
            this.default_select_index = i;
        }

        public int setValue(String str) {
            int i = 0;
            while (true) {
                String[] strArr = this.mValues;
                if (i >= strArr.length) {
                    break;
                } else if (strArr[i].compareTo(str) == 0) {
                    if (this.select_index != i) {
                        nexEffectOptions.this.updated = true;
                    }
                    this.select_index = i;
                } else {
                    i++;
                }
            }
            return 0;
        }

        public int getSelectIndex() {
            return this.select_index;
        }

        public void setSelectIndex(int i) {
            String[] strArr;
            if (i >= 0 && (strArr = this.mItems) != null && i < strArr.length) {
                this.select_index = i;
            }
        }

        public String[] getItems() {
            return this.mItems;
        }

        public String getSelectValue() {
            return this.mValues[this.select_index];
        }
    }

    /* loaded from: classes3.dex */
    public class RangeOpt extends Option {
        private static final long serialVersionUID = 1;
        public int default_value;
        public int mValue;
        public int max_value;
        public int min_value;

        private RangeOpt() {
            super();
        }

        public RangeOpt(String str, String str2, int i, int i2, int i3) {
            super(str, str2);
            this.default_value = i;
            this.max_value = i3;
            this.min_value = i2;
            this.mValue = i;
        }

        public void setValue(int i) {
            int i2 = this.min_value;
            if (i < i2 || i > (i2 = this.max_value)) {
                i = i2;
            }
            if (this.mValue != i) {
                nexEffectOptions.this.updated = true;
            }
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public int min() {
            return this.min_value;
        }

        public int max() {
            return this.max_value;
        }
    }

    /* loaded from: classes3.dex */
    public class SwitchOpt extends Option {
        public boolean default_on;
        public boolean on;

        private SwitchOpt() {
            super();
        }

        public SwitchOpt(String str, String str2, boolean z) {
            super(str, str2);
            this.default_on = z;
            this.on = z;
        }

        public boolean getValue() {
            return this.on;
        }

        public void setValue(boolean z) {
            if (this.on != z) {
                nexEffectOptions.this.updated = true;
            }
            this.on = z;
        }
    }

    public boolean isUpdated() {
        return this.updated;
    }

    public void clearUpadted() {
        this.updated = false;
    }
}
