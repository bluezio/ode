package com.fs.pxe.jbi;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;

public interface JbiMessageExchangeProcessor {

  public void onJbiMessageExchange(MessageExchange mex) 
    throws MessagingException;
}
