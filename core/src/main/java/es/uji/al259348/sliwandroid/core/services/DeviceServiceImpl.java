package es.uji.al259348.sliwandroid.core.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

import es.uji.al259348.sliwandroid.core.model.Device;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DeviceServiceImpl extends AbstractService implements DeviceService {

    private static final String SHARED_PREFERENCES_FILENAME = "DeviceServiceSharedPreferences";
    private static final String SHARED_PREFERENCES_KEY_DEVICE_ID = "deviceId";
    private static final String SHARED_PREFERENCES_KEY_CURRENT_DEVICE = "currentDevice";

    private static final String MESSAGING_REGISTER_REQUEST_TOPIC = "devices/register";
    private static final String MESSAGING_REGISTER_RESPONSE_OK = "200 OK";

    private MessagingService messagingService;

    private ObjectMapper objectMapper;
    private SharedPreferences sharedPreferences;

    private String id;

    public DeviceServiceImpl(Context context) {
        super(context);
        this.messagingService = new MessagingServiceImpl(context);
        this.objectMapper = new ObjectMapper();
        this.sharedPreferences = getSharedPreferences();
    }

    public DeviceServiceImpl(Context context, MessagingService messagingService) {
        super(context);
        this.messagingService = messagingService;
        this.objectMapper = new ObjectMapper();
        this.sharedPreferences = getSharedPreferences();
    }

    private SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
    }

    @Override
    public String getId() {
        if (id == null || id.isEmpty()) {
            id = sharedPreferences.getString(SHARED_PREFERENCES_KEY_DEVICE_ID, "");

            if (id.isEmpty()) {
                id = UUID.randomUUID().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHARED_PREFERENCES_KEY_DEVICE_ID, id);
                editor.apply();
            }
        }

        return id;
    }

    @Override
    public Device getCurrentDevice() {
        Device device = null;

        if (sharedPreferences.contains(SHARED_PREFERENCES_KEY_CURRENT_DEVICE)) {
            String value = sharedPreferences.getString(SHARED_PREFERENCES_KEY_CURRENT_DEVICE, "");
            if (!value.isEmpty()) {
                try {
                    device = objectMapper.readValue(value, Device.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return device;
    }

    @Override
    public boolean setCurrentDevice(Device device) {
        String value = null;

        if (device != null) {
            try {
                value = objectMapper.writeValueAsString(device);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_KEY_CURRENT_DEVICE, value);
        return editor.commit();
    }

    @Override
    public boolean isCurrentDeviceRegistered() {
        return getCurrentDevice() != null;
    }

    @Override
    public Observable<Device> registerCurrentDevice() {
        return Observable.create(subscriber -> {

            String id = UUID.randomUUID().toString();

            Device device = new Device();
            device.setId(id);

            String msg = "";
            try {
                msg = objectMapper.writeValueAsString(device);
            } catch (JsonProcessingException e) {
                subscriber.onError(e);
            }

            messagingService.request(MESSAGING_REGISTER_REQUEST_TOPIC, msg)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe(response -> {
                        if (response.equals(MESSAGING_REGISTER_RESPONSE_OK)) {
                            setCurrentDevice(device);
                            subscriber.onNext(device);
                        } else {
                            subscriber.onError(new Throwable(response));
                        }
                    }, subscriber::onError);
        });
    }

    @Override
    public void onDestroy() {

    }

}
