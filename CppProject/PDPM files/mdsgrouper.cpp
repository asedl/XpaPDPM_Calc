#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <math.h>
//#include <ctime>
#include "mdsgrouper.h"
#include "PTComponent.h"
#include "OTComponent.h"
#include "SLPComponent.h"
#include "NTAComponent.h"
#include "ICD10Retrieval.h"
#include "MiscCalculations.h"
#include "CheckI8000Logic.h"
#include "BehavioralSymptoms.h"
#include "ClinicallyComplex.h"
#include "ExtensiveServices.h"
#include "ReducedPhysicalFunction.h"
#include "SpecialCareHigh.h"
#include "SpecialCareLow.h"
#include "NursingComponent.h"
#include "ConvertToDigit.h"
#include "tinystr.h"
#include "tinyxml.h"

#include "userdll.h"

/*-------------------------------------------------------------------------*/
/* forward declarations and Magic UDF/UDP stuff ...
/*-------------------------------------------------------------------------*/
extern "C"
{
	__declspec(dllexport) void* MAGIC_BIND(void);
	__declspec(dllexport) long* pdpm_mdsgrouper(char* p1, char* p2, long* errorcode, char* hipps, long* retval);
}

#define FUNC_CNT     1

static FUNC_DSC fdsc_tbl[FUNC_CNT] =
{
	{ (Uchar *) "pdpm_mdsgrouper", pdpm_mdsgrouper, 5, (unsigned char*) "AALALL" }
};

static EXT_MODULE ext_module = { 0, NULL_PTR, NULL_PTR, FUNC_CNT, fdsc_tbl, (Uchar*) "pdpm" };

__declspec(dllexport) void* MAGIC_BIND(void)
{
	return (&ext_module);
}

long* pdpm_mdsgrouper(char* pdpmRecord, char* HIPPSCode, long* errorcode, char* HIPPSVersionCode, long* retval) {
	*retval = mdsgrouper(pdpmRecord, HIPPSCode, (int *) errorcode, HIPPSVersionCode);
	return retval;
}





void FillRecord( char * Assessment, char * sData, int iLoc, int len )
{
    iLoc--;
	for ( int i=0; i<len; i++ ) Assessment[iLoc + i] = sData[i];
}
void FillData( char * Assessment, char * sData, int iLoc, int iLen )
{
        iLoc--;
        for ( int i=0; i<iLen; i++ ) sData[i] = Assessment[iLoc + i];
        sData[iLen] = '\0';
}

void Trim_I0020B( char * I0020B )
{
         char TrimI0020[9];
         int i, j;
         j = 0;
         for ( i=0; i<8; i++ )
               if ((I0020B[i] != '^') && (I0020B[i] != '.')  && (I0020B[i] != ' ') )
			   {
                   TrimI0020[j++] = I0020B[i];
			//	   printf("The I0020 Code letter = %c\n",I0020B[i]);
			   }
         TrimI0020[j] = '\0';
//		 printf("The I0020 Code is = %s\n",TrimI0020);

         strcpy( I0020B, TrimI0020 );
}

#ifdef SOLARIS

	 int mdsgrouper( char* pdpmRecord, char* HIPPSCode,  int* errorcode, char* HIPPSVersionCode )

#else

extern "C" __declspec(dllexport) 


		int __stdcall mdsgrouper( char* pdpmRecord, char* HIPPSCode,  int* errorcode, char* HIPPSVersionCode )

