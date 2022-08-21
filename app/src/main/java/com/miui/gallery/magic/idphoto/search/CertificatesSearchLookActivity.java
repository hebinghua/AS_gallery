package com.miui.gallery.magic.idphoto.search;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$integer;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseActivity;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import com.miui.gallery.magic.idphoto.menu.CertificatesMenuModel;
import com.miui.gallery.magic.util.DialogUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.magic.widget.EmptyRecyclerView;
import com.miui.gallery.magic.widget.TouchFrameLayout;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import java.util.ArrayList;
import java.util.Collections;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class CertificatesSearchLookActivity extends BaseActivity implements View.OnClickListener, TouchFrameLayout.OnTouchDownListener {
    public SearchAdapter mAdapter;
    public BaseBroadcastReceiver mBroadcastReceiver;
    public Configuration mConfiguration;
    public EditText mEditSearch;
    public TouchFrameLayout mEditSearchRoot;
    public ArrayList<CategoryItem> mList;
    public EmptyRecyclerView mListView;
    public ArrayList<CategoryItem> mSearchList;
    public View rlEditCancel;

    @Override // com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.ts_magic_id_photo_search_look);
        initView();
        initData();
        initBroadcastReceiver();
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            setOrientation();
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mEditSearch.postDelayed(new Runnable() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.1
            @Override // java.lang.Runnable
            public void run() {
                CertificatesSearchLookActivity.this.mEditSearch.requestFocus();
                ((InputMethodManager) CertificatesSearchLookActivity.this.mEditSearch.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 400L);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public final void initView() {
        this.mListView = (EmptyRecyclerView) findViewById(R$id.magic_idp_search_list);
        this.mEditSearch = (EditText) findViewById(R$id.magic_idp_search_edit);
        this.mEditSearchRoot = (TouchFrameLayout) findViewById(R$id.magic_idp_search_list_root);
        this.mListView.setLayoutManager(new LinearLayoutManager(this));
        this.mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    ((InputMethodManager) CertificatesSearchLookActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(CertificatesSearchLookActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                }
                return false;
            }
        });
        findViewById(R$id.tv_search_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CertificatesSearchLookActivity.this.isSoftShowing()) {
                    ((InputMethodManager) CertificatesSearchLookActivity.this.mEditSearch.getContext().getSystemService("input_method")).hideSoftInputFromWindow(CertificatesSearchLookActivity.this.mEditSearch.getWindowToken(), 0);
                }
                CertificatesSearchLookActivity.this.mEditSearch.postDelayed(new Runnable() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        new Thread() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.3.1.1
                            @Override // java.lang.Thread, java.lang.Runnable
                            public void run() {
                                try {
                                    new Instrumentation().sendKeyDownUpSync(4);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }, 200L);
            }
        });
        View findViewById = findViewById(R$id.rl_edit_cancel);
        this.rlEditCancel = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CertificatesSearchLookActivity.this.mEditSearch.setText("");
                CertificatesSearchLookActivity.this.rlEditCancel.setVisibility(8);
            }
        });
        this.rlEditCancel.setVisibility(8);
        this.mEditSearchRoot.setOnTouchDownListener(this);
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        setOrientation();
        fitListView();
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
        this.mListView.setVisibility(8);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.5
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                Intent intent = new Intent();
                intent.setAction("com.miui.gallery.search");
                intent.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, CertificatesSearchLookActivity.this.mAdapter.getItem(i).getIndex());
                CertificatesSearchLookActivity.this.sendBroadcast(intent);
                return false;
            }
        });
        this.mEditSearch.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.magic.idphoto.search.CertificatesSearchLookActivity.6
            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
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
                    CertificatesSearchLookActivity.this.rlEditCancel.setVisibility(8);
                    CertificatesSearchLookActivity.this.searchEdit(obj);
                    return;
                }
                CertificatesSearchLookActivity.this.rlEditCancel.setVisibility(0);
                CertificatesSearchLookActivity.this.searchEdit(obj);
            }
        });
    }

    public final void fitListView() {
        if (OrientationCheckHelper.isSupportOrientationChange()) {
            this.mListView.setLayoutManager(new StaggeredGridLayoutManager(ResourceUtil.getInt(R$integer.ceriticates_grid_span_count), 1));
            return;
        }
        this.mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public final void searchEdit(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mAdapter.newData(this.mList);
            this.mListView.setVisibility(8);
            return;
        }
        this.mListView.setVisibility(0);
        if (this.mSearchList == null) {
            this.mSearchList = new ArrayList<>();
        }
        this.mSearchList.clear();
        if (str.contains("x")) {
            str = str.replace("x", "×");
        }
        if (str.contains("X")) {
            str = str.replace("X", "×");
        }
        if (str.contains(Marker.ANY_MARKER)) {
            str = str.replace(Marker.ANY_MARKER, "×");
        }
        for (int size = this.mList.size() - 1; size >= 0; size--) {
            CategoryItem categoryItem = this.mList.get(size);
            if (categoryItem.getTitle().contains(str) || categoryItem.getText().contains(str)) {
                this.mSearchList.add(categoryItem);
            }
        }
        Collections.reverse(this.mSearchList);
        this.mAdapter.newData(this.mSearchList);
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

    public final boolean isSoftShowing() {
        int height = getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return (height * 2) / 3 > rect.bottom;
    }

    public final void initBroadcastReceiver() {
        this.mBroadcastReceiver = new BaseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.gallery.search");
        registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    @Override // com.miui.gallery.magic.widget.TouchFrameLayout.OnTouchDownListener
    public void onDown() {
        DialogUtil.hintKbTwo((miuix.androidbasewidget.widget.EditText) this.mEditSearch, getApplication());
    }

    /* loaded from: classes2.dex */
    public class BaseBroadcastReceiver extends BroadcastReceiver {
        public BaseBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (!action.equals("com.miui.gallery.search")) {
                return;
            }
            CertificatesSearchLookActivity.this.finish();
        }
    }
}
