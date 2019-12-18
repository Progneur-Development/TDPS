/****************************************************************
@copyright Progneur Technology
File Name : TDPS_PropertySelectUI.java
Functionality : This file contains list of available properties and default selected properties
                Row up down and right left traversing functionality
Author : Bhavana Patil and Priyanka Tikhe
******************************************************************/
package com.teamcenter.tdps.view;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.tdps.view.TC_Data_Operations;
import java.util.*;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;
	public class TDPS_PropertySelectUI {
	protected static TableColumn TableColumn_ExportProperties = null;
	private Table table_prop_left;
	private Table table_prop_right;
	private Button Button_RightSideArrow;
	private Button Button_LeftSideArrow;
	private Button btnNext;
	TCSession session;
	private Shell shell_TDPS_prop;
	private Button btnCancel;
	private TC_Data_Operations obj;
	private TableColumn left_table_prop_displayName;
	private TableColumn left_table_prop_busiObj;
	private TableColumn right_table_prop_displayName;
	private TableColumn right_table_prop_busiObj;
	private Button upButton;
	private Button downButton;
	LinkedHashMap<String, String> inputCriteriaList;
	private Button btnBack;
	boolean dateChkBoxITtrue;
	private Table tableForSort;
	private Label lblNewLabel;
	
	
	public TDPS_PropertySelectUI(TCSession session,Shell shlTdpsReportfillCriteria, LinkedHashMap<String, String> inputCriteriaList) 
	{
		this.session=session;
		shlTdpsReportfillCriteria.close();
		shell_TDPS_prop=shlTdpsReportfillCriteria;
	    this.inputCriteriaList=inputCriteriaList;
		obj=new TC_Data_Operations(session);
	}
	/**
	 * @param tableStorage 
	 * @param dateChkBoxITtrue 
	 * @wbp.parser.entryPoint
	 */
		public void propertyDisplayDialog(ArrayList<Object> tableStorage, boolean dateChkBoxITtrue) {
	    //Display display = new Display();
			
			this.dateChkBoxITtrue=dateChkBoxITtrue;
			if(shell_TDPS_prop!=null)
				shell_TDPS_prop.dispose();
	    shell_TDPS_prop = new Shell();
	   // shell_TDPS_prop.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    shell_TDPS_prop.setText("TDPS Column Management: Property Dsplay UI");
	    shell_TDPS_prop.setSize(648, 650);
	    shell_TDPS_prop.setLayout(new GridLayout(1, false));
	    
	     Composite composite_main = new Composite(shell_TDPS_prop, SWT.NONE);
	    // composite_main.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_main.setToolTipText("Fill Criteria");
	    composite_main.setLayout(new GridLayout(1, false));
	    GridData gd_composite_main = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_composite_main.heightHint = 224;
	    composite_main.setLayoutData(gd_composite_main);
		  GridData gd_dateTime_fromTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_dateTime_fromTime.widthHint = 72;
	    	
	    Composite composite_PropMain = new Composite(composite_main, SWT.BORDER);
	   // composite_PropMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    GridLayout gl_composite_PropMain = new GridLayout(4, false);
	    composite_PropMain.setLayout(gl_composite_PropMain);
	    GridData gd_composite_PropMain = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_composite_PropMain.heightHint = 272;
	    composite_PropMain.setLayoutData(gd_composite_PropMain);
	    
	    Composite composite_Properties = new Composite(composite_PropMain, SWT.NONE);
	    //composite_Properties.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_Properties.setForeground(SWTResourceManager.getColor(0, 0, 0));
	    composite_Properties.setLayout(new GridLayout(1, false));
	    GridData gd_composite_Properties = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_composite_Properties.widthHint = 250;
	    composite_Properties.setLayoutData(gd_composite_Properties);

	    Label leftTabLebel=new Label(composite_Properties,SWT.NONE);
	    //leftTabLebel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    leftTabLebel.setText("Available Properties:");
	    
	    table_prop_left = new Table(composite_Properties, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI);
	    //table_prop_left.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    table_prop_left.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
	     GridData gd_table_properties = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_table_properties.widthHint = 175;
	    table_prop_left.setLayoutData(gd_table_properties);
	    table_prop_left.setHeaderVisible(true);
	    table_prop_left.setLinesVisible(true);
	     
	     left_table_prop_displayName = new TableColumn(table_prop_left, SWT.BOLD);
	    left_table_prop_displayName.setWidth(250);
	    left_table_prop_displayName.setText("Display name");
	    
	    left_table_prop_displayName.setData(new Comparator<TableItem>() {
	        @Override
	        public int compare(TableItem t1, TableItem t2) {
	            return t1.getText(0).compareTo(t2.getText(0));
	        }
	    });
	    left_table_prop_displayName.addListener(SWT.Selection,  getListener(table_prop_left,left_table_prop_displayName));
	    table_prop_left.setSortColumn(left_table_prop_displayName);
	    table_prop_left.setSortDirection(SWT.UP); 

	    left_table_prop_busiObj = new TableColumn(table_prop_left, SWT.BOLD);
	    left_table_prop_busiObj.setResizable(false);
	    left_table_prop_busiObj.setWidth(0);
	    left_table_prop_busiObj.setText("Business object");
	    left_table_prop_busiObj.pack();
	    left_table_prop_busiObj.setWidth(0);
	    
	   /* left_table_prop_busiObj.setData(new Comparator<TableItem>() {
	        @Override
	        public int compare(TableItem t1, TableItem t2) {
	            return t1.getText(1).compareTo(t2.getText(1));
	        }
	    });
	    left_table_prop_busiObj.addListener(SWT.Selection,  getListener(table_prop_left,left_table_prop_busiObj));
	    table_prop_left.setSortColumn(left_table_prop_busiObj);
	    table_prop_left.setSortDirection(SWT.UP); */
	 	    
	    Composite composite_Arrow = new Composite(composite_PropMain, SWT.NONE);
	   // composite_Arrow.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_Arrow.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		FillLayout fl_composite_Arrow = new FillLayout(SWT.VERTICAL);
		fl_composite_Arrow.spacing = 5;
		fl_composite_Arrow.marginWidth = 4;
		fl_composite_Arrow.marginHeight = 100;
		composite_Arrow.setLayout(fl_composite_Arrow);
	    
	    Button_RightSideArrow = new Button(composite_Arrow, SWT.NONE);
	    Button_RightSideArrow.setText(">");
	    
	    Button_LeftSideArrow = new Button(composite_Arrow, SWT.NONE);
	    Button_LeftSideArrow.setText("<");
	    
	   
	    Composite composite_ExportProperties = new Composite(composite_PropMain, SWT.NONE);
	   // composite_ExportProperties.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_ExportProperties.setLayout(new GridLayout(1, false));
	    GridData gd_composite_ExportProperties = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_composite_ExportProperties.widthHint = 250;
	    composite_ExportProperties.setLayoutData(gd_composite_ExportProperties);
	    
	    lblNewLabel = new Label(composite_ExportProperties, SWT.NONE);
	    //lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    lblNewLabel.setText("Displayed Columns:");
	    
	    table_prop_right = new Table(composite_ExportProperties, SWT.BORDER | SWT.FULL_SELECTION |SWT.MULTI);
	    table_prop_right.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
	    GridData gd_table_ExportProperties = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_table_ExportProperties.widthHint = 175;
	    table_prop_right.setLayoutData(gd_table_ExportProperties);
	    table_prop_right.setHeaderVisible(true);
	    table_prop_right.setLinesVisible(true);
	    
	    right_table_prop_displayName = new TableColumn(table_prop_right, SWT.NONE);
	    right_table_prop_displayName.setWidth(250);
	    right_table_prop_displayName.setText("Display name");
	    right_table_prop_displayName.setData(new Comparator<TableItem>() {
	        @Override
	        public int compare(TableItem t1, TableItem t2) {
	            return t1.getText(0).compareTo(t2.getText(0));
	        }
	    });
	    right_table_prop_displayName.addListener(SWT.Selection, getListener(table_prop_right,right_table_prop_displayName));
	    table_prop_right.setSortColumn(right_table_prop_displayName);
	    table_prop_right.setSortDirection(SWT.UP);
	   	    
	    right_table_prop_busiObj = new TableColumn(table_prop_right, SWT.NONE);
	    right_table_prop_busiObj.setResizable(false);
	    right_table_prop_busiObj.setWidth(0);
	    right_table_prop_busiObj.setText("Business object");
	    right_table_prop_busiObj.pack();
	    right_table_prop_busiObj.setWidth(0);
	   /* right_table_prop_busiObj.setData(new Comparator<TableItem>() {
	        @Override
	        public int compare(TableItem t1, TableItem t2) {
	            return t1.getText(1).compareTo(t2.getText(1));
	        }
	    });
	    right_table_prop_busiObj.addListener(SWT.Selection,  getListener(table_prop_right,right_table_prop_busiObj));
	    table_prop_right.setSortColumn(right_table_prop_busiObj);
	    table_prop_right.setSortDirection(SWT.UP);*/
		    
	    Composite composite_UPDownArrow = new Composite(composite_PropMain, SWT.NONE);
	  //  composite_UPDownArrow.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_UPDownArrow.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		FillLayout fl_composite_UPDownArrow = new FillLayout(SWT.VERTICAL);
		fl_composite_UPDownArrow.spacing = 5;
		fl_composite_UPDownArrow.marginWidth = 4;
		fl_composite_UPDownArrow.marginHeight = 100;
		composite_UPDownArrow.setLayout(fl_composite_UPDownArrow);

		upButton = new Button(composite_UPDownArrow, SWT.NONE);
		upButton.setImage(SWTResourceManager.getImage(TDPS_PropertySelectUI.class, "icons/up.png"));
		upButton.setToolTipText("Up");

		downButton = new Button(composite_UPDownArrow, SWT.NONE);
		downButton.setImage(SWTResourceManager.getImage(TDPS_PropertySelectUI.class,"icons/Down.png"));
	    
	    
	    Composite composite_ExportButton = new Composite(composite_main, SWT.NONE);
	    //composite_ExportButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    GridLayout gl_composite_ExportButton = new GridLayout(3, false);
	    gl_composite_ExportButton.horizontalSpacing = 10;
	    composite_ExportButton.setLayout(gl_composite_ExportButton);
	    GridData gd_composite_ExportButton = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1);
	    gd_composite_ExportButton.heightHint = 34;
	    composite_ExportButton.setLayoutData(gd_composite_ExportButton);
	    
	    btnBack = new Button(composite_ExportButton, SWT.NONE);
	    GridData gd_btnBack = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	    gd_btnBack.widthHint = 60;
	    btnBack.setLayoutData(gd_btnBack);
		btnBack.setText("Back");
	    
	    btnNext = new Button(composite_ExportButton, SWT.NONE);
	    GridData gd_btnNext = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	    gd_btnNext.widthHint = 60;
	    btnNext.setLayoutData(gd_btnNext);
	    btnNext.setText("Next");
	
	   
	    
	    btnCancel = new Button(composite_ExportButton, SWT.PUSH);
	    GridData gd_btnCancel = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	    gd_btnCancel.widthHint = 60;
	    btnCancel.setLayoutData(gd_btnCancel);
	    btnCancel.setText("Cancel");
	    btnCancel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell_TDPS_prop.close();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	 	
	    UIEvents();
	    //data will set on TDPSDisplayReportUI's back button
	    if(tableStorage!=null && tableStorage.size()>0)
	    {
	    	Object left_table_data = tableStorage.get(0);
	    	Object right_table_data=tableStorage.get(1);
		
	    	SetTableOldData(table_prop_left,left_table_data);
	    	SetTableOldData(table_prop_right,right_table_data);
	    }else
	    {
	    	//SetTableData("1_TDPS_PropDisplayLeft_Columns_Pref",table_prop_left,"LEFT_TABLE"); //Right Table Data 1_TDPS_RightPropertyCol
	    	//SetTableData("1_TDPS_PropDisplayLeft_Columns_Pref",table_prop_right,"RIGHT_TABLE"); //Right Table Data 1_TDPS_RightPropertyCol
	    	 SetTableData(table_prop_left,"LEFT_TABLE"); 
	       	SetTableData(table_prop_right,"RIGHT_TABLE");
	    }
	 
	    shell_TDPS_prop.open();
	  }
		
		//128 value for Up functionality
		//1024 Value for Down functionality
		  private void processMoveUpDown(int paramInt)
		  {
		    if ((paramInt != 128) && (paramInt != 1024)) {
		      return;
		    }
		    int rowCount = this.table_prop_right.getItemCount();
		    int[] arrayOfSelectedItems = this.table_prop_right.getSelectionIndices();
		    System.out.println("i=="+rowCount);
		    Arrays.sort(arrayOfSelectedItems);
		    int j = arrayOfSelectedItems[0]; // "0"th location of array
		    System.out.println("jj=="+j);
		    int k = arrayOfSelectedItems[(arrayOfSelectedItems.length - 1)]; // last location of array
		    System.out.println("k=="+k);
		    if ((paramInt == 128) && (j == 0)) {
		      return;
		    }
		    if ((paramInt == 1024) && (k == rowCount - 1)) {
		      return;
		    }		    
		    System.out.println("arrayOfInt1=="+arrayOfSelectedItems);
		    int[] arrayOfInt2;
		    int i1 = (arrayOfInt2 = arrayOfSelectedItems).length;
		    System.out.println("arrayOfInt2=="+arrayOfInt2);
		    System.out.println("i1=="+i1);
		    
		    if(paramInt == 128)
		    {
		        for (int n = 0; n < i1; n++)
			    {
			      int m = arrayOfInt2[n];
			      TableItem localTableItem1 = this.table_prop_right.getItem(m);
			      String str1 = localTableItem1.getText(0);
			      String str2 = localTableItem1.getText(1);
			      String tabItemData=(String) localTableItem1.getData("PROP_REAL_NAME");
			      String OBJECT_DATA_TYPE=(String) localTableItem1.getData("OBJECT_DATA_TYPE");
			      
			      
			      TableItem localTableItem2;
			      this.table_prop_right.remove(m);
			      localTableItem2 = new TableItem(this.table_prop_right, 0, m - 1);
			      localTableItem2.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			      localTableItem2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			      localTableItem2.setText(0, str1);
			      localTableItem2.setText(1, str2);
			      localTableItem2.setData("PROP_REAL_NAME",tabItemData);
			      localTableItem2.setData("OBJECT_DATA_TYPE",OBJECT_DATA_TYPE);
			      this.table_prop_right.select(m - 1);
			    }
		    }else if(paramInt == 1024)
		    {
		        for (int n = (i1-1); n >= 0; n--)
			    {
			      int m = arrayOfInt2[n];
			      TableItem localTableItem1 = this.table_prop_right.getItem(m);
			      String str1 = localTableItem1.getText(0);
			      String str2 = localTableItem1.getText(1);
			      String tabItemData=(String) localTableItem1.getData("PROP_REAL_NAME");
			      String OBJECT_DATA_TYPE=(String) localTableItem1.getData("OBJECT_DATA_TYPE");
			      
			      TableItem localTableItem2;
			      this.table_prop_right.remove(m);
			      localTableItem2 = new TableItem(this.table_prop_right, 0, m + 1);
			      localTableItem2.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			      localTableItem2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			      localTableItem2.setText(0, str1);
			      localTableItem2.setText(1, str2);
			      localTableItem2.setData("PROP_REAL_NAME",tabItemData);
			      localTableItem2.setData("OBJECT_DATA_TYPE",OBJECT_DATA_TYPE);
			       
				  this.table_prop_right.select(m + 1);
			    }
		    }
		    
		
		  }	
		  
		  
		  
	void SetTableData(Table table,String tableName)
	{
		
		//LinkedHashMap<String,LinkedHashMap<String,String>> hashlist=obj.getPreferenceData(prefName,tableName);
		LinkedHashMap<String,LinkedHashMap<String,String>> hashlist=null;
		//String lefttabfile="F:/Progneur_Eclipse/Eclipse_Workspace/workspace/com.teamcenter.tdps/src/LeftColumnList.properties";
		//String righttabfile="F:/Progneur_Eclipse/Eclipse_Workspace/workspace/com.teamcenter.tdps/src/RightColumnList.properties";
		
				
		String lefttabfile = null;
		String righttabfile = null;
		
	     try {
			TCPreferenceService service = session.getPreferenceService();
			String[] paths = service.getStringValues("TDPS_OA_LedgerPropertiesFilePath");
			for(String str:paths)
			{
				String tab=str.split("=")[0];
				if(tab.equals("LEFT_TABLE"))
					lefttabfile=str.split("=")[1];
				else
					righttabfile=str.split("=")[1];
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(tableName.equals("LEFT_TABLE"))
		  hashlist=obj.readPropertyFile(lefttabfile);//obj.getLeftTablePropertyValuList();
		if(tableName.equals("RIGHT_TABLE"))
			  hashlist=obj.getRightTablePropertyValuList();//obj.readPropertyFile(righttabfile);
		Set<String> keys = hashlist.keySet();
		
		for(String key:keys)
		{
			 LinkedHashMap<String, String> list = hashlist.get(key);
			
			 String busObjName=list.get("BUSINESS_OBJ_NAME");			 
			 //left_table_prop_realName.setText(busObjName);			 
			 String prop_realName=list.get("PROP_REAL_NAME");			 
			 String prop_dispName=list.get("PROP_DISPLAY_NAME");
			 String OBJECT_DATA_TYPE=list.get("OBJECT_DATA_TYPE");
			 
			
			// System.out.println("busObjName=="+busObjName+"prop_realName=="+prop_realName+"prop_dispName="+prop_dispName);
			 
			 TableItem item = new TableItem(table, SWT.NONE);
			 item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			 item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			 item.setText(0, prop_dispName);
			 item.setText(1, busObjName);
			 item.setData("PROP_REAL_NAME",prop_realName.trim());//reason: To fetch col data from TC for excel report
			 item.setData("OBJECT_DATA_TYPE",OBJECT_DATA_TYPE.trim());
		
		}
		System.out.println("sorting=="+hashlist.values());
		
		
		
		ArrayList<String> strList=new ArrayList<String>();
		ArrayList<Integer> strLocaionList=new ArrayList<Integer>();
		
		//sort data
		int cnt=0;
		for(String key:keys)
		{
			 LinkedHashMap<String, String> list = hashlist.get(key);
			 strList.add(list.get("PROP_DISPLAY_NAME"));
			 strLocaionList.add(cnt);
			 cnt++;
		}
		 
  }
	Listener sortTable(final Table table, final TableColumn tabCol) {
		Listener sortListener=null;
      try {
    	   sortListener = new Listener() {
    	        public void handleEvent(Event e) {
    	            TableItem[] items = table.getItems();
    	            Collator collator = Collator.getInstance(Locale.getDefault());
    	            TableColumn column = (TableColumn) e.widget;
    	            int index = column == tabCol ? 0 : 1;
    	            for (int i = 1; i < items.length; i++) {
    	                String value1 = items[i].getText(index);
    	                for (int j = 0; j < i; j++) {
    	                    String value2 = items[j].getText(index);
    	                    if (collator.compare(value1, value2) < 0) {
    	                        //String[] values = { items[i].getText(0), items[i].getText(1)};
    	                   	    String[] values=new String[table.getColumnCount()];
    	                   	   
    	                   	    for(int col=0;col<values.length;col++)
    	                		 values[col]=items[i].getText(col);
    	                   	    
    	                        String data = (String) items[i].getData("PROP_REAL_NAME");
    	                        String datatype = (String) items[i].getData("OBJECT_DATA_TYPE");
    	                        
    	                        
    	                        items[i].dispose();
    	                        TableItem item = new TableItem(table, SWT.NONE, j);
    	                        item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
    	                        item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	                        for(int col=0;col<values.length;col++)
    	                       	 item.setText(col,values[col]);

    	                        item.setData("PROP_REAL_NAME",data);
    	                        item.setData("OBJECT_DATA_TYPE",datatype);
    	                        items = table.getItems();
    	                        break;
    	                    }
    	                }
    	            }
    	            table.setSortColumn(column);
    	        }
    	    };
	} catch (Exception e) {
		e.printStackTrace();
	}
	return sortListener;
	}
	
	void internalSort(Table table, TableColumn column)
	{
	 try{

         TableItem[] items = table.getItems();
         Collator collator = Collator.getInstance(Locale.getDefault());
         int index = 0;
         for (int i = 1; i < items.length; i++) {
             String value1 = items[i].getText(index);
             for (int j = 0; j < i; j++) {
                 String value2 = items[j].getText(index);
                 if (collator.compare(value1, value2) < 0) {
                	 
                	 String[] values=new String[table.getColumnCount()];
                     //String[] values = {items[i].getText(0),items[i].getText(1)};
                	 for(int col=0;col<values.length;col++)
                		 values[col]=items[i].getText(col);
                	 
                     String data = (String) items[i].getData("PROP_REAL_NAME");
                     String datatype = (String) items[i].getData("OBJECT_DATA_TYPE");
                     items[i].dispose();
                     TableItem item = new TableItem(table, SWT.NONE, j);
                     item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
                     item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                     for(int col=0;col<values.length;col++)
                    	 item.setText(col,values[col]);
                     
                     item.setData("PROP_REAL_NAME",data);
                     item.setData("OBJECT_DATA_TYPE",datatype);
                     items = table.getItems();
                     break;
                 }
             }
         }
         table.setSortColumn(column);
     
		 
	 }catch(Exception e)
	 {
		 e.printStackTrace();
	 }
	}
	
	void UIEvents()
	{
	
		 Button_RightSideArrow.addSelectionListener(new SelectionAdapter() {
		    	@Override
		    	public void widgetSelected(SelectionEvent event) {
	                System.out.println("inside the handler");
			        
			        TableItem[] tabitems = table_prop_left.getSelection();
			        System.out.println("tabitems=="+tabitems.length);
			        for(int i=0;i<tabitems.length;i++)
			        {
			        	TableItem tItem = tabitems[i];
			        	System.out.println("\n item="+tItem.getText());	        	
			        	
			        	TableItem item = new TableItem(table_prop_right, SWT.NONE);
			        	item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			        	item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			   			  item.setText(0,tItem.getText(0));
			   			  item.setText(1,tItem.getText(1));
			   			  item.setData("PROP_REAL_NAME",tItem.getData("PROP_REAL_NAME"));
			   			  item.setData("OBJECT_DATA_TYPE",tItem.getData("OBJECT_DATA_TYPE"));
			   			  
			   			//}	        	
			        }
			        table_prop_left.remove(table_prop_left.getSelectionIndices());
		        	System.out.println("Remove property from 1st table");
		       	 
		        	//internalSort(table_prop_right,TableColumn_ExportProperties);
		        
		    	}
		    });
		 
		  Button_LeftSideArrow.addSelectionListener(new SelectionAdapter() {
		      	@Override
		      	public void widgetSelected(SelectionEvent event) {
	                System.out.println("inside back the handler");
			        
			        TableItem[] tabitems = table_prop_right.getSelection();
			       
			        System.out.println("tabitems=="+tabitems.length);
			        
			        for(int i=0;i<tabitems.length;i++)
			        {
			        	TableItem tItem = tabitems[i];
			        	System.out.println("back arraow button tItem===="+tItem);
			        	
			        	 //set to 1st table		        
				         TableItem item = new TableItem(table_prop_left, SWT.NONE);
				         item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
				         item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				   		 item.setText(0,tItem.getText(0));
  		   			     item.setText(1,tItem.getText(1));
				   		item.setData("PROP_REAL_NAME",tItem.getData("PROP_REAL_NAME"));
				   		item.setData("OBJECT_DATA_TYPE",tItem.getData("OBJECT_DATA_TYPE"));
				        //}   
			        }
			        table_prop_right.remove(table_prop_right.getSelectionIndices());
		          	System.out.println("Remove property from 2st table");	          
		          	//internalSort(table_prop_left,left_table_prop_displayName);		     	 
			       }	    	
		      });
		   btnBack.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					
					//for old data storage
					ArrayList<Object> tableStorage=new ArrayList<Object>();
					tableStorage.add(getTableData(table_prop_left));
					tableStorage.add(getTableData(table_prop_right));
					if(shell_TDPS_prop!=null)
						shell_TDPS_prop.dispose();
					new TDPS_FillCriteriaUI(session).fillCriteriaDialog(inputCriteriaList,dateChkBoxITtrue,tableStorage);
					
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
				
					
				}
			});
		  

		    btnNext.addSelectionListener(new SelectionListener() {
		    	
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
								
					LinkedHashMap<String,LinkedHashMap<String,String>> ColNameList=new LinkedHashMap<String,LinkedHashMap<String,String>>();
					TableItem[] items=table_prop_right.getItems();
					for(int i=0;i<items.length;i++)
					{
						LinkedHashMap<String,String> data=new LinkedHashMap<String,String>();
						data.put("PROP_DISPLAY_NAME",items[i].getText(0));
						data.put("BUSINESS_OBJ_NAME",items[i].getText(1));
						data.put("PROP_REAL_NAME",(String) items[i].getData("PROP_REAL_NAME"));
						data.put("OBJECT_DATA_TYPE",(String) items[i].getData("OBJECT_DATA_TYPE"));
						
						ColNameList.put((String) items[i].getData("PROP_REAL_NAME")+"_"+i,data);
					}
					System.out.println("ColNameList=="+ColNameList);
					//for old data storage
					ArrayList<Object> tableStorage=new ArrayList<Object>();
					tableStorage.add(getTableData(table_prop_left));
					tableStorage.add(getTableData(table_prop_right));
						
					obj.writeSOAInputTextFile(ColNameList);
					new TDPSDisplayReportUI(session,shell_TDPS_prop,ColNameList,inputCriteriaList,tableStorage,dateChkBoxITtrue).getTDPS_ReportUI();
					
					
					
				}
				
				

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});

		   upButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				 processMoveUpDown(128);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}); 
		   
		   downButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) { 
					processMoveUpDown(1024);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			}); 
	}
		
	LinkedHashMap<String, LinkedHashMap<String, String>> getDatalist(ArrayList<String> excelColList, ModelObject[] modelObjlist)
	{
		LinkedHashMap<String, LinkedHashMap<String, String>> maindatalist = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		try {		
			for(int i = 0 ;i< modelObjlist.length; i++)
			{
				LinkedHashMap<String, String> datalist = new LinkedHashMap<String, String>();
				String object_name = modelObjlist[i].getPropertyDisplayableValue("object_name");
				for(int j = 0 ;j< excelColList.size(); j++)
				{
					String value = modelObjlist[i].getPropertyDisplayableValue(excelColList.get(j));
					System.out.println("object_name_value=="+value);
					datalist.put(excelColList.get(j), value);
				}
				maindatalist.put(object_name, datalist);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maindatalist;
	}
	static String getmonth(String month)
	 {
		 if(month.equals("1") || month.equals("01"))
			 return "Jan";
		 if(month.equals("1") || month.equals("01"))
			 return "Jan";
		 if(month.equals("2") || month.equals("02"))
			 return "Feb";
		 if(month.equals("3") || month.equals("03"))
			 return "Mar";
		 if(month.equals("4") || month.equals("04"))
			 return "Apr";
		 if(month.equals("5") || month.equals("05"))
			 return "May";
		 if(month.equals("6") || month.equals("06"))
			 return "Jun";
		 if(month.equals("7") || month.equals("07"))
			 return "Jul";
		 if(month.equals("8") || month.equals("08"))
			 return "Aug";
		 if(month.equals("9") || month.equals("09"))
			 return "Sep";
		 if(month.equals("10"))
			 return "Oct";
		 if(month.equals("11"))
			 return "Nov";
		 if(month.equals("11"))
			 return "Dec";
		 
		return month;
		 
	 }
	/*Listener getListener(Table table)
	{
		Listener sortListener = null;
		try
		{
			tableForSort=table;
			sortListener=sort_Listener;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return sortListener;
		
	}*/
	
//	Listener sort_Listener = e -> {
	Listener getListener(final Table tableForSort,final TableColumn selectedColumn) {
		Listener sortListener=null;
		
	     	   return sortListener = new Listener() {
	    	        public void handleEvent(Event e) {
	   final  TableColumn sortColumn = tableForSort.getSortColumn();
	  //  TableColumn selectedColumn = (TableColumn) e.widget;
	    int dir = tableForSort.getSortDirection();
	    if (sortColumn == selectedColumn) {
	        dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
	    } else {
	    	tableForSort.setSortColumn(selectedColumn);
	        dir = SWT.UP;
	    }
	    TableItem[] items = tableForSort.getItems();
	    final Comparator<TableItem> comparator = (Comparator<TableItem>) selectedColumn.getData();
	    for (int i = 1; i < items.length; i++) {
	        for (int j = 0; j < i; j++) {
	            if ((comparator.compare(items[i], items[j]) < 0 && dir == SWT.UP) || (comparator.compare(items[i], items[j]) > 0 && dir == SWT.DOWN)) {
	                String[] oldItem = new String[tableForSort.getColumnCount()];
	                String oldTabItemDataStr=(String) items[i].getData("PROP_REAL_NAME");
	                String oldTOBJECT_DATA_TYPEStr=(String) items[i].getData("OBJECT_DATA_TYPE");
	                
	                for (int h = 0; h < tableForSort.getColumnCount(); h++) {
	                	oldItem[h] = items[i].getText(h);
	                }
	                items[i].dispose();
	                TableItem newItem = new TableItem(tableForSort, SWT.NONE, j);
	                newItem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
	                newItem.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	                newItem.setText(oldItem);
	                newItem.setData("PROP_REAL_NAME",oldTabItemDataStr);
	                newItem.setData("OBJECT_DATA_TYPE",oldTOBJECT_DATA_TYPEStr);
	                items = tableForSort.getItems();
	                break;
	            }
	        }
	    }
	    tableForSort.setSortDirection(dir);
	     }
	    };
	}
	private LinkedHashMap<Integer, LinkedHashMap<String, String>> getTableData(Table table_prop_left) {
		
		LinkedHashMap<Integer, LinkedHashMap<String, String>> hashlist=new LinkedHashMap<Integer, LinkedHashMap<String,String>>();
		
		int itemcnt=table_prop_left.getItemCount();
		
		
		if(itemcnt>0)
		{
			for(int row=0;row<itemcnt;row++)
			{
				LinkedHashMap<String, String> list=new LinkedHashMap<String, String>();
				TableItem tabItem = table_prop_left.getItem(row);
				
				list.put("PROP_DISPLAY_NAME", tabItem.getText(0));
				list.put("BUSINESS_OBJ_NAME", tabItem.getText(1));
				list.put("PROP_REAL_NAME", (String)tabItem.getData("PROP_REAL_NAME"));
				list.put("OBJECT_DATA_TYPE", (String)tabItem.getData("OBJECT_DATA_TYPE"));		
				
				hashlist.put(row, list);
			}
		}
		return hashlist;
	}
	
	private void SetTableOldData(Table table,Object obj) {
		try
		{
				
			if(obj!=null )
			{
				LinkedHashMap<String, LinkedHashMap<String, String>> list = (LinkedHashMap<String, LinkedHashMap<String, String>>)obj;
				if(list.size()>0)
				{
					for(int i=0;i<list.size();i++)
					{
						LinkedHashMap<String, String> innlist = list.get(i);
						TableItem item=new TableItem(table,  SWT.NONE);
						item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
						item.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
						item.setText(0,innlist.get("PROP_DISPLAY_NAME"));
						item.setText(1,innlist.get("BUSINESS_OBJ_NAME"));
						item.setData("PROP_REAL_NAME",innlist.get("PROP_REAL_NAME"));
						item.setData("OBJECT_DATA_TYPE",innlist.get("OBJECT_DATA_TYPE"));
						
	
					}
				}
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
	



