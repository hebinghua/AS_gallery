package com.miui.gallery.ui.album.main.utils.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.card.ui.detail.SlideShowHeaderView;
import com.miui.gallery.card.ui.detail.StoryRecyclerViewItem;
import com.miui.gallery.collage.widget.CollageLayout;
import com.miui.gallery.search.navigationpage.NavigationPeopleView;
import com.miui.gallery.search.resultpage.ImageResultHeaderItem;
import com.miui.gallery.search.widget.BannerSearchBar;
import com.miui.gallery.search.widget.IntroIconView;
import com.miui.gallery.ui.AdJustConstraintLayout;
import com.miui.gallery.ui.AlbumPagePrivateView;
import com.miui.gallery.ui.BabyAlbumDetailGridHeaderItem;
import com.miui.gallery.ui.CleanerRotateProgressBar;
import com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout;
import com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.ui.NormalTimeLineGridHeaderItem;
import com.miui.gallery.ui.PeoplePageGridItem;
import com.miui.gallery.ui.PhotoChoiceContainer;
import com.miui.gallery.ui.PhotoChoiceTitle;
import com.miui.gallery.ui.PhotoPageBurstItem;
import com.miui.gallery.ui.PhotoPageGifItem;
import com.miui.gallery.ui.PhotoPageVideoItem;
import com.miui.gallery.ui.RecentTimeLineGridHeaderItem;
import com.miui.gallery.ui.TrashGridItem;
import com.miui.gallery.ui.photoPage.bars.view.LimitRecyclerView;
import com.miui.gallery.view.ExpandAllGridView;
import com.miui.gallery.view.menu.ActionMenuItemView;
import com.miui.gallery.view.menu.ListMenuItemView;
import com.miui.gallery.view.menu.NonResidentFavoriteListMenuItemView;
import com.miui.gallery.widget.ColorRingProgress;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.GalleryPullZoomLayout;
import com.miui.gallery.widget.GalleryViewPager;
import com.miui.gallery.widget.PagerIndicator;
import com.miui.gallery.widget.PanelBar;
import com.miui.gallery.widget.PhotoPageLayout;
import com.miui.gallery.widget.RotateRingView;
import com.miui.gallery.widget.RoundedConstraintLayout;
import com.miui.gallery.widget.ViewPager;
import com.miui.gallery.widget.WHRatioImageView;
import com.miui.gallery.widget.WHRatioRoundedImageView;
import com.miui.gallery.widget.menu.ImmersionMenuItemView;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.slip.VerticalSlipLayout;
import com.miui.gallery.widget.tsd.NestedTwoStageDrawer;
import miuix.appcompat.internal.app.widget.ActionBarContainer;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.app.widget.ScrollingTabContainerView;
import miuix.miuixbasewidget.widget.FilterSortView;
import miuix.slidingwidget.widget.SlidingButton;
import miuix.springback.view.SpringBackLayout;

