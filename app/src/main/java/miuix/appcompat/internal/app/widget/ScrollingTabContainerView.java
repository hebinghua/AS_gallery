package miuix.appcompat.internal.app.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.core.os.TraceCompat;
import java.lang.ref.WeakReference;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.internal.view.ActionBarPolicy;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;

/* loaded from: classes3.dex */
public abstract class ScrollingTabContainerView extends HorizontalScrollView implements AdapterView.OnItemClickListener, ActionBar.FragmentViewPagerChangeListener {
    public boolean mAllowCollapse;
    public int mContentHeight;
    public final LayoutInflater mInflater;
    public int mLastSelectedPosition;
    public int mMaxTabWidth;
    public Paint mPaint;
    public int mSelectedTabIndex;
    public int mStackedTabMaxWidth;
    public TabClickListener mTabClickListener;
    public Bitmap mTabIndicatorBitmap;
    public float mTabIndicatorPosition;
    public LinearLayout mTabLayout;
    public Runnable mTabSelector;
    public Spinner mTabSpinner;
    public boolean mTranslucentIndicator;

    public abstract int getTabBarLayoutRes();

    public abstract int getTabContainerHeight();

    public abstract int getTabTextStyle();

    public abstract int getTabViewLayoutRes();

    public abstract int getTabViewMarginHorizontal();

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    public View updateCustomTabView(ViewGroup viewGroup, View view, TextView textView, ImageView imageView) {
        return null;
    }

    public void updateTabTextStyle(TextView textView) {
    }

