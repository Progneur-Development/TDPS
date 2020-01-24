package com.teamcenter.tdps.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;

import com.mysql.jdbc.PreparedStatement;

public class A_databaseOperationsTestCalss {
	
	static String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TDPSPLMDB;user=infodba;password=infodba";
	private static String insertQuery;
	
public static void main(String args[])
{
	//updateDatabase();
	//updateValuesInDatabase();
	DBConnection();
}
//DBConnection and get DB data
	static void DBConnection()
	{
				
		try {
			
			Class.forName("com.mysql.jdbc.Driver");  
			System.out.println("After class for name");
			String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=TDPSPLMDB;user=infodba;password=infodba";
			Connection con=DriverManager.getConnection(connectionUrl);
			System.out.println("After connection successfull");
			//192.168.102.243:1433
			//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sonoo","root","root"); 
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			System.out.println("After stmt");
			//ResultSet rs=stmt.executeQuery("select * from dbo.SO_ITEM_DETAILS");
			ResultSet rs=stmt.executeQuery("select * from dbo.SO_ITEM_DETAILS;");
			
			System.out.println("rs"+rs);
			
			while(rs.next())  
				System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+" "+rs.getDouble(4)+" "+rs.getDouble(5)+" "+rs.getDouble(6)+" "+rs.getInt(7)
						+" "+rs.getInt(8)+" "+rs.getDouble(9)+""+rs.getString(10)+""+rs.getString(11)+""+rs.getString(12)+""+rs.getString(13));  
				con.close();  
			
			
			/*while(rs.next())  
			System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
			con.close();  */
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
