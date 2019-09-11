
bool DetermineCerebralPalsy(char* I4400, int NursingScore);

bool DetermineMultipleSclerosis(char* I5200, int NursingScore);

bool DetermineParkinsons(char* I5300, int NursingScore);

bool DetermineRespiratoryFailure(char* I6300, char* O0100C2);

bool DetermineRadiationBool(char* O0100B2);

bool DetermineDialysis(char* O0100J2);

bool DetermineFootCheck(char* M1040A, char* M1040B, char* M1040C, char* M1200I);

int DetermineSkinTreatments(char* M1200A, char* M1200B, char* M1200C, char* M1200D, char* M1200E,
							char* M1200G, char* M1200H);

bool CalcUlcerCheck1(int Stage2UlcerCount, int SkinTreatmentCount);

bool CalcUlcerCheck2(int Stage3UlcerCount, int Stage4UlcerCount, int UnstagedUlcerCount, int SkinTreatmentCount);

bool CalcUlcerCheck3(int VenousUlcerCount, int SkinTreatmentCount);

bool CalcUlcerCheck4(int Stage2UlcerCount, int VenousUlcerCount, int SkinTreatmentCount);

bool SpecialCareLowStep1 (bool CerebralPalsy, bool Sclerosis, bool Parkinsons, bool RespiratoryFailure,
						  bool FeedingTube, bool Ulcer1, bool Ulcer2, bool Ulcer3, bool Ulcer4, 
						  bool FootCheck, bool Radiation, bool Dialysis);

char SpecialCareLowStep4 (int PDPMFunctionScoreNursing, bool IsDepressed);