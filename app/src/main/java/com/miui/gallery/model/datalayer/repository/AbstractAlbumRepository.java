package com.miui.gallery.model.datalayer.repository;

import com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.share.IShareAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.trash.ITrashAlbumModel;

/* loaded from: classes2.dex */
public abstract class AbstractAlbumRepository implements ICommonAlbumModel, IAIAlbumModel, ICloudAlbumModel, IHiddenAlbumModel, IOtherAlbumModel, IRubbishAlbumModel, IShareAlbumModel, ITrashAlbumModel {
}
