float CalcEatingFunctionScorePT(char* GG0130A1);
float CalcOralHygieneFunctionScorePT(char* GG0130B1);
float CalcToiletingHygieneFunctionScorePT(char* GG0130C1);
float CalcSitToLyingFunctionScorePT(char* GG0170B1);
float CalcLyingToSittingFunctionScorePT(char* GG0170C1);
float CalcSitToStandFunctionScorePT(char* GG0170D1);
float CalcChairBedFunctionScorePT(char* GG0170E1);
float CalcToiletTransferFunctionScorePT(char* GG0170F1);
float CalcWalkFiftyFunctionScorePT(char* GG0170J1);
float CalcWalkOneFiftyFunctionScorePT(char* GG0170K1);

float	CalcPDPMFunctionScorePT(float EatingFunctionScorePT, float OralHygieneFunctionScorePT, float ToiletingHygieneFunctionScorePT,
							float BedAveragePT, float TransferAveragePT, float WalkingAveragePT);

int DetermineClinicalCategoryPT(int FoundIndex);

char DetermineCaseMixGroupPT(int ClinicalCategoryNumberCode, int FunctionScore);