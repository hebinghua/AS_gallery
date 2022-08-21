package com.miui.itemdrag.animator;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.base.BaseItemAnimator;
import com.miui.itemdrag.animator.base.GeneralItemAnimator;
import com.miui.itemdrag.animator.impl.AddAnimationInfo;
import com.miui.itemdrag.animator.impl.ChangeAnimationInfo;
import com.miui.itemdrag.animator.impl.ItemAddAnimationManager;
import com.miui.itemdrag.animator.impl.ItemChangeAnimationManager;
import com.miui.itemdrag.animator.impl.ItemMoveAnimationManager;
import com.miui.itemdrag.animator.impl.ItemRemoveAnimationManager;
import com.miui.itemdrag.animator.impl.MoveAnimationInfo;
import com.miui.itemdrag.animator.impl.RemoveAnimationInfo;
import java.util.List;

/* loaded from: classes3.dex */
public class RefactoredDefaultItemAnimator extends GeneralItemAnimator {
    @Override // com.miui.itemdrag.animator.base.GeneralItemAnimator
    public void onSetup() {
        setItemAddAnimationsManager(new DefaultItemAddAnimationManager(this));
        setItemRemoveAnimationManager(new DefaultItemRemoveAnimationManager(this));
        setItemChangeAnimationsManager(new DefaultItemChangeAnimationManager(this));
        setItemMoveAnimationsManager(new DefaultItemMoveAnimationManager(this));
    }

    @Override // com.miui.itemdrag.animator.base.GeneralItemAnimator
    public void onSchedulePendingAnimations() {
        schedulePendingAnimationsByDefaultRule();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder, List<Object> list) {
        return !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
    }

    /* loaded from: classes3.dex */
    public static class DefaultItemAddAnimationManager extends ItemAddAnimationManager {
        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedSuccessfully(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        }

