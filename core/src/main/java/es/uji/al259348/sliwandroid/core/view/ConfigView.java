package es.uji.al259348.sliwandroid.core.view;

public interface ConfigView extends View {

    void onNextStep(String msg);
    void onStepProgressUpdated(int progress);
    void onConfigFinished();

}
