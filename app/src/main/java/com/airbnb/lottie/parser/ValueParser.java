package com.airbnb.lottie.parser;

import com.airbnb.lottie.parser.moshi.JsonReader;
import java.io.IOException;

/* loaded from: classes.dex */
public interface ValueParser<V> {
    /* renamed from: parse */
    V mo202parse(JsonReader jsonReader, float f) throws IOException;
}
