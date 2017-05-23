package hoek.bubur.gsmcity.WebService;

import android.content.Context;

import java.io.File;

import hoek.bubur.gsmcity.Conf;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dalbo on 5/22/2017.
 */

public class API {
    public String WSADDR = "http://www.google.com/";
    public static final String ALL_OFFICIAL_RTH = "/getAllOfficialRTH";
    public static final String GEOTAG_GET_LIST_GEOTAG = "/getGeotagRTH";
    public static final String KATEGORI_LIST_RTH_KATEGORI = "/getOfficialRTHKategori";
    public static final String KATEGORI_LIST_RTH_RADIUS = "/getOfficialRTHRadius";
    public static final String SET_IDE = "/upload";
    Context context;
    Conf conf;

    public API(Context context) {
        this.context = context;
        conf = new Conf(context);
        WSADDR = conf.getConf(Conf.CONF_WSADDR);
    }

    public void getHeatMap(Callback callback, boolean official) {
        if(official){
            call(new Request.Builder()
                            .url(WSADDR + ALL_OFFICIAL_RTH)
                            .get().build()

                    , callback);
        }
        else {
            call(new Request.Builder()
                            .url(WSADDR + GEOTAG_GET_LIST_GEOTAG)
                            .get().build()

                    , callback);
        }

    }

    public void getAllOfficialRTH(Callback callback) {
        call(new Request.Builder()

                .url(WSADDR + ALL_OFFICIAL_RTH)
                .get().build(), callback);
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

    public void putIde(String nama, String alamat, String fasilitas, double lat, double lng, double luas, String imgUrl, Callback callback) {
        File image = new File(imgUrl);
        String extension = "";

        int i = imgUrl.lastIndexOf('.');
        if (i > 0) {
            extension = imgUrl.substring(i + 1);
        }
        call(new Request.Builder()
                        .url(WSADDR + SET_IDE)
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("nama", nama)
                                .addFormDataPart("alamat", alamat)
                                .addFormDataPart("fasilitas", fasilitas)
                                .addFormDataPart("lat", Double.toString(lat))
                                .addFormDataPart("lng", Double.toString(lng))
                                .addFormDataPart("luas", Double.toString(luas))
                                .addFormDataPart("foto", "image." + extension, MultipartBody.create(MediaType.parse("image/*"), image))
                                .build()
                        )
                        .build()
                , callback
        );
    }

    private void call(Request request, Callback callback) {
        new OkHttpClient().newCall(request).enqueue(callback);
    }

    public void getGeotagList(Callback callback) {
        call(new Request.Builder()
                        .url(WSADDR + GEOTAG_GET_LIST_GEOTAG)
                        .get().build()

                , callback);
    }
}
