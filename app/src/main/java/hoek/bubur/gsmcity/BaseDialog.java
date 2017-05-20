package hoek.bubur.gsmcity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

/**
 * Created by dalbo on 5/19/2017.
 */

public class BaseDialog extends Dialog {
    boolean active = false;
    ProgressDialog pd;
    Context context;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(context);
        pd.setMessage(context.getString(R.string.pd_memuat));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void showLoad(){
        pd.show();
    }

    public void hideLoad(){
        pd.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public Context getCallerContext(){
        return this.context;
    }

    public Activity getCallerActivity(){
        return ((Activity) this.context);
    }
}
