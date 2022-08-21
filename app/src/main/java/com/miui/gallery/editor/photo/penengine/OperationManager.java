package com.miui.gallery.editor.photo.penengine;

import com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorFragment;
import com.miui.gallery.editor.photo.screen.mosaic.IScreenMosaicOperation;
import com.miui.gallery.editor.photo.screen.text.IScreenTextOperation;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class OperationManager {
    public AppCompatActivity mActivity;
    public IScreenDoodleOperation mDoodleOperation;
    public IScreenMosaicOperation mMosaicOperation;
    public IScreenTextOperation mTextOperation;

    public OperationManager(AppCompatActivity appCompatActivity) {
        this.mActivity = appCompatActivity;
    }

    public IScreenMosaicOperation getScreenMosaicOperation() {
        if (this.mMosaicOperation == null) {
            this.mMosaicOperation = (IScreenMosaicOperation) ((ScreenEditorFragment) this.mActivity.getSupportFragmentManager().findFragmentByTag("fragment_tag_editor")).getScreenOperation(IScreenMosaicOperation.class);
        }
        return this.mMosaicOperation;
    }

    public IScreenTextOperation getScreenTextOperation() {
        if (this.mTextOperation == null) {
            this.mTextOperation = (IScreenTextOperation) ((ScreenEditorFragment) this.mActivity.getSupportFragmentManager().findFragmentByTag("fragment_tag_editor")).getScreenOperation(IScreenTextOperation.class);
        }
        return this.mTextOperation;
    }

    public IScreenDoodleOperation getDoodleOperation() {
        if (this.mDoodleOperation == null) {
            this.mDoodleOperation = (IScreenDoodleOperation) ((ScreenEditorFragment) this.mActivity.getSupportFragmentManager().findFragmentByTag("fragment_tag_editor")).getScreenOperation(IScreenDoodleOperation.class);
        }
        return this.mDoodleOperation;
    }
}
