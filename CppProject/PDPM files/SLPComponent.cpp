#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "SLPComponent.h"
#include "mdsgrouper.h"
#include "DiagnosisCodesLoaded.h"

int DetermineVentilatorSLP(char* O0100F2)
{int Ventilator = 0;
 if (0 == strcmp(O0100F2, "1"))
  {
  Ventilator = 1; 
  }
 return Ventilator;
}

int DetermineSwallowingDisorder(char* K0100A, char* K0100B, char* K0100C, char* K0100D)
{
		int SwallowingDisorder = 0;
		
		if ((0 == strcmp(K0100A, "1")) 
		|| (0 == strcmp(K0100B, "1")) 
		|| (0 == strcmp(K0100C, "1"))
		|| (0 == strcmp(K0100D, "1")))
		{
		SwallowingDisorder = 1;
		}
		return SwallowingDisorder;
}

int DetermineMechanicallyAltered(char* K0510C2)
{
		int MechanicallyAltered = 0;

		if (0 == strcmp(K0510C2, "1")) 
		{
		MechanicallyAltered = 1;
		}
		return MechanicallyAltered;

}

int DetermineAphasia(char* I4300)
{int Aphasia = 0;
 if (0 == strcmp(I4300, "1"))
  {
  Aphasia = 1; 
  }
 return Aphasia;
}

int DetermineCVA(char* I4500)
{int CVA = 0;
 if (0 == strcmp(I4500, "1"))
  {
  CVA = 1; 
  }
 return CVA;
}

int DetermineHemiplegia(char* I4900)
{int Hemiplegia = 0;
 if (0 == strcmp(I4900, "1"))
  {
  Hemiplegia = 1; 
  }
 return Hemiplegia;
}

int DetermineTraumaticBrain(char* I5500)
{int TraumaticBrain = 0;
 if (0 == strcmp(I5500, "1"))
  {
  TraumaticBrain = 1; 
  }
 return TraumaticBrain;
}

int DetermineClinicalCategorySLP(int FoundIndex)
{int ReturnedCategoryValue;

     ReturnedCategoryValue = I0020BSLPCode[FoundIndex];

	 return ReturnedCategoryValue;
}

int CalcMiscSLPComorbidity(int Aphasia, int CVA, int Hemiplegia, int TraumaticBrain, 
							int Tracheostomy, int Ventilator)
{int MiscSLPComorbidity = 0;

if ((Aphasia == 1) || (Hemiplegia == 1) || (TraumaticBrain == 1) || (CVA==1) ||
	(Tracheostomy == 1) || (Ventilator == 1)) 
	{
	MiscSLPComorbidity = 1;
	}
return MiscSLPComorbidity; 
}

