package com.teamcenter.tdps.handlers;



import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.tdps.view.TDPS_FillCriteriaUI;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class QA_ledgerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		AbstractAIFUIApplication application = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) application.getSession();
		
		TDPS_FillCriteriaUI ui=new TDPS_FillCriteriaUI(session);
		ui.fillCriteriaDialog(null,false,null);
		//callSOA(session);
		
		return null;
	
	}
	
	
}
