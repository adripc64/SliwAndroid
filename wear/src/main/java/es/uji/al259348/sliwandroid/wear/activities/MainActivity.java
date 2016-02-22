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
import android.widget.Toast;

import es.uji.al259348.sliwandroid.core.controller.MainController;
import es.uji.al259348.sliwandroid.core.controller.MainControllerImpl;
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

    private static final String STEP_LINK = "stepLink";
    private static final String STEP_CONFIG = "StepConfig";
    private static final String STEP_OK = "stepOk";
    private static final int REQUEST_CODE_CONFIG = 1;

    private MainController controller;
    private View fragmentContent;

    private String step;

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
                controller.decideStep();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIG) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(MainActivity.this, "Configuración terminada.", Toast.LENGTH_SHORT).show();
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(MainActivity.this, "Configuración cancelada.", Toast.LENGTH_SHORT).show();
                    break;
            }
            controller.decideStep();
        }
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
    public void hasToLink() {
        step = STEP_LINK;
        setFragment(ConfirmFragment.newInstance("Es necesario vincular el dispositivo.", "Ok"));
    }

    @Override
    public void onUserLinked(User user) {
        Toast.makeText(MainActivity.this, "Usuario vinculado: " + user.getName(), Toast.LENGTH_SHORT).show();
        controller.decideStep();
    }

    @Override
    public void hasToConfigure() {
        step = STEP_CONFIG;
        Log.v("asf", "Asf");
        setFragment(ConfirmFragment.newInstance("Es necesario configurar el dispositivo.", "Ok"));
    }

    @Override
    public void isOk() {
        step = STEP_OK;
        setFragment(MainFragment.newInstance());
    }

    @Override
    public void onConfirm() {
        switch (step) {
            case STEP_LINK:
                setFragment(LoadingFragment.newInstance("Vinculando..."));
                controller.link();
                break;

            case STEP_CONFIG:
                Intent i = new Intent(MainActivity.this, ConfigActivity.class);
                startActivityForResult(i, REQUEST_CODE_CONFIG);
                break;
        }
    }

    @Override
    public void onUnlink() {
        controller.unlink();
        controller.decideStep();
    }

    @Override
    public void takeSample() {
        controller.takeSample();
    }
}
