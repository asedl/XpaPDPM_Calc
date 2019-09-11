mdsgrouperstart: mdsgrouperunix.so clean

mdsgrouperunix.so: mdsgrouper_proc.o mdsgrouper.o BehavioralSymptoms.o CheckI8000Base.o CheckI8000Logic.o ClinicallyComplex.o ConvertToDigit.o DiagnosisCodesLoaded.o ExtensiveServices.o ICD10Retrieval.o MiscCalculations.o NTAComponent.o NursingComponent.o OTComponent.o PTComponent.o ReducedPhysicalFunction.o SLPComponent.o SpecialCareHigh.o SpecialCareLow.o
	g++ -m64 -shared -o mdsgrouperunix.so mdsgrouper_proc.o mdsgrouper.o BehavioralSymptoms.o CheckI8000Base.o CheckI8000Logic.o ClinicallyComplex.o ConvertToDigit.o DiagnosisCodesLoaded.o ExtensiveServices.o ICD10Retrieval.o MiscCalculations.o NTAComponent.o NursingComponent.o OTComponent.o PTComponent.o ReducedPhysicalFunction.o SLPComponent.o SpecialCareHigh.o SpecialCareLow.o -D SOLARIS

mdsgrouper_proc.o:
	g++ -m64 -fPIC -c mdsgrouper_proc.cpp -I../oci/include -D SOLARIS

BehavioralSymptoms.o:
	g++ -m64 -fPIC -c BehavioralSymptoms.cpp -D SOLARIS
	
CheckI8000Base.o:
	g++ -m64 -fPIC -c CheckI8000Base.cpp -D SOLARIS
	
CheckI8000Logic.o:
	g++ -m64 -fPIC -c CheckI8000Logic.cpp -D SOLARIS
	
ClinicallyComplex.o:
	g++ -m64 -fPIC -c ClinicallyComplex.cpp -D SOLARIS
	
ConvertToDigit.o:
	g++ -m64 -fPIC -c ConvertToDigit.cpp -D SOLARIS
	
DiagnosisCodesLoaded.o:
	g++ -m64 -fPIC -c DiagnosisCodesLoaded.cpp -D SOLARIS
	
ExtensiveServices.o:
	g++ -m64 -fPIC -c ExtensiveServices.cpp -D SOLARIS
	
ICD10Retrieval.o:
	g++ -m64 -fPIC -c ICD10Retrieval.cpp -D SOLARIS
	
mdsgrouper.o:
	g++ -m64 -fPIC -c mdsgrouper.cpp -D SOLARIS
	
MiscCalculations.o:
	g++ -m64 -fPIC -c MiscCalculations.cpp -D SOLARIS
	
NTAComponent.o:
	g++ -m64 -fPIC -c NTAComponent.cpp -D SOLARIS

NursingComponent.o:
	g++ -m64 -fPIC -c NursingComponent.cpp -D SOLARIS
	
OTComponent.o:
	g++ -m64 -fPIC -c OTComponent.cpp -D SOLARIS
	
PTComponent.o:
	g++ -m64 -fPIC -c PTComponent.cpp -D SOLARIS

ReducedPhysicalFunction.o:
	g++ -m64 -fPIC -c ReducedPhysicalFunction.cpp -D SOLARIS
	
SLPComponent.o:
	g++ -m64 -fPIC -c SLPComponent.cpp -D SOLARIS

SpecialCareHigh.o:
	g++ -m64 -fPIC -c SpecialCareHigh.cpp -D SOLARIS

SpecialCareLow.o:
	g++ -m64 -fPIC -c SpecialCareLow.cpp -D SOLARIS
	
clean:
	rm BehavioralSymptoms.o
	rm CheckI8000Base.o
	rm CheckI8000Logic.o
	rm ClinicallyComplex.o
	rm ConvertToDigit.o
	rm DiagnosisCodesLoaded.o
	rm ExtensiveServices.o
	rm ICD10Retrieval.o
	rm mdsgrouper.o
	rm MiscCalculations.o
	rm NTAComponent.o
	rm NursingComponent.o
	rm OTComponent.o
	rm PTComponent.o
	rm ReducedPhysicalFunction.o
	rm SLPComponent.o
	rm SpecialCareHigh.o
	rm SpecialCareLow.o
	rm mdsgrouper_proc.o
