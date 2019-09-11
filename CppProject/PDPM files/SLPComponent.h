int DetermineSwallowingDisorder(char* K0100A, char* K0100B, char* K0100C, char* K0100D);

int DetermineMechanicallyAltered(char* K0510C2);

int DetermineVentilatorSLP(char* O0100F2);

int DetermineAphasia(char* I4300);

int DetermineCVA(char* I4500);

int DetermineHemiplegia(char* I4900);

int DetermineTraumaticBrain(char* I5500);

int CalcMiscSLPComorbidity(int Aphasia, int CVA, int Hemiplegia, int TraumaticBrain, 
							int Tracheostomy, int Ventilator);

int DetermineClinicalCategorySLP(int FoundIndex);

int DetermineCognitiveLevel(char* C0500, char* B0100, char* GG0130A1, char* GG0130C1, char* GG0170B1, char* GG0170C1, 
							  char* GG0170D1, char* GG0170E1, char* GG0170F1, char* C1000, char* B0700, char*C0700, 
							  int PassedBIMSScore);

char DetermineCaseMixGroupSLP(int Category1Count, int Category2Count);