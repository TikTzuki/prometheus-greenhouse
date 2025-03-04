#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "mcu_handle.h"
#include <Wire.h>
const char* ssid = "LongTPT";
const char* password = "12345678";
const char* mqtt_server = "192.168.84.163";
#define MSG_BUFFER_SIZE (50)
char msg[MSG_BUFFER_SIZE];
int value = 0;
#define Pump 14
const int ledGPIO5 = 5;
#define sensorIn A0
const char* Topic = "sensor/0";
String message;
String _topic;
String mess_setup = "";
const char* Toopic = "available";
const char* sensor1 = "sensor/1";
int counter = 0;
int counter1 = 0;
String split_str[5];
char c;
String dataIn;
#define num_seed 5
String uuid[num_seed];
String all_mess[num_seed];
int counter_all = 0;
String object[num_seed];
String mac_address;
WiFiClient espClient;
String actuator_topic = "actuator_available";
PubSubClient client(espClient);
String actuator_payload = "";
String message_payload = "";
unsigned long lastMsg = 0;
int count_reconnect = 0;
void setup() {
  Serial.begin(115200);
  Wire.begin(D1, D2);
  pinMode(Pump, OUTPUT);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  mac_address = WiFi.macAddress();
  object_setup(object);
}
void setup_wifi() {

  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  client.subscribe("ESP8266/4");
  client.subscribe("ESP8266/5");
  client.subscribe("allow");
  for (int i = 0; i < num_seed; i++) {
    String sub = mac_address + "/" + object[i];
    client.subscribe(sub.c_str());
  }
  String actuator_subscribe = mac_address + "/pump";
  Serial.print("actuator topic: ");
  Serial.println(actuator_subscribe);
  client.subscribe(actuator_subscribe.c_str());
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  _topic = "";
  message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  _topic += topic;
  if (_topic == mac_address + "/pump") {
    actuator_payload = message;
    client.subscribe(actuator_payload.c_str());
  } else {
    all_mess[counter_all] = message;
  }
  if (_topic == actuator_payload) {
    if (message == "1") {
      Serial.println(message);
      digitalWrite(Pump, HIGH);
    } else if (message == "0") {
      Serial.println(message);
      digitalWrite(Pump, LOW);
    }
  }


  counter_all += 1;
  Serial.println("done callback");
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (count_reconnect > 15) {
      count_reconnect = 0;
      Serial.println("Let reset it now:");
      ESP.restart();
    }
    count_reconnect++;
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      client.subscribe("ESP8266/4");
      client.subscribe("ESP8266/5");
      client.subscribe("allow");
      for (int i = 0; i < num_seed; i++) {
        String sub = mac_address + "/" + object[i];
        client.subscribe(sub.c_str());
      }
      String actuator_subscribe = mac_address + "/pump";
      client.subscribe(actuator_subscribe.c_str());

    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      ESP.restart();
    }
  }
}

void readData() {
  while (Wire.available() > 0) {
    // Serial.println(Wire.available());
    c = Wire.read();
    if (c == '\n') {
      break;
    } else {
      dataIn += c;
    }
  }
  if (c == '\n') {
    Serial.print("Data In: ");
    Serial.println(dataIn);
    dataIn.trim();
    if (dataIn == "NAN") {
      return;
    } else {
      String* data_receive = splitFullString(dataIn, '+');
      for (int i = 0; i < 4; i++) {
        String data_sensor = data_receive[i];
        String* data_pub = splitString(data_sensor, ":");
        const char* data_send = data_pub[1].c_str();
        const char* send_topic = all_mess[i].c_str();
        Serial.print(send_topic);
        Serial.print(" : ");
        Serial.println(data_send);
        if (data_pub[0] == "W") {
          client.publish(send_topic, data_send);
        } else if (data_pub[0] == "M") {
          client.publish(send_topic, data_send);
        }
        else if(data_pub[0]=="L")
        { 
          client.publish("sensor/10",data_send);
        } 
        else {
          const char* temp = data_pub[0].c_str();
          const char* next_topic=all_mess[i+1].c_str();
          client.publish(send_topic, temp);
          client.publish(next_topic, data_send);
        }
      }
    }
    c = 0;
    dataIn = "";
  }
}

void handleData() {
  readData();
}


void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  if (!Serial) {
    Serial.end();
    delay(100);
    Serial.begin(115200);
  }
  if (counter < 1) {
    Serial.println("publish data");
    for (int i = 0; i < num_seed; i++) {
      mess_setup = mac_address + "/" + object[i];
      client.publish(Toopic, mess_setup.c_str());
      mess_setup = "";
    }
    mess_setup = mac_address + "/pump";
    client.publish(actuator_topic.c_str(), mess_setup.c_str());
    counter++;
  }
  unsigned long now = millis();
  if (now - lastMsg > 3000) {
    lastMsg = now;
    Wire.requestFrom(8, 128);
    handleData();
  }
  
}