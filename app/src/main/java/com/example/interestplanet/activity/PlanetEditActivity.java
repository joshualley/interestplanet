package com.example.interestplanet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ImageService;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class PlanetEditActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private EditText name;
    private ImageView cover;
    private Button save;

    private String coverSuffix = "png";
    private boolean isModify = false;
    private boolean isCoverModify = false;

    private Planet mPlanet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_edit);

        Intent intent = getIntent();
        String planetStr = intent.getStringExtra("planet");
        if (planetStr == null) {
            mPlanet = new Planet();
        } else {
            mPlanet = new Gson().fromJson(planetStr, Planet.class);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (isModify) {
                // If there is a change, show an exit notification
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Hint");
                builder.setMessage("You haven't saved the planet yet. Are you sure to exit?");
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    finish();
                    dialog.dismiss();
                });
                builder.setNeutralButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            } else {
                finish();
            }
        });

        name = findViewById(R.id.name);
        cover = findViewById(R.id.cover);
        save = findViewById(R.id.save);

        setListener();
        initView();

        // set the progress dialog
        progress = new ProgressDialog(this);
        progress.setMessage("Saving...");
        progress.setMax(100);
        progress.setProgress(0);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
    }

    private void setListener() {
        save.setOnClickListener(v -> {
            String aName = name.getText().toString();
            Bitmap bitmap = ((BitmapDrawable)cover.getDrawable()).getBitmap();
            // gene the filename
            String fn = UUID.randomUUID().toString() + "." + coverSuffix;
            User user = (User) StatusStoreService.getInstance().get("user");
            progress.setProgress(10);
            if (isCoverModify) {
                ImageService.upload(user.getId(), fn, bitmap, ((isSuccess, path) -> {
                    progress.setProgress(60);
                    if (isSuccess) {
                        if (mPlanet.getId() == null) {

                            mPlanet.setCreatorId(user.getId());
                            mPlanet.setMemberIds(new ArrayList<String>() {{add(user.getId());}});
                        }
                        mPlanet.setCover(path);
                        mPlanet.setName(aName);
                        progress.setProgress(80);
                        ServiceRegister.PlanetServiceInstance.addOrUpdate(mPlanet);
                        progress.setProgress(100);
                        progress.dismiss();
                        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                        isModify = false;
                        isCoverModify = false;
                    } else {
                        progress.setProgress(100);
                        progress.dismiss();
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
                    }

                }));
            } else if (isModify) {
                if (mPlanet.getId() == null) {
                    mPlanet.setCover("");
                    mPlanet.setCreatorId(user.getId());
                    mPlanet.setMemberIds(new ArrayList<String>() {{add(user.getId());}});
                }
                mPlanet.setName(aName);
                progress.setProgress(60);
                ServiceRegister.PlanetServiceInstance.addOrUpdate(mPlanet);
                progress.setProgress(100);
                progress.dismiss();
                Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                isModify = false;
            }
        });
        cover.setOnClickListener(v -> {
            ImageService.showBottomDialog(this, getResources(), "Change Cover", view -> {
                // open the picture of the system
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            });
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isModify = true;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void initView() {
        if (mPlanet.getId() == null) return;

        name.setText(mPlanet.getName());
        String coverUrl = mPlanet.getCover();
        if (!coverUrl.isEmpty()) {
            ImageService.loadStorageImageTo(this, coverUrl, cover);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try {
                Bitmap srcBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Bitmap bitmap = ImageService.zoom(srcBitmap, 600, 600);
                cover.setImageBitmap(bitmap);

                // get the suffix
                String[] type = cr.getType(uri).split("/");
                if (type.length == 2) {
                    coverSuffix = type[1];
                }
                isModify = true;
                isCoverModify = true;
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}