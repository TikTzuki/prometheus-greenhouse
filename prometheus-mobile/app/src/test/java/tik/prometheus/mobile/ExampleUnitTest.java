package tik.prometheus.mobile;

import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMqtt() throws InterruptedException {

        String publisherId = UUID.randomUUID().toString();
        MqttClient client;
        CountDownLatch latch = new CountDownLatch(1);
        try {
            client = new MqttClient("tcp://192.168.1.3:1883", publisherId);
            System.out.print("connected");
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.print("lost");
                    latch.countDown();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.print(topic);
                    System.out.print(new String(message.getPayload()));
                    client.publish("sub", new MqttMessage("zzzzzzz".getBytes()));
                    latch.countDown();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.print("complete");
                }
            });
            MqttConnectOptions mqOptions = new MqttConnectOptions();
            mqOptions.setCleanSession(true);
            client.connect(mqOptions);
            client.publish("sub", new MqttMessage("hi".getBytes()));
            client.subscribe("newSensor");
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        latch.await();
        for (int i = 0; i < 100; i++) {
            System.out.println(client.isConnected());
            try {
                Thread.sleep(1000);
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}