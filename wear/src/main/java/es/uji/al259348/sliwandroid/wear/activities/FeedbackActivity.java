package es.uji.al259348.sliwandroid.wear.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

import es.uji.al259348.sliwandroid.wear.R;
import es.uji.al259348.sliwandroid.wear.fragments.FeedbackFragment;

public class FeedbackActivity extends Activity implements FeedbackFragment.OnFragmentInteractionListener {

    private View fragmentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                fragmentContent = stub.findViewById(R.id.fragmentContent);
                setFragment(FeedbackFragment.newInstance("a", "b"));
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragmentContent.getId(), fragment);
        transaction.commit();
    }

}
