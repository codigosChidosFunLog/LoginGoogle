package com.example.logingoogle;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {

    private TextInputEditText etNameUser, etNickName, etPhone, etNameNegocio, etDescNegocio;
    private Button etIni, etFin;
    private View containerVendedor;
    private ImageView imgProfile;

    private int mHour, mMinute;
    private String tipoUsuario, nameUSer, nickName,phone, nameNegocio, descNegocio, horaIni, horaFin, uriPhotoProfile;
    private boolean banUbicacion;

    public static final int REQUEST_IMAGE = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etIni = findViewById(R.id.et_horaini);
        etFin = findViewById(R.id.et_horafin);

        etNameUser = findViewById(R.id.et_nameuser);
        etNickName = findViewById(R.id.et_nickname);
        etPhone = findViewById(R.id.et_phone);
        etNameNegocio = findViewById(R.id.et_name_negocio);
        etDescNegocio = findViewById(R.id.et_desc_negocio);

        Spinner spinner = findViewById(R.id.spinner);

        containerVendedor = findViewById(R.id.container_vendedor);
        imgProfile = findViewById(R.id.imgProfile);

        banUbicacion = false;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tipoUsuario = parent.getItemAtPosition(pos).toString();
                containerVendedor.setVisibility(tipoUsuario.equals("Comprador") ? View.GONE : View.VISIBLE );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadProfileDefault();
        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);
    }

    private void loadProfileDefault() {
        GlideApp.with(this).load(R.drawable.perfil)
                .into(imgProfile);
    }

    public void horaInicio(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        horaIni = (hourOfDay<10 ? "0"+hourOfDay : hourOfDay) +":"+
                                (minute<10 ? "0"+minute : minute);
                        etIni.setText(horaIni);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void horaFin(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        horaFin = (hourOfDay<10 ? "0"+hourOfDay : hourOfDay) +":"+
                                (minute<10 ? "0"+minute : minute);
                        etFin.setText(horaFin);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private boolean validateForm(){
        boolean valid = true;

        if (validateTIET(etNameUser))
            nameUSer = etNameUser.getText().toString();
        else
            valid =false;

        if (validateTIET(etNickName))
            nickName = etNickName.getText().toString();
        else
            valid = false;

        if (validateTIET(etPhone))
            phone = etPhone.getText().toString();
        else valid =false;

        if (!tipoUsuario.equals("Comprador")){
            if (validateTIET(etNameNegocio))
                nameNegocio = etNameNegocio.getText().toString();
            else
                valid = false;

            if (validateTIET(etDescNegocio))
                descNegocio = etDescNegocio.getText().toString();
            else
                valid = false;

            if (TextUtils.isEmpty(horaIni) || TextUtils.isEmpty(horaFin)){
                valid = false;
                Toast.makeText(this, "Seleccione la hora de atencion de su negocio", Toast.LENGTH_SHORT).show();
            }

            if (!banUbicacion){
                valid = false;
                Toast.makeText(this, "Seleccione la ubicacion de su negocio", Toast.LENGTH_SHORT).show();
            }
        }
        return valid;
    }

    private boolean validateTIET(TextInputEditText inputEditText){
        String text = inputEditText.getText().toString();
        if (TextUtils.isEmpty(text)){
            inputEditText.setError("Campo vacio");
            return false;
        }else {
            inputEditText.setError(null);
            return true;
        }
    }

    public void selectUbicacion(View view) {
        startActivity(new Intent(this, SelectLocationActivity.class));
    }

    public void go(View view) {
        if (validateForm()){
            //////hacer
        }
    }

    public void loadImage(View view) {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(RegisterUserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(RegisterUserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    // loading profile image from local cache
                    uriPhotoProfile = uri.toString();
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        GlideApp.with(this).load(url)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }
}
