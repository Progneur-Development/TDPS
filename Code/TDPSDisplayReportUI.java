/****************************************************************
@copyright Progneur Technology
File Name : TDPSDisplayReportUI.java
Functionality : Report will be generated using given input criteria on SWT table,
                Table columns can be sort with respect to string,numeric and date 
                Excel report can be generated
Author : Bhavana Patil and Priyanka Tikhe
******************************************************************/
package com.teamcenter.tdps.view;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import com.tdp2.services.loose.oaledgerlib.GetSONumbersDataService;
import com.tdp2.services.loose.oaledgerlib._2015_07.GetSONumbersData;
import com.tdp2.services.loose.oaledgerlib._2015_07.GetSONumbersData.GetPropertyResponse;
import com.teamcenter.rac.kernel.TCComponentGroup;
import com.teamcenter.rac.kernel.TCComponentRole;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.StopWatch;
import com.teamcenter.soa.client.model.ModelObject;

public class TDPSDisplayReportUI {
	private static Table report_table;
	TCSession session;
	private Shell DisplayReportshell;
	private Button btnBack;
	private Button btnExport;
	private Button btnExit;
	LinkedHashMap<String,LinkedHashMap<String,String>> ColNameList;
	private TableColumn reporttable_col;
	private TC_Data_Operations obj;
	boolean dateChkBoxITtrue;
	static int selectedColumnIndex=0;
	private LinkedHashMap<String, String> inputCriteriaList;
	ArrayList<Object> tableStorage;
	private Label lbprogress_status;
	private Label lbdataLoadTime;
	public static boolean dataAvlflag=false;
	LinkedHashMap<String,String> columnNamesHashmap; 
	
public TDPSDisplayReportUI(TCSession session, Shell shell_TDPS_FillCriteria,LinkedHashMap<String,LinkedHashMap<String,String>> ColNameList,LinkedHashMap<String, String> inputCriteriaList, ArrayList<Object> tableStorage, boolean dateChkBoxITtrue)
{
	this.session=session;
	shell_TDPS_FillCriteria.close();
	DisplayReportshell=shell_TDPS_FillCriteria;
	this.ColNameList=ColNameList;
	obj=new TC_Data_Operations(session);
    this.inputCriteriaList=inputCriteriaList;
    this.tableStorage=tableStorage;
    this.dateChkBoxITtrue=dateChkBoxITtrue;
}
	
/**
 * @wbp.parser.entryPoint
 */
public void getTDPS_ReportUI()
{
	if(DisplayReportshell!=null)
		DisplayReportshell.dispose();
	DisplayReportshell = new Shell();
	//DisplayReportshell.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	DisplayReportshell.setSize(754, 699);
	DisplayReportshell.setText("TDPS Report");
	DisplayReportshell.setLayout(new GridLayout(1, false));
	
	Composite composite_Main = new Composite(DisplayReportshell, SWT.NONE);
	//composite_Main.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	composite_Main.setLayout(new GridLayout(1, false));
	composite_Main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	
	Composite composite_Table = new Composite(composite_Main, SWT.NONE);
	//composite_Table.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	composite_Table.setLayout(new GridLayout(1, false));
	GridData gd_composite_Table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	gd_composite_Table.heightHint = 136;
	composite_Table.setLayoutData(gd_composite_Table);
	
	report_table = new Table(composite_Table, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL);
	report_table.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
	//report_table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	report_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	report_table.setHeaderVisible(true);
	report_table.setLinesVisible(true);
	
	tps_displayColumnData();
	
	Composite composite_two = new Composite(composite_Main, SWT.NONE);
	// composite_two.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	 composite_two.setLayout(new GridLayout(2, false));
	 GridData gd_comp_all = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
	 gd_comp_all.heightHint = 49;
	 composite_two.setLayoutData(gd_comp_all);
	
	 Composite composite_label = new Composite(composite_two, SWT.NONE);
	// composite_label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	 GridLayout gl_composite_lb = new GridLayout(3, false);
	 gl_composite_lb.horizontalSpacing = 10;
	 composite_label.setLayout(gl_composite_lb);
	 GridData gd_composite_lbdata = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
	 gd_composite_lbdata.heightHint = 34;
	 composite_label.setLayoutData(gd_composite_lbdata);
	 
	 lbprogress_status =new Label(composite_label,SWT.NONE);
	 GridData gd_lbprogress_status = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	 gd_lbprogress_status.widthHint = 61;
	 lbprogress_status.setLayoutData(gd_lbprogress_status);
	// lbprogress_status.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	 lbprogress_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
	 lbprogress_status.setText("Ready");
	 
	 ProgressBar progressBar3 = new ProgressBar(composite_label, SWT.BORDER | SWT.SMOOTH | SWT.INDETERMINATE);
	 GridData gd_progressBar3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	 gd_progressBar3.widthHint = 40;
	 progressBar3.setLayoutData(gd_progressBar3);
	 
	 lbdataLoadTime =new Label(composite_label,SWT.NONE);
	 GridData gd_lbdataLoadtime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	 gd_lbdataLoadtime.widthHint = 136;
	 lbdataLoadTime.setLayoutData(gd_lbdataLoadtime);
	// lbdataLoadTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	 lbdataLoadTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
	 lbdataLoadTime.setText("Loading Time :");
	
	 Composite composite_Button = new Composite(composite_two, SWT.NONE);
	 //composite_Button.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	 GridLayout gl_composite_ExportButton = new GridLayout(3, false);
	 gl_composite_ExportButton.horizontalSpacing = 10;
	 composite_Button.setLayout(gl_composite_ExportButton);
	 GridData gd_composite_ExportButton = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
	 gd_composite_ExportButton.heightHint = 34;
	 composite_Button.setLayoutData(gd_composite_ExportButton);
	
	btnBack = new Button(composite_Button, SWT.PUSH);
    GridData gd_btnBack = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
    gd_btnBack.widthHint = 60;
    btnBack.setLayoutData(gd_btnBack);
    btnBack.setText("Back");
	
	btnExport = new Button(composite_Button, SWT.PUSH);
	GridData gd_btnExport = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	gd_btnExport.widthHint = 60;
	btnExport.setLayoutData(gd_btnExport);
	btnExport.setText("Export");
	btnExport.setEnabled(false);
	
	btnExit = new Button(composite_Button, SWT.PUSH);
	GridData gd_btnExit = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	gd_btnExit.widthHint = 60;
	btnExit.setLayoutData(gd_btnExit);
	btnExit.setText("Exit");
	getEvents();
	
	DisplayReportshell.open();
	
	//Assign data to table
	try {
		dataAvlflag=false;
		 lbprogress_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		 lbprogress_status.setText("Loading...");
	 
		 StopWatch sw = new StopWatch();
		 sw.start();
		 
		 tdps_displayTableData();
		 
		 sw.stop();
		 
		 long ms = sw.getInterval();
		 System.out.println("ms-=="+ms);
		 String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
		            TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
		            TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
		 
		 System.out.println(hms);
		 lbdataLoadTime.setText("Loading Time:"+hms);
		 lbprogress_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		 lbprogress_status.setText("Ready");
		 progressBar3.setVisible(false);
		 
		 if(!dataAvlflag)
		 {
			  org.eclipse.swt.widgets.MessageBox messageBox;
	       		messageBox = new org.eclipse.swt.widgets.MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION| SWT.OK);
	            messageBox.setText("Information");
	            messageBox.setMessage("Data is not available for given input criteria. \n  Please provide valid input");
	            int rc = messageBox.open();

		 }
		 ExportBtnVisibility();
		 
	} catch (Exception e) {
		 lbprogress_status.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		 lbprogress_status.setText("Ready");
		 e.printStackTrace();
	}

}
private void ExportBtnVisibility() {
	try {
		TCComponentGroup group = session.getCurrentGroup();
		TCComponentRole role = session.getCurrentRole();
		if(dataAvlflag && group.toString().equals("Engineering") && (role.toString().equals("Approver") || role.toString().equals("HOD")))
		{
			btnExport.setEnabled(true);
			
		}else
		{
			btnExport.setEnabled(false);
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}
LinkedHashMap<String,String> getDataTypeList(String inputStr)
{
	LinkedHashMap<String,String> dummyBusObjHashList=new LinkedHashMap<String,String>();
	try {
		
		for(String key:ColNameList.keySet())
		{
			String val=ColNameList.get(key).get("PROP_DISPLAY_NAME").trim();
			String dataType=ColNameList.get(key).get("OBJECT_DATA_TYPE").trim();
			dummyBusObjHashList.put(val,dataType);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return dummyBusObjHashList;
}
ArrayList<String> getUniqueList(String inputStr)
{
	ArrayList<String> dummyBusObjList=new ArrayList<String>();
	try {
		
		for(String key:ColNameList.keySet())
		{
			String val=ColNameList.get(key).get(inputStr).trim();
			dummyBusObjList.add(val);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return dummyBusObjList;
}
ArrayList<String> getUniqueList_old(String inputStr)
{
	LinkedHashMap<String,String> dummyBusObjHashList=new LinkedHashMap<String,String>();
	ArrayList<String> dummyBusObjList=new ArrayList<String>();
	try {
		
		for(String key:ColNameList.keySet())
		{
			String val=ColNameList.get(key).get(inputStr).trim();
			dummyBusObjHashList.put(val, val);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	for(String value:dummyBusObjHashList.keySet())
		dummyBusObjList.add(value);
	return dummyBusObjList;
}


void tps_displayColumnData()
{
	   ArrayList<String> uniqueDisplayNameList=getUniqueList("PROP_DISPLAY_NAME");
	   ArrayList<String> uniqueRealNameList=getUniqueList("PROP_REAL_NAME");
	   LinkedHashMap<String, String> OBJECT_DATA_TYPEList = getDataTypeList("OBJECT_DATA_TYPE");
	   columnNamesHashmap=new LinkedHashMap<String,String>();
		//Set table columns dynamically
		selectedColumnIndex=0;
		for(int i=0;i<uniqueDisplayNameList.size();i++)
		{
			reporttable_col = new TableColumn(report_table, SWT.NONE);
		    reporttable_col.setWidth(100);
		    reporttable_col.setText(uniqueDisplayNameList.get(i));//Get Display name
		    
		    String dType=OBJECT_DATA_TYPEList.get(uniqueDisplayNameList.get(i));
		  //  System.out.println("dType=="+dType);
		    if(dType.equals("integer"))
		    {
		    	 reporttable_col.setData(new Comparator<TableItem>() {
				        @Override
				        public int compare(TableItem t1, TableItem t2) {
				        	int i1=0;
				        	int i2=0;
				        	String tt11 = t1.getText(selectedColumnIndex);
				        	String tt22 = t2.getText(selectedColumnIndex);
				        	if(!tt11.equals(""))
				        		i1 = Integer.parseInt(tt11);
				        	if(!tt22.equals(""))
				        		i2 = Integer.parseInt(tt22);
				        	
				            if (i1 < i2) return -1;
				            if (i1 > i2) return 1;
				            return 0;
				        }
				    });
		    }else if(dType.equals("double"))
		    {
		    	 reporttable_col.setData(new Comparator<TableItem>() {
				        @Override
				        public int compare(TableItem t1, TableItem t2) {
				        	
				        	double i1=0.0;
				        	double i2=0.0;
				        	String tt11 = t1.getText(selectedColumnIndex);
				        	String tt22 = t2.getText(selectedColumnIndex);
				        	if(!tt11.equals(""))
				        		i1 = Double.parseDouble(tt11);
				        	if(!tt22.equals(""))
				        		i2 = Double.parseDouble(tt22);
				         
				        	if (i1 < i2) return -1;
				            if (i1 > i2) return 1;
				            return 0;
				        }
				    });
		    }else if(dType.equals("date"))
		    {
		    	 reporttable_col.setData(new Comparator<TableItem>() {
				        @Override
				        public int compare(TableItem t1, TableItem t2) {
				        	
				        	//long i1 = Date.parse(t1.getText(selectedColumnIndex));
				        	//long i2 = Date.parse(t2.getText(selectedColumnIndex));
				        	
				        	long i1=0;
				        	long i2=0;
				        	String tt11 = t1.getText(selectedColumnIndex);
				        	String tt22 = t2.getText(selectedColumnIndex);
				        	if(!tt11.equals(""))
				        		i1 = Date.parse(tt11);
				        	if(!tt22.equals(""))
				        		i2 = Date.parse(tt22);
				        	
				        	 if (i1 < i2) return -1;
					            if (i1 > i2) return 1;
					            return 0;
				        }
				    });
		    }else
		    {
		    	reporttable_col.setData(new Comparator<TableItem>() {
			        @Override
			        public int compare(TableItem t1, TableItem t2) {
			            return t1.getText(selectedColumnIndex).compareTo(t2.getText(selectedColumnIndex));
			        }
			    });
		    }
		    
		    reporttable_col.addListener(SWT.Selection,  getListener(report_table,reporttable_col));
		    report_table.setSortColumn(reporttable_col);
		    report_table.setSortDirection(SWT.UP);
		    columnNamesHashmap.put(uniqueRealNameList.get(i),i+"="+uniqueDisplayNameList.get(i));
		}
}

private void tdps_displayTableData() {
	// TODO Auto-generated method stub
	try
	{
		String soNumberFromStr=inputCriteriaList.get("SO_NUMBER_FROM");
		String soNumberToStr=inputCriteriaList.get("SO_NUMBER_TO");
		String from_date=inputCriteriaList.get("FROM_DATE");
		String to_date=inputCriteriaList.get("TO_DATE");

	//	ArrayList<String> dummyBusObjList=getUniqueList("BUSINESS_OBJ_NAME");
		//ArrayList<String> dummyRealNameList=getUniqueList("PROP_REAL_NAME");
	
			//SOA call : To execute query with provided SO input criteria.
		// Fetch SO objects and  traverse SO data on give response to RAC
		callSOA(soNumberFromStr,soNumberToStr,from_date,to_date,dateChkBoxITtrue);
	}catch(Exception e)
	{
		e.printStackTrace();
	}
}

void getEvents()
{
  btnExit.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			DisplayReportshell.close();
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		}
	});
	btnBack.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {

    
			new TDPS_PropertySelectUI(session,DisplayReportshell,inputCriteriaList).propertyDisplayDialog(tableStorage,dateChkBoxITtrue);
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		
			
		}
	});
	  //Export to excel button handler
	
	btnExport.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent arg0) {

          System.out.println("Export Button pressed");
         
          try {
        	  
        	  DataExportToExcelReport export=new DataExportToExcelReport(report_table);
        	  export.generateReport();
       	  
          } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
				
		}
	});
}

