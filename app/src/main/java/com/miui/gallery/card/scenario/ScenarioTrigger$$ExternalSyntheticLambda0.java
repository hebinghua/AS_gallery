package com.miui.gallery.card.scenario;

import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScenarioTrigger$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ ScenarioTrigger$$ExternalSyntheticLambda0 INSTANCE = new ScenarioTrigger$$ExternalSyntheticLambda0();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |localRules=%s", (AssistantScenarioStrategy.ScenarioRule) obj);
    }
}
