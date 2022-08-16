package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notes.databinding.ActivityLoginBinding;
import com.example.notes.helper.UIHelper;

/**
 * @author yogitad
 * @since 14-08-2022
 * Login screen activity
 **/
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Bind activity with its layout
     */
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setupViews();
    }

    /**
     * Method used for views setup and initialization
     */
    private void setupViews() {
        //click listeners
        binding.btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            if (validEmail()) {
                //here we have to cross check valid credentials once user enter correct details then show dashboard flow else error
                if (binding.etEmailId.getText().toString().equals("derleyogita@gmail.com") && binding.etPassword.getText().toString().equals("12345")) {
                    //call home activity
                    navigateToHomeActivity();
                } else {
                    Toast.makeText(this, getString(R.string.str_invalid_credentials), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Method used to navigate to login activity
     */
    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * on button click validate email field
     *
     * @return true if validation is ok else false
     */
    private boolean validEmail() {
        boolean isValidate = false;
        if (UIHelper.getInstance().isValidEmail(binding.etEmailId.getText().toString())) {
            isValidate = true;
        } else {
            //for now showing error in toast message ideally show-case prompt or line errors
            Toast.makeText(this, R.string.str_enter_valid_email, Toast.LENGTH_SHORT).show();
        }
        return isValidate;
    }
}