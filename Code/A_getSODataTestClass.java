/****************************************************************
@copyright Progneur Technology
File Name : getSOData.java
Functionality : Fetch SO Data functionality
Author : Bhavana Patil and Priyanka Tikhe
******************************************************************/
package com.teamcenter.tdps.view;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.loose.query.SavedQueryService;
import com.teamcenter.services.loose.query._2006_03.SavedQuery.ExecuteSavedQueryResponse;
import com.teamcenter.services.loose.query._2010_04.SavedQuery.FindSavedQueriesCriteriaInput;
import com.teamcenter.services.loose.query._2010_04.SavedQuery.FindSavedQueriesResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;

public class A_getSODataTestClass {
	TCSession session;
	public A_getSODataTestClass(TCSession session)
	{
		this.session=session;
	}

	public void getdata(String searchSOStr, String fromDate, String toDate, Boolean dateFilteristrue)
	{
		try {
			
			ArrayList<String> uniqueItemNmList=new ArrayList<String>();
			LinkedHashMap<TCComponentItem, TCComponentItemRevision> allTDPSSO_LatestRevList=new LinkedHashMap<TCComponentItem, TCComponentItemRevision>();
			//LinkedHashMap<String, ModelObject> AllLatestRevisionSOList=new LinkedHashMap<String, ModelObject>();
			ModelObject[] modelObjs = getObjectData(searchSOStr,fromDate,toDate,dateFilteristrue);
			for(int i=0;i<modelObjs.length;i++)
			{
				ModelObject modelobj = modelObjs[i];
				String itemStr = modelobj.getPropertyDisplayableValue("item_id");
				//System.out.println("revName=="+itemStr);
				
				TCComponent tccomp=(TCComponent)modelobj;
				Property itemobj = tccomp.getPropertyObject("items_tag");
				TCComponentItem tcCompTDPSSOItem = (TCComponentItem)itemobj.getModelObjectValue();
				//String mainitemID = tcCompTDPSSOItem.getStringProperty("item_id");
				//System.out.println("mainitemID="+mainitemID);
				
				TCComponentItemRevision tcCompTDPSSOItemLatestRev = tcCompTDPSSOItem.getLatestItemRevision();
				if(i==0)
				{
					uniqueItemNmList.add(itemStr);
					allTDPSSO_LatestRevList.put(tcCompTDPSSOItem,tcCompTDPSSOItemLatestRev);
				}else
				{
					if(!uniqueItemNmList.contains(itemStr))
					{
						uniqueItemNmList.add(itemStr);
						allTDPSSO_LatestRevList.put(tcCompTDPSSOItem,tcCompTDPSSOItemLatestRev);
					}			
				}				
			}
			System.out.println("modelObjs size="+modelObjs.length);
			System.out.println("allTDPSSO_LatestRevList size="+allTDPSSO_LatestRevList.size());
			System.out.println("uniqueItemNmList size="+uniqueItemNmList.size());
			
	
			//Data fetching object
			LinkedHashMap<String,String> excelColList=new LinkedHashMap<String,String>();
			excelColList.put("SO Numbers","SO Numbers");//SO Numbers
			excelColList.put("FERT","FERT");//FERT ID
			excelColList.put("Machine No","Machine No");//Machine No 
			excelColList.put("TDPSSORevisionMaster","tdps_remarks");//TDPSSORevisionMaster
			excelColList.put("TDPSFERTRevisionMaster","tdps_frame");//TDPSFERTRevisionMaster
			excelColList.put("TDPSSOEnggForm","tdps_electrical_engg");//TDPSSOEnggForm
			excelColList.put("TDPSAccForm","tdps_slipring_rotor");//TDPSAccForm
			excelColList.put("TDPSGenSpec","tdps_mw");//TDPSGenSpec
			
			
			
			LinkedHashMap<String, LinkedHashMap<String, String>> maindatalist = new LinkedHashMap<String, LinkedHashMap<String,String>>();
			try {	
				
				TCComponentItemRevision TDPSFERTRevision		=	null;
				TCComponentItemRevision TDPSSOItemRevision		=	null;
				TCComponentItemRevision TDPSMachineRevision		=	null;
				TCComponentForm 		TDPSSORevisionMaster	=	null;
				TCComponentForm			TDPSFERTRevisionMaster	=	null;
				TCComponentForm			TDPSSOEnggForm			=   null;
				TCComponentForm			TDPSGenSpec				=   null;
				TCComponentForm			TDPSDocForm				=   null;
				TCComponentForm			TDPSAccForm				=   null;
				TCComponentForm			TDPSContSummForm		=   null;
				//Get fert information
				for(TCComponentItem TDPSSOItem:allTDPSSO_LatestRevList.keySet())
				{
					 TDPSSOItemRevision = allTDPSSO_LatestRevList.get(TDPSSOItem);
					
					//TDPSSO data
					String so_itemid = TDPSSOItem.getPropertyDisplayableValue("item_id");
					
					//TDPSSO ITEM REVISION MASTER FORM data
					AIFComponentContext[] TDPSSOItemRevisionChildren = TDPSSOItemRevision.getChildren();
					for(int p=0;p<TDPSSOItemRevisionChildren.length;p++)
					{
						String revmasterform = (String) TDPSSOItemRevisionChildren[p].getContextDisplayName();
						if(revmasterform.equals("Item Masters"))
						{
							TDPSSORevisionMaster=(TCComponentForm)TDPSSOItemRevisionChildren[p].getComponent();
							//String fertRevID = salesOrderFol.getPropertyDisplayableValue("item_revision_id");
							break;
						}
					}
					
					
					//TDPS Fert revision data
					AIFComponentContext[] primary = TDPSSOItemRevision.getPrimary();
					//System.out.println("primary=="+primary[1]);
					for(int p=0;p<primary.length;p++)
					{
						String salesOdernm = (String) primary[p].getContextDisplayName();
						if(salesOdernm.equals("Sales Orders"))
						{
							TDPSFERTRevision=(TCComponentItemRevision)primary[p].getComponent();
							//String fertRevID = salesOrderFol.getPropertyDisplayableValue("item_revision_id");
							break;
						}
					}
					
					////TDPSFERT REVISION MASTER FORM data
					AIFComponentContext[] TDPSFERTItemRevisionChildren = TDPSSOItemRevision.getChildren();
					for(int p=0;p<TDPSFERTItemRevisionChildren.length;p++)
					{
						String revmasterform = (String) TDPSFERTItemRevisionChildren[p].getContext();
						if(revmasterform.equals("IMAN_master_form_rev"))
						{
							TDPSFERTRevisionMaster=(TCComponentForm)TDPSFERTItemRevisionChildren[p].getComponent();
							//break;
						}else if(revmasterform.equals("TDPS_Machine_Rel"))
						{
							TDPSMachineRevision=(TCComponentItemRevision)TDPSFERTItemRevisionChildren[p].getComponent();
							//break;
						}else if(revmasterform.equals("pseudo_folder"))
						{
							//System.out.println("customer requirement");
							TCComponentPseudoFolder pseudo_folder=(TCComponentPseudoFolder)TDPSFERTItemRevisionChildren[p].getComponent();
							String foldernm=pseudo_folder.getPropertyDisplayableValue("object_string");
							System.out.println("pseudo_folder==="+foldernm);
							if(foldernm.equals("Customer Requirements"))
							{
								try {

									AIFComponentContext[] pseudo_folderchildrens = pseudo_folder.getChildren();
									for(int cr=0;cr<pseudo_folderchildrens.length;cr++)
									{
										
										TCComponentForm tccompForm=(TCComponentForm)pseudo_folderchildrens[cr].getComponent();
										String fromname=tccompForm.getPropertyDisplayableValue("object_type");
										System.out.println("fromname===="+fromname);
										if(fromname.equals("TDPSSOEnggForm"))
										{
											TDPSSOEnggForm=tccompForm;
											//break;
										}else if(fromname.equals("TDPSGenSpec"))
										{
											TDPSGenSpec=tccompForm;
										//	break;
										}else if(fromname.equals("TDPSDocForm"))
										{
											TDPSDocForm=tccompForm;
											//break;
										}else if(fromname.equals("TDPSAccForm"))
										{
											TDPSAccForm=tccompForm;
											//break;
										}else if(fromname.equals("TDPSContSummForm"))
										{
											TDPSContSummForm=tccompForm;
											//break;
										}
									}
								
								} catch (ClassCastException e) {
									// TODO: handle exception
								}
								break;
							}
							//break;
						}
					}
					
					LinkedHashMap<String, String> datalist = new LinkedHashMap<String, String>();
					for(String businessObjname:excelColList.keySet())
					{
						String realName=excelColList.get(businessObjname);
						
						if(businessObjname.equals("SO Numbers"))
						{
							String formattedSoNo="";
							if(TDPSSOItem!=null && TDPSSOItemRevision!=null)
							{
								String SO_numberId = TDPSSOItem.getPropertyDisplayableValue("item_id");
								String SO_numberIdRevId = TDPSSOItemRevision.getPropertyDisplayableValue("item_revision_id");
								formattedSoNo = SO_numberId+"/"+SO_numberIdRevId;
							}
						
							datalist.put(realName, formattedSoNo);
							System.out.println(realName+"=="+formattedSoNo);
						}else if(businessObjname.equals("FERT"))
						{
							String fertID="";
							if(TDPSFERTRevision!=null)
								fertID = TDPSFERTRevision.getPropertyDisplayableValue("item_id");
						
							datalist.put(realName, fertID);
							System.out.println("Fert No=="+fertID);
							
						}else if(businessObjname.equals("Machine No"))
						{
							String dataVal="";
							if(TDPSMachineRevision!=null)
								{
								   String itemid=TDPSMachineRevision.getPropertyDisplayableValue("item_id");
								   String itemrevid=TDPSMachineRevision.getPropertyDisplayableValue("item_revision_id");
								   dataVal=itemid+"/"+itemrevid;
								}
							datalist.put(realName, dataVal);
							System.out.println(realName+"++="+dataVal);
						}else if(businessObjname.equals("TDPSSORevisionMaster"))
						{
							String dataVal="";
							if(TDPSSORevisionMaster!=null)
							      dataVal = TDPSSORevisionMaster.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
							
						}else if(businessObjname.equals("TDPSFERTRevisionMaster"))
						{
							String dataVal="";
							if(TDPSFERTRevisionMaster!=null)
							  dataVal = TDPSFERTRevisionMaster.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
										
						}else if(businessObjname.equals("TDPSSOEnggForm"))
						{
							String dataVal="";
							if(TDPSSOEnggForm!=null)
							 dataVal = TDPSSOEnggForm.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);	
						}else if(businessObjname.equals("TDPSGenSpec"))
						{
							String dataVal="";
							if(TDPSGenSpec!=null)
							 dataVal = TDPSGenSpec.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);	
						}else if(businessObjname.equals("TDPSDocForm"))
						{
							String dataVal="";
							if(TDPSDocForm!=null)
							  dataVal = TDPSDocForm.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);	
						}else if(businessObjname.equals("TDPSAccForm"))
						{
							String dataVal="";
							if(TDPSAccForm!=null)
							  dataVal = TDPSAccForm.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);	
						}else if(businessObjname.equals("TDPSContSummForm"))
						{
							String dataVal="";
							if(TDPSContSummForm!=null)
							  dataVal = TDPSContSummForm.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);	
						}	
						
