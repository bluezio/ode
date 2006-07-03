/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */

package com.fs.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class ISO8601DateParserTest extends TestCase {

  public void test1() throws ParseException {
    String in = "2004-05-31T09:19:31-05:00";

    Date d = ISO8601DateParser.parse(in);
    assertDate(d, 2004, 5, 31, 9, 19, 31, "GMT-5");
  }

  public void test2() throws ParseException {
    String in = "2004-06-23T17:25:31-00:00";
    Date d = ISO8601DateParser.parse(in);
    assertDate(d, 2004, 6, 23, 17, 25, 31, "GMT");
  }

  public void test3() throws ParseException {
    String in = "2004-06-23T17:25-00:00";
    Date d = ISO8601DateParser.parse(in);
    assertDate(d, 2004, 6, 23, 17, 25, 0, "GMT");
  }

  public void test4() throws ParseException {
    String in = "2002-10-02T10:00:00-05:00";
    Date d = ISO8601DateParser.parse(in);
    assertDate(d, 2002, 10, 2, 10, 0, 0, "GMT-5");
  }

  private void assertDate(Date d, int year, int month, int day,
                         int hour, int minute, int second, String tz) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.setTimeZone(TimeZone.getTimeZone(tz));

    assertEquals(year, cal.get(Calendar.YEAR));
    assertEquals(month - 1, cal.get(Calendar.MONTH));
    assertEquals(day, cal.get(Calendar.DAY_OF_MONTH));
    assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute, cal.get(Calendar.MINUTE));
    assertEquals(second, cal.get(Calendar.SECOND));
  }

}
