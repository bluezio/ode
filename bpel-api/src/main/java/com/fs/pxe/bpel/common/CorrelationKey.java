/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.bpel.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.fs.utils.ArrayUtils;


/**
 * <p>Message correlation key. Correlation keys are used to match up incoming
 * messages with a particular process <em>instance</em>. The basic procedure
 * is to generate and save a correlation key when a <code>receive</code> or
 * <code>pick</em> activity is activated, and then to match incoming messages
 * against all correlation keys so saved, finally associating the message with
 * the process instance that had the matching correlation key. In reality this
 * process is somewhat more complicated as pains must be taken to avoid race
 * conditions and to make the matching efficient.</p>
 *
 * <p>The correlation keys used in the above process consists of a collection
 * of name-value pairs, with the name corresponding to a property name (as
 * defined using the <code>&lt;property&gt;</code> element of the BPEL process
 * document) and with the value corresponding to the value of said property as
 * obtained from a message by means of a property alias (as defined using
 * the <code>&lt;propertyAlias;&gt</code> BPEL process document element).
 * </p>
 */
public class CorrelationKey implements Serializable{

	private static final long serialVersionUID = 1L;

  /** CorrelationSet identifier. */
  private int _csetId;

  /** Key values. */
  private final String _keyValues[];


  /** 
   * Constructor.
   * @param csetId correlation set identifier
   * @param keyValues correlation key values
   */
  public CorrelationKey(int csetId, String[] keyValues) {
    _csetId = csetId;
    _keyValues = keyValues;
  }

  public CorrelationKey(String canonicalForm) {
    int firstTilde = canonicalForm.indexOf('~');
    _csetId = Integer.parseInt(canonicalForm.substring(0,firstTilde == -1 ? canonicalForm.length() : firstTilde));

    if (firstTilde != -1) {
      List<String> keys = new ArrayList<String>();
      char chars[] = canonicalForm.toCharArray();
      StringBuffer work = new StringBuffer();
      for (int i = firstTilde+1; i < chars.length; ++i) {
        boolean isLast = (i == chars.length - 1);
        if (chars[i] == '~' && !isLast && chars[i+1] == '~') {
          work.append(chars[i++]);
        }
        else if (chars[i] == '~') {
          keys.add(work.toString());
          work = new StringBuffer();
        } else  {
          work.append(chars[i]);
        }
      }
      keys.add(work.toString());
      _keyValues = new String[keys.size()];
      keys.toArray(_keyValues);
    } else {
      _keyValues = new String[0];
    }
  }

  /** Return the OCorrelation id for the correlation set */
  public int getCSetId(){
  	return _csetId;
  }
  
  /** Return the values for the correlation set */
  public String[] getValues(){
  	return _keyValues;
  }
  

  /**
   * Check if this key matches any member in a set of keys.
   *
   * @param keys set of keys to match against
   *
   * @return <code>true</code> if one of the keys in the set
   *         <code>equals(..)</code> this key, <code>false</code> otherwise
   */
  public boolean isMatch(CorrelationKey[] keys) {
    for (CorrelationKey key : keys)
      if (key.equals(this)) {
        return true;
      }

    return false;
  }

  /**
   * Equals comperator method.
   *
   * @param o <code>CorrelationKey</code> object to compare with
   *
   * @return <code>true</code> if the given object
   */
  public boolean equals(Object o) {
    CorrelationKey okey = (CorrelationKey)o;

    if (okey == null || okey._csetId != _csetId || okey._keyValues.length != _keyValues.length)
      return false;

    for (int i = 0; i < _keyValues.length; ++i)
      if (!_keyValues[i].equals(okey._keyValues[i]))
        return false;

    return true;
  }

  /**
   * Generate a hash code from the hash codes of the elements.
   * @see HashMap#hashCode
   * @see Object#hashCode
   */
  public int hashCode() {
    int hashCode = _csetId;
    for (String _keyValue : _keyValues) hashCode ^= _keyValue.hashCode();
    return hashCode;
  }

  public List<String> toCanonicalList() {
    ArrayList<String> ret = new ArrayList<String>(_keyValues.length+1);
    ret.add(((Integer)_csetId).toString());
    for (String i : _keyValues)
      ret.add(i);
    return ret;
  }
  /**
   * @see Object#toString
   */
  public String toString() {
    StringBuffer buf = new StringBuffer("{CorrelationKey ");
    buf.append("setId=");
    buf.append(_csetId);
    buf.append(", values=");
    buf.append(ArrayUtils.makeCollection(ArrayList.class,_keyValues));
    buf.append('}');

    return buf.toString();
  }

}
