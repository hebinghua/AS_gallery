package com.miui.gallery.magic.idphoto.search;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$integer;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseActivity;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import com.miui.gallery.magic.idphoto.menu.CertificatesMenuModel;
import com.miui.gallery.magic.util.DialogUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.magic.widget.EmptyRecyclerView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CertificatesSearchActivity extends BaseActivity implements View.OnClickListener {
    public View.OnClickListener clickListener = new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchActivity$$ExternalSyntheticLambda0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            CertificatesSearchActivity.$r8$lambda$Mbevyd43t8XDchfZHwiCkFy12T8(CertificatesSearchActivity.this, view);
        }
    };
    public ImageView ivTittleSearchBack;
    public SearchAdapter mAdapter;
    public BaseBroadcastReceiver mBroadcastReceiver;
    public Configuration mConfiguration;
    public EditText mEditSearch;
    public ArrayList<CategoryItem> mList;
    public EmptyRecyclerView mListView;
    public ArrayList<CategoryItem> mSearchList;
    public View mViewSearchLayer;
    public RelativeLayout rlInputBoxSearch;

    public static /* synthetic */ void $r8$lambda$Mbevyd43t8XDchfZHwiCkFy12T8(CertificatesSearchActivity certificatesSearchActivity, View view) {
        certificatesSearchActivity.lambda$new$0(view);
    }

    @Override // com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.ts_magic_id_photo_search);
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        setOrientation();
        initView();
        initData();
        initBroadcastReceiver();
        getWindow().getDecorView().setSystemUiVisibility(6);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
    }

    public final void initView() {
        this.mListView = (EmptyRecyclerView) findViewById(R$id.magic_idp_search_list);
        this.mEditSearch = (EditText) findViewById(R$id.magic_idp_search_edit);
        this.rlInputBoxSearch = (RelativeLayout) findViewById(R$id.rl_input_box_search);
        this.ivTittleSearchBack = (ImageView) findViewById(R$id.iv_tittle_search_back);
        if (BaseMiscUtil.isRTLDirection()) {
            this.ivTittleSearchBack.setRotation(180.0f);
        }
        View findViewById = findViewById(R$id.view_search_layer);
        this.mViewSearchLayer = findViewById;
        findViewById.setOnClickListener(this.clickListener);
        fitListView();
    }

    public /* synthetic */ void lambda$new$0(View view) {
        openSearchLook();
    }

    public final void openSearchLook() {
        startActivityForResult(new Intent(this, CertificatesSearchLookActivity.class), 1221, ActivityOptions.makeSceneTransitionAnimation(this, this.rlInputBoxSearch, getString(R$string.image_transition_name)).toBundle());
    }

    public final void initData() {
        CertificatesMenuModel certificatesMenuModel = new CertificatesMenuModel(null);
        certificatesMenuModel.getContract().getTabsData();
        ArrayList<CategoryItem> listData = certificatesMenuModel.getContract().getListData();
        this.mList = listData;
        listData.remove(0);
        SearchAdapter searchAdapter = new SearchAdapter(this.mList);
        this.mAdapter = searchAdapter;
        this.mListView.setAdapter(searchAdapter);
        this.mListView.setEmptyView(findViewById(R$id.magic_recycler_empty));
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchActivity.1
            {
                CertificatesSearchActivity.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                Intent intent = new Intent();
                intent.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, CertificatesSearchActivity.this.mAdapter.getItem(i).getIndex());
                CertificatesSearchActivity.this.setResult(201, intent);
                CertificatesSearchActivity.this.finish();
                return false;
            }
        });
        this.mListView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchActivity.2
            {
                CertificatesSearchActivity.this = this;
            }

            @Override // android.view.View.OnScrollChangeListener
            public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                DialogUtil.hintKbTwo((miuix.androidbasewidget.widget.EditText) CertificatesSearchActivity.this.mEditSearch, CertificatesSearchActivity.this.getApplication());
            }
        });
        this.mEditSearch.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchActivity.3
            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            {
                CertificatesSearchActivity.this = this;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("mEditSearch", "beforeTextChanged:==>" + ((Object) charSequence));
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                String obj = editable.toString();
                if (TextUtils.isEmpty(obj)) {
                    CertificatesSearchActivity.this.searchEdit(obj);
                }
            }
        });
    }

    public final void searchEdit(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mAdapter.newData(this.mList);
            return;
        }
        if (this.mSearchList == null) {
            this.mSearchList = new ArrayList<>();
        }
        this.mSearchList.clear();
        for (int size = this.mList.size() - 1; size >= 0; size--) {
            CategoryItem categoryItem = this.mList.get(size);
            if (categoryItem.getTitle().contains(str)) {
                this.mSearchList.add(categoryItem);
            }
        }
        this.mAdapter.newData(this.mSearchList);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fitListView();
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            setOrientation();
            resetView();
        }
    }

    public final void fitListView() {
        if (OrientationCheckHelper.isSupportOrientationChange()) {
            this.mListView.setLayoutManager(new StaggeredGridLayoutManager(ResourceUtil.getInt(R$integer.ceriticates_grid_span_count), 1));
            return;
        }
        this.mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public final void resetView() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.rlInputBoxSearch.getLayoutParams());
        layoutParams.width = getResources().getDimensionPixelSize(R$dimen.magic_search_list_width);
        this.rlInputBoxSearch.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mViewSearchLayer.getLayoutParams();
        layoutParams2.width = getResources().getDimensionPixelSize(R$dimen.magic_search_edit_width);
        this.mViewSearchLayer.setLayoutParams(layoutParams2);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.magic_back) {
            finish();
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public final void initBroadcastReceiver() {
        this.mBroadcastReceiver = new BaseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.gallery.search");
        registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    /* loaded from: classes2.dex */
    public class BaseBroadcastReceiver extends BroadcastReceiver {
        public BaseBroadcastReceiver() {
            CertificatesSearchActivity.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (!action.equals("com.miui.gallery.search")) {
                return;
            }
            CertificatesSearchActivity.this.finish();
        }
    }
}