#endif
{
	char* CognitiveLevel; 

//	char HIPPSCodeGenerated[6] = {' ',' ',' ',' ',' ','\0'}; 

	strcpy(HIPPSVersionCode, "1.0000");
	*errorcode = 0;
	int I8000ACode = 0;
	int I8000BCode = 0;
	int I8000CCode = 0;
	int I8000DCode = 0;
	int I8000ECode = 0;
	int I8000FCode = 0;
	int I8000GCode = 0;
	int I8000HCode = 0;
	int I8000ICode = 0;
	int I8000JCode = 0;


	char PrimaryDiagnosisPT[75];
	int PTClinicalCategory = 0;
	int ClinicalCategoryNumberCodeOT = 0;
	int ClinicalCategoryNumberCodePT = 0;
	int ClinicalCategoryNumberCodeSLPPrelim = 0;
	int ClinicalCategoryNumberCodeSLPFinal = 0;
	int SLPAcuteNeurologicalCount = 0;
	int SLPComorbidityScoreCombinedPrelim = 0;
	int SLPComorbidityScoreCombinedFinal = 0;
	bool C0100Test = false;
	bool OrthopedicSurgeryEligible = false; 
	bool NTAFeeding = false;
	bool NonOrthopedicSurgeryEligible = false; 
	bool RecentSurgery = false; bool MajorSurgery = false; bool OrthopedicSurgery = false;
	bool NonOrthopedicSurgery = false; bool CCStep1 = false;
	bool TracheostomyBool = false; bool VentilatorBool = false; bool IsolationBool = false;
	bool IsDepressed = false; bool IsComatose = false; bool HasSepticemia = false; bool IsDiabetic = false;
	bool IsQuadriplegic = false; bool HasPulmonary = false; bool HasFever = false;
	bool HadFeedings = false; bool HadRespiratoryTherapy = false; bool HasParenteralFeeding = false;
	bool HasCerebralPalsy = false; bool HasMultipleSclerosis = false; bool HasParkinsons = false;
	bool HasRespiratoryFailure = false; bool HasStage2PressureUlcers = false; bool FootCheck;
	bool HasStage3PressureUlcers = false; bool ClinicallyComplexStep1 = false;
	bool HadRadiationTreatment = false; bool HadDialysisTreatment = false;
	bool NursingScore14 = false; bool NursingScore15 = false;
	bool UlcerCheck1 = false; bool UlcerCheck2 = false; bool UlcerCheck3 = false; bool UlcerCheck4 = false;
	bool Pneumonia = false; bool Vomiting = false; bool WeightLoss = false; bool FeedingTube = false; 
	bool SpecialCareHigh1 = false; bool FeverCombo = false; bool SpecialCareLow1 = false;
	bool ExtensiveServices1 = false; bool BehavioralSymptoms3 = false;
	bool BehavioralSymptoms4 = false; bool CheckImpairment = false;
	
    char NursingCaseMixES[6];
	int FoundIndex = 0; int DepressedScore1 = 0; int DepressedScore2 =0 ; int BIMSScore = 0;
    
	int PDPMFunctionScoreRounded = 0;
	int PresenceNeurologicCount = 0;
	int MechanicallyAlteredCount = 0;
	int RestorativeNursingCount = 0;
	
	char A0310B[3];
	char B0100[2], B0700[2]; 
	char C0100[2], C0500[3], C0700[2], C1000[2];
	char D0300[3], D0600[3];
	char E0100A[2], E0100B[2], E0200A[2], E0200B[2], E0200C[2], E0800[2], E0900[2];
	char GG0130A1[3], GG0130B1[3], GG0130C1[3], GG0170B1[3], GG0170C1[3], GG0170D1[3], GG0170E1[3], GG0170F1[3], GG0170I1[3];
	char GG0170J1[3], GG0170K1[3];
	char GG0130A5[3], GG0130B5[3], GG0130C5[3], GG0170B5[3], GG0170C5[3], GG0170D5[3], GG0170E5[3], GG0170F5[3], GG0170I5[3];
	char GG0170J5[3], GG0170K5[3];
	char H0100C[2], H0100D[2], H0200C[2], H0500[2];
	char I0020B[9], I1300[2], I1700[2], I2000[2], I2100[2], I2500[2], I2900[2], I4300[2], I4400[2], I4500[2], I4900[2], I5100[2]; 
	char I5200[2], I5300[2], I5500[2], I5600[2], I6200[2], I6300[2];
	char I8000A[9], I8000B[9], I8000C[9], I8000D[9], I8000E[9], I8000F[9], I8000G[9], I8000H[9], I8000I[9], I8000J[9];
	char J1100C[2], J1550A[2], J1550B[2], J2100[2], J2300[2], J2310[2], J2320[2], J2330[2], J2400[2], J2410[2], J2420[2], J2500[2], J2510[2];
	char J2520[2], J2530[2], J2600[2], J2610[2], J2620[2], J2700[2], J2710[2], J2800[2], J2810[2], J2900[2], J2910[2], J2920[2], J2930[2], J2940[2];
	char K0100A[2], K0100B[2], K0100C[2], K0100D[2], K0300[2], K0510A1[2], K0510A2[2], K0510B1[2], K0510B2[2], K0510C2[2];
	char K0710A2[2], K0710B2[2], K0710A3[2], K0710B3[2];
	char M0300B1[2], M0300C1[2], M0300D1[2], M0300F1[2], M1030[2], M1040A[2], M1040B[2], M1040C[2], M1040D[2], M1040E[2], M1040F[2]; 
	char M1200A[2], M1200B[2], M1200C[2], M1200D[2], M1200E[2], M1200F[2], M1200G[2], M1200H[2], M1200I[2];
	char N0350A[2], N0350B[2];
	char O0100A2[2], O0100B2[2], O0100C2[2], O0100D2[2], O0100E2[2], O0100F2[2], O0100H2[2],O0100I2[2], O0100J2[2], O0100M2[2], O0400D2[2];
	char O0500A[2], O0500B[2], O0500C[2], O0500D[2], O0500E[2], O0500F[2], O0500G[2], O0500H[2], O0500I[2], O0500J[2];
	 
	int CognitiveValue = 0; int CognitiveValue1 = 0; int CognitiveValue2 = 0; int CognitiveValue3 = 0;
	int AssessmentConvert = 0;

	float EatingFunctionScore = 0.0; float OralHygieneFunctionScore = 0.0; float ToiletingHygieneFunctionScore = 0.0;
	float PDPMFunctionScorePT = 0; float PDPMFunctionScoreNursing = 0;
	int PDPMFunctionScoreNursingRounded = 0;
	int Stage2UlcerConverted = 0; int Stage3UlcerConverted = 0; int Stage4UlcerConverted = 0; int UnstagedUlcerConverted = 0;
	int VenousUlcerConverted = 0; int SkinTreatmentCount = 0; int InsulinInjections = 0; int InsulinOrders = 0;
	int B0700Converted = 0; int C1000Converted = 0; int ImpairIndicate1 = 0; int ImpairIndicate2 = 0; int BehavioralSymptoms2 = 0;
	
	float SitToLyingFunctionScore = 0.0; float LyingToSittingFunctionScore = 0.0; 
	float SitToStandFunctionScore = 0.0; float ChairBedFunctionScore = 0.0; float ToiletTransferFunctionScore = 0.0;
	float WalkFiftyFunctionScore = 0.0; float WalkOneFiftyFunctionScore = 0.0;
	float BedMobilityAverage = 0.0; float TransferAverage = 0.0; float WalkingAverage = 0.0;
	float SitToLyingFunctionScoreOT = 0.0; float LyingToSittingFunctionScoreOT = 0.0;
	float SitToStandFunctionScoreOT = 0.0; float ChairBedFunctionScoreOT = 0.0;
	float ToiletTransferFunctionScoreOT = 0.0; float WalkFiftyFunctionScoreOT = 0.0;
	float WalkOneFiftyFunctionScoreOT = 0.0; float BedMobilityAverageOT = 0.0;
	float TransferAverageOT = 0.0; float WalkingAverageOT = 0.0;

	int NTAScoreI8000A = 0; int NTAScoreI8000B = 0; int NTAScoreI8000C = 0; int NTAScoreI8000D = 0;
	int NTAScoreI8000E = 0 ; int NTAScoreI8000F = 0; int NTAScoreI8000G = 0; int NTAScoreI8000H = 0;
	int NTAScoreI8000I = 0; int NTAScoreI8000J = 0; int NTAScoreToPass = 0;

	int SLPScoreI8000A = 0; int SLPScoreI8000B = 0; int SLPScoreI8000C = 0; int SLPScoreI8000D = 0;
	int SLPScoreI8000E = 0 ; int SLPScoreI8000F = 0; int SLPScoreI8000G = 0; int SLPScoreI8000H = 0;
	int SLPScoreI8000I = 0; int SLPScoreI8000J = 0; int SLPSummation = 0; int SLPSummationPass = 0;
	int C0100Int = 0;

	int I8000Score=0; int IntraVenousMedicationScore=0; int VentilatorScore=0; int TransfusionScore=0;
	int MultipleSclerosisScore=0; int AsthmaCOPDScore=0; int WoundInfectionScore=0; int DiabetesMellitusScore=0;
	int DiabeticFootScore=0; int TracheostomyCareScore=0; int ResistantOrganismScore=0; int IsolationScore=0;
	int RadiationScore=0; int UnhealedPressureScore=0; int FootInfectionScore=0; int IntermittentCatheterizationScore=0;
	int InflammatoryBowelScore=0; int SuctioningScore=0; int FeedingTubeScore=0; int MalnutritionScore=0;
	int OstomyScore=0; int NTAFeedingScore=0; int RespiratoryTherapy = 0;

	int AphasiaScore = 0; int CVAScore = 0; int HemiplegiaScore = 0; int TraumaticBrainScore = 0;
	int TracheostomySLPScore = 0; int VentilatorSLPScore = 0;

	int MiscSLPComorbidityScore = 0;

		*errorcode = 0;

		FillData( pdpmRecord, A0310B, 272, 2 );
		FillData( pdpmRecord, B0100, 511, 1 );
		FillData( pdpmRecord, B0700, 515, 1 );
		FillData( pdpmRecord, C0100, 519, 1 );
		FillData( pdpmRecord, C0500, 527, 2 );

		FillData( pdpmRecord, C0700, 530, 1 );
		FillData( pdpmRecord, C1000, 537, 1 );
		FillData( pdpmRecord, D0300, 562, 2 );
		FillData( pdpmRecord, D0600, 585, 2 );
		FillData( pdpmRecord, E0100A, 588, 1 );
		// 10
		FillData( pdpmRecord, E0100B, 589, 1 );
		FillData( pdpmRecord, E0200A, 591, 1 );
		FillData( pdpmRecord, E0200B, 592, 1 );
		FillData( pdpmRecord, E0200C, 593, 1 );
		FillData( pdpmRecord, E0800, 601, 1 );

		FillData( pdpmRecord, E0900, 602, 1 );
		FillData( pdpmRecord, H0100C, 684, 1 );
		FillData( pdpmRecord, H0100D, 685, 1 );
		FillData( pdpmRecord, H0200C, 689, 1 );
		FillData( pdpmRecord, H0500, 692, 1 );
		// 20
		FillData( pdpmRecord, I1300, 705, 1 );
		FillData( pdpmRecord, I1700, 710, 1 );
		FillData( pdpmRecord, I2000, 711, 1 );
		FillData( pdpmRecord, I2100, 712, 1 );
		FillData( pdpmRecord, I2500, 716, 1 );

		FillData( pdpmRecord, I2900, 717, 1 );
		FillData( pdpmRecord, I4300, 727, 1 );
		FillData( pdpmRecord, I4400, 728, 1 );
		FillData( pdpmRecord, I4500, 729, 1 );
		FillData( pdpmRecord, I4900, 731, 1 );
		// 30
		FillData( pdpmRecord, I5100, 733, 1 );
		FillData( pdpmRecord, I5200, 734, 1 );
		FillData( pdpmRecord, I5300, 736, 1 );
		FillData( pdpmRecord, I5500, 739, 1 );
		FillData( pdpmRecord, I5600, 740, 1 );

		FillData( pdpmRecord, I6200, 747, 1 );
		FillData( pdpmRecord, I6300, 748, 1 );
		FillData( pdpmRecord, I8000A, 751, 8 );
		FillData( pdpmRecord, I8000B, 759, 8 );
		FillData( pdpmRecord, I8000C, 767, 8 );
		// 40
		FillData( pdpmRecord, I8000D, 775, 8 );
		FillData( pdpmRecord, I8000E, 783, 8 );
		FillData( pdpmRecord, I8000F, 791, 8 );
		FillData( pdpmRecord, I8000G, 799, 8 );
		FillData( pdpmRecord, I8000H, 807, 8 );

		FillData( pdpmRecord, I8000I, 815, 8 );
		FillData( pdpmRecord, I8000J, 823, 8 );
		FillData( pdpmRecord, J1100C, 851, 1 );
		FillData( pdpmRecord, J1550A, 855, 1 );
		FillData( pdpmRecord, J1550B, 856, 1 );
		// 50
		FillData( pdpmRecord, K0100A, 867, 1 );
		FillData( pdpmRecord, K0100B, 868, 1 );
		FillData( pdpmRecord, K0100C, 869, 1 );
		FillData( pdpmRecord, K0100D, 870, 1 );
		FillData( pdpmRecord, K0300, 877, 1 );

		FillData( pdpmRecord, M0300B1, 900, 1 );
		FillData( pdpmRecord, M0300C1, 910, 1 );
		FillData( pdpmRecord, M0300D1, 912, 1 );
		FillData( pdpmRecord, M0300F1, 916, 1 );
		FillData( pdpmRecord, M1030, 940, 1 );
		// 60
		FillData( pdpmRecord, M1040A, 941, 1 );
		FillData( pdpmRecord, M1040B, 942, 1 );
		FillData( pdpmRecord, M1040C, 943, 1 );
		FillData( pdpmRecord, M1040D, 944, 1 );
		FillData( pdpmRecord, M1040E, 945, 1 );

		FillData( pdpmRecord, M1040F, 946, 1 );
		FillData( pdpmRecord, M1200A, 948, 1 );
		FillData( pdpmRecord, M1200B, 949, 1 );
		FillData( pdpmRecord, M1200C, 950, 1 );
		FillData( pdpmRecord, M1200D, 951, 1 );
		// 70
		FillData( pdpmRecord, M1200E, 952, 1 );
		FillData( pdpmRecord, M1200F, 953, 1 );
		FillData( pdpmRecord, M1200G, 954, 1 );
		FillData( pdpmRecord, M1200H, 955, 1 );
		FillData( pdpmRecord, M1200I, 956, 1 );

		FillData( pdpmRecord, N0350A, 959, 1 );
		FillData( pdpmRecord, N0350B, 960, 1 );
		FillData( pdpmRecord, O0100A2, 970, 1 );
		FillData( pdpmRecord, O0100B2, 972, 1 );
		FillData( pdpmRecord, O0100C2, 974, 1 );
		// 80
		FillData( pdpmRecord, O0100D2, 976, 1 );
		FillData( pdpmRecord, O0100E2, 978, 1 );
		FillData( pdpmRecord, O0100F2, 980, 1 );
		FillData( pdpmRecord, O0100H2, 984, 1 );
		FillData( pdpmRecord, O0100I2, 986, 1 );

		FillData( pdpmRecord, O0100J2, 988, 1 );
		FillData( pdpmRecord, O0100M2, 993, 1 );
		FillData( pdpmRecord, O0400D2, 1099, 1 );
		FillData( pdpmRecord, O0500A, 1110, 1 );
		FillData( pdpmRecord, O0500B, 1111, 1 );
		// 90
		FillData( pdpmRecord, O0500C, 1112, 1 );
		FillData( pdpmRecord, O0500D, 1113, 1 );
		FillData( pdpmRecord, O0500E, 1114, 1 );
		FillData( pdpmRecord, O0500F, 1115, 1 );
		FillData( pdpmRecord, O0500G, 1116, 1 );

		FillData( pdpmRecord, O0500H, 1117, 1 );
		FillData( pdpmRecord, O0500I, 1118, 1 );
		FillData( pdpmRecord, O0500J, 1119, 1 );
		FillData( pdpmRecord, K0510A1, 1444, 1 );
		FillData( pdpmRecord, K0510A2, 1445, 1 );
		// 100
		FillData( pdpmRecord, K0510B1, 1446, 1 );
		FillData( pdpmRecord, K0510B2, 1447, 1 );
		FillData( pdpmRecord, K0510C2, 1449, 1 );
		FillData( pdpmRecord, K0710A2, 1467, 1 );
		FillData( pdpmRecord, K0710A3, 1468, 1 );

		FillData( pdpmRecord, K0710B2, 1470, 1 );
		FillData( pdpmRecord, K0710B3, 1471, 1 );
		FillData( pdpmRecord, GG0130A1, 1498, 2 );
		FillData( pdpmRecord, GG0130A5, 1704, 2 );
		FillData( pdpmRecord, GG0130B1, 1504, 2 );
		// 110
		FillData( pdpmRecord, GG0130B5, 1706, 2 );
		FillData( pdpmRecord, GG0130C1, 1510, 2 );
		FillData( pdpmRecord, GG0130C5, 1708, 2 );
		FillData( pdpmRecord, GG0170B1, 1516, 2 );
		FillData( pdpmRecord, GG0170B5, 1710, 2 );

		FillData( pdpmRecord, GG0170C1, 1522, 2 );
		FillData( pdpmRecord, GG0170C5, 1712, 2 );
		FillData( pdpmRecord, GG0170D1, 1528, 2 );
		FillData( pdpmRecord, GG0170D5, 1714, 2 );
		FillData( pdpmRecord, GG0170E1, 1534, 2 );
		// 120
		FillData( pdpmRecord, GG0170E5, 1716, 2 );
		FillData( pdpmRecord, GG0170F1, 1540, 2 );
		FillData( pdpmRecord, GG0170F5, 1718, 2 );
		FillData( pdpmRecord, GG0170I1, 1651, 2 );
		FillData( pdpmRecord, GG0170I5, 1720, 2 );

		FillData( pdpmRecord, GG0170J1, 1548, 2 );
		FillData( pdpmRecord, GG0170J5, 1722, 2 );
		FillData( pdpmRecord, GG0170K1, 1554, 2 );
		FillData( pdpmRecord, GG0170K5, 1724, 2 );		
		FillData( pdpmRecord, I0020B, 1726, 8 );
		// 130
		FillData( pdpmRecord, J2100, 1734, 1 );
		FillData( pdpmRecord, J2300, 1735, 1 );
		FillData( pdpmRecord, J2310, 1736, 1 );
		FillData( pdpmRecord, J2320, 1737, 1 );
		FillData( pdpmRecord, J2330, 1738, 1 );

		FillData( pdpmRecord, J2400, 1739, 1 );
		FillData( pdpmRecord, J2410, 1740, 1 );
		FillData( pdpmRecord, J2420, 1741, 1 );
		FillData( pdpmRecord, J2500, 1743, 1 );
		FillData( pdpmRecord, J2510, 1744, 1 );
		// 140
		FillData( pdpmRecord, J2520, 1745, 1 );
		FillData( pdpmRecord, J2530, 1746, 1 );
		FillData( pdpmRecord, J2600, 1748, 1 );
		FillData( pdpmRecord, J2610, 1749, 1 );
		FillData( pdpmRecord, J2620, 1750, 1 );

		FillData( pdpmRecord, J2700, 1752, 1 );
		FillData( pdpmRecord, J2710, 1753, 1 );
		FillData( pdpmRecord, J2800, 1755, 1 );
		FillData( pdpmRecord, J2810, 1756, 1 );
		FillData( pdpmRecord, J2900, 1758, 1 );
		// 150
		FillData( pdpmRecord, J2910, 1759, 1 );
		FillData( pdpmRecord, J2920, 1760, 1 );
		FillData( pdpmRecord, J2930, 1761, 1 );
		FillData( pdpmRecord, J2940, 1762, 1 );
		
		Trim_I0020B(I0020B);
		Trim_I0020B(I8000A);
		Trim_I0020B(I8000B);
		Trim_I0020B(I8000C);
		Trim_I0020B(I8000D);
		Trim_I0020B(I8000E);
		Trim_I0020B(I8000F);
		Trim_I0020B(I8000G);
		Trim_I0020B(I8000H);
		Trim_I0020B(I8000I);
		Trim_I0020B(I8000J);

		AssessmentConvert = ConversionFunctionTwoDigit(A0310B);
		HIPPSCodeGenerated[4] = DetermineAssessment(AssessmentConvert);
		C0100Int = ConversionFunctionOneDigit(C0100);
		BIMSScore = ConversionFunctionTwoDigit(C0500);
		DepressedScore1 = ConversionFunctionTwoDigit(D0300);
		DepressedScore2 = ConversionFunctionTwoDigit(D0600);
		
		IsDepressed = DetermineDepression(DepressedScore1, DepressedScore2);

		if (AssessmentConvert == 1)
		{
		CognitiveValue = DetermineCognitiveLevel(C0500, B0100, GG0130A1, GG0130C1, GG0170B1, GG0170C1, GG0170D1, GG0170E1, GG0170F1, C1000, B0700, C0700, BIMSScore);
		}
		else if (AssessmentConvert == 8)
		{
		CognitiveValue = DetermineCognitiveLevel(C0500, B0100, GG0130A5, GG0130C5, GG0170B5, GG0170C5, GG0170D5, GG0170E5, GG0170F5, C1000, B0700, C0700, BIMSScore);
		}
		

		FoundIndex = DeterminePrimaryDiagnosis(I0020B);
//		printf("I0020B is: %s\n",I0020B);
//		printf("The found index is: %i\n",FoundIndex);
//		printf("The surgery eligiblity code is = %i\n",I0020BEligibility[FoundIndex]);
		if (AssessmentConvert==1)
		{
		EatingFunctionScore = CalcEatingFunctionScorePT(GG0130A1);
		OralHygieneFunctionScore = CalcOralHygieneFunctionScorePT(GG0130B1);
		ToiletingHygieneFunctionScore = CalcToiletingHygieneFunctionScorePT(GG0130C1);
		SitToLyingFunctionScore = CalcSitToLyingFunctionScorePT(GG0170B1);
		LyingToSittingFunctionScore = CalcLyingToSittingFunctionScorePT(GG0170C1);
		SitToStandFunctionScore = CalcSitToStandFunctionScorePT(GG0170D1);
		ChairBedFunctionScore = CalcChairBedFunctionScorePT(GG0170E1);
		ToiletTransferFunctionScore = CalcToiletTransferFunctionScorePT(GG0170F1);
		WalkFiftyFunctionScore = CalcWalkFiftyFunctionScorePT(GG0170J1);
		WalkOneFiftyFunctionScore = CalcWalkOneFiftyFunctionScorePT(GG0170K1);

		BedMobilityAverage = AverageTwoNumbers(SitToLyingFunctionScore, LyingToSittingFunctionScore);
		TransferAverage = AverageThreeNumbers(SitToStandFunctionScore, ChairBedFunctionScore, ToiletTransferFunctionScore);
		WalkingAverage = AverageTwoNumbers(WalkFiftyFunctionScore, WalkOneFiftyFunctionScore);


		PDPMFunctionScorePT = CalcPDPMFunctionScorePT(EatingFunctionScore, OralHygieneFunctionScore, ToiletingHygieneFunctionScore,
							  BedMobilityAverage, TransferAverage, WalkingAverage);

		PDPMFunctionScoreRounded = RoundNumber(PDPMFunctionScorePT);

		PDPMFunctionScoreNursing = CalcPDPMFunctionScoreNursing(EatingFunctionScore, ToiletingHygieneFunctionScore,
							  BedMobilityAverage, TransferAverage);
		PDPMFunctionScoreNursingRounded = RoundNumber(PDPMFunctionScoreNursing);
	

		NursingScore14 = NursingScore14Check(PDPMFunctionScoreNursingRounded);
		NursingScore15 = NursingScore15Check(PDPMFunctionScoreNursingRounded);
		}
		else if (AssessmentConvert==8)
		{
		EatingFunctionScore = CalcEatingFunctionScorePT(GG0130A5);
		OralHygieneFunctionScore = CalcOralHygieneFunctionScorePT(GG0130B5);
		ToiletingHygieneFunctionScore = CalcToiletingHygieneFunctionScorePT(GG0130C5);
		SitToLyingFunctionScore = CalcSitToLyingFunctionScorePT(GG0170B5);
		LyingToSittingFunctionScore = CalcLyingToSittingFunctionScorePT(GG0170C5);
		SitToStandFunctionScore = CalcSitToStandFunctionScorePT(GG0170D5);
		ChairBedFunctionScore = CalcChairBedFunctionScorePT(GG0170E5);
		ToiletTransferFunctionScore = CalcToiletTransferFunctionScorePT(GG0170F5);
		WalkFiftyFunctionScore = CalcWalkFiftyFunctionScorePT(GG0170J5);
		WalkOneFiftyFunctionScore = CalcWalkOneFiftyFunctionScorePT(GG0170K5);
	
		
		BedMobilityAverage = AverageTwoNumbers(SitToLyingFunctionScore, LyingToSittingFunctionScore);
		TransferAverage = AverageThreeNumbers(SitToStandFunctionScore, ChairBedFunctionScore, ToiletTransferFunctionScore);
		WalkingAverage = AverageTwoNumbers(WalkFiftyFunctionScore, WalkOneFiftyFunctionScore);

		PDPMFunctionScorePT = CalcPDPMFunctionScorePT(EatingFunctionScore, OralHygieneFunctionScore, ToiletingHygieneFunctionScore,
							  BedMobilityAverage, TransferAverage, WalkingAverage);

		PDPMFunctionScoreRounded = RoundNumber(PDPMFunctionScorePT);
		
		PDPMFunctionScoreNursing = CalcPDPMFunctionScoreNursing(EatingFunctionScore, ToiletingHygieneFunctionScore,
							  BedMobilityAverage, TransferAverage);
	
		PDPMFunctionScoreNursingRounded = RoundNumber(PDPMFunctionScoreNursing);
		NursingScore14 = NursingScore14Check(PDPMFunctionScoreNursingRounded);
		NursingScore15 = NursingScore15Check(PDPMFunctionScoreNursingRounded);
		}
	

		OrthopedicSurgeryEligible = DetermineOrthopedicSurgeryEligibility(FoundIndex);	
		NonOrthopedicSurgeryEligible = DetermineNonOrthopedicSurgeryEligibility(FoundIndex);
		
		RecentSurgery = DetermineRecentSurgery(J2100);
		MajorSurgery = DetermineMajorSurgery(J2300, J2310, J2320, J2330, J2400, J2410, J2420);
		OrthopedicSurgery = DetermineOrthopedicSurgery(J2500, J2510, J2520, J2530);
		NonOrthopedicSurgery = DetermineNonOrthopedicSurgery(J2600, J2610, J2620, J2700, J2710, J2800, J2810, J2900, J2910, J2920, J2930, J2940);
			if (OrthopedicSurgeryEligible)
			{
			if (RecentSurgery)
				{
				if (MajorSurgery)
					{
					ClinicalCategoryNumberCodePT = 1;
					}
				else if (OrthopedicSurgery)
					{
					ClinicalCategoryNumberCodePT = 2;
					}
				else 
					{
					ClinicalCategoryNumberCodePT = DetermineClinicalCategoryPT(FoundIndex);
					}
				 }
			else if (!RecentSurgery) 
				{
					 ClinicalCategoryNumberCodePT = DetermineClinicalCategoryPT(FoundIndex);
				}
			}

		   else if (NonOrthopedicSurgeryEligible)
			{
					
			if (RecentSurgery)
				{
					 if (NonOrthopedicSurgery)
					 {
					 ClinicalCategoryNumberCodePT = 4;
					 }
					 else 
					 {
					 ClinicalCategoryNumberCodePT = DetermineClinicalCategoryPT(FoundIndex);
					 }

				}
			else if (!RecentSurgery)
			{
	
				ClinicalCategoryNumberCodePT = DetermineClinicalCategoryPT(FoundIndex);
				
				}
		
			}
		   else 
		   {
		//	  printf("The found index is: %i\n",FoundIndex);
			
		   ClinicalCategoryNumberCodePT = DetermineClinicalCategoryPT(FoundIndex);
		   }

	
		HIPPSCodeGenerated[0] = DetermineCaseMixGroupPT(ClinicalCategoryNumberCodePT, PDPMFunctionScoreRounded);

		if (isalpha(HIPPSCodeGenerated[0]) == 0) 
		{
		*errorcode = 5;
		}

		ClinicalCategoryNumberCodeSLPPrelim = DetermineClinicalCategorySLP(FoundIndex);
			
		if (ClinicalCategoryNumberCodeSLPPrelim == 2)
		{
			if (OrthopedicSurgeryEligible)
			{
			if (RecentSurgery)
				{
				if (MajorSurgery)
					{
					ClinicalCategoryNumberCodeSLPFinal = 1;
					}
				else if (OrthopedicSurgery)
					{
					ClinicalCategoryNumberCodeSLPFinal = 1;
					}
				else 
					{
					ClinicalCategoryNumberCodeSLPFinal = 2;
					}
				}
			}

		   else if (NonOrthopedicSurgeryEligible)
			{
			if (RecentSurgery)
				{
					 if (NonOrthopedicSurgery)
					 {
					 ClinicalCategoryNumberCodeSLPFinal = 1;
					 }
				}
			else 
				{
				ClinicalCategoryNumberCodeSLPFinal = 2;
				}
			}
		   else 
		   {
			ClinicalCategoryNumberCodeSLPFinal = 2;
		   }
		}
		else
		{
		ClinicalCategoryNumberCodeSLPFinal = 1;
		}

		if (ClinicalCategoryNumberCodeSLPFinal == 2)
		{
        SLPAcuteNeurologicalCount = 1;
		}

		SLPScoreI8000A = ReturnSLPScore(I8000A);
		SLPScoreI8000B = ReturnSLPScore(I8000B);
		SLPScoreI8000C = ReturnSLPScore(I8000C);
		SLPScoreI8000D = ReturnSLPScore(I8000D);
		SLPScoreI8000E = ReturnSLPScore(I8000E);
		SLPScoreI8000F = ReturnSLPScore(I8000F);
		SLPScoreI8000G = ReturnSLPScore(I8000G);
		SLPScoreI8000H = ReturnSLPScore(I8000H);
		SLPScoreI8000I = ReturnSLPScore(I8000I);
		SLPScoreI8000J = ReturnSLPScore(I8000J);

		SLPSummation = SLPScoreI8000A + SLPScoreI8000B + SLPScoreI8000C + SLPScoreI8000D +
					   SLPScoreI8000E + SLPScoreI8000F + SLPScoreI8000G + SLPScoreI8000H +
					   SLPScoreI8000I + SLPScoreI8000J;

		if (SLPSummation > 0)
		{SLPSummationPass = 1;}

		AphasiaScore = DetermineAphasia(I4300);
		CVAScore = DetermineCVA(I4500);
		HemiplegiaScore = DetermineHemiplegia(I4900);
		TraumaticBrainScore = DetermineTraumaticBrain(I5500);
		TracheostomySLPScore = DetermineTracheostomyCare(O0100E2);
		VentilatorSLPScore = DetermineVentilatorSLP(O0100F2);


		MiscSLPComorbidityScore = CalcMiscSLPComorbidity(AphasiaScore, CVAScore, HemiplegiaScore, TraumaticBrainScore, 
							 TracheostomySLPScore, VentilatorSLPScore);

		SLPComorbidityScoreCombinedPrelim = SLPSummationPass + MiscSLPComorbidityScore; 

		if (SLPComorbidityScoreCombinedPrelim>0)
		{
		SLPComorbidityScoreCombinedFinal = 1;
		}

		if (AssessmentConvert==1)
		{
		PresenceNeurologicCount = DetermineCognitiveLevel(C0500, B0100, GG0130A1, GG0130C1, GG0170B1, GG0170C1, 
							  GG0170D1, GG0170E1, GG0170F1, C1000, B0700, C0700, BIMSScore) + SLPComorbidityScoreCombinedFinal + SLPAcuteNeurologicalCount;
		}
		else if (AssessmentConvert==8)
		{
		PresenceNeurologicCount = DetermineCognitiveLevel(C0500, B0100, GG0130A5, GG0130C5, GG0170B5, GG0170C5, 
							  GG0170D5, GG0170E5, GG0170F5, C1000, B0700, C0700, BIMSScore) + SLPComorbidityScoreCombinedFinal + SLPAcuteNeurologicalCount;
		
		}

		MechanicallyAlteredCount = DetermineSwallowingDisorder(K0100A, K0100B, K0100C, K0100D) + 
								   DetermineMechanicallyAltered(K0510C2);
	
		HIPPSCodeGenerated[1] = DetermineCaseMixGroupSLP(PresenceNeurologicCount, MechanicallyAlteredCount);


		if (isalpha(HIPPSCodeGenerated[1]) == 0) 
		{
		*errorcode = 5;
		}
		
		Stage2UlcerConverted = ConversionFunctionOneDigit(M0300B1);
		Stage3UlcerConverted = ConversionFunctionOneDigit(M0300C1);
		Stage4UlcerConverted = ConversionFunctionOneDigit(M0300D1);
		UnstagedUlcerConverted = ConversionFunctionOneDigit(M0300F1);

		VenousUlcerConverted = ConversionFunctionOneDigit(M1030);
		InsulinInjections = ConversionFunctionOneDigit(N0350A);
		InsulinOrders = ConversionFunctionOneDigit(N0350B);
		RespiratoryTherapy = ConversionFunctionOneDigit(O0400D2);
		SkinTreatmentCount = DetermineSkinTreatments(M1200A, M1200B, M1200C, M1200D, M1200E, M1200G, M1200H);
		B0700Converted = ConversionFunctionOneDigit(B0700);
		C1000Converted = ConversionFunctionOneDigit(C1000);

		TracheostomyBool = DetermineTracheostomyCareBool(O0100E2);
		VentilatorBool = DetermineVentilatorBool(O0100F2);
		IsolationBool = DetermineIsolationBool(O0100M2);

		ExtensiveServices1 = ExtensiveServicesStep1(TracheostomyBool, VentilatorBool, IsolationBool);
       
		if (ExtensiveServices1)
		{
			if (NursingScore14)
			{
				if  ((TracheostomyBool)&&(VentilatorBool))
					{
					strcpy( NursingCaseMixES, "ES3" ); HIPPSCodeGenerated[2] = 'A'; 
					}	
				else if ((TracheostomyBool)||(VentilatorBool))
					{
					strcpy( NursingCaseMixES, "ES2" ); HIPPSCodeGenerated[2] = 'B';
					}
				else if ((IsolationBool)&&(!VentilatorBool)&&(!TracheostomyBool))
					{
					strcpy( NursingCaseMixES, "ES1" ); HIPPSCodeGenerated[2] = 'C';
					}
			}
		    else if (NursingScore15)
			{ 
				
				HIPPSCodeGenerated[2] = DetermineClinicallyComplexStep3(IsDepressed, PDPMFunctionScoreNursingRounded);
			}
		}
		else if (!ExtensiveServices1)
		{
			if (AssessmentConvert == 1)
			{
			IsComatose = DetermineIfComatose(B0100, GG0130A1, GG0130C1, GG0170B1, GG0170C1, 
						 GG0170D1, GG0170E1, GG0170F1);
			}
			else if (AssessmentConvert == 8)
			{
			IsComatose = DetermineIfComatose(B0100, GG0130A5, GG0130C5, GG0170B5, GG0170C5, 
						 GG0170D5, GG0170E5, GG0170F5);
			
			}
			HasSepticemia = DetermineSepticemia(I2100);
			IsDiabetic = DetermineDiabetes(I2900, InsulinInjections, InsulinOrders);
			IsQuadriplegic =  DetermineQuadriplegia(I5100, PDPMFunctionScoreNursingRounded);
			HasPulmonary = DeterminePulmonaryDisease(I6200, J1100C);
			HasFever = DetermineFever(J1550A);
			HasParenteralFeeding = DetermineParenteralFeeding(K0510A1, K0510A2);
	//		HadFeedings = DetermineIfHadFeeding(K0510B1, K0510B2, K0710A3, K0710B3);
			HadRespiratoryTherapy = DetermineRespiratory(RespiratoryTherapy);
			Pneumonia = DeterminePneumonia(I2000);
			Vomiting = DetermineVomiting(J1550B);
			WeightLoss = DetermineWeightLoss(K0300);
			FeedingTube = DetermineIfHadFeeding(K0510B1, K0510B2, K0710A3, K0710B3);
			FeverCombo = DetermineFeverCombo(HasFever, Pneumonia, Vomiting, WeightLoss, FeedingTube);

			SpecialCareHigh1 = SpecialCareHighStep1(IsComatose,HasSepticemia, IsDiabetic, IsQuadriplegic, HasPulmonary, 
							   FeverCombo, HasParenteralFeeding, HadRespiratoryTherapy);

			if (SpecialCareHigh1)
			{
				if (NursingScore14)
				{
					HIPPSCodeGenerated[2] = SpecialCareHighStep4(PDPMFunctionScoreNursingRounded, IsDepressed);
				}
				else if (NursingScore15)
				{ 
					HIPPSCodeGenerated[2] = DetermineClinicallyComplexStep3(IsDepressed, PDPMFunctionScoreNursingRounded);
				}
			}
			else if (!SpecialCareHigh1)
			{ 
				HasCerebralPalsy = DetermineCerebralPalsy(I4400, PDPMFunctionScoreNursingRounded);
				HasMultipleSclerosis = DetermineMultipleSclerosis(I5200, PDPMFunctionScoreNursingRounded);
				HasParkinsons = DetermineParkinsons(I5300, PDPMFunctionScoreNursingRounded);
				HadRadiationTreatment = DetermineRadiationBool(O0100B2);
				HadDialysisTreatment = DetermineDialysis(O0100J2);
				HasRespiratoryFailure = DetermineRespiratoryFailure(I6300, O0100C2);
				UlcerCheck1 = CalcUlcerCheck1(Stage2UlcerConverted, SkinTreatmentCount);
				UlcerCheck2 = CalcUlcerCheck2(Stage3UlcerConverted, Stage4UlcerConverted, UnstagedUlcerConverted, SkinTreatmentCount);
				UlcerCheck3 = CalcUlcerCheck3(VenousUlcerConverted, SkinTreatmentCount);
				UlcerCheck4 = CalcUlcerCheck4(Stage2UlcerConverted, VenousUlcerConverted, SkinTreatmentCount);
				FootCheck = DetermineFootCheck(M1040A, M1040B, M1040C, M1200I);

				SpecialCareLow1 = SpecialCareLowStep1 (HasCerebralPalsy, HasMultipleSclerosis, HasParkinsons, 
					HasRespiratoryFailure, FeedingTube, UlcerCheck1, UlcerCheck2, UlcerCheck3, UlcerCheck4, FootCheck, 
					HadRadiationTreatment, HadDialysisTreatment);

				if (SpecialCareLow1)
				{
					if (NursingScore14)
					{
					HIPPSCodeGenerated[2] = SpecialCareLowStep4(PDPMFunctionScoreNursingRounded, IsDepressed);
					}
					else if (NursingScore15)
					{	 
					HIPPSCodeGenerated[2] = DetermineClinicallyComplexStep3(IsDepressed, PDPMFunctionScoreNursingRounded);
					}
				}
				else if (!SpecialCareLow1)
				{
				RestorativeNursingCount = DetermineRestorativeNursingCount(H0200C, H0500, O0500A, O0500B, O0500C, O0500D, O0500F, O0500E, O0500G, 
										   O0500H, O0500I, O0500J);
				ClinicallyComplexStep1 = DetermineClinicallyComplexStep1(I2000, I4900, M1040D, M1040E, M1040F,
							M1200F, M1200G, M1200H, O0100A2, O0100C2, O0100H2, O0100I2, PDPMFunctionScoreNursingRounded);
				BehavioralSymptoms4 = BehavioralSymptomsStep4(E0100A, E0100B, E0200A, E0200B, E0200C, E0800, E0900);
						
							if (ClinicallyComplexStep1)
							{
							HIPPSCodeGenerated[2] = DetermineClinicallyComplexStep3(IsDepressed, PDPMFunctionScoreNursingRounded);
							}
							else if (!ClinicallyComplexStep1)
							{		
										
									if (PDPMFunctionScoreNursingRounded>=11)
									{   
										ImpairIndicate1 = DetermineImpairmentIndicators1(B0700Converted, C0700, C1000Converted);
										ImpairIndicate2 = DetermineImpairmentIndicators2(B0700Converted, C1000Converted);
										CheckImpairment = (ImpairIndicate1, ImpairIndicate2);
										if (C0100Int == 1)
										{	
											C0100Test = true;
										}
										BehavioralSymptoms2 = BehavioralSymptomsStep2(BIMSScore, C0100Test);
									
										if (BehavioralSymptoms2 == 3)
										{
										BehavioralSymptoms3 = BehavioralSymptomsStep3(IsComatose, C1000, CheckImpairment);
												if (BehavioralSymptoms3)
												{
												HIPPSCodeGenerated[2] = BehavioralSymptomsStep6(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
												}
												else if (!BehavioralSymptoms3)
												{
												BehavioralSymptoms4 = BehavioralSymptomsStep4(E0100A, E0100B, E0200A, E0200B, 
												E0200C, E0800, E0900);
														if (BehavioralSymptoms4)
														{
														HIPPSCodeGenerated[2] = BehavioralSymptomsStep6(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
														}
														else if (!BehavioralSymptoms4)
														{
														HIPPSCodeGenerated[2] = CalcReducedPhysicalFunction(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
														}
												}
									
										}
										else if (BehavioralSymptoms2 == 4)
										{
										BehavioralSymptoms4 = BehavioralSymptomsStep4(E0100A, E0100B, E0200A, E0200B, E0200C, E0800, E0900);
													if (BehavioralSymptoms4)
													{
													HIPPSCodeGenerated[2] = BehavioralSymptomsStep6(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
													}
													else if (!BehavioralSymptoms4)
													{
													HIPPSCodeGenerated[2] = CalcReducedPhysicalFunction(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
													}

										}
										else if (BehavioralSymptoms2 == 5)
										{
										HIPPSCodeGenerated[2] = BehavioralSymptomsStep6(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
										}
									}
									else if (PDPMFunctionScoreNursingRounded<=10)
									{
									
									HIPPSCodeGenerated[2] = CalcReducedPhysicalFunction(RestorativeNursingCount, PDPMFunctionScoreNursingRounded);
									}
									}
									}
						
						//	}
		
			}

		

		}	
		if (isalpha(HIPPSCodeGenerated[2]) == 0) 
		{
		*errorcode = 5;
		}
		

		NTAScoreI8000A = ReturnNTAScore(I8000A, &I8000ACode);
		NTAScoreI8000B = ReturnNTAScore(I8000B, &I8000BCode);
		NTAScoreI8000C = ReturnNTAScore(I8000C, &I8000CCode);
		NTAScoreI8000D = ReturnNTAScore(I8000D, &I8000DCode);
		NTAScoreI8000E = ReturnNTAScore(I8000E, &I8000ECode);
		NTAScoreI8000F = ReturnNTAScore(I8000F, &I8000FCode);
		NTAScoreI8000G = ReturnNTAScore(I8000G, &I8000GCode);
		NTAScoreI8000H = ReturnNTAScore(I8000H, &I8000HCode);
		NTAScoreI8000I = ReturnNTAScore(I8000I, &I8000ICode);
		NTAScoreI8000J = ReturnNTAScore(I8000J, &I8000JCode);

//		printf("I8000A Code = %i\n",I8000ACode);
	

	
		I8000Score = I8000Score + NTAScoreI8000A;
		if (I8000ACode != I8000BCode)
		{
		I8000Score = I8000Score + NTAScoreI8000B;
		}
		if ((I8000ACode != I8000CCode) && (I8000BCode != I8000CCode))
		{
		I8000Score = I8000Score + NTAScoreI8000C;
		}
		if ((I8000ACode != I8000DCode) && (I8000BCode != I8000DCode) && (I8000CCode != I8000DCode))
		{
		I8000Score = I8000Score + NTAScoreI8000D;
		}
		if ((I8000ACode != I8000ECode) && (I8000BCode != I8000ECode) && (I8000CCode != I8000ECode) && (I8000DCode != I8000ECode))
		{
		I8000Score = I8000Score + NTAScoreI8000E;
		}
		if ((I8000ACode != I8000FCode) && (I8000BCode != I8000FCode) && (I8000CCode != I8000FCode) && (I8000DCode != I8000FCode) && (I8000ECode != I8000FCode))	
		{
		I8000Score = I8000Score + NTAScoreI8000F;
		}
		if ((I8000ACode != I8000GCode) && (I8000BCode != I8000GCode) && (I8000CCode != I8000GCode) && (I8000DCode != I8000GCode) && (I8000ECode != I8000GCode) && (I8000FCode != I8000GCode))	
		{
		I8000Score = I8000Score + NTAScoreI8000G;
		}
		if ((I8000ACode != I8000HCode) && (I8000BCode != I8000HCode) && (I8000CCode != I8000HCode) && (I8000DCode != I8000HCode) && (I8000ECode != I8000HCode) && (I8000FCode != I8000HCode) && (I8000GCode != I8000HCode))
		{
		I8000Score = I8000Score + NTAScoreI8000H;
		}
		if ((I8000ACode != I8000ICode) && (I8000BCode != I8000ICode) && (I8000CCode != I8000ICode) && (I8000DCode != I8000ICode) && (I8000ECode != I8000ICode) && (I8000FCode != I8000ICode) && (I8000GCode != I8000ICode) && (I8000HCode != I8000ICode))	
		{
		I8000Score = I8000Score + NTAScoreI8000I;
		}
		if ((I8000ACode != I8000JCode) && (I8000BCode != I8000JCode) && (I8000CCode != I8000JCode) && (I8000DCode != I8000JCode) && (I8000ECode != I8000JCode) && (I8000FCode != I8000JCode) && (I8000GCode != I8000JCode) && (I8000HCode != I8000JCode) && (I8000ICode != I8000JCode))
		{
		I8000Score = I8000Score + NTAScoreI8000J;
		}

	//	printf("The NTA I8000 Score is = %i\n",I8000Score);

		IntraVenousMedicationScore = DetermineIntravenousMedication(O0100H2);
		VentilatorScore = DetermineVentilator(O0100F2);
		TransfusionScore = DetermineTransfusion(O0100I2);
		MultipleSclerosisScore = DetermineMultipleSclerosis(I5200);
		AsthmaCOPDScore = DetermineAsthmaCOPD(I6200);
		WoundInfectionScore = DetermineWoundInfection(I2500);
		DiabetesMellitusScore = DetermineDiabetesMellitus(I2900);
		DiabeticFootScore = DetermineDiabeticFoot(M1040B);
		TracheostomyCareScore = DetermineTracheostomyCare(O0100E2);
		ResistantOrganismScore = DetermineResistantOrganism(I1700);
		IsolationScore = DetermineIsolation(O0100M2);;
		RadiationScore = DetermineRadiation(O0100B2);
		UnhealedPressureScore = DetermineUnhealedPressure(M0300D1);
		IntermittentCatheterizationScore = DetermineIntermittentCatheterization(H0100D);
		InflammatoryBowelScore = DetermineInflammatoryBowel(I1300);
		SuctioningScore = DetermineSuctioning(O0100D2);
		FeedingTubeScore = DetermineFeedingTube(K0510B2);
		MalnutritionScore = DetermineMalnutrition(I5600);
		OstomyScore = DetermineOstomy(H0100C);
		FootInfectionScore= DetermineFootInfection(M1040A, M1040C);


		NTAFeeding = DetermineNTAFeeding(K0510A2);
		

		if (NTAFeeding)
		{
			NTAFeedingScore = DetermineNTAFeedingScore(K0710A2, K0710B2);
		}
		else if (!NTAFeeding)
		{
		NTAFeedingScore = 0;
		}

		NTAScoreToPass = DetermineNTAScore(I8000Score,IntraVenousMedicationScore,VentilatorScore,
						 TransfusionScore,MultipleSclerosisScore,AsthmaCOPDScore,WoundInfectionScore,
						 DiabetesMellitusScore,DiabeticFootScore,TracheostomyCareScore,ResistantOrganismScore,
						 IsolationScore,RadiationScore,UnhealedPressureScore,IntermittentCatheterizationScore,InflammatoryBowelScore,SuctioningScore,
						 FeedingTubeScore,MalnutritionScore,OstomyScore,FootInfectionScore, NTAFeedingScore);
		HIPPSCodeGenerated[3] = DetermineCaseMixGroupNTA(NTAScoreToPass);


		if (isalpha(HIPPSCodeGenerated[3]) == 0) 
		{
		*errorcode = 5;
		}

		strcpy(HIPPSCode, HIPPSCodeGenerated);
		printf("The HIPPS Code is = %s\n",HIPPSCode);
		printf("The Error Code is = %i\n",*errorcode);

	return 0;
	

}
/*
#ifdef SOLARIS
   int mdsgrouper_XML( char* sXMLRecord, char* HIPPSCode,  int* errorcode, char* HIPPSVersionCode )
 #else
 extern "C" __declspec(dllexport) int __stdcall
	mdsgrouper_XML_FILE( char* sXMLFile, char* HIPPSCode,  int* errorcode, char* HIPPSVersionCode ) 
#endif
{
	char pdpmXMLRecord[3691];
    memset(pdpmXMLRecord, ' ', 3691);
	pdpmXMLRecord[3689] = '%';
	pdpmXMLRecord[3690] = '\0';

	TiXmlDocument doc( sXMLFile );
	bool loadOkay = doc.LoadFile();

	if ( !loadOkay )
		{
			printf( "Could not load xml file %s. Error=%s. Exiting.\n", sXMLFile, doc.ErrorDesc() );
            return 99;
		}

		TiXmlNode* node = 0;
		TiXmlElement* pdpmElement = 0;
		TiXmlElement* itemElement = 0;
		TiXmlElement*	element;

		node = doc.FirstChild( "ASSESSMENT" );
		assert( node );
		pdpmElement = node->ToElement();
		assert( pdpmElement  );

		node = doc.RootElement();
		assert( node );
		// Walk all the elements in a node.
		char name[9];
		char itemValue[9];
		int itemCount = 0;

		for( element = pdpmElement->FirstChildElement();
			 element;
			 element = element->NextSiblingElement() )
		{
             strcpy( name,element->Value() );
			 for ( int i=0; i<154; i++ ) {
			     if ( 0==strcmp(name, items[i].name) ) {
                      strcpy(itemValue, element->GetText());
					  FillRecord( pdpmXMLRecord, itemValue, items[i].loc, strlen(itemValue) );
                      itemCount++;
					  break;
				      }
			}

		}

	int errorCode = mdsgrouper( pdpmXMLRecord, HIPPSCode,  errorcode, HIPPSVersionCode  );

    return errorCode;
}

*/
