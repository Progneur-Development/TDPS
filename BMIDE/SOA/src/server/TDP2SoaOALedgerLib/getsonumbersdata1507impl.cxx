/* 
 @<COPYRIGHT>@
 ==================================================
 Copyright 2012
 Siemens Product Lifecycle Management Software Inc.
 All Rights Reserved.
 ==================================================
 @<COPYRIGHT>@
*/

/****************************************************************
 File Name : getsonumbersdata1507impl.cxx
 Functionality : To get SO search criteria inputs from RAC code.
                 Read property names,business objects and data types from property file
                 file which is created by RAC code.Fetch Data from Teamcenter server
                 Send response to RAc code through map
 Author : Bhavana Patil
 ******************************************************************/

#include <unidefs.h>
#if defined(SUN)
#include <unistd.h>
#endif

#include <getsonumbersdata1507impl.hxx>

using namespace TDP2::Soa::OALedgerLib::_2015_07;
using namespace Teamcenter::Soa::Server;
std::vector< char*> uniqueSOITemVector;

std::map<std::string,std::string> allSOdetailsList;
GetSONumbersDataImpl::GetPropertyResponse GetSONumbersDataImpl::getSOnumbersObjProperties ( const std::vector< std::string >& soInputFieldVector )
{
 	GetPropertyResponse response;
	printf("inside GetSONumbersDataImpl function");

	if(soInputFieldVector.size()>0)
	{
		ITK_set_bypass(true);

		std::vector<const char*> QryInpVec(soInputFieldVector.size(),nullptr);
		for (int i=0; i<soInputFieldVector.size();i++) {
			QryInpVec[i]= soInputFieldVector[i].c_str();
		}

		allSOdetailsList.clear();
		tdps_fetchData(QryInpVec);

		ITK_set_bypass(false);

	}
	response.propertyResp=allSOdetailsList;

	return response;
}

