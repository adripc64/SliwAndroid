package es.uji.al259348.sliwandroid.wear;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigProgressBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigProgressBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigProgressBarFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final TextView tvProgress = (TextView) rootView.findViewById(R.id.tvProgress);

        progressBar.setProgress(0);
        tvProgress.setText("0 %");

        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){

                int progress = 0;
                int max = 100;
                int step = 10;

                while (progress < max) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progress += step;

                    final int p = progress;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(p);
                            tvProgress.setText(String.valueOf(p) + " %");
                        }
                    });
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onConfigStepFinish();
                    }
                });

            }
        });
        t.start();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onConfigStepFinish();
    }
}
