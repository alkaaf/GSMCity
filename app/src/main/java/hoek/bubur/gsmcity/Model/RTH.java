package hoek.bubur.gsmcity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalbo on 5/20/2017.
 */

public class RTH implements Parcelable {

    @SerializedName("id")
    int id;
    @SerializedName("id_kategori")
    int idKategori;
    @SerializedName("nama")
    String namaLokasi;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("fasilitas")
    String fasilitas;
    @SerializedName("url")
    String urlGambar;
    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;
    @SerializedName("jarak")
    double jarak; // km
    @SerializedName("luas")
    double luas; // km

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public RTH(int id, String namaLokasi, String fasilitas, double lat, double lng, double jarak, double luas) {
        this.id = id;
        this.namaLokasi = namaLokasi;
        this.fasilitas = fasilitas;
        this.lat = lat;
        this.lng = lng;
        this.jarak = jarak;
        this.luas = luas;
    }

    public RTH(int id, String namaLokasi, String fasilitas, double jarak, double lat, double lng) {
        this.id = id;
        this.namaLokasi = namaLokasi;
        this.fasilitas = fasilitas;
        this.lat = lat;
        this.lng = lng;
        this.jarak = jarak;
    }

    public RTH(int id, String namaLokasi, String fasilitas, double jarak) {
        this.id = id;
        this.namaLokasi = namaLokasi;
        this.fasilitas = fasilitas;
        this.jarak = jarak;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public double getLuas() {
        return luas;
    }

    public void setLuas(double luas) {
        this.luas = luas;
    }

    public RTH() {
    }

    public static List<RTH> getSample() {
        List<RTH> l = new ArrayList<>();
        l.add(new RTH(1, "Sawetq", "Kamar mandi1", 321, 1, 0));
        l.add(new RTH(1, "Sawetw", "Kamar mandi3", 322, 12, 10));
        l.add(new RTH(1, "Sawete", "Kamar mandi4", 323, 13, 20));
        l.add(new RTH(1, "Sawetr", "Kamar mandi5", 324, 14, 30));
        l.add(new RTH(1, "Sawett", "Kamar mandi6", 325, 15, 40));
        l.add(new RTH(1, "Sawety", "Kamar mandi7", 326, 15, 50));
        l.add(new RTH(1, "Sawetu", "Kamar mandi8", 327, 16, 5));
        l.add(new RTH(1, "Saweti", "Kamar mandi9", 328, 17, 7));
        l.add(new RTH(1, "Saweto", "Kamar mandi9", 329, 18, 8));
        l.add(new RTH(1, "Sawetp", "Kamar mandis", 320, 19, 9));
        l.add(new RTH(1, "Sawetq", "Kamar mandid", 321, 10, 51));
        return l;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idKategori);
        dest.writeString(this.namaLokasi);
        dest.writeString(this.alamat);
        dest.writeString(this.fasilitas);
        dest.writeString(this.urlGambar);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.jarak);
        dest.writeDouble(this.luas);
    }

    protected RTH(Parcel in) {
        this.id = in.readInt();
        this.idKategori = in.readInt();
        this.namaLokasi = in.readString();
        this.alamat = in.readString();
        this.fasilitas = in.readString();
        this.urlGambar = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.jarak = in.readDouble();
        this.luas = in.readDouble();
    }

    public static final Creator<RTH> CREATOR = new Creator<RTH>() {
        @Override
        public RTH createFromParcel(Parcel source) {
            return new RTH(source);
        }

        @Override
        public RTH[] newArray(int size) {
            return new RTH[size];
        }
    };
}
