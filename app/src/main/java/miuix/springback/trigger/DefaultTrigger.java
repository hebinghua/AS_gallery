package miuix.springback.trigger;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.search.statistics.SearchStatUtils;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.springback.R$dimen;
import miuix.springback.R$id;
import miuix.springback.trigger.BaseTrigger;
import miuix.springback.trigger.CustomTrigger;
import miuix.springback.view.SpringBackLayout;

/* loaded from: classes3.dex */
public class DefaultTrigger extends CustomTrigger {
    public static int mIndeterminateTop;
    public Context mContext;
    public Pair<Integer, Integer> mIndeterminateActionPoint;
    public Pair<Integer, Integer> mIndeterminateSimpleActionPoint;
    public Pair<Integer, Integer> mIndeterminateUpActionPoint;
    public ProgressBar mLoadingIndicator;
    public CustomTrigger.OnIndeterminateActionDataListener mOnActionDataListener;
    public BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener mOnIndeterminateActionViewListener;
    public BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener mOnIndeterminateUpActionViewListener;
    public BaseTrigger.SimpleAction.OnSimpleActionViewListener mOnSimpleActionViewListener;
    public CustomTrigger.OnIndeterminateUpActionDataListener mOnUpActionDataListener;
    public View mTrackingIndicator;
    public int mTrackingIndicatorBottom;
    public TextView mTrackingIndicatorLabel;
    public int mTrackingIndicatorLabelBottom;
    public int mTrackingIndicatorLabelTop;
    public ProgressBar mUpLoadingIndicator;
    public ViewGroup mUpTrackingContainer;
    public View mUpTrackingIndicator;
    public TextView mUpTrackingIndicatorLabel;

    public final void initSimpleActionView() {
    }

