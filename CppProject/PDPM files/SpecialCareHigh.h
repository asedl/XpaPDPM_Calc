bool DetermineIfComatose(char* GG0130A1, char* GG0130C1, char* GG0170B1, char* GG0170C1, 
						 char* GG0170D1, char* GG0170E1, char* GG0170F1);

bool DetermineSepticemia(char* I2100);

bool DetermineDiabetes(char* I2900, int InsulinInjections, int InsulinOrders);

bool DetermineQuadriplegia(char* I5100, int NursingScore);

bool DeterminePulmonaryDisease(char* I6200, char* J1100C);

bool DetermineFever(char* J1550A);

bool DetermineFeverCombo(bool Fever, bool Pneumonia, bool Vomiting, bool WeightLoss, bool FeedTube);

bool DetermineRespiratory(int RespiratoryTherapyDays);

bool DetermineParenteralFeeding(char* K0510A1, char* K0510A2);

bool DeterminePneumonia(char* I2000);

bool DetermineVomiting(char* J1550B);

bool DetermineWeightLoss(char* K0300);



bool SpecialCareHighStep1(bool Comatose, bool Septicemia, bool Diabetes, bool Quadriplegia,
						  bool PulmonaryDisease, bool Fever, bool Parenteral, bool Respiratory);

char SpecialCareHighStep4 (int PDPMFunctionScoreNursing, bool IsDepressed);