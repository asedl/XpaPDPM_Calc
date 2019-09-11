#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "SpecialCareLow.h"


bool DetermineCerebralPalsy(char* I4400, int NursingScore)
{bool CerebralPalsy = false;
	if ((0 == strcmp(I4400, "1"))&& (NursingScore <=11)) 
		{
		CerebralPalsy = true;
		}
return CerebralPalsy;
}

bool DetermineMultipleSclerosis(char* I5200, int NursingScore)
{bool MultipleSclerosis = false;
	if ((0 == strcmp(I5200, "1"))&& (NursingScore <=11)) 
		{
		MultipleSclerosis = true;
		}
return MultipleSclerosis;
}

bool DetermineParkinsons(char* I5300, int NursingScore)
{bool Parkinsons = false;
	if ((0 == strcmp(I5300, "1"))&& (NursingScore <=11)) 
		{
		Parkinsons = true;
		}
return Parkinsons;
}

bool DetermineRespiratoryFailure(char* I6300, char* O0100C2)
{ bool RespiratoryFailure = false;
	if((0 == strcmp(I6300, "1"))&&(0 == strcmp(O0100C2, "1"))) 
	{
	RespiratoryFailure = true;
	}
  return RespiratoryFailure;
}

bool DetermineRadiationBool(char* O0100B2)
{bool Radiation = false;
		if (0 == strcmp(O0100B2, "1")) 
		{
		Radiation = true;
		}
return Radiation;
}

bool DetermineDialysis(char* O0100J2)
{bool Dialysis = false;
		if (0 == strcmp(O0100J2, "1")) 
		{
		Dialysis = true;
		}
return Dialysis;
}

bool DetermineFootCheck(char* M1040A, char* M1040B, char* M1040C, char* M1200I)
{bool FootCheck = false;
if (((0 == strcmp(M1040A, "1")) || (0 == strcmp(M1040B, "1")) 
	 || (0 == strcmp(M1040C, "1"))) && (0 == strcmp(M1200I, "1")))
	{
	FootCheck = true; 
	}
return FootCheck;
}

bool CalcUlcerCheck1(int Stage2UlcerCount, int SkinTreatmentCount)
{bool UlcerCheck1 = false;
if ((Stage2UlcerCount>=2)&&(SkinTreatmentCount>=2))
{
UlcerCheck1 = true;
}
return UlcerCheck1;
}

bool CalcUlcerCheck2(int Stage3UlcerCount, int Stage4UlcerCount, int UnstagedUlcerCount, int SkinTreatmentCount)
{bool UlcerCheck2 = false;
if (((Stage3UlcerCount>=1)||(Stage4UlcerCount>=1)||(UnstagedUlcerCount>=1))&&(SkinTreatmentCount>=2))
{
UlcerCheck2 = true;
}
return UlcerCheck2;
}

bool CalcUlcerCheck3(int VenousUlcerCount, int SkinTreatmentCount)
{bool UlcerCheck3 = false;
if ((VenousUlcerCount>=2)&&(SkinTreatmentCount>=2))
{
UlcerCheck3 = true;
}
return UlcerCheck3;
}

bool CalcUlcerCheck4(int Stage2UlcerCount, int VenousUlcerCount, int SkinTreatmentCount)
{bool UlcerCheck4 = false;
if ((Stage2UlcerCount==1)&&(VenousUlcerCount==1)&&(SkinTreatmentCount>=2))
{
UlcerCheck4 = true;
}
return UlcerCheck4;
}

int DetermineSkinTreatments(char* M1200A, char* M1200B, char* M1200C, char* M1200D, char* M1200E,
							char* M1200G, char* M1200H)
{int SkinTreatmentNumber = 0;

	if ((0 == strcmp(M1200A, "1")) || (0 == strcmp(M1200B, "1")))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
    if (0 == strcmp(M1200C, "1"))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
	if (0 == strcmp(M1200D, "1"))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
    if (0 == strcmp(M1200E, "1"))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
	if (0 == strcmp(M1200G, "1"))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
	if (0 == strcmp(M1200H, "1"))
	{
	 SkinTreatmentNumber = SkinTreatmentNumber + 1;
	}
//	 printf("The skin treatment number is: %i\n",SkinTreatmentNumber);
return SkinTreatmentNumber;
}

bool SpecialCareLowStep1 (bool CerebralPalsy, bool Sclerosis, bool Parkinsons, bool RespiratoryFailure,
						  bool FeedingTube, bool Ulcer1, bool Ulcer2, bool Ulcer3, bool Ulcer4, 
						  bool FootCheck, bool Radiation, bool Dialysis)
{ bool SpecialCareLow = false;
	if ((CerebralPalsy) || (Sclerosis) || (Parkinsons) || (RespiratoryFailure) || (FeedingTube)
		|| (Ulcer1) || (Ulcer2) || (Ulcer3) || (Ulcer4) || (FootCheck) || (Radiation) || (Dialysis))
	{
	 SpecialCareLow = true;
	}
  return SpecialCareLow;
}
char SpecialCareLowStep4 (int PDPMFunctionScoreNursing, bool IsDepressed)
{   
	char HIPPSCodeReturnedLow;	
	char NursingCaseMixSCHigh[6];

		if (((PDPMFunctionScoreNursing<=5)&&(PDPMFunctionScoreNursing>=0))&&(IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "LDE2" ); HIPPSCodeReturnedLow = 'H';
					}	
					else if (((PDPMFunctionScoreNursing<=5)&&(PDPMFunctionScoreNursing>=0))&&(!IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "LDE1" ); HIPPSCodeReturnedLow = 'I';
					}
					else if (((PDPMFunctionScoreNursing<=14)&&(PDPMFunctionScoreNursing>=6))&&(IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "LBC2" ); HIPPSCodeReturnedLow = 'J';
					}
					else if (((PDPMFunctionScoreNursing<=14)&&(PDPMFunctionScoreNursing>=6))&&(!IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "LBC1" ); HIPPSCodeReturnedLow = 'K';
					}

	return HIPPSCodeReturnedLow;
}