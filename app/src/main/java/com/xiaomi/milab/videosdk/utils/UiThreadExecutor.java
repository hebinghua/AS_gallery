package com.xiaomi.milab.videosdk.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public final class UiThreadExecutor {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper()) { // from class: com.xiaomi.milab.videosdk.utils.UiThreadExecutor.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Runnable callback = message.getCallback();
            if (callback != null) {
                callback.run();
                UiThreadExecutor.decrementToken((Token) message.obj);
                return;
            }
            super.handleMessage(message);
        }
    };
    private static final Map<String, Token> TOKENS = new HashMap();

    private UiThreadExecutor() {
    }

    public static void runTask(String str, Runnable runnable, long j) {
        if ("".equals(str)) {
            HANDLER.postDelayed(runnable, j);
            return;
        }
        HANDLER.postAtTime(runnable, nextToken(str), SystemClock.uptimeMillis() + j);
    }

    private static Token nextToken(String str) {
        Token token;
        Map<String, Token> map = TOKENS;
        synchronized (map) {
            token = map.get(str);
            if (token == null) {
                token = new Token(str);
                map.put(str, token);
            }
            token.runnablesCount++;
        }
        return token;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void decrementToken(Token token) {
        String str;
        Token remove;
        Map<String, Token> map = TOKENS;
        synchronized (map) {
            int i = token.runnablesCount - 1;
            token.runnablesCount = i;
            if (i == 0 && (remove = map.remove((str = token.id))) != token) {
                map.put(str, remove);
            }
        }
    }

    public static void cancelAll(String str) {
        Token remove;
        Map<String, Token> map = TOKENS;
        synchronized (map) {
            remove = map.remove(str);
        }
        if (remove == null) {
            return;
        }
        HANDLER.removeCallbacksAndMessages(remove);
    }

    /* loaded from: classes3.dex */
    public static final class Token {
        public final String id;
        public int runnablesCount;

        public Token(String str) {
            this.runnablesCount = 0;
            this.id = str;
        }
    }
}
