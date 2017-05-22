package hoek.bubur.gsmcity.WebService;

import android.content.Context;

import hoek.bubur.gsmcity.Conf;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dalbo on 5/22/2017.
 */

public class API {
    public String WSADDR = "http://www.google.com/";
    public static final String GEOTAG_GET_LIST_GEOTAG = "";
    public static final String KATEGORI_LIST_RTH_KATEGORI = "/getOfficialRTHKategori";
    public static final String KATEGORI_LIST_RTH_RADIUS = "/getOfficialRTHRadius";

    Context context;
    Conf conf;
    public API(Context context) {
        this.context = context;
        conf = new Conf(context);
        WSADDR = conf.getConf(Conf.CONF_WSADDR);
    }

    public void getGeoTagList(double lat, double lng, Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + GEOTAG_GET_LIST_GEOTAG)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("lat", Double.toString(lat))
                                .addFormDataPart("lng", Double.toString(lng))
                                .build()
                        )
                        .build()
                , callback);
    }

    public void getRTHOfficial(int idKategori, Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + KATEGORI_LIST_RTH_KATEGORI)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("id_kategori", Integer.toString(idKategori))
                                .build()
                        )
                        .build()
                , callback);
    }

    public void getRadiusRTHOfficial(double lat, double lng, String alamat, String nama, double jarak, Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + KATEGORI_LIST_RTH_RADIUS)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("lat", Double.toString(lat))
                                .addFormDataPart("lng", Double.toString(lng))
                                .addFormDataPart("alamat", alamat)
                                .addFormDataPart("nama", nama)
                                .addFormDataPart("jarak", Double.toString(jarak))
                                .build()
                        )
                        .build()
                , callback);
    }

    private void call(Request request, Callback callback) {
        new OkHttpClient().newCall(request).enqueue(callback);
    }
}
