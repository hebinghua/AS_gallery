package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.Merger;
import com.miui.gallery.util.BaseBuildUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class ComponentsStrategy extends BaseStrategy {
    @SerializedName("component-priority")
    private ArrayList<Priority> mPriority;
    @SerializedName("component-priority-international")
    private ArrayList<Priority> mPriorityInternational;
    @SerializedName("share-components")
    private List<Component> mShareComponents;
    @SerializedName("share-components-international")
    private List<Component> mShareComponentsInternational;
    public static final Comparator<Priority> MATCH_ORDER = new Comparator<Priority>() { // from class: com.miui.gallery.cloudcontrol.strategies.ComponentsStrategy.1
        @Override // java.util.Comparator
        public int compare(Priority priority, Priority priority2) {
            int i = priority.mMatchOrder;
            int i2 = priority2.mMatchOrder;
            if (i > i2) {
                return 1;
            }
            return i == i2 ? 0 : -1;
        }
    };
    public static final Merger<ComponentsStrategy> CLOUD_FIRST_MERGER = new Merger<ComponentsStrategy>() { // from class: com.miui.gallery.cloudcontrol.strategies.ComponentsStrategy.2
        @Override // com.miui.gallery.cloudcontrol.Merger
        public ComponentsStrategy merge(ComponentsStrategy componentsStrategy, ComponentsStrategy componentsStrategy2) {
            if (componentsStrategy2.mPriorityInternational == null) {
                if (componentsStrategy.mPriorityInternational == null || componentsStrategy.mPriorityInternational.isEmpty()) {
                    componentsStrategy2.mPriorityInternational = new ArrayList();
                } else {
                    componentsStrategy2.mPriorityInternational = (ArrayList) componentsStrategy.mPriorityInternational.clone();
                }
            }
            if (componentsStrategy2.mPriority == null) {
                if (componentsStrategy.mPriority == null || componentsStrategy.mPriority.isEmpty()) {
                    componentsStrategy2.mPriority = new ArrayList();
                } else {
                    componentsStrategy2.mPriority = (ArrayList) componentsStrategy.mPriority.clone();
                }
            }
            return componentsStrategy2;
        }
    };

    public List<Component> getShareComponents() {
        List<Component> list;
        if (BaseBuildUtil.isInternational()) {
            list = this.mShareComponentsInternational;
        } else {
            list = this.mShareComponents;
        }
        return list != null ? list : new ArrayList();
    }

    public List<Priority> getComponentPriority() {
        ArrayList<Priority> arrayList;
        if (BaseBuildUtil.isInternational()) {
            arrayList = this.mPriorityInternational;
        } else {
            arrayList = this.mPriority;
        }
        return arrayList != null ? arrayList : new ArrayList();
    }

    public static ComponentsStrategy createDefault() {
        ComponentsStrategy componentsStrategy = new ComponentsStrategy();
        if (BaseBuildUtil.isInternational()) {
            ArrayList arrayList = new ArrayList();
            componentsStrategy.mShareComponentsInternational = arrayList;
            arrayList.add(new Component("com.google.android.gms", "com.google.android.gms.nearby.sharing.ShareSheetActivity", -1));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.twitter.android", "com.twitter.android.composer.ComposerActivity", 0));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.instagram.android", "com.instagram.android.activity.ShareHandlerActivity", 1));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.whatsapp", "com.whatsapp.ContactPicker", 2));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias", 3));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.facebook.orca", "com.facebook.messenger.intents.ShareIntentHandler", 4));
            componentsStrategy.mShareComponentsInternational.add(new Component("org.telegram.messenger", "org.telegram.ui.LaunchActivity", 5));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.bsb.hike", "com.bsb.hike.ui.ComposeChatActivity", 6));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.twitter.android", "com.twitter.android.DMActivity", 7));
            componentsStrategy.mShareComponentsInternational.add(new Component("com.xiaomi.midrop", "com.xiaomi.midrop.sender.ui.TransmissionActivity", 8));
        } else {
            ArrayList arrayList2 = new ArrayList();
            componentsStrategy.mShareComponents = arrayList2;
            arrayList2.add(new Component("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", 0));
            componentsStrategy.mShareComponents.add(new Component("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI", 1));
            componentsStrategy.mShareComponents.add(new Component("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity", 2));
            componentsStrategy.mShareComponents.add(new Component("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity", 3));
        }
        componentsStrategy.mPriorityInternational = new ArrayList<>();
        componentsStrategy.mPriority = new ArrayList<>();
        return componentsStrategy;
    }

    public String toString() {
        return "ComponentsStrategy{mShareComponents=" + this.mShareComponents + ", mShareComponentsInternational=" + this.mShareComponentsInternational + ", mPriority=" + this.mPriority + ", mPriorityInternational=" + this.mPriorityInternational + '}';
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        ArrayList<Priority> arrayList;
        if (BaseBuildUtil.isInternational()) {
            addAdditionalComponentTopOrder(new Component("com.google.android.gms", "com.google.android.gms.nearby.sharing.ShareSheetActivity", -1));
        }
        if (BaseBuildUtil.isInternational()) {
            arrayList = this.mPriorityInternational;
        } else {
            arrayList = this.mPriority;
        }
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        for (Priority priority : arrayList) {
            priority.initMathOrder();
        }
        Collections.sort(arrayList, MATCH_ORDER);
    }

    public final void addAdditionalComponentTopOrder(Component component) {
        List<Component> list;
        if (BaseBuildUtil.isInternational() && (list = this.mShareComponentsInternational) != null && !list.contains(component)) {
            this.mShareComponentsInternational.add(0, component);
            return;
        }
        List<Component> list2 = this.mShareComponents;
        if (list2 == null || list2.contains(component)) {
            return;
        }
        this.mShareComponents.add(0, component);
    }

    /* loaded from: classes.dex */
    public static class Priority {
        @SerializedName("class-name")
        public final String className;
        public int mMatchOrder;
        @SerializedName("package-name")
        public final String packageName;
        @SerializedName("package-prefix")
        public final String packagePrefix;
        @SerializedName("system")
        public final Boolean system;
        @SerializedName("value")
        public final int value;

        public void initMathOrder() {
            int i = this.system != null ? 3 : Integer.MAX_VALUE;
            if (this.packagePrefix != null) {
                i = 2;
            }
            if (this.packageName != null) {
                i = 1;
            }
            if (this.className != null) {
                i = 0;
            }
            this.mMatchOrder = i;
        }

        public String toString() {
            return "Priority{packagePrefix='" + this.packagePrefix + CoreConstants.SINGLE_QUOTE_CHAR + ", packageName='" + this.packageName + CoreConstants.SINGLE_QUOTE_CHAR + ", className='" + this.className + CoreConstants.SINGLE_QUOTE_CHAR + ", value=" + this.value + ", system=" + this.system + ", mMatchOrder=" + this.mMatchOrder + '}';
        }

        public boolean match(String str, String str2, boolean z) {
            String str3 = this.packageName;
            if (str3 != null) {
                return str3.equals(str) && (TextUtils.isEmpty(this.className) || this.className.equals(str2));
            }
            String str4 = this.packagePrefix;
            if (str4 != null) {
                return str.startsWith(str4);
            }
            Boolean bool = this.system;
            return bool != null && bool.booleanValue() == z;
        }
    }

    /* loaded from: classes.dex */
    public static class Component {
        @SerializedName("class-name")
        private String mClassName;
        @SerializedName("order")
        private int mOrder;
        @SerializedName("package-name")
        private String mPackageName;

        public Component(String str, String str2, int i) {
            this.mPackageName = str;
            this.mClassName = str2;
            this.mOrder = i;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public String getClassName() {
            return this.mClassName;
        }

        public int getOrder() {
            return this.mOrder;
        }

        public String toString() {
            return "Component{mPackageName='" + this.mPackageName + CoreConstants.SINGLE_QUOTE_CHAR + ", mClassName='" + this.mClassName + CoreConstants.SINGLE_QUOTE_CHAR + ", mOrder=" + this.mOrder + '}';
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Component)) {
                return false;
            }
            Component component = (Component) obj;
            return component.getPackageName().equals(this.mPackageName) && component.getClassName().equals(this.mClassName) && component.getOrder() == this.mOrder;
        }

        public int hashCode() {
            return Objects.hash(this.mPackageName, this.mClassName, Integer.valueOf(this.mOrder));
        }
    }
}
