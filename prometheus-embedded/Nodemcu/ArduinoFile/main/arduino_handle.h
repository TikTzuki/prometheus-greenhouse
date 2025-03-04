#ifndef ARDUINO_HANDLE.H
#define ARDUINO_HANDLE.H
#include <Arduino.h>

#include<Wire.h>
class Water
{
  private:
         byte SensorIn;
         byte WaterSensor;
  public:
       Water(byte SensorIn,byte WaterSensor);
       int waterLevel();
       String waterTopic();
};

String temp_humidTopic();
class Moisture
{
  private:
        byte MoistureSensor;
  public:
       Moisture(byte MoistureSensor);
       String moistureTopic();
};

class Light
{
  private:
         byte LightSensor;
  public:
        Light(byte LightSensor);
        String lightTopic();
};
class RGB
{private:
      byte R;
      byte G;
      byte B;
 public:
      RGB(byte R,byte G,byte B);
      void rgbColor(int r,int g,int b);
};
void pumpWater(byte SensorInput,String order);
String splitString(String s,String deli);
#endif
