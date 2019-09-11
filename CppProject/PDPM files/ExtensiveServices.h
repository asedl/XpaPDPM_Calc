bool DetermineTracheostomyCareBool(char* O0100E2);
bool DetermineVentilatorBool(char* O0100F2);
bool DetermineIsolationBool(char* O0100M2);

bool ExtensiveServicesStep1(bool Tracheostomy, bool Ventilator, bool Isolation);

bool ESStep2(int NursingScore);

char* CalcExtensiveServicesCaseMixGroup(char* ClinicalCategory, int FunctionScore);