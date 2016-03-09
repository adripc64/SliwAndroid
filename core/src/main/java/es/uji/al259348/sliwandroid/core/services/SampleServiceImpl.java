package es.uji.al259348.sliwandroid.core.services;

import android.content.Context;
import android.util.Log;

import es.uji.al259348.sliwandroid.core.model.Sample;
import es.uji.al259348.sliwandroid.core.repositories.sqlite.SQLiteSampleRepository;

public class SampleServiceImpl extends AbstractService implements SampleService {

    private WifiService wifiService;
    private MessagingService messagingService;

    private SQLiteSampleRepository sampleRepository;

    public SampleServiceImpl(Context context) {
        super(context);
        this.wifiService = new WifiServiceImpl(context);
        this.messagingService = new MessagingServiceImpl(context);
        this.sampleRepository = new SQLiteSampleRepository(context);
    }

    @Override
    public void onDestroy() {
        this.sampleRepository.onDestroy();
    }

    @Override
    public Sample save(Sample sample) {

        Log.d("SampleService", "Saving the sample: " + sample + " ...");
        sampleRepository.save(sample);

        return sample;
    }

}