    public DefaultTrigger(Context context) {
        super(context);
        this.mTrackingIndicatorBottom = 0;
        this.mTrackingIndicatorLabelTop = 0;
        this.mTrackingIndicatorLabelBottom = 0;
        this.mOnActionDataListener = new CustomTrigger.OnIndeterminateActionDataListener() { // from class: miuix.springback.trigger.DefaultTrigger.1
        };
        this.mOnUpActionDataListener = new CustomTrigger.OnIndeterminateUpActionDataListener() { // from class: miuix.springback.trigger.DefaultTrigger.2
        };
        this.mOnIndeterminateActionViewListener = new BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener() { // from class: miuix.springback.trigger.DefaultTrigger.3
            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public float getViewRestartOffsetPoint() {
                return -1.0f;
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewActivated(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewActivating(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewEntering(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewExit(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewFinished(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewStarting(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewStart(int i) {
                DefaultTrigger.this.mLoadingIndicator.setVisibility(8);
                DefaultTrigger.this.mTrackingIndicator.setVisibility(0);
                DefaultTrigger.this.mTrackingIndicatorLabel.setVisibility(0);
                BaseTrigger.IndeterminateAction indeterminateAction = DefaultTrigger.this.getIndeterminateAction();
                if (indeterminateAction != null) {
                    DefaultTrigger.this.mTrackingIndicatorLabel.setText(indeterminateAction.mTriggerTexts[0]);
                }
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewEntered(int i) {
                DefaultTrigger.this.mTrackingIndicator.setVisibility(0);
                DefaultTrigger.this.mTrackingIndicatorLabel.setVisibility(0);
                if (DefaultTrigger.this.isExitSimpleAction()) {
                    DefaultTrigger.this.getIndicatorContainer().setVisibility(8);
                }
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener
            public void onViewTriggered(int i) {
                DefaultTrigger.this.mLoadingIndicator.setVisibility(0);
                DefaultTrigger.this.mTrackingIndicator.setVisibility(0);
                DefaultTrigger.this.mTrackingIndicatorLabel.setVisibility(0);
                BaseTrigger.IndeterminateAction indeterminateAction = DefaultTrigger.this.getIndeterminateAction();
                if (indeterminateAction != null) {
                    DefaultTrigger.this.mTrackingIndicatorLabel.setText(indeterminateAction.mTriggerTexts[2]);
                }
                if (DefaultTrigger.this.mLoadingIndicator.getVisibility() == 0) {
                    DefaultTrigger.this.mLoadingIndicator.setAlpha(1.0f);
                    DefaultTrigger.this.mLoadingIndicator.setScaleX(1.0f);
                    DefaultTrigger.this.mLoadingIndicator.setScaleY(1.0f);
                }
            }
        };
        this.mOnSimpleActionViewListener = new BaseTrigger.SimpleAction.OnSimpleActionViewListener() { // from class: miuix.springback.trigger.DefaultTrigger.4
            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public float getViewRestartOffsetPoint() {
                return -1.0f;
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewActivated(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewActivating(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewEntering(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewExit(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewFinished(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewStarting(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewTriggered(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewStart(int i) {
                DefaultTrigger.this.getIndicatorContainer().setVisibility(0);
            }

            @Override // miuix.springback.trigger.BaseTrigger.SimpleAction.OnSimpleActionViewListener
            public void onViewEntered(int i) {
                DefaultTrigger defaultTrigger = DefaultTrigger.this;
                defaultTrigger.viewShow(defaultTrigger.getIndicatorContainer());
                if (DefaultTrigger.this.isExitIndeterminateAction()) {
                    DefaultTrigger.this.mTrackingIndicator.setVisibility(8);
                    DefaultTrigger.this.mTrackingIndicatorLabel.setVisibility(8);
                }
            }
        };
        this.mOnIndeterminateUpActionViewListener = new BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener() { // from class: miuix.springback.trigger.DefaultTrigger.5
            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public float getViewRestartOffsetPoint() {
                return -1.0f;
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewActivated(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewActivating(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewEntered(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewEntering(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewExit(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewFinished(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewStarting(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewTriggered(int i) {
            }

            @Override // miuix.springback.trigger.BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener
            public void onViewStart(int i) {
                DefaultTrigger.this.mUpTrackingContainer.setVisibility(0);
                BaseTrigger.IndeterminateUpAction indeterminateUpAction = DefaultTrigger.this.getIndeterminateUpAction();
                if (indeterminateUpAction == null || !indeterminateUpAction.isNoData()) {
                    if (indeterminateUpAction == null) {
                        return;
                    }
                    DefaultTrigger.this.mUpTrackingIndicator.setVisibility(0);
                    DefaultTrigger.this.mUpLoadingIndicator.setVisibility(0);
                    DefaultTrigger.this.mUpTrackingIndicatorLabel.setVisibility(0);
                    DefaultTrigger.this.mUpTrackingIndicatorLabel.setText(indeterminateUpAction.mTriggerTexts[0]);
                    return;
                }
                DefaultTrigger.this.mUpTrackingIndicator.setVisibility(8);
                DefaultTrigger.this.mUpLoadingIndicator.setVisibility(8);
                if (indeterminateUpAction.getCountNoData() < 3) {
                    DefaultTrigger.this.mUpTrackingIndicatorLabel.setText(indeterminateUpAction.mTriggerTexts[2]);
                } else {
                    DefaultTrigger.this.mUpTrackingIndicatorLabel.setText(indeterminateUpAction.mTriggerTexts[3]);
                }
            }
        };
        this.mContext = context;
        setOnActionDataListener(this.mOnActionDataListener);
        setOnUpActionDataListener(this.mOnUpActionDataListener);
        mIndeterminateTop = context.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_tracking_progress_bg_margintop);
        this.mIndeterminateActionPoint = new Pair<>(0, Integer.valueOf(this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_indeterminate_distance) + 0));
        this.mIndeterminateUpActionPoint = new Pair<>(0, Integer.valueOf(this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_upindeterminate_distance) + 0));
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_sbl_action_simple_enter);
        this.mIndeterminateSimpleActionPoint = new Pair<>(Integer.valueOf(dimensionPixelSize), Integer.valueOf(dimensionPixelSize));
    }

    @Override // miuix.springback.trigger.CustomTrigger, miuix.springback.trigger.BaseTrigger
    public void addAction(BaseTrigger.Action action) {
        super.addAction(action);
        if (action instanceof BaseTrigger.IndeterminateUpAction) {
            initIndeterminateUpActionView();
            BaseTrigger.IndeterminateUpAction indeterminateUpAction = (BaseTrigger.IndeterminateUpAction) action;
            setOnIndeterminateUpActionViewListener(this.mOnIndeterminateUpActionViewListener);
            updateTextIdToString(this.mContext, indeterminateUpAction.mTriggerTextIDs, indeterminateUpAction.mTriggerTexts);
        } else if (action instanceof BaseTrigger.IndeterminateAction) {
            initIndeterminateActionView();
            BaseTrigger.IndeterminateAction indeterminateAction = (BaseTrigger.IndeterminateAction) action;
            setOnIndeterminateActionViewListener(this.mOnIndeterminateActionViewListener);
            updateTextIdToString(this.mContext, indeterminateAction.mTriggerTextIDs, indeterminateAction.mTriggerTexts);
        } else if (!(action instanceof BaseTrigger.SimpleAction)) {
        } else {
            initSimpleActionView();
            setOnSimpleActionViewListener(this.mOnSimpleActionViewListener);
        }
    }

    public final void initIndeterminateActionView() {
        this.mTrackingIndicator = getRootContainer().findViewById(R$id.tracking_progress);
        this.mTrackingIndicatorLabel = (TextView) getRootContainer().findViewById(R$id.tracking_progress_label);
        this.mLoadingIndicator = (ProgressBar) getRootContainer().findViewById(R$id.loading_progress);
    }

    public final void initIndeterminateUpActionView() {
        this.mUpTrackingContainer = (ViewGroup) getIndeterminateUpView().findViewById(R$id.tracking_progress_up_container);
        this.mUpTrackingIndicator = getIndeterminateUpView().findViewById(R$id.tracking_progress_up);
        this.mUpTrackingIndicatorLabel = (TextView) getIndeterminateUpView().findViewById(R$id.tracking_progress_up_label);
        this.mUpLoadingIndicator = (ProgressBar) getIndeterminateUpView().findViewById(R$id.loading_progress_up);
    }

    public final void updateTextIdToString(Context context, int[] iArr, String[] strArr) {
        if (iArr != null) {
            for (int i = 0; i < iArr.length; i++) {
                strArr[i] = context.getResources().getString(iArr[i]);
            }
        }
    }

    @Override // miuix.springback.trigger.CustomTrigger
    public void onSpringBackLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (isExitIndeterminateAction()) {
            for (int i9 = 0; i9 < getActions().size(); i9++) {
                BaseTrigger.Action action = getActions().get(i9);
                if (action instanceof BaseTrigger.IndeterminateAction) {
                    BaseTrigger.IndeterminateAction indeterminateAction = (BaseTrigger.IndeterminateAction) action;
                    if (mIndeterminateTop >= this.mTrackingIndicator.getTop()) {
                        this.mLoadingIndicator.offsetTopAndBottom(indeterminateAction.mEnterPoint - 0);
                        this.mTrackingIndicator.offsetTopAndBottom(indeterminateAction.mEnterPoint - 0);
                        this.mTrackingIndicatorLabel.offsetTopAndBottom(indeterminateAction.mEnterPoint - 0);
                    }
                }
            }
            if (this.mTrackingIndicator.getVisibility() == 0 && getCurrentAction() != null && (getCurrentAction() instanceof BaseTrigger.IndeterminateAction)) {
                if (this.mTrackingIndicatorBottom <= 0) {
                    this.mTrackingIndicatorBottom = this.mTrackingIndicator.getBottom();
                }
                if (this.mTrackingIndicatorLabelTop <= 0 || this.mTrackingIndicatorLabelBottom <= 0) {
                    this.mTrackingIndicatorLabelTop = this.mTrackingIndicatorLabel.getTop();
                    this.mTrackingIndicatorLabelBottom = this.mTrackingIndicatorLabel.getBottom();
                }
                if ((this.mLoadingIndicator.getVisibility() == 8 || this.mLoadingIndicator.getVisibility() == 4) && getCurrentState() != this.mActionComplete && getRootContainer().getHeight() > getCurrentAction().mTriggerPoint) {
                    this.mTrackingIndicator.setBottom(this.mTrackingIndicatorBottom + (getRootContainer().getHeight() - getCurrentAction().mTriggerPoint));
                }
            }
        }
        if (!isExitSimpleAction() || getIndicatorContainer().getVisibility() != 0) {
            return;
        }
        getIndicatorContainer().offsetTopAndBottom(this.mScrollDistance - getIndicatorContainer().getMeasuredHeight());
    }

    @Override // miuix.springback.trigger.CustomTrigger
    public void onSpringBackScrolled(SpringBackLayout springBackLayout, int i, int i2, int i3) {
        if (i3 < 0 && isExitIndeterminateUpAction() && getCurrentAction() != null && (getCurrentAction() instanceof BaseTrigger.IndeterminateUpAction)) {
            this.mUpTrackingContainer.setTranslationY(Math.max(getIndeterminateUpView().getHeight() - getIndeterminateUpAction().mTriggerPoint, 0));
        }
        if (isExitIndeterminateAction() && getCurrentAction() != null && (getCurrentAction() instanceof BaseTrigger.IndeterminateAction)) {
            BaseTrigger.IndeterminateAction indeterminateAction = (BaseTrigger.IndeterminateAction) getCurrentAction();
            if (this.mTrackingIndicator.getVisibility() == 0) {
                this.mTrackingIndicatorBottom = this.mTrackingIndicator.getTop() + this.mTrackingIndicator.getWidth();
                this.mTrackingIndicatorLabelTop = this.mTrackingIndicatorLabel.getTop();
                this.mTrackingIndicatorLabelBottom = this.mTrackingIndicatorLabel.getBottom();
                float f = indeterminateAction.mTriggerPoint;
                float max = Math.max(0.0f, Math.min(getRootContainer().getHeight() / f, 1.0f));
                float f2 = 0.5f * f;
                float max2 = Math.max(0.0f, ((float) getRootContainer().getHeight()) < f2 ? 0.0f : Math.min((getRootContainer().getHeight() - f2) / f2, 1.0f));
                float max3 = Math.max(0.0f, ((float) getRootContainer().getHeight()) < f2 ? 0.0f : Math.min((getRootContainer().getHeight() - (0.7f * f)) / (f * 0.3f), 1.0f));
                float f3 = (-this.mTrackingIndicator.getWidth()) * (1.0f - max);
                this.mTrackingIndicator.setAlpha(max2);
                this.mTrackingIndicator.setScaleX(max);
                this.mTrackingIndicator.setScaleY(max);
                this.mTrackingIndicatorLabel.setAlpha(max3);
                this.mTrackingIndicatorLabel.setTop(this.mTrackingIndicatorLabelTop);
                this.mTrackingIndicatorLabel.setBottom(this.mTrackingIndicatorLabelBottom);
                if (this.mLoadingIndicator.getVisibility() == 0) {
                    this.mLoadingIndicator.setAlpha(max2);
                    this.mLoadingIndicator.setScaleX(max);
                    this.mLoadingIndicator.setScaleY(max);
                }
                if (getRootContainer().getHeight() < indeterminateAction.mTriggerPoint) {
                    if (max3 > 0.0f) {
                        this.mTrackingIndicatorLabel.setTranslationY(f3);
                    }
                    if (getCurrentState() == this.mTracking) {
                        this.mTrackingIndicatorLabel.setText(indeterminateAction.mTriggerTexts[0]);
                    }
                    this.mTrackingIndicator.setBottom(this.mTrackingIndicatorBottom);
                } else if (getRootContainer().getHeight() >= indeterminateAction.mTriggerPoint) {
                    int height = this.mTrackingIndicatorBottom + (getRootContainer().getHeight() - indeterminateAction.mTriggerPoint);
                    if (this.mLoadingIndicator.getVisibility() != 0 && getCurrentState() != this.mActionComplete) {
                        this.mTrackingIndicator.setBottom(height);
                        this.mTrackingIndicatorLabel.setTranslationY(getRootContainer().getHeight() - indeterminateAction.mTriggerPoint);
                    } else {
                        this.mTrackingIndicatorLabel.setTranslationY(0.0f);
                    }
                    if (getCurrentState() == this.mTracking) {
                        this.mTrackingIndicatorLabel.setText(indeterminateAction.mTriggerTexts[1]);
                    }
                }
            }
        }
        if (isExitSimpleAction() && getCurrentAction() != null && (getCurrentAction() instanceof BaseTrigger.SimpleAction) && getRootContainer().getHeight() < getCurrentAction().mEnterPoint) {
            getIndicatorContainer().setVisibility(8);
        } else if (isExitSimpleAction() && getCurrentAction() != null && (getCurrentAction() instanceof BaseTrigger.SimpleAction) && getRootContainer().getHeight() >= getCurrentAction().mEnterPoint && getIndicatorContainer().getVisibility() == 8) {
            getIndicatorContainer().setVisibility(0);
            viewShow(getIndicatorContainer());
        }
        if (!isExitSimpleAction() || getCurrentAction() == null || getIndicatorContainer().getVisibility() != 0) {
            return;
        }
        getIndicatorContainer().offsetTopAndBottom(-i2);
    }

    public final void viewShow(View view) {
        if (view != null) {
            view.setVisibility(0);
            AnimState animState = new AnimState("start");
            ViewProperty viewProperty = ViewProperty.ALPHA;
            AnimState add = animState.add(viewProperty, SearchStatUtils.POW);
            ViewProperty viewProperty2 = ViewProperty.TRANSLATION_Y;
            AnimState add2 = add.add(viewProperty2, -180.0d);
            AnimState add3 = new AnimState("show").add(viewProperty, 1.0d).add(viewProperty2, 25.0d);
            Folme.useAt(view).state().setFlags(1L).fromTo(add2, add3, new AnimConfig().setEase(EaseManager.getStyle(4, 120.0f, 0.99f, 0.1f))).then(new AnimState("hide").add(viewProperty, 1.0d).add(viewProperty2, SearchStatUtils.POW), new AnimConfig().setEase(EaseManager.getStyle(4, 40.0f, 0.99f, 0.1f)));
        }
    }
}