void tdps_fetchData(vector <const char*> QryInpVec)
{
		uniqueSOITemVector.clear();

		//Read file data
		std::vector< char*> PropertyDataVec ;
		int iStatus = ITK_ok;
		int		 ifound					  =	   0;
		char*	 SOnumber				  =   NULL;
		char    *sDataValue				  =	  NULL;
		tag_t*   soFoudTags				  =	   NULL;

		tag_t	TDPSGenSpec				  =   NULLTAG;
		tag_t	TDPSDocForm				  =   NULLTAG;
		tag_t	TDPSAccForm				  =   NULLTAG;
		tag_t	TDPSContSummForm		  =   NULLTAG;
		tag_t	TDPSSOEnggForm			  =   NULLTAG;
		tag_t 	TDPSSORevisionMaster	  =	  NULLTAG;
		tag_t	TDPSFERTRevisionMaster	  =   NULLTAG;
		tag_t	TDPSMachineRevisionMaster =   NULLTAG;
		tag_t   TDPSFERTRevision		  =	  NULLTAG;
		tag_t   TDPSSOItemRevision		  =   NULLTAG;
		tag_t   TDPSMachineRevision		  =	  NULLTAG;
		std::vector< char*> readDataVec ;

		PropertyDataVec = readFilePropertyLines();

		soSearchQuery(QryInpVec,&ifound,&soFoudTags);
		int soCnt=0;
		//Get unique SO item list from  query search
		if(ifound>0)
		{
			int ii=0;
			for(ii=0;ii<ifound;ii++)
			{
				  tag_t tTDPSSO=NULLTAG;
				  char  *sonamestr		  =	  NULL;

				   ITEM_ask_item_of_rev(soFoudTags[ii],&tTDPSSO);
				   ITK_CALL(AOM_ask_value_string(tTDPSSO,"item_id",&sonamestr));

					int result=checkValuePresentOrNot(uniqueSOITemVector,sonamestr);
					if(result==0)
					{
						//printf("\n ** SO Number=%s",sonamestr);
						uniqueSOITemVector.push_back(sonamestr);


				int   SOscoObjcnt		  =  0;
				int   SOPriObjcnt		  =	 0;
				tag_t *SOsecondary_objects  =  NULL;
				tag_t *SOPrimary_objects  =   NULL;
				TDPSGenSpec				  =   NULLTAG;
				TDPSDocForm				  =   NULLTAG;
				TDPSAccForm				  =   NULLTAG;
				TDPSContSummForm		  =   NULLTAG;
				TDPSSOEnggForm			  =   NULLTAG;
			 	TDPSSORevisionMaster	  =	  NULLTAG;
				TDPSFERTRevisionMaster	  =   NULLTAG;
				TDPSMachineRevisionMaster =   NULLTAG;
			    TDPSFERTRevision		  =	  NULLTAG;
			    TDPSSOItemRevision		  =   NULLTAG;
			    TDPSMachineRevision		  =	  NULLTAG;
			    tag_t fertRevTag		  =   NULLTAG;
				tag_t machineRevTag		  =   NULLTAG;
			    tag_t fertTag			  =   NULLTAG;
				tag_t machineTag		  =   NULLTAG;
				char  *sonamestr		  =	  NULL;


				ITEM_ask_latest_rev(tTDPSSO,&TDPSSOItemRevision);
				ITK_CALL(AOM_ask_value_string(TDPSSOItemRevision,"item_id",&sonamestr));
				printf("%d=%s \t",soCnt,sonamestr);

				//TDPS Fert revision data
				ITK_CALL(GRM_list_primary_objects_only(TDPSSOItemRevision,NULLTAG,&SOPriObjcnt,&SOPrimary_objects));
				if(SOPriObjcnt>0)
				{
					//char* objstr=NULL;
					getOutputObjTag("TDPSFERTRevision",SOPriObjcnt,SOPrimary_objects,&fertRevTag);
					 ITEM_ask_item_of_rev(fertRevTag,&fertTag);
					 ITEM_ask_latest_rev(fertTag,&TDPSFERTRevision);
					// AOM_ask_value_string(TDPSFERTRevision,"object_string",&objstr);
					//printf("\n objstr==%s",objstr);
				}

				//TDPSSO Item Rvision secondary objects
				ITK_CALL(GRM_list_secondary_objects_only(TDPSSOItemRevision,NULLTAG,&SOscoObjcnt,&SOsecondary_objects));
				if(SOscoObjcnt>0)
				{
					//TDPSSO Item Rvision master form
					iStatus=getOutputObjTag("TDPSSORevisionMaster",SOscoObjcnt,SOsecondary_objects,&TDPSSORevisionMaster);
					if(iStatus!=ITK_ok)
						TDPSSORevisionMaster=NULLTAG;
				//	printf("\n TDPSSORevisionMaster==%d",TDPSSORevisionMaster);
					//TDPS Machine item revision
					iStatus=getOutputObjTag("TDPSMachineRevision",SOscoObjcnt,SOsecondary_objects,&machineRevTag);
					if(iStatus!=ITK_ok)
						machineRevTag=NULLTAG;
					else
					{
						 ITEM_ask_item_of_rev(machineRevTag,&machineTag);
					     ITEM_ask_latest_rev(machineTag,&TDPSMachineRevision);
					}

					//printf("\n TDPSMachineRevision==%d",TDPSMachineRevision);

					//TDPSMACHINE REVISION MASTER FORM data
					tag_t  *MachineScondary_objects= NULLTAG;
					int MacscoObjcnt=0;

					if(TDPSMachineRevision!=NULLTAG)
					{
					  	ITK_CALL(GRM_list_secondary_objects_only(TDPSMachineRevision,NULLTAG,&MacscoObjcnt,&MachineScondary_objects));
						if(MacscoObjcnt>0)
						{
							iStatus=getOutputObjTag("TDPSMachineRevisionMaster",MacscoObjcnt,MachineScondary_objects,&TDPSMachineRevisionMaster);
							if(iStatus!=ITK_ok)
								TDPSMachineRevisionMaster=NULLTAG;
						//	printf("\n TDPSMachineRevisionMaster==%d",TDPSMachineRevisionMaster);
						}
						SAFE_SM_FREE(MachineScondary_objects);
					}


					//TDPSSO TDPSSOEnggForm form
					iStatus=getOutputObjTag("TDPSSOEnggForm",SOscoObjcnt,SOsecondary_objects,&TDPSSOEnggForm);
					//printf("\n iStatus==%d",iStatus);
					if(iStatus!=ITK_ok)
					   TDPSSOEnggForm=NULLTAG;
					//printf("\n TDPSSOEnggForm==%d",TDPSSOEnggForm);

					//TDPSSO TDPSGenSpec form
					iStatus=getOutputObjTag("TDPSGenSpec",SOscoObjcnt,SOsecondary_objects,&TDPSGenSpec);
					if(iStatus!=ITK_ok)
					   TDPSGenSpec=NULLTAG;
					//printf("\n TDPSGenSpec==%d",TDPSGenSpec);

					//TDPSSO TDPSDocForm form
					iStatus=getOutputObjTag("TDPSDocForm",SOscoObjcnt,SOsecondary_objects,&TDPSDocForm);
					if(iStatus!=ITK_ok)
					   TDPSDocForm=NULLTAG;
					//printf("\n TDPSDocForm==%d",TDPSDocForm);

					//TDPSSO TDPSDocForm form
					iStatus=getOutputObjTag("TDPSAccForm",SOscoObjcnt,SOsecondary_objects,&TDPSAccForm);
					if(iStatus!=ITK_ok)
					   TDPSAccForm=NULLTAG;
					//printf("\n TDPSAccForm==%d",TDPSAccForm);

					//TDPSSO TDPSDocForm form
					iStatus=getOutputObjTag("TDPSContSummForm",SOscoObjcnt,SOsecondary_objects,&TDPSContSummForm);
					if(iStatus!=ITK_ok)
					   TDPSContSummForm=NULLTAG;
					//printf("\n TDPSContSummForm==%d",TDPSContSummForm);
				}

				//TDPSFERT REVISION MASTER FORM data
				tag_t  *FERTsecondary_objects= NULLTAG;
				int FERTscoObjcnt=0;

				if(TDPSFERTRevision!=NULLTAG)
				{
					ITK_CALL(GRM_list_secondary_objects_only(TDPSFERTRevision,NULLTAG,&FERTscoObjcnt,&FERTsecondary_objects));
					if(FERTscoObjcnt>0)
					{
						iStatus=getOutputObjTag("TDPSFERTRevisionMaster",FERTscoObjcnt,FERTsecondary_objects,&TDPSFERTRevisionMaster);
						if(iStatus!=ITK_ok)
					        TDPSFERTRevisionMaster=NULLTAG;

					}
				}



				 std::string DataValStr;
				//Read File real names and fetch values

				int ik=0;
				for(ik=0;ik<PropertyDataVec.size();ik++)
				{

					char *temp =NULL;
					//printf("\n PropertyDataVec[%d]==%s",ik,PropertyDataVec[ik]);

					readDataVec.clear();
					 char* tok;
					 temp=(char*)malloc(tc_strlen(PropertyDataVec[ik])+1);
					  tc_strcpy(temp,PropertyDataVec[ik]);

						for (tok = tc_strtok(temp, "=");
							tok && *tok;
							tok = tc_strtok(NULL, "="))
						{
							//printf("\n *tok=%s",tok);
								readDataVec.push_back(tok);

						}

							char *businessObjNm=readDataVec[0];
							char *prop_realNm=readDataVec[1];
							//char *prop_dispNm=readDataVec[2];
							char *prop_type=chop(readDataVec[3]); // commented unresolved issue perpose

							//printf("\n businessObjNm==%s   prop_realNm==%s   prop_dispNm=%s  prop_type==%s",businessObjNm,prop_realNm,prop_dispNm,prop_type);

							tag_t businessObj = NULLTAG;

							sDataValue        = NULL;
							sDataValue=(char*)malloc(sizeof(char)* 2000);
							tc_strcpy(sDataValue,"");

								if(tc_strcmp(businessObjNm,"TDPSSOItemRevision")==0)
								{
									businessObj=TDPSSOItemRevision;
								}else if(tc_strcmp(businessObjNm,"TDPSFERTRevision")==0)
								{
									businessObj=TDPSFERTRevision;
								}else if(tc_strcmp(businessObjNm,"TDPSMachineRevision")==0)
								{
									businessObj=TDPSMachineRevision;
								}
								else if(tc_strcmp(businessObjNm,"TDPSMachineRevisionMaster")==0)
								{
									businessObj=TDPSMachineRevisionMaster;
								}else if(tc_strcmp(businessObjNm,"TDPSSORevisionMaster")==0)
								{
									businessObj=TDPSSORevisionMaster;
								}else if(tc_strcmp(businessObjNm,"TDPSFERTRevisionMaster")==0)
								{
									businessObj=TDPSFERTRevisionMaster;
								}else if(tc_strcmp(businessObjNm,"TDPSSOEnggForm")==0)
								{
									businessObj=TDPSSOEnggForm;
								}else if(tc_strcmp(businessObjNm,"TDPSGenSpec")==0)
								{
									businessObj=TDPSGenSpec;
								}else if(tc_strcmp(businessObjNm,"TDPSDocForm")==0)
								{
									businessObj=TDPSDocForm;
								}else if(tc_strcmp(businessObjNm,"TDPSAccForm")==0)
								{
									businessObj=TDPSAccForm;
								}else if(tc_strcmp(businessObjNm,"TDPSContSummForm")==0)
								{
									businessObj=TDPSContSummForm;
								}

								if(tc_strcmp(businessObjNm,"SO Numbers")==0 || tc_strcmp(prop_realNm,"SO Numbers")==0 )
								{
									if(TDPSSOItemRevision!=NULLTAG)
									getdisplayStr(TDPSSOItemRevision,&sDataValue);
									SOnumber=(char*)malloc(tc_strlen(sDataValue)+1);
									tc_strcpy(SOnumber,sDataValue);
									//printf("%s=%s\n",prop_realNm, sDataValue);
								}else if(tc_strcmp(businessObjNm,"FERT")==0 || tc_strcmp(prop_realNm,"FERT")==0)
								{
									if(TDPSFERTRevision!=NULLTAG)
									{	getdisplayStr(TDPSFERTRevision,&sDataValue);
										//AOM_ask_value_string(TDPSFERTRevision,"item_id",&sDataValue);
									}
									//printf("%s=%s\n",prop_realNm, sDataValue);
								}
								else if(tc_strcmp(businessObjNm,"Machine No")==0 || tc_strcmp(prop_realNm,"Machine No")==0)
								{
									getMachineList(SOscoObjcnt,SOsecondary_objects,&sDataValue);
									//printf("%s=%s\n",prop_realNm, sDataValue);
								}
								else if(tc_strcmp(STRING,prop_type)==0)
								{
										if(businessObj!=NULLTAG)
											ITK_CALL(AOM_ask_value_string(businessObj,prop_realNm,&sDataValue));
										//printf("%s=%s\n",prop_realNm, sDataValue);
								}
								else if(tc_strcmp(INT,prop_type)==0)
								{
									int dval=0;
									if(businessObj!=NULLTAG)
									{
										ITK_CALL(AOM_ask_value_int(businessObj,prop_realNm,&dval));
										itoa_simple(sDataValue,dval);
									}
									//printf("%s=%s\n",prop_realNm, sDataValue);
								}
								else if(tc_strcmp(DOUBLE,prop_type)==0)
								{
									double dval=0.0;
									if(businessObj!=NULLTAG)
									{
										ITK_CALL(AOM_ask_value_double(businessObj,prop_realNm,&dval));

									}
									if(tc_strcmp(prop_realNm,"tdps_mva"))
										sprintf(sDataValue,"%.4f", dval);
									else if(tc_strcmp(prop_realNm,"tdps_mw"))
										sprintf(sDataValue,"%.4f", dval);
									else if(tc_strcmp(prop_realNm,"tdps_voltage"))
										sprintf(sDataValue,"%.4f", dval);
									else if(tc_strcmp(prop_realNm,"tdps_inertia_constant"))
										sprintf(sDataValue,"%.4f", dval);
									else
										sprintf(sDataValue,"%.2f", dval);
									//printf("%s=%s\n",prop_realNm, sDataValue);
								}
								else if(tc_strcmp(DATE,prop_type)==0)
								{
									date_t tDateT;
									if(businessObj!=NULLTAG)
									{
										ITK_CALL(AOM_ask_value_date(businessObj,prop_realNm,&tDateT));
										ITK_CALL(ITK_date_to_string(tDateT,&sDataValue));
									}
								}
								//printf("%s=%s\n",prop_realNm, sDataValue);

						  if(sDataValue==NULL || tc_strcmp(sDataValue,"")==0)
								sDataValue="BLANK_VALUE";

							DataValStr.append(prop_realNm);
							DataValStr.append("|*|");
							DataValStr.append(sDataValue);
							if(ik!=PropertyDataVec.size()-1)
							DataValStr.append("|^|");



						  //  printf("\n Real Name='%s'  sDataValue===%s",prop_realNm,sDataValue);
					}

				std::string propName(SOnumber);
				//std::string propValue(propertyValues);
				allSOdetailsList.insert(std::pair <std::string,std::string>(propName,DataValStr));

			/*	if(sDataValue!=NULL)
					SAFE_SM_FREE(sDataValue);*/

				SAFE_SM_FREE(SOsecondary_objects);
				SAFE_SM_FREE(SOPrimary_objects);
			    SAFE_SM_FREE(FERTsecondary_objects);

				soCnt++;
			}

		}
	  }
	/*  if(sDataValue!=NULL)
			SAFE_SM_FREE(sDataValue);*/

}
int getMachineList(int objCnt,tag_t *objectList,char** sDataValue)
{
	char *obj_type = NULL;
	int iStatus = ITK_ok;
	std:: string machineNames;
	char* strNm=NULL;
	tag_t machineTag=NULLTAG;
	tag_t tTDPSMachineRev=NULLTAG;
	if(objCnt>0)
	{
		for(int oCnt=0;oCnt<objCnt;oCnt++)
		{
			iStatus=AOM_ask_value_string(objectList[oCnt],"object_type",&obj_type);
			if(iStatus==ITK_ok)
			{
				if(tc_strcmp(obj_type,"TDPSMachineRevision")==0)
				{
					 ITEM_ask_item_of_rev(objectList[oCnt],&machineTag);
					 ITEM_ask_latest_rev(machineTag,&tTDPSMachineRev);

					getdisplayStr(tTDPSMachineRev,&strNm);
					machineNames.append(strNm);
					if(oCnt!=objCnt-1)
					  machineNames.append(",");
				}
			}

		}
	}
	if(!machineNames.empty() && machineNames.size()!=0)
	{
		if (machineNames.find(',') != std::string::npos)
		{
			 machineNames.pop_back();
		}
		*sDataValue=(char*)realloc(*sDataValue,machineNames.size()+1);
		tc_strcpy(*sDataValue,machineNames.c_str());
	}else
	{
		tc_strcpy(*sDataValue,"");
	}
	return iStatus;
}
std::vector< char*> readFilePropertyLines()
{
	std::vector< char*> PropertyVec;
	PropertyVec.clear();
	//Read File real names and fetch values
	char *RightTablePrpertyFile="C:/Temp/TDPS_COLUMNS/RightTablePropertiesSOAInpFile.txt";
	//char *RightTablePrpertyFile="F:/Progneur_Work/Prop_file/RightColumnList1_ITK.txt";
	FILE* stream = fopen(RightTablePrpertyFile, "r");
	char line[1024];
	char *ln=NULL;
		while (fgets(line, 1024, stream))
		{
			ln=NULL;
			ln=(char*)malloc(strlen(line)+1);
			tc_strcpy(ln,line);

			PropertyVec.push_back(ln);
		}

	return PropertyVec;
}
string convertToString(char* a, int size)
{
    int i;
    string s = "";
    for (i = 0; i < size; i++) {
        s = s + a[i];
    }
    return s;
}
bool invalidChar (char c)
{
    return !(c>=0 && c <128);
}
void stripUnicode(string & str)
{
	 str.erase(std::remove_if(str.begin(),str.end(), invalidChar), str.end());
}
int checkValuePresentOrNot(std::vector<char*> array,char *eachvalue)
{
	int vals = 0;
	for(std::vector<char*>::iterator it = array.begin(); it != array.end(); ++it)
	{
		//Check if Group is Present in Vector or not
		if(tc_strcmp(*it,eachvalue)==0)
		{
			vals=1;
		}

	}
	return vals;
}

