package xyz.shpasha.androidtfs.ui.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import xyz.shpasha.androidtfs.R;

public class StateFragment extends Fragment {

    private TextView errorView;
    private ProgressBar loadingBar;
    private ImageView imageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.state_fragment, container, false);
        loadingBar = view.findViewById(R.id.loadingBar);
        errorView = view.findViewById(R.id.errorView);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static StateFragment newInstance() {
        return new StateFragment();
    }

    public void setLoading(boolean loading) {
        errorView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        if (loading)
            loadingBar.setVisibility(View.VISIBLE);
        else
            loadingBar.setVisibility(View.GONE);
    }

    public void setError(String message) {
        loadingBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        errorView.setText(message);
    }
}
