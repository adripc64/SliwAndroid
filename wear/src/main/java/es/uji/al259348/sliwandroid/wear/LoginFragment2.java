package es.uji.al259348.sliwandroid.wear;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment2 extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LoginFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment2 newInstance() {
        return new LoginFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login2, container, false);

        final Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinner1);
        final Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinner2);
        final Spinner sp3 = (Spinner) rootView.findViewById(R.id.spinner3);
        final Spinner sp4 = (Spinner) rootView.findViewById(R.id.spinner4);
        Button btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLogin(
                            sp1.getSelectedItem().toString() +
                            sp2.getSelectedItem().toString() +
                            sp3.getSelectedItem().toString() +
                            sp4.getSelectedItem().toString()
                    );
                }
            }
        });

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
        void onLogin(String id);
    }
}
