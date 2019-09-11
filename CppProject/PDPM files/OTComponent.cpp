#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "OTComponent.h"

int OTClinicalCategory[40950];

int CalcEatingFunctionScoreOT(char* GG0130A1)
{ int EatingFunctionScore = 0;

  if (0 == strcmp(GG0130A1, "01") || 0 == strcmp(GG0130A1, "07") || 0 == strcmp(GG0130A1, "09") 
	 ||0 == strcmp(GG0130A1, "10") || 0 == strcmp(GG0130A1, "88") || 0 == strcmp(GG0130A1, "  ")
	 || 0 == strcmp(GG0130A1, " -") || 0 == strcmp(GG0130A1, "- "))
  {	
     EatingFunctionScore = EatingFunctionScore;
  }
  else if (0 == strcmp(GG0130A1, "02"))
  {
	 EatingFunctionScore = EatingFunctionScore + 1;
  }
  else if (0 == strcmp(GG0130A1, "03"))
  {
	 EatingFunctionScore = EatingFunctionScore + 2;
  }
  else if (0 == strcmp(GG0130A1, "04"))
  {
	 EatingFunctionScore = EatingFunctionScore + 3;
  }
  else if (0 == strcmp(GG0130A1, "05") || (0 == strcmp(GG0130A1, "06")))
  {
	 EatingFunctionScore = EatingFunctionScore + 4;
  }
  return EatingFunctionScore;
}

int CalcOralHygieneFunctionScoreOT(char* GG0130B1)
{ int OralHygieneFunctionScore = 0;

  if (0 == strcmp(GG0130B1, "01") || 0 == strcmp(GG0130B1, "07") || 0 == strcmp(GG0130B1, "09") 
	 ||0 == strcmp(GG0130B1, "10") || 0 == strcmp(GG0130B1, "88")|| 0 == strcmp(GG0130B1, "  ")
	 || 0 == strcmp(GG0130B1, " -") || 0 == strcmp(GG0130B1, "- "))
  {	
     OralHygieneFunctionScore = OralHygieneFunctionScore;
  }
  else if (0 == strcmp(GG0130B1, "02"))
  {
	 OralHygieneFunctionScore = OralHygieneFunctionScore + 1;
  }
  else if (0 == strcmp(GG0130B1, "03"))
  {
	 OralHygieneFunctionScore = OralHygieneFunctionScore + 2;
  }
  else if (0 == strcmp(GG0130B1, "04"))
  {
	 OralHygieneFunctionScore = OralHygieneFunctionScore + 3;
  }
  else if (0 == strcmp(GG0130B1, "05") || (0 == strcmp(GG0130B1, "06")))
  {
	 OralHygieneFunctionScore = OralHygieneFunctionScore + 4;
  }
  return OralHygieneFunctionScore;
}

int CalcToiletingHygieneFunctionScoreOT(char* GG0130C1)
{ int ToiletingHygieneFunctionScore = 0;

  if (0 == strcmp(GG0130C1, "01") || 0 == strcmp(GG0130C1, "07") || 0 == strcmp(GG0130C1, "09") 
	 ||0 == strcmp(GG0130C1, "10") || 0 == strcmp(GG0130C1, "88")|| 0 == strcmp(GG0130C1, "  ")
	 || 0 == strcmp(GG0130C1, " -") || 0 == strcmp(GG0130C1, "- "))
  {	
     ToiletingHygieneFunctionScore = ToiletingHygieneFunctionScore;
  }
  else if (0 == strcmp(GG0130C1, "02"))
  {
	 ToiletingHygieneFunctionScore = ToiletingHygieneFunctionScore + 1;
  }
  else if (0 == strcmp(GG0130C1, "03"))
  {
	 ToiletingHygieneFunctionScore = ToiletingHygieneFunctionScore + 2;
  }
  else if (0 == strcmp(GG0130C1, "04"))
  {
	 ToiletingHygieneFunctionScore = ToiletingHygieneFunctionScore + 3;
  }
  else if (0 == strcmp(GG0130C1, "05") || (0 == strcmp(GG0130C1, "06")))
  {
	 ToiletingHygieneFunctionScore = ToiletingHygieneFunctionScore + 4;
  }
  return ToiletingHygieneFunctionScore;
}

