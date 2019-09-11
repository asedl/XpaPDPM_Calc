 #include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <time.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>
#include "mdsgrouper.h"
#include <oci.h>


typedef struct myCtx 
{ 
  OCIExtProcContext * ctx;		/* Context passed to all external procs */
  OCIEnv *            envhp;    /* OCI environment handle */
  OCISvcCtx *         svchp;    /* OCI Service handle */
  OCIError *          errhp;    /* OCI Error handle */
}
myCtxStruct;


/*
 * This routine works like the PLSQL builtin by the same name.  It 
 * sets an application defined error message and error code.  It also
 * works like the C printf routine in that you pass to it a C format
 * string and then a varying number of arguments
 *
 * The resulting string is limited to 8k in size.  Note the call
 * to debugf.  Debugf is the macro above, we will use it throughout
 * the code to 'instrument' it.  
 */

static void raise_application_error(  myCtxStruct * myCtx,
                                     int           errCode, 
                                     char *        errMsg, ...)
{
	char    msg[8192];
	va_list ap;

    va_start(ap,errMsg);
    vsprintf( msg, errMsg, ap );
    va_end(ap);

    if ( OCIExtProcRaiseExcpWithMsg(myCtx->ctx,errCode,(oratext*)msg,0) == 
                                                OCIEXTPROC_ERROR )
		return;
}



/* 
 * This is a convienence routine to allocate storage for an error message
 * and return it.  Note that the type of storage we allocate is CALL based,
 * hence, when we return from the extproc, OCI will automatically free it
 * for us.
 *
 * This routine removes the trailing newline from the error message as well
 */
static char * lastOciError( myCtxStruct * myCtx )
{
sb4       errcode;
char      * errbuf = (char*)OCIExtProcAllocCallMemory( myCtx->ctx, 256 );

    strcpy( errbuf, "unable to retrieve message\n" );
    OCIErrorGet( myCtx->errhp, 1, NULL, &errcode, (oratext*)errbuf, 
                 255, OCI_HTYPE_ERROR );
    errbuf[strlen(errbuf)-1] = 0;
    return errbuf;
}

/*
 * Here is the init routine.  This routine must be called very early 
 * in the execution of any external procedure using this template.  It
 * will 
 *   o retrieve the OCI handles and set them in the context for us
 *   o retrieve our CONTEXT, if the context has not yet been allocated,
 *     it will allocate storage for our context and save it.  The memory
 *     is allocated to last as long as our process lasts.
 *   o it will read and retrieve the values from the parameter file if
 *     they have not been.  You can add more parameters of your own by:
 *     - adding elements the the myCtxStruct above
 *     - increment the counter sent into ExtractSetNumKeys appropriately
 *     - adding a call to ExtractSetKey
 *     - adding a call to ExtractTo* (string, bool, int, etc)
 *   o it will initialize the FILE apis via OCIFileInit().  This is crucial
 *     for the successful operation of debugf above.
 *
 * You may add other "init" type of calls.  for example, if you choose to 
 * use the String Formatting Interface (similar to vsprintf() but in OCI)
 * You could add a call to OCIFormatInit() here.  You would add the 
 * corresponding OCIFormatTerm() call to term() below.
 *
 */
static myCtxStruct * init( OCIExtProcContext * ctx )
{
ub1          bfalse = 0;
myCtxStruct *myCtx = NULL;
OCIEnv      *envhp;
OCISvcCtx   *svchp;
OCIError    *errhp;
ub4          key = 1;


    if ( OCIExtProcGetEnv( ctx, &envhp, &svchp, &errhp ) != OCI_SUCCESS )
    {
         OCIExtProcRaiseExcpWithMsg(ctx,20000,
                                   (oratext*)"failed to get OCI Connection",0);
         return NULL;
    }
    if ( OCIContextGetValue( envhp, errhp, (ub1*)&key, sizeof(key), 
                             (dvoid**)&myCtx ) != OCI_SUCCESS ) 
    {
        OCIExtProcRaiseExcpWithMsg(ctx,20000,(oratext*)"failed to get OCI Context",0);
        return NULL;
    }
 
    if ( myCtx == NULL )
    {
        if ( OCIMemoryAlloc( envhp, errhp, (dvoid**)&myCtx, 
                             OCI_DURATION_PROCESS, 
                             sizeof(myCtxStruct), 
                             OCI_MEMORY_CLEARED ) != OCI_SUCCESS )
        {
            OCIExtProcRaiseExcpWithMsg(ctx,20000,
                                      (oratext*)"failed to get OCI Memory",0);
            return NULL;
        }
        myCtx->ctx   = ctx;
        myCtx->envhp = envhp;
        myCtx->svchp = svchp;
        myCtx->errhp = errhp;
        if ( OCIContextSetValue( envhp, errhp, 
                                 OCI_DURATION_SESSION, (ub1*)&key, 
                                 sizeof(key), myCtx ) != OCI_SUCCESS )
        {
            raise_application_error(myCtx, 20000, "%s", lastOciError(myCtx));
            return NULL;
        }

        if (( OCIExtractInit( envhp, errhp ) != OCI_SUCCESS )  ||
            ( OCIExtractSetNumKeys( envhp, errhp, 3 ) != OCI_SUCCESS ) ||
            ( OCIExtractTerm( envhp, errhp ) != OCI_SUCCESS ))
        {
            raise_application_error(myCtx, 20000, "%s", lastOciError(myCtx));
            return NULL;
        }
    }
    else
    {
        myCtx->ctx   = ctx;
        myCtx->envhp = envhp;
        myCtx->svchp = svchp;
        myCtx->errhp = errhp;
    }
    if ( OCIFileInit( myCtx->envhp, myCtx->errhp ) != OCI_SUCCESS ) 
    {
        raise_application_error(myCtx, 20000, "%s", lastOciError(myCtx));
        return NULL;
    }
    return myCtx;
}

