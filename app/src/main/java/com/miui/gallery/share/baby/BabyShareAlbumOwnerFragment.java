package com.miui.gallery.share.baby;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.share.AlbumShareOperations;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.CloudUserCacheEntry;
import com.miui.gallery.share.ShareAlbumOwnerBaseFragment;
import com.miui.gallery.share.ShareUserAdapterBase;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class BabyShareAlbumOwnerFragment extends ShareAlbumOwnerBaseFragment {
    public static final RelationEntry[] sDefaultRelationEntries = {new RelationEntry("father", R.string.relation_text_father), new RelationEntry("mother", R.string.relation_text_mother), new RelationEntry("grandfather", R.string.relation_text_grandfather), new RelationEntry("grandmother", R.string.relation_text_grandmother), new RelationEntry("maternalGrandfather", R.string.relation_text_maternal_grandfather), new RelationEntry("maternalGrandmother", R.string.relation_text_maternal_grandmother)};

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public int getPreferencesResourceId() {
        return R.xml.baby_share_album_preference;
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment, com.miui.gallery.share.ShareAlbumBaseFragment
    public List<CloudUserCacheEntry> getShareUsers() {
        RelationEntry[] relationEntryArr;
        boolean z;
        List<CloudUserCacheEntry> shareUsers = super.getShareUsers();
        shareUsers.add(getOwnerEntry(GalleryCloudUtils.getAccountName()));
        for (RelationEntry relationEntry : sDefaultRelationEntries) {
            Iterator<CloudUserCacheEntry> it = shareUsers.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (TextUtils.equals(relationEntry.relation, it.next().mRelation)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                shareUsers.add(new CloudUserCacheEntry(this.mAlbumId, null, 0L, relationEntry.relation, getString(relationEntry.relationTextId), null, null));
            }
        }
        shareUsers.add(new CloudUserCacheEntry(this.mAlbumId, null, 0L, "family", getString(R.string.family_member_invitation_text), null, null));
        return shareUsers;
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public ShareUserAdapterBase createShareUserAdapter() {
        return new BabyAlbumShareUserAdapter(getContext(), GalleryCloudUtils.getAccountName());
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment
    public void requestInvitation(CloudUserCacheEntry cloudUserCacheEntry, AlbumShareUIManager.OnCompletionListener<Void, AlbumShareOperations.OutgoingInvitation> onCompletionListener) {
        if (cloudUserCacheEntry != null) {
            String str = cloudUserCacheEntry.mRelationText;
            if (TextUtils.isEmpty(str) && TextUtils.equals(cloudUserCacheEntry.mRelation, "family")) {
                str = getString(R.string.relation_text_family);
            }
            String str2 = str;
            CloudUserCacheEntry ownerEntry = getOwnerEntry(GalleryCloudUtils.getAccountName());
            requestInvitation(cloudUserCacheEntry.mRelation, str2, ownerEntry.mRelation, ownerEntry.mRelationText, onCompletionListener);
        }
    }

    /* loaded from: classes2.dex */
    public static class RelationEntry {
        public String relation;
        public int relationTextId;

        public RelationEntry(String str, int i) {
            this.relation = str;
            this.relationTextId = i;
        }
    }
}
