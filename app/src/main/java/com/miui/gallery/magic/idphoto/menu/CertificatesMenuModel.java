package com.miui.gallery.magic.idphoto.menu;

import android.text.TextUtils;
import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.idphoto.bean.CategoryColorItem;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class CertificatesMenuModel extends BaseModel<CertificatesMenuPresenter, IMenu$M> {
    public String[] mTabs;
    public HashMap<String, String[]> mapCategory;

    public CertificatesMenuModel(CertificatesMenuPresenter certificatesMenuPresenter) {
        super(certificatesMenuPresenter);
        this.mapCategory = new HashMap<>();
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$M mo1070initContract() {
        return new IMenu$M() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuModel.1
            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public String[] getTabsData() {
                CertificatesMenuModel.this.mTabs = ResourceUtil.getArrayById(R$array.magic_idp_size_category);
                return CertificatesMenuModel.this.mTabs;
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public ArrayList<CategoryItem> getListData() {
                return CertificatesMenuModel.this.loadListData();
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public ArrayList<CategoryColorItem> getListColorData() {
                return CertificatesMenuModel.this.loadColorListData();
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public int getTabIndex(String str) {
                for (int i = 0; i < CertificatesMenuModel.this.mTabs.length; i++) {
                    if (CertificatesMenuModel.this.mTabs[i].equals(str)) {
                        return i;
                    }
                }
                return 0;
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public int getMapCategorySizeFromTabs(int i) {
                return ((String[]) CertificatesMenuModel.this.mapCategory.get(CertificatesMenuModel.this.mTabs[i])).length;
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$M
            public int[] getWidthHeight(String str, String str2) {
                int[] iArr = {0, 0, 0, 0};
                if (TextUtils.isEmpty(str)) {
                    return iArr;
                }
                String[] split = str.replace(" px", "").split("×");
                String[] split2 = str2.replace(" mm", "").split("×");
                iArr[0] = Integer.valueOf(split[0]).intValue();
                iArr[1] = Integer.valueOf(split[1]).intValue();
                iArr[2] = Integer.valueOf(split2[0]).intValue();
                iArr[3] = Integer.valueOf(split2[1]).intValue();
                return iArr;
            }
        };
    }

    public final ArrayList loadListData() {
        ArrayList<CategoryItem> arrayList = new ArrayList<>();
        String[] arrayById = ResourceUtil.getArrayById(R$array.magic_idp_size_category_common);
        String[] arrayById2 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_visa);
        String[] arrayById3 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_profession);
        String[] arrayById4 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_certificates);
        String[] arrayById5 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_education);
        String[] arrayById6 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_examination);
        initCategorySize();
        addToList(arrayById, this.mTabs[0], arrayList);
        addToList(arrayById2, this.mTabs[1], arrayList);
        addToList(arrayById3, this.mTabs[2], arrayList);
        addToList(arrayById4, this.mTabs[3], arrayList);
        addToList(arrayById5, this.mTabs[4], arrayList);
        addToList(arrayById6, this.mTabs[5], arrayList);
        arrayList.get(0).setText("");
        return arrayList;
    }

    public final ArrayList loadColorListData() {
        ArrayList arrayList = new ArrayList();
        for (String str : ResourceUtil.getArrayById(R$array.magic_idp_color_common)) {
            arrayList.add(new CategoryColorItem(str));
        }
        ((CategoryColorItem) arrayList.get(0)).setCheck(true);
        return arrayList;
    }

    public final void addToList(String[] strArr, String str, ArrayList<CategoryItem> arrayList) {
        for (int i = 0; i < strArr.length; i++) {
            String[] categoryText = getCategoryText(str, i);
            int size = arrayList.size();
            arrayList.add(new CategoryItem(strArr[i], categoryText[0] + " px", Boolean.FALSE, str, categoryText[1] + " mm", size));
        }
    }

    public final String[] getCategoryText(String str, int i) {
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("type:===>" + str);
        return this.mapCategory.get(str)[i].split("\\+");
    }

    public final Map<String, String[]> initCategorySize() {
        String[] arrayById = ResourceUtil.getArrayById(R$array.magic_idp_size_category_common_value);
        String[] arrayById2 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_visa_value);
        String[] arrayById3 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_profession_value);
        String[] arrayById4 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_certificates_value);
        String[] arrayById5 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_education_value);
        String[] arrayById6 = ResourceUtil.getArrayById(R$array.magic_idp_size_category_examination_value);
        this.mapCategory.put(this.mTabs[0], arrayById);
        this.mapCategory.put(this.mTabs[1], arrayById2);
        this.mapCategory.put(this.mTabs[2], arrayById3);
        this.mapCategory.put(this.mTabs[3], arrayById4);
        this.mapCategory.put(this.mTabs[4], arrayById5);
        this.mapCategory.put(this.mTabs[5], arrayById6);
        return this.mapCategory;
    }
}
