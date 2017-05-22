package hoek.bubur.gsmcity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dalbo on 5/20/2017.
 */

public class Conf {
    Context context;
    SharedPreferences sp;

    public static final String CONF_WSADDR = "confwsaddr";

    public Conf(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putConf(String key, String val) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public String getConf(String key) {
        return sp.getString(key, "");
    }
}
