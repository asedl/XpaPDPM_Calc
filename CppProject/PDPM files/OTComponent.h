int CalcEatingFunctionScoreOT(char* GG0130A1);
int CalcOralHygieneFunctionScoreOT(char* GG0130B1);
int CalcToiletingHygieneFunctionScoreOT(char* GG0130C1);
float CalcSitToLyingFunctionScoreOT(char* GG0170B1);
float CalcLyingToSittingFunctionScoreOT(char* GG0170C1);
float CalcSitToStandFunctionScoreOT(char* GG0170D1);
float CalcChairBedFunctionScoreOT(char* GG0170E1);
float CalcToiletTransferFunctionScoreOT(char* GG0170F1);
float CalcWalkFiftyFunctionScoreOT(char* GG0170J1);
float CalcWalkOneFiftyFunctionScoreOT(char* GG0170K1);

int	CalcPDPMFunctionScoreOT(int EatingFunctionScoreOT, int OralHygieneFunctionScoreOT, int ToiletingHygieneFunctionScoreOT,
							int BedAverageOT, int TransferAverageOT, int WalkingAverageOT);

int DetermineClinicalCategoryOT(int FoundIndex);

char* DetermineCaseMixGroupOT(char* ClinicalCategory, int FunctionScore);