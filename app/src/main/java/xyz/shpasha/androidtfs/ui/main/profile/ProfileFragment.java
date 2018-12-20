package xyz.shpasha.androidtfs.ui.main.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import xyz.shpasha.androidtfs.R;
import xyz.shpasha.androidtfs.model.User;
import xyz.shpasha.androidtfs.source.local.AppDatabase;
import xyz.shpasha.androidtfs.source.remote.ApiService;
import xyz.shpasha.androidtfs.source.remote.DataRepository;
import xyz.shpasha.androidtfs.ui.main.callbacks.ErrorListener;
import xyz.shpasha.androidtfs.viewmodel.ViewModelFactory;

public class ProfileFragment extends Fragment implements TextWatcher {

    private ProfileViewModel profileViewModel;
    private TextView descriptionText, emailText, nameText, secondNameText, midNameText, birthdayText, phoneText, contactCityText, universityText;
    private ImageView photoView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton editButton;
    private User user;
    private ErrorListener errorListener;

    boolean editFlag = false;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public void provideErrorListener(ErrorListener errorListener) { this.errorListener = errorListener; }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        descriptionText = view.findViewById(R.id.descriptionText);
        nameText = view.findViewById(R.id.nameText);
        birthdayText = view.findViewById(R.id.wasbornText);
        secondNameText = view.findViewById(R.id.secondNameText);
        midNameText = view.findViewById(R.id.midNameText);
        photoView = view.findViewById(R.id.photoView);
        emailText = view.findViewById(R.id.emailText);
        phoneText = view.findViewById(R.id.phoneText);
        contactCityText = view.findViewById(R.id.contactCityText);
        universityText = view.findViewById(R.id.universityText);
        editButton = view.findViewById(R.id.editButton);
        editButton.hide();

        swipeRefreshLayout.setOnRefreshListener(() -> profileViewModel.getUser());

        editButton.setOnClickListener(v -> {
            user.setDescription(descriptionText.getText().toString());
            user.setFirstName(nameText.getText().toString());
            user.setBirthday(birthdayText.getText().toString());
            user.setLastName(secondNameText.getText().toString());
            user.setMiddleName(midNameText.getText().toString());
            user.setEmail(emailText.getText().toString());
            user.setPhoneMobile(phoneText.getText().toString());
            user.setRegion(contactCityText.getText().toString());
            user.setUniversity(universityText.getText().toString());
            profileViewModel.edit(user);
        });

        nameText.addTextChangedListener(this);
        birthdayText.addTextChangedListener(this);
        secondNameText.addTextChangedListener(this);
        midNameText.addTextChangedListener(this);
        descriptionText.addTextChangedListener(this);
        emailText.addTextChangedListener(this);
        phoneText.addTextChangedListener(this);
        contactCityText.addTextChangedListener(this);
        universityText.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        if (user == null) {
            profileViewModel.getUser();
        } else {
            loadPhoto();
        }
    }

    private void initViewModel() {

        DataRepository repository = DataRepository.getInstance();
        repository.setDb(AppDatabase.getInstance(getContext()));

        profileViewModel = ViewModelProviders.of(this, new ViewModelFactory(repository)).get(ProfileViewModel.class);
        profileViewModel.userResource().observe(this, userResource -> {
            if (userResource == null) {onError("Unknown error"); return;}
            switch (userResource.status) {
                case SUCCESS: {
                    setUser(userResource.data);
                    onUserSuccess();
                    break;
                }
                case LOADING: {
                    onLoading();
                    break;
                }
                case ERROR: {
                    setUser(userResource.data);
                    onError(userResource.message);
                    break;
                }
            }
        });
        profileViewModel.editResultResource().observe(this, editResultResource -> {
            if (editResultResource == null) {onError("Unknown error"); return;}
            switch (editResultResource.status) {
                case SUCCESS: {
                    onEditSuccess();
                    break;
                }
                case LOADING: {
                    onLoading();
                    break;
                }
                case ERROR: {
                    onError(editResultResource.message);
                    break;
                }
            }
        });
    }

    private void onUserSuccess() {
        swipeRefreshLayout.setRefreshing(false);
    }
    private void onEditSuccess() {
        Toast.makeText(getContext(), "Пользователь отредактирован", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
        editButton.hide();
    }

    private void onLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }
    private void onError(String message) {
        swipeRefreshLayout.setRefreshing(false);
        errorListener.onError(message);
    }

    private void setUser(User user) {
        if (user == null) return;
        this.user = user;

        editFlag = false;
        nameText.setText(user.getFirstName());
        birthdayText.setText(user.getBirthday());
        secondNameText.setText(user.getLastName());
        midNameText.setText(user.getMiddleName());
        descriptionText.setText(user.getDescription());

        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhoneMobile());
        contactCityText.setText(user.getRegion());
        universityText.setText(user.getUniversity());
        editFlag = true;
        loadPhoto();
    }
    private void loadPhoto() {
        String url = ApiService.API_URL + user.getAvatar().substring(1);
        Glide.with(this)
                .load(url)
                .into(photoView);
    }

    @Override
    public void onResume() {
        editButton.hide();
        super.onResume();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (editFlag) editButton.show();
    }
}
