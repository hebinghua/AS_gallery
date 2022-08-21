package miuix.view.inputmethod;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import miuix.core.util.SoftReferenceSingleton;

/* loaded from: classes3.dex */
public class InputMethodHelper {
    public static final SoftReferenceSingleton<InputMethodHelper> INSTANCE = new SoftReferenceSingleton<InputMethodHelper>() { // from class: miuix.view.inputmethod.InputMethodHelper.1
        @Override // miuix.core.util.SoftReferenceSingleton
        public InputMethodHelper createInstance(Object obj) {
            return new InputMethodHelper((Context) obj);
        }
    };
    public InputMethodManager mManager;

    public InputMethodHelper(Context context) {
        this.mManager = (InputMethodManager) context.getApplicationContext().getSystemService("input_method");
    }

    public static InputMethodHelper getInstance(Context context) {
        return INSTANCE.get(context);
    }

    public void showKeyBoard(EditText editText) {
        editText.requestFocus();
        this.mManager.viewClicked(editText);
        this.mManager.showSoftInput(editText, 0);
    }

    public void hideKeyBoard(EditText editText) {
        this.mManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
    }
}
