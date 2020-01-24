/****************************************************************
@copyright Progneur Technology
File Name : TC_Data_Operations.java
Functionality : TC data fetching and traversing and file reading writing functionality
Author : Bhavana Patil and Priyanka Tikhe
******************************************************************/
package com.teamcenter.tdps.view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.loose.query.SavedQueryService;
import com.teamcenter.services.loose.query._2006_03.SavedQuery.ExecuteSavedQueryResponse;
import com.teamcenter.services.loose.query._2010_04.SavedQuery.FindSavedQueriesCriteriaInput;
import com.teamcenter.services.loose.query._2010_04.SavedQuery.FindSavedQueriesResponse;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;


public class TC_Data_Operations {
	
	TCSession session;
	
	public TC_Data_Operations(TCSession session) {
		this.session=session;
	}	
	public ModelObject[] getObjectData(LinkedHashMap<String, String> inputCriteriaList, boolean dateChkBoxITtrue) 
	{
		ModelObject[] resultObj = null;
		//System.out.println("\n inside getItemData");
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
		
		ArrayList<String> valueList=new ArrayList<String>();
		ArrayList<String> entryList=new ArrayList<String>();
				
	
		if(inputCriteriaList!=null && inputCriteriaList.size()>0)
		{
			for(String key:inputCriteriaList.keySet())
			{
				if(inputCriteriaList.get(key)!=null && !inputCriteriaList.get(key).equals(""))
				{
					entryList.add(key);
					valueList.add(inputCriteriaList.get(key));
				}
			}
			
			String[] entries  = new String[entryList.size()];
			String[] values  = new String[entryList.size()];
			
			for(int e=0;e<entryList.size();e++)
				entries[e]=entryList.get(e);
			
			for(int v=0;v<valueList.size();v++)
				values[v]=valueList.get(v);
			
			ModelObject modelObj = savequery[0];
			ExecuteSavedQueryResponse result;

			try {
				result = service.executeSavedQuery(modelObj, entries, values, 10);
				  int count = result.nFound;
				 resultObj = result.objects;
				 System.out.println("count==="+count);

			} catch (Exception e) {
				e.printStackTrace();
			}
			 		
		}
		
		
		return resultObj; //got model object 
	} 
	//public void getdatadatalist(string searchsostr, string fromdate, string todate, boolean datefilteristrue)
	LinkedHashMap<String, LinkedHashMap<String,String>> getDataList(LinkedHashMap<String, LinkedHashMap<String, String>> colNameList, ModelObject[] modelObjs)
	{
		LinkedHashMap<String, LinkedHashMap<String, String>> maindatalist = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		try {
			
			ArrayList<String> uniqueItemNmList=new ArrayList<String>();
			LinkedHashMap<TCComponentItem, TCComponentItemRevision> allTDPSSO_LatestRevList=new LinkedHashMap<TCComponentItem, TCComponentItemRevision>();
			//LinkedHashMap<String, ModelObject> AllLatestRevisionSOList=new LinkedHashMap<String, ModelObject>();
			//ModelObject[] modelObjs = getObjectData(searchSOStr,fromDate,toDate,dateFilteristrue);
			for(int i=0;i<modelObjs.length;i++)
			{
				ModelObject modelobj = modelObjs[i];
				String itemStr = modelobj.getPropertyDisplayableValue("item_id");
				//System.out.println("revName=="+itemStr);
				
				TCComponent tccomp=(TCComponent)modelobj;
				Property itemobj = tccomp.getPropertyObject("items_tag");
				TCComponentItem tcCompTDPSSOItem = (TCComponentItem)itemobj.getModelObjectValue();
			//	String mainitemID = tcCompTDPSSOItem.getStringProperty("item_id");
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
			
				
			
			try {	
				
				TCComponentForm			TDPSGenSpec				=   null;
				TCComponentForm			TDPSDocForm				=   null;
				TCComponentForm			TDPSAccForm				=   null;
				TCComponentForm			TDPSContSummForm		=   null;
				TCComponentForm			TDPSSOEnggForm			=   null;
				TCComponentForm 		TDPSSORevisionMaster	=	null;
				TCComponentForm			TDPSFERTRevisionMaster	=	null;
				TCComponentForm			TDPSMachineRevisionMaster = null;
				TCComponentItemRevision TDPSFERTRevision		=	null;
				TCComponentItemRevision TDPSSOItemRevision		=	null;
				TCComponentItemRevision TDPSMachineRevision		=	null;
				
				
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
						String fertchild = (String) TDPSFERTItemRevisionChildren[p].getContext();
						if(fertchild.equals("IMAN_master_form_rev"))
						{
							TDPSFERTRevisionMaster=(TCComponentForm)TDPSFERTItemRevisionChildren[p].getComponent();
							//break;
						}else if(fertchild.equals("TDPS_Machine_Rel"))
						{
							TDPSMachineRevision=(TCComponentItemRevision)TDPSFERTItemRevisionChildren[p].getComponent();
							AIFComponentContext[] machineChildren = TDPSMachineRevision.getChildren();
							for(int m=0;m<machineChildren.length;m++)
							{
								String machinechild = (String) machineChildren[m].getContext();
								if(machinechild.equals("IMAN_master_form_rev"))
								{
									TDPSMachineRevisionMaster=(TCComponentForm)machineChildren[m].getComponent();
									break;
								}
								
							}
						}else if(fertchild.equals("pseudo_folder"))
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
					for(String key:colNameList.keySet())
					{
						String businessObjname=colNameList.get(key).get("BUSINESS_OBJ_NAME");
						String realName=colNameList.get(key).get("PROP_REAL_NAME");
						
						
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
						}else if(businessObjname.equals("TDPSSOItemRevision"))
						{
							String dataVal="";
							if(TDPSSOItemRevision!=null)
							  dataVal = TDPSSOItemRevision.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
										
						}
						else if(businessObjname.equals("TDPSFERTRevision"))
						{
							String dataVal="";
							if(TDPSFERTRevision!=null)
							  dataVal = TDPSFERTRevision.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
										
						}else if(businessObjname.equals("TDPSMachineRevision"))
						{
							String dataVal="";
							if(TDPSMachineRevision!=null)
							  dataVal = TDPSMachineRevision.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
										
						}else if(businessObjname.equals("TDPSMachineRevisionMaster"))
						{
							String dataVal="";
							if(TDPSMachineRevisionMaster!=null)
							  dataVal = TDPSMachineRevisionMaster.getPropertyDisplayableValue(realName);
							datalist.put(realName, dataVal);
							System.out.println(realName+"=="+dataVal);
										
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
	   return maindatalist;	
	}

	
	String[] getBusinessObjList() 
	{
		String[] businessObjList = null;//= { "Item","Dataset","Form","Change","Item Element","Design","Workflow Process","Company","Schedule","Document" };
		ArrayList<String> arrList=new ArrayList<String>();
		   try
		   {			   
			  TCComponentItemType tCComponentType = (TCComponentItemType) session.getTypeComponent("Item");	
			  		  
			 String values[] = tCComponentType.getTypeNames();
			 
			 for(int j=0;j<values.length;j++)
			 {
				 arrList.add(values[j]);
				 TCComponentItemType typeObj = (TCComponentItemType) session.getTypeComponent(values[j]);
				  System.out.println("typeObj=="+typeObj);
				 List<String> children = typeObj.getChildTypeNames();
				 
				   for(int i=0;i<children.size();i++)
				   {
					   arrList.add(children.get(i));
				   }
					 
			 }
			 businessObjList=new String[arrList.size()]; 
			 for(int k=0;k<arrList.size();k++)
				 businessObjList[k]=arrList.get(k);			
			// businessObjList = (String[]) arrList.toArray();
			   
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
			return businessObjList;
		}
	
	public String[] getBusinessObjTypeProperties(String selBObjecttype) {		
		String[] propertyArr=null;
		try {			
			TCComponentType tt = session.getTypeComponent(selBObjecttype);
						
			propertyArr= tt.getPropertyNames();
					} catch (Exception e) {
			e.printStackTrace();
		}
		return propertyArr;	
	}
	
	public LinkedHashMap<String,LinkedHashMap<String,String>> getPreferenceData(String prefName,String mode)
	{
		
		
		String filename;
		if(mode.equals("LEFT_TABLE"))
			filename="LeftColumn_config.properties";
		else if(mode.equals("RIGHT_TABLE"))
			filename="RightColumn_config.properties";
		else
			filename="RightColumn_config.properties";
			
		
		 LinkedHashMap<String,LinkedHashMap<String,String>> hashlist=new  LinkedHashMap<String,LinkedHashMap<String,String>>();
		try {
			LinkedHashMap<String, String> list1=new LinkedHashMap<String, String>();
			//checkfile exists of not
			File file=null;
					
			  TCPreferenceService preferService=session.getPreferenceService();
			  String[] values=preferService.getStringValues(prefName);

			  System.out.println("values length=="+values.length);
			  System.out.println("values=="+values);
				for(int i=0;i<values.length;i++)
				{				
					System.out.println(values[i]);
					if(values[i]!=null && values[i].length()>0)
					{
						LinkedHashMap<String,String> innerList=new  LinkedHashMap<String,String>();
					 String businessObj = values[i].split(":")[0];
					 String propRealName = values[i].split(":")[1].split("/")[0];
					 String propDisplayName = values[i].split(":")[1].split("/")[1];
					 innerList.put("BUSINESS_OBJ_NAME",businessObj);
					 innerList.put("PROP_REAL_NAME",propRealName);
					  innerList.put("PROP_DISPLAY_NAME",propDisplayName);
					  hashlist.put(businessObj+"_"+i, innerList);	
										
					}
					System.out.println(hashlist);
				}
			
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return hashlist;
	}
	public static File createTDPSColumnManagFiles(String filename) {
		File folderpath = null;
		File file =null;
		try {
			String usersTempFolderPath = System.getProperty("user.home")
					+ File.separator + "TDPS_COLUMNS";
					//+ "LeftColumn_config.properties";
			
			folderpath = new File(usersTempFolderPath);
			if (!folderpath.exists()) 
				folderpath.mkdir();
			
			String filepath=usersTempFolderPath + File.separator+filename;
			file =new File(filepath);
			if (!file.exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;

	}
	
	public static void writeSOAInputTextFile(LinkedHashMap<String, LinkedHashMap<String, String>> colNameListT) {
		try {
			File folderpath = null;
			File file =null;
			String filepath = null;
			try {
				String TempFolderPath = "C:/Temp/"+"TDPS_COLUMNS";
						//+ "LeftColumn_config.properties";
				
				folderpath = new File(TempFolderPath);
				if (!folderpath.exists()) 
					folderpath.mkdir();
				
				 filepath=TempFolderPath + File.separator+"RightTablePropertiesSOAInpFile.txt";
				file =new File(filepath);
				if (!file.exists()) {
					file.getParentFile().mkdir();
					file.createNewFile();
				}
//				}else
//				{
//					//file.deleteOnExit();
//					//file.createNewFile();
//				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintWriter writer = new PrintWriter(filepath, "UTF-8");
			
			//writer.println("The second line");
			
			int cnt=0;
			System.out.println("colNameListT.size()=="+colNameListT.size());
			if(colNameListT.size()>0)
			{
				for(String key:colNameListT.keySet())
				{
					String PROP_DISPLAY_NAME = colNameListT.get(key).get("PROP_DISPLAY_NAME").trim();
					String BUSINESS_OBJ_NAME = colNameListT.get(key).get("BUSINESS_OBJ_NAME").trim();
					String PROP_REAL_NAME = colNameListT.get(key).get("PROP_REAL_NAME").trim();
					String OBJECT_DATA_TYPE = colNameListT.get(key).get("OBJECT_DATA_TYPE").trim();
					
					
					String inStr=BUSINESS_OBJ_NAME+"="+PROP_REAL_NAME+"="+OBJECT_DATA_TYPE+"="+OBJECT_DATA_TYPE;
					//inStr = inStr.replaceAll("[^\\p{ASCII}]", "");
					
					writer.println(inStr);
					System.out.println("cnt=="+cnt);
					cnt++;
					
				}
			}
			//This line is added to store release date column for data fetching while soa call \\bhavana 16-1-2020
			String date_datecol="TDPSSOItemRevision=date_released=date=date";
			writer.println(date_datecol);
			
			writer.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	 
	    private static String cleanTextContent(String text) 
	    {
	        // strips off all non-ASCII characters
	        text = text.replaceAll("[^\\x00-\\x7F]", "");
	 
	        // erases all the ASCII control characters
	        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
	         
	        // removes non-printable characters from Unicode
	        text = text.replaceAll("\\p{C}", "");
	 
	        return text.trim();
	    }
	public static void writePropertyFile(Table table,String mode) {
			
		String filename=null;
		if(mode.equals("LEFT_TABLE"))
			filename="LeftColumn_config.properties";
		else if(mode.equals("RIGHT_TABLE"))
			filename="RightColumn_config.properties";
		else
			filename="RightColumn_config.properties";
		
		File file = createTDPSColumnManagFiles(filename);
		
		if(table.getItemCount()>0)
		{
			try (OutputStream output = new FileOutputStream(file)) {

				Properties prop = new Properties();
					
				TableItem[] items=table.getItems();
				for(int i=0;i<items.length;i++)
				{
					String PROP_DISPLAY_NAME=items[i].getText(0);
					String BUSINESS_OBJ_NAME=items[i].getText(1);
					String PROP_REAL_NAME=(String) items[i].getData("PROP_REAL_NAME");
					prop.setProperty(BUSINESS_OBJ_NAME+"_"+i, PROP_DISPLAY_NAME+","+BUSINESS_OBJ_NAME+","+PROP_REAL_NAME);
				}
			
				// save properties to project root folder
				prop.store(output, "Store Property data");

				System.out.println(prop);

			} catch (IOException io) {
				io.printStackTrace();
			}
		}

	}
	
	LinkedHashMap<String, LinkedHashMap<String, String>> readPropertyFile(String file) {

		LinkedHashMap<String, LinkedHashMap<String, String>> hashlist = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		
		// Read Property file
		try (InputStream input = new FileInputStream(file)) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);
       		Set<Object> objects = prop.keySet();
       		/*ArrayList<Integer> objectsKeys=new ArrayList<Integer>();
       		for(Object obj:objects)
       		{
       			if(obj!=null)
       			{
       			    String dd=(String)obj;
       			    if(dd.length()>0)
       			    	objectsKeys.add(Integer.parseInt(dd));
       			}
       		}
       		Collections.sort(objectsKeys);*/
       		int i=0;
       		for(Object obj:objects)
			{
				String value=(String)prop.get(obj);
				if(value!=null && value.trim().length()>0)
				{
					LinkedHashMap<String, String> innerList = new LinkedHashMap<String, String>();
					innerList.put("BUSINESS_OBJ_NAME",value.split(":")[0]);
					innerList.put("PROP_DISPLAY_NAME",value.split(":")[2]);
					innerList.put("PROP_REAL_NAME",value.split(":")[1]);
					innerList.put("OBJECT_DATA_TYPE",value.split(":")[3]);
			
					hashlist.put(value.split(":")[0]+"_"+i, innerList);	
					i++;
				}
				
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	
		return hashlist;
	}

	LinkedHashMap<String,LinkedHashMap<String,String>> getRightTablePropertyValuList()
	 {
	
		
		int i=0;
		LinkedHashMap<String, LinkedHashMap<String, String>> hashList = new LinkedHashMap<String, LinkedHashMap<String,String>>();	
		LinkedHashMap<String, String> list0 = new LinkedHashMap<String, String>();
		list0.put("PROP_DISPLAY_NAME","SO #");
		list0.put("BUSINESS_OBJ_NAME","SO Numbers");
		list0.put("PROP_REAL_NAME","SO Numbers");
		list0.put("OBJECT_DATA_TYPE","string");
		hashList.put("SO Numbers"+(i++), list0);
		
		LinkedHashMap<String, String> list1 = new LinkedHashMap<String, String>();
		list1.put("PROP_DISPLAY_NAME","FERT #");
		list1.put("BUSINESS_OBJ_NAME","FERT");
		list1.put("PROP_REAL_NAME","FERT");
		list1.put("OBJECT_DATA_TYPE","string");
		hashList.put("FERT"+(i++), list1);
		
		LinkedHashMap<String, String> list2 = new LinkedHashMap<String, String>();
		list2.put("PROP_DISPLAY_NAME","Frame");
		list2.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list2.put("PROP_REAL_NAME","tdps_frame");
		list2.put("OBJECT_DATA_TYPE","string");
		hashList.put("TDPSGenSpec"+(i++), list2);
		
		LinkedHashMap<String, String> list3 = new LinkedHashMap<String, String>();
		list3.put("PROP_DISPLAY_NAME","MVA");
		list3.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list3.put("PROP_REAL_NAME","tdps_mva");
		list3.put("OBJECT_DATA_TYPE","double");
		hashList.put("TDPSGenSpec"+(i++), list3);
		
		LinkedHashMap<String, String> list4 = new LinkedHashMap<String, String>();
		list4.put("PROP_DISPLAY_NAME","Rating (MW)");
		list4.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list4.put("PROP_REAL_NAME","tdps_mw");
		list4.put("OBJECT_DATA_TYPE","double");
		hashList.put("TDPSGenSpec"+(i++), list4);
		
		LinkedHashMap<String, String> list5= new LinkedHashMap<String, String>();
		list5.put("PROP_DISPLAY_NAME","Voltage (in KV)");
		list5.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list5.put("PROP_REAL_NAME","tdps_voltage");
		list5.put("OBJECT_DATA_TYPE","double");
		hashList.put("TDPSGenSpec"+(i++), list5);
		
		LinkedHashMap<String, String> list6= new LinkedHashMap<String, String>();
		list6.put("PROP_DISPLAY_NAME","No. Of Poles");
		list6.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list6.put("PROP_REAL_NAME","tdps_no_of_poles");
		list6.put("OBJECT_DATA_TYPE","integer");
		hashList.put("TDPSGenSpec"+(i++), list6);
		
		LinkedHashMap<String, String> list7= new LinkedHashMap<String, String>();
		list7.put("PROP_DISPLAY_NAME","Frequency (Hz)");
		list7.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list7.put("PROP_REAL_NAME","tdps_frequency");	
		list7.put("OBJECT_DATA_TYPE","integer");
		hashList.put("TDPSGenSpec"+(i++), list7);
		
		LinkedHashMap<String, String> list8= new LinkedHashMap<String, String>();
		list8.put("PROP_DISPLAY_NAME","PF");
		list8.put("BUSINESS_OBJ_NAME","TDPSGenSpec");
		list8.put("PROP_REAL_NAME","tdps_pf");	
		list8.put("OBJECT_DATA_TYPE","double");
		hashList.put("TDPSGenSpec"+(i++), list8);

		LinkedHashMap<String, String> list9= new LinkedHashMap<String, String>();
		list9.put("PROP_DISPLAY_NAME","Customer Name");
		list9.put("BUSINESS_OBJ_NAME","TDPSSORevisionMaster");
		list9.put("PROP_REAL_NAME","tdps_customer_name");
		list9.put("OBJECT_DATA_TYPE","string");
		hashList.put("TDPSSORevisionMaster"+(i++), list9);
		
		LinkedHashMap<String, String> list10= new LinkedHashMap<String, String>();
		list10.put("PROP_DISPLAY_NAME","End User");
		list10.put("BUSINESS_OBJ_NAME","TDPSSORevisionMaster");
		list10.put("PROP_REAL_NAME","tdps_end_user");	
		list10.put("OBJECT_DATA_TYPE","string");
		hashList.put("TDPSSORevisionMaster"+(i++), list10);
		
		LinkedHashMap<String, String> list11= new LinkedHashMap<String, String>();
		list11.put("PROP_DISPLAY_NAME","Country Installation");
		list11.put("BUSINESS_OBJ_NAME","TDPSSORevisionMaster");
		list11.put("PROP_REAL_NAME","tdps_country_installation");	
		list11.put("OBJECT_DATA_TYPE","string");
		hashList.put("TDPSSORevisionMaster"+(i++), list11);
		
		LinkedHashMap<String, String> list12= new LinkedHashMap<String, String>();
		list12.put("PROP_DISPLAY_NAME","Dispatch Date");
		list12.put("BUSINESS_OBJ_NAME","TDPSMachineRevisionMaster");
		list12.put("PROP_REAL_NAME","tdps_dispatch_date");	
		list12.put("OBJECT_DATA_TYPE","date");
		hashList.put("TDPSMachineRevisionMaster"+(i++), list12);
		
		/*LinkedHashMap<String, String> list13= new LinkedHashMap<String, String>();
		list13.put("PROP_DISPLAY_NAME","Date Released");
		list13.put("BUSINESS_OBJ_NAME","TDPSSORevision");
		list13.put("PROP_REAL_NAME","date_released");	
		list13.put("OBJECT_DATA_TYPE","date");
		hashList.put("TDPSSORevision"+(i++), list13);*/
		return hashList;
	 }
}