package com.miui.gallery.editor.photo.app.miuibeautify;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.arcsoftbeauty.ArcsoftBeautyJni;
import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.RenderRecord;
import com.miui.gallery.editor.photo.app.menu.MiuiBeautyView;
import com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautifyData;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyManager;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment;
import com.miui.gallery.editor.photo.widgets.CommonBottomMenuWithUndo;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

@Deprecated
/* loaded from: classes2.dex */
public class MiuiBeautyFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<MiuiBeautifyData, AbstractEffectFragment>> implements RenderRecord {
    public static int BEAUTY_RECORDS_MAX = 10;
    public int mAnimAppearDelay;
    public int mAnimAppearDuration;
    public int mAnimDisappearDuration;
    public int mAnimOffset;
    public View mAnimalLayout;
    public List<MiuiBeautifyData> mBeautyParameters;
    public CommonBottomMenuWithUndo mBottomMenuPanel;
    public ChildMenuFragment.Callbacks mChildMenuCallback;
    public int mCurrentDataIndex;
    public boolean mDetectFace;
    public boolean mDetectTaskDone;
    public ChildFragmentData[] mFragmentData;
    public int[] mIcons;
    public boolean mInMainLayout;
    public OnItemClickListener mOnItemClickListener;
    public ProgressDialog mProgressDialog;
    public int mRecordCurr;
    public int mRecordHead;
    public int mRecordTail;
    public TextView mTitle;

