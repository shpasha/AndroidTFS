package xyz.shpasha.androidtfs.ui.auth;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.main.MainActivity;
import xyz.shpasha.androidtfs.viewmodel.ViewModelFactory;

import java.util.Set;

public class AuthActivity extends AppCompatActivity {

    private TextView passwordView, emailView;
    private ProgressBar progressBar;
    private Button signinButton;
    private String signinText;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();
        initViewModel();
    }

    private void initViewModel() {
        DataRepository dataRepository = DataRepository.getInstance();
        authViewModel = ViewModelProviders.of(this, new ViewModelFactory(dataRepository)).get(AuthViewModel.class);
        authViewModel.authDetails().observe(this, authDetail -> {
            if (authDetail == null) {
                onError("Unknown error");
                return;
            }
            switch (authDetail.status) {
                case SUCCESS: {
                    if (authDetail.data != null) {
                        saveCookies(authDetail.data);
                        onSuccess();
                    } else {
                        onError("Не удалось получитиь Cookies");
                    }
                    break;
                }
                case LOADING: {
                    onLoading();
                    break;
                }
                case ERROR: {
                    onError(authDetail.message);
                    break;
                }

            }
        });
    }

    private void saveCookies(Set<String> cookies) {
        getSharedPreferences("params", Context.MODE_PRIVATE).edit()
                .putStringSet("cookies", cookies)
                .apply();
    }

    private void initViews() {
        passwordView = findViewById(R.id.passwordView);
        emailView = findViewById(R.id.emailView);
        progressBar = findViewById(R.id.progressBar);
        signinButton = findViewById(R.id.signinButton);

        signinButton.setOnClickListener(v -> {
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();
            authViewModel.signIn(email, password);
        });
    }

    private void onSuccess() {
        setResult(Activity.RESULT_OK);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onLoading() {
        signinText = signinButton.getText().toString();
        signinButton.setText("");
        signinButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        signinButton.setEnabled(true);
        signinButton.setText(signinText);
        progressBar.setVisibility(View.GONE);
    }
}