LinkedHashMap<String, LinkedHashMap<String, String>> getDatalist_old(ArrayList<String>excelColList, ModelObject[] modelObjlist)
{
	LinkedHashMap<String, LinkedHashMap<String, String>> maindatalist = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	try {		
		
		for(int i = 0 ;i< modelObjlist.length; i++)
		{
			LinkedHashMap<String, String> datalist = new LinkedHashMap<String, String>();
			String object_name = modelObjlist[i].getPropertyDisplayableValue("object_name");
			String item_id = modelObjlist[i].getPropertyDisplayableValue("object_name");
			
			for(String relName:excelColList)
			{
				String value = modelObjlist[i].getPropertyDisplayableValue(relName);
				System.out.println("object_name_value=="+value);
				datalist.put(relName, value);
			}

			maindatalist.put(item_id+"_"+i, datalist);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return maindatalist;
}

Listener getListener(final Table tableForSort,final TableColumn selectedColumn) {
	Listener sortListener=null;
     	   return sortListener = new Listener() {
    	        public void handleEvent(Event e) {
    final TableColumn sortColumn = tableForSort.getSortColumn();
  //  TableColumn selectedColumn = (TableColumn) e.widget;
    int dir = tableForSort.getSortDirection();
      
    //set column index for column setData
    String selColname=selectedColumn.getText();
    selectedColumnIndex=0;
    TableColumn[] columns = tableForSort.getColumns();
    for(int col=0;col<columns.length;col++)
    {
    	String colname=columns[col].getText();
    	if(colname.equals(selColname))
    	{
    	  System.out.println("selColname==="+selColname);	
    	  selectedColumnIndex=col;
    	  break;
    	}
    }
    
    
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

void callSOA(String soNumberFromStr, String soNumberToStr,String from_date, String to_date, boolean dateChkBoxITtrue2)
{
	// TODO Auto-generated method stub
      Map<String,String> allDataList=new LinkedHashMap<String, String>();
      
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

    		GetSONumbersData service = GetSONumbersDataService.getService(session.getSoaConnection());
    		GetPropertyResponse response = service.getSOnumbersObjProperties(QryInpVec);
    		if(response!=null)
    		{
    			Map<String, String>	map=response.propertyResp;
    			Set <Entry<String, String>>set=map.entrySet();
    			Iterator<Entry<String, String>>itr=set.iterator();
    			TableColumn[] columns = report_table.getColumns();
    			while(itr.hasNext())
    			{
    				Entry<String, String>entry=itr.next();
    			//	System.out.println(entry.getKey()+"=====> "+entry.getValue());
    				
    				
    				
    				if(!entry.getValue().equals(""))
    				{
    					String sodata=entry.getValue();
    						
    					String data[]=sodata.split("\\|\\^\\|");
    					
    					if(data.length>=0)
    					{
    						TableItem item=new TableItem(report_table, SWT.NONE);
    						item.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
    					 // System.out.println("SO data="+sodata);
    						for(int i=0;i<data.length;i++)
    						{
    							//System.out.println("data[i] ="+data[i]);
    							if(data[i].contains("|*|"))
    							{
    								String relname = data[i].split("\\|\\*\\|")[0];
    								String coloLocationStr = columnNamesHashmap.get(relname).split("=")[0];
    								int ColLoc = Integer.parseInt(coloLocationStr);
    								String tabITemStr = data[i].split("\\|\\*\\|")[1];
    								if(tabITemStr.equals("BLANK_VALUE"))
    								  item.setText(ColLoc,"");
    								else
    									item.setText(ColLoc,tabITemStr);
    							
    								dataAvlflag=true;						
    							}
    							
    						}
    					}
    						
    				}else
    					dataAvlflag=false;
    			}
    		}else
    			dataAvlflag=false;
    	 
	} catch (Exception e) {
		e.printStackTrace();
	}
	
//	GetSONumbersData obj=session.gets
	//obj.getSOnumbersObjProperties(data);
	
	
}

}

