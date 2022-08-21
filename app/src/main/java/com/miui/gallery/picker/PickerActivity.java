package com.miui.gallery.picker;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import miui.gallery.support.MiuiSdkCompat;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public abstract class PickerActivity extends PickerCompatActivity {
    public static final String[] PICKABLE_PROJECTION = {j.c, "sha1", "microthumbfile", "thumbnailFile", "localFile", "serverType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageLength", "exifImageWidth", "title", "mimeType"};
    public Decor mDecor;
    public Picker mPicker;
    public CharSequence mPickerTitle;
    public int mScreenSize;
    public CharSequence mTitle;

    public abstract void onDone(int i);

    public void onScreenSizeChange(Configuration configuration) {
    }

    public void onScreenSizeChangeWhenCreate(Configuration configuration) {
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            restoreInstanceState(bundle);
        } else {
            try {
                Picker onCreatePicker = onCreatePicker();
                this.mPicker = onCreatePicker;
                onCreatePicker.setFromType(getIntent().getIntExtra("extra_from_type", -1));
            } catch (RuntimeException e) {
                DefaultLogger.e("PickerActivity", e);
                super.onCreate(bundle);
                finish();
                return;
            }
        }
        Decor onCreateDecor = onCreateDecor();
        this.mDecor = onCreateDecor;
        onCreateDecor.applyTheme();
        super.onCreate(bundle);
        this.mDecor.applyActionBar();
        updateTitle();
    }

    @Override // com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.screenLayout & 15;
        if (this.mScreenSize != i) {
            onScreenSizeChange(configuration);
            this.mScreenSize = i;
        }
        initActionBar();
        this.mDecor.applyActionBar();
        this.mDecor.setTitle(this.mTitle);
        this.mDecor.setPickerTitle(this.mPickerTitle);
    }

    public Picker onCreatePicker() {
        int i;
        Intent intent = getIntent();
        String resolveType = intent.resolveType(this);
        int i2 = 1;
        if (intent.hasExtra("pick-upper-bound")) {
            i = intent.getIntExtra("pick-upper-bound", -1);
            DefaultLogger.d("PickerActivity", "initial pick bound: %d", Integer.valueOf(i));
        } else if (intent.getBooleanExtra("android.intent.extra.ALLOW_MULTIPLE", false)) {
            DefaultLogger.d("PickerActivity", "standard pick multiple");
            i = -1;
        } else {
            i = 1;
        }
        int intExtra = intent.getIntExtra("pick-lower-bound", 1);
        if (intExtra < 1) {
            intExtra = 1;
        }
        if (i != -1 && intExtra <= i) {
            i2 = intExtra;
        }
        SimplePicker simplePicker = new SimplePicker(this, i, i2, new LinkedHashSet());
        if ("image/*".equals(resolveType)) {
            simplePicker.setMediaType(Picker.MediaType.IMAGE);
        } else if ("video/*".equals(resolveType)) {
            simplePicker.setMediaType(Picker.MediaType.VIDEO);
        } else if ("vnd.android.cursor.dir/image".equals(resolveType)) {
            simplePicker.setMediaType(Picker.MediaType.IMAGE);
        } else if ("vnd.android.cursor.dir/video".equals(resolveType)) {
            simplePicker.setMediaType(Picker.MediaType.VIDEO);
        } else {
            simplePicker.setMediaType(Picker.MediaType.ALL);
        }
        if (intent.getBooleanExtra("pick-need-id", false)) {
            simplePicker.setResultType(Picker.ResultType.ID);
        } else if (GalleryOpenProvider.needReturnContentUri(this, getCallingPackage())) {
            simplePicker.setResultType(Picker.ResultType.OPEN_URI);
        } else if ("vnd.android.cursor.dir/image".equals(resolveType) || "vnd.android.cursor.dir/video".equals(resolveType)) {
            simplePicker.setResultType(Picker.ResultType.LEGACY_MEDIA);
        } else {
            simplePicker.setResultType(Picker.ResultType.LEGACY_GENERAL);
        }
        if (intent.getBooleanExtra("pick-need-origin", false)) {
            simplePicker.setImageType(Picker.ImageType.ORIGIN);
        } else if (intent.getBooleanExtra("pick-need-origin-download-info", false)) {
            simplePicker.setImageType(Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO);
        }
        if (intent.hasExtra("extra_filter_media_type")) {
            simplePicker.setFilterMimeTypes(intent.getStringArrayExtra("extra_filter_media_type"));
        }
        simplePicker.setPickOwner(intent.getBooleanExtra("pick-owner", false));
        DefaultLogger.d("PickerActivity", "creating picker, capacity is %d", Integer.valueOf(i));
        return simplePicker;
    }

    public Decor onCreateDecor() {
        return Decor.create(this);
    }

    public void onCancel() {
        super.finish();
    }

    @Override // com.miui.gallery.picker.PickerCompatActivity, android.app.Activity
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mDecor.setTitle(charSequence);
    }

    public final void setPickerTitle(CharSequence charSequence) {
        this.mPickerTitle = charSequence;
        this.mDecor.setPickerTitle(charSequence);
    }

    public Picker getPicker() {
        return this.mPicker;
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof PickerImpl) {
            ((PickerImpl) fragment).attach(this.mPicker);
        }
    }

    public final void updateTitle() {
        String quantityString;
        int count = this.mPicker.count();
        Picker.Mode mode = this.mPicker.getMode();
        if (count > 0) {
            if (this.mPicker.getMediaType() == Picker.MediaType.VIDEO) {
                quantityString = getString(R.string.picker_title_format_video, new Object[]{Integer.valueOf(count)});
            } else {
                quantityString = (mode != Picker.Mode.MULTIPLE || this.mPicker.capacity() == 1000) ? mo546getActivity().getResources().getQuantityString(R.plurals.miuix_appcompat_items_selected, count, Integer.valueOf(count)) : getString(R.string.picker_title_selection_format_multiple, new Object[]{Integer.valueOf(this.mPicker.baseline()), Integer.valueOf(this.mPicker.capacity()), Integer.valueOf(count)});
            }
            setPickerTitle(quantityString);
        } else if (mode == Picker.Mode.MULTIPLE && this.mPicker.capacity() != 1000) {
            if (this.mPicker.baseline() == this.mPicker.capacity()) {
                setPickerTitle(getString(R.string.picker_title_specify_format, new Object[]{Integer.valueOf(this.mPicker.baseline())}));
            } else if (this.mPicker.getMediaType() == Picker.MediaType.VIDEO) {
                setPickerTitle(getString(R.string.picker_title_format_video, new Object[]{Integer.valueOf(this.mPicker.count())}));
            } else {
                setPickerTitle(getString(R.string.picker_title_format, new Object[]{Integer.valueOf(this.mPicker.baseline()), Integer.valueOf(this.mPicker.capacity())}));
            }
        } else if (mode == Picker.Mode.SINGLE) {
            if (this.mPicker.getMediaType() == Picker.MediaType.VIDEO) {
                setPickerTitle(getString(R.string.picker_title_format_video, new Object[]{Integer.valueOf(count)}));
            } else {
                setPickerTitle(getString(R.string.picker_title_specify_format_one, new Object[]{Integer.valueOf(this.mPicker.capacity())}));
            }
        } else {
            setPickerTitle(getString(R.string.custom_select_title_select_item));
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("screenSize", this.mScreenSize);
        bundle.putSerializable("com.miui.gallery.picker.PickerActivity.results", copyPicker(this.mPicker));
        bundle.putSerializable("com.miui.gallery.picker.PickerActivity.capacity", Integer.valueOf(this.mPicker.capacity()));
        bundle.putSerializable("com.miui.gallery.picker.PickerActivity.baseline", Integer.valueOf(this.mPicker.baseline()));
        bundle.putInt("com.miui.gallery.picker.PickerActivity.media_type", this.mPicker.getMediaType().ordinal());
        bundle.putInt("com.miui.gallery.picker.PickerActivity.result_type", this.mPicker.getResultType().ordinal());
        bundle.putInt("com.miui.gallery.picker.PickerActivity.from_type", this.mPicker.getFromType());
    }

    public final void restoreInstanceState(Bundle bundle) {
        DefaultLogger.d("PickerActivity", "restore instance state for picker: ");
        Configuration configuration = getResources().getConfiguration();
        this.mScreenSize = configuration.screenLayout & 15;
        if (this.mScreenSize != bundle.getInt("screenSize")) {
            onScreenSizeChangeWhenCreate(configuration);
        }
        int i = bundle.getInt("com.miui.gallery.picker.PickerActivity.capacity", 1);
        int i2 = bundle.getInt("com.miui.gallery.picker.PickerActivity.baseline", 1);
        Set set = (Set) bundle.getSerializable("com.miui.gallery.picker.PickerActivity.results");
        if (set == null) {
            set = new LinkedHashSet(i);
        }
        SimplePicker simplePicker = new SimplePicker(this, i, i2, set);
        this.mPicker = simplePicker;
        simplePicker.setFromType(bundle.getInt("com.miui.gallery.picker.PickerActivity.from_type"));
        int i3 = bundle.getInt("com.miui.gallery.picker.PickerActivity.media_type");
        int i4 = bundle.getInt("com.miui.gallery.picker.PickerActivity.result_type");
        this.mPicker.setMediaType(Picker.MediaType.values()[i3]);
        this.mPicker.setResultType(Picker.ResultType.values()[i4]);
        DefaultLogger.d("PickerActivity", "picker[capacity:%d, size:%d] restored.", Integer.valueOf(i), Integer.valueOf(set.size()));
    }

    public static HashSet<String> copyPicker(Picker picker) {
        if (picker != null) {
            LinkedHashSet linkedHashSet = new LinkedHashSet(picker.count());
            Iterator<String> it = picker.iterator();
            while (it.hasNext()) {
                linkedHashSet.add(it.next());
            }
            return linkedHashSet;
        }
        return new LinkedHashSet(0);
    }

    /* loaded from: classes2.dex */
    public static abstract class Decor {
        public PickerActivity mActivity;

        public abstract void applyActionBar();

        public abstract void applyTheme();

        public abstract void setPickerTitle(CharSequence charSequence);

        public abstract void setTitle(CharSequence charSequence);

        public Decor(PickerActivity pickerActivity) {
            this.mActivity = pickerActivity;
        }

        public static Decor create(PickerActivity pickerActivity) {
            return new Multiple(pickerActivity);
        }

        /* loaded from: classes2.dex */
        public static class Multiple extends Decor {
            public Button mDoneButton;
            public TextView mTitle;

            @Override // com.miui.gallery.picker.PickerActivity.Decor
            public void applyTheme() {
            }

            @Override // com.miui.gallery.picker.PickerActivity.Decor
            public void setTitle(CharSequence charSequence) {
            }

            public Multiple(PickerActivity pickerActivity) {
                super(pickerActivity);
            }

            @Override // com.miui.gallery.picker.PickerActivity.Decor
            public void applyActionBar() {
                DefaultLogger.d("PickerActivity", "applyActionBar");
                ActionBar appCompatActionBar = this.mActivity.getAppCompatActionBar();
                boolean z = true;
                appCompatActionBar.setDisplayShowCustomEnabled(true);
                appCompatActionBar.setTabsMode(false);
                View customView = appCompatActionBar.getCustomView();
                this.mTitle = (TextView) customView.findViewById(16908310);
                Button button = (Button) customView.findViewById(16908313);
                MiuiSdkCompat.setEditActionModeButton(this.mActivity, button, 3);
                button.setContentDescription(this.mActivity.getResources().getString(R.string.back));
                button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.picker.PickerActivity.Decor.Multiple.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        Multiple.this.mActivity.mPicker.cancel();
                    }
                });
                Button button2 = (Button) customView.findViewById(16908314);
                this.mDoneButton = button2;
                MiuiSdkCompat.setEditActionModeButton(this.mActivity, button2, 2);
                this.mDoneButton.setOnClickListener(new SingleClickListener() { // from class: com.miui.gallery.picker.PickerActivity.Decor.Multiple.2
                    @Override // com.miui.gallery.listener.SingleClickListener
                    public void onSingleClick(View view) {
                        Multiple.this.mActivity.mPicker.done(-1);
                    }
                });
                if (this.mActivity.mPicker.getMode() == Picker.Mode.SINGLE) {
                    this.mDoneButton.setVisibility(4);
                } else {
                    Button button3 = this.mDoneButton;
                    if (this.mActivity.mPicker.count() < this.mActivity.mPicker.baseline()) {
                        z = false;
                    }
                    button3.setEnabled(z);
                }
                this.mActivity.mPicker.registerObserver(new DataSetObserver() { // from class: com.miui.gallery.picker.PickerActivity.Decor.Multiple.3
                    @Override // android.database.DataSetObserver
                    public void onChanged() {
                        super.onChanged();
                        Multiple.this.mActivity.updateTitle();
                        Multiple.this.mDoneButton.setEnabled(Multiple.this.mActivity.mPicker.count() >= Multiple.this.mActivity.mPicker.baseline());
                    }

                    @Override // android.database.DataSetObserver
                    public void onInvalidated() {
                        super.onInvalidated();
                        Multiple.this.mActivity.updateTitle();
                        Multiple.this.mDoneButton.setEnabled(Multiple.this.mActivity.mPicker.count() >= Multiple.this.mActivity.mPicker.baseline());
                    }
                });
            }

            @Override // com.miui.gallery.picker.PickerActivity.Decor
            public void setPickerTitle(CharSequence charSequence) {
                this.mTitle.setText(charSequence);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SimplePicker implements Picker {
        public final int mBaseline;
        public final int mCapacity;
        public String[] mFilterMimeTypes;
        public int mFromType;
        public Picker.ImageType mImageType = Picker.ImageType.THUMBNAIL;
        public Picker.MediaType mMediaType;
        public DataSetObservable mObservable;
        public Picker.Mode mPickMode;
        public boolean mPickOwner;
        public PickerActivity mPickerActivity;
        public Picker.ResultType mResultType;
        public Set<String> mResults;

        public SimplePicker(PickerActivity pickerActivity, int i, int i2, Set<String> set) {
            if (set == null) {
                throw new IllegalArgumentException("Result can't be null");
            }
            i = i == -1 ? 1000 : i;
            if (i >= 0 && i <= 1) {
                this.mPickMode = Picker.Mode.SINGLE;
                i = 1;
            } else {
                this.mPickMode = Picker.Mode.MULTIPLE;
            }
            if (set.size() > i) {
                throw new IllegalArgumentException(String.format("ResultMap size (%d) is too large this picker (%d)", Integer.valueOf(set.size()), Integer.valueOf(i)));
            }
            this.mPickerActivity = pickerActivity;
            this.mResults = set;
            this.mCapacity = i;
            this.mBaseline = i2;
            this.mObservable = new DataSetObservable();
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean pick(String str) {
            if (this.mPickMode == Picker.Mode.SINGLE) {
                this.mResults.clear();
            } else if (isFull()) {
                return false;
            }
            boolean add = this.mResults.add(str);
            if (add) {
                this.mObservable.notifyChanged();
            }
            return add;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setFilterMimeTypes(String[] strArr) {
            this.mFilterMimeTypes = strArr;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public String[] getFilterMimeTypes() {
            return this.mFilterMimeTypes;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean pickAll(List<String> list) {
            if (isFull()) {
                return false;
            }
            int size = list.size();
            int i = 0;
            boolean z = false;
            while (!isFull() && i < size) {
                int min = Math.min(Math.max(0, capacity() - count()) + i, size);
                z |= this.mResults.addAll(list.subList(i, min));
                i = min;
            }
            if (z) {
                this.mObservable.notifyChanged();
            }
            return z;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean remove(String str) {
            boolean remove = this.mResults.remove(str);
            if (remove) {
                this.mObservable.notifyChanged();
            }
            return remove;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean removeAll(List<String> list) {
            boolean removeAll = this.mResults.removeAll(list);
            if (removeAll) {
                this.mObservable.notifyChanged();
            }
            return removeAll;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean clear() {
            if (this.mResults.isEmpty()) {
                return false;
            }
            this.mResults.clear();
            this.mObservable.notifyChanged();
            return true;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean contains(String str) {
            return this.mResults.contains(str);
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public int capacity() {
            return this.mCapacity;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public int baseline() {
            return this.mBaseline;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public int count() {
            return this.mResults.size();
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public Picker.MediaType getMediaType() {
            return this.mMediaType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setMediaType(Picker.MediaType mediaType) {
            this.mMediaType = mediaType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setImageType(Picker.ImageType imageType) {
            this.mImageType = imageType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public Picker.ImageType getImageType() {
            return this.mImageType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setResultType(Picker.ResultType resultType) {
            this.mResultType = resultType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public Picker.ResultType getResultType() {
            return this.mResultType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void done(int i) {
            LinearMotorHelper.performHapticFeedback(this.mPickerActivity, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            this.mPickerActivity.onDone(i);
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void cancel() {
            LinearMotorHelper.performHapticFeedback(this.mPickerActivity, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            this.mPickerActivity.onCancel();
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean isFull() {
            return count() >= capacity();
        }

        @Override // java.lang.Iterable
        public Iterator<String> iterator() {
            return this.mResults.iterator();
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public Picker.Mode getMode() {
            return this.mPickMode;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setFromType(int i) {
            this.mFromType = i;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public int getFromType() {
            return this.mFromType;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public boolean isPickOwner() {
            return this.mPickOwner;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void setPickOwner(boolean z) {
            this.mPickOwner = z;
        }

        @Override // com.miui.gallery.picker.helper.Picker
        public void registerObserver(DataSetObserver dataSetObserver) {
            this.mObservable.registerObserver(dataSetObserver);
        }

        public String toString() {
            return "SimplePicker{mResults=" + this.mResults + '}';
        }
    }
}
