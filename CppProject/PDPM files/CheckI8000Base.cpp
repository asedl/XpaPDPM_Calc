#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "CheckI8000Base.h"
#include "mdsgrouper.h"


bool CheckALS(char* I8000)
{bool ALS = false;
 for( int index = 0; index < 1; index++ ) {
      if(0 == strcmp(I8000, ALSCodes[index]))
	  {
	  ALS = true;
	  break;
	  }
   }
	return ALS;
}

bool CheckApraxia(char* I8000)
{bool Apraxia = false;
 for( int index = 0; index < 6; index++ ) {
      if(0 == strcmp(I8000, ApraxiaCodes[index]))
	  {
	  Apraxia = true;
	  break;
	  }
   }
	return Apraxia;
}

bool CheckDyspagia(char* I8000)
{bool Dyspagia = false;
 for( int index = 0; index < 6; index++ ) {
      if(0 == strcmp(I8000, DyspagiaCodes[index]))
	  {
	  Dyspagia = true;
	  break;
	  }
   }
	return Dyspagia;
}

bool CheckLaryngealCancer(char* I8000)
{bool LaryngealCancer = false;
 for( int index = 0; index < 6; index++ ) {
      if(0 == strcmp(I8000, LaryngealCancerCodes[index]))
	  {
	  LaryngealCancer = true;
	  break;
	  }
   }
	return LaryngealCancer;
}

bool CheckOralCancer(char* I8000)
{bool OralCancer = false;
 for( int index = 0; index < 49; index++ ) {
      if(0 == strcmp(I8000, OralCancerCodes[index]))
	  {
	  OralCancer = true;
	  break;
	  }
   }
	return OralCancer;
}

bool CheckSpeechLanguage(char* I8000)
{bool SpeechLanguage = false;
 for( int index = 0; index < 30; index++ ) {
      if(0 == strcmp(I8000, SpeechLanguageCodes[index]))
	  {
	  SpeechLanguage = true;
	  break;
	  }
   }
	return SpeechLanguage;
}


bool CheckLungTransplant(char* I8000)
{bool LungTransplant = false;
 for( int index = 0; index < 14; index++ ) {
      if(0 == strcmp(I8000, LungTransplantCodes[index]))
	  {
	  LungTransplant = true;
	  break;
	  }
   }

	return LungTransplant;
}

bool CheckMajorOrgan(char* I8000)
{bool MajorOrgan = false;
 for( int index = 0; index < 49; index++ ) {
      if(0 == strcmp(I8000, MajorOrganCodes[index]))
	  {
	  MajorOrgan = true;
	  break;
	  }
   }
	return MajorOrgan;
}

bool CheckOpportunisticInfections(char* I8000)
{bool OpportunisticInfections = false;
 for( int index = 0; index < 37; index++ ) {
      if(0 == strcmp(I8000, OpportunisticInfectionsCodes[index]))
	  {
	  OpportunisticInfections = true;
	  break;
	  }
   }
	return OpportunisticInfections;
}

bool CheckBoneNecrosis(char* I8000)
{bool BoneNecrosis = false;
 for( int index = 0; index < 410; index++ ) {
      if(0 == strcmp(I8000, BoneNecrosisCodes[index]))
	  {
	  BoneNecrosis = true;
	  break;
	  }
   }
	return BoneNecrosis;
}

bool CheckMyeloidLeukemia(char* I8000)
{bool MyeloidLeukemia = false;
 for( int index = 0; index < 3; index++ ) {
      if(0 == strcmp(I8000, MyeloidLeukemiaCodes[index]))
	  {
	  MyeloidLeukemia = true;
	  break;
	  }
   }
	return MyeloidLeukemia;
}

bool CheckEndocarditis(char* I8000)
{bool Endocarditis = false;
 for( int index = 0; index < 13; index++ ) {
      if(0 == strcmp(I8000, EndocarditisCodes[index]))
	  {
	  Endocarditis = true;
	  break;
	  }
   }
	return Endocarditis;
}


bool CheckImmuneDisorders(char* I8000)
{bool ImmuneDisorders = false;
 for( int index = 0; index < 47; index++ ) {
      if(0 == strcmp(I8000, ImmuneDisordersCodes[index]))
	  {
	  ImmuneDisorders = true;
	  break;
	  }
   }
	return ImmuneDisorders;
}

bool CheckLiverDisease(char* I8000)
{bool LiverDisease = false;
 for( int index = 0; index < 14; index++ ) {
      if(0 == strcmp(I8000, LiverDiseaseCodes[index]))
	  {
	  LiverDisease = true;
	  break;
	  }
   }
	return LiverDisease;
}

bool CheckNarcolepsy(char* I8000)
{bool Narcolepsy = false;
 for( int index = 0; index < 4; index++ ) {
      if(0 == strcmp(I8000, NarcolepsyCodes[index]))
	  {
	  Narcolepsy = true;
	  break;
	  }
   }
	return Narcolepsy;
}

bool CheckCysticFibrosis(char* I8000)
{bool CysticFibrosis = false;
 for( int index = 0; index < 5; index++ ) {
      if(0 == strcmp(I8000, CysticFibrosisCodes[index]))
	  {
	  CysticFibrosis = true;
	  break;
	  }
   }
	return CysticFibrosis;
}

bool CheckSpecificImmuneDisorders(char* I8000)
{bool SpecificImmuneDisorders = false;
 for( int index = 0; index < 2; index++ ) {
      if(0 == strcmp(I8000, SpecificImmuneDisordersCodes[index]))
	  {
	  SpecificImmuneDisorders = true;
	  break;
	  }
   }
	return SpecificImmuneDisorders;
}

