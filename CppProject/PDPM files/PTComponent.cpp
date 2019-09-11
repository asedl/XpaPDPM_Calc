#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "PTComponent.h"
#include "mdsgrouper.h"
#include "DiagnosisCodesLoaded.h"


float CalcEatingFunctionScorePT(char* GG0130A1)
{ float EatingFunctionScore = 0;

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

float CalcOralHygieneFunctionScorePT(char* GG0130B1)
{ float OralHygieneFunctionScore = 0;

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

float CalcToiletingHygieneFunctionScorePT(char* GG0130C1)
{ float ToiletingHygieneFunctionScore = 0;

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

float CalcSitToLyingFunctionScorePT(char* GG0170B1)
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

float CalcLyingToSittingFunctionScorePT(char* GG0170C1)
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

float CalcSitToStandFunctionScorePT(char* GG0170D1)
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

float CalcChairBedFunctionScorePT(char* GG0170E1)
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

float CalcToiletTransferFunctionScorePT(char* GG0170F1)
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

float CalcWalkFiftyFunctionScorePT(char* GG0170J1)
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

float CalcWalkOneFiftyFunctionScorePT(char* GG0170K1)
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

float CalcPDPMFunctionScorePT(float EatingFunctionScorePT, float OralHygieneFunctionScorePT, float ToiletingHygieneFunctionScorePT,
							float BedAveragePT, float TransferAveragePT, float WalkingAveragePT)
  {float PDPMFunctionScorePT = 0;

	PDPMFunctionScorePT = EatingFunctionScorePT + OralHygieneFunctionScorePT + ToiletingHygieneFunctionScorePT +
						  BedAveragePT + TransferAveragePT + WalkingAveragePT ; 

	return PDPMFunctionScorePT;
  }

int DetermineClinicalCategoryPT(int FoundIndex)
{char ReturnedCategoryValuePT;

     ReturnedCategoryValuePT = I0020BPTCode[FoundIndex];

	 return ReturnedCategoryValuePT;
}



char DetermineCaseMixGroupPT(int ClinicalCategoryNumberCode, int FunctionScore)
{
    char CaseMixGroupPT[3];
	char HIPPSCodeGenerate1;
	
	if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupPT, "TA" ); HIPPSCodeGenerate1 = 'A'; }

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupPT, "TB" ); HIPPSCodeGenerate1 = 'B';}

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupPT, "TC" ); HIPPSCodeGenerate1 = 'C';}

	else if ((ClinicalCategoryNumberCode == 1) && (FunctionScore == 24))
	{strcpy( CaseMixGroupPT, "TD" ); HIPPSCodeGenerate1 = 'D';}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupPT, "TE" ); HIPPSCodeGenerate1 = 'E';}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupPT, "TF" ); HIPPSCodeGenerate1 = 'F';}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupPT, "TG" ); HIPPSCodeGenerate1 = 'G';}

	else if ((ClinicalCategoryNumberCode == 2) && (FunctionScore == 24))
	{strcpy( CaseMixGroupPT, "TH" ); HIPPSCodeGenerate1= 'H';}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupPT, "TI" ); HIPPSCodeGenerate1 = 'I';}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupPT, "TJ" ); HIPPSCodeGenerate1 = 'J';}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupPT, "TK" ); HIPPSCodeGenerate1 = 'K';}

	else if ((ClinicalCategoryNumberCode == 3) && (FunctionScore == 24))
	{strcpy( CaseMixGroupPT, "TL" ); HIPPSCodeGenerate1 = 'L';}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=5) && (FunctionScore >= 0))
	{strcpy( CaseMixGroupPT, "TM" ); HIPPSCodeGenerate1 = 'M';}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=9) && (FunctionScore >= 6))
	{strcpy( CaseMixGroupPT, "TN" ); HIPPSCodeGenerate1 = 'N';}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore <=23) && (FunctionScore >= 10)) 
	{strcpy( CaseMixGroupPT, "TO" ); HIPPSCodeGenerate1 = 'O';}

	else if ((ClinicalCategoryNumberCode == 4) && (FunctionScore == 24))
	{strcpy( CaseMixGroupPT, "TP" ); HIPPSCodeGenerate1= 'P';}

	else
	{HIPPSCodeGenerate1= 'Z';}
	
	return HIPPSCodeGenerate1;
}