package es.uji.al259348.sliwandroid.wear;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Toast;

import es.uji.al259348.sliwandroid.core.MqttClient;
import es.uji.al259348.sliwandroid.core.controller.MainController;
import es.uji.al259348.sliwandroid.core.services.ResponseListener;

public class MainActivity extends Activity implements
        MainFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        LoginFragment2.OnFragmentInteractionListener {

    private MainController controller;
    private View fragmentContent;
    private boolean secondLoginShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController(this);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                fragmentContent = stub.findViewById(R.id.fragmentContent);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(fragmentContent.getId(), LoginFragment.newInstance());
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

            Toast.makeText(MainActivity.this, "Configuración terminada.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(MainActivity.this, "Configuración cancelada.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLogin(String id) {
        if (!secondLoginShown) {
//            controller.requestUserLinkedTo(new ResponseListener() {
//                @Override
//                public void onResponse(String msg) {
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                }
//            });


            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(fragmentContent.getId(), LoginFragment2.newInstance());
            transaction.commit();
            secondLoginShown = true;
        } else if (id.equals("0000")) {
            Toast toast = Toast.makeText(MainActivity.this, "Usuario no válido.", Toast.LENGTH_SHORT);
            //toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else {
            Intent i = new Intent(MainActivity.this, ConfigActivity.class);
            startActivityForResult(i, 0);
        }
    }
}
