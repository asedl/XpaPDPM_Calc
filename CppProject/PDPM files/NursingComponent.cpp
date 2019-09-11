#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "NursingComponent.h"


float CalcEatingFunctionScoreNursing(char* GG0130A1)
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


float CalcToiletingHygieneFunctionScoreNursing(char* GG0130C1)
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

float CalcSitToLyingFunctionScoreNursing(char* GG0170B1)
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

float CalcLyingToSittingFunctionScoreNursing(char* GG0170C1)
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

float CalcSitToStandFunctionScoreNursing(char* GG0170D1)
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

float CalcChairBedFunctionScoreNursing(char* GG0170E1)
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

float CalcToiletTransferFunctionScoreNursing(char* GG0170F1)
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

float CalcPDPMFunctionScoreNursing(float EatingFunctionScore, float ToiletingHygieneFunctionScore,
							float BedAverage, float TransferAverage)
  {float PDPMFunctionScoreNursing = 0;

	PDPMFunctionScoreNursing = EatingFunctionScore + ToiletingHygieneFunctionScore +
						  BedAverage + TransferAverage; 

	return PDPMFunctionScoreNursing;
  }