    public ScrollingTabContainerView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mLastSelectedPosition = -1;
        LayoutInflater from = LayoutInflater.from(context);
        this.mInflater = from;
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R$styleable.ActionBar, 16843470, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_tabIndicator);
        this.mTranslucentIndicator = obtainStyledAttributes.getBoolean(R$styleable.ActionBar_translucentTabIndicator, true);
        this.mTabIndicatorBitmap = getTabIndicatorBitmap(drawable);
        obtainStyledAttributes.recycle();
        if (this.mTranslucentIndicator) {
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        setHorizontalScrollBarEnabled(false);
        this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
        LinearLayout linearLayout = (LinearLayout) from.inflate(getTabBarLayoutRes(), (ViewGroup) this, false);
        this.mTabLayout = linearLayout;
        addView(linearLayout, new FrameLayout.LayoutParams(-2, -2));
    }

    public void setEmbeded(boolean z) {
        setHorizontalFadingEdgeEnabled(true);
    }

    public final Bitmap getTabIndicatorBitmap(Drawable drawable) {
        Bitmap createBitmap;
        if (drawable == null) {
            return null;
        }
        if (this.mTranslucentIndicator) {
            createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ALPHA_8);
        } else {
            createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Bitmap bitmap = this.mTabIndicatorBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, this.mTabIndicatorPosition, getHeight() - this.mTabIndicatorBitmap.getHeight(), this.mPaint);
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mTabLayout.getChildAt(this.mSelectedTabIndex) != null) {
            setTabIndicatorPosition(this.mSelectedTabIndex);
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        boolean z = mode == 1073741824;
        setFillViewport(z);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount > 1 && (mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            if (childCount > 2) {
                this.mMaxTabWidth = (int) (View.MeasureSpec.getSize(i) * 0.4f);
            } else {
                this.mMaxTabWidth = (int) (View.MeasureSpec.getSize(i) * 0.6f);
            }
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        } else {
            this.mMaxTabWidth = -1;
        }
        int i3 = this.mContentHeight;
        if (i3 != -2) {
            i2 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        }
        int measuredWidth = getMeasuredWidth();
        super.onMeasure(i, i2);
        int measuredWidth2 = getMeasuredWidth();
        if (!z || measuredWidth == measuredWidth2) {
            return;
        }
        setTabSelected(this.mSelectedTabIndex);
    }

    public float getTabIndicatorPosition() {
        return this.mTabIndicatorPosition;
    }

    public void setTabIndicatorPosition(int i) {
        setTabIndicatorPosition(i, 0.0f);
    }

    public void setTabIndicatorPosition(int i, float f) {
        View childAt;
        float width;
        if (this.mTabIndicatorBitmap != null) {
            View childAt2 = this.mTabLayout.getChildAt(i);
            if (this.mTabLayout.getChildAt(i + 1) == null) {
                width = childAt2.getWidth();
            } else {
                width = (childAt2.getWidth() + childAt.getWidth()) / 2.0f;
            }
            this.mTabIndicatorPosition = childAt2.getLeft() + ((childAt2.getWidth() - this.mTabIndicatorBitmap.getWidth()) / 2) + (width * f);
            invalidate();
        }
    }

    public void setAllowCollapse(boolean z) {
        this.mAllowCollapse = z;
    }

    public void setTabSelected(int i) {
        setTabSelected(i, true);
    }

    public void setTabSelected(int i, boolean z) {
        this.mSelectedTabIndex = i;
        int childCount = this.mTabLayout.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = this.mTabLayout.getChildAt(i2);
            boolean z2 = i2 == i;
            childAt.setSelected(z2);
            if (z2) {
                if (z) {
                    animateToTab(i);
                } else {
                    scrollToTab(i);
                }
            }
            i2++;
        }
    }

    public void setContentHeight(int i) {
        if (this.mContentHeight != i) {
            this.mContentHeight = i;
            requestLayout();
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(getContext());
        setContentHeight(getTabContainerHeight());
        this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
    }

    public void scrollToTab(int i) {
        View childAt = this.mTabLayout.getChildAt(i);
        scrollTo(childAt.getLeft() - ((getWidth() - childAt.getWidth()) / 2), 0);
    }

    public void animateToTab(int i) {
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        TabSelectorRunnable tabSelectorRunnable = new TabSelectorRunnable(this, i);
        this.mTabSelector = tabSelectorRunnable;
        post(tabSelectorRunnable);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            post(runnable);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    public final TabView createTabView(ActionBar.Tab tab, boolean z) {
        TraceCompat.beginSection("inflate#attach");
        TabView tabView = (TabView) this.mInflater.inflate(getTabViewLayoutRes(), (ViewGroup) this.mTabLayout, false);
        tabView.attach(this, tab, z);
        TraceCompat.endSection();
        if (z) {
            tabView.setBackground(null);
            tabView.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mContentHeight));
        } else {
            tabView.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener(this);
            }
            tabView.setOnClickListener(this.mTabClickListener);
        }
        if (this.mTabLayout.getChildCount() != 0) {
            ((LinearLayout.LayoutParams) tabView.getLayoutParams()).setMarginStart(getTabViewMarginHorizontal());
        }
        return tabView;
    }

    public void addTab(ActionBar.Tab tab, boolean z) {
        TraceCompat.beginSection("createTabView");
        TabView createTabView = createTabView(tab, false);
        TraceCompat.endSection();
        this.mTabLayout.addView(createTabView);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (z) {
            createTabView.setSelected(true);
            this.mLastSelectedPosition = this.mTabLayout.getChildCount() - 1;
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void addTab(ActionBar.Tab tab, int i, boolean z) {
        TabView createTabView = createTabView(tab, false);
        this.mTabLayout.addView(createTabView, i);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (z) {
            createTabView.setSelected(true);
            this.mLastSelectedPosition = this.mTabLayout.getChildCount() - 1;
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void updateTab(int i) {
        ((TabView) this.mTabLayout.getChildAt(i)).update();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeTabAt(int i) {
        LinearLayout linearLayout = this.mTabLayout;
        if (linearLayout != null && linearLayout.getChildAt(i) != null) {
            this.mTabLayout.removeViewAt(i);
        }
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    public void removeAllTabs() {
        LinearLayout linearLayout = this.mTabLayout;
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter) spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            requestLayout();
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ((TabView) view).getTab().select();
    }

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        setTabIndicatorPosition(i, f);
    }

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageSelected(int i) {
        TabView tabView = (TabView) this.mTabLayout.getChildAt(i);
        if (tabView != null) {
            tabView.sendAccessibilityEvent(4);
        }
        setTabIndicatorPosition(i);
        int i2 = this.mLastSelectedPosition;
        if (i2 != -1) {
            boolean z = true;
            if (Math.abs(i2 - i) == 1) {
                TabView tabView2 = (TabView) this.mTabLayout.getChildAt(this.mLastSelectedPosition);
                ScrollingTabTextView scrollingTabTextView = null;
                ScrollingTabTextView scrollingTabTextView2 = tabView2 != null ? (ScrollingTabTextView) tabView2.getTextView() : null;
                if (tabView != null) {
                    scrollingTabTextView = (ScrollingTabTextView) tabView.getTextView();
                }
                if (scrollingTabTextView2 != null && scrollingTabTextView != null) {
                    if (ViewUtils.isLayoutRtl(this)) {
                        z = false;
                        scrollingTabTextView2.startScrollAnimation(z);
                        scrollingTabTextView.startScrollAnimation(z);
                    } else {
                        z = false;
                        scrollingTabTextView2.startScrollAnimation(z);
                        scrollingTabTextView.startScrollAnimation(z);
                    }
                }
            }
        }
        this.mLastSelectedPosition = i;
    }

    /* loaded from: classes3.dex */
    public static class TabView extends LinearLayout {
        public ImageView mBadgeView;
        public View mCustomView;
        public ImageView mIconView;
        public ScrollingTabContainerView mParent;
        public ActionBar.Tab mTab;
        public TextView mTextView;

        public TabView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            Folme.useAt(this).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this, new AnimConfig[0]);
        }

        public void attach(ScrollingTabContainerView scrollingTabContainerView, ActionBar.Tab tab, boolean z) {
            this.mParent = scrollingTabContainerView;
            this.mTab = tab;
            if (z) {
                setGravity(8388627);
            }
            update();
        }

        public void bindTab(ActionBar.Tab tab) {
            this.mTab = tab;
            update();
        }

        public void update() {
            int intrinsicHeight;
            int lineHeight;
            ActionBar.Tab tab = this.mTab;
            View customView = tab.getCustomView();
            if (customView != null) {
                this.mCustomView = this.mParent.updateCustomTabView(this, customView, this.mTextView, this.mIconView);
            } else {
                View view = this.mCustomView;
                if (view != null) {
                    removeView(view);
                    this.mCustomView = null;
                }
                Context context = getContext();
                Drawable icon = tab.getIcon();
                CharSequence text = tab.getText();
                if (icon != null) {
                    if (this.mIconView == null) {
                        ImageView imageView = new ImageView(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.gravity = 16;
                        imageView.setLayoutParams(layoutParams);
                        addView(imageView, 0);
                        this.mIconView = imageView;
                    }
                    this.mIconView.setImageDrawable(icon);
                    this.mIconView.setVisibility(0);
                } else {
                    ImageView imageView2 = this.mIconView;
                    if (imageView2 != null) {
                        imageView2.setVisibility(8);
                        this.mIconView.setImageDrawable(null);
                    }
                }
                if (text != null) {
                    if (this.mTextView == null) {
                        ScrollingTabTextView scrollingTabTextView = new ScrollingTabTextView(context, null, this.mParent.getTabTextStyle());
                        scrollingTabTextView.setEllipsize(TextUtils.TruncateAt.END);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams2.gravity = 16;
                        scrollingTabTextView.setLayoutParams(layoutParams2);
                        addView(scrollingTabTextView);
                        this.mTextView = scrollingTabTextView;
                    }
                    this.mTextView.setText(text);
                    this.mTextView.setVisibility(0);
                    Drawable resolveDrawable = AttributeResolver.resolveDrawable(context, R$attr.actionBarTabBadgeIcon);
                    if (resolveDrawable != null && this.mBadgeView == null) {
                        ImageView imageView3 = new ImageView(context);
                        imageView3.setImageDrawable(resolveDrawable);
                        imageView3.setVisibility(8);
                        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams3.gravity = 48;
                        Drawable background = getBackground();
                        if (background != null && (intrinsicHeight = background.getIntrinsicHeight()) > (lineHeight = this.mTextView.getLineHeight())) {
                            layoutParams3.topMargin = (intrinsicHeight - lineHeight) / 2;
                        }
                        imageView3.setLayoutParams(layoutParams3);
                        addView(imageView3);
                        this.mBadgeView = imageView3;
                    }
                } else {
                    TextView textView = this.mTextView;
                    if (textView != null) {
                        textView.setVisibility(8);
                        this.mTextView.setText((CharSequence) null);
                    }
                }
                ImageView imageView4 = this.mIconView;
                if (imageView4 != null) {
                    imageView4.setContentDescription(tab.getContentDescription());
                }
            }
            this.mParent.updateTabTextStyle(this.mTextView);
        }

        public ActionBar.Tab getTab() {
            return this.mTab;
        }

        public TextView getTextView() {
            return this.mTextView;
        }
    }

    /* loaded from: classes3.dex */
    public class TabAdapter extends BaseAdapter {
        public final /* synthetic */ ScrollingTabContainerView this$0;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.this$0.mTabLayout.getChildCount();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return ((TabView) this.this$0.mTabLayout.getChildAt(i)).getTab();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                return this.this$0.createTabView((ActionBar.Tab) getItem(i), true);
            }
            ((TabView) view).bindTab((ActionBar.Tab) getItem(i));
            return view;
        }
    }

    /* loaded from: classes3.dex */
    public static class TabSelectorRunnable implements Runnable {
        public int mPosition;
        public WeakReference<ScrollingTabContainerView> mRefs;

        public TabSelectorRunnable(ScrollingTabContainerView scrollingTabContainerView, int i) {
            this.mRefs = new WeakReference<>(scrollingTabContainerView);
            this.mPosition = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            View childAt;
            WeakReference<ScrollingTabContainerView> weakReference = this.mRefs;
            ScrollingTabContainerView scrollingTabContainerView = weakReference != null ? weakReference.get() : null;
            if (scrollingTabContainerView == null || (childAt = scrollingTabContainerView.mTabLayout.getChildAt(this.mPosition)) == null) {
                return;
            }
            scrollingTabContainerView.smoothScrollTo(childAt.getLeft() - ((scrollingTabContainerView.getWidth() - childAt.getWidth()) / 2), 0);
            scrollingTabContainerView.mTabSelector = null;
        }
    }

    /* loaded from: classes3.dex */
    public static class TabClickListener implements View.OnClickListener {
        public WeakReference<ScrollingTabContainerView> mRefs;

        public TabClickListener(ScrollingTabContainerView scrollingTabContainerView) {
            this.mRefs = new WeakReference<>(scrollingTabContainerView);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WeakReference<ScrollingTabContainerView> weakReference = this.mRefs;
            ScrollingTabContainerView scrollingTabContainerView = weakReference != null ? weakReference.get() : null;
            if (scrollingTabContainerView == null) {
                return;
            }
            ((TabView) view).getTab().select();
            int childCount = scrollingTabContainerView.mTabLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = scrollingTabContainerView.mTabLayout.getChildAt(i);
                childAt.setSelected(childAt == view);
            }
        }
    }
}
