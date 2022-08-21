package com.miui.gallery.magic.special.effects.image.menu;

import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.fetch.ArtResourceFetcher;
import com.miui.gallery.magic.special.effects.image.bean.SpecialIconItem;
import com.miui.gallery.magic.util.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MagicMenuModel extends BaseModel<MagicMenuPresenter, IMenu$M> {
    public static /* synthetic */ List $r8$lambda$Sd4WxADdIRaA2TKjjKTtXzMSOXA(String[] strArr, String[] strArr2, String[] strArr3) {
        return lambda$initContract$0(strArr, strArr2, strArr3);
    }

    public MagicMenuModel(MagicMenuPresenter magicMenuPresenter) {
        super(magicMenuPresenter);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$M mo1070initContract() {
        String resourceBasePath = ArtResourceFetcher.INSTANCE.getResourceBasePath();
        final String[] strArr = {resourceBasePath + "mask1.png", resourceBasePath + "mask2.png", resourceBasePath + "mask3.png"};
        final String[] arrayById = ResourceUtil.getArrayById(R$array.magic_effects_title);
        final String[] arrayById2 = ResourceUtil.getArrayById(R$array.magic_effects_icon_thumb);
        return new IMenu$M() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuModel$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$M
            public final List getData() {
                return MagicMenuModel.$r8$lambda$Sd4WxADdIRaA2TKjjKTtXzMSOXA(arrayById2, strArr, arrayById);
            }
        };
    }

    public static /* synthetic */ List lambda$initContract$0(String[] strArr, String[] strArr2, String[] strArr3) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            boolean z = !ArtResourceFetcher.INSTANCE.isExistResource();
            if (i == 4) {
                arrayList.add(new SpecialIconItem(strArr[i], z, strArr2, false, strArr3[i]));
            } else {
                arrayList.add(new SpecialIconItem(strArr[i], null, false, strArr3[i]));
            }
        }
        return arrayList;
    }
}
