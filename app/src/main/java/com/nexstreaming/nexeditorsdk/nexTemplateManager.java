package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson_nex.Gson;
import com.nexstreaming.app.common.util.l;
import com.nexstreaming.kminternal.json.TemplateMetaData;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.nexeditorsdk.exception.ExpiredTimeException;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class nexTemplateManager {
    private static final String TAG = "nexTemplateManager";
    private static boolean isApplyTemplate;
    private static nexTemplateManager sSingleton;
    private nexTemplateComposer composer;
    private Context mAppContext;
    private boolean mCancel;
    private Context mResContext;
    private static final ExecutorService sInstallThreadExcutor = Executors.newSingleThreadExecutor();
    private static boolean sAutoAspectSelect = true;
    private static nexAssetPackageManager.Category[] supportCategory = {nexAssetPackageManager.Category.template, nexAssetPackageManager.Category.beattemplate};
    private String errorMsg = "";
    private List<Template> templateEntries = new ArrayList();
    private List<Template> externalView_templateEntries = null;
    private int lastError = 0;
    private boolean mUseClipSpeed = false;
    private boolean mUseVideoTrim = true;
    private int mVideoMemorySize = 0;
    private Object m_templateEntryLock = new Object();

    /* loaded from: classes3.dex */
    public static class Template extends nexAssetPackageManager.c {
        private float[] aspect;
        private String bgmId;
        private String[] ids;
        private int internalSourceCount;
        private int maxAspect;
        private int maxExtendCount;
        private int maxRecommendCount;
        private int maxSourceCount;
        private boolean parsed;
        private int selectAspect;

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Category category() {
            return super.category();
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
        public /* bridge */ /* synthetic */ boolean isDelete() {
            return super.isDelete();
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

        public Template() {
            this.parsed = false;
            this.maxExtendCount = -1;
            this.internalSourceCount = -1;
        }

        private Template(nexAssetPackageManager.Item item) {
            super(item);
            this.parsed = false;
            this.maxExtendCount = -1;
            this.internalSourceCount = -1;
        }

        public static Template promote(nexAssetPackageManager.Item item) {
            for (nexAssetPackageManager.Category category : nexTemplateManager.supportCategory) {
                if (item.category() == category) {
                    return new Template(item);
                }
            }
            return null;
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

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String id() {
            if (nexTemplateManager.sAutoAspectSelect) {
                selectAspect();
            }
            return this.ids[this.selectAspect];
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String name(String str) {
            String assetName = packageInfo().assetName(str);
            return assetName != null ? assetName : super.name(str);
        }

        public float aspect() {
            if (nexTemplateManager.sAutoAspectSelect) {
                selectAspect();
            }
            return this.aspect[this.selectAspect];
        }

        public float[] getSupportedAspects() {
            int i = this.maxAspect;
            float[] fArr = new float[i];
            for (int i2 = 0; i2 < i; i2++) {
                fArr[i2] = this.aspect[i2];
            }
            return fArr;
        }

        public void selectAspect(int i) {
            if (this.maxAspect <= i) {
                return;
            }
            this.selectAspect = i;
        }

        public String getVersion() {
            if (this.id.contains(".ver_")) {
                String str = this.id;
                return str.substring(str.indexOf(".ver_") + 5, this.id.indexOf(".ver_") + 8);
            }
            return "200";
        }

        private void selectAspect() {
            float aspectRatio = nexApplicationConfig.getAspectRatio();
            float f = 3.0f;
            for (int i = 0; i < this.maxAspect; i++) {
                float f2 = aspectRatio - this.aspect[i];
                if (f2 < 0.0f) {
                    f2 *= -1.0f;
                }
                if (f > f2) {
                    this.selectAspect = i;
                    f = f2;
                }
            }
        }

        public void setAspect(String str) {
            if (this.maxAspect >= 5) {
                return;
            }
            if (this.aspect == null) {
                this.aspect = new float[5];
                this.ids = new String[5];
            }
            if (str.contains("9v16")) {
                this.aspect[this.maxAspect] = 0.5625f;
            } else if (str.contains("2v1")) {
                this.aspect[this.maxAspect] = 2.0f;
            } else if (str.contains("1v2")) {
                this.aspect[this.maxAspect] = 0.5f;
            } else if (str.contains("1v1")) {
                this.aspect[this.maxAspect] = 1.0f;
            } else {
                this.aspect[this.maxAspect] = 1.7777778f;
            }
            String[] strArr = this.ids;
            int i = this.maxAspect;
            strArr[i] = str;
            this.maxAspect = i + 1;
            selectAspect();
        }

        public String defaultBGMId() {
            String AssetPackageTemplateJsonToString;
            if (category() == nexAssetPackageManager.Category.beattemplate) {
                parseTemplate();
                return this.bgmId;
            }
            if (this.bgmId == null && (AssetPackageTemplateJsonToString = nexTemplateComposer.AssetPackageTemplateJsonToString(id())) != null) {
                try {
                    this.bgmId = new JSONObject(AssetPackageTemplateJsonToString).getString("template_bgm");
                } catch (JSONException unused) {
                }
            }
            return this.bgmId;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String[] getSupportedLocales() {
            return packageInfo() == null ? new String[0] : packageInfo().getSupportedLocales();
        }
    }

    public List<Template> getTemplates() {
        if (this.externalView_templateEntries == null) {
            this.externalView_templateEntries = Collections.unmodifiableList(this.templateEntries);
        }
        return this.externalView_templateEntries;
    }

    public int[] getTemplateAssetIdxs() {
        int[] iArr;
        synchronized (this.m_templateEntryLock) {
            ArrayList<Integer> arrayList = new ArrayList();
            for (Template template : this.templateEntries) {
                if (arrayList.size() == 0) {
                    arrayList.add(Integer.valueOf(template.packageInfo().assetIdx()));
                } else {
                    for (Integer num : arrayList) {
                        num.intValue();
                        template.packageInfo().assetIdx();
                    }
                    arrayList.add(Integer.valueOf(template.packageInfo().assetIdx()));
                }
            }
            int size = arrayList.size();
            iArr = new int[size];
            for (int i = 0; i < size; i++) {
                iArr[i] = ((Integer) arrayList.get(i)).intValue();
            }
        }
        return iArr;
    }

    public String[] getTemplateAssetIds() {
        String[] strArr;
        synchronized (this.m_templateEntryLock) {
            ArrayList<String> arrayList = new ArrayList();
            for (Template template : this.templateEntries) {
                if (arrayList.size() == 0) {
                    arrayList.add(template.packageInfo().assetId());
                } else {
                    for (String str : arrayList) {
                        str.compareTo(template.packageInfo().assetId());
                    }
                    arrayList.add(template.packageInfo().assetId());
                }
            }
            int size = arrayList.size();
            strArr = new String[size];
            for (int i = 0; i < size; i++) {
                strArr[i] = (String) arrayList.get(i);
            }
        }
        return strArr;
    }

    public Template getTemplateById(String str) {
        synchronized (this.m_templateEntryLock) {
            for (Template template : this.templateEntries) {
                if (template.id().compareTo(str) == 0) {
                    return template;
                }
            }
            return null;
        }
    }

    private nexTemplateManager(Context context, Context context2) {
        this.mAppContext = context;
        this.mResContext = context2;
    }

    public static nexTemplateManager getTemplateManager(Context context, Context context2) {
        nexTemplateManager nextemplatemanager = sSingleton;
        if (nextemplatemanager != null && !nextemplatemanager.mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexTemplateManager(context, context2);
        }
        return sSingleton;
    }

    public static nexTemplateManager getTemplateManager() {
        return sSingleton;
    }

    private Template getAssetTemplate(String str) {
        for (Template template : this.templateEntries) {
            if (template.packageInfo().assetId().compareTo(str) == 0) {
                return template;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resolveTemplate(boolean z) {
        synchronized (this.m_templateEntryLock) {
            this.templateEntries.clear();
            for (nexAssetPackageManager.Category category : supportCategory) {
                for (nexAssetPackageManager.Item item : nexAssetPackageManager.getAssetPackageManager(this.mAppContext).getInstalledAssetItems(category)) {
                    if (!item.hidden()) {
                        if (z) {
                            nexAssetPackageManager.getAssetPackageManager(this.mAppContext);
                            if (nexAssetPackageManager.checkExpireAsset(item.packageInfo())) {
                            }
                        }
                        Template assetTemplate = getAssetTemplate(item.packageInfo().assetId());
                        if (assetTemplate == null) {
                            assetTemplate = Template.promote(item);
                            this.templateEntries.add(assetTemplate);
                        }
                        assetTemplate.setAspect(item.id());
                    }
                }
            }
        }
    }

    public void loadTemplate(final Runnable runnable) {
        new Thread(new Runnable() { // from class: com.nexstreaming.nexeditorsdk.nexTemplateManager.1
            @Override // java.lang.Runnable
            public void run() {
                nexTemplateManager.this.resolveTemplate(false);
                runnable.run();
            }
        }).start();
    }

    public void loadTemplate() {
        resolveTemplate(false);
    }

    public void loadTemplate(boolean z) {
        resolveTemplate(z);
    }

    private String getNameById(String str) {
        synchronized (this.m_templateEntryLock) {
            for (Template template : this.templateEntries) {
                if (template.id() != null && template.id().compareTo(str) == 0) {
                    return template.name(null);
                }
            }
            return null;
        }
    }

    public boolean applyTemplateToProjectById(nexProject nexproject, String str) throws ExpiredTimeException {
        if (isApplyTemplate) {
            Log.d(TAG, "applyTemplateToProjectById errorMsg= already run applyTemplate");
            this.lastError = -3;
            return false;
        }
        isApplyTemplate = true;
        try {
            boolean internalApplyTemplateToProjectById = internalApplyTemplateToProjectById(nexproject, str, true, nexEngine.nexUndetectedFaceCrop.NONE.getValue());
            isApplyTemplate = false;
            return internalApplyTemplateToProjectById;
        } catch (ExpiredTimeException e) {
            isApplyTemplate = false;
            throw e;
        }
    }

    public boolean applyTemplateToProjectById(nexProject nexproject, String str, boolean z) throws ExpiredTimeException {
        if (isApplyTemplate) {
            Log.d(TAG, "applyTemplateToProjectById errorMsg= already run applyTemplate");
            this.lastError = -3;
            return false;
        }
        isApplyTemplate = true;
        try {
            boolean internalApplyTemplateToProjectById = internalApplyTemplateToProjectById(nexproject, str, z, nexEngine.nexUndetectedFaceCrop.NONE.getValue());
            isApplyTemplate = false;
            return internalApplyTemplateToProjectById;
        } catch (ExpiredTimeException e) {
            isApplyTemplate = false;
            throw e;
        }
    }

    public boolean applyTemplateToProjectById(nexProject nexproject, String str, boolean z, int i) throws ExpiredTimeException {
        if (isApplyTemplate) {
            Log.d(TAG, "applyTemplateToProjectById errorMsg= already run applyTemplate");
            this.lastError = -3;
            return false;
        }
        isApplyTemplate = true;
        try {
            boolean internalApplyTemplateToProjectById = internalApplyTemplateToProjectById(nexproject, str, z, i);
            isApplyTemplate = false;
            return internalApplyTemplateToProjectById;
        } catch (ExpiredTimeException e) {
            isApplyTemplate = false;
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean internalApplyTemplateToProjectById(nexProject nexproject, String str, boolean z, final int i) throws ExpiredTimeException {
        l lVar = new l();
        lVar.a();
        this.lastError = 0;
        if (getNameById(str) == null) {
            this.lastError = -1;
            return false;
        }
        Template templateById = getTemplateById(str);
        if (templateById == null) {
            this.lastError = -1;
            return false;
        } else if (nexAssetPackageManager.checkExpireAsset(templateById.packageInfo())) {
            this.lastError = -2;
            throw new ExpiredTimeException(str);
        } else if (templateById.category() == nexAssetPackageManager.Category.beattemplate) {
            return nexBeatTemplateManager.internalApplyTemplateToProjectById(nexproject, str);
        } else {
            if (this.composer == null) {
                this.composer = new nexTemplateComposer();
            }
            EditorGlobal.a().a((Thread) null);
            if (i != 0) {
                final nexClip clip = nexproject.getClip(0, true);
                if (clip.getClipType() == 1) {
                    EditorGlobal.a().e();
                    Thread thread = new Thread(new Runnable() { // from class: com.nexstreaming.nexeditorsdk.nexTemplateManager.2
                        @Override // java.lang.Runnable
                        public void run() {
                            nexTemplateManager.this.getFirstFace(clip, i);
                        }
                    });
                    thread.start();
                    EditorGlobal.a().a(thread);
                }
            }
            this.composer.setUseProjectSpeed(this.mUseClipSpeed);
            if (nexConfig.getProperty(2) == 1) {
                this.composer.setOverlappedTransitionFlag(false);
            } else {
                this.composer.setOverlappedTransitionFlag(z);
            }
            String templateEffects2Project = this.composer.setTemplateEffects2Project(nexproject, this.mAppContext, this.mResContext, str, false);
            this.errorMsg = templateEffects2Project;
            if (templateEffects2Project != null) {
                Log.d(TAG, "internalApplyTemplateToProjectById errorMsg=" + this.errorMsg);
                this.composer.release();
                lVar.b();
                Log.d(TAG, "internalApplyTemplateToProjectById error elapsed=" + lVar.toString());
                this.lastError = -2;
                return false;
            }
            this.composer.release();
            lVar.b();
            Log.d(TAG, "internalApplyTemplateToProjectById elapsed=" + lVar.toString());
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public final class a extends AsyncTask<Void, Integer, Void> {
        public nexProject a;
        public String b;
        public Runnable c;
        public boolean d;
        private boolean f;

        public a(nexProject nexproject, String str, boolean z, Runnable runnable) {
            this.a = nexproject;
            this.b = str;
            this.c = runnable;
            this.d = z;
        }

        @Override // android.os.AsyncTask
        /* renamed from: a */
        public Void doInBackground(Void... voidArr) {
            try {
                this.f = nexTemplateManager.this.internalApplyTemplateToProjectById(this.a, this.b, this.d, nexEngine.nexUndetectedFaceCrop.NONE.getValue());
                return null;
            } catch (ExpiredTimeException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override // android.os.AsyncTask
        /* renamed from: a */
        public void onPostExecute(Void r1) {
            Runnable runnable;
            if (!nexTemplateManager.this.mCancel && (runnable = this.c) != null) {
                runnable.run();
            }
            boolean unused = nexTemplateManager.isApplyTemplate = false;
        }
    }

    public boolean applyTemplateAsyncToProjectById(nexProject nexproject, String str, Runnable runnable) throws ExpiredTimeException {
        if (isApplyTemplate) {
            this.lastError = -3;
            return false;
        }
        Template templateById = getTemplateById(str);
        if (templateById == null) {
            this.lastError = -1;
            return false;
        } else if (nexAssetPackageManager.checkExpireAsset(templateById.packageInfo())) {
            this.lastError = -2;
            throw new ExpiredTimeException(str);
        } else {
            isApplyTemplate = true;
            this.mCancel = false;
            new a(nexproject, str, true, runnable).executeOnExecutor(sInstallThreadExcutor, new Void[0]);
            return true;
        }
    }

    public boolean applyTemplateAsyncToProjectById(nexProject nexproject, String str, boolean z, Runnable runnable) throws ExpiredTimeException {
        if (isApplyTemplate) {
            this.lastError = -3;
            return false;
        }
        Template templateById = getTemplateById(str);
        if (templateById == null) {
            this.lastError = -1;
            return false;
        } else if (nexAssetPackageManager.checkExpireAsset(templateById.packageInfo())) {
            this.lastError = -2;
            throw new ExpiredTimeException(str);
        } else {
            isApplyTemplate = true;
            this.mCancel = false;
            new a(nexproject, str, z, runnable).executeOnExecutor(sInstallThreadExcutor, new Void[0]);
            return true;
        }
    }

    public String getLastErrorMessage() {
        return this.errorMsg;
    }

    public String[] getTemplateIds() {
        int size = this.templateEntries.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = this.templateEntries.get(i).id();
        }
        return strArr;
    }

    public void uninstallPackageById(String str) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).uninstallPackageById(str);
    }

    public int findNewPackages() {
        return nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).findNewPackages();
    }

    public boolean isInstallingPackages() {
        return nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).isInstallingPackages();
    }

    public void installPackagesAsync(nexAssetPackageManager.OnInstallPackageListener onInstallPackageListener) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackagesAsync(onInstallPackageListener);
    }

    public void installPackagesAsync(int i, nexAssetPackageManager.OnInstallPackageListener onInstallPackageListener) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackagesAsync(i, onInstallPackageListener);
    }

    public void cancel() {
        nexTemplateComposer nextemplatecomposer;
        this.mCancel = true;
        if (isApplyTemplate && (nextemplatecomposer = this.composer) != null) {
            nextemplatecomposer.setCancel();
        }
    }

    public int getLastError() {
        return this.lastError;
    }

    public String getTemplateItemId(boolean z, int i) {
        if (z) {
            loadTemplate();
        }
        String str = null;
        if (i < 2) {
            return null;
        }
        synchronized (this.m_templateEntryLock) {
            Iterator<Template> it = this.templateEntries.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Template next = it.next();
                if (next.packageInfo().assetIdx() == i) {
                    str = next.id();
                    break;
                }
            }
        }
        return str;
    }

    @Deprecated
    public void getFirstFace(nexClip nexclip, int i) {
        Log.d(TAG, "getFirstFace In");
        String path = nexclip.getPath();
        if (com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(path) == null) {
            Log.d(TAG, "getFirstFace Proc");
            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(path, new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(path), true, this.mAppContext));
        }
        Log.d(TAG, "getFirstFace Out");
    }

    public void onFirstFaceDone(nexClip nexclip, com.nexstreaming.kminternal.kinemaster.utils.facedetect.a aVar, int i) {
        boolean z;
        int width = nexclip.getCrop().getWidth();
        int height = nexclip.getCrop().getHeight();
        RectF rectF = new RectF();
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        Rect rect3 = new Rect(0, 0, width, height);
        aVar.a(rectF);
        float f = width;
        float f2 = height;
        rect.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
        rect2.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
        if (rect.isEmpty()) {
            rect.set(0, 0, (width * 3) / 4, (height * 3) / 4);
            rect.offset((int) ((width * Math.random()) / 4.0d), (int) ((height * Math.random()) / 4.0d));
            Log.d(TAG, "Face Detection Empty ");
            z = false;
        } else {
            int width2 = (width / 4) - rect.width();
            if (width2 >= 2) {
                int i2 = width2 / 2;
                rect.left -= i2;
                rect.right += i2;
                Log.d(TAG, "Face Detection width addSpace>0 ");
            }
            int height2 = (height / 4) - rect.height();
            if (height2 >= 2) {
                int i3 = height2 / 2;
                rect.top -= i3;
                rect.bottom += i3;
                Log.d(TAG, "Face Detection height addSpace>0 ");
            }
            nexclip.getCrop().growToAspect(rect, nexApplicationConfig.getAspectRatio());
            if (!rect.intersect(0, 0, width, height)) {
                rect.set(0, 0, (width * 2) / 3, (height * 2) / 3);
                Log.d(TAG, "Face Detection insect not ");
                rect.offset((int) ((width * Math.random()) / 3.0d), (int) ((height * Math.random()) / 3.0d));
            }
            rect3.set(0, 0, (width * 3) / 4, (height * 3) / 4);
            rect3.offset((int) ((width * Math.random()) / 4.0d), (int) ((height * Math.random()) / 4.0d));
            z = true;
        }
        if (z) {
            nexclip.getCrop().shrinkToAspect(rect, nexApplicationConfig.getAspectRatio());
            nexclip.getCrop().shrinkToAspect(rect3, nexApplicationConfig.getAspectRatio());
            Log.d(TAG, "Face Detection aync true  ");
            int i4 = rect.right;
            int i5 = rect.left;
            int i6 = (i4 - i5) / 4;
            rect.left = i5 - i6;
            rect.right = i4 + i6;
            int i7 = rect.bottom;
            int i8 = rect.top;
            int i9 = (i7 - i8) / 4;
            rect.top = i8 - i9;
            rect.bottom = i7 + i9;
            nexclip.getCrop().growToAspect(rect, nexApplicationConfig.getAspectRatio());
            nexclip.getCrop().setStartPosition(rect3);
            nexclip.getCrop().setEndPosition(rect);
            nexclip.getCrop().setFacePosition(rect2);
            nexclip.getCrop().getStartPositionRaw(rect3);
            nexclip.getCrop().getEndPositionRaw(rect);
            nexclip.getCrop().getFacePositionRaw(rect2);
            EditorGlobal.a().a(1, 1, rect3, rect, rect2);
        } else {
            nexclip.getCrop().shrinkToAspect(rect, nexApplicationConfig.getAspectRatio());
            if (i == 1) {
                nexclip.getCrop().shrinkToAspect(rect3, nexApplicationConfig.getAspectRatio());
            } else {
                nexclip.getCrop().growToAspect(rect3, nexApplicationConfig.getAspectRatio());
            }
            nexclip.getCrop().setStartPosition(rect);
            nexclip.getCrop().setEndPosition(rect3);
            nexclip.getCrop().setFacePosition(rect2);
            nexclip.getCrop().getStartPositionRaw(rect);
            nexclip.getCrop().getEndPositionRaw(rect3);
            nexclip.getCrop().getFacePositionRaw(rect2);
            Log.d(TAG, "Face Detection aync false  ");
            EditorGlobal.a().a(1, 0, rect3, rect, rect2);
        }
        nexclip.setFaceDetectProcessed(z, rect2);
        Log.d(TAG, "Face Detection aync thread end =" + nexclip.getPath());
    }

    public void setUseClipSpeed(boolean z) {
        this.mUseClipSpeed = z;
    }

    public void setVideoClipTrimMode(boolean z) {
        this.mUseVideoTrim = z;
    }

    public void setVideoMemorySize(int i) {
        this.mVideoMemorySize = i;
    }

    public static void setAutoSelectFromAspect(boolean z) {
        sAutoAspectSelect = z;
    }

    public boolean updateTemplate(boolean z, nexAssetPackageManager.Item item) {
        synchronized (this.m_templateEntryLock) {
            boolean z2 = false;
            if (z) {
                if (item.hidden()) {
                    return false;
                }
                Template assetTemplate = getAssetTemplate(item.packageInfo().assetId());
                if (assetTemplate == null) {
                    assetTemplate = Template.promote(item);
                    this.templateEntries.add(assetTemplate);
                }
                assetTemplate.setAspect(item.id());
            } else {
                for (Template template : this.templateEntries) {
                    if (template.packageInfo().assetIdx() == item.packageInfo().assetIdx()) {
                        this.templateEntries.remove(template);
                    }
                }
                return z2;
            }
            z2 = true;
            return z2;
        }
    }
}
