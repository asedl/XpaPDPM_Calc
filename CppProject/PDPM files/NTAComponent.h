bool DetermineNTAFeeding(char* K0510A2);
int DetermineNTAFeedingScore(char* K0710A2, char* K0710B2);

int DetermineIntravenousMedication(char* O0100H2);
int DetermineVentilator(char* O0100F2);
int DetermineTransfusion(char* O0100I2);
int DetermineMultipleSclerosis(char* I5200);
int DetermineAsthmaCOPD(char* I6200);
int DetermineWoundInfection(char* I2500);
int DetermineDiabetesMellitus(char* I2900);
int DetermineDiabeticFoot(char* M1040B);
int DetermineTracheostomyCare(char* O0100E2);
int DetermineResistantOrganism(char* I1700);
int DetermineIsolation(char* O0100M2);
int DetermineRadiation(char* O0100B2);
int DetermineUnhealedPressure(char* M0300D1);
int DetermineIntermittentCatheterization(char* H0100D);
int DetermineInflammatoryBowel(char* I1300);
int DetermineSuctioning(char* O0100D2);
int DetermineFeedingTube(char* K0510B2);
int DetermineMalnutrition(char* I5600);
int DetermineOstomy(char* H0100C);
int DetermineFootInfection(char* M1040A, char* M1040C);

int DetermineNTAScore(int I8000Score, int IntraVenousMedicationScore, int VentilatorScore,
					  int TransfusionScore, int MultipleSclerosisScore, int AsthmaCOPDScore,
					  int WoundInfectionScore, int DiabetesMellitusScore, int DiabeticFootScore,
					  int TracheostomyCareScore, int ResistantOrganismScore, int IsolationScore,
					  int RadiationScore, int UnhealedPressureScore, int IntermittentCatheterizationScore, int InflammatoryBowelScore, 
					  int SuctioningScore, int FeedingTubeScore, int MalnutritionScore, int OstomyScore, int FootInfectionScore, int NTAFeedingScore);

char DetermineCaseMixGroupNTA(int NTAScore);