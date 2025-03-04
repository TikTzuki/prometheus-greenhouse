#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <iostream>
#include <algorithm>
#include<ArduinoJson.h>
#define PUMP D2
const char* ssid = "long 2.4G";
const char* password = "Ld201199";
const char* mqtt_server = "192.168.1.3";
WiFiClient espClient;
PubSubClient client(espClient);
unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE  (50)
char msg[MSG_BUFFER_SIZE];
int counter=0;
void setup() {
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  pinMode(PUMP,OUTPUT);
}
void setup_wifi() {

  delay(10);
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
  randomSeed(micros());
  client.subscribe("pump");
}

void callback(char* topic, byte* payload, unsigned int length) {
 String _topic="";
  String message="";
  for (int i = 0; i < length; i++) {
    message+=(char)payload[i];
  }
  _topic=topic;
  client.subscribe(message.c_str());
  message.trim();
  if(message=="1")
  {
    digitalWrite(PUMP,HIGH);
  }
  else if(message=="0")
  {
    digitalWrite(PUMP,LOW);
  }
  _topic+="\n";
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      client.subscribe("ESP8266/4");
      client.subscribe("ESP8266/5");
      client.subscribe("allow");
      client.subscribe("pump");

  } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");      
    }
  }
}
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  Serial.println("yeah");
  
}
