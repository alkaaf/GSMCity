package hoek.bubur.gsmcity.WebService;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dalbo on 5/22/2017.
 */

public class API {

    public static final String WSADDR = "";
    public static final String GEOTAG_CTL = "";
    public static final String GEOTAG_GET_LIST_GEOTAG = "";
    public static final String KATEGORI_CTL = "";
    public static final String KATEGORI_LIST_RTH = "";
    public static final String KATEGORI_LIST_RTH_RADIUS = "";

    public void getGeoTagList(double lat, double lng, Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + GEOTAG_CTL + GEOTAG_GET_LIST_GEOTAG)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("lat", Double.toString(lat))
                                .addFormDataPart("lng", Double.toString(lng))
                                .build()
                        )
                        .build()
                , callback);
    }

    public void getRTHOfficial(int kategori, Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + KATEGORI_CTL + KATEGORI_LIST_RTH)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("id", Integer.toString(kategori))
                                .build()
                        )
                        .build()
                , callback);
    }

    public void getRadiusRTHOfficial(double lat, double lng, String alamat, String nama, double jarak, Callback callback){
        call(new Request.Builder()
                        .url(WSADDR + KATEGORI_CTL + KATEGORI_LIST_RTH)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("lat", Double.toString(lat))
                                .addFormDataPart("lng", Double.toString(lng))
                                .addFormDataPart("alamat",alamat)
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
