package com.miui.gallery.ui.renameface;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.DisplayFolderItem;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.permission.cn.legacy.CtaPermissions;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.ui.BaseFragment;
import com.miui.gallery.ui.PeopleRelationSetDialogFragment;
import com.miui.gallery.ui.renameface.FolderItemsLoader;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class InputFaceNameFragment extends BaseFragment {
    public static String[] sProjection = {Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_URI, Phone.PHOTO_THUMBNAIL_URI};
    public GetContactsInfo mGetContactInfoHelper;
    public Handler mHandler = new Handler();
    public boolean mInputTextChanged;
    public EditText mInputView;
    public boolean mIsRelationSetted;
    public ListView mListView;
    public FolderItemsLoader mLoader;
    public MergeNameAdapter mMergeAdapter;
    public boolean mOnlyHasContactResult;
    public boolean mOnlyUseContactAdapter;
    public String mOriginalName;
    public String mOriginalSetLocalId;

    /* loaded from: classes2.dex */
    public static class Phone {
        public static String CONTACT_ID = "_id";
        public static String DISPLAY_NAME = "display_name";
        public static String NUMBER = "data1";
        public static String PHOTO_THUMBNAIL_URI = "photo_thumb_uri";
        public static String PHOTO_URI = "photo_uri";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "face_input_name";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onInflateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(R.layout.input_face_name_fragment, viewGroup, false);
        Intent intent = this.mActivity.getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            this.mOnlyUseContactAdapter = extras.getBoolean("only_use_contact", false);
            this.mOriginalName = extras.getString("original_name", "");
            this.mIsRelationSetted = extras.getBoolean("is_relation_setted", false);
            this.mOriginalSetLocalId = extras.getString("original_path_album_local_id", "");
        }
        this.mOnlyHasContactResult = true;
        initTitleBar();
        initEditText();
        initFaceNameLoader();
        initMergeNameAdapter();
        initListView(inflate);
        return inflate;
    }

    public final void stopFaceLoaderAndFinish() {
        this.mLoader.cancel();
        this.mActivity.finish();
    }

    public void onBackPressed() {
        stopFaceLoaderAndFinish();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        updateNameList();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        MergeNameAdapter mergeNameAdapter = this.mMergeAdapter;
        if (mergeNameAdapter != null) {
            mergeNameAdapter.changeCursor(null);
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftKeyboard();
    }

    public final void initTitleBar() {
        View inflate = this.mActivity.getLayoutInflater().inflate(R.layout.input_face_name_activity_title, (ViewGroup) null);
        inflate.setLayoutParams(new ActionBar.LayoutParams(-1, -1));
        this.mActivity.getAppCompatActionBar().setDisplayOptions(16, 16);
        this.mActivity.getAppCompatActionBar().setCustomView(inflate);
        this.mInputView = (EditText) inflate.findViewById(R.id.autoCompleteTextView);
        if (!TextUtils.isEmpty(this.mOriginalName)) {
            this.mInputView.setText(this.mOriginalName);
            this.mInputView.selectAll();
        }
        inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InputFaceNameFragment.this.stopFaceLoaderAndFinish();
            }
        });
        inflate.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = InputFaceNameFragment.this.mInputView.getText().toString();
                if (obj != null) {
                    obj = obj.trim();
                }
                String str = obj;
                if (!TextUtils.isEmpty(str)) {
                    boolean validateIsRepeatName = InputFaceNameFragment.this.validateIsRepeatName(str);
                    if (validateIsRepeatName || !TextUtils.isEmpty(InputFaceNameFragment.this.mOriginalName) || InputFaceNameFragment.this.mOnlyUseContactAdapter || InputFaceNameFragment.this.mIsRelationSetted) {
                        InputFaceNameFragment.this.onOkClick(str, validateIsRepeatName, null, null, null);
                        return;
                    } else {
                        InputFaceNameFragment.this.showSetRelationDialog(str, null);
                        return;
                    }
                }
                ToastUtils.makeText(InputFaceNameFragment.this.mActivity, InputFaceNameFragment.this.getString(R.string.input_or_select_a_name));
            }
        });
    }

    public final void onOkClick(String str, boolean z, String str2, String str3, String str4) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("name", str);
        bundle.putBoolean("is_repeat_name", z);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString("phone", str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            bundle.putString("relation_with_me", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            bundle.putString("relation_with_me_text", str4);
        }
        intent.putExtras(bundle);
        this.mActivity.setResult(-1, intent);
        hideSoftKeyboard();
        stopFaceLoaderAndFinish();
    }

    public void showSetRelationDialog(final String str, final String str2) {
        PeopleRelationSetDialogFragment.createRelationSetDialog(this.mActivity, this.mActivity.getString(R.string.relation_with_you), null, 1, new PeopleRelationSetDialogFragment.RelationSelectedListener() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.3
            @Override // com.miui.gallery.ui.PeopleRelationSetDialogFragment.RelationSelectedListener
            public void onRelationSelected(String str3, String str4) {
                InputFaceNameFragment.this.onOkClick(str, false, str2, str3, str4);
            }
        });
    }

    public final void initEditText() {
        this.mInputView.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.4
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                InputFaceNameFragment.this.mInputTextChanged = true;
                InputFaceNameFragment.this.updateNameList();
            }
        });
    }

    public final boolean isAccessContactAllowed() {
        boolean z = false;
        if (!IntentUtil.isContactPackageInstalled()) {
            return false;
        }
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            z = PermissionUtils.checkPermission((Activity) appCompatActivity, "android.permission.READ_CONTACTS");
        } else {
            DefaultLogger.w("InputFaceNameFragment", "Maybe have detached");
        }
        return !BaseBuildUtil.isInternational() ? z & CtaPermissions.isPrivacyAllowed("android.permission.READ_CONTACTS") : z;
    }

    public void updateNameList() {
        String obj = this.mInputTextChanged ? this.mInputView.getText().toString() : null;
        if (TextUtils.isEmpty(obj) && this.mOnlyUseContactAdapter) {
            this.mOnlyHasContactResult = true;
            this.mGetContactInfoHelper.getContactCursor();
        } else {
            changeMergeResultCursor(obj);
        }
        this.mMergeAdapter.notifyDataSetChanged();
    }

    public final void changeMergeResultCursor(final String str) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Cursor>() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Cursor mo1807run(ThreadPool.JobContext jobContext) {
                return InputFaceNameFragment.this.mGetContactInfoHelper.getSuggestionCursor(str);
            }
        }, new FutureListener<Cursor>() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.6
            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<Cursor> future) {
                Cursor[] cursorArr;
                if (future == null) {
                    return;
                }
                Cursor cursor = future.get();
                boolean z = (cursor == null || cursor.getCount() == 0) ? false : true;
                Cursor faceSuggest = InputFaceNameFragment.this.getFaceSuggest(str);
                boolean z2 = faceSuggest != null && faceSuggest.getCount() > 0;
                InputFaceNameFragment.this.mOnlyHasContactResult = !z2;
                if (z && z2) {
                    cursorArr = new Cursor[]{faceSuggest, InputFaceNameFragment.this.getContactTipRowCursor(), cursor};
                } else if (z) {
                    cursorArr = new Cursor[]{InputFaceNameFragment.this.getContactTipRowCursor(), cursor};
                    BaseMiscUtil.closeSilently(faceSuggest);
                } else {
                    cursorArr = new Cursor[]{faceSuggest};
                    BaseMiscUtil.closeSilently(cursor);
                }
                final MergeCursor mergeCursor = new MergeCursor(cursorArr);
                InputFaceNameFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        InputFaceNameFragment.this.mMergeAdapter.changeCursor(mergeCursor);
                    }
                });
            }
        });
    }

    public final Cursor getFaceSuggest(String str) {
        MatrixCursor matrixCursor = new MatrixCursor(sProjection);
        String trim = str != null ? str.trim() : "";
        ArrayList<DisplayFolderItem> tryToGetLoadedItems = this.mLoader.tryToGetLoadedItems(AbstractComponentTracker.LINGERING_TIMEOUT);
        if (BaseMiscUtil.isValid(tryToGetLoadedItems)) {
            Iterator<DisplayFolderItem> it = tryToGetLoadedItems.iterator();
            int i = 100;
            while (it.hasNext()) {
                DisplayFolderItem next = it.next();
                if (next.name.toLowerCase().startsWith(trim.toLowerCase())) {
                    matrixCursor.addRow(new Object[]{String.valueOf(i), next.name, "face", null, null});
                    i++;
                }
            }
        }
        return matrixCursor;
    }

    public final Cursor getContactTipRowCursor() {
        MatrixCursor matrixCursor = new MatrixCursor(sProjection);
        matrixCursor.addRow(new Object[]{String.valueOf(-1), isAdded() ? getString(R.string.contact_tip) : "", "contact", null, null});
        return matrixCursor;
    }

    public final DisplayFolderItem getDisplayItemByName(String str) {
        Iterator<DisplayFolderItem> it = this.mLoader.tryToGetLoadedItems(AbstractComponentTracker.LINGERING_TIMEOUT).iterator();
        while (it.hasNext()) {
            DisplayFolderItem next = it.next();
            if (next.name.equalsIgnoreCase(str)) {
                return next;
            }
        }
        return null;
    }

    public final boolean validateIsRepeatName(String str) {
        return FaceAlbumRenameHandler.getDisplayFolderItem(this.mLoader.tryToGetLoadedItems(300L), str.trim()) != null;
    }

    public final void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list);
        this.mListView = listView;
        listView.setAdapter((ListAdapter) this.mMergeAdapter);
        this.mListView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.7
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 1) {
                    InputFaceNameFragment.this.hideSoftKeyboard();
                }
            }
        });
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.8
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                Cursor cursor = (Cursor) InputFaceNameFragment.this.mMergeAdapter.getItem(i);
                String phoneNumber = InputFaceNameFragment.getPhoneNumber(cursor);
                if (phoneNumber.equalsIgnoreCase("contact")) {
                    return;
                }
                String string = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
                boolean validateIsRepeatName = InputFaceNameFragment.this.validateIsRepeatName(string);
                if (validateIsRepeatName || !TextUtils.isEmpty(InputFaceNameFragment.this.mOriginalName) || InputFaceNameFragment.this.mOnlyUseContactAdapter || TextUtils.isEmpty(phoneNumber) || InputFaceNameFragment.this.mIsRelationSetted) {
                    InputFaceNameFragment.this.onOkClick(string, validateIsRepeatName, phoneNumber, null, null);
                } else {
                    InputFaceNameFragment.this.showSetRelationDialog(string, phoneNumber);
                }
            }
        });
    }

    public final void initFaceNameLoader() {
        this.mLoader = new FaceFolderItemsLoader(this.mActivity, null, new FolderItemsLoader.LoaderUpdatedItems() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.9
            @Override // com.miui.gallery.ui.renameface.FolderItemsLoader.LoaderUpdatedItems
            public void onLoaderUpdatedItems() {
                if (!InputFaceNameFragment.this.mOnlyUseContactAdapter) {
                    InputFaceNameFragment.this.changeMergeResultCursor(null);
                    InputFaceNameFragment.this.mMergeAdapter.notifyDataSetChanged();
                }
            }
        }, !TextUtils.isEmpty(this.mOriginalSetLocalId) ? new long[]{Long.parseLong(this.mOriginalSetLocalId)} : null);
    }

    public final void initMergeNameAdapter() {
        this.mGetContactInfoHelper = new GetContactsInfo(this.mActivity);
        this.mMergeAdapter = new MergeNameAdapter(this.mActivity);
    }

    public final void hideSoftKeyboard() {
        ((InputMethodManager) this.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.mListView.getWindowToken(), 0);
    }

    public final void displayView(View view) {
        if (view.getVisibility() == 8) {
            view.setVisibility(0);
        }
    }

    public final void disappearView(View view) {
        if (view.getVisibility() == 0) {
            view.setVisibility(8);
        }
    }

    public static PeopleContactInfo getContactInfo(Context context, Intent intent) {
        PeopleContactInfo peopleContactInfo = new PeopleContactInfo();
        if (intent.getData() != null) {
            Uri data = intent.getData();
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(data, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    peopleContactInfo.name = cursor.getString(cursor.getColumnIndex("display_name"));
                    peopleContactInfo.phone = getPhoneNumber(cursor);
                    peopleContactInfo.coverPath = getCoverPath(cursor);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                peopleContactInfo.name = extras.getString("name", "");
                peopleContactInfo.phone = extras.getString("phone", "");
                boolean z = extras.getBoolean("is_repeat_name");
                peopleContactInfo.isRepeatName = z;
                if (z) {
                    peopleContactInfo.localGroupId = extras.getString("repeat_local_group_id", "");
                }
                peopleContactInfo.relationWithMe = extras.getString("relation_with_me", "");
                peopleContactInfo.relationWithMeText = extras.getString("relation_with_me_text", "");
            }
        }
        return peopleContactInfo;
    }

    public static String getPhoneNumber(Cursor cursor) {
        String str;
        try {
            str = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
        } catch (Exception unused) {
            str = null;
        }
        return str == null ? "" : str;
    }

    public static String getCoverPath(Cursor cursor) {
        try {
            String string = cursor.getString(cursor.getColumnIndex(Phone.PHOTO_URI));
            return string == null ? cursor.getString(cursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI)) : string;
        } catch (Exception unused) {
            return "";
        }
    }

    /* loaded from: classes2.dex */
    public static class ContactsInfo {
        public String _id;
        public String coverPath;
        public String displayName;
        public String phoneNumber;

        public ContactsInfo() {
        }
    }

    /* loaded from: classes2.dex */
    public class GetContactsInfo {
        public Context context;

        public GetContactsInfo(Context context) {
            this.context = context;
        }

        public final void getContactCursor() {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Cursor>() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.GetContactsInfo.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Cursor mo1807run(ThreadPool.JobContext jobContext) {
                    if (!InputFaceNameFragment.this.isAccessContactAllowed()) {
                        return null;
                    }
                    try {
                        return new MergeCursor(new Cursor[]{InputFaceNameFragment.this.getContactTipRowCursor(), GetContactsInfo.this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, InputFaceNameFragment.sProjection, null, null, " display_name COLLATE LOCALIZED ")});
                    } catch (IllegalArgumentException e) {
                        DefaultLogger.e("InputFaceNameFragment", e);
                        return null;
                    }
                }
            }, new FutureListener<Cursor>() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.GetContactsInfo.2
                @Override // com.miui.gallery.concurrent.FutureListener
                public void onFutureDone(Future<Cursor> future) {
                    if (future == null) {
                        return;
                    }
                    final Cursor cursor = future.get();
                    InputFaceNameFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.renameface.InputFaceNameFragment.GetContactsInfo.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (InputFaceNameFragment.this.mOnlyUseContactAdapter) {
                                InputFaceNameFragment.this.mMergeAdapter.changeCursor(cursor);
                                InputFaceNameFragment.this.mMergeAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }

        public final Cursor getSuggestionCursor(String str) {
            Uri uri;
            Uri uri2;
            ContentResolver contentResolver = InputFaceNameFragment.this.mActivity.getContentResolver();
            if (InputFaceNameFragment.this.isAccessContactAllowed()) {
                if (!TextUtils.isEmpty(str)) {
                    uri2 = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, InputFaceNameFragment.sqlEscapeString(str.replace('/', ' ')));
                } else {
                    uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                }
                uri = uri2;
            } else {
                uri = null;
            }
            if (uri == null) {
                return null;
            }
            try {
                return contentResolver.query(uri, InputFaceNameFragment.sProjection, null, null, " display_name COLLATE LOCALIZED ");
            } catch (IllegalArgumentException e) {
                DefaultLogger.e("InputFaceNameFragment", e);
                return null;
            }
        }
    }

    public static String sqlEscapeString(String str) {
        if (str.indexOf(39) != -1) {
            StringBuilder sb = new StringBuilder();
            int length = str.length();
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if (charAt == '\'') {
                    sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
                }
                sb.append(charAt);
            }
            return sb.toString();
        }
        return str;
    }

    /* loaded from: classes2.dex */
    public class MergeNameAdapter extends CursorAdapter {
        public int CONTECT_HEADER_VIEW;
        public int NORMAL_VIEW;
        public ContactsInfo mContactsInfo;
        public LayoutInflater mInflater;

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 2;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public ImageView avartarImageView;
            public TextView name;

            public ViewHolder() {
            }
        }

        public MergeNameAdapter(Context context) {
            super(context, null);
            this.CONTECT_HEADER_VIEW = 0;
            this.NORMAL_VIEW = 1;
            this.mInflater = null;
            this.mContactsInfo = new ContactsInfo();
            this.mInflater = LayoutInflater.from(context);
            InputFaceNameFragment.this.mGetContactInfoHelper.getContactCursor();
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View inflate;
            if (getItemViewTypeByCursor(cursor) == this.CONTECT_HEADER_VIEW) {
                inflate = this.mInflater.inflate(R.layout.input_face_name_contact_header_item, viewGroup, false);
                inflate.setTag(R.id.tag_view_type, Integer.valueOf(this.CONTECT_HEADER_VIEW));
            } else {
                inflate = this.mInflater.inflate(R.layout.input_face_name_item, viewGroup, false);
                inflate.setTag(R.id.tag_view_type, Integer.valueOf(this.NORMAL_VIEW));
            }
            FolmeUtil.setDefaultTouchAnim(inflate, null, false, false, false);
            return inflate;
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            Bitmap safeDecodeBitmap;
            if (((Integer) view.getTag(R.id.tag_view_type)).intValue() == this.CONTECT_HEADER_VIEW) {
                bindHeaderView(view, context, cursor);
                return;
            }
            this.mContactsInfo._id = cursor.getString(cursor.getColumnIndex(Phone.CONTACT_ID));
            this.mContactsInfo.phoneNumber = InputFaceNameFragment.getPhoneNumber(cursor);
            this.mContactsInfo.displayName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            this.mContactsInfo.coverPath = InputFaceNameFragment.getCoverPath(cursor);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.avartarImageView = (ImageView) view.findViewById(R.id.folder_cover);
                view.setTag(viewHolder);
            }
            viewHolder.name.setText(this.mContactsInfo.displayName);
            if (this.mContactsInfo.phoneNumber.equalsIgnoreCase("face")) {
                DisplayFolderItem displayItemByName = InputFaceNameFragment.this.getDisplayItemByName(this.mContactsInfo.displayName);
                if (displayItemByName == null) {
                    return;
                }
                Glide.with(view).mo985asBitmap().mo962load(GalleryModel.of(displayItemByName.thumbnailPath)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(((FaceDisplayFolderItem) displayItemByName).mFacePosRelative)).into(viewHolder.avartarImageView);
                return;
            }
            viewHolder.avartarImageView.setImageResource(R.drawable.people_face_default);
            if (this.mContactsInfo.coverPath == null || (safeDecodeBitmap = BitmapUtils.safeDecodeBitmap(InputFaceNameFragment.this.mActivity.getContentResolver(), Uri.parse(this.mContactsInfo.coverPath))) == null) {
                return;
            }
            viewHolder.avartarImageView.setImageBitmap(safeDecodeBitmap);
        }

        public final void bindHeaderView(View view, Context context, Cursor cursor) {
            View findViewById = view.findViewById(R.id.divider);
            if (InputFaceNameFragment.this.mOnlyHasContactResult) {
                InputFaceNameFragment.this.disappearView(findViewById);
            } else {
                InputFaceNameFragment.this.displayView(findViewById);
            }
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i) {
            Cursor cursor = getCursor();
            if (cursor.moveToPosition(i)) {
                return getItemViewTypeByCursor(cursor);
            }
            return -1;
        }

        public final int getItemViewTypeByCursor(Cursor cursor) {
            return InputFaceNameFragment.getPhoneNumber(cursor).equalsIgnoreCase("contact") ? this.CONTECT_HEADER_VIEW : this.NORMAL_VIEW;
        }
    }
}
