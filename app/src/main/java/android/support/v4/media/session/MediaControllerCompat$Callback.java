package android.support.v4.media.session;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaControllerCompatApi21;
import android.support.v4.media.session.MediaSessionCompat;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes.dex */
public abstract class MediaControllerCompat$Callback implements IBinder.DeathRecipient {
    public final Object mCallbackObj;
    public IMediaControllerCallback mIControllerCallback;

    public void onAudioInfoChanged(MediaControllerCompat$PlaybackInfo mediaControllerCompat$PlaybackInfo) {
    }

    public void onExtrasChanged(Bundle bundle) {
    }

    public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
    }

    public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
    }

    public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) {
    }

    public void onQueueTitleChanged(CharSequence charSequence) {
    }

    public void onSessionDestroyed() {
    }

    public void onSessionEvent(String str, Bundle bundle) {
    }

    public void postToHandler(int i, Object obj, Bundle bundle) {
    }

    public MediaControllerCompat$Callback() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mCallbackObj = MediaControllerCompatApi21.createCallback(new StubApi21(this));
            return;
        }
        StubCompat stubCompat = new StubCompat(this);
        this.mIControllerCallback = stubCompat;
        this.mCallbackObj = stubCompat;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        postToHandler(8, null, null);
    }

    /* loaded from: classes.dex */
    public static class StubApi21 implements MediaControllerCompatApi21.Callback {
        public final WeakReference<MediaControllerCompat$Callback> mCallback;

        public StubApi21(MediaControllerCompat$Callback mediaControllerCompat$Callback) {
            this.mCallback = new WeakReference<>(mediaControllerCompat$Callback);
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onSessionDestroyed() {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onSessionDestroyed();
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onSessionEvent(String str, Bundle bundle) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                if (mediaControllerCompat$Callback.mIControllerCallback != null && Build.VERSION.SDK_INT < 23) {
                    return;
                }
                mediaControllerCompat$Callback.onSessionEvent(str, bundle);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onPlaybackStateChanged(Object obj) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback == null || mediaControllerCompat$Callback.mIControllerCallback != null) {
                return;
            }
            mediaControllerCompat$Callback.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(obj));
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onMetadataChanged(Object obj) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(obj));
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onQueueChanged(List<?> list) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(list));
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onQueueTitleChanged(CharSequence charSequence) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onQueueTitleChanged(charSequence);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onExtrasChanged(Bundle bundle) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onExtrasChanged(bundle);
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompatApi21.Callback
        public void onAudioInfoChanged(int i, int i2, int i3, int i4, int i5) {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.onAudioInfoChanged(new MediaControllerCompat$PlaybackInfo(i, i2, i3, i4, i5));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class StubCompat extends IMediaControllerCallback.Stub {
        public final WeakReference<MediaControllerCompat$Callback> mCallback;

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onShuffleModeChangedRemoved(boolean z) throws RemoteException {
        }

        public StubCompat(MediaControllerCompat$Callback mediaControllerCompat$Callback) {
            this.mCallback = new WeakReference<>(mediaControllerCompat$Callback);
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onEvent(String str, Bundle bundle) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(1, str, bundle);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onSessionDestroyed() throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(8, null, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(2, playbackStateCompat, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(3, mediaMetadataCompat, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(5, list, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(6, charSequence, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onCaptioningEnabledChanged(boolean z) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(11, Boolean.valueOf(z), null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onRepeatModeChanged(int i) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(9, Integer.valueOf(i), null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onShuffleModeChanged(int i) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(12, Integer.valueOf(i), null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onExtrasChanged(Bundle bundle) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(7, bundle, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(4, parcelableVolumeInfo != null ? new MediaControllerCompat$PlaybackInfo(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume) : null, null);
            }
        }

        @Override // android.support.v4.media.session.IMediaControllerCallback
        public void onSessionReady() throws RemoteException {
            MediaControllerCompat$Callback mediaControllerCompat$Callback = this.mCallback.get();
            if (mediaControllerCompat$Callback != null) {
                mediaControllerCompat$Callback.postToHandler(13, null, null);
            }
        }
    }
}
