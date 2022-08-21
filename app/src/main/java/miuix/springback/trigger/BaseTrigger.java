package miuix.springback.trigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import miuix.springback.R$dimen;
import miuix.springback.R$string;

/* loaded from: classes3.dex */
public abstract class BaseTrigger {
    public static int mDefaultIndeterminateDistance;
    public static int mDefaultSimpleEnter;
    public static int mDefaultSimpleTrigger;
    public static int mDefaultUpIndeterminateDistance;
    public List<Action> mActions = new ArrayList();
    public Action mUpAction;

    /* loaded from: classes3.dex */
    public static abstract class IndeterminateAction extends Action {
        public static final int[] DEFAULT_TRIGGER_TEXTIDS = {R$string.miuix_sbl_tracking_progress_labe_pull_to_refresh, R$string.miuix_sbl_tracking_progress_labe_release_to_refresh, R$string.miuix_sbl_tracking_progress_labe_refreshing, R$string.miuix_sbl_tracking_progress_labe_refreshed};
        public OnActionCompleteListener mCompleteListener;
        public int[] mTriggerTextIDs;
        public String[] mTriggerTexts;

        /* loaded from: classes3.dex */
        public interface OnActionCompleteListener {
        }

        /* loaded from: classes3.dex */
        public interface OnIndeterminateActionViewListener {
            float getViewRestartOffsetPoint();

            void onViewActivated(int i);

            void onViewActivating(int i);

            void onViewEntered(int i);

            void onViewEntering(int i);

            void onViewExit(int i);

            void onViewFinished(int i);

            void onViewStart(int i);

            void onViewStarting(int i);

            void onViewTriggered(int i);
        }
    }

    public BaseTrigger(Context context) {
        mDefaultIndeterminateDistance = context.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_indeterminate_distance);
        mDefaultUpIndeterminateDistance = context.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_upindeterminate_distance);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_simple_enter);
        mDefaultSimpleEnter = dimensionPixelSize;
        mDefaultSimpleTrigger = dimensionPixelSize;
    }

    public List<Action> getActions() {
        return this.mActions;
    }

    public IndeterminateAction getIndeterminateAction() {
        for (int i = 0; i < this.mActions.size(); i++) {
            Action action = this.mActions.get(i);
            if (action != null && (action instanceof IndeterminateAction)) {
                return (IndeterminateAction) action;
            }
        }
        return null;
    }

    public IndeterminateUpAction getIndeterminateUpAction() {
        return (IndeterminateUpAction) this.mUpAction;
    }

    public void addAction(Action action) {
        if (action instanceof IndeterminateUpAction) {
            this.mUpAction = action;
            return;
        }
        int binarySearch = Collections.binarySearch(this.mActions, action, Action.DISTANCE_COMPARATOR);
        if (binarySearch < 0) {
            this.mActions.add((-binarySearch) - 1, action);
            return;
        }
        throw new IllegalArgumentException("action conflict.");
    }

    /* loaded from: classes3.dex */
    public static abstract class Action {
        public static final Comparator<Action> DISTANCE_COMPARATOR = new Comparator<Action>() { // from class: miuix.springback.trigger.BaseTrigger.Action.1
            @Override // java.util.Comparator
            public int compare(Action action, Action action2) {
                return Integer.compare(action.mEnterPoint, action2.mEnterPoint);
            }
        };
        public int mEnterPoint;
        public int mTriggerPoint;

        public abstract void onActivated();

        public View onCreateIndicator(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            return null;
        }

        public abstract void onEntered();

        public abstract void onExit();

        public abstract void onFinished();

        public abstract void onTriggered();

        public Action(int i, int i2) {
            if (i < 0 || i2 < 0 || i2 < i) {
                throw new IllegalArgumentException("not allow enterPoint < 0 or triggerPoint < 0 or triggerPoint < enterPoint!");
            }
            this.mEnterPoint = i;
            this.mTriggerPoint = i2;
        }

        public void notifyTriggered() {
            onTriggered();
        }

        public void notifyEntered() {
            onEntered();
        }

        public void notifyExit() {
            onExit();
        }

        public void notifyActivated() {
            onActivated();
        }

        public void notifyFinished() {
            onFinished();
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class SimpleAction extends Action {

        /* loaded from: classes3.dex */
        public interface OnSimpleActionViewListener {
            float getViewRestartOffsetPoint();

            void onViewActivated(int i);

            void onViewActivating(int i);

            void onViewEntered(int i);

            void onViewEntering(int i);

            void onViewExit(int i);

            void onViewFinished(int i);

            void onViewStart(int i);

            void onViewStarting(int i);

            void onViewTriggered(int i);
        }

        public SimpleAction() {
            super(BaseTrigger.mDefaultSimpleEnter, BaseTrigger.mDefaultSimpleTrigger);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class IndeterminateUpAction extends Action {
        public static final int[] DEFAULT_TRIGGER_TEXTIDS = {R$string.miuix_sbl_tracking_progress_labe_up_refresh, R$string.miuix_sbl_tracking_progress_labe_up_refresh_fail, R$string.miuix_sbl_tracking_progress_labe_up_nodata, R$string.miuix_sbl_tracking_progress_labe_up_none};
        public int mCountNoData;
        public int[] mTriggerTextIDs;
        public String[] mTriggerTexts;
        public OnUpActionDataListener mUpDataListener;

        /* loaded from: classes3.dex */
        public interface OnIndeterminateUpActionViewListener {
            float getViewRestartOffsetPoint();

            void onViewActivated(int i);

            void onViewActivating(int i);

            void onViewEntered(int i);

            void onViewEntering(int i);

            void onViewExit(int i);

            void onViewFinished(int i);

            void onViewStart(int i);

            void onViewStarting(int i);

            void onViewTriggered(int i);
        }

        /* loaded from: classes3.dex */
        public interface OnUpActionDataListener {
        }

        public boolean isNoData() {
            return this.mCountNoData > 0;
        }

        public int getCountNoData() {
            return this.mCountNoData;
        }
    }
}
