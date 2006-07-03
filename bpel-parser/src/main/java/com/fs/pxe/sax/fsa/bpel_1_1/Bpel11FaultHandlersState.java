/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.sax.fsa.bpel_1_1;

import com.fs.pxe.bom.api.FaultHandler;
import com.fs.pxe.bom.impl.nodes.FaultHandlerImpl;
import com.fs.pxe.sax.fsa.ParseContext;
import com.fs.pxe.sax.fsa.ParseException;
import com.fs.pxe.sax.fsa.State;
import com.fs.pxe.sax.fsa.StateFactory;
import com.fs.sax.evt.StartElement;

class Bpel11FaultHandlersState extends BaseBpelState {

  private static final StateFactory _factory = new Factory();
  private FaultHandlerImpl _f;
   
  private Bpel11FaultHandlersState(StartElement se, ParseContext pc) throws ParseException {
    super(pc);
    _f = new FaultHandlerImpl();
    _f.setNamespaceContext(se.getNamespaceContext());
    _f.setLineNo(se.getLocation().getLineNumber());
  }
  
  public FaultHandler getFaultHandler() {
    return _f;
  }
  
  /**
   * @see com.fs.pxe.sax.fsa.State#handleChildCompleted(com.fs.pxe.sax.fsa.State)
   */
  public void handleChildCompleted(State pn) throws ParseException {
    switch (pn.getType()) {
    case BPEL11_CATCH:
    case BPEL11_CATCHALL:
      _f.addCatch(((Bpel11CatchAllState)pn).getCatch());
      break;
    default:
      super.handleChildCompleted(pn);
    }
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
    return BPEL11_FAULTHANDLERS;
  }
  
  static class Factory implements StateFactory {
    
    public State newInstance(StartElement se, ParseContext pc) throws ParseException {
      return new Bpel11FaultHandlersState(se, null);
    }
  }
}
