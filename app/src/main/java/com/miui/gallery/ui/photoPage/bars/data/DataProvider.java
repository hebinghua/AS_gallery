package com.miui.gallery.ui.photoPage.bars.data;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.Loader;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes2.dex */
public class DataProvider implements IDataProvider {
    public final FieldData mFieldData;
    public final PhotoPageLoader mPhotoPageLoader;
    public final ViewModelData mViewModelData;

    /* loaded from: classes2.dex */
    public static class FieldData {
        public boolean isAlwaysShowMenu;
        public boolean isArgumentsConfirmPassWord;
        public boolean isFromCamera;
        public boolean isFromCustomWidget;
        public boolean isFromFileManager;
        public boolean isFromInternal;
        public boolean isFromNote;
        public boolean isFromRecommendFacePage;
        public boolean isFromScreenRecorder;
        public boolean isFromTrash;
        public boolean isHideInAdvanceByLandAction;
        public boolean isInPreviewMode;
        public boolean isNeedConfirmPassWord;
        public boolean isPlaySlideshow;
        public boolean isPreviewMode;
        public boolean isStartWhenLocked;
        public boolean isStartWhenLockedAndSecret;
        public boolean isStartingHomePage;
        public boolean isSupportFordBurst;
        public boolean isTransparentTheme;
        public boolean isUsingCameraAnim;
        public boolean isVideoPlayerSupportActionBarAdjust;
        public Bundle mArguments;
        public String mEnterLocation;
        public long mEnterTime;
        public EnterTypeUtils.EnterType mEnterType;
        public long mOperationMask;
        public int mCurrentClassification = -1;
        public Current mCurrent = new Current();

