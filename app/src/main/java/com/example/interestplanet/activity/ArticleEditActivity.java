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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ImageService;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ArticleEditActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private ImageButton saveBtn;
    private TextView title;
    private TextView content;
    private ImageView cover;

    private String planetId;
    private Article mArticle;
    private String coverSuffix = "png";
    private boolean isModify = false;
    private boolean isCoverModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);

        // try to get the article
        Intent intent = getIntent();
        planetId = intent.getStringExtra("planetId");
        String articleStr = intent.getStringExtra("article");
        if (articleStr == null) {
            mArticle = new Article();
        } else {
            mArticle = new Gson().fromJson(articleStr, Article.class);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (isModify) {
                // If there is a change, show an exit notification
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Hint");
                builder.setMessage("You haven't saved the article yet. Are you sure to exit?");
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

        saveBtn = findViewById(R.id.save_btn);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        cover = findViewById(R.id.cover);

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

    public void initView() {
        if (mArticle.getId() == null) return;

        title.setText(mArticle.getTitle());
        content.setText(mArticle.getContent());
        String coverUrl = mArticle.getCover();
        if (!coverUrl.isEmpty()) {
            ImageService.loadStorageImageTo(this, coverUrl, cover);
        }
    }

    private void setListener() {
        saveBtn.setOnClickListener(v -> {
            progress.show();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // get data
            String aTitle = title.getText().toString();
            String aContent = content.getText().toString();
            Bitmap bitmap = ((BitmapDrawable)cover.getDrawable()).getBitmap();
            // gene the filename
            String fn = UUID.randomUUID().toString() + "." + coverSuffix;
            User user = (User) StatusStoreService.getInstance().get("user");
            progress.setProgress(10);
            if (isCoverModify) {
                // upload the cover and save the article
                ImageService.upload(user.getId(), fn, bitmap, ((isSuccess, path) -> {
                    progress.setProgress(60);
                    if (isSuccess) {
                        mArticle.setCover(path);
                        mArticle.setTitle(aTitle);
                        mArticle.setContent(aContent);
                        if (mArticle.getId() == null) {
                            String now = formatter.format(new Date(System.currentTimeMillis()));
                            mArticle.setDate(now);
                            mArticle.setPlanetId(planetId);
                            mArticle.setAuthorId(user.getId());
                        }
                        progress.setProgress(80);
                        ServiceRegister.ArticleServiceInstance.addOrUpdate(mArticle);
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
                mArticle.setTitle(aTitle);
                mArticle.setContent(aContent);
                if (mArticle.getId() == null) {
                    String now = formatter.format(new Date(System.currentTimeMillis()));
                    mArticle.setDate(now);
                    mArticle.setCover("");
                    mArticle.setPlanetId(planetId);
                    mArticle.setAuthorId(user.getId());
                }
                progress.setProgress(60);
                ServiceRegister.ArticleServiceInstance.addOrUpdate(mArticle);
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
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isModify = true;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isModify = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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