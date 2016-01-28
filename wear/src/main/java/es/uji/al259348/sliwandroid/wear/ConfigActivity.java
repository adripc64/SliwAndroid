package es.uji.al259348.sliwandroid.wear;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

import es.uji.al259348.sliwandroid.core.controller.ConfigController;
import es.uji.al259348.sliwandroid.core.model.Config;

public class ConfigActivity extends Activity implements
        ConfigStartFragment.OnFragmentInteractionListener,
        ConfigStepFragment.OnFragmentInteractionListener,
        ConfigProgressBarFragment.OnFragmentInteractionListener {

    private ConfigController configController;

    private View fragmentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        configController = new ConfigController();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                fragmentContent = stub.findViewById(R.id.fragmentContent);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(fragmentContent.getId(), ConfigStartFragment.newInstance(""));
                transaction.commit();
            }
        });
    }

    @Override
    public void onConfigStart() {
        Config.startConfig();
        if (!Config.hasFinished()) {
            String msg = Config.getCurrentStepMsg();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(fragmentContent.getId(), ConfigStepFragment.newInstance(msg));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onConfigStepStart() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragmentContent.getId(), ConfigProgressBarFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onConfigStepFinish() {
        Config.next();
        if (!Config.hasFinished()) {
            String msg = Config.getCurrentStepMsg();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(fragmentContent.getId(), ConfigStepFragment.newInstance(msg));
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Intent i = getIntent();
            i.putExtra("RESULTADO", "asdf");
            setResult(0, i);
            finish();
        }
    }
}