float CalcSitToLyingFunctionScoreOT(char* GG0170B1)
{ float SitToLyingFunctionScore = 0;

  if (0 == strcmp(GG0170B1, "01") || 0 == strcmp(GG0170B1, "07") || 0 == strcmp(GG0170B1, "09") 
	 ||0 == strcmp(GG0170B1, "10") || 0 == strcmp(GG0170B1, "88") || 0 == strcmp(GG0170B1, "  ")
	 || 0 == strcmp(GG0170B1, " -") || 0 == strcmp(GG0170B1, "- "))
  {	
     SitToLyingFunctionScore = SitToLyingFunctionScore;
  }
  else if (0 == strcmp(GG0170B1, "02"))
  {
	 SitToLyingFunctionScore = SitToLyingFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170B1, "03"))
  {
	 SitToLyingFunctionScore = SitToLyingFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170B1, "04"))
  {
	 SitToLyingFunctionScore = SitToLyingFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170B1, "05") || (0 == strcmp(GG0170B1, "06")))
  {
	 SitToLyingFunctionScore = SitToLyingFunctionScore + 4;
  }
  return SitToLyingFunctionScore;
}

float CalcLyingToSittingFunctionScoreOT(char* GG0170C1)
{ float LyingToSittingFunctionScore = 0;

  if (0 == strcmp(GG0170C1, "01") || 0 == strcmp(GG0170C1, "07") || 0 == strcmp(GG0170C1, "09") 
	 ||0 == strcmp(GG0170C1, "10") || 0 == strcmp(GG0170C1, "88")|| 0 == strcmp(GG0170C1, "  ")
	 || 0 == strcmp(GG0170C1, " -") || 0 == strcmp(GG0170C1, "- "))
  {	
     LyingToSittingFunctionScore = LyingToSittingFunctionScore;
  }
  else if (0 == strcmp(GG0170C1, "02"))
  {
	 LyingToSittingFunctionScore = LyingToSittingFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170C1, "03"))
  {
	 LyingToSittingFunctionScore = LyingToSittingFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170C1, "04"))
  {
	 LyingToSittingFunctionScore = LyingToSittingFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170C1, "05") || (0 == strcmp(GG0170C1, "06")))
  {
	 LyingToSittingFunctionScore = LyingToSittingFunctionScore + 4;
  }
  return LyingToSittingFunctionScore;
}

float CalcSitToStandFunctionScoreOT(char* GG0170D1)
{ float SitToStandFunctionScore = 0;

  if (0 == strcmp(GG0170D1, "01") || 0 == strcmp(GG0170D1, "07") || 0 == strcmp(GG0170D1, "09") 
	 ||0 == strcmp(GG0170D1, "10") || 0 == strcmp(GG0170D1, "88")|| 0 == strcmp(GG0170D1, "  ")
	 || 0 == strcmp(GG0170D1, " -") || 0 == strcmp(GG0170D1, "- "))
  {	
     SitToStandFunctionScore = SitToStandFunctionScore;
  }
  else if (0 == strcmp(GG0170D1, "02"))
  {
	 SitToStandFunctionScore = SitToStandFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170D1, "03"))
  {
	 SitToStandFunctionScore = SitToStandFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170D1, "04"))
  {
	 SitToStandFunctionScore = SitToStandFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170D1, "05") || (0 == strcmp(GG0170D1, "06")))
  {
	 SitToStandFunctionScore = SitToStandFunctionScore + 4;
  }
  return SitToStandFunctionScore;
}

float CalcChairBedFunctionScoreOT(char* GG0170E1)
{ float ChairBedFunctionScore = 0;

  if (0 == strcmp(GG0170E1, "01") || 0 == strcmp(GG0170E1, "07") || 0 == strcmp(GG0170E1, "09") 
	 ||0 == strcmp(GG0170E1, "10") || 0 == strcmp(GG0170E1, "88")|| 0 == strcmp(GG0170E1, "  ")
	 || 0 == strcmp(GG0170E1, " -") || 0 == strcmp(GG0170E1, "- "))
  {	
     ChairBedFunctionScore = ChairBedFunctionScore;
  }
  else if (0 == strcmp(GG0170E1, "02"))
  {
	 ChairBedFunctionScore = ChairBedFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170E1, "03"))
  {
	 ChairBedFunctionScore = ChairBedFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170E1, "04"))
  {
	 ChairBedFunctionScore = ChairBedFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170E1, "05") || (0 == strcmp(GG0170E1, "06")))
  {
	 ChairBedFunctionScore = ChairBedFunctionScore + 4;
  }
  return ChairBedFunctionScore;
}

float CalcToiletTransferFunctionScoreOT(char* GG0170F1)
{ float ToiletTransferFunctionScore = 0;

  if (0 == strcmp(GG0170F1, "01") || 0 == strcmp(GG0170F1, "07") || 0 == strcmp(GG0170F1, "09") 
	 ||0 == strcmp(GG0170F1, "10") || 0 == strcmp(GG0170F1, "88")|| 0 == strcmp(GG0170F1, "  ")
	 || 0 == strcmp(GG0170F1, " -") || 0 == strcmp(GG0170F1, "- "))
  {	
     ToiletTransferFunctionScore = ToiletTransferFunctionScore;
  }
  else if (0 == strcmp(GG0170F1, "02"))
  {
	 ToiletTransferFunctionScore = ToiletTransferFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170F1, "03"))
  {
	 ToiletTransferFunctionScore = ToiletTransferFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170F1, "04"))
  {
	 ToiletTransferFunctionScore = ToiletTransferFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170F1, "05") || (0 == strcmp(GG0170F1, "06")))
  {
	 ToiletTransferFunctionScore = ToiletTransferFunctionScore + 4;
  }
  return ToiletTransferFunctionScore;
}

