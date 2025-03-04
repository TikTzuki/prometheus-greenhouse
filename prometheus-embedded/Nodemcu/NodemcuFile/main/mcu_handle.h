#ifndef MCU_HANDLE.H
#define MCU_HANDLE.H
#include <Arduino.h>
#define ARRAY_SIZE(x) sizeof(x)/sizeof(x[0])
String *splitString(String s,String deli);
String* splitFullString(String s,char delimeter);
String uuid4();
String hex_print(int n,int mn ,int mx);
void object_setup(String object[]);
void pumpWater(byte SensorInput,String order);
#endif