/* loaded from: classes2.dex */
public class GalleryViewCreator {
    public static final IViewCreator sAlbumPageViewCreator = new DefaultAlbumPageViewCreator();
    public static final IViewCreator sCommonViewCreator = new CommonViewCreator();
    public static final LayoutInflater.Factory2 sCommonViewFactory = new LayoutInflater.Factory2() { // from class: com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator.1
        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
            return GalleryViewCreator.sCommonViewCreator.onCreateView(view, str, context, attributeSet);
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String str, Context context, AttributeSet attributeSet) {
            return GalleryViewCreator.sCommonViewCreator.onCreateView(null, str, context, attributeSet);
        }
    };
    public static final LayoutInflater.Factory2 sAlbumPageViewFactory = new LayoutInflater.Factory2() { // from class: com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator.2
        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
            return GalleryViewCreator.sAlbumPageViewCreator.onCreateView(view, str, context, attributeSet);
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String str, Context context, AttributeSet attributeSet) {
            return GalleryViewCreator.sAlbumPageViewCreator.onCreateView(null, str, context, attributeSet);
        }
    };

    public static LayoutInflater.Factory2 getAlbumPageViewFactory() {
        return sAlbumPageViewFactory;
    }

    public static LayoutInflater.Factory2 getViewFactory() {
        return sCommonViewFactory;
    }

    /* loaded from: classes2.dex */
    public static class DefaultAlbumPageViewCreator implements IViewCreator {
        @Override // com.miui.gallery.ui.album.main.utils.factory.IViewCreator
        public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
            return createView(view, str, context, attributeSet);
        }

        public View createView(View view, String str, Context context, AttributeSet attributeSet) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1616617949:
                    if (str.equals("com.miui.gallery.ui.AdJustConstraintLayout")) {
                        c = 0;
                        break;
                    }
                    break;
                case -979739473:
                    if (str.equals("androidx.constraintlayout.widget.ConstraintLayout")) {
                        c = 1;
                        break;
                    }
                    break;
                case -938935918:
                    if (str.equals("TextView")) {
                        c = 2;
                        break;
                    }
                    break;
                case 141732585:
                    if (str.equals("androidx.recyclerview.widget.RecyclerView")) {
                        c = 3;
                        break;
                    }
                    break;
                case 416599120:
                    if (str.equals("com.miui.gallery.widget.WHRatioRoundedImageView")) {
                        c = 4;
                        break;
                    }
                    break;
                case 831117471:
                    if (str.equals("com.miui.gallery.widget.EmptyPage")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1125864064:
                    if (str.equals("ImageView")) {
                        c = 6;
                        break;
                    }
                    break;
                case 1127291599:
                    if (str.equals("LinearLayout")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1216317283:
                    if (str.equals("com.miui.gallery.widget.WHRatioImageView")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 1260470547:
                    if (str.equals("ViewStub")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 1265864088:
                    if (str.equals("miuix.slidingwidget.widget.SlidingButton")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1401621482:
                    if (str.equals("com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout")) {
                        c = 11;
                        break;
                    }
                    break;
                case 1564176919:
                    if (str.equals("com.miui.gallery.widget.RoundedConstraintLayout")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 1601505219:
                    if (str.equals("CheckBox")) {
                        c = '\r';
                        break;
                    }
                    break;
                case 1793173321:
                    if (str.equals("com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout")) {
                        c = 14;
                        break;
                    }
                    break;
                case 1893602176:
                    if (str.equals("com.miui.gallery.widget.tsd.NestedTwoStageDrawer")) {
                        c = 15;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return new AdJustConstraintLayout(context, attributeSet);
                case 1:
                    return new ConstraintLayout(context, attributeSet);
                case 2:
                    return new TextView(context, attributeSet);
                case 3:
                    return new RecyclerView(context, attributeSet);
                case 4:
                    return new WHRatioRoundedImageView(context, attributeSet);
                case 5:
                    return new EmptyPage(context, attributeSet);
                case 6:
                    return new ImageView(context, attributeSet);
                case 7:
                    return new LinearLayout(context, attributeSet);
                case '\b':
                    return new WHRatioImageView(context, attributeSet);
                case '\t':
                    return new ViewStub(context, attributeSet);
                case '\n':
                    return new SlidingButton(context, attributeSet);
                case 11:
                    return new CommonWrapperCheckableGridItemLayout(context, attributeSet);
                case '\f':
                    return new RoundedConstraintLayout(context, attributeSet);
                case '\r':
                    return new CheckBox(context, attributeSet);
                case 14:
                    return new CommonWrapperCheckableLinearItemLayout(context, attributeSet);
                case 15:
                    return new NestedTwoStageDrawer(context, attributeSet);
                default:
                    return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CommonViewCreator implements IViewCreator {
        @Override // com.miui.gallery.ui.album.main.utils.factory.IViewCreator
        public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
            return createView(view, str, context, attributeSet);
        }

        public View createView(View view, String str, Context context, AttributeSet attributeSet) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2140381704:
                    if (str.equals("com.miui.gallery.card.ui.detail.StoryRecyclerViewItem")) {
                        c = 0;
                        break;
                    }
                    break;
                case -2109895607:
                    if (str.equals("com.miui.gallery.widget.PagerIndicator")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1968433030:
                    if (str.equals("com.miui.gallery.search.navigationpage.NavigationPeopleView")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1922170973:
                    if (str.equals("com.miui.gallery.widget.ColorRingProgress")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1874387336:
                    if (str.equals("com.miui.gallery.ui.MicroThumbGridItem")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1813453230:
                    if (str.equals("com.miui.gallery.ui.PhotoChoiceTitle")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1750297253:
                    if (str.equals("com.miui.gallery.view.menu.NonResidentFavoriteListMenuItemView")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1616617949:
                    if (str.equals("com.miui.gallery.ui.AdJustConstraintLayout")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1148222740:
                    if (str.equals("com.miui.gallery.card.ui.detail.SlideShowHeaderView")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -1009462698:
                    if (str.equals("com.miui.gallery.view.ExpandAllGridView")) {
                        c = '\t';
                        break;
                    }
                    break;
                case -1005702249:
                    if (str.equals("com.miui.gallery.widget.GalleryPullZoomLayout")) {
                        c = '\n';
                        break;
                    }
                    break;
                case -979739473:
                    if (str.equals("androidx.constraintlayout.widget.ConstraintLayout")) {
                        c = 11;
                        break;
                    }
                    break;
                case -938935918:
                    if (str.equals("TextView")) {
                        c = '\f';
                        break;
                    }
                    break;
                case -632905302:
                    if (str.equals("com.miui.gallery.ui.photoPage.bars.view.LimitRecyclerView")) {
                        c = '\r';
                        break;
                    }
                    break;
                case -443652810:
                    if (str.equals("RelativeLayout")) {
                        c = 14;
                        break;
                    }
                    break;
                case -128904763:
                    if (str.equals("miuix.appcompat.internal.app.widget.ActionBarView")) {
                        c = 15;
                        break;
                    }
                    break;
                case -128157989:
                    if (str.equals("com.miui.gallery.ui.NormalTimeLineGridHeaderItem")) {
                        c = 16;
                        break;
                    }
                    break;
                case -41964337:
                    if (str.equals("com.miui.gallery.search.resultpage.ImageResultHeaderItem")) {
                        c = 17;
                        break;
                    }
                    break;
                case -7270793:
                    if (str.equals("com.miui.gallery.ui.AlbumPagePrivateView")) {
                        c = 18;
                        break;
                    }
                    break;
                case 141732585:
                    if (str.equals("androidx.recyclerview.widget.RecyclerView")) {
                        c = 19;
                        break;
                    }
                    break;
                case 163327980:
                    if (str.equals("com.miui.gallery.widget.PanelBar")) {
                        c = 20;
                        break;
                    }
                    break;
                case 169881183:
                    if (str.equals("miuix.appcompat.internal.app.widget.ScrollingTabContainerView$TabView")) {
                        c = 21;
                        break;
                    }
                    break;
                case 185489082:
                    if (str.equals("com.miui.gallery.ui.PhotoPageVideoItem")) {
                        c = 22;
                        break;
                    }
                    break;
                case 227639319:
                    if (str.equals("androidx.appcompat.widget.AppCompatImageView")) {
                        c = 23;
                        break;
                    }
                    break;
                case 476534752:
                    if (str.equals("com.miui.gallery.view.menu.ActionMenuItemView")) {
                        c = 24;
                        break;
                    }
                    break;
                case 536893456:
                    if (str.equals("com.miui.gallery.widget.menu.ImmersionMenuItemView")) {
                        c = 25;
                        break;
                    }
                    break;
                case 663785281:
                    if (str.equals("com.miui.gallery.widget.ViewPager")) {
                        c = 26;
                        break;
                    }
                    break;
                case 798026081:
                    if (str.equals("miuix.appcompat.internal.app.widget.ActionBarContainer")) {
                        c = 27;
                        break;
                    }
                    break;
                case 910084653:
                    if (str.equals("com.miui.gallery.widget.RotateRingView")) {
                        c = 28;
                        break;
                    }
                    break;
                case 974773370:
                    if (str.equals("miuix.appcompat.internal.app.widget.ActionBarOverlayLayout")) {
                        c = 29;
                        break;
                    }
                    break;
                case 1041003657:
                    if (str.equals("androidx.core.widget.NestedScrollView")) {
                        c = 30;
                        break;
                    }
                    break;
                case 1097497337:
                    if (str.equals("com.miui.gallery.search.widget.IntroIconView")) {
                        c = 31;
                        break;
                    }
                    break;
                case 1125864064:
                    if (str.equals("ImageView")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 1127291599:
                    if (str.equals("LinearLayout")) {
                        c = '!';
                        break;
                    }
                    break;
                case 1190564430:
                    if (str.equals("miuix.miuixbasewidget.widget.FilterSortView")) {
                        c = CoreConstants.DOUBLE_QUOTE_CHAR;
                        break;
                    }
                    break;
                case 1216317283:
                    if (str.equals("com.miui.gallery.widget.WHRatioImageView")) {
                        c = '#';
                        break;
                    }
                    break;
                case 1260470547:
                    if (str.equals("ViewStub")) {
                        c = CoreConstants.DOLLAR;
                        break;
                    }
                    break;
                case 1265864088:
                    if (str.equals("miuix.slidingwidget.widget.SlidingButton")) {
                        c = CoreConstants.PERCENT_CHAR;
                        break;
                    }
                    break;
                case 1277066801:
                    if (str.equals("com.miui.gallery.widget.slip.VerticalSlipLayout")) {
                        c = '&';
                        break;
                    }
                    break;
                case 1310765783:
                    if (str.equals("FrameLayout")) {
                        c = CoreConstants.SINGLE_QUOTE_CHAR;
                        break;
                    }
                    break;
                case 1328496745:
                    if (str.equals("com.miui.gallery.widget.GalleryViewPager")) {
                        c = CoreConstants.LEFT_PARENTHESIS_CHAR;
                        break;
                    }
                    break;
                case 1359302879:
                    if (str.equals("com.miui.gallery.ui.PhotoPageBurstItem")) {
                        c = CoreConstants.RIGHT_PARENTHESIS_CHAR;
                        break;
                    }
                    break;
                case 1367333346:
                    if (str.equals("com.miui.gallery.ui.CleanerRotateProgressBar")) {
                        c = '*';
                        break;
                    }
                    break;
                case 1411260100:
                    if (str.equals("com.miui.gallery.ui.PeoplePageGridItem")) {
                        c = '+';
                        break;
                    }
                    break;
                case 1506957499:
                    if (str.equals("com.miui.gallery.collage.widget.CollageLayout")) {
                        c = CoreConstants.COMMA_CHAR;
                        break;
                    }
                    break;
                case 1591259471:
                    if (str.equals("com.miui.gallery.ui.RecentTimeLineGridHeaderItem")) {
                        c = CoreConstants.DASH_CHAR;
                        break;
                    }
                    break;
                case 1601505219:
                    if (str.equals("CheckBox")) {
                        c = CoreConstants.DOT;
                        break;
                    }
                    break;
                case 1763702126:
                    if (str.equals("com.miui.gallery.search.widget.BannerSearchBar")) {
                        c = '/';
                        break;
                    }
                    break;
                case 1806794504:
                    if (str.equals("com.miui.gallery.view.menu.ListMenuItemView")) {
                        c = '0';
                        break;
                    }
                    break;
                case 1821019427:
                    if (str.equals("com.miui.gallery.ui.BabyAlbumDetailGridHeaderItem")) {
                        c = '1';
                        break;
                    }
                    break;
                case 1893602176:
                    if (str.equals("com.miui.gallery.widget.tsd.NestedTwoStageDrawer")) {
                        c = '2';
                        break;
                    }
                    break;
                case 1919924355:
                    if (str.equals("com.miui.gallery.ui.PhotoPageGifItem")) {
                        c = '3';
                        break;
                    }
                    break;
                case 1954513787:
                    if (str.equals("com.miui.gallery.ui.PhotoChoiceContainer")) {
                        c = '4';
                        break;
                    }
                    break;
                case 1954975556:
                    if (str.equals("com.miui.gallery.ui.TrashGridItem")) {
                        c = '5';
                        break;
                    }
                    break;
                case 1970964393:
                    if (str.equals("com.miui.gallery.widget.recyclerview.GalleryRecyclerView")) {
                        c = '6';
                        break;
                    }
                    break;
                case 2001146706:
                    if (str.equals("Button")) {
                        c = '7';
                        break;
                    }
                    break;
                case 2045674259:
                    if (str.equals("miuix.springback.view.SpringBackLayout")) {
                        c = '8';
                        break;
                    }
                    break;
                case 2059813682:
                    if (str.equals("ScrollView")) {
                        c = '9';
                        break;
                    }
                    break;
                case 2125278030:
                    if (str.equals("com.miui.gallery.widget.PhotoPageLayout")) {
                        c = CoreConstants.COLON_CHAR;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return new StoryRecyclerViewItem(context, attributeSet);
                case 1:
                    return new PagerIndicator(context, attributeSet);
                case 2:
                    return new NavigationPeopleView(context, attributeSet);
                case 3:
                    return new ColorRingProgress(context, attributeSet);
                case 4:
                    return new MicroThumbGridItem(context, attributeSet);
                case 5:
                    return new PhotoChoiceTitle(context, attributeSet);
                case 6:
                    return new NonResidentFavoriteListMenuItemView(context, attributeSet);
                case 7:
                    return new AdJustConstraintLayout(context, attributeSet);
                case '\b':
                    return new SlideShowHeaderView(context, attributeSet);
                case '\t':
                    return new ExpandAllGridView(context, attributeSet);
                case '\n':
                    return new GalleryPullZoomLayout(context, attributeSet);
                case 11:
                    return new ConstraintLayout(context, attributeSet);
                case '\f':
                    return new TextView(context, attributeSet);
                case '\r':
                    return new LimitRecyclerView(context, attributeSet);
                case 14:
                    return new RelativeLayout(context, attributeSet);
                case 15:
                    return new ActionBarView(context, attributeSet);
                case 16:
                    return new NormalTimeLineGridHeaderItem(context, attributeSet);
                case 17:
                    return new ImageResultHeaderItem(context, attributeSet);
                case 18:
                    return new AlbumPagePrivateView(context, attributeSet);
                case 19:
                    return new RecyclerView(context, attributeSet);
                case 20:
                    return new PanelBar(context, attributeSet);
                case 21:
                    return new ScrollingTabContainerView.TabView(context, attributeSet);
                case 22:
                    return new PhotoPageVideoItem(context, attributeSet);
                case 23:
                    return new AppCompatImageView(context, attributeSet);
                case 24:
                    return new ActionMenuItemView(context, attributeSet);
                case 25:
                    return new ImmersionMenuItemView(context, attributeSet);
                case 26:
                    return new ViewPager(context, attributeSet);
                case 27:
                    return new ActionBarContainer(context, attributeSet);
                case 28:
                    return new RotateRingView(context, attributeSet);
                case 29:
                    return new ActionBarOverlayLayout(context, attributeSet);
                case 30:
                    return new NestedScrollView(context, attributeSet);
                case 31:
                    return new IntroIconView(context, attributeSet);
                case ' ':
                    return new ImageView(context, attributeSet);
                case '!':
                    return new LinearLayout(context, attributeSet);
                case '\"':
                    return new FilterSortView(context, attributeSet);
                case '#':
                    return new WHRatioImageView(context, attributeSet);
                case '$':
                    return new ViewStub(context, attributeSet);
                case '%':
                    return new SlidingButton(context, attributeSet);
                case '&':
                    return new VerticalSlipLayout(context, attributeSet);
                case '\'':
                    return new FrameLayout(context, attributeSet);
                case '(':
                    return new GalleryViewPager(context, attributeSet);
                case ')':
                    return new PhotoPageBurstItem(context, attributeSet);
                case '*':
                    return new CleanerRotateProgressBar(context, attributeSet);
                case '+':
                    return new PeoplePageGridItem(context, attributeSet);
                case ',':
                    return new CollageLayout(context, attributeSet);
                case '-':
                    return new RecentTimeLineGridHeaderItem(context, attributeSet);
                case '.':
                    return new CheckBox(context, attributeSet);
                case '/':
                    return new BannerSearchBar(context, attributeSet);
                case '0':
                    return new ListMenuItemView(context, attributeSet);
                case '1':
                    return new BabyAlbumDetailGridHeaderItem(context, attributeSet);
                case '2':
                    return new NestedTwoStageDrawer(context, attributeSet);
                case '3':
                    return new PhotoPageGifItem(context, attributeSet);
                case '4':
                    return new PhotoChoiceContainer(context, attributeSet);
                case '5':
                    return new TrashGridItem(context, attributeSet);
                case '6':
                    return new GalleryRecyclerView(context, attributeSet);
                case '7':
                    return new Button(context, attributeSet);
                case '8':
                    return new SpringBackLayout(context, attributeSet);
                case '9':
                    return new ScrollView(context, attributeSet);
                case ':':
                    return new PhotoPageLayout(context, attributeSet);
                default:
                    return null;
            }
        }
    }
}
