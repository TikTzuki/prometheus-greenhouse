#include <SoftwareSerial.h>
SoftwareSerial S1(2,3);
void setup() {
  Serial.begin(9600);
  S1.begin(4800);
}
void loop()
{
  if(S1.available()>0)
  {
    String message="";
    message=S1.read();
    Serial.println(message+"12");
    Serial.println("hey");
  }
  S1.write("Fuck");
  delay(500);
}
