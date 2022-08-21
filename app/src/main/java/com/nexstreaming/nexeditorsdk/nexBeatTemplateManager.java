package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.google.gson_nex.Gson;
import com.nexstreaming.kminternal.json.TemplateMetaData;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class nexBeatTemplateManager {
    private static final String TAG = "nexMusicTempMan";
    private static Context mAppContext;
    private static nexBeatTemplateManager sSingleton;
    private List<BeatTemplate> musicTemplates = new ArrayList();
    private List<BeatTemplate> externalView_musicTemplates = null;
    private Object m_musicTemplateLock = new Object();

    /* loaded from: classes3.dex */
    public enum Level {
        Recommend,
        Extends,
        Max
    }

    /* loaded from: classes3.dex */
    public static class BeatTemplate extends nexAssetPackageManager.c {
        private String bgmId;
        private int internalSourceCount;
        private int maxExtendCount;
        private int maxRecommendCount;
        private int maxSourceCount;
        private boolean parsed;

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Category category() {
            return super.category();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ String[] getSupportedLocales() {
            return super.getSupportedLocales();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean hidden() {
            return super.hidden();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ Bitmap icon() {
            return super.icon();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ String id() {
            return super.id();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean isDelete() {
            return super.isDelete();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ String name(String str) {
            return super.name(str);
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Asset packageInfo() {
            return super.packageInfo();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ Bitmap thumbnail() {
            return super.thumbnail();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.ItemMethodType type() {
            return super.type();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean validate() {
            return super.validate();
        }

        public BeatTemplate(nexAssetPackageManager.Item item) {
            super(item);
            this.parsed = false;
            this.maxExtendCount = -1;
            this.internalSourceCount = -1;
        }

        public static BeatTemplate promote(nexAssetPackageManager.c cVar) {
            if (cVar.category() != nexAssetPackageManager.Category.beattemplate) {
                return null;
            }
            return new BeatTemplate(cVar);
        }

        private void parseTemplate() {
            TemplateMetaData.Music music;
            if (!this.parsed) {
                int i = 1;
                this.parsed = true;
                String AssetPackageTemplateJsonToString = nexTemplateComposer.AssetPackageTemplateJsonToString(id());
                if (AssetPackageTemplateJsonToString == null || (music = (TemplateMetaData.Music) new Gson().fromJson(AssetPackageTemplateJsonToString, (Class<Object>) TemplateMetaData.Music.class)) == null) {
                    return;
                }
                this.bgmId = music.string_audio_id;
                int i2 = -1;
                Iterator<TemplateMetaData.EffectEntry> it = music.list_effectEntries.iterator();
                int i3 = 0;
                int i4 = 0;
                while (it.hasNext()) {
                    TemplateMetaData.EffectEntry next = it.next();
                    i2++;
                    if (next.b_source_change || next.int_priority > 0) {
                        if (next.int_priority > 0) {
                            i3++;
                        } else if (next.internal_clip_id != null) {
                            i4++;
                        } else {
                            i++;
                        }
                    }
                }
                this.maxSourceCount = i2;
                this.maxRecommendCount = i;
                this.maxExtendCount = i3;
                this.internalSourceCount = i4;
            }
        }

        public String getBGMId() {
            parseTemplate();
            return this.bgmId;
        }

        public int getInternalSourceCount() {
            if (this.internalSourceCount == -1) {
                parseTemplate();
            }
            return this.internalSourceCount;
        }

        public int getMaxExtendCount() {
            if (this.maxExtendCount < 0) {
                parseTemplate();
            }
            return this.maxExtendCount;
        }

        public int getMaxRecommendCount() {
            if (this.maxRecommendCount == 0) {
                parseTemplate();
            }
            return this.maxRecommendCount;
        }

        public int getMaxSourceCount() {
            if (this.maxSourceCount == 0) {
                if (id().contains(".sc.")) {
                    String id = id();
                    String substring = id.substring(id.indexOf(".sc.") + 4, id.length());
                    if (substring != null && substring.length() > 0) {
                        int parseInt = Integer.parseInt(substring);
                        this.maxSourceCount = parseInt;
                        return parseInt;
                    }
                } else {
                    parseTemplate();
                }
            }
            return this.maxSourceCount;
        }
    }

    private nexBeatTemplateManager(Context context) {
        mAppContext = context;
    }

    public static nexBeatTemplateManager getBeatTemplateManager(Context context) {
        if (sSingleton != null && !mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexBeatTemplateManager(context);
        }
        return sSingleton;
    }

    private void resolve(boolean z) {
        synchronized (this.m_musicTemplateLock) {
            this.musicTemplates.clear();
            for (nexAssetPackageManager.Item item : nexAssetPackageManager.getAssetPackageManager(mAppContext).getInstalledAssetItems(nexAssetPackageManager.Category.beattemplate)) {
                if (!item.hidden()) {
                    if (z) {
                        nexAssetPackageManager.getAssetPackageManager(mAppContext);
                        if (nexAssetPackageManager.checkExpireAsset(item.packageInfo())) {
                        }
                    }
                    this.musicTemplates.add(new BeatTemplate(item));
                }
            }
        }
    }

    public void loadTemplate() {
        resolve(false);
    }

    public void loadTemplate(boolean z) {
        resolve(z);
    }

    public BeatTemplate getBeatTemplate(String str) {
        synchronized (this.m_musicTemplateLock) {
            for (BeatTemplate beatTemplate : this.musicTemplates) {
                if (beatTemplate.id().compareTo(str) == 0) {
                    return beatTemplate;
                }
            }
            return null;
        }
    }

    public List<BeatTemplate> getBeatTemplates() {
        List<BeatTemplate> list;
        synchronized (this.m_musicTemplateLock) {
            if (this.externalView_musicTemplates == null) {
                this.externalView_musicTemplates = Collections.unmodifiableList(this.musicTemplates);
            }
            list = this.externalView_musicTemplates;
        }
        return list;
    }

    /* loaded from: classes3.dex */
    public static class a {
        public int a;
        public int b;
        public String c;
        public String d;
        public boolean e;
        public String f;
        public int g;
        public int h;

        private a() {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:147:0x0497, code lost:
        if (r7 >= r4.length) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0499, code lost:
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x049b, code lost:
        r8 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x04b3, code lost:
        if (r7 >= r4.length) goto L173;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean internalApplyTemplateToProjectById(com.nexstreaming.nexeditorsdk.nexProject r19, java.lang.String r20) {
        /*
            Method dump skipped, instructions count: 1392
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexBeatTemplateManager.internalApplyTemplateToProjectById(com.nexstreaming.nexeditorsdk.nexProject, java.lang.String):boolean");
    }

    public boolean applyTemplateToProjectById(nexProject nexproject, String str) {
        BeatTemplate beatTemplate = getBeatTemplate(str);
        if (beatTemplate != null && !nexAssetPackageManager.checkExpireAsset(beatTemplate.packageInfo())) {
            return internalApplyTemplateToProjectById(nexproject, str);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:131:0x03d9, code lost:
        if (r0 >= r4.length) goto L160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x03db, code lost:
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x03dd, code lost:
        r10 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x03f5, code lost:
        if (r0 >= r4.length) goto L160;
     */
    /* JADX WARN: Removed duplicated region for block: B:195:0x022a A[EDGE_INSN: B:195:0x022a->B:90:0x022a ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x027a A[LOOP:2: B:91:0x0274->B:93:0x027a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean applyTemplateToProjectById2(com.nexstreaming.nexeditorsdk.nexProject r21, java.lang.String r22) {
        /*
            Method dump skipped, instructions count: 1186
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexBeatTemplateManager.applyTemplateToProjectById2(com.nexstreaming.nexeditorsdk.nexProject, java.lang.String):boolean");
    }

    public boolean updateBeatTemplate(boolean z, nexAssetPackageManager.Item item) {
        synchronized (this.m_musicTemplateLock) {
            Log.d(TAG, "updateMusicTemplate(" + z + "," + item.packageInfo().assetIdx() + ")");
            if (z) {
                if (!item.hidden()) {
                    this.musicTemplates.add(new BeatTemplate(item));
                }
            } else {
                Iterator<BeatTemplate> it = this.musicTemplates.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BeatTemplate next = it.next();
                    if (next.id().compareTo(item.id()) == 0) {
                        this.musicTemplates.remove(next);
                        break;
                    }
                }
            }
        }
        return false;
    }
}
