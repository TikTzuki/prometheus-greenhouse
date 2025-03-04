#include "mcu_handle.h"
String* splitString(String s,String deli)
{
  int from=s.indexOf(deli);
  String* res=new String[2];
 String result="";
 String type="";
 for(int i=0;i<from;i++)
 {
  type+=s[i];
 }
 for(int i=from+1;i<s.length();i++)
 {
  result+=s[i];
 }
 res[0]=result;
 res[1]=type;
 return res;
}

String* splitFullString(String s,char delimeter)
{
  String* res=new String[10];
  String temp="";
  int c=0;
  for(int i=0;i<s.length();i++)
  {
    if(s[i]==delimeter)
    {
      res[c++]=temp;
      temp="";
    }
   else
   {
    temp+=s[i];
   }
  }
  res[c+1]=temp;
  return res;
}
String uuid4()
{
  String res="";
  res+=hex_print(8,0,15);
  res+="-";
  res+=hex_print(4,0,15);
  res+="-";
  res+="4";
  res+=hex_print(3,0,15);
  res+="-";
  res+=hex_print(1,8,11);
  res+=hex_print(3,0,15);
  res+="-";
  res+=hex_print(12,0,15);
  return res;
}

String hex_print(int n,int mn ,int mx)
{
  String res="";
for(;n>0;n--){
int X = random(mn,mx);
String re=String( X ,HEX );
res+=re;
}
return res;
}

void pumpWater(byte SensorInput,String order)
{ 
  if(order=="1")
  {
    digitalWrite(SensorInput,HIGH);
  }
  else
  {
    digitalWrite(SensorInput,LOW);
  }
}

void object_setup(String object[])
{
  object[0]="Water";
  object[1]="Moisture";
  object[2]="Humidity";
  object[3]="Temperature";
  object[4]="";
}
