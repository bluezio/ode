/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.bom.impl.nodes;

import com.fs.pxe.bom.api.Activity;
import com.fs.pxe.bom.api.Expression;
import com.fs.pxe.bom.api.OnAlarm;
import com.fs.utils.NSContext;

/**
 * BPEL object model representation of an <code>onAlarm</code> decleration.
 */
public class OnAlarmImpl extends ScopeImpl implements OnAlarm {

  private static final long serialVersionUID = -1L;

  private Expression _for;
  private Expression _until;
  private Expression _repeatEvery;
  private Activity _activity;

  /**
   * Hack, could be declared in SCOPE, or PICK
   */
  private Object _declaredIn;

  public OnAlarmImpl() {
    super();
  }

  public String getType() {
    return "onAlarm";
  }

  public OnAlarmImpl(NSContext nsContext) {
    super(nsContext);
  }

  public Activity getActivity() {
    return _activity;
  }

  public void setActivity(com.fs.pxe.bom.api.Activity activity) {
    _activity = activity;
  }

  public void setFor(Expression for1) {
    _for = for1;
  }

  public Expression getFor() {
    return _for;
  }

  public void setUntil(Expression until) {
    _until = until;
  }

  public Expression getUntil() {
    return _until;
  }

	public Expression getRepeatEvery() {
		return _repeatEvery;
  }
  
	public void setRepeatEvery(Expression repeatEvery) {
		_repeatEvery = repeatEvery;
	}
  
  void setDeclaredIn(Object declaredIn) {
    _declaredIn = declaredIn;
  }
}
