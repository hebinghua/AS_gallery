package com.miui.gallery.vlog.caption.ai;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.Arrays;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/* loaded from: classes2.dex */
public class AiVoiceToTextChannel {
    public ChannelCallback mChannelCallback;
    public int mMessageStatus;
    public boolean mSetup;
    public WebSocket mWebSocket;

    /* loaded from: classes2.dex */
    public interface ChannelCallback {
        void onClose();

        void onError(String str);

        void onOpen(AiVoiceToTextChannel aiVoiceToTextChannel);

        void onReceiveData(JsonObject jsonObject);
    }

    public AiVoiceToTextChannel(ChannelCallback channelCallback) {
        this.mChannelCallback = channelCallback;
    }

    public void connect() {
        this.mWebSocket = new OkHttpClient.Builder().build().newWebSocket(new Request.Builder().url(VoiceApiUtils.getUrl()).build(), new ThisWebSocketListener());
    }

    public boolean send(byte[] bArr, int i, int i2) {
        if (!this.mSetup || bArr == null || bArr.length == 0 || this.mWebSocket == null) {
            return false;
        }
        this.mWebSocket.send(getMessageFrame(bArr, i, i2));
        return true;
    }

    public boolean sendEnd() {
        WebSocket webSocket;
        if (this.mSetup && (webSocket = this.mWebSocket) != null) {
            webSocket.send(getEndFrame());
            return true;
        }
        return false;
    }

    public void close() {
        WebSocket webSocket = this.mWebSocket;
        if (webSocket != null) {
            webSocket.close(1000, "initiative close");
            this.mWebSocket = null;
        }
    }

    public void cancel() {
        WebSocket webSocket = this.mWebSocket;
        if (webSocket != null) {
            webSocket.cancel();
            this.mWebSocket = null;
        }
    }

    public final String getEndFrame() {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("status", (Number) 2);
        jsonObject2.addProperty("format", "audio/L16;rate=16000");
        jsonObject2.addProperty("encoding", "raw");
        jsonObject2.addProperty("audio", "");
        jsonObject.add("data", jsonObject2);
        return jsonObject.toString();
    }

    public final String getMessageFrame(byte[] bArr, int i, int i2) {
        JsonObject jsonObject = new JsonObject();
        if (this.mMessageStatus == 0) {
            JsonObject jsonObject2 = new JsonObject();
            JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("app_id", VoiceApiUtils.getAppID());
            jsonObject2.addProperty("aue", "raw");
            jsonObject2.addProperty("language", "cn_en");
            jsonObject2.addProperty("accent", "mandarin");
            jsonObject2.addProperty("domain", "xiaomi");
            jsonObject2.addProperty("rf", "deserted");
            jsonObject2.addProperty("rate", "16000");
            jsonObject2.addProperty("vgap", (Number) 15);
            jsonObject.add("common", jsonObject3);
            jsonObject.add("business", jsonObject2);
        }
        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("status", Integer.valueOf(this.mMessageStatus));
        jsonObject4.addProperty("format", "audio/L16;rate=16000");
        jsonObject4.addProperty("encoding", "raw");
        jsonObject4.addProperty("audio", VoiceApiUtils.encodeToStringBase64RFC4648(Arrays.copyOfRange(bArr, i, Math.min(bArr.length, i2 + i))));
        jsonObject.add("data", jsonObject4);
        if (this.mMessageStatus == 0) {
            this.mMessageStatus = 1;
        }
        return jsonObject.toString();
    }

    /* loaded from: classes2.dex */
    public class ThisWebSocketListener extends WebSocketListener {
        public ThisWebSocketListener() {
        }

        @Override // okhttp3.WebSocketListener
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            DefaultLogger.d("AiVoiceToTextChannel", "onOpen %s", AiVoiceToTextChannel.this.responseStr(response));
            AiVoiceToTextChannel.this.mSetup = true;
            if (AiVoiceToTextChannel.this.mChannelCallback != null) {
                AiVoiceToTextChannel.this.mChannelCallback.onOpen(AiVoiceToTextChannel.this);
            }
        }

        @Override // okhttp3.WebSocketListener
        public void onMessage(WebSocket webSocket, String str) {
            super.onMessage(webSocket, str);
            DefaultLogger.d("AiVoiceToTextChannel", "onMessage: " + str);
            try {
                JsonObject asJsonObject = new JsonParser().parse(str).getAsJsonObject();
                int asInt = asJsonObject.get("code").getAsInt();
                if (asInt == 0) {
                    JsonObject asJsonObject2 = asJsonObject.get("data").getAsJsonObject();
                    if (AiVoiceToTextChannel.this.mChannelCallback != null) {
                        AiVoiceToTextChannel.this.mChannelCallback.onReceiveData(asJsonObject2);
                    }
                } else {
                    String asString = asJsonObject.getAsJsonPrimitive("message").getAsString();
                    DefaultLogger.d("AiVoiceToTextChannel", "business exception %d %s", Integer.valueOf(asInt), asString);
                    if (AiVoiceToTextChannel.this.mChannelCallback != null) {
                        AiVoiceToTextChannel.this.mChannelCallback.onError(asString);
                    }
                }
            } catch (Exception e) {
                DefaultLogger.d("AiVoiceToTextChannel", "parse message error " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override // okhttp3.WebSocketListener
        public void onClosed(WebSocket webSocket, int i, String str) {
            super.onClosed(webSocket, i, str);
            AiVoiceToTextChannel.this.mSetup = false;
            DefaultLogger.d("AiVoiceToTextChannel", "onClosed: %d %s", Integer.valueOf(i), str);
            if (AiVoiceToTextChannel.this.mChannelCallback != null) {
                AiVoiceToTextChannel.this.mChannelCallback.onClose();
            }
        }

        @Override // okhttp3.WebSocketListener
        public void onFailure(WebSocket webSocket, Throwable th, Response response) {
            super.onFailure(webSocket, th, response);
            AiVoiceToTextChannel.this.mSetup = false;
            DefaultLogger.d("AiVoiceToTextChannel", "onFailure: %s, %s", th, AiVoiceToTextChannel.this.responseStr(response));
            if (AiVoiceToTextChannel.this.mChannelCallback != null) {
                AiVoiceToTextChannel.this.mChannelCallback.onError(th.getMessage());
            }
        }
    }

    public final String responseStr(Response response) {
        if (response != null && response.body() != null) {
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
