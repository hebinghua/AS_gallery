package com.miui.gallery.ui.photoPage;

import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.activity.BaseExternalPhotoPageActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.IPhotoPageManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.OrientationSensor;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class PhotoPageOrientationManager extends PhotoPageBaseManager implements OrientationSensor.OnOrientationChangedListener {
    public boolean isEnterScreenOrientationChanged;
    public boolean isEnterScreenOrientationLocked;
    public ContentObserver mAccelerometerRotationObserver;
    public int mCurrentDegree;
    public int mCurrentRequestOrientation;
    public IDataProvider mDataProvider;
    public int mEnterRequestedOrientation;
    public boolean mFirstResume;
    public boolean mIsOrientationLocked;
    public OrientationSensor mOrientationSensor;
    public int mRestoreOrientation;
    public boolean mSensorEnable;
    public boolean needAdjustScreenOrientationIfFromCamera;

    /* loaded from: classes2.dex */
    public interface IPhotoPageOrientationManagerController extends IPhotoPageManager.IPhotoPageManagerController {
        boolean checkSensorAvailable();

        int getEnterRequestedOrientation();

        int getRequestedOrientation();

        boolean isLandscapeConfiguration();

        boolean isOrientationChanged();

        boolean isOrientationLocked();

        boolean isPortraitConfiguration();

        boolean isScreenOrientationLocked();

        void noteRestoreOrientation();

        void onLockClick();

        void onRotateClick();

        void setDataProvider(IDataProvider iDataProvider);

        void setOrientationLocked(boolean z);

        boolean setRequestedOrientation(int i, String str);

        void setSensorEnable(boolean z);

        void tryRestoreOrientation();
    }

    public final int getScreenOrientation(int i) {
        if (i != 0) {
            if (i == 1) {
                return 0;
            }
            if (i == 2) {
                return 9;
            }
            return i != 3 ? 2 : 8;
        }
        return 1;
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onDestroyView() {
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onPause() {
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onViewInflated() {
    }

    public IPhotoPageOrientationManagerController getManagerController() {
        return new PhotoPageOrientationManagerController();
    }

    public PhotoPageOrientationManager(PhotoPageFragment photoPageFragment, Map<String, Object> map, IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback) {
        super(photoPageFragment, map, iPhotoPageManagerCallback);
        this.mEnterRequestedOrientation = -1;
        this.mCurrentRequestOrientation = -1;
        this.mRestoreOrientation = -1;
        this.mSensorEnable = true;
        this.mFirstResume = true;
        this.mCurrentDegree = -1;
        this.needAdjustScreenOrientationIfFromCamera = this.isFromCamera;
        this.mOrientationSensor = new OrientationSensor(this.mActivity);
    }

    /* loaded from: classes2.dex */
    public class PhotoPageOrientationManagerController implements IPhotoPageOrientationManagerController {
        public PhotoPageOrientationManagerController() {
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void setSensorEnable(boolean z) {
            PhotoPageOrientationManager.this.setSensorEnable(z);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean checkSensorAvailable() {
            return PhotoPageOrientationManager.this.isSensorEnable();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void tryRestoreOrientation() {
            PhotoPageOrientationManager.this.tryRestoreOrientation();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean isScreenOrientationLocked() {
            return PhotoPageOrientationManager.this.isScreenOrientationLocked();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean isOrientationLocked() {
            return PhotoPageOrientationManager.this.isOrientationLocked();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void setOrientationLocked(boolean z) {
            PhotoPageOrientationManager.this.setOrientationLocked(z);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean setRequestedOrientation(int i, String str) {
            return PhotoPageOrientationManager.this.setRequestedOrientation(i, str);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public int getRequestedOrientation() {
            return PhotoPageOrientationManager.this.getRequestedOrientation();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public int getEnterRequestedOrientation() {
            return PhotoPageOrientationManager.this.getEnterRequestedOrientation();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean isOrientationChanged() {
            return PhotoPageOrientationManager.this.isOrientationChanged();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean isLandscapeConfiguration() {
            return PhotoPageOrientationManager.this.isLandscapeConfiguration();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public boolean isPortraitConfiguration() {
            return PhotoPageOrientationManager.this.isPortraitConfiguration();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void onRotateClick() {
            PhotoPageOrientationManager.this.onRotateClick();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void onLockClick() {
            PhotoPageOrientationManager.this.onLockClick();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void noteRestoreOrientation() {
            PhotoPageOrientationManager.this.noteRestoreOrientation();
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageOrientationManager.IPhotoPageOrientationManagerController
        public void setDataProvider(IDataProvider iDataProvider) {
            PhotoPageOrientationManager.this.mDataProvider = iDataProvider;
        }
    }

    /* loaded from: classes2.dex */
    public static final class AccelerometerRotationChangedListener extends ContentObserver {
        public final WeakReference<PhotoPageOrientationManager> mRef;

        public AccelerometerRotationChangedListener(Handler handler, PhotoPageOrientationManager photoPageOrientationManager) {
            super(handler);
            this.mRef = new WeakReference<>(photoPageOrientationManager);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            PhotoPageOrientationManager photoPageOrientationManager;
            WeakReference<PhotoPageOrientationManager> weakReference = this.mRef;
            if (weakReference == null || weakReference.get() == null || (photoPageOrientationManager = this.mRef.get()) == null) {
                return;
            }
            photoPageOrientationManager.onAccelerometerRotationChanged();
        }
    }

    @Override // com.miui.gallery.util.OrientationSensor.OnOrientationChangedListener
    public void onOrientationChanged(int i, int i2) {
        IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback = this.mPhotoPageManagerCallback;
        if (iPhotoPageManagerCallback != null) {
            iPhotoPageManagerCallback.onOrientationChanged(i, i2);
        }
        this.mCurrentDegree = i2;
        if (!this.mIsOrientationLocked && this.needAdjustScreenOrientationIfFromCamera) {
            setRequestedOrientation(4, "onOrientationChanged");
            this.needAdjustScreenOrientationIfFromCamera = false;
        }
        DefaultLogger.d("PhotoPageFragment_Orientation", "onSensorOrientationChanged old %s, new %s", Integer.valueOf(i), Integer.valueOf(i2));
    }

    public final void onAccelerometerRotationChanged() {
        boolean z;
        int i;
        boolean z2 = false;
        this.mIsOrientationLocked = false;
        IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback = this.mPhotoPageManagerCallback;
        if (iPhotoPageManagerCallback != null) {
            iPhotoPageManagerCallback.onAccelerometerRotationChange();
        }
        boolean isScreenOrientationLocked = isScreenOrientationLocked();
        int i2 = -1;
        if (!this.isEnterScreenOrientationLocked || !isScreenOrientationLocked) {
            z = false;
        } else {
            i2 = getScreenOrientation(((WindowManager) this.mActivity.getSystemService("window")).getDefaultDisplay().getRotation());
            z = true;
        }
        DefaultLogger.d("PhotoPageFragment_Orientation", "onAccelerometerRotationChanged, lock = %b", Boolean.valueOf(isScreenOrientationLocked));
        if (isScreenOrientationLocked && !this.isFromCamera) {
            lock();
        } else {
            unlock();
        }
        if (z && i2 == (i = this.mCurrentRequestOrientation) && i != this.mEnterRequestedOrientation) {
            z2 = true;
        }
        this.isEnterScreenOrientationChanged = z2;
    }

    public final void lock() {
        int rotation = ((WindowManager) this.mActivity.getSystemService("window")).getDefaultDisplay().getRotation();
        if (setRequestedOrientation(getScreenOrientation(rotation), "lock")) {
            this.mIsOrientationLocked = true;
            recordCountEvent(true, rotation);
        }
    }

    public final void unlock() {
        if (setRequestedOrientation(4, "unlock")) {
            this.mIsOrientationLocked = false;
            int i = this.mCurrentDegree;
            if (i != -1) {
                i = OrientationSensor.transfer2SurfaceRotation(i);
            }
            recordCountEvent(false, i);
        }
    }

    public final void recordCountEvent(boolean z, int i) {
        HashMap hashMap = new HashMap();
        String str = z ? "lock" : "unlock";
        hashMap.put(str, i + "");
        SamplingStatHelper.recordCountEvent("photo", "orientation_lock_by_accelerometer", hashMap);
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onCreate(Bundle bundle) {
        this.isEnterScreenOrientationLocked = isScreenOrientationLocked();
        this.mIsOrientationLocked = bundle != null && bundle.getBoolean("photo_locked_state");
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onStart() {
        int i;
        boolean isScreenOrientationLocked = isScreenOrientationLocked();
        DefaultLogger.d("PhotoPageFragment_Orientation", "isInFreeWindowMode [%b] isFromCamera [%b] isScreenOrientationLocked [%b] mEnterRequestedOrientation [%d] mCurrentRequestOrientation [%d]", Boolean.valueOf(this.isInFreeWindowMode), Boolean.valueOf(this.isFromCamera), Boolean.valueOf(isScreenOrientationLocked), Integer.valueOf(this.mEnterRequestedOrientation), Integer.valueOf(this.mCurrentRequestOrientation));
        if (this.isInFreeWindowMode || this.isFromCamera || !isScreenOrientationLocked || (i = this.mCurrentRequestOrientation) == 1 || i == 9 || i == 0 || i == 8) {
            return;
        }
        int screenOrientation = getScreenOrientation(((WindowManager) this.mActivity.getSystemService("window")).getDefaultDisplay().getRotation());
        this.mEnterRequestedOrientation = screenOrientation;
        setRequestedOrientation(screenOrientation, "onStart_setScreenConstantOrientation");
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onResume() {
        enableOrientationSensor();
        enableAccelerometerRotationObserver();
        if (this.isFromCamera) {
            tryRestoreOrientation();
        }
        if (this.mFirstResume) {
            this.mFirstResume = false;
        }
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onDoExit() {
        if (!isScreenOrientationLocked() && !this.isFromCamera) {
            setRequestedOrientation(4, "onDoExit");
        }
        if (!isOrientationChanged() || !this.isEnterScreenOrientationLocked || !isScreenOrientationLocked() || this.isFromCamera) {
            return;
        }
        setRequestedOrientation(getEnterRequestedOrientation(), "onDoExit");
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onDestroy() {
        disableOrientationSensor();
        disableAccelerometerRotationObserver();
        this.mOrientationSensor = null;
        this.mAccelerometerRotationObserver = null;
        this.mCurrentRequestOrientation = -1;
        this.mEnterRequestedOrientation = -1;
        this.mSensorEnable = true;
        this.mFirstResume = true;
        this.mIsOrientationLocked = false;
        this.needAdjustScreenOrientationIfFromCamera = false;
        this.mCurrentDegree = -1;
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onSaveInstanceState(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        bundle.putBoolean("photo_locked_state", this.mIsOrientationLocked);
    }

    @Override // com.miui.gallery.ui.photoPage.IPhotoPageManager
    public void onSettleItem(BaseDataItem baseDataItem) {
        setSensorEnable(baseDataItem != null);
    }

    public final void setSensorEnable(boolean z) {
        this.mSensorEnable = z;
        if (this.mFragment.isResumed()) {
            if (this.mSensorEnable) {
                enableOrientationSensor();
            } else {
                disableOrientationSensor();
            }
        }
    }

    public final boolean isSensorEnable() {
        return !isScreenOrientationLocked() && !isOrientationLocked();
    }

    public final void enableOrientationSensor() {
        OrientationSensor orientationSensor;
        if (!this.mSensorEnable || !this.mFragment.isResumed() || (orientationSensor = this.mOrientationSensor) == null || orientationSensor.isEnabled()) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_Orientation", "enableOrientationSensor");
        this.mOrientationSensor.setOrientationChangedListener(this);
        this.mOrientationSensor.enable();
    }

    public final void disableOrientationSensor() {
        OrientationSensor orientationSensor = this.mOrientationSensor;
        if (orientationSensor == null || !orientationSensor.isEnabled()) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_Orientation", "disableOrientationSensor");
        this.mOrientationSensor.setOrientationChangedListener(null);
        this.mOrientationSensor.disable();
    }

    public final void enableAccelerometerRotationObserver() {
        if (this.mFragment.isResumed() && this.mAccelerometerRotationObserver == null) {
            DefaultLogger.d("PhotoPageFragment_Orientation", "enableAccelerometerRotationObserver");
            this.mAccelerometerRotationObserver = new AccelerometerRotationChangedListener(ThreadManager.getMainHandler(), this);
            this.mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor("accelerometer_rotation"), true, this.mAccelerometerRotationObserver);
        }
    }

    public final void disableAccelerometerRotationObserver() {
        if (this.mAccelerometerRotationObserver != null) {
            DefaultLogger.d("PhotoPageFragment_Orientation", "disableAccelerometerRotationObserver");
            this.mActivity.getContentResolver().unregisterContentObserver(this.mAccelerometerRotationObserver);
            this.mAccelerometerRotationObserver = null;
        }
    }

    public final void noteRestoreOrientation() {
        this.mRestoreOrientation = this.mCurrentRequestOrientation;
    }

    public final int getRestoreOrientation() {
        int i = this.mRestoreOrientation;
        return i == -1 ? this.mEnterRequestedOrientation : i;
    }

    public final void tryRestoreOrientation() {
        if (this.isInFreeWindowMode) {
            return;
        }
        if (this.isFromCamera && this.mFirstResume) {
            return;
        }
        int restoreOrientation = getRestoreOrientation();
        boolean isScreenOrientationLocked = isScreenOrientationLocked();
        if (restoreOrientation == 4 && isScreenOrientationLocked && !this.isFromCamera) {
            return;
        }
        if (this.isFromCamera || (!isScreenOrientationLocked && !isOrientationLocked())) {
            restoreOrientation = 4;
        }
        DefaultLogger.d("PhotoPageFragment_Orientation", "tryRestoreOrientation orientation is %d", Integer.valueOf(restoreOrientation));
        setRequestedOrientation(restoreOrientation, "tryRestoreOrientation");
    }

    public final void onRotateClick() {
        int i;
        if (isLandscapeConfiguration()) {
            i = 1;
        } else {
            i = this.mCurrentDegree == 90 ? 8 : 0;
        }
        setRequestedOrientation(i, "onRotateClick");
        recordRotateClickEvent(i);
    }

    public final void onLockClick() {
        setOrientationLocked(!this.mIsOrientationLocked);
        SamplingStatHelper.recordCountEvent("photo", "click_lock_orientation_button");
        TrackController.trackClick("403.11.3.1.11152", "403.11.0.1.11151");
    }

    public final void setOrientationLocked(boolean z) {
        if (z) {
            lock();
        } else {
            unlock();
        }
    }

    public final void recordRotateClickEvent(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, i + "");
        SamplingStatHelper.recordCountEvent("photo", "click_rotate_button", hashMap);
        TrackController.trackClick("403.11.3.1.11153", "403.11.0.1.11151");
    }

    public final boolean isScreenOrientationLocked() {
        return Settings.System.getInt(this.mActivity.getContentResolver(), "accelerometer_rotation", 0) == 0;
    }

    public final boolean isOrientationLocked() {
        return this.mIsOrientationLocked;
    }

    public final boolean isOrientationChanged() {
        if (!this.isFromCamera) {
            return this.mCurrentRequestOrientation != this.mEnterRequestedOrientation && isScreenOrientationLocked() && !this.isEnterScreenOrientationChanged;
        }
        int i = this.mCurrentDegree;
        return (-1 == i || i == 0 || BaseExternalPhotoPageActivity.getScreenOrientation(i) == this.mEnterRequestedOrientation) ? false : true;
    }

    public final int getEnterRequestedOrientation() {
        return this.mEnterRequestedOrientation;
    }

    public final boolean setRequestedOrientation(int i, String str) {
        if (this.mCurrentRequestOrientation == i) {
            return false;
        }
        this.mCurrentRequestOrientation = i;
        DefaultLogger.d("PhotoPageFragment_Orientation", "setRequestedOrientation orientation is %d invoker [%s]", Integer.valueOf(i), str);
        return SystemUiUtil.setRequestedOrientation(i, this.mActivity);
    }

    public final int getRequestedOrientation() {
        int requestedOrientation = this.mActivity.getRequestedOrientation();
        if (this.mCurrentRequestOrientation != requestedOrientation) {
            this.mCurrentRequestOrientation = requestedOrientation;
        }
        return this.mCurrentRequestOrientation;
    }

    public final boolean isLandscapeConfiguration() {
        return this.mActivity.getResources().getConfiguration().orientation == 2;
    }

    public final boolean isPortraitConfiguration() {
        return this.mActivity.getResources().getConfiguration().orientation == 1;
    }
}
