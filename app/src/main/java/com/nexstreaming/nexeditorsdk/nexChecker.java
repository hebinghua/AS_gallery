package com.nexstreaming.nexeditorsdk;

import com.nexstreaming.checkcaps.a;

/* loaded from: classes3.dex */
public final class nexChecker {
    public static final int UHD_HEIGHT = 2160;
    public static final int UHD_WIDTH = 3840;
    private static com.nexstreaming.checkcaps.a mChecker;

    /* loaded from: classes3.dex */
    public interface nexCheckerListener {
        void onCheckerCapsResult(int i);
    }

    public static void checkUHD(final nexCheckerListener nexcheckerlistener) {
        com.nexstreaming.checkcaps.a aVar = new com.nexstreaming.checkcaps.a(false);
        mChecker = aVar;
        aVar.a(new a.InterfaceC0103a() { // from class: com.nexstreaming.nexeditorsdk.nexChecker.1
            @Override // com.nexstreaming.checkcaps.a.InterfaceC0103a
            public void a(com.nexstreaming.checkcaps.a aVar2, String str) {
            }

            @Override // com.nexstreaming.checkcaps.a.InterfaceC0103a
            public void a(com.nexstreaming.checkcaps.a aVar2, int i) {
                nexCheckerListener.this.onCheckerCapsResult(i);
            }
        });
        mChecker.a(UHD_WIDTH, UHD_HEIGHT);
    }
}
