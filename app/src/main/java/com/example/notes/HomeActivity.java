package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.notes.databinding.ActivityHomeBinding;
import com.example.notes.helper.UIHelper;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author yogitad
 * @since 14-08-2022
 * Home Activity - Parent Activity After splash
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Bind activity with its layout
     */
    private ActivityHomeBinding binding;

    /**
     * Back pressed count flag
     */
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        //hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Realm Database configuration
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(getResources().getString(R.string.str_db_name))//database name
                .allowWritesOnUiThread(true)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //call default Home fragment
        UIHelper.getInstance().replaceFragment(HomeActivity.this, DashboardFragment.newInstance(),
                "Dashboard", new Bundle(), R.id.fragment_container,
                false, false);
    }

    /**
     * Method to show message on back pressed
     */
    private void handleAppExitBack() {
        if (doubleBackToExitPressedOnce) {
            HomeActivity.this.finish();
        }
        this.doubleBackToExitPressedOnce = true;
        UIHelper.getInstance().showSnackBar(this, binding.getRoot(), getResources().getString(R.string.back_press));
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onBackPressed() {
        //handle back press fragment wise
        handleAppExitBack();
    }
}