float CalcWalkFiftyFunctionScoreOT(char* GG0170J1)
{ float WalkFiftyFunctionScore = 0;

  if (0 == strcmp(GG0170J1, "01") || 0 == strcmp(GG0170J1, "07") || 0 == strcmp(GG0170J1, "09") 
	 ||0 == strcmp(GG0170J1, "10") || 0 == strcmp(GG0170J1, "88")|| 0 == strcmp(GG0170J1, "  ")
	 || 0 == strcmp(GG0170J1, " -") || 0 == strcmp(GG0170J1, "- "))
  {	
     WalkFiftyFunctionScore = WalkFiftyFunctionScore;
  }
  else if (0 == strcmp(GG0170J1, "02"))
  {
	 WalkFiftyFunctionScore = WalkFiftyFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170J1, "03"))
  {
	 WalkFiftyFunctionScore = WalkFiftyFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170J1, "04"))
  {
	 WalkFiftyFunctionScore = WalkFiftyFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170J1, "05") || (0 == strcmp(GG0170J1, "06")))
  {
	 WalkFiftyFunctionScore = WalkFiftyFunctionScore + 4;
  }
  return WalkFiftyFunctionScore;
}

float CalcWalkOneFiftyFunctionScoreOT(char* GG0170K1)
{ float WalkOneFiftyFunctionScore = 0;

  if (0 == strcmp(GG0170K1, "01") || 0 == strcmp(GG0170K1, "07") || 0 == strcmp(GG0170K1, "09") 
	 ||0 == strcmp(GG0170K1, "10") || 0 == strcmp(GG0170K1, "88")|| 0 == strcmp(GG0170K1, "  ")
	 || 0 == strcmp(GG0170K1, " -") || 0 == strcmp(GG0170K1, "- "))
  {	
     WalkOneFiftyFunctionScore = WalkOneFiftyFunctionScore;
  }
  else if (0 == strcmp(GG0170K1, "02"))
  {
	 WalkOneFiftyFunctionScore = WalkOneFiftyFunctionScore + 1;
  }
  else if (0 == strcmp(GG0170K1, "03"))
  {
	 WalkOneFiftyFunctionScore = WalkOneFiftyFunctionScore + 2;
  }
  else if (0 == strcmp(GG0170K1, "04"))
  {
	 WalkOneFiftyFunctionScore = WalkOneFiftyFunctionScore + 3;
  }
  else if (0 == strcmp(GG0170K1, "05") || (0 == strcmp(GG0170K1, "06")))
  {
	 WalkOneFiftyFunctionScore = WalkOneFiftyFunctionScore + 4;
  }
  return WalkOneFiftyFunctionScore;
}

int	CalcPDPMFunctionScoreOT(int EatingFunctionScoreOT, int OralHygieneFunctionScoreOT, int ToiletingHygieneFunctionScoreOT,
							int BedAverageOT, int TransferAverageOT, int WalkingAverageOT)
  {int PDPMFunctionScoreOT = 0;

	PDPMFunctionScoreOT = EatingFunctionScoreOT + OralHygieneFunctionScoreOT + ToiletingHygieneFunctionScoreOT +
						  BedAverageOT + TransferAverageOT + WalkingAverageOT ; 

	return PDPMFunctionScoreOT;
  }



int DetermineClinicalCategoryOT(int FoundIndex)
{int ReturnedCategoryValue;

     ReturnedCategoryValue = OTClinicalCategory[FoundIndex];

	 return ReturnedCategoryValue;
}


char* DetermineCaseMixGroupOT(int ClinicalCategoryNumberCode, int FunctionScore)
{
    char CaseMixGroupOT[3];
	
	if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupOT, "TA" );}

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupOT, "TB" );}

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupOT, "TC" );}

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore == 24))
	{strcpy( CaseMixGroupOT, "TD" );}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupOT, "TE" );}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupOT, "TF" );}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupOT, "TG" );}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore == 24))
	{strcpy( CaseMixGroupOT, "TH" );}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupOT, "TI" );}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupOT, "TJ" );}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupOT, "TK" );}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore == 24))
	{strcpy( CaseMixGroupOT, "TL" );}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupOT, "TM" );}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupOT, "TN" );}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupOT, "TO" );}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore == 24))
	{strcpy( CaseMixGroupOT, "TP" );}
	
	return CaseMixGroupOT;
}