#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "ClinicallyComplex.h"


bool DetermineClinicallyComplexStep1(char* I2000, char* I4900, char* M1040D, char* M1040E, char* M1040F,
							char* M1200F, char* M1200G, char* M1200H, char* O0100A2, char* O0100C2, char* O0100H2, 
							char* O0100I2, int NursingFunctionScore)
{bool CCStep1 = false;   
		if ((0 == strcmp(I2000, "1")) 
		|| ((0 == strcmp(I4900, "1")) && (NursingFunctionScore <= 11))
		|| ((0 == strcmp(M1040D, "1") || 0 == strcmp(M1040E, "1")) && (0 == strcmp(M1200F, "1") || 0 == strcmp(M1200G, "1") || 0 == strcmp(M1200H, "1") ))
		|| (0 == strcmp(M1040F, "1"))
		|| (0 == strcmp(O0100A2, "1"))
		|| (0 == strcmp(O0100C2, "1"))
		|| (0 == strcmp(O0100H2, "1"))
		|| (0 == strcmp(O0100I2, "1")))
		{
		CCStep1 = true;
		}

 return CCStep1;
}

char DetermineClinicallyComplexStep3(bool IsDepressed, int NursingFunctionScore)
	{	
		char ClinicallyComplexCaseMix[6];
		char HIPPSCode3; 

		if ((NursingFunctionScore <= 5) && (NursingFunctionScore >= 0))
			{
					if (IsDepressed)
					{
					strcpy( ClinicallyComplexCaseMix, "CDE2" ); HIPPSCode3 = 'L';
					}
					else if (!IsDepressed)
					{
					strcpy( ClinicallyComplexCaseMix, "CDE1" ); HIPPSCode3 = 'M';
					}
			}	
			else if ((NursingFunctionScore <= 14) && (NursingFunctionScore >= 6))
			{
					if (IsDepressed)
					{
					strcpy( ClinicallyComplexCaseMix, "CBC2" ); HIPPSCode3 = 'N';
					}
					else if (!IsDepressed)
					{
					strcpy( ClinicallyComplexCaseMix, "CBC1" ); HIPPSCode3 = 'P';
					}
			}	
			else if ((NursingFunctionScore <= 16) && (NursingFunctionScore >= 15))
			{
					if (IsDepressed)
					{
					
					strcpy( ClinicallyComplexCaseMix, "CA2" ); HIPPSCode3 = 'O';
					}
					else if (!IsDepressed)
					{
					strcpy( ClinicallyComplexCaseMix, "CA1" ); HIPPSCode3 = 'Q';
					}
			}	
	return HIPPSCode3;
	}
