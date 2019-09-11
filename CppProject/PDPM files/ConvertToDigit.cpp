#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "ConvertToDigit.h"

int ConversionFunctionTwoDigit(char* RetrievedString)
{int convertednumber = 0;
if(0 == strcmp(RetrievedString, "00") || 0 == strcmp(RetrievedString, "0 " ) || 0 == strcmp(RetrievedString, " 0" ))
{
convertednumber = 0;
}
else if (0 == strcmp(RetrievedString, "01")|| 0 == strcmp(RetrievedString, " 1" )|| 0 == strcmp(RetrievedString, "1 "))
{
convertednumber = 1;
}
else if (0 == strcmp(RetrievedString, "02")|| 0 == strcmp(RetrievedString, " 2" )|| 0 == strcmp(RetrievedString, "2 "))
{
convertednumber = 2;
}
else if (0 == strcmp(RetrievedString, "03")|| 0 == strcmp(RetrievedString, " 3" )|| 0 == strcmp(RetrievedString, "3 "))
{
convertednumber = 3;
}
else if (0 == strcmp(RetrievedString, "04")|| 0 == strcmp(RetrievedString, " 4" )|| 0 == strcmp(RetrievedString, "4 "))
{
convertednumber = 4;
}
else if (0 == strcmp(RetrievedString, "05")|| 0 == strcmp(RetrievedString, " 5" )|| 0 == strcmp(RetrievedString, "5 "))
{
convertednumber = 5;
}
else if (0 == strcmp(RetrievedString, "06")|| 0 == strcmp(RetrievedString, " 6" )|| 0 == strcmp(RetrievedString, "6 "))
{
convertednumber = 6;
}
else if (0 == strcmp(RetrievedString, "07")|| 0 == strcmp(RetrievedString, " 7" )|| 0 == strcmp(RetrievedString, "7 "))
{
convertednumber = 7;
}
else if (0 == strcmp(RetrievedString, "08")|| 0 == strcmp(RetrievedString, " 8" )|| 0 == strcmp(RetrievedString, "8 "))
{
convertednumber = 8;
}
else if (0 == strcmp(RetrievedString, "09")|| 0 == strcmp(RetrievedString, " 9" )|| 0 == strcmp(RetrievedString, "9 "))
{
convertednumber = 9;
}
else if (0 == strcmp(RetrievedString, "10"))
{
convertednumber = 10;
}
else if (0 == strcmp(RetrievedString, "11"))
{
convertednumber = 11;
}
else if (0 == strcmp(RetrievedString, "12"))
{
convertednumber = 12;
}
else if (0 == strcmp(RetrievedString, "13"))
{
convertednumber = 13;
}
else if (0 == strcmp(RetrievedString, "14"))
{
convertednumber = 14;
}
else if (0 == strcmp(RetrievedString, "15"))
{
convertednumber = 15;
}
else if (0 == strcmp(RetrievedString, "16"))
{
convertednumber = 16;
}
else if (0 == strcmp(RetrievedString, "17"))
{
convertednumber = 17;
}
else if (0 == strcmp(RetrievedString, "18"))
{
convertednumber = 18;
}
else if (0 == strcmp(RetrievedString, "19"))
{
convertednumber = 19;
}
else if (0 == strcmp(RetrievedString, "20"))
{
convertednumber = 20;
}
else if (0 == strcmp(RetrievedString, "21"))
{
convertednumber = 21;
}
else if (0 == strcmp(RetrievedString, "22"))
{
convertednumber = 22;
}
else if (0 == strcmp(RetrievedString, "23"))
{
convertednumber = 23;
}
else if (0 == strcmp(RetrievedString, "24"))
{
convertednumber = 24;
}
else if (0 == strcmp(RetrievedString, "25"))
{
convertednumber = 25;
}
else if (0 == strcmp(RetrievedString, "26"))
{
convertednumber = 26;
}
else if (0 == strcmp(RetrievedString, "27"))
{
convertednumber = 27;
}
else if (0 == strcmp(RetrievedString, "28"))
{
convertednumber = 28;
}
else if (0 == strcmp(RetrievedString, "29"))
{
convertednumber = 29;
}
else if (0 == strcmp(RetrievedString, "30"))
{
convertednumber = 30;
}
else if (0 == strcmp(RetrievedString, "99"))
{
convertednumber = 99;
}


return convertednumber;
}

int ConversionFunctionOneDigit(char* RetrievedString)
{int convertednumber = 0;
if(0 == strcmp(RetrievedString, "0" ))
{
convertednumber = 0;
}
else if (0 == strcmp(RetrievedString, "1"))
{
convertednumber = 1;
}
else if (0 == strcmp(RetrievedString, "2"))
{
convertednumber = 2;
}
else if (0 == strcmp(RetrievedString, "3"))
{
convertednumber = 3;
}
else if (0 == strcmp(RetrievedString, "4"))
{
convertednumber = 4;
}
else if (0 == strcmp(RetrievedString, "5"))
{
convertednumber = 5;
}
else if (0 == strcmp(RetrievedString, "6"))
{
convertednumber = 6;
}
else if (0 == strcmp(RetrievedString, "7"))
{
convertednumber = 7;
}
else if (0 == strcmp(RetrievedString, "8"))
{
convertednumber = 8;
}
else if (0 == strcmp(RetrievedString, "9"))
{
convertednumber = 9;
}
else
{
convertednumber = 0;
}
return convertednumber;
}