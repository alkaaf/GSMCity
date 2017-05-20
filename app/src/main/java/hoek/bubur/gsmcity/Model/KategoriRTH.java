package hoek.bubur.gsmcity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalbo on 5/20/2017.
 */

public class KategoriRTH implements Parcelable {
    int id;
    String namaKategori;

    public KategoriRTH() {
    }

    public KategoriRTH(int id, String namaKategori) {
        this.id = id;
        this.namaKategori = namaKategori;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public static List<KategoriRTH> getSample() {
        List<KategoriRTH> l = new ArrayList<>();
        l.add(new KategoriRTH(1, "Taman"));
        l.add(new KategoriRTH(2, "Hutan Kota"));
        l.add(new KategoriRTH(3, "Pulau & Median Jalan"));
        l.add(new KategoriRTH(4, "SUTET"));
        l.add(new KategoriRTH(5, "Mata Air"));
        l.add(new KategoriRTH(6, "Makam"));
        return l;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.namaKategori);
    }

    protected KategoriRTH(Parcel in) {
        this.id = in.readInt();
        this.namaKategori = in.readString();
    }

    public static final Parcelable.Creator<KategoriRTH> CREATOR = new Parcelable.Creator<KategoriRTH>() {
        @Override
        public KategoriRTH createFromParcel(Parcel source) {
            return new KategoriRTH(source);
        }

        @Override
        public KategoriRTH[] newArray(int size) {
            return new KategoriRTH[size];
        }
    };
}
