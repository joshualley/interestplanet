package com.example.interestplanet.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.interestplanet.R;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ImageService;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.firebase.storage.FirebaseStorage;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends Fragment {

    private ImageView userAvatar;
    private Button logoutBtn;
    private TextView username;
    private TextView userDesc;

    private TextView myPlanets;
    private TextView myArticles;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_mine, container, false);

        logoutBtn = fragment.findViewById(R.id.logoutBtn);
        username = fragment.findViewById(R.id.user_name);
        userDesc = fragment.findViewById(R.id.user_desc);
        userAvatar = fragment.findViewById(R.id.user_avatar);
        myPlanets = fragment.findViewById(R.id.my_planets);
        myArticles = fragment.findViewById(R.id.my_articles);

        User user = (User) StatusStoreService.getInstance().get("user");
        if (user != null) {
            username.setText(user.getUsername());
            userDesc.setText(user.getSign());
            // load the user's avatar
            if (user.getAvatarUrl().isEmpty()) {
                userAvatar.setImageResource(R.drawable.no_image);
            } else {
                ImageService.loadStorageImageTo(getContext(), user.getAvatarUrl(), userAvatar);
            }
        }

        onClick();

        return fragment;
    }


    private void onClick() {
        logoutBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            // finish the MainActivity
            getActivity().finish();
        });
        userAvatar.setOnClickListener(v -> {
            ImageService.showBottomDialog(getContext(), getResources(), "Change Avatar", view -> {
                // open the picture of the system
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            });
        });
        myArticles.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyArticlesActivity.class);
            startActivity(intent);
        });
        myPlanets.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyPlanetsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getActivity().getContentResolver();
            Bitmap srcBitmap = null;
            try {
                srcBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmap = ImageService.zoom(srcBitmap, 200, 200);
            // gene a filename
            String[] type = cr.getType(uri).split("/");
            String suffix = "png";
            if (type.length == 2) {
                suffix = type[1];
            }
            String fn = UUID.randomUUID().toString() + "." + suffix;
            // Log.i("filename", fn);

            User user = (User) StatusStoreService.getInstance().get("user");
            // upload the user's avatar
            ImageService.upload(user.getId(), fn, bitmap, ((isSuccess, path) -> {
                if (isSuccess) {
                    userAvatar.setImageBitmap(bitmap);
                    // user.setAvatarUrl(path);
                    // ServiceRegister.UserServiceInstance.addOrUpdate(user);
                    // StatusStoreService.getInstance().set("user", user);
                    Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}