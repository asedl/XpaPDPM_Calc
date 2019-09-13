#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "CheckI8000Base.h"
#include "CheckI8000Logic.h"

int ReturnNTAScore(char* I8000, int *I8000Code)
{int I8000Score = 0;
bool LungTransplant = false; bool MajorOrgan = false; bool OpportunisticInfections = false;
bool BoneNecrosis = false; bool MyeloidLeukemia = false; bool Endocarditis = false;
bool ImmuneDisorders = false; bool LiverDisease = false; bool Narcolepsy = false;
bool CysticFibrosis = false; bool SpecificImmuneDisorders = false; bool MorbidObesity = false; 
bool PsoriaticAnthropathy = false; bool ChronicPancreatitis = false; bool ProlificRetinopathy = false; 
bool ImplantedDevice = false; bool AscepticNecrosis = false; bool RespiratoryFailure = false;
bool Myelodysplastic = false; bool TissueDisorders = false; bool DiabeticRetinopathy = false;
bool SevereSkin = false; bool IntractableEpilepsy = false; bool ImmuneDisorder = false;
bool CirrhosisLiver = false; bool RespiratoryArrest = false; bool PulmonaryFibrosis = false;
bool CodeFound = false;


if (!CodeFound)
{	
	CodeFound = CheckLungTransplant(I8000);
		if (CodeFound)
		{
		I8000Score = 3;
		*I8000Code = 1;
		}
}
if (!CodeFound)
{	
	CodeFound = CheckMajorOrgan(I8000);
		if (CodeFound)
		{
		I8000Score = 2;
		*I8000Code = 2;
		}
}
if (!CodeFound)
{
	CodeFound = CheckOpportunisticInfections(I8000);
		if (CodeFound)
		{
		I8000Score = 2;
		*I8000Code = 3;
		}
}
if (!CodeFound)
{
	CodeFound = CheckBoneNecrosis(I8000);
		if (CodeFound)
		{
		I8000Score = 2;
		*I8000Code = 4;
		}
}
if (!CodeFound)
{
    CodeFound = CheckMyeloidLeukemia(I8000);
		if (CodeFound)
		{
		I8000Score = 2;
		*I8000Code = 5;
		}
}
if (!CodeFound)
{
	CodeFound = CheckEndocarditis(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 6;
		}
}
if (!CodeFound)
{
	CodeFound = CheckImmuneDisorders(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 7;
		}
}
if (!CodeFound)
{
	CodeFound = CheckLiverDisease(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 8;
		}
}
if (!CodeFound)
{
	CodeFound = CheckNarcolepsy(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 9;
		}
}
if (!CodeFound)
{
	CodeFound = CheckCysticFibrosis(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 10;
		}
}
if (!CodeFound)
{
	CodeFound = CheckSpecificImmuneDisorders(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 11;
		}
}
if (!CodeFound)
{
	CodeFound = CheckMorbidObesity(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 12;
		}
}
if (!CodeFound)
{
	CodeFound = CheckPsoriaticAnthropathy(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 13;
		}
}
if (!CodeFound)
{
	CodeFound = CheckChronicPancreatitis(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 14;
		}
}
if (!CodeFound)
{
	CodeFound = CheckProlificRetinopathy(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 15;
		}
}
if (!CodeFound)
{
	CodeFound = CheckImplantedDevice(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 16;
		}
}
if (!CodeFound)
{
	CodeFound = CheckAscepticNecrosis(I8000);
		if (CodeFound == true)
		{
		I8000Score = 1;
		*I8000Code = 17;
		}
}
if (!CodeFound)
{
	CodeFound = CheckRespiratoryFailure(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 18;
		}
}
if (!CodeFound)
{
	CodeFound = CheckMyelodysplastic(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 19;
		}
}
if (!CodeFound)
{
	CodeFound = CheckTissueDisorders(I8000);
		if (CodeFound == true)
		{
		I8000Score = 1;
		*I8000Code = 20;
		}
}
if (!CodeFound)
{
	CodeFound = CheckDiabeticRetinopathy(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 21;
		}
}
if (!CodeFound)
{
	CodeFound = CheckSevereSkin(I8000);
		if (CodeFound == true)
		{	
		I8000Score = 1;
		*I8000Code = 22;
		}
}
if (!CodeFound)
{
	CodeFound = CheckIntractableEpilepsy(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 23;
		}
}
if (!CodeFound)
{	
	CodeFound = CheckImmuneDisorder(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 24;
		}
}
if (!CodeFound)
{
	CodeFound = CheckCirrhosisLiver(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 25;
		}
}
if (!CodeFound)
{
	CodeFound = CheckRespiratoryArrest(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 26;
		}
}
if (!CodeFound)
{
	CodeFound = CheckPulmonaryFibrosis(I8000);
		if (CodeFound)
		{
		I8000Score = 1;
		*I8000Code = 27;
		}
}


return I8000Score;
}

int ReturnSLPScore(char* I8000)
{int SLPScore = 0;
bool ALSSLP = false; bool ApraxiaSLP = false; bool DyspagiaSLP = false;
bool LaryngealCancer = false; bool OralCancer = false; bool SpeechLanguage = false;
bool CodeFoundSLP = false;

if (!CodeFoundSLP)
{
    CodeFoundSLP = CheckALS(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}
if (!CodeFoundSLP)
{
	CodeFoundSLP = CheckApraxia(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}
if (!CodeFoundSLP)
{
	CodeFoundSLP = CheckDyspagia(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}
if (!CodeFoundSLP)
{
	CodeFoundSLP = CheckLaryngealCancer(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}
if (!CodeFoundSLP)
{
	CodeFoundSLP = CheckOralCancer(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}
if (!CodeFoundSLP)
{
    CodeFoundSLP = CheckSpeechLanguage(I8000);
		if (CodeFoundSLP)
		{
		SLPScore = 1;
		}
}

return SLPScore;
}