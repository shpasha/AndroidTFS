package xyz.shpasha.androidtfs.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.auth.AuthViewModel;
import xyz.shpasha.androidtfs.ui.main.mycourse.MyCourseFragment;
import xyz.shpasha.androidtfs.ui.main.mycourse.MyCourseViewModel;
import xyz.shpasha.androidtfs.ui.main.profile.ProfileViewModel;
import xyz.shpasha.androidtfs.ui.main.settings.SettingsViewModel;
import xyz.shpasha.androidtfs.ui.test.TestViewModel;


public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private DataRepository dataRepository;

    public ViewModelFactory(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AuthViewModel.class) {
            return (T) new AuthViewModel(dataRepository);
        }
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel(dataRepository);
        }
        if (modelClass == MyCourseViewModel.class) {
            return (T) new MyCourseViewModel(dataRepository);
        }
        if (modelClass == TestViewModel.class) {
            return (T) new TestViewModel(dataRepository);
        }
        if (modelClass == SettingsViewModel.class) {
            return (T) new SettingsViewModel(dataRepository);
        }
        return null;
    }
}

