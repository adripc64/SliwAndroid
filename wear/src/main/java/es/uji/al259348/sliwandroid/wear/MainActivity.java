package es.uji.al259348.sliwandroid.wear;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Toast;

import es.uji.al259348.sliwandroid.core.MqttClient;

public class MainActivity extends Activity implements
        MainFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener {

    private View fragmentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            Toast.makeText(MainActivity.this, "Configuración terminada...", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(MainActivity.this, "Configuración cancelada...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLogin(String id) {
        MqttClient a;

        Intent i = new Intent(MainActivity.this, ConfigActivity.class);
        startActivityForResult(i, 0);
    }
}
