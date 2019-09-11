#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "NTAComponent.h"
#include "mdsgrouper.h"

bool DetermineNTAFeeding(char* K0510A2)
{bool NTAFeeding = false;
 if (0 == strcmp(K0510A2, "1"))
 {
 NTAFeeding = true;
 }
 return NTAFeeding;
}

int DetermineNTAFeedingScore(char* K0710A2, char* K0710B2)
{int NTAFeedingScore = 0;
 if (0 == strcmp(K0710A2, "3"))
	{
	NTAFeedingScore = 7;
	}
 else if ((0 == strcmp(K0710A2, "2"))&&(0 == strcmp(K0710B2, "2")))
   {
   NTAFeedingScore = 3;
   }
 return NTAFeedingScore;
}

int DetermineIntravenousMedication(char* O0100H2)
{int IntravenousMedication = 0;
 if (0 == strcmp(O0100H2, "1"))
  {
  IntravenousMedication = 5; 
  }
 return IntravenousMedication;
}

int DetermineVentilator(char* O0100F2)
{int Ventilator = 0;
 if (0 == strcmp(O0100F2, "1"))
  {
  Ventilator = 4; 
  }
 return Ventilator;
}

int DetermineTransfusion(char* O0100I2)
{int Transfusion = 0;
 if (0 == strcmp(O0100I2, "1"))
  {
  Transfusion = 2; 
  }
 return Transfusion;
}

int DetermineMultipleSclerosis(char* I5200)
{int MultipleSclerosis = 0;
 if (0 == strcmp(I5200, "1"))
  {
  MultipleSclerosis = 2; 
  }
 return MultipleSclerosis;
}

int DetermineAsthmaCOPD(char* I6200)
{int AsthmaCOPD = 0;
 if (0 == strcmp(I6200, "1"))
  {
  AsthmaCOPD = 2; 
  }
 return AsthmaCOPD;
}

int DetermineWoundInfection(char* I2500)
{int WoundInfection = 0;
 if (0 == strcmp(I2500, "1"))
  {
  WoundInfection = 2; 
  }
 return WoundInfection;
}

int DetermineDiabetesMellitus(char* I2900)
{int DiabetesMellitus = 0;
 if (0 == strcmp(I2900, "1"))
  {
  DiabetesMellitus = 2; 
  }
 return DiabetesMellitus;
}

int DetermineDiabeticFoot(char* M1040B)
{int DiabeticFoot = 0;
 if (0 == strcmp(M1040B, "1"))
  {
  DiabeticFoot = 1; 
  }
 return DiabeticFoot;
}

int DetermineTracheostomyCare(char* O0100E2)
{int TracheostomyCare = 0;
 if (0 == strcmp(O0100E2, "1"))
  {
  TracheostomyCare = 1; 
  }
 return TracheostomyCare;
}

int DetermineResistantOrganism(char* I1700)
{int ResistantOrganism = 0;
 if (0 == strcmp(I1700, "1"))
  {
  ResistantOrganism = 1; 
  }
 return ResistantOrganism;
}

int DetermineIsolation(char* O0100M2)
{int Isolation = 0;
 if (0 == strcmp(O0100M2, "1"))
  {
  Isolation = 1; 
  }
 return Isolation;
}

int DetermineRadiation(char* O0100B2)
{int Radiation = 0;
 if (0 == strcmp(O0100B2, "1"))
  {
  Radiation = 1; 
  }
 return Radiation;
}

int DetermineUnhealedPressure(char* M0300D1)
{int UnhealedPressure = 0;
 if (0 == strcmp(M0300D1, "1"))
  {
  UnhealedPressure = 1; 
  }
 return UnhealedPressure;
}


int DetermineIntermittentCatheterization(char* H0100D)
{int IntermittentCatheterization = 0;
 if (0 == strcmp(H0100D, "1"))
  {
  IntermittentCatheterization = 1; 
  }
 return IntermittentCatheterization;
}

