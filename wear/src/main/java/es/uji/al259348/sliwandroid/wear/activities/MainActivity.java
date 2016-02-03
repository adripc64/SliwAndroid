package es.uji.al259348.sliwandroid.wear.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import es.uji.al259348.sliwandroid.core.controller.MainController;
import es.uji.al259348.sliwandroid.core.controller.MainControllerImpl;
import es.uji.al259348.sliwandroid.core.model.Config;
import es.uji.al259348.sliwandroid.core.model.Location;
import es.uji.al259348.sliwandroid.core.model.User;
import es.uji.al259348.sliwandroid.core.view.MainView;
import es.uji.al259348.sliwandroid.wear.R;
import es.uji.al259348.sliwandroid.wear.fragments.ConfirmFragment;
import es.uji.al259348.sliwandroid.wear.fragments.LoadingFragment;
import es.uji.al259348.sliwandroid.wear.fragments.MainFragment;

public class MainActivity extends Activity implements
        MainView,
        MainFragment.OnFragmentInteractionListener,
        ConfirmFragment.OnFragmentInteractionListener {

    private MainController controller;
    private View fragmentContent;

    private User userLinked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainControllerImpl(this);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                fragmentContent = stub.findViewById(R.id.fragmentContent);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(fragmentContent.getId(), ConfirmFragment.newInstance("Es necesario vincular el dispositivo.", "Ok"));
                transaction.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(fragmentContent.getId(), MainFragment.newInstance());
            transaction.commit();

            Config config = null;
            try {
                config = (new ObjectMapper()).readValue(data.getStringExtra("config"), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("CONFIG", "Config finished!!: " + config);

            Toast.makeText(MainActivity.this, "Configuración terminada.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(MainActivity.this, "Configuración cancelada.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onUserLinked(User user) {
        userLinked = user;

        Toast.makeText(MainActivity.this, "Hola " + user.getName(), Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragmentContent.getId(), ConfirmFragment.newInstance("Es necesario configurar el dispositivo.", "Ok"));
        transaction.commit();

    }

    @Override
    public void onConfirm() {
        if (userLinked == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(fragmentContent.getId(), LoadingFragment.newInstance("Vinculando..."));
            transaction.commit();
            controller.retrieveUserLinked();
        } else {
            Intent i = new Intent(MainActivity.this, ConfigActivity.class);
            try {
                String jsonLocations = (new ObjectMapper()).writeValueAsString(userLinked.getLocations());
                i.putExtra("locations", jsonLocations);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            startActivityForResult(i, 0);
        }
    }
}