						//datalist.put(realName, value);
					}
					maindatalist.put(so_itemid, datalist);

				//	maindatalist.put(SO_numberId+"_"+i, datalist);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("maindatalist=="+maindatalist);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public ModelObject[] getObjectData(String searchSOStr,String from_date, String to_date, boolean dateChkBoxITtrue) 
	{
		ModelObject[] resultObj = null;
		System.out.println("\n inside getItemData");
		SavedQueryService service = SavedQueryService.getService(session.getSoaConnection());
		
		String[] qryName = { "SO_Search..." };
		String[] qryDesc = { "" };
		
		FindSavedQueriesCriteriaInput queryObject1 = new FindSavedQueriesCriteriaInput();
		queryObject1.queryNames = qryName;
		queryObject1.queryDescs = qryDesc;
		
		FindSavedQueriesCriteriaInput queryObject[] = new FindSavedQueriesCriteriaInput[1];
		queryObject[0] = queryObject1;
		
		ModelObject[] savequery = null;
		FindSavedQueriesResponse response;
		try {
			response = service.findSavedQueries(queryObject);
			savequery = response.savedQueries;

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] entries = null;
		String[] values  = null;
		
		if(dateChkBoxITtrue)
		{
			entries = new String[]{ "ID","Released After","Released Before"};
			values = new String[]{ searchSOStr,from_date,to_date};			
		}else
		{
			entries = new String[] { "ID"};
			values = new String[] { searchSOStr};
		}

		
		ModelObject modelObj = savequery[0];
		ExecuteSavedQueryResponse result;

		try {
			result = service.executeSavedQuery(modelObj, entries, values, 100000);
			  int count = result.nFound;
			 resultObj = result.objects;
			 System.out.println("count==="+count);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObj; //got model object 
	}
	
	
}