        /* loaded from: classes2.dex */
        public static class Current {
            public BaseDataItem dataItem;
            public BaseDataSet dataSet;
            public boolean isInMultiWindowMode;
            public boolean isSlipped;
            public PhotoPageItem itemView;
            public Configuration mConfiguration;
            public int position;
            public int slipState = 0;
            public final Observer<Integer> mCurrentPositionObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda5
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.m1619$r8$lambda$syDeTsJYcduD4I5Wr1zWx00X8(DataProvider.FieldData.Current.this, (Integer) obj);
                }
            };
            public final Observer<BaseDataItem> mCurrentDataItemObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda1
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.$r8$lambda$0xT0qElPl_jXDQfei750Wx1tZP4(DataProvider.FieldData.Current.this, (BaseDataItem) obj);
                }
            };
            public final Observer<BaseDataSet> mCurrentDataSetObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda2
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.m1617$r8$lambda$1Qj8JFALy6JI0oIFLQ7paJLuH4(DataProvider.FieldData.Current.this, (BaseDataSet) obj);
                }
            };
            public final Observer<Configuration> mConfigurationObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda0
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.m1620$r8$lambda$vdex29GxfAqbsgTdRfeGuX94(DataProvider.FieldData.Current.this, (Configuration) obj);
                }
            };
            public final Observer<Boolean> isInMultiWindowModeObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda4
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.$r8$lambda$mK7FyQW6RIlaUR5a4cmEquy1FTg(DataProvider.FieldData.Current.this, (Boolean) obj);
                }
            };
            public final Observer<Boolean> isSlippedObserver = new Observer() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$FieldData$Current$$ExternalSyntheticLambda3
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    DataProvider.FieldData.Current.m1618$r8$lambda$Mk8H3CcEYqHREpz6T4gCuYcPk(DataProvider.FieldData.Current.this, (Boolean) obj);
                }
            };

            public static /* synthetic */ void $r8$lambda$0xT0qElPl_jXDQfei750Wx1tZP4(Current current, BaseDataItem baseDataItem) {
                current.lambda$new$1(baseDataItem);
            }

            /* renamed from: $r8$lambda$1Qj8JFALy6JI0o-IFLQ7paJLuH4 */
            public static /* synthetic */ void m1617$r8$lambda$1Qj8JFALy6JI0oIFLQ7paJLuH4(Current current, BaseDataSet baseDataSet) {
                current.lambda$new$2(baseDataSet);
            }

            /* renamed from: $r8$lambda$Mk8H3CcEYqHREp-z6T4gCuYc-Pk */
            public static /* synthetic */ void m1618$r8$lambda$Mk8H3CcEYqHREpz6T4gCuYcPk(Current current, Boolean bool) {
                current.lambda$new$5(bool);
            }

            public static /* synthetic */ void $r8$lambda$mK7FyQW6RIlaUR5a4cmEquy1FTg(Current current, Boolean bool) {
                current.lambda$new$4(bool);
            }

            /* renamed from: $r8$lambda$syDe-TsJYcduD4I5Wr1-zWx00X8 */
            public static /* synthetic */ void m1619$r8$lambda$syDeTsJYcduD4I5Wr1zWx00X8(Current current, Integer num) {
                current.lambda$new$0(num);
            }

            /* renamed from: $r8$lambda$vde-x29Gx--fAqbsgTdRfeGuX94 */
            public static /* synthetic */ void m1620$r8$lambda$vdex29GxfAqbsgTdRfeGuX94(Current current, Configuration configuration) {
                current.lambda$new$3(configuration);
            }

            public /* synthetic */ void lambda$new$0(Integer num) {
                this.position = num.intValue();
            }

            public /* synthetic */ void lambda$new$1(BaseDataItem baseDataItem) {
                this.dataItem = baseDataItem;
            }

            public /* synthetic */ void lambda$new$2(BaseDataSet baseDataSet) {
                this.dataSet = baseDataSet;
            }

            public /* synthetic */ void lambda$new$3(Configuration configuration) {
                this.mConfiguration = configuration;
            }

            public /* synthetic */ void lambda$new$4(Boolean bool) {
                this.isInMultiWindowMode = bool.booleanValue();
            }

            public /* synthetic */ void lambda$new$5(Boolean bool) {
                this.isSlipped = bool.booleanValue();
            }

            public int getPosition() {
                return this.position;
            }

            public BaseDataItem getDataItem() {
                return this.dataItem;
            }

            public BaseDataSet getDataSet() {
                return this.dataSet;
            }

            public Configuration getConfiguration() {
                return this.mConfiguration;
            }

            public boolean isInMultiWindowMode() {
                return this.isInMultiWindowMode;
            }
        }

        public boolean parseArguments(Bundle bundle) {
            if (bundle == null) {
                return false;
            }
            this.mArguments = bundle;
            if (TextUtils.isEmpty(bundle.getString("photo_uri"))) {
                return false;
            }
            EnterTypeUtils.EnterType enterFrom = EnterTypeUtils.getEnterFrom(bundle);
            this.mEnterType = enterFrom;
            this.isFromScreenRecorder = EnterTypeUtils.isFromScreenRecorder(enterFrom);
            this.isFromInternal = EnterTypeUtils.isFromInternal(this.mEnterType);
            this.isFromTrash = EnterTypeUtils.isFromTrash(this.mEnterType);
            this.isFromRecommendFacePage = EnterTypeUtils.isFromRecommendFacePage(this.mEnterType);
            boolean isFromCamera = EnterTypeUtils.isFromCamera(this.mEnterType);
            this.isFromCamera = isFromCamera;
            this.isInPreviewMode = isFromCamera;
            boolean isFromCustomWidget = EnterTypeUtils.isFromCustomWidget(this.mEnterType);
            this.isFromCustomWidget = isFromCustomWidget;
            if (isFromCustomWidget) {
                this.isInPreviewMode = true;
            }
            this.isFromFileManager = EnterTypeUtils.isFromFileManager(this.mEnterType);
            this.isFromNote = EnterTypeUtils.isFromNote(this.mEnterType);
            this.isUsingCameraAnim = EnterTypeUtils.isUsingCameraAnim(this.mEnterType);
            this.isVideoPlayerSupportActionBarAdjust = VideoPlayerCompat.isVideoPlayerSupportActionBarAdjust();
            this.isTransparentTheme = bundle.getInt("key_theme", 0) == 1;
            this.isAlwaysShowMenu = bundle.getBoolean("photo_always_show_menubar", false);
            boolean z = bundle.getBoolean("photo_preview_mode");
            this.isPreviewMode = z;
            if (z) {
                this.isInPreviewMode = true;
            }
            this.isSupportFordBurst = !bundle.getBoolean("unford_burst", false);
            this.mOperationMask = bundle.getLong("support_operation_mask", -1L);
            this.isArgumentsConfirmPassWord = bundle.getBoolean("need_confirm_psw", false);
            ImageLoadParams imageLoadParams = (ImageLoadParams) bundle.getParcelable("photo_transition_data");
            if (imageLoadParams == null) {
                return true;
            }
            this.mEnterTime = imageLoadParams.getCreateTime();
            this.mEnterLocation = imageLoadParams.getLocation();
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static class ViewModelData extends ViewModel {
        public final MutableLiveData<Map<String, ProcessingMedia>> mProcessingMediaMap = new MutableLiveData<>();
        public final MutableLiveData<Boolean> isFirstLoadProcessingMedia = new MutableLiveData<>();
        public final MutableLiveData<Integer> mCurrentPosition = new MutableLiveData<>();
        public final MutableLiveData<BaseDataItem> mCurrentDataItem = new MutableLiveData<>();
        public final MutableLiveData<Pair<BaseDataItem, Integer>> mPrepareDataItem = new MutableLiveData<>();
        public final MutableLiveData<BaseDataSet> mCurrentDataSet = new MutableLiveData<>();
        public final MutableLiveData<ArrayList<IMenuItemDelegate>> mResidentMenuItems = new MutableLiveData<>();
        public final MutableLiveData<ArrayList<IMenuItemDelegate>> mNonResidentMenuItems = new MutableLiveData<>();
        public final MutableLiveData<Configuration> mConfiguration = new MutableLiveData<>();
        public final MutableLiveData<Boolean> isInMultiWindowMode = new MutableLiveData<>();
        public final MutableLiveData<Boolean> isSlipped = new MutableLiveData<>();

        /* loaded from: classes2.dex */
        public interface IBaseDataSetObserver extends Observer<BaseDataSet> {
        }

        /* loaded from: classes2.dex */
        public interface IIsFirstLoadProcessingMediaObserver extends Observer<Boolean> {
        }

        /* loaded from: classes2.dex */
        public interface IProcessingMediaMapObserver extends Observer<Map<String, ProcessingMedia>> {
        }

        public static /* synthetic */ void $r8$lambda$4e8_ouL4KUxdiJDucKarFYJRzhE(ViewModelData viewModelData, Observer observer) {
            viewModelData.lambda$removeNonResidentMenuItemsObserver$11(observer);
        }

        public static /* synthetic */ void $r8$lambda$7U09JBRMuUVATqjfGORRUMzs17I(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addPrepareDataItemObserver$2(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$HT8jDlnSS8nsBqzZNAyFhx676sQ(ViewModelData viewModelData, Observer observer) {
            viewModelData.lambda$removeInMultiWindowModeObserver$10(observer);
        }

        public static /* synthetic */ void $r8$lambda$S3zP2zxeCWkh94e93iQZKrAwzx8(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addCurrentDataItemObserver$1(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$W9PODLcwrinDKJwOy9qMlSakFBY(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addConfigurationObserver$6(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$XFChtvU5C_6GA3baKB4ropk4NnM(ViewModelData viewModelData, Observer observer) {
            viewModelData.lambda$removeResidentMenuItemsObserver$9(observer);
        }

        public static /* synthetic */ void $r8$lambda$ZcSRqzYo28du15jZXyu4Zb5vJ6o(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addIsSlippedObserver$8(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$hAEQ8eqXpW_7Tl72DrfFHmSplOU(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addResidentMenuItemsObserver$4(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$hQ6VX_3T3LCGpwzMH4WCcDUvXD0(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addCurrentDataSetObserver$3(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$hWw856DNwzKzWgA86jAUT_ssBhs(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addNonResidentMenuItemsObserver$5(lifecycleOwner, observer);
        }

        public static /* synthetic */ void $r8$lambda$l1rchhD3GVeyohgum2ABZLrSlXo(ViewModelData viewModelData, Observer observer) {
            viewModelData.lambda$addInMultiWindowModeObserver$7(observer);
        }

        public static /* synthetic */ void $r8$lambda$vcp0zrwzrFkpj6SbYYzXA9IzvUY(ViewModelData viewModelData, LifecycleOwner lifecycleOwner, Observer observer) {
            viewModelData.lambda$addCurrentPositionObserver$0(lifecycleOwner, observer);
        }

        public void setInMultiWindowMode(boolean z) {
            this.isInMultiWindowMode.setValue(Boolean.valueOf(z));
        }

        public void setConfiguration(Configuration configuration) {
            this.mConfiguration.postValue(configuration);
        }

        public void setProcessingMediaMap(Map<String, ProcessingMedia> map) {
            this.mProcessingMediaMap.postValue(map);
        }

        public void setIsFirstLoadProcessingMedia(boolean z) {
            this.isFirstLoadProcessingMedia.postValue(Boolean.valueOf(z));
        }

        public void setCurrentPosition(int i) {
            this.mCurrentPosition.postValue(Integer.valueOf(i));
        }

        public void setCurrentDataItem(BaseDataItem baseDataItem) {
            this.mCurrentDataItem.postValue(baseDataItem);
        }

        public void setPrepareData(BaseDataItem baseDataItem, int i) {
            this.mPrepareDataItem.setValue(new Pair<>(baseDataItem, Integer.valueOf(i)));
        }

        public void setCurrentDataSet(BaseDataSet baseDataSet) {
            this.mCurrentDataSet.postValue(baseDataSet);
        }

        public void setResidentMenuItems(ArrayList<IMenuItemDelegate> arrayList) {
            this.mResidentMenuItems.postValue(arrayList);
        }

        public void setNonResidentMenuItems(ArrayList<IMenuItemDelegate> arrayList) {
            this.mNonResidentMenuItems.postValue(arrayList);
        }

        public void setSlippedValue(boolean z) {
            this.isSlipped.postValue(Boolean.valueOf(z));
        }

        public void addCurrentPositionObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<Integer> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$vcp0zrwzrFkpj6SbYYzXA9IzvUY(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addCurrentPositionObserver$0(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mCurrentPosition.observe(lifecycleOwner, observer);
        }

        public void addCurrentDataItemObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<BaseDataItem> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$S3zP2zxeCWkh94e93iQZKrAwzx8(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addCurrentDataItemObserver$1(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mCurrentDataItem.observe(lifecycleOwner, observer);
        }

        public void addPrepareDataItemObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<Pair<BaseDataItem, Integer>> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$7U09JBRMuUVATqjfGORRUMzs17I(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addPrepareDataItemObserver$2(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mPrepareDataItem.observe(lifecycleOwner, observer);
        }

        public void addCurrentDataSetObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<BaseDataSet> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$hQ6VX_3T3LCGpwzMH4WCcDUvXD0(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addCurrentDataSetObserver$3(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mCurrentDataSet.observe(lifecycleOwner, observer);
        }

        public void addResidentMenuItemsObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<ArrayList<IMenuItemDelegate>> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$hAEQ8eqXpW_7Tl72DrfFHmSplOU(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addResidentMenuItemsObserver$4(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mResidentMenuItems.observe(lifecycleOwner, observer);
        }

        public void addNonResidentMenuItemsObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<ArrayList<IMenuItemDelegate>> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$hWw856DNwzKzWgA86jAUT_ssBhs(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addNonResidentMenuItemsObserver$5(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mNonResidentMenuItems.observe(lifecycleOwner, observer);
        }

        public void addConfigurationObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<Configuration> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$W9PODLcwrinDKJwOy9qMlSakFBY(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addConfigurationObserver$6(LifecycleOwner lifecycleOwner, Observer observer) {
            this.mConfiguration.observe(lifecycleOwner, observer);
        }

        public void addInMultiWindowModeObserver(FragmentActivity fragmentActivity, LifecycleOwner lifecycleOwner, final Observer<Boolean> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$l1rchhD3GVeyohgum2ABZLrSlXo(DataProvider.ViewModelData.this, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addInMultiWindowModeObserver$7(Observer observer) {
            this.isInMultiWindowMode.observeForever(observer);
        }

        public void addIsSlippedObserver(FragmentActivity fragmentActivity, final LifecycleOwner lifecycleOwner, final Observer<Boolean> observer) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed() || fragmentActivity.isFinishing()) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$ZcSRqzYo28du15jZXyu4Zb5vJ6o(DataProvider.ViewModelData.this, lifecycleOwner, observer);
                }
            });
        }

        public /* synthetic */ void lambda$addIsSlippedObserver$8(LifecycleOwner lifecycleOwner, Observer observer) {
            this.isSlipped.observe(lifecycleOwner, observer);
        }

        public /* synthetic */ void lambda$removeResidentMenuItemsObserver$9(Observer observer) {
            this.mResidentMenuItems.removeObserver(observer);
        }

        public void removeResidentMenuItemsObserver(FragmentActivity fragmentActivity, final Observer<ArrayList<IMenuItemDelegate>> observer) {
            if (fragmentActivity == null) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$XFChtvU5C_6GA3baKB4ropk4NnM(DataProvider.ViewModelData.this, observer);
                }
            });
        }

        public /* synthetic */ void lambda$removeInMultiWindowModeObserver$10(Observer observer) {
            this.isInMultiWindowMode.removeObserver(observer);
        }

        public void removeInMultiWindowModeObserver(FragmentActivity fragmentActivity, final Observer<Boolean> observer) {
            if (fragmentActivity == null) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$HT8jDlnSS8nsBqzZNAyFhx676sQ(DataProvider.ViewModelData.this, observer);
                }
            });
        }

        public /* synthetic */ void lambda$removeNonResidentMenuItemsObserver$11(Observer observer) {
            this.mNonResidentMenuItems.removeObserver(observer);
        }

        public void removeNonResidentMenuItemsObserver(FragmentActivity fragmentActivity, final Observer<ArrayList<IMenuItemDelegate>> observer) {
            if (fragmentActivity == null) {
                return;
            }
            fragmentActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.data.DataProvider$ViewModelData$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    DataProvider.ViewModelData.$r8$lambda$4e8_ouL4KUxdiJDucKarFYJRzhE(DataProvider.ViewModelData.this, observer);
                }
            });
        }

        public void release(GalleryFragment galleryFragment) {
            this.mProcessingMediaMap.removeObservers(galleryFragment);
            this.isFirstLoadProcessingMedia.removeObservers(galleryFragment);
            this.mCurrentPosition.removeObservers(galleryFragment);
            this.mCurrentDataItem.removeObservers(galleryFragment);
            this.mPrepareDataItem.removeObservers(galleryFragment);
            this.mCurrentDataSet.removeObservers(galleryFragment);
            this.mResidentMenuItems.removeObservers(galleryFragment);
            this.mNonResidentMenuItems.removeObservers(galleryFragment);
            this.mConfiguration.removeObservers(galleryFragment);
            this.isInMultiWindowMode.removeObservers(galleryFragment);
            this.isSlipped.removeObservers(galleryFragment);
            this.mResidentMenuItems.setValue(null);
            this.mNonResidentMenuItems.setValue(null);
        }
    }

    public DataProvider(GalleryFragment galleryFragment) {
        FieldData fieldData = new FieldData();
        this.mFieldData = fieldData;
        ViewModelData viewModelData = (ViewModelData) new ViewModelProvider(galleryFragment).get(ViewModelData.class);
        this.mViewModelData = viewModelData;
        this.mPhotoPageLoader = new PhotoPageLoader(galleryFragment, viewModelData);
        FragmentActivity activity = galleryFragment.getActivity();
        if (activity != null) {
            viewModelData.addCurrentPositionObserver(activity, galleryFragment, fieldData.mCurrent.mCurrentPositionObserver);
            viewModelData.addCurrentDataItemObserver(activity, galleryFragment, fieldData.mCurrent.mCurrentDataItemObserver);
            viewModelData.addCurrentDataSetObserver(activity, galleryFragment, fieldData.mCurrent.mCurrentDataSetObserver);
            viewModelData.addConfigurationObserver(activity, galleryFragment, fieldData.mCurrent.mConfigurationObserver);
            viewModelData.addInMultiWindowModeObserver(activity, galleryFragment, fieldData.mCurrent.isInMultiWindowModeObserver);
            viewModelData.addIsSlippedObserver(activity, galleryFragment, fieldData.mCurrent.isSlippedObserver);
            return;
        }
        DefaultLogger.e("DataProvider", "activity is null !");
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public void startLoadData(LifecycleOwner lifecycleOwner, ViewModelData.IBaseDataSetObserver iBaseDataSetObserver, ViewModelData.IProcessingMediaMapObserver iProcessingMediaMapObserver, ViewModelData.IIsFirstLoadProcessingMediaObserver iIsFirstLoadProcessingMediaObserver) {
        if (iBaseDataSetObserver != null) {
            this.mViewModelData.mCurrentDataSet.observe(lifecycleOwner, iBaseDataSetObserver);
        }
        if (iProcessingMediaMapObserver != null) {
            this.mViewModelData.mProcessingMediaMap.observe(lifecycleOwner, iProcessingMediaMapObserver);
        }
        if (iIsFirstLoadProcessingMediaObserver != null) {
            this.mViewModelData.isFirstLoadProcessingMedia.observe(lifecycleOwner, iIsFirstLoadProcessingMediaObserver);
        }
        this.mPhotoPageLoader.startToLoad();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public void onContentChanged() {
        this.mPhotoPageLoader.onContentChanged();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public void loadInBackground() {
        this.mPhotoPageLoader.loadInBackground();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public void cancelBackgroundLoad() {
        this.mPhotoPageLoader.cancelBackgroundLoad();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public Loader getCurrentPhotoLoader() {
        return this.mPhotoPageLoader.getCurrentPhotoLoader();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public boolean isProcessingMedia(BaseDataItem baseDataItem) {
        return this.mPhotoPageLoader.isProcessingMedia(baseDataItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public void release(GalleryFragment galleryFragment) {
        this.mPhotoPageLoader.release();
        this.mViewModelData.removeInMultiWindowModeObserver(galleryFragment.getActivity(), this.mFieldData.mCurrent.isInMultiWindowModeObserver);
        this.mViewModelData.release(galleryFragment);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public ViewModelData getViewModelData() {
        return this.mViewModelData;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.data.IDataProvider
    public FieldData getFieldData() {
        return this.mFieldData;
    }
}
