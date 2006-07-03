/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.sax.fsa.bpel_2_0;

import com.fs.pxe.bom.api.Expression;
import com.fs.pxe.bom.impl.nodes.ExpressionImpl;
import com.fs.pxe.sax.fsa.*;
import com.fs.sax.evt.SaxEvent;
import com.fs.sax.evt.StartElement;
import com.fs.sax.evt.XmlAttributes;

class BpelExpressionState extends BaseBpelState {

  private static final StateFactory _factory = new Factory();
  private ExpressionImpl _expr;
	private DOMGenerator _domGenerator;
  
  BpelExpressionState(StartElement se,ParseContext pc) throws ParseException {
    super(pc);
    
    XmlAttributes attr = se.getAttributes();
    if(attr.hasAtt("expressionLanguage")){
      _expr = new ExpressionImpl(attr.getValue("expressionLanguage"));
    }else{
      _expr = new ExpressionImpl();
    }
    _expr.setNamespaceContext(se.getNamespaceContext());
    _expr.setLineNo(se.getLocation().getLineNumber());

    _domGenerator = new DOMGenerator();
  }
  
  Expression getExpression(){
    return _expr;
  }
  
  /**
   * @see com.fs.pxe.sax.fsa.State#handleSaxEvent(com.fs.sax.evt.SaxEvent)
   */
  public void handleSaxEvent(SaxEvent se) throws ParseException {
    _domGenerator.handleSaxEvent(se);
  }
  /**
   * @see com.fs.pxe.sax.fsa.State#done()
   */
  public void done(){
    _expr.setNode(_domGenerator.getRoot());
  }
  
  /**
   * @see com.fs.pxe.sax.fsa.State#getFactory()
   */
  public StateFactory getFactory() {
    return _factory;
  }

  /**
   * @see com.fs.pxe.sax.fsa.State#getType()
   */
  public int getType() {
    return BPEL_EXPRESSION;
  }
  
  static class Factory implements StateFactory {
    
    public State newInstance(StartElement se, ParseContext pc) throws ParseException {
      return new BpelExpressionState(se,pc);
    }
  }
}
