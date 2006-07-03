/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.sax.fsa.bpel_2_0;

import com.fs.pxe.bom.api.Activity;
import com.fs.pxe.bom.impl.nodes.CompensateActivityImpl;
import com.fs.pxe.sax.fsa.ParseContext;
import com.fs.pxe.sax.fsa.ParseException;
import com.fs.pxe.sax.fsa.State;
import com.fs.pxe.sax.fsa.StateFactory;
import com.fs.sax.evt.StartElement;
import com.fs.sax.evt.XmlAttributes;

class BpelCompensateActivityState extends BpelBaseActivityState {

  private static final StateFactory _factory = new Factory();

  BpelCompensateActivityState(StartElement se, ParseContext pc) throws ParseException {
    super(se,pc);
  }
  
  protected Activity createActivity(StartElement se) throws ParseException {
    CompensateActivityImpl cai = new CompensateActivityImpl();
    XmlAttributes atts = se.getAttributes();
    if (atts.hasAtt("scope")) {
      cai.setScopeToCompensate(atts.getValue("scope"));
    }
    return cai;
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
    return BPEL_COMPENSATE;
  }
  
  static class Factory implements StateFactory {
    
    public State newInstance(StartElement se, ParseContext pc) throws ParseException {
      return new BpelCompensateActivityState(se,pc);
    }
  }
}
