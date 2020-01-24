package com.teamcenter.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;

import com.mysql.jdbc.PreparedStatement;

public class DBOperations {
	
	static String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TDPSPLMDB;user=infodba;password=infodba";
	private static String insertQuery;
	
public static void main(String args[])
{
	//updateDatabase();
	//updateValuesInDatabase();
	//populateDBTableInUI();
}

	//update values in DBTable success
	public static void updateValuesInDatabase(LinkedHashMap<String, LinkedHashMap<String, String>> alldataList) 
	{
		try {
			String SO_Numbers = "SO_Numbers";
			String SO_Numbers_val ="";
			String FERT = "FERT";
			String FERT_val =""; 
			String tdps_frame = "tdps_frame";
			String tdps_frame_val =""; 
			String tdps_mva = "tdps_mva";
			String tdps_mva_val ="0.0"; 
			String tdps_mw = "tdps_mw";
			String tdps_mw_val ="0.0"; 
			String tdps_voltage = "tdps_voltage";
			String tdps_voltage_val ="0.0"; 
			String tdps_no_of_poles = "tdps_no_of_poles";
			String tdps_no_of_poles_val ="0"; 
			String tdps_frequency = "tdps_frequency";
			String tdps_frequency_val ="0"; 
			String tdps_pf = "tdps_pf";
			String tdps_pf_val ="0.0"; 
			String tdps_end_user = "tdps_end_user";
			String tdps_end_user_val =""; 
			String tdps_customer_name = "tdps_customer_name";
			String tdps_customer_name_val =""; 
			String tdps_country_installation = "tdps_country_installation";
			String tdps_country_installation_val =""; 
			String tdps_dispatch_date = "tdps_dispatch_date";
			String tdps_dispatch_date_val =""; 
			String date_released = "date_released";
			String date_released_val =null; 

         
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");  
				Connection conn=DriverManager.getConnection(connectionUrl);
				Statement	stmt = conn.createStatement();
				
				//Delete table vlues
				 System.out.println("Deleting table in given database...");
			     String deleteTabValQuery = "DELETE FROM dbo.SO_ITEM_DETAILS;";
			     stmt.executeUpdate(deleteTabValQuery);
			   //  System.out.println("Table  values are deleted in given database...");
			     
			     if(stmt!=null)
			    	 stmt.close();
			     
			     if(conn!=null)
			         conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(String So_key:alldataList.keySet())
			{
				LinkedHashMap<String, String> so_row = alldataList.get(So_key);
				
			
				SO_Numbers_val=stringValidation(so_row.get("SO Numbers"));
				FERT_val=stringValidation(so_row.get(FERT));
				tdps_frame_val=stringValidation(so_row.get(tdps_frame));
				tdps_mva_val=doubleValidation(so_row.get(tdps_mva));
				tdps_mw_val=doubleValidation(so_row.get(tdps_mw));
				tdps_voltage_val=doubleValidation(so_row.get(tdps_voltage));
				tdps_no_of_poles_val=integerValidation(so_row.get(tdps_no_of_poles));
				tdps_frequency_val=integerValidation(so_row.get(tdps_frequency));
				tdps_pf_val=doubleValidation(so_row.get(tdps_pf));
				tdps_end_user_val=stringValidation(so_row.get(tdps_end_user));
				tdps_customer_name_val=stringValidation(so_row.get(tdps_customer_name));
				tdps_country_installation_val=stringValidation(so_row.get(tdps_country_installation));
				tdps_dispatch_date_val=stringValidation(so_row.get(tdps_dispatch_date));
				date_released_val=dateValidation(so_row.get(date_released));
				
				
				//correct data
				insertQuery="INSERT INTO dbo.SO_ITEM_DETAILS ("+SO_Numbers+","+FERT+","+tdps_frame+","+tdps_mva+","+tdps_mw+","+tdps_voltage+","
						+ ""+tdps_no_of_poles+","+tdps_frequency+","+tdps_pf+","+tdps_end_user+","+tdps_customer_name+
						","+tdps_country_installation+","+tdps_dispatch_date+","+date_released+")"
						+ " values('"+SO_Numbers_val+"','"+FERT_val+"','"+tdps_frame_val+"','"+tdps_mva_val+"',"+tdps_mw_val+","+tdps_voltage_val+","
						+tdps_no_of_poles_val+","+tdps_frequency_val+","+tdps_pf_val+",'"+tdps_end_user_val+"','"+tdps_customer_name_val+"','"+tdps_country_installation_val+"','"+tdps_dispatch_date_val+"',"+date_released_val+")";
				 System.out.println("insertQuery=="+insertQuery);
				//Insert query
				try 
				{
					  
					Connection conn_ins=DriverManager.getConnection(connectionUrl);
					Statement	stmt_ins = conn_ins.createStatement();
			
					//Execute database base value insert query 
					 stmt_ins.execute(insertQuery);
					
				     
				     if(stmt_ins!=null)
				    	 stmt_ins.close();
				     
				     if(conn_ins!=null)
				         conn_ins.close();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
			System.out.println("insertQuery==="+insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	private static String stringValidation(String data) 
	{
		String validData="";
		try
		{
			if(data!=null && data.length()>0)
				validData=data;
			
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		return validData;
	}
	private static String dateValidation(String data) 
	{
		String validData=null;
		try
		{
			if(data!=null && data.length()>0)
				validData="'"+data+"'";
			else
				validData=null;
			
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		return validData;
	}
	private static String doubleValidation(String data) 
	{
		String validData="0.0";
		try
		{
			if(data!=null && data.length()>0)
				validData=data;
			
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		return validData;
	}
	private static String integerValidation(String data) 
	{
		String validData="0";
		try
		{
			if(data!=null && data.length()>0)
				validData=data;
			
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		return validData;
	}
	
	//populate database table using input quary
	/*public static void populateDBTableInUI() 
	{
		try {
			//Database connection
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(connectionUrl);
			System.out.println("GOt connected");
			
	Statement	stmt = con.createStatement();
	//ResultSet rs=stmt.executeQuery("select * from dbo.SO_ITEM_DETAILS;");
	
	 stmt.execute("INSERT INTO dbo.SO_ITEM_DETAILS (tdps_frame,FERT) "
            + "VALUES ('abcd','qwer')");
	
	stmt.execute("INSERT INTO dbo.SO_ITEM_DETAILS (FERT,tdps_frame,tdps_mva,tdps_mw,tdps_voltage,tdps_no_of_poles,tdps_frequency,tdps_pf,tdps_end_user,tdps_customer_name,tdps_country_installation,tdps_dispatch_date,date_released) values('2222','testpart',11.50,11.20,1.6000,14,10,1.8000,'INDIA CONTROLS','Powerica Limited','India','01-Dec-2007 12:00','21-Dec-2007 12:00')");
	//stmt.execute("INSERT INTO dbo.SO_ITEM_DETAILS ()");
	ResultSet rs=stmt.executeQuery("select * from dbo.SO_ITEM_DETAILS;");
	
	
	int data = rs.getFetchSize();
	System.out.println("data "+data);
	
	 if(stmt != null)
	 {
		 System.out.println("Updated Values In Database"+stmt);
	 }
			} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/

	  LinkedHashMap<String, LinkedHashMap<String, String>> readColumnHeaderFile()
	  {
		 LinkedHashMap<String, LinkedHashMap<String, String>> valuHashMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		 try
		 {
			 File file = new File("C:\\Temp\\TDPS_COLUMNS\\RightTablePropertiesSOAInpFile.txt"); 
			  BufferedReader br = new BufferedReader(new FileReader(file)); 
			  String sLine; 
			  int lintCnt=0;
			 
			  while ((sLine = br.readLine()) != null) 
			  { 
				  if(sLine!=null && sLine.trim().length()>0)
					{
						LinkedHashMap<String, String> innerList = new LinkedHashMap<String, String>();
						innerList.put("BUSINESS_OBJ_NAME",sLine.split("=")[0]);
						innerList.put("PROP_REAL_NAME",sLine.split("=")[1]);
						innerList.put("OBJECT_DATA_TYPE",sLine.split("=")[2]);
				
						valuHashMap .put(sLine.split("=")[0]+"_"+lintCnt, innerList);	
						lintCnt++;
					}
			  }
			  System.out.println("valuHashMap=="+valuHashMap);
			 
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
				 
		 
		return valuHashMap;
		   
	  }
}
