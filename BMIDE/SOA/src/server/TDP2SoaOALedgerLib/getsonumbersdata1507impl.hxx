/* 
 @<COPYRIGHT>@
 ==================================================
/****************************************************************
 File Name : getsonumbersdata1507impl.hxx
 Functionality : Header files,member function and member variables declaration.
 Author : Bhavana Patil
 ==================================================
 @<COPYRIGHT>@
*/

#ifndef TEAMCENTER_SERVICES_OALEDGERLIB_2015_07_GETSONUMBERSDATA_IMPL_HXX 
#define TEAMCENTER_SERVICES_OALEDGERLIB_2015_07_GETSONUMBERSDATA_IMPL_HXX


#include <getsonumbersdata1507.hxx>

#ifdef __cplusplus
extern "C" {
	}
#endif
#include<conio.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include<tc\tc.h>
#include <time.h>
#include<sa\user.h>
#include<tccore\item.h>
#include<string.h>
#include<tccore\workspaceobject.h>
#include<tccore\aom_prop.h>
#include<tccore\aom.h>
#include<tc\emh.h>
#include<fclasses\tc_string.h>
#include <fclasses/tc_date.h>
#include<stdlib.h>
#include <tcinit/tcinit.h>
#include <tccore/item_errors.h>
#include <tc/tc.h>
#include <tc/tc_util.h>
#include<vector>
#include<map>
#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>
#include <sstream>
#include <iterator>
#include <tc/Folder.h>
#include <qry/qry.h>
#include <tc/LoggedInUser.hxx>
#include <base_utils/TcResultStatus.hxx>
#include <base_utils/IFail.hxx>
#include <direct.h>
#include <process.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <direct.h>
#include <TCCORE/grm.h>
#include<sa/sa.h>
#include<epm/epm_toolkit_tc_utils.h>
#include<epm/epm.h>
#include <map>
#include <utility>
#include <assert.h>
#include<limits.h>
#include <algorithm>
#include <cctype>

using namespace std;

//#define EXIT_FAILURE 1
#define ITK_CALL(x) {           \
    int stat;                     \
    char *err_string;             \
    if( (stat = (x)) != ITK_ok)   \
    {                             \
    EMH_get_error_string (NULLTAG, stat, &err_string);                 \
    cout<<"\n ERROR: "<<stat << "ERROR MSG: \n"<<err_string;           \
    cout<<"\n FUNCTION: "<<#x << "\n FILE:"<<__FILE__ <<"\n LINE : " <<__LINE__;           \
    if(err_string) MEM_free(err_string);                                \
    }                                                                    \
}

#define STRING "string"
#define INT "integer"
#define DOUBLE "double"
#define DATE "date"
#include <OALedgerLib_exports.h>

namespace TDP2
{
    namespace Soa
    {
        namespace OALedgerLib
        {
            namespace _2015_07
            {
                class GetSONumbersDataImpl;
            }
        }
    }
}
void tdps_fetchData(vector <const char*> QryInpVec);
int checkValuePresentOrNot(std::vector<char*> array,char *eachvalue);
static char *itoa_simple_helper(char *dest, int i);
char *itoa_simple(char *dest, int i) ;
int soSearchQuery(std::vector<const char*> QryInpVec,int *ifound,tag_t **soFoudTags);
int getMachineList(int objCnt,tag_t *objectList,char**sDataValue);
int getOutputObjTag(char *inp_type,int objCnt,tag_t *objectList,tag_t *outputObj);
int getdisplayStr(tag_t objTag,char** sDataValue);
char* getSubStrItem(const char *itemstr,int position,int length);
char* chop(const char *inp);
std::vector< char*> readFilePropertyLines();
string convertToString(char* a, int size);
bool invalidChar (char c);
void stripUnicode(string & str);

class SOAOALEDGERLIB_API TDP2::Soa::OALedgerLib::_2015_07::GetSONumbersDataImpl : public TDP2::Soa::OALedgerLib::_2015_07::GetSONumbersData

{
public:

    virtual GetSONumbersDataImpl::GetPropertyResponse getSOnumbersObjProperties ( const std::vector< std::string >& soInputFieldVector );


};

#include <OALedgerLib_undef.h>
#endif