bool CheckMorbidObesity(char* I8000)
{bool MorbidObesity = false;
 for( int index = 0; index < 7; index++ ) {
      if(0 == strcmp(I8000, MorbidObesityCodes[index]))
	  {
	  MorbidObesity = true;
	  break;
	  }
   }
	return MorbidObesity;
}

bool CheckPsoriaticAnthropathy(char* I8000)
{bool PsoriaticAnthropathy = false;
 for( int index = 0; index < 14; index++ ) {
      if(0 == strcmp(I8000, PsoriaticAnthropathyCodes[index]))
	  {
	  PsoriaticAnthropathy = true;
	  break;
	  }
   }
	return PsoriaticAnthropathy;
}

bool CheckChronicPancreatitis(char* I8000)
{bool ChronicPancreatitis = false;
 for( int index = 0; index < 2; index++ ) {
      if(0 == strcmp(I8000, ChronicPancreatitisCodes[index]))
	  {
	  ChronicPancreatitis = true;
	  break;
	  }
   }
	return ChronicPancreatitis;
}

bool CheckProlificRetinopathy(char* I8000)
{bool ProlificRetinopathy = false;
 for( int index = 0; index < 124; index++ ) {
      if(0 == strcmp(I8000, ProlificRetinopathyCodes[index]))
	  {
	  ProlificRetinopathy = true;
	  break;
	  }
   }
	return ProlificRetinopathy;
}

bool CheckImplantedDevice(char* I8000)
{bool ImplantedDevice = false;
 for( int index = 0; index < 322; index++ ) {
      if(0 == strcmp(I8000, ImplantedDeviceCodes[index]))
	  {
	  ImplantedDevice = true;
	  break;
	  }
   }
	return ImplantedDevice;
}

bool CheckAscepticNecrosis(char* I8000)
{bool AscepticNecrosis = false;
 for( int index = 0; index < 241; index++ ) {
      if(0 == strcmp(I8000, AscepticNecrosisCodes[index]))
	  {
	  AscepticNecrosis = true;
	  break;
	  }
   }
	return AscepticNecrosis;
}

bool CheckRespiratoryFailure(char* I8000)
{bool RespiratoryFailure = false;
 for( int index = 0; index < 27; index++ ) {
      if(0 == strcmp(I8000, RespiratoryFailureCodes[index]))
	  {
	  RespiratoryFailure = true;
	  break;
	  }
   }
	return RespiratoryFailure;
}

bool CheckMyelodysplastic(char* I8000)
{bool Myelodysplastic = false;
 for( int index = 0; index < 12; index++ ) {
      if(0 == strcmp(I8000, MyelodysplasticCodes[index]))
	  {
	  Myelodysplastic = true;
	  break;
	  }
   }
	return Myelodysplastic;
}

bool CheckTissueDisorders(char* I8000)
{bool TissueDisorders = false;
 for( int index = 0; index < 137; index++ ) {
      if(0 == strcmp(I8000, TissueDisordersCodes[index]))
	  {
	  TissueDisorders = true;
	  break;
	  }
   }
	return TissueDisorders;
}

bool CheckDiabeticRetinopathy(char* I8000)
{bool DiabeticRetinopathy = false;
 for( int index = 0; index < 130; index++ ) {
      if(0 == strcmp(I8000, DiabeticRetinopathyCodes[index]))
	  {
	  DiabeticRetinopathy = true;
	  break;
	  }
   }
	return DiabeticRetinopathy;
}

bool CheckSevereSkin(char* I8000)
{bool SevereSkin = false;
 for( int index = 0; index < 96; index++ ) {
      if(0 == strcmp(I8000, SevereSkinCodes[index]))
	  {
	  SevereSkin = true;
	  break;
	  }
   }
	return SevereSkin;
}

bool CheckIntractableEpilepsy(char* I8000)
{bool IntractableEpilepsy = false;
 for( int index = 0; index < 22; index++ ) {
      if(0 == strcmp(I8000, IntractableEpilepsyCodes[index]))
	  {
	  IntractableEpilepsy = true;
	  break;
	  }
   }
	return IntractableEpilepsy;
}

bool CheckImmuneDisorder(char* I8000)
{bool ImmuneDisorder = false;
 for( int index = 0; index < 19; index++ ) {
      if(0 == strcmp(I8000, ImmuneDisorderCodes[index]))
	  {
	  ImmuneDisorder = true;
	  break;
	  }
   }
	return ImmuneDisorder;
}

bool CheckCirrhosisLiver(char* I8000)
{bool CirrhosisLiver = false;
 for( int index = 0; index < 10; index++ ) {
      if(0 == strcmp(I8000, CirrhosisLiverCodes[index]))
	  {
	  CirrhosisLiver = true;
	  break;
	  }
   }
	return CirrhosisLiver;
}

bool CheckRespiratoryArrest(char* I8000)
{bool RespiratoryArrest = false;
 for( int index = 0; index < 1; index++ ) {
      if(0 == strcmp(I8000, RespiratoryArrestCodes[index]))
	  {
	  RespiratoryArrest = true;
	  break;
	  }
   }
	return RespiratoryArrest;
}

bool CheckPulmonaryFibrosis(char* I8000)
{bool PulmonaryFibrosis = false;
 for( int index = 0; index < 43; index++ ) {
      if(0 == strcmp(I8000, PulmonaryFibrosisCodes[index]))
	  {
	  PulmonaryFibrosis = true;
	  break;
	  }
   }
	return PulmonaryFibrosis;
}