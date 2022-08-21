package com.miui.gallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CopyOrMoveDialog;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class AddPhotosFragment extends AndroidFragment {
    public long mAlbumId;
    public MediaAndAlbumOperations.OnAddAlbumListener mOnAddAlbumListener;

    /* renamed from: $r8$lambda$-Lt7Uc8LNKKVFaMTRtzid1ZE3go */
    public static /* synthetic */ void m1422$r8$lambda$Lt7Uc8LNKKVFaMTRtzid1ZE3go(AddPhotosFragment addPhotosFragment, long[] jArr, FragmentActivity fragmentActivity, int i) {
        addPhotosFragment.lambda$doAddPhotos$0(jArr, fragmentActivity, i);
    }

    public static void addPhotos(Fragment fragment, long j, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        FragmentTransaction beginTransaction = fragment.getChildFragmentManager().beginTransaction();
        AddPhotosFragment addPhotosFragment = new AddPhotosFragment();
        addPhotosFragment.setOnAddAlbumListener(onAddAlbumListener);
        Bundle bundle = new Bundle();
        bundle.putLong("key_album_id", j);
        addPhotosFragment.setArguments(bundle);
        beginTransaction.add(addPhotosFragment, "AddPhotosFragment");
        beginTransaction.commitAllowingStateLoss();
    }

    public void setOnAddAlbumListener(MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        this.mOnAddAlbumListener = onAddAlbumListener;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new View(getActivity());
    }

    public final void pickPhotos() {
        Intent intent = new Intent(getActivity(), PickGalleryActivity.class);
        intent.putExtra("pick-upper-bound", -1);
        intent.putExtra("pick-need-id", true);
        startActivityForResult(intent, 7);
    }

    public final void doAddPhotos(final long[] jArr) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            CopyOrMoveDialog copyOrMoveDialog = new CopyOrMoveDialog();
            copyOrMoveDialog.setOnOperationSelectedListener(new CopyOrMoveDialog.OnOperationSelectedListener() { // from class: com.miui.gallery.ui.AddPhotosFragment$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.CopyOrMoveDialog.OnOperationSelectedListener
                public final void onOperationSelected(FragmentActivity fragmentActivity, int i) {
                    AddPhotosFragment.m1422$r8$lambda$Lt7Uc8LNKKVFaMTRtzid1ZE3go(AddPhotosFragment.this, jArr, fragmentActivity, i);
                }
            });
            copyOrMoveDialog.showAllowingStateLoss(parentFragment.getFragmentManager(), "CopyOrMoveDialog");
            return;
        }
        DefaultLogger.e("AddPhotosFragment", "Add photo show CopyOrMoveDialog fail,Do copy operation for default!");
        recordCopyMoveAction(false);
    }

    public /* synthetic */ void lambda$doAddPhotos$0(long[] jArr, FragmentActivity fragmentActivity, int i) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        if (i == 1) {
            CopyMoveDialogFragment.show(fragmentActivity, this.mAlbumId, jArr, false, this.mOnAddAlbumListener);
            recordCopyMoveAction(false);
        } else if (i == 2) {
        } else {
            CopyMoveDialogFragment.show(fragmentActivity, this.mAlbumId, jArr, true, this.mOnAddAlbumListener);
            recordCopyMoveAction(true);
        }
    }

    public static void recordCopyMoveAction(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "move" + String.valueOf(z));
        hashMap.put("from", "AddPhotosFragment");
        SamplingStatHelper.recordCountEvent("organize_photos", "move_or_copy", hashMap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("pick-result-data");
            if (arrayList == null || arrayList.isEmpty()) {
                setResult(0);
                return;
            }
            long[] jArr = new long[arrayList.size()];
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                jArr[i3] = ((Long) arrayList.get(i3)).longValue();
            }
            doAddPhotos(jArr);
            setResult(-1);
            return;
        }
        setResult(0);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        this.mAlbumId = arguments != null ? arguments.getLong("key_album_id", 0L) : 0L;
        boolean z = arguments != null && arguments.getBoolean("key_launch_pick", false);
        if (this.mAlbumId == 0) {
            setResult(0);
        } else if (z) {
        } else {
            pickPhotos();
            if (arguments == null) {
                return;
            }
            arguments.putBoolean("key_launch_pick", true);
        }
    }

    public final void setResult(int i) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.onActivityResult(33, i, getArguments() != null ? new Intent().putExtras(getArguments()) : null);
        }
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}