/* removes the newline character from a string */
char* chop(const char *inp)
{
	char* s=(char *)malloc(tc_strlen(inp)+1);
	tc_strcpy(s,inp);
	s[strcspn ( s, "\n" )] = '\0';
	return s;
}

static char *itoa_simple_helper(char *dest, int i) {
  if (i <= -10) {
    dest = itoa_simple_helper(dest, i/10);
  }
  *dest++ = '0' - i%10;
  return dest;
}

char *itoa_simple(char *dest, int i) {
  char *s = dest;
  if (i < 0) {
    *s++ = '-';
  } else {
    i = -i;
  }
  *itoa_simple_helper(s, i) = '\0';
  return dest;
}
int getdisplayStr(tag_t objTag,char** sDataValue)
{
	//char *retStr=NULL;
	int iStatus=ITK_ok;
	char *itemid=NULL;
	char *revid=NULL;
	ITK_CALL(AOM_ask_value_string(objTag,"item_id",&itemid));
	ITK_CALL(AOM_ask_value_string(objTag,"item_revision_id",&revid));

	//printf("\n itemid==%s \t item_revision_id=%s",itemid,revid );
	*sDataValue=(char*)realloc(*sDataValue,tc_strlen(itemid)+tc_strlen(revid)+2);
	tc_strcpy(*sDataValue,itemid);
	tc_strcat(*sDataValue,"/");
	tc_strcat(*sDataValue,revid);
	//printf("\n **sDataValue==%s",*sDataValue);
	return iStatus;
}
int getOutputObjTag(char *inp_type,int objCnt,tag_t *objectList,tag_t *outputObj)
{
	int istatus=ITK_ok;
	char *obj_type = NULL;
	*outputObj = NULLTAG;

	if(objCnt>0)
	{
		for(int oCnt=0;oCnt<objCnt;oCnt++)
		{
			istatus = AOM_ask_value_string(objectList[oCnt],"object_type",&obj_type);
			if(istatus==ITK_ok)
			{
				//printf("\n obj_type=%s",obj_type);
				if(tc_strcmp(obj_type,inp_type)==0)
				{
					*outputObj=objectList[oCnt];
					break;
				}
			}

		}
	}

	return istatus;
}

