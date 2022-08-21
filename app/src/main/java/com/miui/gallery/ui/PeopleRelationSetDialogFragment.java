package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.ui.PeopleRelationCreatorDialogFragment;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PeopleRelationSetDialogFragment extends GalleryDialogFragment {
    public RelationSelectedListener mRelationSelectedListener;
    public ArrayList<String> mRelationNameItems = new ArrayList<>();
    public ArrayList<String> mRelationValueItems = new ArrayList<>();

    /* loaded from: classes2.dex */
    public interface RelationSelectedListener {
        void onRelationSelected(String str, String str2);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initDialogAdapter();
    }

    public final void initDialogAdapter() {
        this.mRelationNameItems.addAll(getArguments().getStringArrayList("relation_names"));
        this.mRelationValueItems.addAll(getArguments().getStringArrayList("relation_values"));
        this.mRelationNameItems.add(getString(R.string.define_by_user));
        this.mRelationValueItems.add(getString(R.string.define_by_user));
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("people_relation_set_title"));
        int defaultIndexOfDialog = getDefaultIndexOfDialog();
        ArrayList<String> arrayList = this.mRelationNameItems;
        builder.setSingleChoiceItems((CharSequence[]) arrayList.toArray(new String[arrayList.size()]), defaultIndexOfDialog, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PeopleRelationSetDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i != PeopleRelationSetDialogFragment.this.mRelationValueItems.indexOf(PeopleRelationSetDialogFragment.this.getString(R.string.define_by_user))) {
                    String str = (String) PeopleRelationSetDialogFragment.this.mRelationValueItems.get(i);
                    String userDefineRelation = PeopleContactInfo.getSystemRelationValueItems().contains(str) ? str : PeopleContactInfo.getUserDefineRelation();
                    if (PeopleRelationSetDialogFragment.this.mRelationSelectedListener == null) {
                        return;
                    }
                    PeopleRelationSetDialogFragment.this.mRelationSelectedListener.onRelationSelected(userDefineRelation, str);
                    return;
                }
                PeopleRelationCreatorDialogFragment peopleRelationCreatorDialogFragment = new PeopleRelationCreatorDialogFragment();
                peopleRelationCreatorDialogFragment.setOnCreatedListener(new PeopleRelationCreatorDialogFragment.OnRelationCreatedListener() { // from class: com.miui.gallery.ui.PeopleRelationSetDialogFragment.1.1
                    @Override // com.miui.gallery.ui.PeopleRelationCreatorDialogFragment.OnRelationCreatedListener
                    public void onRelationCreated(String str2) {
                        if (PeopleRelationSetDialogFragment.this.mRelationSelectedListener != null) {
                            PeopleRelationSetDialogFragment.this.mRelationSelectedListener.onRelationSelected(PeopleContactInfo.getUserDefineRelation(), str2);
                        }
                    }
                });
                peopleRelationCreatorDialogFragment.showAllowingStateLoss(PeopleRelationSetDialogFragment.this.getActivity().getSupportFragmentManager(), "PeopleRelationCreatorDialogFragment");
            }
        });
        return builder.create();
    }

    public final int getDefaultIndexOfDialog() {
        String string = getArguments().getString("default_relation");
        if (TextUtils.isEmpty(string)) {
            return -1;
        }
        return this.mRelationValueItems.indexOf(string);
    }

    public void setRelationSelectedListener(RelationSelectedListener relationSelectedListener) {
        this.mRelationSelectedListener = relationSelectedListener;
    }

    public static void createRelationSetDialog(FragmentActivity fragmentActivity, String str, String str2, int i, int i2, RelationSelectedListener relationSelectedListener) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.addAll(PeopleContactInfo.getSystemRelationNameItems());
        arrayList.addAll(PeopleContactInfo.getSystemRelationValueItems());
        boolean z = true;
        if (i > 1 || i2 > 0) {
            z = false;
        }
        if (!z) {
            int indexOf = arrayList.indexOf(PeopleContactInfo.getRelationValue(PeopleContactInfo.Relation.myself));
            arrayList.remove(indexOf);
            arrayList2.remove(indexOf);
        }
        ArrayList<String> userDefineRelations = PeopleContactInfo.UserDefineRelation.getUserDefineRelations();
        if (BaseMiscUtil.isValid(userDefineRelations)) {
            arrayList.addAll(userDefineRelations);
            arrayList2.addAll(userDefineRelations);
        }
        PeopleContactInfo.UserDefineRelation.setUserDefineRelations(userDefineRelations);
        Bundle bundle = new Bundle();
        bundle.putString("people_relation_set_title", str);
        bundle.putString("default_relation", str2);
        bundle.putStringArrayList("relation_names", arrayList2);
        bundle.putStringArrayList("relation_values", arrayList);
        doCreateDialog(fragmentActivity, bundle, relationSelectedListener);
    }

    public static void createRelationSetDialog(final FragmentActivity fragmentActivity, final String str, final String str2, final int i, final RelationSelectedListener relationSelectedListener) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Bundle>() { // from class: com.miui.gallery.ui.PeopleRelationSetDialogFragment.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Bundle mo1807run(ThreadPool.JobContext jobContext) {
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<String> arrayList2 = new ArrayList<>();
                arrayList2.addAll(PeopleContactInfo.getSystemRelationNameItems());
                arrayList.addAll(PeopleContactInfo.getSystemRelationValueItems());
                boolean z = false;
                boolean z2 = i <= 1;
                if (z2) {
                    List<Long> queryPeopleIdOfRelation = FaceManager.queryPeopleIdOfRelation(fragmentActivity, PeopleContactInfo.Relation.myself.getRelationType());
                    if (queryPeopleIdOfRelation == null || queryPeopleIdOfRelation.size() <= 0) {
                        z = true;
                    }
                    z2 = z;
                }
                if (!z2) {
                    int indexOf = arrayList.indexOf(PeopleContactInfo.getRelationValue(PeopleContactInfo.Relation.myself));
                    arrayList.remove(indexOf);
                    arrayList2.remove(indexOf);
                }
                ArrayList<String> userDefineRelations = PeopleContactInfo.UserDefineRelation.getUserDefineRelations();
                if (userDefineRelations == null) {
                    userDefineRelations = FaceManager.queryAllUserDefineRelationsOfPeople(fragmentActivity);
                }
                if (BaseMiscUtil.isValid(userDefineRelations)) {
                    arrayList.addAll(userDefineRelations);
                    arrayList2.addAll(userDefineRelations);
                }
                PeopleContactInfo.UserDefineRelation.setUserDefineRelations(userDefineRelations);
                Bundle bundle = new Bundle();
                bundle.putString("people_relation_set_title", str);
                bundle.putString("default_relation", str2);
                bundle.putStringArrayList("relation_names", arrayList2);
                bundle.putStringArrayList("relation_values", arrayList);
                return bundle;
            }
        }, new FutureHandler<Bundle>() { // from class: com.miui.gallery.ui.PeopleRelationSetDialogFragment.3
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<Bundle> future) {
                if (future == null || future.get() == null) {
                    return;
                }
                PeopleRelationSetDialogFragment.doCreateDialog(FragmentActivity.this, future.get(), relationSelectedListener);
            }
        });
    }

    public static void doCreateDialog(FragmentActivity fragmentActivity, Bundle bundle, RelationSelectedListener relationSelectedListener) {
        PeopleRelationSetDialogFragment peopleRelationSetDialogFragment = new PeopleRelationSetDialogFragment();
        peopleRelationSetDialogFragment.setArguments(bundle);
        peopleRelationSetDialogFragment.setRelationSelectedListener(relationSelectedListener);
        peopleRelationSetDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "PeopleRelationSetDialogFragment");
    }
}
