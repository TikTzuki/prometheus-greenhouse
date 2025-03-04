package tik.prometheus.mobile.services;

import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import tik.prometheus.mobile.Configs;

public class MqttHelper {
    private static final String TAG = MqttHelper.class.toString();

    public static MqttAndroidClient createSensorListener(Context context, String topic, MqttSensorViewHolder viewHolder) {
        String clientId = MqttClient.generateClientId();
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        String url = "tcp://" + Configs.INSTANCE.getConfigs().getBrokerHost() + ":" + Configs.INSTANCE.getConfigs().getBrokerPort();
//        Log.i(TAG, "createSensorListener: " + url);
        MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(context, url, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                viewHolder.updateMqttValue(null, "NaN");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                viewHolder.updateMqttValue(topic, message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connect succeed: " + topic);
                    try {
                        mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
//                                Log.i(TAG, "subscribed succeed: " + topic);
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Log.i(TAG, "connect failed: " + topic);
                    exception.printStackTrace();
                }
            });

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        return mqttAndroidClient;
    }


}