/* 
 * This must be called after any successful call to init() above.  It
 * should be the last thing you call in your routine before returning
 * from C to SQL
 */
static void term( myCtxStruct * myCtx )
{
    OCIFileTerm( myCtx->envhp, myCtx->errhp );
}

/* 
 * error codes go here.  Error numbers must be in the range of
 * 20000 to 20999.  Each extproc will register all of their error
 * codes here.  It will make it easier to "pragma EXCEPTION_INIT" them
 * later in the PLSQL code.  This will let plsql programs catch nice 
 * named exceptions
 */

#define ERROR_OCI_ERROR     20001
#define ERROR_STR_TOO_SMALL 20002
#define ERROR_RAW_TOO_SMALL 20003
#define ERROR_CLOB_NULL     20004
#define ERROR_ARRAY_NULL    20005


extern "C"
#ifdef SOLARIS
int
#else
__declspec(dllexport)
int
#endif
callmdsgrouper( OCIExtProcContext * ctx   /* CONTEXT */,
            char * pdpmRecord, 
            char * HIPPSCode, 
			int * errorcode,
			char * VersionCode)
			

{
	int ret_code = 0;
	double     tmp_dbl;
	boolean    exists;
	int        i;
	myCtxStruct*myCtx;

    if ( (myCtx = init( ctx )) == NULL ) return -1;
	
	ret_code =  mdsgrouper(pdpmRecord, HIPPSCode,  errorcode, VersionCode);

    term(myCtx);

	return ret_code;
}

// extern "C"
// #ifdef SOLARIS
// int
// #else
// __declspec(dllexport)
// int
// #endif
// callcmg310_xml( OCIExtProcContext * ctx   /* CONTEXT */,
//                char * sIrfRecord, 
//                char * sCmgValue, 
//			    int * iErrorCode, 
//			    char * sMotorScore, 
//			    int * iCognitive, 
//			    int * iAge, 
//			    char *sCmgVersion, 
//			    char * sDllVersion )
//{
//	int ret_code = 0;
//	double     tmp_dbl;
//	boolean    exists;
//	int        i;
//	myCtxStruct*myCtx;

//    if ( (myCtx = init( ctx )) == NULL ) return -1;

//	ret_code =  CMG_310_XML( sIrfRecord, 
//                             sCmgValue, 
//			                 iErrorCode, 
//			                 sMotorScore, 
//			                 iCognitive, 
//			                 iAge, 
//			                 sCmgVersion, 
//			                 sDllVersion ); 


//    term(myCtx);

//	return ret_code;
// }

// extern "C"
// #ifdef SOLARIS
// int
// #else
// __declspec(dllexport)
// int
// #endif
// callcmg310_xml_file( OCIExtProcContext * ctx   /* CONTEXT */,
//                    char * sFileName, 
//                     char * sCmgValue, 
//			         int * iErrorCode, 
//			         char * sMotorScore, 
//			         int * iCognitive, 
//			         int * iAge, 
//			         char *sCmgVersion, 
//			         char * sDllVersion )
// {
//	int ret_code = 0;
//	double     tmp_dbl;
//	boolean    exists;
//	int        i;
//	myCtxStruct*myCtx;

//   if ( (myCtx = init( ctx )) == NULL ) return -1;

//	ret_code =  CMG_310_XML_FILE( sFileName, 
//                                 sCmgValue, 
//			                      iErrorCode, 
//			                      sMotorScore, 
//			                      iCognitive, 
//			                      iAge, 
//			                      sCmgVersion, 
//			                      sDllVersion ); 


//    term(myCtx);

//	return ret_code;
// }

