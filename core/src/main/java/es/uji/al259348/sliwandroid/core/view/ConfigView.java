package es.uji.al259348.sliwandroid.core.view;

import android.content.Context;

public interface ConfigView {

    Context getContext();

    void onNextStep(String msg);
    void onStepProgressUpdated(int progress);
    void onConfigFinished();

}
