package com.miui.gallery.movie.ui.factory;

import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.R$drawable;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.entity.TemplateResource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateFactory {
    public static String getTemplatePath(String str) {
        return MovieConfig.sTemplateDir + File.separator + str;
    }

    public static List<TemplateResource> getLocalTemplateEntities() {
        ArrayList arrayList = new ArrayList();
        TemplateResource templateResource = new TemplateResource();
        templateResource.imageId = R$drawable.movie_template_default;
        templateResource.stringId = R$string.movie_template_normal;
        templateResource.isPackageAssets = true;
        templateResource.nameKey = "movieAssetsNormal";
        templateResource.downloadState = 17;
        arrayList.add(templateResource);
        return arrayList;
    }
}
