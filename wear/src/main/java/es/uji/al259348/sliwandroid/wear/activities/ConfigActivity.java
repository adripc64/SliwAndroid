package es.uji.al259348.sliwandroid.wear.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import es.uji.al259348.sliwandroid.core.controller.ConfigController;
import es.uji.al259348.sliwandroid.core.controller.ConfigControllerImpl;
import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.Location;
import es.uji.al259348.sliwandroid.core.model.User;
import es.uji.al259348.sliwandroid.core.view.ConfigView;
import es.uji.al259348.sliwandroid.wear.R;
import es.uji.al259348.sliwandroid.wear.fragments.ConfigProgressBarFragment;
import es.uji.al259348.sliwandroid.wear.fragments.ConfigStartFragment;
import es.uji.al259348.sliwandroid.wear.fragments.ConfigStepFragment;

public class ConfigActivity extends Activity implements
        ConfigView,
        ConfigStartFragment.OnFragmentInteractionListener,
        ConfigStepFragment.OnFragmentInteractionListener {

    private ConfigController configController;

    private View fragmentContent;
    private ConfigProgressBarFragment configProgressBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        try {
            User user = (new ObjectMapper()).readValue(getIntent().getStringExtra("user"), User.class);
            configController = new ConfigControllerImpl(this, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                fragmentContent = stub.findViewById(R.id.fragmentContent);
                setFragment(ConfigStartFragment.newInstance(""));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        configController.onDestroy();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragmentContent.getId(), fragment);
        transaction.commit();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onNextStep(String msg) {
        setFragment(ConfigStepFragment.newInstance(msg));
    }

    @Override
    public void onStepProgressUpdated(int progress) {
        if (configProgressBarFragment != null) {
            configProgressBarFragment.updateProgress(progress);
        }
    }

    @Override
    public void onConfigFinished() {
        Intent i = getIntent();
        i.putExtra("config", "");
        setResult(0, i);
        finish();
    }

    @Override
    public void onConfigStart() {
        configController.startConfig();
    }

    @Override
    public void onConfigStepStart() {
        configProgressBarFragment = ConfigProgressBarFragment.newInstance();
        setFragment(configProgressBarFragment);
        configController.startStep();
    }

}
