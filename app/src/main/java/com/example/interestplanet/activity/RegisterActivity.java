package com.example.interestplanet.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ServiceRegister;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText passwordConfirmEt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);

        usernameEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        passwordConfirmEt = findViewById(R.id.passwordConfirm);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> {
            String uname = usernameEt.getText().toString();
            String pwd = passwordEt.getText().toString();
            String pwdConfirm = passwordConfirmEt.getText().toString();
            // check the empty input
            if (uname.equals("") || pwd.equals("") || pwdConfirm.equals("")) {
                Toast.makeText(this,
                        "The username, password or confirm password cannot be empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pwd.equals(pwdConfirm)) {
                Toast.makeText(this,
                        "The password is not equal to the confirm password!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            ServiceRegister.UserServiceInstance.findByUsername(uname, u -> {
                if (u != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "The username has been existed!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // add a new user
                    String encryptedPwd = ServiceRegister.UserServiceInstance.encryptPwd(pwd);
                    User user = new User(uname, encryptedPwd, "", "");
                    ServiceRegister.UserServiceInstance.addOrUpdate(user);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Register successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}