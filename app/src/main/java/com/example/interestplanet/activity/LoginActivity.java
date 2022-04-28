package com.example.interestplanet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import com.example.interestplanet.R;
import com.example.interestplanet.service.DataInitService;
import com.example.interestplanet.service.SQLiteRecordService;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private Button registerBtnTv;

    private SwitchCompat isRememberPwd;

    SQLiteRecordService mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtnTv = findViewById(R.id.registerBtnTv);
        isRememberPwd = findViewById(R.id.is_remember_pwd);

        onClick();

        mDB = new SQLiteRecordService(this);
        loadUserLoginRecord();
        //DataInitService.Init();
    }

    private void loadUserLoginRecord() {
        SQLiteRecordService.UserRecord record = mDB.getLastOne();
        if (record == null) return;
        usernameEt.setText(record.username);
        passwordEt.setText(record.password);
    }

    private void onClick() {
        loginBtn.setOnClickListener(v -> {
            String uname = usernameEt.getText().toString();
            String pwd = passwordEt.getText().toString();

            // check the empty input
            if (uname.equals("") || pwd.equals("")) {
                Toast.makeText(this, "The username or password cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            ServiceRegister.UserServiceInstance
                    .findByUsername(uname, u -> {
                if (u == null) {
                    Toast.makeText(LoginActivity.this, "Username doesn't existed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String encryptPwd = ServiceRegister.UserServiceInstance.encryptPwd(pwd);
                if (!encryptPwd.equals(u.getPassword())) {
                    Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                } else {
                    if (isRememberPwd.isChecked()) {
                        // record the username and password
                        mDB.addOrUpdate(uname, pwd);
                    } else {
                        mDB.deleteByUsername(uname);
                    }

                    // save the login user's information
                    StatusStoreService.getInstance().push("user", u);
                    // start the main activity
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        });
        registerBtnTv.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
        passwordEt.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            // Carry the user password along with the username
            String uname = usernameEt.getText().toString();
            SQLiteRecordService.UserRecord record = mDB.getByUsername(uname);
            if (record == null) return;
            passwordEt.setText(record.password);
        });
    }
}