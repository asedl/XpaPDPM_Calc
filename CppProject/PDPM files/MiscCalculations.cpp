#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "MiscCalculations.h"

int RoundNumber(float numbertoround)
{int length;
 char tempstorage[8];
 char roundedresult[8];
 char afterdecimal[2];
 int beforedecimal;

 sprintf(tempstorage, "%0.3f", numbertoround);
 length = strlen(tempstorage);
 strncpy(roundedresult, tempstorage,length-4);
 roundedresult[length-4]='\0';

 afterdecimal[0] = tempstorage[length-3];
 afterdecimal[1] = '\0';
 beforedecimal = atoi(roundedresult);

 if (atoi(afterdecimal) >= 5)
 {beforedecimal = beforedecimal+1;
 }
 else 
 {beforedecimal = beforedecimal;
 }
 return beforedecimal;
}

float AverageTwoNumbers(float PassNum1, float PassNum2)
{float AverageResult = 0.0;
float Denominator = 2.0;

AverageResult = (PassNum1+PassNum2)/Denominator;

return AverageResult;
}

float AverageThreeNumbers(float PassNum1, float PassNum2, float PassNum3)
{float AverageResult = 0.0;
 float Denominator = 3.0;
		
	AverageResult = (PassNum1+PassNum2+PassNum3)/Denominator;	

return AverageResult;
}

char DetermineAssessment(int A0310B)
{   
	char HIPPSCode5; 

	if (A0310B == 1) 
	{
	HIPPSCode5 = '1';
	}
	else if (A0310B == 8) 
	{
	HIPPSCode5 = '0';
	}
	else if (A0310B == 99)
	{
	HIPPSCode5 = '6';
	}
	/*
	else
	{
	HIPPSCode5 = ' ';
	}
	*/
	return HIPPSCode5;
}

int DetermineRestorativeNursingCount(char* H0200C, char* H0500, char* O0500A, char* O0500B, char* O0500C,
							char* O0500D, char* O0500F, char* O0500E, char* O0500G, char* O0500H, char* O0500I,
							char* O0500J)
{int RestorativeNursingCount = 0;

	if ((0 == strcmp(H0200C, "1")) || (0 == strcmp(H0500, "1")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
    if ((0 == strcmp(O0500A, "6")) || (0 == strcmp(O0500A, "7")) || (0 == strcmp(O0500B, "6")) || (0 == strcmp(O0500B, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500C, "6")) || (0 == strcmp(O0500C, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
    if ((0 == strcmp(O0500D, "6")) || (0 == strcmp(O0500D, "7")) || (0 == strcmp(O0500F, "6")) || (0 == strcmp(O0500F, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500E, "6")) || (0 == strcmp(O0500E, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500G, "6")) || (0 == strcmp(O0500G, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500H, "6")) || (0 == strcmp(O0500H, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500I, "6")) || (0 == strcmp(O0500I, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}
	if ((0 == strcmp(O0500J, "6")) || (0 == strcmp(O0500J, "7")))
	{
	 RestorativeNursingCount = RestorativeNursingCount + 1;
	}

return RestorativeNursingCount;
}

bool DetermineDepression(int D300Score, int D600Score)
{bool IsDepressed = false;
	if ((D300Score>=10 && D300Score != 99)||(D600Score>=10))
	{
	IsDepressed = true;
	}
return IsDepressed;
}

bool DetermineIfComatose(char* B0100, char* GG0130A1, char* GG0130C1, char* GG0170B1, char* GG0170C1, 
						 char* GG0170D1, char* GG0170E1, char* GG0170F1)
{
		bool IsComatose = false;
		
		if ((0 == strcmp(B0100, "01")&&
		    (0 == strcmp(GG0130A1, "01") || 0 == strcmp(GG0130A1, "09") || 0 == strcmp(GG0130A1, "88"))
		&& (0 == strcmp(GG0130C1, "01") || 0 == strcmp(GG0130C1, "09") || 0 == strcmp(GG0130C1, "88"))
		&& (0 == strcmp(GG0170B1, "01") || 0 == strcmp(GG0170B1, "09") || 0 == strcmp(GG0170B1, "88"))
		&& (0 == strcmp(GG0170C1, "01") || 0 == strcmp(GG0170C1, "09") || 0 == strcmp(GG0170C1, "88"))
		&& (0 == strcmp(GG0170D1, "01") || 0 == strcmp(GG0170D1, "09") || 0 == strcmp(GG0170D1, "88"))
		&& (0 == strcmp(GG0170E1, "01") || 0 == strcmp(GG0170E1, "09") || 0 == strcmp(GG0170E1, "88"))
		&& (0 == strcmp(GG0170F1, "01") || 0 == strcmp(GG0170F1, "09") || 0 == strcmp(GG0170F1, "88"))))
		{
		IsComatose = true;
		}

		return IsComatose;
}




bool DetermineIfHadFeeding(char* K0510B1, char* K0510B2, char* K0710A3, char* K0710B3)
{bool HadFeeding = false;
 if (( 0 == strcmp(K0510B1, "1") && 0 == strcmp(K0710A3, "3"))
  ||((0 == strcmp(K0510B1, "1")&&(0 == strcmp(K0710A3, "2"))&&(0 == strcmp(K0710B3, "2")))
  ||((0 == strcmp(K0510B2, "1"))&&(0 == strcmp(K0710A3, "3")))
  ||(0 == strcmp(K0510B2, "1"))&&(0 == strcmp(K0710A3, "2"))&&(0 == strcmp(K0710B3, "2"))))
 {
 HadFeeding = true;
 }
 return HadFeeding;
}

bool NursingScore14Check(int NursingScore)
{ bool NursingScore14 = false;
  if (NursingScore <= 14)
  {
  NursingScore14  = true;
  }
  return NursingScore14 ;
}

bool NursingScore15Check(int NursingScore)
{ bool NursingScore15 = false;
  if ((NursingScore <= 16)&&(NursingScore >= 15))
  {
  NursingScore15 = true;
  }
  return NursingScore15;
}