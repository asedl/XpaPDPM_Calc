 // char ICD10Codes[40000][9];

int DeterminePrimaryDiagnosis(char* I0020B);

bool DetermineOrthopedicSurgeryEligibility(int SurgeryIndex);

bool DetermineNonOrthopedicSurgeryEligibility(int SurgeryIndex);

bool DetermineRecentSurgery(char* J2100);

bool DetermineMajorSurgery(char* J2300, char* J2310, char* J2320, char* J2330, char* J2400, char* J2410, char* J2420);

bool DetermineOrthopedicSurgery(char* J2500, char* J2510, char* J2520, char* J2530);

bool DetermineNonOrthopedicSurgery(char* J2600, char* J2610, char* J2620, char* J2700, char* J2710, char* J2800, char* J2810, 
								   char* J2900, char* J2910, char* J2920, char* J2930, char* J2940);
								
	