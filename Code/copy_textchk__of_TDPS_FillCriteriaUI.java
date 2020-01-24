/****************************************************************
 @copyright Progneur Technology
 File Name : TDPS_FillCriteriaUI
 Functionality : This file takes the input criteria for SO search
 Author : Bhavana Patil
 ******************************************************************/

package com.teamcenter.tdps.view;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;

import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.tdps.view.TC_Data_Operations;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.wb.swt.SWTResourceManager;
	public class copy_textchk__of_TDPS_FillCriteriaUI {
	protected static TableColumn TableColumn_ExportProperties = null;
	private Button btnNext;
	TCSession session;
	private Text text_SoNumberFrom;
	private Shell shell_tdps_criteria;
	private Button btnCancel;
	private TC_Data_Operations obj;
	private DateTime dateTime_fromDate;
	private DateTime dateTime_ToDate;
	private DateTime dateTime_fromTime;
	private DateTime dateTime_toTime;
	private Composite composite_date;
	private Button btnCheckButton;
	private Label lbSoNumberTo;
	private Text text_SoNumberTo;
	private ArrayList<Object> tableStorage;
	protected Text soTo_Text;
	protected Text sofrom_Text;
	
	public copy_textchk__of_TDPS_FillCriteriaUI(TCSession session) {
		this.session=session;
		obj=new TC_Data_Operations(session); 
		
	}
	/**
	 * @param dateChkBoxITtrue 
	 * @param tableStorage 
	 * @param inputCriteriaList 
	 * @wbp.parser.entryPoint
	 */
public void fillCriteriaDialog(LinkedHashMap<String, String> oldinputCriteriaList, boolean dateChkBoxITtrue, ArrayList<Object> tableStorage) 
   {
	    //Display display = new Display();
	    this.tableStorage=tableStorage;
	    shell_tdps_criteria = new Shell();
	   // shell_tdps_criteria.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    shell_tdps_criteria.setText("TDPS Report: Fill Criteria");
	    shell_tdps_criteria.setSize(498, 313);
	    shell_tdps_criteria.setLayout(new GridLayout(1, false));
	    	    
	    Composite composite_main = new Composite(shell_tdps_criteria, SWT.NONE);
	    //composite_main.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    composite_main.setToolTipText("Fill Criteria");
	    composite_main.setLayout(new GridLayout(1, false));
	    GridData gd_composite_main = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	    gd_composite_main.heightHint = 224;
	    composite_main.setLayoutData(gd_composite_main);
	    
	    final Composite composite_selectedType = new Composite(composite_main, SWT.BORDER);
	    //composite_selectedType.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    GridLayout gl_composite_selectedType = new GridLayout(3, false);
	    gl_composite_selectedType.verticalSpacing = 8;
	    gl_composite_selectedType.marginTop = 15;
	    gl_composite_selectedType.marginRight = 20;
	    gl_composite_selectedType.marginLeft = 20;
	    gl_composite_selectedType.marginBottom = 1;
	    gl_composite_selectedType.marginHeight = 15;
	    composite_selectedType.setLayout(gl_composite_selectedType);
	    GridData gd_composite_selectedType = new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1);
	    gd_composite_selectedType.heightHint = 210;
	    composite_selectedType.setLayoutData(gd_composite_selectedType);
	    
	    Label lblSoNumber = new Label(composite_selectedType, SWT.HORIZONTAL);
	    //lblSoNumber.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    GridData gd_lblSoNumber = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	    gd_lblSoNumber.widthHint = 100;
	    lblSoNumber.setLayoutData(gd_lblSoNumber);
	    lblSoNumber.setText("SO Number From :");
	    
		  text_SoNumberFrom = new Text(composite_selectedType, SWT.BORDER);
		  GridData gd_text = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		  gd_text.widthHint = 90;
		  text_SoNumberFrom.setLayoutData(gd_text);
		  new Label(composite_selectedType, SWT.NONE);
		  text_SoNumberFrom.setText("SO-");
		  
		  lbSoNumberTo = new Label(composite_selectedType, SWT.NONE);
		  GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		  gd_lblNewLabel.widthHint = 100;
		  lbSoNumberTo.setLayoutData(gd_lblNewLabel);
		  //lbSoNumberTo.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		  lbSoNumberTo.setText("SO Number To   :");
		  
		  text_SoNumberTo =new Text(composite_selectedType, SWT.BORDER);
		  GridData gd_text1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		  gd_text1.widthHint = 90;
		  text_SoNumberTo.setLayoutData(gd_text1);
		  text_SoNumberTo.setText("SO-");
		 
		  new Label(composite_selectedType, SWT.NONE);
		  
		  btnCheckButton = new Button(composite_selectedType, SWT.CHECK);
		  //btnCheckButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		  btnCheckButton.setText("Date Selection(Released SO)");
		  btnCheckButton.setSelection(dateChkBoxITtrue);
		  new Label(composite_selectedType, SWT.NONE);
		  new Label(composite_selectedType, SWT.NONE);
		  
		  composite_date = new Composite(composite_selectedType, SWT.BORDER);
		 // composite_date.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		  GridLayout gl_composite_date = new GridLayout(3, false);
		  gl_composite_date.marginTop = 5;
		  gl_composite_date.verticalSpacing = 8;
		  composite_date.setLayout(gl_composite_date);
		  GridData gd_composite_date = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		  gd_composite_date.widthHint = 257;
		  gd_composite_date.heightHint = 75;
		  composite_date.setLayoutData(gd_composite_date);
		  
		  new Label(composite_selectedType, SWT.NONE);
		  new Label(composite_selectedType, SWT.NONE);
		  
		  Label lblFromDate = new Label(composite_date, SWT.NONE);
		  //lblFromDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		  GridData gd_lblFromDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_lblFromDate.widthHint = 70;
		  lblFromDate.setLayoutData(gd_lblFromDate);
		  lblFromDate.setText("From Date:");
		  
		  dateTime_fromDate = new DateTime(composite_date,SWT.DROP_DOWN| SWT.BORDER);
		  GridData gd_dateTime_fromDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_dateTime_fromDate.widthHint = 85;
		  dateTime_fromDate.setLayoutData(gd_dateTime_fromDate);
	  
		  
		 dateTime_fromTime = new DateTime(composite_date,SWT.TIME | SWT.SHORT | SWT.BORDER);
		  GridData gd_dateTime_fromTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_dateTime_fromTime.widthHint = 72;
		  dateTime_fromTime.setLayoutData(gd_dateTime_fromTime);
		  	  
		  Label lblToDate = new Label(composite_date, SWT.NONE);
		  //lblToDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		  GridData gd_lblToDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_lblToDate.widthHint = 70;
		  lblToDate.setLayoutData(gd_lblToDate);
		  lblToDate.setText("To Date:");
		  
		  dateTime_ToDate = new DateTime(composite_date, SWT.DROP_DOWN| SWT.BORDER);
		  GridData gd_dateTime_ToDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_dateTime_ToDate.widthHint = 85;
		  dateTime_ToDate.setLayoutData(gd_dateTime_ToDate);
		  
		  dateTime_toTime = new DateTime(composite_date,SWT.TIME | SWT.SHORT | SWT.BORDER);
		  GridData gd_dateTime_toTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_dateTime_toTime.widthHint = 72;
		  dateTime_toTime.setLayoutData(gd_dateTime_toTime);
		  new Label(composite_selectedType, SWT.NONE);
		
	    
	    Composite composite_ExportButton = new Composite(composite_main, SWT.NONE);
	    //composite_ExportButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    GridLayout gl_composite_ExportButton = new GridLayout(2, false);
	    gl_composite_ExportButton.horizontalSpacing = 10;
	    composite_ExportButton.setLayout(gl_composite_ExportButton);
	    GridData gd_composite_ExportButton = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1);
	    gd_composite_ExportButton.heightHint = 34;
	    composite_ExportButton.setLayoutData(gd_composite_ExportButton);
	    
	    btnNext = new Button(composite_ExportButton, SWT.PUSH);
	    GridData gd_btnNext = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	    gd_btnNext.widthHint = 60;
	    btnNext.setLayoutData(gd_btnNext);
	    btnNext.setText("Next");
	    btnNext.setEnabled(false);
	
	   
	    
	    btnCancel = new Button(composite_ExportButton, SWT.PUSH);
	    GridData gd_btnCancel = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	    gd_btnCancel.widthHint = 60;
	    btnCancel.setLayoutData(gd_btnCancel);
	    btnCancel.setText("Cancel");
	    btnCancel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell_tdps_criteria.close();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    getUIEvents();
	    setEnableFields(btnCheckButton.getSelection());
	    //Used for TDPS_PRoertySelectUI's back button data set
	    if(oldinputCriteriaList!=null && oldinputCriteriaList.size()>0)
	    {
	    	setOldData(oldinputCriteriaList);
	    }
	   // shell_tdps_criteria.pack();
	    shell_tdps_criteria.open();
	   
	  }

    void setEnableFields(boolean istrue)
    {
    	dateTime_fromDate.setEnabled(istrue);
    	dateTime_fromTime.setEnabled(istrue);
    	dateTime_ToDate.setEnabled(istrue);
    	dateTime_toTime.setEnabled(istrue);
    	
    }
	private void getUIEvents() {
		text_SoNumberFrom.addModifyListener(new ModifyListener(){
		  		public void modifyText(ModifyEvent event) {
		          // Get the widget whose text was modified
		  		
		  			try {
		  			  sofrom_Text = (Text) event.widget;
				         // System.out.println(text.getText());
				          if(sofrom_Text.getText()!=null && !sofrom_Text.getText().equals("")&&  sofrom_Text.getText().length()>3 && !sofrom_Text.getText().equals("SO-") 
				        		  && soTo_Text.getText()!=null && !soTo_Text.getText().equals("") &&  soTo_Text.getText().length()>3 && !sofrom_Text.getText().equals("SO-") )
				            btnNext.setEnabled(true);
				          else
				        	  btnNext.setEnabled(false);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
		  		  }
		      });
		
		text_SoNumberTo.addModifyListener(new ModifyListener(){
		      public void modifyText(ModifyEvent event) {
		    	  
		    	  try {
		    		// Get the widget whose text was modified
			           soTo_Text = (Text) event.widget;
			          //System.out.println(text.getText());
			          if(soTo_Text.getText()!=null &&  !soTo_Text.getText().equals("") && soTo_Text.getText().length()>3 && !soTo_Text.getText().equals("SO-") 
			        		  && sofrom_Text.getText()!=null && !sofrom_Text.getText().equals("")&&  sofrom_Text.getText().length()>3 && !sofrom_Text.getText().equals("SO-"))
			            btnNext.setEnabled(true);
			          else
			        	  btnNext.setEnabled(false);
		    	  } catch (Exception e) {
						e.printStackTrace();
					}
		          
		        }
		      });
		
		btnCheckButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("btnCheckButton.getSelection()"+btnCheckButton.getSelection());
				if(btnCheckButton.getSelection())
				{
					setEnableFields(true);
					btnNext.setEnabled(true);
					System.out.println("inside the composite enable");
				}else
				{
					setEnableFields(false);
					btnNext.setEnabled(false);
					System.out.println("inside the composite disable");
				}
			}
			
		});
	    btnNext.addSelectionListener(new SelectionListener() {
	    	
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					
					boolean dateChkBoxITtrue = btnCheckButton.getSelection();
					String SoNumberFromStr=text_SoNumberFrom.getText();
					String SoNumberToStr=text_SoNumberTo.getText();
					String from_date=dateTime_fromDate.getDay()+"-"+getmonth(""+(dateTime_fromDate.getMonth () + 1))+"-"+dateTime_fromDate.getYear()+" "+dateTime_fromTime.getHours()+":"+dateTime_fromTime.getMinutes();
					String to_date=dateTime_ToDate.getDay()+"-"+getmonth(""+(dateTime_ToDate.getMonth () + 1))+"-"+dateTime_ToDate.getYear()+" "+dateTime_toTime.getHours()+":"+dateTime_toTime.getMinutes();
					
						
					LinkedHashMap<String,String> inputCriteriaList=new LinkedHashMap<String,String>();
					inputCriteriaList.put("SO_NUMBER_FROM",SoNumberFromStr);
					inputCriteriaList.put("SO_NUMBER_TO",SoNumberToStr);
					inputCriteriaList.put("FROM_DATE",from_date);
					inputCriteriaList.put("TO_DATE",to_date);
					
					/*//List for query check
					LinkedHashMap<String,String> inputForQuery=new LinkedHashMap<String,String>();
					inputForQuery.put("item_id_After",SoNumberFromStr);
					inputForQuery.put("item_id_Before",SoNumberToStr);
					if(dateChkBoxITtrue)
					{
						inputForQuery.put("Released After",from_date);
						inputForQuery.put("Released Before",to_date);
					}else
					{
						inputForQuery.put("Released After","");
						inputForQuery.put("Released Before","");
						
					}
					
					//Data is available or not checekd by query 
					ModelObject[] modelObjs = obj.getObjectData(inputForQuery,dateChkBoxITtrue);*/
					Date dFrmDate=null;
					Date dToDate=null;
					try {
						 dFrmDate=new Date(from_date);
						 dToDate=new Date(to_date);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					/* boolean chkNotMaxDataInLowDuration=false;
					if(dateChkBoxITtrue)
						chkNotMaxDataInLowDuration=getChkMaxDataInLowDuration(inputCriteriaList);*/
					
					boolean isValidSORan = true;
					if(SoNumberFromStr.trim().equals("*") && SoNumberToStr.trim().equals("*"))
						isValidSORan=false;
					else if(SoNumberFromStr.trim().equalsIgnoreCase("SO-") && SoNumberToStr.trim().equalsIgnoreCase("SO-"))
					   isValidSORan=false;
					//else if(SoNumberFromStr.trim().length()>0 && SoNumberToStr.trim().length()>0)
					 // isValidSORan=isValidSORange(SoNumberFromStr,SoNumberToStr);
					
					//int result = SoNumberFromStr.compareTo(SoNumberToStr);
					//System.out.println("Result==     "+result);
					
					
					if(!isValidSORan)
					//if(result== 1)
					{
						org.eclipse.swt.widgets.MessageBox messageBox;
				       	messageBox = new org.eclipse.swt.widgets.MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION| SWT.OK);
				        messageBox.setText("Information");
				       // messageBox.setMessage("Please enter the valid SO range for search criteria \n eg.\t SO-01-0011 to SO-01-0015 \n\t SO-11* to  SO-11-9999 \n\t SO-11*");
				        messageBox.setMessage("Please enter valid SO range for search criteria");
				        int rc = messageBox.open();
						
					}
					else if(dateChkBoxITtrue && (!dFrmDate.before(dToDate) || dFrmDate.equals(dToDate)))
					{						
						org.eclipse.swt.widgets.MessageBox messageBox;
				       	messageBox = new org.eclipse.swt.widgets.MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION| SWT.OK);
				        messageBox.setText("Information");
				        messageBox.setMessage("\t Please enter valid date \n 'To Date' date/time should be greater than 'From Date' date/time");
				        int rc = messageBox.open();

					}/*else if(modelObjs==null || modelObjs.length<=0)
					{
						org.eclipse.swt.widgets.MessageBox messageBox;
				       	messageBox = new org.eclipse.swt.widgets.MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION| SWT.OK);
				        messageBox.setText("Information");
				        messageBox.setMessage("Data is not availabe for given input search criteria \n Please try with different inputs \n eg.\t SO-01-0011 to SO-01-0015 \n\t SO-11* to  SO-11-9999 \n\t SO-11*");
				        int rc = messageBox.open();
					}
					else if(chkNotMaxDataInLowDuration)
					{
						 MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						    messageBox.setMessage("You are trying to search maximum SO objects in low duration. \n Do you want to terminate and go back to the input window, or go ahead.");
						    messageBox.setText("Alert Message");
						    int response = messageBox.open();
						    if (response == SWT.YES) {
						        try {
						        	new TDPS_PropertySelectUI(session,shell_tdps_criteria,inputCriteriaList).propertyDisplayDialog(tableStorage,dateChkBoxITtrue);
						        }
						        catch (Exception e) {
						            e.printStackTrace();
						        }
						    }
						    	
					}*/else
						new TDPS_PropertySelectUI(session,shell_tdps_criteria,inputCriteriaList).propertyDisplayDialog(tableStorage,dateChkBoxITtrue);
					
				}
			
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		
	}
	
	private boolean getChkMaxDataInLowDuration(LinkedHashMap<String, String> inputCrieria) 
	{
		boolean ChkMaxDataInLowDuration=false;
		
		 String SoFrom = inputCrieria.get("SO_NUMBER_FROM");
		  String SoTo =   inputCrieria.get("SO_NUMBER_TO");
		
		  int TodalSoSearchObj=0;
		 if(SoFrom!=null && SoTo!=null && SoFrom.length()==10 && SoTo.length()==10)
		 {
			 String fromNo= SoFrom.substring(SoFrom.length() - 4);
			 String toNo =SoTo.substring(SoTo.length() - 4);
			 TodalSoSearchObj=Integer.parseInt(toNo) - Integer.parseInt(fromNo);
		 }
		 
		
		long daysBetween = 0;
		try {
			 SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
		           
			 String dateBeforeString = inputCrieria.get("TO_DATE");
			  String dateAfterString =   inputCrieria.get("FROM_DATE");
			 
			       Date dateBefore = myFormat.parse(dateBeforeString);
			       Date dateAfter = myFormat.parse(dateAfterString);
			      long difference = dateBefore.getTime() - dateAfter.getTime();
		           daysBetween = (difference / (1000*60*60*24));
	               /* You can also convert the milliseconds to days using this method
	                * float daysBetween = 
	                *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
	                */
		       System.out.println("Number of Days between dates: "+daysBetween);
		       
		       
		       
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(TodalSoSearchObj>=499 && daysBetween<=31)
			ChkMaxDataInLowDuration=true;
		else
			ChkMaxDataInLowDuration=false;
		
		return ChkMaxDataInLowDuration;
	}
	public static boolean isValidSORange( String SoFrom,String SoTo )
	{
		boolean chkRes;
		 if(SoFrom.contains("*") && Pattern.compile( "[0-9]" ).matcher( SoFrom ).find())
		 {
			 
		 }else if (!Pattern.compile( "[0-9]" ).matcher( SoFrom ).find())
			   SoFrom="0";
		   
		 if(SoTo.contains("*") && Pattern.compile( "[0-9]" ).matcher( SoTo ).find())
		 {
			 
		 }else if(!Pattern.compile( "[0-9]" ).matcher( SoTo ).find())
			   SoTo="0";
			   
		   SoFrom=SoFrom.replaceAll("[^0-9.]","");
		   SoTo=SoTo.replaceAll("[^0-9.]","");
		   
		   int len1=SoFrom.length();
		   int len2=SoTo.length();
		   
		   if(len1<len2)
		   {
			   int diff=len2-len1;
			   for(int i=0;i<diff;i++)
				   SoFrom=SoFrom+"0";
			   
		   }
		   
		   int digits = Integer.parseInt(SoFrom);
		   int digits1 = Integer.parseInt(SoTo);

//		   System.out.println("\n Given Number is :"+digits+"   and  digits1="+digits1);
		 
		  
		   if(digits > digits1)
			   chkRes=false;
		   else
			   chkRes=true;
		  
		return chkRes;
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
	
	static int getmonthInt(String month)
	 {
		int mn=1;
		 if(month.equals("Jan"))
			 return 1;
		 if(month.equals("Feb"))
			 return 2;
		 if(month.equals("Mar"))
			 return 3;
		 if(month.equals("Apr"))
			 return 4;
		 if(month.equals("May"))
			 return 5;
		 if(month.equals("Jun"))
			 return 6;
		 if(month.equals("Jul"))
			 return 7;
		 if(month.equals("Aug"))
			 return 8;
		 if(month.equals("Sep"))
			 return 9;
		 if(month.equals("Oct"))
			 return 10;
		 if(month.equals("Nov"))
			 return 11;
		 if(month.equals("Sec"))
			 return 12;
		 
		return mn;
		 
	 }
	public Text getText_SoNumber() {
		return text_SoNumberFrom;
	}
	public void setText_SoNumber(Text text_SoNumber) {
		this.text_SoNumberFrom = text_SoNumber;
	}
	public DateTime getDateTime_fromDate() {
		return dateTime_fromDate;
	}
	public void setDateTime_fromDate(DateTime dateTime_fromDate) {
		this.dateTime_fromDate = dateTime_fromDate;
	}
	public DateTime getDateTime_ToDate() {
		return dateTime_ToDate;
	}
	public void setDateTime_ToDate(DateTime dateTime_ToDate) {
		this.dateTime_ToDate = dateTime_ToDate;
	}
	public DateTime getDateTime_fromTime() {
		return dateTime_fromTime;
	}
	public void setDateTime_fromTime(DateTime dateTime_fromTime) {
		this.dateTime_fromTime = dateTime_fromTime;
	}
	public DateTime getDateTime_toTime() {
		return dateTime_toTime;
	}
	public void setDateTime_toTime(DateTime dateTime_toTime) {
		this.dateTime_toTime = dateTime_toTime;
	}
	public void setOldData(LinkedHashMap<String, String> inputCriteriaList) {
		// TODO Auto-generated method stub
		
		String SO_numberFrom=inputCriteriaList.get("SO_NUMBER_FROM");
		String SO_numberTo=inputCriteriaList.get("SO_NUMBER_TO");
		String fromDate=inputCriteriaList.get("FROM_DATE");
		String toDate=inputCriteriaList.get("TO_DATE");
		
		text_SoNumberFrom.setText(SO_numberFrom);
		text_SoNumberTo.setText(SO_numberTo);
		
		//From date
		String[] fromdateArr=fromDate.split(" ");
		int day = Integer.parseInt(fromdateArr[0].split("-")[0]);
		int month = getmonthInt(fromdateArr[0].split("-")[1]);
		int year = Integer.parseInt(fromdateArr[0].split("-")[2]);
		
		int hours= Integer.parseInt(fromdateArr[1].split(":")[0]);
		int minutes= Integer.parseInt(fromdateArr[1].split(":")[1]);
	
		dateTime_fromDate.setDate(year, (month-1), day);
		dateTime_fromTime.setTime(hours, minutes, 00);
		
		//To date
		String[] toDateArr=toDate.split(" ");
		int day1 = Integer.parseInt(toDateArr[0].split("-")[0]);
		int month1 = getmonthInt(toDateArr[0].split("-")[1]);
		int year1 = Integer.parseInt(toDateArr[0].split("-")[2]);
		
		int hours1= Integer.parseInt(toDateArr[1].split(":")[0]);
		int minutes1= Integer.parseInt(toDateArr[1].split(":")[1]);
		
		
		dateTime_ToDate.setDate(year1, (month1-1), day1);
		dateTime_toTime.setTime(hours1, minutes1, 00);
		
		
		System.out.println("fromDate=="+fromDate);
		System.out.println("to date=="+toDate);
		
	}
		  
	}