        public DefaultItemAddAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onCreateAnimation(AddAnimationInfo addAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(addAnimationInfo.holder.itemView);
            animate.alpha(1.0f);
            animate.setDuration(getDuration());
            if (getInterpolator() != null) {
                animate.setInterpolator(getInterpolator());
            }
            startActiveItemAnimation(addAnimationInfo, addAnimationInfo.holder, animate);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedBeforeStarted(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setAlpha(1.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationCancel(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setAlpha(1.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.ItemAddAnimationManager
        public boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder) {
            resetAnimation(viewHolder);
            viewHolder.itemView.setAlpha(0.0f);
            enqueuePendingAnimationInfo(new AddAnimationInfo(viewHolder));
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public static class DefaultItemRemoveAnimationManager extends ItemRemoveAnimationManager {
        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationCancel(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        }

        public DefaultItemRemoveAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onCreateAnimation(RemoveAnimationInfo removeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(removeAnimationInfo.holder.itemView);
            animate.setDuration(getDuration());
            animate.alpha(0.0f);
            if (getInterpolator() != null) {
                animate.setInterpolator(getInterpolator());
            }
            startActiveItemAnimation(removeAnimationInfo, removeAnimationInfo.holder, animate);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedSuccessfully(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setAlpha(1.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedBeforeStarted(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setAlpha(1.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.ItemRemoveAnimationManager
        public boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder) {
            resetAnimation(viewHolder);
            enqueuePendingAnimationInfo(new RemoveAnimationInfo(viewHolder));
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public static class DefaultItemChangeAnimationManager extends ItemChangeAnimationManager {
        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationCancel(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        }

        public DefaultItemChangeAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        @Override // com.miui.itemdrag.animator.impl.ItemChangeAnimationManager
        public void onCreateChangeAnimationForOldItem(ChangeAnimationInfo changeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(changeAnimationInfo.oldHolder.itemView);
            animate.setDuration(getDuration());
            animate.translationX(changeAnimationInfo.toX - changeAnimationInfo.fromX);
            animate.translationY(changeAnimationInfo.toY - changeAnimationInfo.fromY);
            if (getInterpolator() != null) {
                animate.setInterpolator(getInterpolator());
            }
            animate.alpha(0.0f);
            startActiveItemAnimation(changeAnimationInfo, changeAnimationInfo.oldHolder, animate);
        }

        @Override // com.miui.itemdrag.animator.impl.ItemChangeAnimationManager
        public void onCreateChangeAnimationForNewItem(ChangeAnimationInfo changeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(changeAnimationInfo.newHolder.itemView);
            animate.translationX(0.0f);
            animate.translationY(0.0f);
            animate.setDuration(getDuration());
            if (getInterpolator() != null) {
                animate.setInterpolator(getInterpolator());
            }
            animate.alpha(1.0f);
            startActiveItemAnimation(changeAnimationInfo, changeAnimationInfo.newHolder, animate);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedSuccessfully(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedBeforeStarted(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.ItemChangeAnimationManager
        public boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
            float translationX = viewHolder.itemView.getTranslationX();
            float translationY = viewHolder.itemView.getTranslationY();
            float alpha = viewHolder.itemView.getAlpha();
            resetAnimation(viewHolder);
            int i5 = (int) ((i3 - i) - translationX);
            int i6 = (int) ((i4 - i2) - translationY);
            viewHolder.itemView.setTranslationX(translationX);
            viewHolder.itemView.setTranslationY(translationY);
            viewHolder.itemView.setAlpha(alpha);
            if (viewHolder2 != null) {
                resetAnimation(viewHolder2);
                viewHolder2.itemView.setTranslationX(-i5);
                viewHolder2.itemView.setTranslationY(-i6);
                viewHolder2.itemView.setAlpha(0.0f);
            }
            enqueuePendingAnimationInfo(new ChangeAnimationInfo(viewHolder, viewHolder2, i, i2, i3, i4));
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public static class DefaultItemMoveAnimationManager extends ItemMoveAnimationManager {
        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedSuccessfully(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        }

        public DefaultItemMoveAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onCreateAnimation(MoveAnimationInfo moveAnimationInfo) {
            View view = moveAnimationInfo.holder.itemView;
            int i = moveAnimationInfo.toX - moveAnimationInfo.fromX;
            int i2 = moveAnimationInfo.toY - moveAnimationInfo.fromY;
            if (i != 0) {
                ViewCompat.animate(view).translationX(0.0f);
            }
            if (i2 != 0) {
                ViewCompat.animate(view).translationY(0.0f);
            }
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(view);
            animate.setDuration(getDuration());
            if (getInterpolator() != null) {
                animate.setInterpolator(getInterpolator());
            }
            startActiveItemAnimation(moveAnimationInfo, moveAnimationInfo.holder, animate);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationEndedBeforeStarted(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
        }

        @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
        public void onAnimationCancel(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            int i = moveAnimationInfo.toX - moveAnimationInfo.fromX;
            int i2 = moveAnimationInfo.toY - moveAnimationInfo.fromY;
            if (i != 0) {
                ViewCompat.animate(view).translationX(0.0f);
            }
            if (i2 != 0) {
                ViewCompat.animate(view).translationY(0.0f);
            }
            if (i != 0) {
                view.setTranslationX(0.0f);
            }
            if (i2 != 0) {
                view.setTranslationY(0.0f);
            }
        }

        @Override // com.miui.itemdrag.animator.impl.ItemMoveAnimationManager
        public boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            View view = viewHolder.itemView;
            int translationX = (int) (i + view.getTranslationX());
            int translationY = (int) (i2 + viewHolder.itemView.getTranslationY());
            resetAnimation(viewHolder);
            int i5 = i3 - translationX;
            int i6 = i4 - translationY;
            MoveAnimationInfo moveAnimationInfo = new MoveAnimationInfo(viewHolder, translationX, translationY, i3, i4);
            if (i5 == 0 && i6 == 0) {
                dispatchFinished(moveAnimationInfo, moveAnimationInfo.holder);
                moveAnimationInfo.clear(moveAnimationInfo.holder);
                return false;
            }
            if (i5 != 0) {
                view.setTranslationX(-i5);
            }
            if (i6 != 0) {
                view.setTranslationY(-i6);
            }
            enqueuePendingAnimationInfo(moveAnimationInfo);
            return true;
        }
    }
}
