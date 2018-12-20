package xyz.shpasha.androidtfs.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.auth.AuthActivity;
import xyz.shpasha.androidtfs.ui.main.callbacks.ErrorListener;
import xyz.shpasha.androidtfs.ui.main.callbacks.LogoutListener;
import xyz.shpasha.androidtfs.ui.main.mycourse.MyCourseFragment;
import xyz.shpasha.androidtfs.ui.main.profile.ProfileFragment;
import xyz.shpasha.androidtfs.ui.main.settings.SettingsFragment;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ErrorListener, LogoutListener {

    private final int ACTIVITY_AUTH = 123;
    private final String CURRENT_FRAGMENT = "CUR_FRAG";

    private BottomNavigationView bottomNavigationView;
    private ProfileFragment profileFragment;
    private MyCourseFragment myCourseFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!isAuthenticated()) {
            startActivityForResult(new Intent(this, AuthActivity.class), ACTIVITY_AUTH);
            finish();
        } else {
            Set<String> cookies = getSharedPreferences("params", MODE_PRIVATE).getStringSet("cookies", new HashSet<>());
            DataRepository.getInstance().setCookies(cookies);
        }

        initFragments();
        initViews();

        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(CURRENT_FRAGMENT);
            showFragById(id);
        } else {
            showProfile();
        }

    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    showFragById(item.getItemId());
                    return true;
                }
        );
    }

    private void initFragments() {
        profileFragment = ProfileFragment.newInstance();
        myCourseFragment = MyCourseFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();

        profileFragment.provideErrorListener(this);
        myCourseFragment.provideErrorListener(this);
        settingsFragment.provideErrorListener(this);
        settingsFragment.provideLogoutListener(this);
    }
    private void showFragById(int id) {
        switch (id) {
            case R.id.action_profile:
                showProfile();
                break;
            case R.id.action_course:
                showMyCourse();
                break;
            case R.id.action_settings:
                showSettings();
                break;
        }
    }

    private void showMyCourse() {
        setTitle("Мой курс");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, myCourseFragment)
                .commit();
    }
    private void showProfile() {
        setTitle("Профиль");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, profileFragment)
                .commit();
    }
    private void showSettings() {
        setTitle("Настройки");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, settingsFragment)
                .commit();
    }

    private boolean isAuthenticated() {
        return getSharedPreferences("params", Context.MODE_PRIVATE).contains("cookies");
    }
    private void onAuthenticated(Integer resultCode) {
        switch (resultCode) {
            case Activity.RESULT_CANCELED: {
                finish();
                break;
            }
            case Activity.RESULT_OK: {
                recreate();
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, bottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ACTIVITY_AUTH) {
            onAuthenticated(resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onError(String message) {
        if (message.contains("auth")) {
            onLogout();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogout() {
        getSharedPreferences("params", Context.MODE_PRIVATE).edit().remove("cookies").apply();
        recreate();
    }
}
