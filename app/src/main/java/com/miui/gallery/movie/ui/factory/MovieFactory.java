package com.miui.gallery.movie.ui.factory;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.nvsdk.NvMovieManager;
import com.miui.gallery.movie.xmsdk.XmMovieManager;

/* loaded from: classes2.dex */
public class MovieFactory {
    public static String getTemplateNameById(int i) {
        switch (i) {
            case 1:
                return "movieTemplateTravel";
            case 2:
                return "movieTemplateBaby";
            case 3:
                return "movieTemplateParty";
            case 4:
                return "movieTemplateFood";
            case 5:
                return "movieTemplateXmas";
            case 6:
                return "movieTemplateNewYear";
            case 7:
                return "movieTemplateSelfie";
            case 8:
                return "movieTemplatePet";
            case 9:
                return "movieTemplatePetDog";
            default:
                return "movieAssetsNormal";
        }
    }

    public static MovieManager createMovieManager(Context context) {
        MovieManager nvMovieManager;
        if (MovieConfig.isUserXmSdk()) {
            nvMovieManager = new XmMovieManager();
        } else {
            nvMovieManager = new NvMovieManager();
        }
        nvMovieManager.init(context);
        MovieConfig.init(context);
        return nvMovieManager;
    }

    public static String getParentTemplateName(String str) {
        return TextUtils.equals("movieTemplatePetDog", str) ? "movieTemplatePet" : str;
    }
}
