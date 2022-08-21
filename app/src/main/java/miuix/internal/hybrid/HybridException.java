package miuix.internal.hybrid;

import miuix.hybrid.Response;

/* loaded from: classes3.dex */
public class HybridException extends Exception {
    private static final long serialVersionUID = 1;
    private Response mResponse;

    public HybridException() {
        super(new Response(200).toString());
        this.mResponse = new Response(200);
    }

    public HybridException(int i, String str) {
        super(new Response(i, str).toString());
        this.mResponse = new Response(i, str);
    }

    public Response getResponse() {
        return this.mResponse;
    }
}
