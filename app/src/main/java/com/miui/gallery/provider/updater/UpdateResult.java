package com.miui.gallery.provider.updater;

/* loaded from: classes2.dex */
public class UpdateResult {
    public static final UpdateResult DEFAULT_RESULT = new Builder().build();
    public boolean isRecreateAlbumTable;
    public boolean isRecreateCloudSettingsTable;
    public boolean isRecreateCloudTable;
    public boolean isRecreateCloudUserTable;
    public boolean isRecreateFavoriteTable;
    public boolean isRecreateOwnerSubUbi;
    public boolean isRecreatePeopleFaceTable;
    public boolean isRecreateShareAlbumTable;
    public boolean isRecreateShareImageTable;
    public boolean isRecreateShareUserTable;
    public boolean isRecreateSharerSubUbi;
    public boolean success;

    public UpdateResult(Builder builder) {
        this.success = builder.success;
        this.isRecreateAlbumTable = builder.isRecreateAlbumTable;
        this.isRecreateCloudTable = builder.isRecreateCloudTable;
        this.isRecreateCloudSettingsTable = builder.isRecreateCloudSettingsTable;
        this.isRecreateShareUserTable = builder.isRecreateShareUserTable;
        this.isRecreateShareAlbumTable = builder.isRecreateShareAlbumTable;
        this.isRecreateShareImageTable = builder.isRecreateShareImageTable;
        this.isRecreateCloudUserTable = builder.isRecreateCloudUserTable;
        this.isRecreateOwnerSubUbi = builder.isRecreateOwnerSubUbi;
        this.isRecreateSharerSubUbi = builder.isRecreateSharerSubUbi;
        this.isRecreatePeopleFaceTable = builder.isRecreatePeopleFaceTable;
        this.isRecreateFavoriteTable = builder.isRecreateFavoriteTable;
    }

    public static UpdateResult defaultResult() {
        return DEFAULT_RESULT;
    }

    public void merge(UpdateResult updateResult) {
        if (updateResult == null) {
            return;
        }
        this.success &= updateResult.success;
        this.isRecreateAlbumTable |= updateResult.isRecreateAlbumTable;
        this.isRecreateCloudTable |= updateResult.isRecreateCloudTable;
        this.isRecreateCloudSettingsTable |= updateResult.isRecreateCloudSettingsTable;
        this.isRecreateShareUserTable |= updateResult.isRecreateShareUserTable;
        this.isRecreateShareAlbumTable |= updateResult.isRecreateShareAlbumTable;
        this.isRecreateShareImageTable |= updateResult.isRecreateShareImageTable;
        this.isRecreateCloudUserTable |= updateResult.isRecreateCloudUserTable;
        this.isRecreateOwnerSubUbi |= updateResult.isRecreateOwnerSubUbi;
        this.isRecreateSharerSubUbi |= updateResult.isRecreateSharerSubUbi;
        this.isRecreatePeopleFaceTable |= updateResult.isRecreatePeopleFaceTable;
        this.isRecreateFavoriteTable = updateResult.isRecreateFavoriteTable | this.isRecreateFavoriteTable;
    }

    public boolean isRecreateTableAlbum() {
        return this.isRecreateAlbumTable;
    }

    public boolean isRecreateTableCloud() {
        return this.isRecreateCloudTable;
    }

    public boolean isRecreateTableCloudSettings() {
        return this.isRecreateCloudSettingsTable;
    }

    public boolean isRecreateTableShareUser() {
        return this.isRecreateShareUserTable;
    }

    public boolean isRecreateTableShareAlbum() {
        return this.isRecreateShareAlbumTable;
    }

    public boolean isRecreateTableShareImage() {
        return this.isRecreateShareImageTable;
    }

    public boolean isRecreateTableCloudUser() {
        return this.isRecreateCloudUserTable;
    }

    public boolean isRecreateTableOwnerSubUbi() {
        return this.isRecreateOwnerSubUbi;
    }

    public boolean isRecreateTableSharerSubUbi() {
        return this.isRecreateSharerSubUbi;
    }

    public boolean isRecreateTablePeopleFace() {
        return this.isRecreatePeopleFaceTable;
    }

    public boolean isRecreateTableFavorite() {
        return this.isRecreateFavoriteTable;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean success = true;
        public boolean isRecreateAlbumTable = false;
        public boolean isRecreateCloudTable = false;
        public boolean isRecreateCloudSettingsTable = false;
        public boolean isRecreateShareUserTable = false;
        public boolean isRecreateShareAlbumTable = false;
        public boolean isRecreateShareImageTable = false;
        public boolean isRecreateCloudUserTable = false;
        public boolean isRecreateOwnerSubUbi = false;
        public boolean isRecreateSharerSubUbi = false;
        public boolean isRecreatePeopleFaceTable = false;
        public boolean isRecreateFavoriteTable = false;
        public boolean upgradeViewsAndTriggers = false;

        public Builder success(boolean z) {
            this.success = z;
            return this;
        }

        public Builder recreateTableAlbum() {
            this.isRecreateAlbumTable = true;
            return this;
        }

        public Builder recreateTableCloud() {
            this.isRecreateCloudTable = true;
            return this;
        }

        public Builder recreateTableCloudSettings() {
            this.isRecreateCloudSettingsTable = true;
            return this;
        }

        public Builder recreateTableShareUser() {
            this.isRecreateShareUserTable = true;
            return this;
        }

        public Builder recreateTableShareAlbum() {
            this.isRecreateShareAlbumTable = true;
            return this;
        }

        public Builder recreateTableShareImage() {
            this.isRecreateShareImageTable = true;
            return this;
        }

        public Builder recreateTableCloudUser() {
            this.isRecreateCloudUserTable = true;
            return this;
        }

        public Builder recreateTableOwnerSubUbi() {
            this.isRecreateOwnerSubUbi = true;
            return this;
        }

        public Builder recreateTableSharerSubUbi() {
            this.isRecreateSharerSubUbi = true;
            return this;
        }

        public Builder recreateTablePeopleFace() {
            this.isRecreatePeopleFaceTable = true;
            return this;
        }

        public Builder recreateTableFavorite() {
            this.isRecreateFavoriteTable = true;
            return this;
        }

        public UpdateResult build() {
            return new UpdateResult(this);
        }
    }
}
