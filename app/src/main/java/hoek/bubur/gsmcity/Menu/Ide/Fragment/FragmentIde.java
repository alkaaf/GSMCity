package hoek.bubur.gsmcity.Menu.Ide.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseApp;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.Model.ResponseHandler;
import hoek.bubur.gsmcity.R;
import hoek.bubur.gsmcity.WebService.API;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dalbo on 5/22/2017.
 */

public class FragmentIde extends BaseFragment {
    @BindView(R.id.iNama)
    EditText iNama;
    @BindView(R.id.iAlamat)
    EditText iAlamat;
    @BindView(R.id.iFasilitas)
    EditText iFasilitas;
    @BindView(R.id.iLuas)
    EditText iLuas;
    @BindView(R.id.bFoto)
    Button bFoto;
    @BindView(R.id.bSimpan)
    Button bSimpan;
    @BindView(R.id.imgPrev)
    ImageView imgPrev;
    private ResponseHandler handler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ide, container, false);
        ButterKnife.bind(this, v);

        bFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(getActivity());
            }
        });
        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkData()) {
                    API api = new API(getContext());
                    showLoad();
                    api.putIde(
                            getStringViewValue(iNama),
                            getStringViewValue(iAlamat),
                            getStringViewValue(iFasilitas),
                            BaseApp.getLat(),
                            BaseApp.getLng(),
                            getDoubleViewValue(iLuas),
                            resultUri.getPath(),
                            new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    if (isActive()) {
                                        Toast.makeText(getContext(), "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                                        hideLoad();
                                    }
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        try {
                                            handler = new Gson().fromJson(response.body().string(), ResponseHandler.class);
                                            if (isActive()) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (handler.isSuccess()) {
                                                            clearInput();
                                                        }
                                                        Toast.makeText(getContext(), handler.getMessage(), Toast.LENGTH_SHORT).show();
                                                        hideLoad();
                                                    }
                                                });
                                            }
                                        } catch (JsonSyntaxException e) {
                                            e.printStackTrace();
                                            if(isActive()){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(), "Gagal parsing data", Toast.LENGTH_SHORT).show();
                                                        hideLoad();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                    );
                }
            }
        });
        return v;
    }

    public void clearInput() {
        iNama.setText("");
        iAlamat.setText("");
        iFasilitas.setText("");
        iLuas.setText("");
        resultUri = null;
        imgPrev.setImageBitmap(null);
    }

    Uri mCropImageUri;
    Uri resultUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
//            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
//                imgBukti.setImageURI(imageUri);
            }
        }

        // on image aftar cropped
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult actRes = CropImage.getActivityResult(data);
            resultUri = actRes.getUri();
            imgPrev.setImageURI(resultUri);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setRequestedSize(1024, 768)
                .start(getActivity());
    }

    public String getStringViewValue(EditText v) {
        if (v.getText().toString().isEmpty()) {
            return "";
        } else {
            return v.getText().toString();
        }
    }

    public double getDoubleViewValue(EditText v) {
        if (v.getText().toString().isEmpty()) {
            return 0;
        } else {
            return Double.parseDouble(v.getText().toString());
        }
    }

    public boolean checkData() {

        if (getStringViewValue(iAlamat).equals("")) {
            Toast.makeText(getContext(), "Harap masukkan alamat RTH", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getStringViewValue(iNama).equals("")) {
            Toast.makeText(getContext(), "Harap masukkan Nama RTH", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getDoubleViewValue(iLuas) == 0) {
            Toast.makeText(getContext(), "Harap masukkan luas RTH", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (resultUri == null) {
            Toast.makeText(getContext(), "Gambar belum dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (BaseApp.getLatLng() == null) {
            Toast.makeText(getContext(), "Lokasi anda belum ditemukan. Harap tunggu sebentar", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
