#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "ReducedPhysicalFunction.h"


char CalcReducedPhysicalFunction(int RestorativeNursingCount, int NursingFunctionScore)
	{	
		char ReducedFunctionCaseMix[6];
		char HIPPSCode3; 

		if ((NursingFunctionScore <= 5) && (NursingFunctionScore >= 0))
			{
					if ((RestorativeNursingCount<=1) && (RestorativeNursingCount>=0))
					{
					strcpy( ReducedFunctionCaseMix, "PDE1" ); HIPPSCode3 = 'U';
					}
					else if (RestorativeNursingCount>=2)
					{
					strcpy( ReducedFunctionCaseMix, "PDE2" ); HIPPSCode3 = 'T';
					}
			}	
			else if ((NursingFunctionScore <= 14) && (NursingFunctionScore >= 6))
			{
					if ((RestorativeNursingCount<=1) && (RestorativeNursingCount>=0))
					{
					strcpy( ReducedFunctionCaseMix, "PBC1" ); HIPPSCode3 = 'X';
					}
					else if (RestorativeNursingCount>=2)
					{
					strcpy( ReducedFunctionCaseMix, "PBC2" ); HIPPSCode3 = 'V';
					}
			}	
			else if ((NursingFunctionScore <= 16) && (NursingFunctionScore >= 15))
			{
					if ((RestorativeNursingCount<=1) && (RestorativeNursingCount>=0))
					{
					strcpy( ReducedFunctionCaseMix, "PA1" ); HIPPSCode3 = 'Y';
					}
					else if (RestorativeNursingCount>=2)
					{
					strcpy( ReducedFunctionCaseMix, "PA2" ); HIPPSCode3 = 'W';
					}
			}	
	return HIPPSCode3;
	}