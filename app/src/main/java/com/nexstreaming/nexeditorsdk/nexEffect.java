package com.nexstreaming.nexeditorsdk;

import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemParameterType;
import com.nexstreaming.app.common.nexasset.assetpackage.h;
import com.nexstreaming.app.common.nexasset.assetpackage.i;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexEffectOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public abstract class nexEffect {
    public static final int kEFFECT_CLIP_AUTO = 1;
    public static final int kEFFECT_CLIP_USER = 2;
    public static final int kEFFECT_NONE = 0;
    public static final int kEFFECT_OVERLAY_FILTER = 5;
    public static final int kEFFECT_TRANSITION_AUTO = 3;
    public static final int kEFFECT_TRANSITION_USER = 4;
    private static final int kMaxStringTrackCount = 8;
    public String mAutoID;
    public int mDuration;
    public String mID;
    public String mName;
    private b mObserver;
    public boolean mOptionsUpdate;
    public boolean mUpdated;
    public HashMap<String, String> m_effectOptions;
    public int mType = 0;
    public nexAssetPackageManager.ItemMethodType itemMethodType = nexAssetPackageManager.ItemMethodType.ItemExtra;
    private String[] mTitles = null;
    public boolean mIsResolveOptions = false;

    public boolean isValidId(String str) {
        return true;
    }

    public int getType() {
        return this.mType;
    }

    public String getId() {
        int i = this.mType;
        if (i == 0) {
            return "none";
        }
        if (i == 1 || i == 3) {
            String str = this.mAutoID;
            if (str != null) {
                return str;
            }
            Log.d("nexEffect", "mType=" + this.mType + " mAutoID=null");
            return "none";
        }
        String str2 = this.mID;
        if (str2 != null) {
            return str2;
        }
        Log.d("nexEffect", "mType=" + this.mType + " mID=null");
        return "none";
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int i) {
        if (this.mDuration != i) {
            this.mUpdated = true;
            setModified(false);
        }
        this.mDuration = i;
    }

    public void setEffectNone() {
        if (this.mType != 0) {
            this.mUpdated = true;
            this.mOptionsUpdate = false;
            setModified(false);
            resetOptions();
            this.mIsResolveOptions = false;
        }
        this.mType = 0;
    }

    public boolean setEffect(String str, int i) {
        this.mType = i;
        String str2 = this.mID;
        if ((str2 == null || str.compareTo(str2) != 0) && isValidId(str)) {
            this.mUpdated = true;
            this.mID = str;
            this.mOptionsUpdate = false;
            resetOptions();
            this.mIsResolveOptions = false;
            return true;
        }
        return false;
    }

    public void setModified(boolean z) {
        b bVar = this.mObserver;
        if (bVar != null) {
            bVar.updateTimeLine(z);
        }
    }

    public void setObserver(b bVar) {
        this.mObserver = bVar;
    }

    public void setTitle(int i, String str) {
        if (this.mTitles == null) {
            this.mTitles = new String[8];
        }
        if (i < 8) {
            this.mTitles[i] = str;
        }
    }

    public String getTitle(int i) {
        String[] strArr = this.mTitles;
        if (strArr != null && i < 8) {
            return strArr[i];
        }
        return null;
    }

    public String[] getTitles() {
        return this.mTitles;
    }

    private boolean isAllEmptyTitle() {
        if (this.mTitles == null) {
            return true;
        }
        for (int i = 0; i < 8; i++) {
            if (this.mTitles[i] != null) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01d9 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void encodeEffectOptions(java.lang.StringBuilder r9, com.nexstreaming.nexeditorsdk.nexEffectOptions r10) {
        /*
            Method dump skipped, instructions count: 474
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexEffect.encodeEffectOptions(java.lang.StringBuilder, com.nexstreaming.nexeditorsdk.nexEffectOptions):void");
    }

    public static String getTitleOptions(nexEffectOptions nexeffectoptions) {
        StringBuilder sb = new StringBuilder();
        if (nexeffectoptions.mEffectType == 5) {
            encodeEffectOptions(sb, nexeffectoptions);
        } else {
            sb.append(0);
            sb.append(CoreConstants.COMMA_CHAR);
            sb.append(10000);
            sb.append('?');
            int i = nexeffectoptions.mEffectType;
            if (i == 4 || i == 3) {
                encodeEffectOptions(sb, nexeffectoptions);
            }
            sb.append('?');
            int i2 = nexeffectoptions.mEffectType;
            if (i2 == 2 || i2 == 1) {
                encodeEffectOptions(sb, nexeffectoptions);
            }
        }
        return sb.toString();
    }

    private void resetOptions() {
        HashMap<String, String> hashMap = this.m_effectOptions;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public boolean updateEffectOptions(nexEffectOptions nexeffectoptions, boolean z) {
        if (this.m_effectOptions == null) {
            this.m_effectOptions = new HashMap<>();
        }
        if (nexeffectoptions.mEffectID.compareTo(this.mID) == 0) {
            this.mOptionsUpdate = true;
            if (z) {
                this.m_effectOptions.clear();
            }
            List<nexEffectOptions.TextOpt> textOptions = nexeffectoptions.getTextOptions();
            if (textOptions != null) {
                int i = 0;
                for (nexEffectOptions.TextOpt textOpt : textOptions) {
                    if (z) {
                        if (textOpt.getText() != null) {
                            setTitle(i, textOpt.getText());
                            this.m_effectOptions.put(textOpt.getId(), getTitle(i));
                        }
                    } else if (getTitle(i) != null) {
                        textOpt.setText(getTitle(i));
                    }
                    i++;
                }
            }
            List<nexEffectOptions.ColorOpt> colorOptions = nexeffectoptions.getColorOptions();
            if (colorOptions != null) {
                for (nexEffectOptions.ColorOpt colorOpt : colorOptions) {
                    if (z) {
                        if (colorOpt.default_argb_color != colorOpt.argb_color) {
                            this.m_effectOptions.put(colorOpt.getId(), com.nexstreaming.app.common.util.c.a(colorOpt.getARGBformat()));
                        }
                    } else {
                        String str = this.m_effectOptions.get(colorOpt.getId());
                        if (str != null) {
                            colorOpt.argb_color = com.nexstreaming.app.common.util.c.a(str);
                        }
                    }
                }
            }
            List<nexEffectOptions.SelectOpt> selectOptions = nexeffectoptions.getSelectOptions();
            if (selectOptions != null) {
                for (nexEffectOptions.SelectOpt selectOpt : selectOptions) {
                    if (z) {
                        if (selectOpt.default_select_index != selectOpt.select_index) {
                            this.m_effectOptions.put(selectOpt.getId(), selectOpt.getSelectValue());
                        }
                    } else {
                        String str2 = this.m_effectOptions.get(selectOpt.getId());
                        if (str2 != null) {
                            selectOpt.setValue(str2);
                        }
                    }
                }
            }
            List<nexEffectOptions.RangeOpt> rangeOptions = nexeffectoptions.getRangeOptions();
            if (rangeOptions != null) {
                for (nexEffectOptions.RangeOpt rangeOpt : rangeOptions) {
                    if (z) {
                        if (rangeOpt.default_value != rangeOpt.mValue) {
                            this.m_effectOptions.put(rangeOpt.getId(), "" + rangeOpt.getValue());
                        }
                    } else {
                        rangeOpt.setValue(Integer.parseInt(this.m_effectOptions.get(rangeOpt.getId())));
                    }
                }
            }
            List<nexEffectOptions.SwitchOpt> switchOptions = nexeffectoptions.getSwitchOptions();
            if (switchOptions != null) {
                for (nexEffectOptions.SwitchOpt switchOpt : switchOptions) {
                    if (z) {
                        boolean z2 = switchOpt.default_on;
                        boolean z3 = switchOpt.on;
                        if (z2 != z3) {
                            if (z3) {
                                this.m_effectOptions.put(switchOpt.getId(), "on");
                            } else {
                                this.m_effectOptions.put(switchOpt.getId(), "off");
                            }
                        }
                    } else if (this.m_effectOptions.get(switchOpt.getId()).compareTo("on") == 0) {
                        switchOpt.setValue(true);
                    } else {
                        switchOpt.setValue(false);
                    }
                }
            }
            this.mIsResolveOptions = true;
            return true;
        }
        return false;
    }

    public Map<String, String> getEffectOptions(String str) {
        if (this.m_effectOptions == null) {
            this.m_effectOptions = new HashMap<>();
        }
        int i = this.mType;
        if (i == 1 || i == 3) {
            this.m_effectOptions.clear();
        }
        if (str == null && isAllEmptyTitle()) {
            return this.m_effectOptions;
        }
        h hVar = null;
        try {
            hVar = i.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), getId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        if (hVar != null) {
            int i2 = 0;
            for (com.nexstreaming.app.common.nexasset.assetpackage.g gVar : hVar.a()) {
                if (gVar.a() == ItemParameterType.TEXT) {
                    if (str != null) {
                        this.m_effectOptions.put(gVar.e(), str);
                    } else {
                        if (getTitle(i2) != null) {
                            this.m_effectOptions.put(gVar.e(), getTitle(i2));
                        }
                        i2++;
                    }
                }
            }
        }
        return this.m_effectOptions;
    }

    public int getTitleCount() {
        int i = 0;
        if (this.mTitles == null) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            String[] strArr = this.mTitles;
            if (i >= strArr.length) {
                return i2;
            }
            if (strArr[i] != null && !strArr[i].isEmpty()) {
                i2++;
            }
            i++;
        }
    }

    public nexAssetPackageManager.ItemMethodType getMethodType() {
        if (this.itemMethodType == nexAssetPackageManager.ItemMethodType.ItemExtra) {
            if (this.mID.compareToIgnoreCase("none") == 0) {
                this.itemMethodType = nexAssetPackageManager.ItemMethodType.ItemKedl;
            } else {
                com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID);
                if (c != null) {
                    this.itemMethodType = nexAssetPackageManager.getMethodType(c.getType());
                }
            }
        }
        return this.itemMethodType;
    }
}
