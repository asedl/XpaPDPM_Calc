#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "ICD10Retrieval.h"
#include "DiagnosisCodesLoaded.h"


int DeterminePrimaryDiagnosis(char* I0020B)
{int FoundIndex=0;

for( int index = 0; index < 37312; index++ ) {
      if(0 == strcmp(I0020B, I0020BCodes[index]))
	  {
	  FoundIndex = index;
	  break;
	  }
   }
return FoundIndex;
}


bool DetermineOrthopedicSurgeryEligibility(int SurgeryIndex)
{bool OrthopedicSurgeryEligible = false;
int RetrievedOrthopedicValue = 0; 
RetrievedOrthopedicValue = I0020BEligibility[SurgeryIndex];
 if(I0020BEligibility[SurgeryIndex]== 3)
 {
	OrthopedicSurgeryEligible = true;
//	printf("This I0020B is Major Surgery Eligible!!");
 }
return OrthopedicSurgeryEligible;
}

bool DetermineNonOrthopedicSurgeryEligibility(int SurgeryIndex)
{bool NonOrthopedicSurgeryEligible = false;

 if(I0020BEligibility[SurgeryIndex]==2)
 {
 NonOrthopedicSurgeryEligible = true;
 }
return NonOrthopedicSurgeryEligible;
}

bool DetermineRecentSurgery(char* J2100)
{bool RecentSurgery = false;

 if (0 == strcmp(J2100, "1"))
 {
 RecentSurgery = true;
 }
 return RecentSurgery;
}

bool DetermineMajorSurgery(char* J2300, char* J2310, char* J2320, char* J2330, char* J2400, char* J2410, char* J2420)
{bool MajorSurgery = false;

	if ((0 == strcmp (J2300, "1")) 
		|| (0 == strcmp (J2310, "1"))
		|| (0 == strcmp (J2320, "1")) 
		|| (0 == strcmp (J2330, "1"))
		|| (0 == strcmp (J2400, "1"))
		|| (0 == strcmp (J2410, "1"))
		|| (0 == strcmp (J2420, "1")))
	{
	MajorSurgery = true;
	}
	return MajorSurgery;
}


bool DetermineOrthopedicSurgery(char* J2500, char* J2510, char* J2520, char* J2530)
{ bool OrthopedicSurgery = false;
			
		 if	((0 == strcmp (J2500, "1"))
			|| (0 == strcmp (J2510, "1"))
			|| (0 == strcmp (J2520, "1"))
			|| (0 == strcmp (J2530, "1")))
		 {
		 OrthopedicSurgery = true;
		 }
  return OrthopedicSurgery;
}

bool DetermineNonOrthopedicSurgery(char* J2600, char* J2610, char* J2620, char* J2700, char* J2710, char* J2800, char* J2810, 
								   char* J2900, char* J2910, char* J2920, char* J2930, char* J2940)
{bool NonOrthopedicSurgery = false;

		 if	((0 == strcmp (J2600, "1"))
			|| (0 == strcmp (J2610, "1"))
			|| (0 == strcmp (J2620, "1"))
			|| (0 == strcmp (J2700, "1"))
			|| (0 == strcmp (J2710, "1"))
			|| (0 == strcmp (J2800, "1"))
			|| (0 == strcmp (J2810, "1"))
			|| (0 == strcmp (J2900, "1"))
			|| (0 == strcmp (J2910, "1"))
			|| (0 == strcmp (J2920, "1"))
			|| (0 == strcmp (J2930, "1"))
			|| (0 == strcmp (J2940, "1")))
		 {
		 NonOrthopedicSurgery = true;
		 }

 return NonOrthopedicSurgery; 
}