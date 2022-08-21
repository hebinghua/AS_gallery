package miuix.appcompat.app.floatingactivity;

import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public interface OnFloatingCallback extends OnFloatingActivityCallback {
    int getPageCount();

    void getSnapShotAndSetPanel(AppCompatActivity appCompatActivity);

    void onDragEnd();

    void onDragStart();

    void onHideBehindPage();
}