    public MiuiBeautyFragment() {
        super(Effect.MIUIBEAUTIFY);
        this.mRecordHead = 0;
        this.mRecordCurr = 0;
        this.mInMainLayout = true;
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.MiuiBeautyFragment.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                if (!MiuiBeautyFragment.this.mInMainLayout) {
                    return false;
                }
                ChildFragmentData childFragmentData = MiuiBeautyFragment.this.mFragmentData[i];
                ChildMenuFragment newMenu = childFragmentData.newMenu();
                newMenu.setCallback(MiuiBeautyFragment.this.mChildMenuCallback);
                MiuiBeautyFragment.this.updateView(newMenu);
                AbstractEffectFragment abstractEffectFragment = (AbstractEffectFragment) MiuiBeautyFragment.this.getRenderFragment();
                if (abstractEffectFragment instanceof MiuiBeautyRenderFragment) {
                    if (!MiuiBeautyFragment.this.mDetectTaskDone) {
                        DefaultLogger.d("MiuiBeautyFragment", "detect task not done");
                        return false;
                    }
                    MiuiBeautyEffect miuiBeautyEffect = childFragmentData.beautyEffect;
                    if (miuiBeautyEffect != null && miuiBeautyEffect.needFace() && !MiuiBeautyFragment.this.mDetectFace) {
                        ToastUtils.makeText(MiuiBeautyFragment.this.getActivity(), (int) R.string.photo_editor_miui_beauty_no_face);
                        return true;
                    }
                    MiuiBeautyRenderFragment miuiBeautyRenderFragment = (MiuiBeautyRenderFragment) abstractEffectFragment;
                    miuiBeautyRenderFragment.setOnBeautyProcessListener(newMenu);
                    MiuiBeautyFragment.this.showAnimal(false);
                    MiuiBeautyFragment.this.getChildFragmentManager().beginTransaction().add(R.id.child_menu_panel, newMenu).commit();
                    miuiBeautyRenderFragment.setCompareOrigin(false);
                    miuiBeautyRenderFragment.enableComparison(false);
                    MiuiBeautyFragment.this.mInMainLayout = false;
                    MiuiBeautyFragment.this.mBottomMenuPanel.updateBottomBar(true);
                    MiuiBeautyFragment.this.mCurrentDataIndex = i;
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", ((MiuiBeautifyData) MiuiBeautyFragment.this.mBeautyParameters.get(MiuiBeautyFragment.this.mCurrentDataIndex)).name);
                    SamplingStatHelper.recordCountEvent("miui_beautify", "miui_beauty_item_click", hashMap);
                }
                return true;
            }
        };
        this.mChildMenuCallback = new ChildMenuFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.MiuiBeautyFragment.2
            @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment.Callbacks
            public void onBeautyParameterChanged(ChildMenuFragment childMenuFragment) {
                AbstractEffectFragment abstractEffectFragment = (AbstractEffectFragment) MiuiBeautyFragment.this.getRenderFragment();
                if (abstractEffectFragment instanceof MiuiBeautyRenderFragment) {
                    MiuiBeautyRenderFragment miuiBeautyRenderFragment = (MiuiBeautyRenderFragment) abstractEffectFragment;
                    abstractEffectFragment.add(null, childMenuFragment.getBeautyParameterTable());
                    miuiBeautyRenderFragment.setShowProgressDelay(!(childMenuFragment instanceof SmartBeautyFragment));
                    abstractEffectFragment.render();
                    if (miuiBeautyRenderFragment.isComparisonEnable()) {
                        return;
                    }
                    miuiBeautyRenderFragment.enableComparison(true);
                }
            }

            @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment.Callbacks
            public void changeTitle(String str) {
                MiuiBeautyFragment.this.mTitle.setText(str);
            }
        };
        ChildFragmentData[] childFragmentDataArr = new ChildFragmentData[this.mSdkProvider.list().size() + 1];
        this.mFragmentData = childFragmentDataArr;
        childFragmentDataArr[0] = new ChildFragmentData(SmartBeautyFragment.class, null);
        MiuiBeautyEffect[] beautyEffects = MiuiBeautyManager.getBeautyEffects();
        for (int i = 0; i < beautyEffects.length; i++) {
            if (beautyEffects[i].mBeautyType == BeautyParameterType.WHITEN_STRENGTH) {
                this.mFragmentData[i + 1] = new ChildFragmentData(DoubleParameterBeautyFragment.class, beautyEffects[i]);
            } else if (beautyEffects[i].mBeautyType == BeautyParameterType.DEBLEMISH) {
                this.mFragmentData[i + 1] = new ChildFragmentData(SwitchParameterBeautyFragment.class, beautyEffects[i]);
            } else {
                this.mFragmentData[i + 1] = new ChildFragmentData(SingleParameterBeautyFragment.class, beautyEffects[i]);
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList arrayList = new ArrayList(this.mSdkProvider.list());
        ArrayList arrayList2 = new ArrayList(arrayList.size() + 1);
        this.mBeautyParameters = arrayList2;
        arrayList2.add(new SmartBeautyData((short) 0, getActivity().getString(R.string.photo_editor_miui_beauty_menu_smart_beauty)));
        this.mBeautyParameters.addAll(arrayList);
        this.mIcons = new int[]{R.drawable.icon_smart_beauty, R.drawable.icon_beauty_face_thin, R.drawable.icon_beauty_skin_white, R.drawable.icon_beauty_smooth, R.drawable.icon_beauty_eye_large, R.drawable.icon_beauty_eye_bright, R.drawable.icon_beauty_depouch, R.drawable.icon_beauty_deblemish, R.drawable.icon_beauty_relighting, R.drawable.icon_beauty_nose_thin, R.drawable.icon_beauty_lips};
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new MiuiBeautyView(viewGroup.getContext());
    }

    public final void initAnimal() {
        this.mAnimOffset = getActivity().getResources().getDimensionPixelSize(R.dimen.photo_editor_enter_sub_editor_main_menu_offset);
        this.mAnimAppearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_appear_duration);
        this.mAnimDisappearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_disappear_duration);
        this.mAnimAppearDelay = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_appear_delay);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        BeautyParameterAdapter beautyParameterAdapter = new BeautyParameterAdapter(this.mBeautyParameters, this.mIcons);
        SimpleRecyclerViewNoSpring simpleRecyclerViewNoSpring = (SimpleRecyclerViewNoSpring) view.findViewById(R.id.parameter_list);
        simpleRecyclerViewNoSpring.setAdapter(beautyParameterAdapter);
        beautyParameterAdapter.setOnItemClickListener(this.mOnItemClickListener);
        simpleRecyclerViewNoSpring.addItemDecoration(new BlankDivider(getResources(), R.dimen.photo_editor_navigator_left_margin));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(simpleRecyclerViewNoSpring);
        CommonBottomMenuWithUndo commonBottomMenuWithUndo = (CommonBottomMenuWithUndo) view.findViewById(R.id.bottom_bar);
        this.mBottomMenuPanel = commonBottomMenuWithUndo;
        commonBottomMenuWithUndo.setTitle(R.string.photo_editor_miui_beauty);
        this.mBottomMenuPanel.setRenderRecordListener(this);
        this.mBottomMenuPanel.updateBottomBar(true);
        this.mAnimalLayout = view.findViewById(R.id.layout);
        this.mTitle = (TextView) view.findViewById(R.id.title);
        initAnimal();
        new DetectFaceTask(this).execute(new Void[0]);
    }

    public final void showAnimal(boolean z) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -this.mAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setDuration(this.mAnimAppearDuration);
            objectAnimator.setStartDelay(this.mAnimAppearDelay);
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
        } else {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, -this.mAnimOffset), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
            objectAnimator.setDuration(this.mAnimDisappearDuration);
            objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
        }
        objectAnimator.setTarget(this.mAnimalLayout);
        objectAnimator.start();
    }

    public void recordCurrent() {
        int i = BEAUTY_RECORDS_MAX;
        int i2 = (this.mRecordCurr + 1) % i;
        this.mRecordCurr = i2;
        this.mRecordTail = i2;
        if (i2 == this.mRecordHead) {
            this.mRecordHead = (i2 + 1) % i;
        }
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void previousRecord() {
        boolean z = true;
        int i = (this.mRecordCurr - 1) % BEAUTY_RECORDS_MAX;
        this.mRecordCurr = i;
        if (i < 0) {
            this.mRecordCurr = i + 10;
        }
        AbstractEffectFragment renderFragment = getRenderFragment();
        if (renderFragment instanceof MiuiBeautyRenderFragment) {
            ((MiuiBeautyRenderFragment) renderFragment).previousRecord();
        }
        CommonBottomMenuWithUndo commonBottomMenuWithUndo = this.mBottomMenuPanel;
        int i2 = this.mRecordCurr;
        boolean z2 = i2 != this.mRecordHead;
        if (i2 == this.mRecordTail) {
            z = false;
        }
        commonBottomMenuWithUndo.onMenuUpdated(z2, z);
    }

    @Override // com.miui.gallery.editor.photo.app.RenderRecord
    public void nextRecord() {
        boolean z = true;
        this.mRecordCurr = (this.mRecordCurr + 1) % BEAUTY_RECORDS_MAX;
        AbstractEffectFragment renderFragment = getRenderFragment();
        if (renderFragment instanceof MiuiBeautyRenderFragment) {
            ((MiuiBeautyRenderFragment) renderFragment).nextRecord();
        }
        CommonBottomMenuWithUndo commonBottomMenuWithUndo = this.mBottomMenuPanel;
        int i = this.mRecordCurr;
        boolean z2 = i != this.mRecordHead;
        if (i == this.mRecordTail) {
            z = false;
        }
        commonBottomMenuWithUndo.onMenuUpdated(z2, z);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void showProcessDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getActivity().getString(R.string.photo_editor_saving));
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.show();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void hideProcessDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }

    public final void updateView(Object obj) {
        if (getView() == null) {
            return;
        }
        ((MiuiBeautyView) getView()).updateGuideLine(obj);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void notifyDiscard() {
        if (hasChildFragment()) {
            AbstractEffectFragment renderFragment = getRenderFragment();
            renderFragment.add(null, null);
            renderFragment.render();
            if (renderFragment instanceof MiuiBeautyRenderFragment) {
                MiuiBeautyRenderFragment miuiBeautyRenderFragment = (MiuiBeautyRenderFragment) renderFragment;
                miuiBeautyRenderFragment.setCompareOrigin(true);
                if (miuiBeautyRenderFragment.isEmpty()) {
                    miuiBeautyRenderFragment.enableComparison(false);
                }
            }
            backToMainMenu();
        } else {
            super.notifyDiscard();
        }
        updateView(this);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void notifySave() {
        if (hasChildFragment()) {
            AbstractEffectFragment renderFragment = getRenderFragment();
            if (renderFragment instanceof MiuiBeautyRenderFragment) {
                MiuiBeautyRenderFragment miuiBeautyRenderFragment = (MiuiBeautyRenderFragment) renderFragment;
                if (miuiBeautyRenderFragment.isBeautyParamWorked()) {
                    miuiBeautyRenderFragment.recordCurrent();
                    recordCurrent();
                    this.mBottomMenuPanel.onMenuUpdated(this.mRecordCurr != this.mRecordHead, false);
                    miuiBeautyRenderFragment.setCompareOrigin(true);
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", this.mBeautyParameters.get(this.mCurrentDataIndex).name);
                    SamplingStatHelper.recordCountEvent("miui_beautify", "miui_beauty_item_save", hashMap);
                }
            }
            backToMainMenu();
        } else {
            super.notifySave();
        }
        updateView(this);
    }

    public final boolean hasChildFragment() {
        return getChildFragmentManager().findFragmentById(R.id.child_menu_panel) != null;
    }

    public final void backToMainMenu() {
        CommonBottomMenuWithUndo commonBottomMenuWithUndo = this.mBottomMenuPanel;
        commonBottomMenuWithUndo.updateBottomBar(commonBottomMenuWithUndo.isShowTitle());
        this.mInMainLayout = true;
        FragmentManager childFragmentManager = getChildFragmentManager();
        Fragment findFragmentById = childFragmentManager.findFragmentById(R.id.child_menu_panel);
        if (findFragmentById != null) {
            showAnimal(true);
            childFragmentManager.beginTransaction().remove(findFragmentById).commit();
        }
        this.mTitle.setText(R.string.photo_editor_miui_beauty);
    }

    /* loaded from: classes2.dex */
    public static class ChildFragmentData {
        public final MiuiBeautyEffect beautyEffect;
        public final Class<? extends ChildMenuFragment> menu;

        public ChildFragmentData(Class<? extends ChildMenuFragment> cls, MiuiBeautyEffect miuiBeautyEffect) {
            this.menu = cls;
            this.beautyEffect = miuiBeautyEffect;
        }

        public ChildMenuFragment newMenu() {
            try {
                ChildMenuFragment newInstance = this.menu.newInstance();
                Bundle bundle = new Bundle();
                bundle.putParcelable("BEAUTY_EFFECT", this.beautyEffect);
                newInstance.setArguments(bundle);
                return newInstance;
            } catch (IllegalAccessException e) {
                DefaultLogger.w("MiuiBeautyFragment", e);
                throw new IllegalStateException(e);
            } catch (InstantiationException e2) {
                DefaultLogger.w("MiuiBeautyFragment", e2);
                throw new IllegalStateException(e2);
            }
        }
    }

    public int detectFaceNum() {
        Bitmap preview = getPreview();
        if (preview == null) {
            return 0;
        }
        return ArcsoftBeautyJni.detectFaceNum(preview, preview.getWidth(), preview.getHeight());
    }

    public void setDetectFace(boolean z) {
        this.mDetectFace = z;
    }

    public void setDetectTaskDone(boolean z) {
        this.mDetectTaskDone = z;
    }

    /* loaded from: classes2.dex */
    public static class DetectFaceTask extends AsyncTask<Void, Void, Integer> {
        public WeakReference<MiuiBeautyFragment> mFragmentWeakReference;

        public DetectFaceTask(MiuiBeautyFragment miuiBeautyFragment) {
            this.mFragmentWeakReference = new WeakReference<>(miuiBeautyFragment);
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            MiuiBeautyFragment miuiBeautyFragment = this.mFragmentWeakReference.get();
            if (miuiBeautyFragment != null) {
                return Integer.valueOf(miuiBeautyFragment.detectFaceNum());
            }
            return 0;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((DetectFaceTask) num);
            MiuiBeautyFragment miuiBeautyFragment = this.mFragmentWeakReference.get();
            if (miuiBeautyFragment != null) {
                miuiBeautyFragment.setDetectFace(num.intValue() > 0);
                miuiBeautyFragment.setDetectTaskDone(true);
            }
        }
    }
}