int DetermineCognitiveLevel(char* C0500, char* B0100, char* GG0130A1, char* GG0130C1, char* GG0170B1, char* GG0170C1, 
							  char* GG0170D1, char* GG0170E1, char* GG0170F1, char* C1000, char* B0700, char*C0700, 
							  int PassedBIMSScore)
{
    char CognitiveLevel[20];
	int BasicImpairmentCount=0;
	int SevereImpairmentCount=0;
	int BIMSScore=0;
	int CognitiveValue=0;

	if  (0 == strcmp( C0500, "99" ) ||  0 == strcmp( C0500, "- " ) ||  0 == strcmp( C0500, " -" )|| 0 == strcmp( C0500, "  " ))
	{ 
	
		if ((0 == strcmp (B0100, "1") 
		&& (0 == strcmp(GG0130A1, "01") || 0 == strcmp(GG0130A1, "09") || 0 == strcmp(GG0130A1, "88"))
		&& (0 == strcmp(GG0130C1, "01") || 0 == strcmp(GG0130C1, "09") || 0 == strcmp(GG0130C1, "88"))
		&& (0 == strcmp(GG0170B1, "01") || 0 == strcmp(GG0170B1, "09") || 0 == strcmp(GG0170B1, "88"))
		&& (0 == strcmp(GG0170C1, "01") || 0 == strcmp(GG0170C1, "09") || 0 == strcmp(GG0170C1, "88"))
		&& (0 == strcmp(GG0170D1, "01") || 0 == strcmp(GG0170D1, "09") || 0 == strcmp(GG0170D1, "88"))
		&& (0 == strcmp(GG0170E1, "01") || 0 == strcmp(GG0170E1, "09") || 0 == strcmp(GG0170E1, "88"))
		&& (0 == strcmp(GG0170F1, "01") || 0 == strcmp(GG0170F1, "09") || 0 == strcmp(GG0170F1, "88")))
		|| (0 == strcmp(C1000, "3")))
		{
		strcpy( CognitiveLevel, "Severely Impaired" );
		CognitiveValue = 1;
		}
		else 
		{
			if (0 == strcmp(C1000, "1") || 0 == strcmp(C1000, "2"))
			{BasicImpairmentCount=BasicImpairmentCount+1;
		//	printf("The Basic Impairment count is = %i\n",BasicImpairmentCount);
			}

			if (0 == strcmp(B0700, "1") || 0 == strcmp(B0700, "2") || 0 == strcmp(B0700, "3"))
			{BasicImpairmentCount=BasicImpairmentCount+1;}
			
			if (0 == strcmp(C0700, "1"))
			{BasicImpairmentCount=BasicImpairmentCount+1;}

			if (0 == strcmp(C1000, "2"))
			{SevereImpairmentCount=SevereImpairmentCount+1;}

			if (0 == strcmp(B0700, "2") || 0 == strcmp(B0700, "3"))
			{SevereImpairmentCount=SevereImpairmentCount+1;}


				if ((SevereImpairmentCount == 1 || SevereImpairmentCount == 2) && (BasicImpairmentCount == 2 || BasicImpairmentCount == 3))
				{strcpy( CognitiveLevel, "Moderately Impaired" );
				CognitiveValue = 1;}

				else if ((SevereImpairmentCount == 0 || SevereImpairmentCount == 1 || SevereImpairmentCount == 2) && (BasicImpairmentCount == 1))
				{strcpy( CognitiveLevel, "Mildly Impaired" );
				CognitiveValue = 1;}

				else if ((BasicImpairmentCount == 2 || BasicImpairmentCount == 3) && (SevereImpairmentCount == 0))
				{strcpy( CognitiveLevel, "Mildly Impaired" );
				CognitiveValue = 1;}

				else if ((BasicImpairmentCount == 0) && (SevereImpairmentCount == 0))
				{strcpy( CognitiveLevel, "Cognitively Intact" );
				CognitiveValue = 0;}

				return CognitiveValue;
		}
	}
	else
	{
		BIMSScore = PassedBIMSScore;

		if ((BIMSScore<=7)&&(BIMSScore>=0))
			{strcpy( CognitiveLevel, "Moderately Impaired" );
			CognitiveValue = 1;
			}

		else if ((BIMSScore>=8)&&(BIMSScore<=12))
			{strcpy( CognitiveLevel, "Mildly Impaired" );
			CognitiveValue = 1;
			}

		else if ((BIMSScore>=13)&&(BIMSScore<=15))
			{strcpy( CognitiveLevel, "Cognitively Intact" );
			CognitiveValue = 0;
			}

		return CognitiveValue;
	}
	return CognitiveValue;
}

char DetermineCaseMixGroupSLP(int Category1Count, int Category2Count)
{
    char CaseMixGroupSLP[3];
	char HIPPSCodeGenerated2;
	
	if ((Category1Count == 0) && (Category2Count == 0))
	{strcpy( CaseMixGroupSLP, "SA" ); HIPPSCodeGenerated2 = 'A';}

	else if ((Category1Count == 0) && (Category2Count == 1))
	{strcpy( CaseMixGroupSLP, "SB" ); HIPPSCodeGenerated2 = 'B';}

	else if((Category1Count == 0) && (Category2Count == 2))
	{strcpy( CaseMixGroupSLP, "SC" ); HIPPSCodeGenerated2 = 'C';}

	else if ((Category1Count == 1) && (Category2Count == 0))
	{strcpy( CaseMixGroupSLP, "SD" ); HIPPSCodeGenerated2= 'D';}

	else if ((Category1Count == 1) && (Category2Count == 1))
	{strcpy( CaseMixGroupSLP, "SE" ); HIPPSCodeGenerated2 = 'E';}

	else if ((Category1Count == 1) && (Category2Count == 2))
	{strcpy( CaseMixGroupSLP, "SF" ); HIPPSCodeGenerated2 = 'F';}

	else if ((Category1Count == 2) && (Category2Count == 0))
	{strcpy( CaseMixGroupSLP, "SG" ); HIPPSCodeGenerated2 = 'G';}

	else if ((Category1Count == 2) && (Category2Count == 1))
	{strcpy( CaseMixGroupSLP, "SH" ); HIPPSCodeGenerated2 = 'H';}

	else if ((Category1Count == 2) && (Category2Count == 2))
	{strcpy( CaseMixGroupSLP, "SI" ); HIPPSCodeGenerated2 = 'I';}

	else if ((Category1Count == 3) && (Category2Count == 0))
	{strcpy( CaseMixGroupSLP, "SJ" ); HIPPSCodeGenerated2 = 'J';}

	else if ((Category1Count == 3) && (Category2Count == 1))
	{strcpy( CaseMixGroupSLP, "SK" ); HIPPSCodeGenerated2 = 'K';}

	else if ((Category1Count == 3) && (Category2Count == 2))
	{strcpy( CaseMixGroupSLP, "SL" ); HIPPSCodeGenerated2 = 'L';}
	
	return HIPPSCodeGenerated2;
}
