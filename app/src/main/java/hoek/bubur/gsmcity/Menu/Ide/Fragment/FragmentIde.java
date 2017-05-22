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

import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseFragment;
import hoek.bubur.gsmcity.R;

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
    Button bSimpatn;
    @BindView(R.id.imgPrev)
    ImageView imgPrev;

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
        return v;
    }

    Uri mCropImageUri;
    Uri resultUri;

//    @Override
//    @SuppressLint("NewApi")
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super(requestCode,resultCode,data);
//        Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
//        // handle result of pick image chooser
//        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
////            Uri imageUri = CropImage.getPickImageResultUri(this, data);
//
//            // For API >= 23 we need to check specifically that we have permissions to read external storage.
//            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
//                // request permissions and handle the result in onRequestPermissionsResult()
//                mCropImageUri = imageUri;
//                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
//            } else {
//                // no permissions required or already grunted, can start crop image activity
//                startCropImageActivity(imageUri);
////                imgBukti.setImageURI(imageUri);
//            }
//        }
//
//        // on image aftar cropped
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            CropImage.ActivityResult actRes = CropImage.getActivityResult(data);
//            resultUri = actRes.getUri();
//            imgPrev.setImageURI(resultUri);
//        }
//    }


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
}
