#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "BehavioralSymptoms.h"


int DetermineImpairmentIndicators1(int B0700Converted, char* C0700, int C1000Converted)
{
	int ImpairmentIndicators1Count = 0;

	if (B0700Converted>0)
	{
		ImpairmentIndicators1Count = ImpairmentIndicators1Count+1;
	}
	if (0 == strcmp(C0700, "1"))
	{
		ImpairmentIndicators1Count = ImpairmentIndicators1Count+1;
	}
	if (C1000Converted>0)
	{
		ImpairmentIndicators1Count = ImpairmentIndicators1Count+1;
	}
	return ImpairmentIndicators1Count;
}

int DetermineImpairmentIndicators2(int B0700Converted, int C1000Converted)
{
	int ImpairmentIndicators2Count = 0;

	if (B0700Converted>=2)
	{
		ImpairmentIndicators2Count = ImpairmentIndicators2Count+1;
	}
	if (C1000Converted>=2)
	{
		ImpairmentIndicators2Count = ImpairmentIndicators2Count+1;
	}

	return ImpairmentIndicators2Count;
}

int BehavioralSymptomsStep2(int BIMSCode, bool C0100Test)
{
	int NextStep = 0;
	if ((BIMSCode<=9) && (BIMSCode>0))
	{
	NextStep = 5;
	}
	else if ((BIMSCode>9) && (BIMSCode!=99))
	{
	NextStep = 4;
	}
	else if ((BIMSCode==99) || (BIMSCode==0)  || (C0100Test == true))
	{
	NextStep = 3;
	}
	return NextStep;
}

bool CheckImpair(int ImpairmentIndicators1Count, int ImpairmentIndicators2Count)
{  bool IsImpaired = false;
	if ((ImpairmentIndicators1Count>=2) && (ImpairmentIndicators2Count>=1))
	{
	IsImpaired = true;
	}
	return IsImpaired;
}

bool BehavioralSymptomsStep3(bool Comatose, char* C1000, bool ImpairmentCheck)
{
	bool BehavioralSymptoms3 = false;

	if ((Comatose) || (0 == strcmp(C1000, "3")) || (ImpairmentCheck))
	{
	BehavioralSymptoms3 = true;
	}
	return BehavioralSymptoms3;
}
bool BehavioralSymptomsStep4(char* E0100A, char* E0100B, char* E0200A, char* E0200B, char* E0200C,
							  char* E0800, char* E0900)
{
    bool BehavSymptomsStep4 = false;

		if ((0 == strcmp(E0100A, "1")) 
		|| (0 == strcmp(E0100B, "1")) 
		|| ((0 == strcmp(E0200A, "2"))|| (0 == strcmp(E0200A, "3")))
		|| ((0 == strcmp(E0200B, "2"))|| (0 == strcmp(E0200B, "3")))
		|| ((0 == strcmp(E0200C, "2"))|| (0 == strcmp(E0200C, "3")))
		|| ((0 == strcmp(E0800, "2"))|| (0 == strcmp(E0800, "3")))
		|| ((0 == strcmp(E0900, "2"))|| (0 == strcmp(E0900, "3"))))
		{
		BehavSymptomsStep4 = true;
		}
	return BehavSymptomsStep4;
}

char BehavioralSymptomsStep6(int RestorativeNursingScore, int NursingFunctionScore)
	{	
		char ClinicallyComplexCaseMix[6];
		char HIPPSCode3; 

		if ((NursingFunctionScore <= 16) && (NursingFunctionScore >= 11))
			{
					if ((RestorativeNursingScore <= 1) && (RestorativeNursingScore >= 0))
					{
					strcpy( ClinicallyComplexCaseMix, "BAB1" ); HIPPSCode3 = 'S';
					printf("The Nursing Case Mix Group Is %s\n",ClinicallyComplexCaseMix);
					}
					else if (RestorativeNursingScore >= 2)
					{
					strcpy( ClinicallyComplexCaseMix, "BAB2" ); HIPPSCode3 = 'R';
					printf("The Nursing Case Mix Group Is %s\n",ClinicallyComplexCaseMix);
					}
			}	
				
	return HIPPSCode3;
	}