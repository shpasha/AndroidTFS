package xyz.shpasha.androidtfs.ui.main.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.main.callbacks.ErrorListener;
import xyz.shpasha.androidtfs.ui.main.callbacks.LogoutListener;
import xyz.shpasha.androidtfs.viewmodel.ViewModelFactory;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Button signoutButton;
    private ErrorListener errorListener;
    private LogoutListener logoutListener;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        signoutButton = view.findViewById(R.id.signoutButton);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    private void initViewModel() {
        DataRepository repository = DataRepository.getInstance();
        settingsViewModel = ViewModelProviders.of(this, new ViewModelFactory(repository)).get(SettingsViewModel.class);

        signoutButton.setOnClickListener(v -> {
            settingsViewModel.signoutResource().observe(this, signoutResource -> {
                if (signoutResource == null) {
                    onError("Unknown error");
                    return;
                }
                switch (signoutResource.status) {
                    case SUCCESS: {
                        onSuccess();
                        break;
                    }
                    case LOADING: {
                        onLoading();
                        break;
                    }
                    case ERROR: {
                        onError(signoutResource.message);
                        break;
                    }

                }
            });
            settingsViewModel.signout();
        });

    }

    private void onSuccess() {
        logoutListener.onLogout();
        signoutButton.setEnabled(true);
    }
    private void onLoading() {
        signoutButton.setEnabled(false);
    }
    private void onError(String message) {
        signoutButton.setEnabled(true);
        errorListener.onError(message);
    }

    public void provideLogoutListener(LogoutListener logoutListener) { this.logoutListener = logoutListener; }
    public void provideErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}
