/* InitPropertyBindingImpl

	Purpose:
		
	Description:
		
	History:
		Aug 1, 2011 2:43:33 PM, Created by dennis

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.bind.impl;

import java.util.Map;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Form;
import org.zkoss.bind.FormExt;
import org.zkoss.bind.sys.BindEvaluatorX;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ConditionType;
import org.zkoss.bind.sys.InitFormBinding;
import org.zkoss.bind.sys.InitPropertyBinding;
import org.zkoss.xel.ExpressionX;
import org.zkoss.zk.ui.Component;

/**
 * Implementation of {@link InitPropertyBinding}.
 * @author Dennis
 * @since 6.0.0
 */
public class InitFormBindingImpl extends FormBindingImpl implements InitFormBinding {
	private static final long serialVersionUID = 1463169907348730644L;
	@Override
	protected boolean ignoreTracker(){
		//init only loaded once, so it don't need to add to tracker.
		return true;
	}
	
	public InitFormBindingImpl(Binder binder, Component comp, String formId, String initExpr, Map<String, Object> bindingArgs) {
		super(binder, comp, formId, initExpr, ConditionType.PROMPT, null, bindingArgs);
	}

	public void load(BindContext ctx) {
		final Component comp = getComponent();
		final BindEvaluatorX eval = getBinder().getEvaluatorX();
		final Binder binder = getBinder();
		//get data from property
		Object value = eval.getValue(ctx, comp, _accessInfo.getProperty());
		if(!(value instanceof Form)){
			final Form form = getFormBean();
			if(form instanceof FormExt){
				FormExt fex = (FormExt)form;
				//ZK-1005 ZK 6.0.1 validation fails on nested bean
				//sets the last loaded bean express of the form
				comp.setAttribute(BinderImpl.LOAD_FORM_EXPRESSION, getPropertyString());
				
				for (String field : fex.getLoadFieldNames()) {
					final ExpressionX expr = getFieldExpression(eval, field);
					if (expr != null) {
						final Object fieldval = eval.getValue(ctx, comp, expr);
						//ZK-911. Save into Form bean via expression(so will use form's AccessFieldName)
						final ExpressionX formExpr = getFormExpression(eval, field);
						eval.setValue(null, comp, formExpr, fieldval);
					}
				}
				fex.resetDirty(); //initial loading, mark form as clean
			}
			binder.notifyChange(form, "."); //notify change of fx and fx.*
			if(form instanceof FormExt){//after notify form
				binder.notifyChange(((FormExt)form).getStatus(), ".");//notify change of fxStatus and fxStatus.*
			}
		}else{
			((BinderCtrl)binder).storeForm(getComponent(), getFormId(), (Form)value);
		}
	}
}