int soSearchQuery(std::vector<const char*> QryInpVec,int *ifound,tag_t **soFoudTags)
{
		  //printf("\n inside soSearchQuery function");
			tag_t tQueryTag = NULLTAG;
			tag_t *tagArray = NULLTAG;
			int cnt = 0;
			int status = ITK_ok;

			char **entries=NULL;
			char **values=NULL;

			entries=(char**)malloc(sizeof(char*)*100);
			values=(char**)malloc(sizeof(char*)*100);
			int inpCnt=0;
			std::vector< char*> QryinpFields;

			QryinpFields.push_back("item_id_After");
			QryinpFields.push_back("item_id_Before");
			QryinpFields.push_back("Released After");
			QryinpFields.push_back("Released Before");

			for(int q=0;q<QryInpVec.size();q++)
			{

				if(QryInpVec[q]!=NULL && !(tc_strcmp(QryInpVec[q],"")==0))
				{
					entries[inpCnt]=(char*)malloc(tc_strlen(QryinpFields[q])*100);
					values[inpCnt]=(char*)malloc(tc_strlen(QryInpVec[q])*100);

					tc_strcpy(entries[inpCnt],QryinpFields[q]);
					tc_strcpy(values[inpCnt],QryInpVec[q]);

					inpCnt++;
				}
			}

			QRY_find2("SO_Search...", &tQueryTag );

			if(tQueryTag!= NULLTAG)
			{
				status = QRY_execute(tQueryTag, inpCnt, entries, values, &cnt, &tagArray);
			}
			*ifound=cnt;
			*soFoudTags=tagArray;

			printf("\n  ifound===%d",*ifound);

			return status;
}

