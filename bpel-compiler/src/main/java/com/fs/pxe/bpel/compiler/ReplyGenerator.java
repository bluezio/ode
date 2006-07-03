/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.bpel.compiler;

import com.fs.pxe.bom.api.Activity;
import com.fs.pxe.bom.api.Correlation;
import com.fs.pxe.bpel.capi.CompilationException;
import com.fs.pxe.bpel.o.*;
import com.fs.utils.msg.MessageBundle;

import java.util.Iterator;


/**
 * Generates code for <code>&lt;reply&gt;</code> activities.
 */
class ReplyGenerator extends DefaultActivityGenerator  {

  private static final CommonCompilationMessages _cmsgsGeneral =
    MessageBundle.getMessages(CommonCompilationMessages.class);

  private static final ReplyGeneratorMessages __cmsgsLocal =
    MessageBundle.getMessages(ReplyGeneratorMessages.class);

  public OActivity newInstance(Activity src) {
    return new OReply(_context.getOProcess());
  }

  public void compile(OActivity output, Activity src) {
    com.fs.pxe.bom.api.ReplyActivity replyDef = (com.fs.pxe.bom.api.ReplyActivity) src;
    OReply oreply = (OReply) output;

    oreply.isFaultReply = replyDef.getFaultName() != null;
    oreply.partnerLink = _context.resolvePartnerLink(replyDef.getPartnerLink());
    oreply.messageExchangeId = replyDef.getMessageExchangeId();
    if (replyDef.getVariable() != null) {
      oreply.variable = _context.resolveVariable(replyDef.getVariable());
      if (!(oreply.variable.type instanceof OMessageVarType))
        throw new CompilationException(_cmsgsGeneral.errMessageVariableRequired(oreply.variable.name));
    }

    // The portType on the reply is not necessary, so we check its validty only when present.
    if (replyDef.getPortType() != null && !oreply.partnerLink.myRolePortType.getQName().equals(replyDef.getPortType()))
      throw new CompilationException(_cmsgsGeneral.errPortTypeMismatch(replyDef.getPortType(),oreply.partnerLink.myRolePortType.getQName()));

    oreply.operation = _context.resolveMyRoleOperation(oreply.partnerLink, replyDef.getOperation());
    if (oreply.operation.getOutput() == null)
      throw new CompilationException(_cmsgsGeneral.errTwoWayOperationExpected(oreply.operation.getName()));

    if (oreply.isFaultReply) {
      // This is a bit fishy, WSDL fault names are not QNAMEs, but BPEL fault names are...
      if (replyDef.getFaultName().getNamespaceURI().equals(oreply.partnerLink.myRolePortType.getQName().getNamespaceURI()))
        oreply.fault = oreply.operation.getFault(replyDef.getFaultName().getLocalPart());
      if (oreply.fault == null)
        throw new CompilationException(__cmsgsLocal.errUndeclaredFault(replyDef.getFaultName().getLocalPart(), oreply.operation.getName()));
      if (oreply.variable != null && !((OMessageVarType)oreply.variable.type).messageType.equals(oreply.fault.getMessage().getQName()))
        throw new CompilationException(_cmsgsGeneral.errVariableTypeMismatch(oreply.variable.name, oreply.fault.getMessage().getQName(), ((OMessageVarType)oreply.variable.type).messageType));
    } else /* !oreply.isFaultReply */ {
      assert oreply.fault == null;
      if (oreply.variable == null)
        throw new CompilationException(__cmsgsLocal.errOutputVariableMustBeSpecified());
      if (!((OMessageVarType)oreply.variable.type).messageType.equals(oreply.operation.getOutput().getMessage().getQName()))
        throw new CompilationException(_cmsgsGeneral.errVariableTypeMismatch(oreply.variable.name, oreply.operation.getOutput().getMessage().getQName(),((OMessageVarType)oreply.variable.type).messageType));
    }

    for (Iterator<Correlation> i = replyDef.getCorrelations().iterator(); i.hasNext(); ) {
      Correlation correlation = i.next();
      OScope.CorrelationSet cset = _context.resolveCorrelationSet(correlation.getCorrelationSet());

      switch (correlation.getInitiate()) {
        case Correlation.INITIATE_NO:
          oreply.assertCorrelations.add(cset);
          break;
        case Correlation.INITIATE_YES:
          oreply.initCorrelations.add(cset);
          break;
        default:
          // TODO: Make error for this.
          throw new AssertionError();
      }

      for (Iterator<OProcess.OProperty> j = cset.properties.iterator(); j.hasNext(); ) {
        OProcess.OProperty property = j.next();
        // Force resolution of alias, to make sure that we have one for this variable-property pair.
        _context.resolvePropertyAlias(oreply.variable, property.name);
      }
    }
  }
}
