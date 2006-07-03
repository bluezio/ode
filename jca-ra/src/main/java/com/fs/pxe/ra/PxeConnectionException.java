/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.ra;

import javax.resource.ResourceException;

/**
 * Exception thrown in case of PXE communication failures.
 */
public class PxeConnectionException extends ResourceException {
  /**
   * Constructor.
   * @param message the message
   */
  PxeConnectionException(String message) {
    super(message);
  }
  
  /**
   * Constructor.
   * @param message the message
   * @param cause a <code>Throwable</code> cause for this exception
   */
  PxeConnectionException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }

  /**
   * Constructor.
   * @param cause a <code>Throwable</code> cause for this exception
   */
  PxeConnectionException(Throwable cause) {
    super(cause.getMessage());
    initCause(cause);
  }
}
