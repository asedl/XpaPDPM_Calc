#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <ctime>
#include "ExtensiveServices.h"

bool DetermineTracheostomyCareBool(char* O0100E2)
{bool TracheostomyCare = false;
 if (0 == strcmp(O0100E2, "1"))
  {
  TracheostomyCare = true; 
  }
 return TracheostomyCare;
}

bool DetermineVentilatorBool(char* O0100F2)
{bool Ventilator = false;
 if (0 == strcmp(O0100F2, "1"))
  {
  Ventilator = true; 
  }
 return Ventilator;
}

bool DetermineIsolationBool(char* O0100M2)
{bool Isolation = false;
 if (0 == strcmp(O0100M2, "1"))
  {
  Isolation = true; 
  }
 return Isolation;
}

bool ExtensiveServicesStep1(bool Tracheostomy, bool Ventilator, bool Isolation)
{bool ExtensiveServices1 = false;
  if ((Tracheostomy)||(Ventilator)||(Isolation))
  {
	ExtensiveServices1 = true; 
  }
return ExtensiveServices1;
}

