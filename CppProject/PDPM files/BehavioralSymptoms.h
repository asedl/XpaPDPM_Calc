int DetermineImpairmentIndicators1(int B0700Converted, char* C0700, int C1000Converted);

int DetermineImpairmentIndicators2(int B0700Converted, int C1000Converted);

bool DetermineSeverelyImpairedCognition(char* C1000);

int BehavioralSymptomsStep2(int BIMSCode, bool C0100Test);

bool BehavioralSymptomsStep3(bool Comatose, char* C1000, bool ImpairmentCheck);

bool BehavioralSymptomsStep4(char* E0100A, char* E0100B, char* E0200A, char* E0200B, char* E0200C,
							  char* E0800, char* E0900);

char BehavioralSymptomsStep6(int RestorativeNursingScore, int NursingFunctionScore);