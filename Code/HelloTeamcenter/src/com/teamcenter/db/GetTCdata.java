package com.teamcenter.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;



import com.tdp2.services.loose.oaledgerlib.GetSONumbersDataService;
import com.tdp2.services.loose.oaledgerlib._2015_07.GetSONumbersData;
import com.tdp2.services.loose.oaledgerlib._2015_07.GetSONumbersData.GetPropertyResponse;
import com.teamcenter.clientx.AppXSession;

public class GetTCdata {

	AppXSession session;

public GetTCdata(AppXSession session) {
		this.session=session;
	}

LinkedHashMap<String, LinkedHashMap<String, String>> callSOA(String soNumberFromStr, String soNumberToStr,String from_date, String to_date, boolean dateChkBoxITtrue2)
	{
		LinkedHashMap<String, LinkedHashMap<String, String>> allDataList=new LinkedHashMap<String, LinkedHashMap<String,String>>();
		try{
			//readPropertyFile("F:/Progneur_Work/TDPSDataFetchingPerformanceEnchancement/PropertyFile/columnNames.properties");
			// TODO Auto-generated method stub
		    //  Map<String,String> allDataList=new LinkedHashMap<String, String>();
		      
		     String[] QryInpVec=new String[4];
		     QryInpVec[0]=soNumberFromStr;
		 	 QryInpVec[1]=soNumberToStr;
		     if(!dateChkBoxITtrue2)
		     {
		    	 QryInpVec[2]="";
		    	 QryInpVec[3]="";
		     }else
		     {
		    	 QryInpVec[2]=from_date;
		    	 QryInpVec[3]=to_date;
		     }
			 
		     try {

		    		//GetSONumbersData service = GetSONumbersDataService.getService(session.getSoaConnection());
		    	 GetSONumbersData service = GetSONumbersDataService.getService(session.getConnection());
		    		GetPropertyResponse response = service.getSOnumbersObjProperties(QryInpVec);
		    		if(response!=null)
		    		{
		    			Map<String, String>	map=response.propertyResp;
		    			Set <Entry<String, String>>set=map.entrySet();
		    			Iterator<Entry<String, String>>itr=set.iterator();
		    			//TableColumn[] columns = report_table.getColumns();
		    			while(itr.hasNext())
		    			{
		    				Entry<String, String>entry=itr.next();
		    			//	System.out.println(entry.getKey()+"=====> "+entry.getValue());
		    				
		    				if(!entry.getValue().equals(""))
		    				{
		    					String sodata=entry.getValue();
		    						
		    					String data[]=sodata.split("\\|\\^\\|");
		    					String sonumber = null;
		    					if(data.length>=0)
		    					{
		    						LinkedHashMap<String, String> innerList=new LinkedHashMap<String, String>();
		    						for(int i=0;i<data.length;i++)
		    						{
		    							System.out.println("data[i] ="+data[i]);
		    							if(data[i].contains("|*|"))
		    							{
		    								String relname = data[i].split("\\|\\*\\|")[0];
		    								if(relname.equals("SO Numbers"))
		    									sonumber=data[i].split("\\|\\*\\|")[1];
		    							
		    								String dataValue = data[i].split("\\|\\*\\|")[1];
		    								if(dataValue.equals("BLANK_VALUE"))
		    									innerList.put(relname, "");
		    								else
		    									innerList.put(relname, dataValue);
		    								
		    													
		    							}
		    							
		    						}
		    						allDataList.put(sonumber, innerList);
		    					}
		    						
		    				}
		    			}
		    		}
		    	 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			GetSONumbersData obj=session.gets
			//obj.getSOnumbersObjProperties(data);
			
			System.out.println("allDataList="+allDataList);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return allDataList;
		
	}
	/*LinkedHashMap<String, String> readPropertyFile(String file) {

		LinkedHashMap<String, String> hashlist = new LinkedHashMap<String, String>();
		
		// Read Property file
		try (InputStream input = new FileInputStream(file)) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);
       		Set<Object> objects = prop.keySet();
       		ArrayList<Integer> objectsKeys=new ArrayList<Integer>();
       		for(Object obj:objects)
       		{
       			if(obj!=null)
       			{
       			    String dd=(String)obj;
       			    if(dd.length()>0)
       			    	objectsKeys.add(Integer.parseInt(dd));
       			}
       		}
       		Collections.sort(objectsKeys);
       		int i=0;
       		for(Object obj:objects)
			{
				String value=(String)prop.get(obj);
				if(value!=null && value.trim().length()>0)
				{
					String realName=value.split(":")[1];
					String displayName=value.split(":")[2];
					hashlist.put(realName);	
					i++;
				}
				
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	
		return hashlist;
	}*/
}
