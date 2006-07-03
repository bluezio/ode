/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.sax.fsa.bpel_1_1;

import com.fs.pxe.bom.api.CompensationHandler;
import com.fs.pxe.bom.impl.nodes.CompensationHandlerImpl;
import com.fs.pxe.sax.fsa.ParseContext;
import com.fs.pxe.sax.fsa.ParseException;
import com.fs.pxe.sax.fsa.State;
import com.fs.pxe.sax.fsa.StateFactory;
import com.fs.sax.evt.StartElement;

class Bpel11CompensationHandlerState extends BaseBpelState {

  private static final StateFactory _factory = new Factory();
  private CompensationHandlerImpl _c;
  
  Bpel11CompensationHandlerState(StartElement se, ParseContext pc) throws ParseException {
    super(pc);
    _c = new CompensationHandlerImpl();
    _c.setNamespaceContext(se.getNamespaceContext());
    _c.setLineNo(se.getLocation().getLineNumber());
  }
  
  public void handleChildCompleted(State pn) throws ParseException {
    if (pn instanceof ActivityStateI) {
      _c.setActivity(((ActivityStateI)pn).getActivity());
    } else {
      super.handleChildCompleted(pn);
    }
  }
  
  public CompensationHandler getCompensationHandler() {
    return _c;
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
    return BPEL11_COMPENSATIONHANDLER;
  }
  
  static class Factory implements StateFactory {
    
    public State newInstance(StartElement se, ParseContext pc) throws ParseException {
      return new Bpel11CompensationHandlerState(se,pc);
    }
  }
}
