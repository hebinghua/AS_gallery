package com.miui.gallery.strategy;

import android.content.res.Configuration;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public final class StrategyFactory {
    public IStrategy$IDirectionStrategy mDirectionStrategy;
    public IStrategy$INavigationBarStrategy mNavigationBarStrategy;
    public IStrategy$IWindowModeStrategy mWindowModeStrategy;
    public IStrategy$IWindowSizeStrategy mWindowSizeStrategy;
    public StrategyContext.DisableStrategyType mDisAbleStrategyType = StrategyContext.DisableStrategyType.NULL;
    public final StrategyHandler mStrategyHandler = new StrategyHandler();

    public final StrategyHandler configStrategyHandler(IStrategy$IDirectionStrategy iStrategy$IDirectionStrategy, IStrategy$IWindowModeStrategy iStrategy$IWindowModeStrategy, IStrategy$IWindowSizeStrategy iStrategy$IWindowSizeStrategy) {
        this.mStrategyHandler.setDirectionStrategy(iStrategy$IDirectionStrategy);
        this.mStrategyHandler.setWindowModeStrategy(iStrategy$IWindowModeStrategy);
        this.mStrategyHandler.setScreenSizeStrategy(iStrategy$IWindowSizeStrategy);
        return this.mStrategyHandler;
    }

    public StrategyHandler getStrategyHandler(boolean z) {
        StrategyContext.DisableStrategyType disableStrategyType = this.mDisAbleStrategyType;
        if (disableStrategyType == StrategyContext.DisableStrategyType.ALL || disableStrategyType == StrategyContext.DisableStrategyType.NAVIGATION_BAR) {
            return null;
        }
        if (z) {
            if (disableStrategyType == StrategyContext.DisableStrategyType.FULL_SCREEN) {
                return null;
            }
            IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy = this.mNavigationBarStrategy;
            if (iStrategy$INavigationBarStrategy != null && (iStrategy$INavigationBarStrategy instanceof FullScreenNavigationBarStrategy)) {
                return this.mStrategyHandler.setNavigationBarStrategy(iStrategy$INavigationBarStrategy);
            }
            FullScreenNavigationBarStrategy fullScreenNavigationBarStrategy = new FullScreenNavigationBarStrategy();
            this.mNavigationBarStrategy = fullScreenNavigationBarStrategy;
            return this.mStrategyHandler.setNavigationBarStrategy(fullScreenNavigationBarStrategy);
        } else if (disableStrategyType == StrategyContext.DisableStrategyType.TRADITION) {
            return null;
        } else {
            IStrategy$INavigationBarStrategy iStrategy$INavigationBarStrategy2 = this.mNavigationBarStrategy;
            if (iStrategy$INavigationBarStrategy2 != null && (iStrategy$INavigationBarStrategy2 instanceof TraditionNavigationBarStrategy)) {
                return this.mStrategyHandler.setNavigationBarStrategy(iStrategy$INavigationBarStrategy2);
            }
            TraditionNavigationBarStrategy traditionNavigationBarStrategy = new TraditionNavigationBarStrategy();
            this.mNavigationBarStrategy = traditionNavigationBarStrategy;
            return this.mStrategyHandler.setNavigationBarStrategy(traditionNavigationBarStrategy);
        }
    }

    public StrategyHandler getStrategyHandler(IStrategyFollower iStrategyFollower) {
        IStrategy$IDirectionStrategy directionStrategy = getDirectionStrategy(iStrategyFollower);
        IStrategy$IWindowModeStrategy windowModeStrategy = getWindowModeStrategy(iStrategyFollower);
        IStrategy$IWindowSizeStrategy windowSizeStrategy = getWindowSizeStrategy(iStrategyFollower);
        if (directionStrategy == null && windowModeStrategy == null && windowSizeStrategy == null) {
            return null;
        }
        return configStrategyHandler(directionStrategy, windowModeStrategy, windowSizeStrategy);
    }

    public StrategyHandler getStrategyHandler(IStrategyFollower iStrategyFollower, Configuration configuration) {
        IStrategy$IDirectionStrategy directionStrategy = getDirectionStrategy(configuration);
        IStrategy$IWindowModeStrategy windowModeStrategy = getWindowModeStrategy(iStrategyFollower);
        IStrategy$IWindowSizeStrategy windowSizeStrategy = getWindowSizeStrategy(iStrategyFollower);
        if (directionStrategy == null && windowModeStrategy == null && windowSizeStrategy == null) {
            return null;
        }
        return configStrategyHandler(directionStrategy, windowModeStrategy, windowSizeStrategy);
    }

    public StrategyHandler getStrategyHandler(IStrategyFollower iStrategyFollower, boolean z) {
        IStrategy$IDirectionStrategy directionStrategy = getDirectionStrategy(iStrategyFollower);
        IStrategy$IWindowModeStrategy windowModeStrategy = getWindowModeStrategy(z);
        IStrategy$IWindowSizeStrategy windowSizeStrategy = getWindowSizeStrategy(iStrategyFollower);
        if (directionStrategy == null && windowModeStrategy == null && windowSizeStrategy == null) {
            return null;
        }
        return configStrategyHandler(directionStrategy, windowModeStrategy, windowSizeStrategy);
    }

    public StrategyHandler getStrategyHandler(boolean z, Configuration configuration) {
        IStrategy$IDirectionStrategy directionStrategy = getDirectionStrategy(configuration);
        IStrategy$IWindowModeStrategy windowModeStrategy = getWindowModeStrategy(z);
        IStrategy$IWindowSizeStrategy windowSizeStrategy = getWindowSizeStrategy(BaseBuildUtil.isLargeHorizontalWindow());
        if (directionStrategy == null && windowModeStrategy == null && windowSizeStrategy == null) {
            return null;
        }
        return configStrategyHandler(directionStrategy, windowModeStrategy, windowSizeStrategy);
    }

    public final IStrategy$IDirectionStrategy getDirectionStrategy(IStrategyFollower iStrategyFollower) {
        if (iStrategyFollower == null || iStrategyFollower.mo546getActivity() == null) {
            return null;
        }
        return getDirectionStrategy(iStrategyFollower.mo546getActivity().getResources().getConfiguration().orientation);
    }

    public final IStrategy$IDirectionStrategy getDirectionStrategy(Configuration configuration) {
        return getDirectionStrategy(configuration.orientation);
    }

    public final IStrategy$IDirectionStrategy getDirectionStrategy(int i) {
        StrategyContext.DisableStrategyType disableStrategyType = this.mDisAbleStrategyType;
        if (disableStrategyType == StrategyContext.DisableStrategyType.ALL || disableStrategyType == StrategyContext.DisableStrategyType.DIRECTION) {
            return null;
        }
        if (i == 2) {
            if (disableStrategyType == StrategyContext.DisableStrategyType.LANDSCAPE_DIRECTION) {
                return null;
            }
            IStrategy$IDirectionStrategy iStrategy$IDirectionStrategy = this.mDirectionStrategy;
            if (iStrategy$IDirectionStrategy != null && (iStrategy$IDirectionStrategy instanceof LandscapeStrategy)) {
                return iStrategy$IDirectionStrategy;
            }
            LandscapeStrategy landscapeStrategy = new LandscapeStrategy();
            this.mDirectionStrategy = landscapeStrategy;
            return landscapeStrategy;
        } else if (disableStrategyType == StrategyContext.DisableStrategyType.PORTRAIT_DIRECTION) {
            return null;
        } else {
            IStrategy$IDirectionStrategy iStrategy$IDirectionStrategy2 = this.mDirectionStrategy;
            if (iStrategy$IDirectionStrategy2 != null && (iStrategy$IDirectionStrategy2 instanceof PortraitStrategy)) {
                return iStrategy$IDirectionStrategy2;
            }
            PortraitStrategy portraitStrategy = new PortraitStrategy();
            this.mDirectionStrategy = portraitStrategy;
            return portraitStrategy;
        }
    }

    public final IStrategy$IWindowModeStrategy getWindowModeStrategy(IStrategyFollower iStrategyFollower) {
        if (iStrategyFollower == null || iStrategyFollower.mo546getActivity() == null) {
            return null;
        }
        return getWindowModeStrategy(iStrategyFollower.mo546getActivity().isInMultiWindowMode());
    }

    public final IStrategy$IWindowModeStrategy getWindowModeStrategy(boolean z) {
        StrategyContext.DisableStrategyType disableStrategyType = this.mDisAbleStrategyType;
        if (disableStrategyType == StrategyContext.DisableStrategyType.ALL || disableStrategyType == StrategyContext.DisableStrategyType.WINDOW_MODE) {
            return null;
        }
        if (z) {
            if (disableStrategyType == StrategyContext.DisableStrategyType.IN_MULTI_WINDOW) {
                return null;
            }
            IStrategy$IWindowModeStrategy iStrategy$IWindowModeStrategy = this.mWindowModeStrategy;
            if (iStrategy$IWindowModeStrategy != null && (iStrategy$IWindowModeStrategy instanceof InMultiWindowStrategy)) {
                return iStrategy$IWindowModeStrategy;
            }
            InMultiWindowStrategy inMultiWindowStrategy = new InMultiWindowStrategy();
            this.mWindowModeStrategy = inMultiWindowStrategy;
            return inMultiWindowStrategy;
        } else if (disableStrategyType == StrategyContext.DisableStrategyType.IN_SINGLE_WINDOW) {
            return null;
        } else {
            IStrategy$IWindowModeStrategy iStrategy$IWindowModeStrategy2 = this.mWindowModeStrategy;
            if (iStrategy$IWindowModeStrategy2 != null && (iStrategy$IWindowModeStrategy2 instanceof InSingleWindowStrategy)) {
                return iStrategy$IWindowModeStrategy2;
            }
            InSingleWindowStrategy inSingleWindowStrategy = new InSingleWindowStrategy();
            this.mWindowModeStrategy = inSingleWindowStrategy;
            return inSingleWindowStrategy;
        }
    }

    public final IStrategy$IWindowSizeStrategy getWindowSizeStrategy(IStrategyFollower iStrategyFollower) {
        if (iStrategyFollower == null || iStrategyFollower.mo546getActivity() == null) {
            return null;
        }
        return getWindowSizeStrategy(BaseBuildUtil.isLargeScreenDevice());
    }

    public final IStrategy$IWindowSizeStrategy getWindowSizeStrategy(boolean z) {
        StrategyContext.DisableStrategyType disableStrategyType = this.mDisAbleStrategyType;
        if (disableStrategyType == StrategyContext.DisableStrategyType.ALL || disableStrategyType == StrategyContext.DisableStrategyType.SCREEN_SIZE) {
            return null;
        }
        if (!z) {
            if (disableStrategyType == StrategyContext.DisableStrategyType.SMALL_SCREEN) {
                return null;
            }
            IStrategy$IWindowSizeStrategy iStrategy$IWindowSizeStrategy = this.mWindowSizeStrategy;
            if (iStrategy$IWindowSizeStrategy != null && (iStrategy$IWindowSizeStrategy instanceof SmallWindowStrategy)) {
                return iStrategy$IWindowSizeStrategy;
            }
            SmallWindowStrategy smallWindowStrategy = new SmallWindowStrategy();
            this.mWindowSizeStrategy = smallWindowStrategy;
            return smallWindowStrategy;
        } else if (disableStrategyType == StrategyContext.DisableStrategyType.LARGE_SCREEN) {
            return null;
        } else {
            IStrategy$IWindowSizeStrategy iStrategy$IWindowSizeStrategy2 = this.mWindowSizeStrategy;
            if (iStrategy$IWindowSizeStrategy2 != null && (iStrategy$IWindowSizeStrategy2 instanceof LargeWindowStrategy)) {
                return iStrategy$IWindowSizeStrategy2;
            }
            LargeWindowStrategy largeWindowStrategy = new LargeWindowStrategy();
            this.mWindowSizeStrategy = largeWindowStrategy;
            return largeWindowStrategy;
        }
    }

    public void setDisableStrategy(StrategyContext.DisableStrategyType disableStrategyType) {
        this.mDisAbleStrategyType = disableStrategyType;
    }
}
