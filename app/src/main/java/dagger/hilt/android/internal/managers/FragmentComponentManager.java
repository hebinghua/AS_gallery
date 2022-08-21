package dagger.hilt.android.internal.managers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import dagger.hilt.EntryPoints;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.internal.GeneratedComponentManager;
import dagger.hilt.internal.Preconditions;

/* loaded from: classes3.dex */
public class FragmentComponentManager implements GeneratedComponentManager<Object> {
    public volatile Object component;
    public final Object componentLock = new Object();
    public final Fragment fragment;

    /* loaded from: classes3.dex */
    public interface FragmentComponentBuilderEntryPoint {
        FragmentComponentBuilder fragmentComponentBuilder();
    }

    public void validate(Fragment fragment) {
    }

    public FragmentComponentManager(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override // dagger.hilt.internal.GeneratedComponentManager
    /* renamed from: generatedComponent */
    public Object mo2560generatedComponent() {
        if (this.component == null) {
            synchronized (this.componentLock) {
                if (this.component == null) {
                    this.component = createComponent();
                }
            }
        }
        return this.component;
    }

    public final Object createComponent() {
        Preconditions.checkNotNull(this.fragment.getHost(), "Hilt Fragments must be attached before creating the component.");
        Preconditions.checkState(this.fragment.getHost() instanceof GeneratedComponentManager, "Hilt Fragments must be attached to an @AndroidEntryPoint Activity. Found: %s", this.fragment.getHost().getClass());
        validate(this.fragment);
        return ((FragmentComponentBuilderEntryPoint) EntryPoints.get(this.fragment.getHost(), FragmentComponentBuilderEntryPoint.class)).fragmentComponentBuilder().mo452fragment(this.fragment).mo451build();
    }

    public static final Context findActivity(Context context) {
        while ((context instanceof ContextWrapper) && !(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return context;
    }

    public static ContextWrapper createContextWrapper(Context base, Fragment fragment) {
        return new ViewComponentManager$FragmentContextWrapper(base, fragment);
    }

    public static ContextWrapper createContextWrapper(LayoutInflater baseInflater, Fragment fragment) {
        return new ViewComponentManager$FragmentContextWrapper(baseInflater, fragment);
    }
}
