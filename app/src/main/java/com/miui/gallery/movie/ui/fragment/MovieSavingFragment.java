package com.miui.gallery.movie.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.R$style;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.ui.fragment.MovieSavingFragment;
import com.miui.gallery.movie.ui.listener.IShareDataCallback;
import com.miui.gallery.movie.utils.MovieStorage;
import com.miui.gallery.movie.utils.stat.MovieStatUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class MovieSavingFragment extends GalleryDialogFragment {
    public long mLastBackPressedTime;
    public OnSavingFinishListener mOnSavingFinishListener;
    public ProgressDialog mProgressDialog;
    public IShareDataCallback mShareCallback;

    /* loaded from: classes2.dex */
    public interface OnSavingFinishListener {
        void onFinish(boolean z, boolean z2, String str);
    }

    public static /* synthetic */ void $r8$lambda$Lct2i4D_PbdOG0Irim3lZfXrUrI(MovieSavingFragment movieSavingFragment, Context context, boolean z, boolean z2, String str) {
        movieSavingFragment.lambda$showAndShare$2(context, z, z2, str);
    }

    public static /* synthetic */ void $r8$lambda$QzuNdr5XJykfBBU3fB7nKeLn6DE(MovieSavingFragment movieSavingFragment, Context context, boolean z, boolean z2, String str) {
        movieSavingFragment.lambda$show$1(context, z, z2, str);
    }

    public static /* synthetic */ boolean $r8$lambda$eWr5UUvcoFz45OfWNz0y0qOFzbo(MovieSavingFragment movieSavingFragment, DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return movieSavingFragment.lambda$onCreateDialog$0(dialogInterface, i, keyEvent);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), R$style.MovieTheme_ExportDialog);
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R$string.movie_saving));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieSavingFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return MovieSavingFragment.$r8$lambda$eWr5UUvcoFz45OfWNz0y0qOFzbo(MovieSavingFragment.this, dialogInterface, i, keyEvent);
            }
        });
        setCancelable(false);
        return this.mProgressDialog;
    }

    public /* synthetic */ boolean lambda$onCreateDialog$0(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getAction() == 0) {
            return backPress();
        }
        return false;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IShareDataCallback) {
            this.mShareCallback = (IShareDataCallback) context;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mShareCallback = null;
        this.mOnSavingFinishListener = null;
    }

    public void setProgress(int i) {
        this.mProgressDialog.setProgress(i);
    }

    public final boolean backPress() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.makeText(getActivity(), getString(R$string.movie_save_stop_tips), 0);
            return true;
        }
        this.mLastBackPressedTime = 0L;
        dismissSafely();
        IShareDataCallback iShareDataCallback = this.mShareCallback;
        if (iShareDataCallback == null) {
            return true;
        }
        iShareDataCallback.cancelExport();
        return true;
    }

    public void show(FragmentActivity fragmentActivity, MovieManager movieManager, MovieInfo movieInfo, boolean z, OnSavingFinishListener onSavingFinishListener) {
        MovieStatUtils.statSaveClick(z, movieInfo);
        final Context applicationContext = fragmentActivity.getApplicationContext();
        this.mOnSavingFinishListener = onSavingFinishListener;
        doSaving(applicationContext, fragmentActivity.getSupportFragmentManager(), movieManager, new OnSavingFinishListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieSavingFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.movie.ui.fragment.MovieSavingFragment.OnSavingFinishListener
            public final void onFinish(boolean z2, boolean z3, String str) {
                MovieSavingFragment.$r8$lambda$QzuNdr5XJykfBBU3fB7nKeLn6DE(MovieSavingFragment.this, applicationContext, z2, z3, str);
            }
        });
    }

    public /* synthetic */ void lambda$show$1(Context context, boolean z, boolean z2, String str) {
        if (z) {
            OnSavingFinishListener onSavingFinishListener = this.mOnSavingFinishListener;
            if (onSavingFinishListener != null) {
                onSavingFinishListener.onFinish(z, z2, str);
            }
            ToastUtils.makeText(context, R$string.movie_save_successfully, 0);
        } else if (!z2) {
            ToastUtils.makeText(context, R$string.movie_save_failed, 0);
        }
        MovieStatUtils.statSaveResult(z ? "0" : "-1");
    }

    public void showAndShare(FragmentActivity fragmentActivity, MovieManager movieManager, MovieInfo movieInfo) {
        MovieStatUtils.statShareClick(movieInfo);
        final Context applicationContext = fragmentActivity.getApplicationContext();
        doSaving(applicationContext, fragmentActivity.getSupportFragmentManager(), movieManager, new OnSavingFinishListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieSavingFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.movie.ui.fragment.MovieSavingFragment.OnSavingFinishListener
            public final void onFinish(boolean z, boolean z2, String str) {
                MovieSavingFragment.$r8$lambda$Lct2i4D_PbdOG0Irim3lZfXrUrI(MovieSavingFragment.this, applicationContext, z, z2, str);
            }
        });
    }

    public /* synthetic */ void lambda$showAndShare$2(Context context, boolean z, boolean z2, String str) {
        if (!z) {
            if (z2) {
                return;
            }
            ToastUtils.makeText(context, R$string.movie_save_failed, 0);
            return;
        }
        ToastUtils.makeText(context, R$string.movie_save_successfully, 0);
        IShareDataCallback iShareDataCallback = this.mShareCallback;
        if (iShareDataCallback == null) {
            return;
        }
        iShareDataCallback.handleShareEvent(str);
    }

    public final void doSaving(Context context, FragmentManager fragmentManager, MovieManager movieManager, OnSavingFinishListener onSavingFinishListener) {
        if (!movieManager.isReadyForExport()) {
            DefaultLogger.w("MovieSavingFragment", "is not readyForExport");
            return;
        }
        String tempFilePath = MovieStorage.getTempFilePath();
        movieManager.export(tempFilePath, new AnonymousClass1(fragmentManager, tempFilePath, context, onSavingFinishListener));
    }

    /* renamed from: com.miui.gallery.movie.ui.fragment.MovieSavingFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements MovieManager.EncodeStateInterface {
        public final /* synthetic */ Context val$context;
        public final /* synthetic */ FragmentManager val$fragmentManager;
        public final /* synthetic */ OnSavingFinishListener val$onSavingFinishListener;
        public final /* synthetic */ String val$tempFilePath;

        /* renamed from: $r8$lambda$GfQg3XLn49TF-UtwnwedkqDHq9U */
        public static /* synthetic */ String m1160$r8$lambda$GfQg3XLn49TFUtwnwedkqDHq9U(String str, String str2, Context context, ThreadPool.JobContext jobContext) {
            return lambda$onEncodeEnd$0(str, str2, context, jobContext);
        }

        public AnonymousClass1(FragmentManager fragmentManager, String str, Context context, OnSavingFinishListener onSavingFinishListener) {
            MovieSavingFragment.this = r1;
            this.val$fragmentManager = fragmentManager;
            this.val$tempFilePath = str;
            this.val$context = context;
            this.val$onSavingFinishListener = onSavingFinishListener;
        }

        @Override // com.miui.gallery.movie.core.MovieManager.EncodeStateInterface
        public void onEncodeStart() {
            DefaultLogger.d("MovieSavingFragment", "save start");
            if (!MovieSavingFragment.this.isAdded()) {
                MovieSavingFragment.this.showAllowingStateLoss(this.val$fragmentManager, "MovieSavingFragment");
            }
        }

        @Override // com.miui.gallery.movie.core.MovieManager.EncodeStateInterface
        public void onEncodeProgress(int i) {
            MovieSavingFragment.this.setProgress(i);
        }

        @Override // com.miui.gallery.movie.core.MovieManager.EncodeStateInterface
        public void onEncodeEnd(final boolean z, final boolean z2, int i) {
            DefaultLogger.d("MovieSavingFragment", "save result: %b, code: %d, path: %s", Boolean.valueOf(z), Integer.valueOf(i), this.val$tempFilePath);
            if (z) {
                final String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("MovieSavingFragment", "doSaving");
                ThreadPool miscPool = ThreadManager.getMiscPool();
                final String str = this.val$tempFilePath;
                final Context context = this.val$context;
                miscPool.submit(new ThreadPool.Job() { // from class: com.miui.gallery.movie.ui.fragment.MovieSavingFragment$1$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public final Object mo1807run(ThreadPool.JobContext jobContext) {
                        return MovieSavingFragment.AnonymousClass1.m1160$r8$lambda$GfQg3XLn49TFUtwnwedkqDHq9U(str, appendInvokerTag, context, jobContext);
                    }
                }, new FutureHandler<String>() { // from class: com.miui.gallery.movie.ui.fragment.MovieSavingFragment.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // com.miui.gallery.concurrent.FutureHandler
                    public void onPostExecute(Future<String> future) {
                        MovieSavingFragment.this.dismissSafely();
                        OnSavingFinishListener onSavingFinishListener = AnonymousClass1.this.val$onSavingFinishListener;
                        if (onSavingFinishListener != null) {
                            onSavingFinishListener.onFinish(z, z2, future.get());
                        }
                    }
                });
                return;
            }
            MovieSavingFragment.this.dismissSafely();
            OnSavingFinishListener onSavingFinishListener = this.val$onSavingFinishListener;
            if (onSavingFinishListener == null) {
                return;
            }
            onSavingFinishListener.onFinish(z, z2, null);
        }

        public static /* synthetic */ String lambda$onEncodeEnd$0(String str, String str2, Context context, ThreadPool.JobContext jobContext) {
            String outputMediaFilePath = MovieStorage.getOutputMediaFilePath();
            StorageSolutionProvider.get().moveFile(str, outputMediaFilePath, str2);
            MovieDependsModule movieDependsModule = (MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class);
            if (movieDependsModule != null) {
                movieDependsModule.scanSingleFile(context, outputMediaFilePath);
            }
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(outputMediaFilePath, IStoragePermissionStrategy.Permission.QUERY, str2);
            if (documentFile != null) {
                StorageSolutionProvider.get().apply(documentFile);
            }
            return outputMediaFilePath;
        }
    }
}
