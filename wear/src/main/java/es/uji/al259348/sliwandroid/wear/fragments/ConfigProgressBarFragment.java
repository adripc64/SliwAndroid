package es.uji.al259348.sliwandroid.wear.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import es.uji.al259348.sliwandroid.wear.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigProgressBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigProgressBarFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvProgress;

    public ConfigProgressBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfigProgressBarFragment.
     */
    public static ConfigProgressBarFragment newInstance() {
        return new ConfigProgressBarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config_progress_bar, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        tvProgress = (TextView) rootView.findViewById(R.id.tvProgress);

        updateProgress(0);

        return rootView;
    }

    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
        tvProgress.setText(String.valueOf(progress) + " %");
    }

}
