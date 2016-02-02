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

import es.uji.al259348.sliwandroid.core.controller.MainController;
import es.uji.al259348.sliwandroid.core.controller.MainControllerImpl;
import es.uji.al259348.sliwandroid.core.model.User;
import es.uji.al259348.sliwandroid.core.view.MainView;
import es.uji.al259348.sliwandroid.wear.R;
import es.uji.al259348.sliwandroid.wear.fragments.LoginFragment;
import es.uji.al259348.sliwandroid.wear.fragments.LoginFragment2;
import es.uji.al259348.sliwandroid.wear.fragments.MainFragment;

public class MainActivity extends Activity implements
        MainView,
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

        controller = new MainControllerImpl(this);

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
            Log.d("THREAD", "onLogin | " + Thread.currentThread().getName());
            controller.retrieveUserLinked();


//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(fragmentContent.getId(), LoginFragment2.newInstance());
//            transaction.commit();
//            secondLoginShown = true;
        } else if (id.equals("0000")) {
            Toast toast = Toast.makeText(MainActivity.this, "Usuario no válido.", Toast.LENGTH_SHORT);
            //toast.getView().setBackgroundColor(Color.RED);
            toast.show();
        } else {
            Intent i = new Intent(MainActivity.this, ConfigActivity.class);
            startActivityForResult(i, 0);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onUserLinked(User user) {
        Toast.makeText(MainActivity.this, "Hola " + user.getName(), Toast.LENGTH_SHORT).show();
    }
}
