package com.miui.pickdrag.base.stack;

import com.miui.pickdrag.base.BasePickerDragActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class PickerActivityStackImpl implements PickerActivityStacker {
    public final List<WeakReference<BasePickerDragActivity>> sPickerActivityStack = new ArrayList();

    @Override // com.miui.pickdrag.base.stack.PickerActivityStacker
    public void add(BasePickerDragActivity basePickerDragActivity) {
        this.sPickerActivityStack.add(new WeakReference<>(basePickerDragActivity));
    }

    @Override // com.miui.pickdrag.base.stack.PickerActivityStacker
    public void remove(BasePickerDragActivity basePickerDragActivity) {
        BasePickerDragActivity basePickerDragActivity2;
        if (this.sPickerActivityStack.isEmpty()) {
            return;
        }
        for (WeakReference<BasePickerDragActivity> weakReference : this.sPickerActivityStack) {
            if (weakReference != null && (basePickerDragActivity2 = weakReference.get()) != null && basePickerDragActivity2 == basePickerDragActivity) {
                this.sPickerActivityStack.remove(weakReference);
                return;
            }
        }
    }
}