int DetermineInflammatoryBowel(char* I1300)
{int InflammatoryBowel = 0;
 if (0 == strcmp(I1300, "1"))
  {
  InflammatoryBowel = 1; 
  }
 return InflammatoryBowel;
}


int DetermineSuctioning(char* O0100D2)
{int Suctioning = 0;
 if (0 == strcmp(O0100D2, "1"))
  {
  Suctioning = 1; 
  }
 return Suctioning;
}

int DetermineFeedingTube(char* K0510B2)
{int FeedingTube = 0;
 if (0 == strcmp(K0510B2, "1"))
  {
  FeedingTube = 1; 
  }
 return FeedingTube;
}

int DetermineMalnutrition(char* I5600)
{int Malnutrition = 0;
 if (0 == strcmp(I5600, "1"))
  {
  Malnutrition = 1; 
  }
 return Malnutrition;
}

int DetermineOstomy(char* H0100C)
{int Ostomy = 0;
 if (0 == strcmp(H0100C, "1"))
  {
  Ostomy = 1; 
  }
 return Ostomy;
}

int DetermineFootInfection(char* M1040A, char* M1040C)
{int FootInfection = 0;
 if ((0 == strcmp(M1040A, "1")) || (0 == strcmp(M1040C, "1")))
  {
  FootInfection = 1; 
  }
 return FootInfection;
}

int DetermineNTAScore(int I8000Score, int IntraVenousMedicationScore, int VentilatorScore,
					  int TransfusionScore, int MultipleSclerosisScore, int AsthmaCOPDScore,
					  int WoundInfectionScore, int DiabetesMellitusScore, int DiabeticFootScore,
					  int TracheostomyCareScore, int ResistantOrganismScore, int IsolationScore,
					  int RadiationScore, int UnhealedPressureScore, int IntermittentCatheterizationScore, int InflammatoryBowelScore, 
					  int SuctioningScore, int FeedingTubeScore, int MalnutritionScore, int OstomyScore, int FootInfectionScore, int NTAFeedingScore)
{int NTAScore = 0;

NTAScore = I8000Score + IntraVenousMedicationScore + VentilatorScore + TransfusionScore + MultipleSclerosisScore 
		   + AsthmaCOPDScore + WoundInfectionScore + DiabetesMellitusScore + DiabeticFootScore + TracheostomyCareScore
		   + ResistantOrganismScore + IsolationScore + RadiationScore + UnhealedPressureScore + IntermittentCatheterizationScore + InflammatoryBowelScore + SuctioningScore + FeedingTubeScore +
		   MalnutritionScore + OstomyScore+FootInfectionScore;

return NTAScore;
}
char DetermineCaseMixGroupNTA(int NTAScore)
{
    char CaseMixGroupNTA[3];
	char HIPPSCodeGenerated3;
	
	if (NTAScore >= 12)
	{strcpy( CaseMixGroupNTA, "NA" ); HIPPSCodeGenerated3 = 'A';}

	else if ((NTAScore <=11) && (NTAScore >= 9))
	{strcpy( CaseMixGroupNTA, "NB" ); HIPPSCodeGenerated3 = 'B';}

	else if ((NTAScore <=8) && (NTAScore >= 6)) 
	{strcpy( CaseMixGroupNTA, "NC" ); HIPPSCodeGenerated3 = 'C';}

	else if ((NTAScore <=5) && (NTAScore >= 3))
	{strcpy( CaseMixGroupNTA, "ND" ); HIPPSCodeGenerated3 = 'D';}

	else if ((NTAScore <=2) && (NTAScore >= 1))
	{strcpy( CaseMixGroupNTA, "NE" ); HIPPSCodeGenerated3 = 'E';}

	else if (NTAScore == 0)
	{strcpy( CaseMixGroupNTA, "NF" ); HIPPSCodeGenerated3 = 'F';}

	return HIPPSCodeGenerated3;
}