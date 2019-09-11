#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "SpecialCareHigh.h"


bool DetermineSepticemia(char* I2100)
{bool Septicemia = false;
		if (0 == strcmp(I2100, "1")) 
		{
		Septicemia = true;
		}
return Septicemia;
}

bool DetermineDiabetes(char* I2900, int InsulinInjections, int InsulinOrders)
{bool Diabetes = false;
	if((0 == strcmp(I2900, "1")) && (InsulinInjections==7)&& (InsulinOrders>=2)) 
	{
	Diabetes = true;
	}
	return Diabetes;
}

bool DetermineQuadriplegia(char* I5100, int NursingScore)
{bool Quadriplegia = false;
		if ((0 == strcmp(I5100, "1"))&& (NursingScore <=11)) 
		{
		Quadriplegia = true;
		}
return Quadriplegia;
}

	
bool DeterminePulmonaryDisease(char* I6200, char* J1100C)
{bool PulmonaryDisease = false;
	if((0 == strcmp(I6200, "1"))&&(0 == strcmp(J1100C, "1"))) 
	{
	PulmonaryDisease = true;
	}
	return PulmonaryDisease;
}

bool DetermineFever(char* J1550A)
{bool Fever = false;
	if(0 == strcmp(J1550A, "1"))
	{
	Fever = true;
	}
	return Fever;
}

bool DetermineFeverCombo(bool Fever, bool Pneumonia, bool Vomiting, bool WeightLoss, bool FeedTube)
{bool FeverCombo = false;
	if((Fever) && ((Pneumonia) || (Vomiting) || (WeightLoss) || (FeedTube)))
	{
	FeverCombo = true;
	}
	return FeverCombo;
}

bool DetermineParenteralFeeding(char* K0510A1, char* K0510A2)
{bool ParenteralFeeding = false;
	if((0 == strcmp(K0510A1, "1"))||(0 == strcmp(K0510A2, "1"))) 
	{
	ParenteralFeeding = true;
	}
	return ParenteralFeeding;
}

bool DeterminePneumonia(char* I2000)
{bool Pneumonia = false;
	if (0 == strcmp(I2000, "1")) 
		{
		Pneumonia = true;
		}	
	return Pneumonia;
}

bool DetermineVomiting(char* J1550B)
{bool Vomiting = false;
	if (0 == strcmp(J1550B, "1")) 
		{
		Vomiting = true;
		}	
	return Vomiting;
}

bool DetermineWeightLoss(char* K0300)
{bool WeightLoss = false;
	if ((0 == strcmp(K0300, "1")) || (0 == strcmp(K0300, "2")))
		{
		WeightLoss = true;
		}	
	return WeightLoss;
}

bool DetermineRespiratory(int RespiratoryTherapyDays)
{bool Respiratory = false;
	if (RespiratoryTherapyDays==7) 
		{
		Respiratory = true;
		}
return Respiratory;
}

bool SpecialCareHighStep1(bool Comatose, bool Septicemia, bool Diabetes, bool Quadriplegia,
						  bool PulmonaryDisease, bool Fever, bool Parenteral, bool Respiratory)
{bool SpecialCareHigh = false;
	if ((Comatose)||(Septicemia)||(Diabetes)||(Quadriplegia)||(PulmonaryDisease)||(Fever)||(Parenteral)||(Respiratory))
	{
	SpecialCareHigh = true;
	}
return SpecialCareHigh; 
}

char SpecialCareHighStep4 (int PDPMFunctionScoreNursing, bool IsDepressed)
{   
	char HIPPSCodeReturned;	
	char NursingCaseMixSCHigh[6];

					if (((PDPMFunctionScoreNursing<=5)&&(PDPMFunctionScoreNursing>=0))&&(IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "HDE2" ); HIPPSCodeReturned = 'D';
					}	
					else if (((PDPMFunctionScoreNursing<=5)&&(PDPMFunctionScoreNursing>=0))&&(!IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "HDE1" ); HIPPSCodeReturned = 'E';
					}
					else if (((PDPMFunctionScoreNursing<=14)&&(PDPMFunctionScoreNursing>=6))&&(IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "HBC2" ); HIPPSCodeReturned = 'F';
					}
					else if (((PDPMFunctionScoreNursing<=14)&&(PDPMFunctionScoreNursing>=6))&&(!IsDepressed))
					{
					strcpy( NursingCaseMixSCHigh, "HBC1" ); HIPPSCodeReturned = 'G';
					}

	return HIPPSCodeReturned;
}