package com.airbnb.lottie.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.model.MutablePair;
import com.airbnb.lottie.utils.Logger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class FontAssetManager {
    public final AssetManager assetManager;
    public final MutablePair<String> tempPair = new MutablePair<>();
    public final Map<MutablePair<String>, Typeface> fontMap = new HashMap();
    public final Map<String, Typeface> fontFamilies = new HashMap();
    public String defaultFontFileExtension = ".ttf";

    public void setDelegate(FontAssetDelegate fontAssetDelegate) {
    }

    public FontAssetManager(Drawable.Callback callback, FontAssetDelegate fontAssetDelegate) {
        if (!(callback instanceof View)) {
            Logger.warning("LottieDrawable must be inside of a view for images to work.");
            this.assetManager = null;
            return;
        }
        this.assetManager = ((View) callback).getContext().getAssets();
    }

    public Typeface getTypeface(String str, String str2) {
        this.tempPair.set(str, str2);
        Typeface typeface = this.fontMap.get(this.tempPair);
        if (typeface != null) {
            return typeface;
        }
        Typeface typefaceForStyle = typefaceForStyle(getFontFamily(str), str2);
        this.fontMap.put(this.tempPair, typefaceForStyle);
        return typefaceForStyle;
    }

    public final Typeface getFontFamily(String str) {
        Typeface typeface = this.fontFamilies.get(str);
        if (typeface != null) {
            return typeface;
        }
        Typeface createFromAsset = Typeface.createFromAsset(this.assetManager, "fonts/" + str + this.defaultFontFileExtension);
        this.fontFamilies.put(str, createFromAsset);
        return createFromAsset;
    }

    public final Typeface typefaceForStyle(Typeface typeface, String str) {
        boolean contains = str.contains("Italic");
        boolean contains2 = str.contains("Bold");
        int i = (!contains || !contains2) ? contains ? 2 : contains2 ? 1 : 0 : 3;
        return typeface.getStyle() == i ? typeface : Typeface.create(typeface, i);
    }
}
