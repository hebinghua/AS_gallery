package com.miui.gallery.editor.photo.core.imports.text;

import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.dialog.DialogManager;
import com.miui.gallery.editor.photo.core.imports.text.dialog.LocalDialogModel;
import com.miui.gallery.util.Scheme;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class TextManager {
    public static List<TextData> getScreenTextBubbleData() {
        DialogManager dialogManager = new DialogManager();
        ArrayList arrayList = new ArrayList();
        int[] iArr = {R.drawable.screen_text_edit_bubble_none_icon, R.drawable.screen_text_edit_bubble_circular_icon, R.drawable.screen_text_edit_bubble_rectangle_icon, R.drawable.screen_text_edit_bubble_rect_horizontal_icon, R.drawable.screen_text_edit_bubble_oval_icon};
        List<BaseDialogModel> dialogModelList = dialogManager.getDialogModelList();
        removeSignatureData(dialogModelList);
        for (int i = 0; i < dialogModelList.size() && i < 5; i++) {
            BaseDialogModel baseDialogModel = dialogModelList.get(i);
            baseDialogModel.dialogSmallIconPath = Scheme.DRAWABLE.wrap(Integer.toString(iArr[i]));
            arrayList.add(new TextConfig((short) 0, baseDialogModel.name, baseDialogModel));
        }
        return arrayList;
    }

    public static void removeSignatureData(List<BaseDialogModel> list) {
        Iterator<BaseDialogModel> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().name.equals(DialogManager.LocalDialog.SIGNATURE.name())) {
                it.remove();
                return;
            }
        }
    }

    public static TextConfig getDefaultTextConfig() {
        DialogManager.LocalDialog localDialog = DialogManager.LocalDialog.NONE;
        LocalDialogModel localDialogModel = new LocalDialogModel(localDialog.mBackGroundColor, localDialog.mSmallIcon, localDialog.mGraphics, 0, localDialog.mLeftPercent, localDialog.mTopPercent, localDialog.mRightPercent, localDialog.mBottomPercent, localDialog.mIsCorner, localDialog.mCornerPosition, localDialog.name(), localDialog.mTalkbackName, localDialog.mType);
        return new TextConfig((short) 0, localDialogModel.name, localDialogModel);
    }
}
