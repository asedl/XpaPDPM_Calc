float CalcEatingFunctionScoreNursing(char* GG0130A1);
float CalcToiletingHygieneFunctionScoreNursing(char* GG0130C1);
float CalcSitToLyingFunctionScoreNursing(char* GG0170B1);
float CalcLyingToSittingFunctionScoreNursing(char* GG0170C1);
float CalcSitToStandFunctionScoreNursing(char* GG0170D1);
float CalcChairBedFunctionScoreNursing(char* GG0170E1);
float CalcToiletTransferFunctionScoreNursing(char* GG0170F1);


float CalcPDPMFunctionScoreNursing(float EatingFunctionScore, float ToiletingHygieneFunctionScore,
							float BedAverage, float TransferAverage);


