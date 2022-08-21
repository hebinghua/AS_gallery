package com.miui.gallery.people.mark;

import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.people.mark.MarkPeopleSuggestionContract;
import com.miui.gallery.people.model.People;
import com.miui.gallery.stat.SamplingStatHelper;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class MarkPeopleSuggestionPresenter {
    public Context mContext;
    public Bundle mMarkInfo;
    public MarkPeopleSuggestionContract.Model mModel;
    public ArrayList<People> mPeopleSuggestion;
    public boolean mStopped = false;
    public MarkPeopleSuggestionContract.View mView;

    public MarkPeopleSuggestionPresenter(Context context, MarkPeopleSuggestionContract.View view, MarkPeopleSuggestionContract.Model model, Bundle bundle) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;
        this.mMarkInfo = bundle;
    }

    public void start() {
        this.mStopped = false;
        if ((!this.mModel.needMark(this.mMarkInfo) || this.mView.isSuggestionDialogVisible()) && !this.mView.isSuggestionDialogVisible()) {
            return;
        }
        this.mModel.loadMarkSuggestion(this.mContext, this.mMarkInfo, new MarkPeopleSuggestionContract.LoadMarkSuggestionCallback() { // from class: com.miui.gallery.people.mark.MarkPeopleSuggestionPresenter.1
            @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.LoadMarkSuggestionCallback
            public void onPeopleSuggestionLoadFailed() {
            }

            @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.LoadMarkSuggestionCallback
            public void onNoNeedMark() {
                MarkPeopleSuggestionPresenter.this.mModel.onMarkSuggestionTriggered(MarkPeopleSuggestionPresenter.this.mMarkInfo);
                MarkPeopleSuggestionPresenter.this.setMarkResult(true, false);
            }

            @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.LoadMarkSuggestionCallback
            public void onPeopleSuggestionLoaded(ArrayList<People> arrayList) {
                if (MarkPeopleSuggestionPresenter.this.mStopped || arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                MarkPeopleSuggestionPresenter.this.mPeopleSuggestion = arrayList;
                MarkPeopleSuggestionPresenter.this.mView.showSuggestionDialog(MarkPeopleSuggestionPresenter.this.mContext.getString(R.string.mark_myself_suggestion_title), MarkPeopleSuggestionPresenter.this.mContext.getString(R.string.mark_myself_suggestion_load_more), arrayList);
                MarkPeopleSuggestionPresenter.this.mModel.onMarkSuggestionTriggered(MarkPeopleSuggestionPresenter.this.mMarkInfo);
            }
        });
    }

    public void markPeople(People people) {
        this.mView.dismissSuggestionDialog();
        this.mModel.markPeople(this.mContext, people, this.mMarkInfo, new MarkPeopleSuggestionContract.MarkPeopleCallback() { // from class: com.miui.gallery.people.mark.MarkPeopleSuggestionPresenter.2
            @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.MarkPeopleCallback
            public void onMarkPeopleSucceeded() {
                if (!MarkPeopleSuggestionPresenter.this.mStopped) {
                    MarkPeopleSuggestionPresenter.this.mView.showMarkResult(true, MarkPeopleSuggestionPresenter.this.mContext.getString(R.string.mark_operation_succeeded));
                }
                MarkPeopleSuggestionPresenter.this.setMarkResult(true, false);
            }

            @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.MarkPeopleCallback
            public void onMarkPeopleFailed(String str) {
                if (!MarkPeopleSuggestionPresenter.this.mStopped) {
                    MarkPeopleSuggestionPresenter.this.mView.showMarkResult(false, str);
                }
                MarkPeopleSuggestionPresenter.this.setMarkResult(false, false);
            }
        });
    }

    public void loadMore() {
        if (this.mView.isSuggestionDialogVisible()) {
            this.mView.dismissSuggestionDialog();
        }
        ArrayList<People> arrayList = this.mPeopleSuggestion;
        if (arrayList != null && arrayList.size() < this.mMarkInfo.getInt("option_suggestion_limit")) {
            SamplingStatHelper.recordCountEvent("people_mark", "mark_myself_dialog_close");
            return;
        }
        this.mView.goToActivity(MarkPeopleSuggestionContract.buildMarkUriForMarkInfo(this.mMarkInfo));
        SamplingStatHelper.recordCountEvent("people_mark", "mark_myself_dialog_load_more");
    }

    public void ignoreSuggestion() {
        if (this.mView.isSuggestionDialogVisible()) {
            this.mView.dismissSuggestionDialog();
        }
        this.mView.showInfoDialog(this.mContext.getString(R.string.set_group), this.mContext.getString(R.string.mark_people_ignore_info_message));
        setMarkResult(false, false);
        SamplingStatHelper.recordCountEvent("people_mark", "mark_myself_dialog_close");
    }

    public void stop() {
        this.mStopped = true;
    }

    public void setLoadMoreMarkResult(boolean z) {
        setMarkResult(z, true);
    }

    public final void setMarkResult(boolean z, boolean z2) {
        this.mModel.setMarkResult(this.mMarkInfo, z);
        String str = z2 ? z ? "mark_myself_load_more_mark" : "mark_myself_load_more_close" : z ? "mark_myself_dialog_mark" : "mark_myself_dialog_close";
        HashMap hashMap = null;
        if (!z2 && this.mPeopleSuggestion != null) {
            hashMap = new HashMap(2);
            hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mPeopleSuggestion.size()));
            int maxFaceCount = getMaxFaceCount(this.mPeopleSuggestion);
            hashMap.put("count_extra", maxFaceCount <= 5 ? "1-5" : maxFaceCount <= 10 ? "6-10" : maxFaceCount <= 50 ? "11-50" : maxFaceCount <= 100 ? "51-100" : ">100");
        }
        SamplingStatHelper.recordCountEvent("people_mark", str, hashMap);
    }

    public final int getMaxFaceCount(ArrayList<People> arrayList) {
        int i = 0;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<People> it = arrayList.iterator();
            while (it.hasNext()) {
                i = Math.max(it.next().getFaceCount(), i);
            }
        }
        return i;
    }
}
