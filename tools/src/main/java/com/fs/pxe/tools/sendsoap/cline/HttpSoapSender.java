/*
 * File:      $RCSfile$
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package com.fs.pxe.tools.sendsoap.cline;

import com.fs.pxe.tools.ToolMessages;
import com.fs.utils.StreamUtils;
import com.fs.utils.cli.*;
import com.fs.utils.msg.CommonMessages;
import com.fs.utils.msg.MessageBundle;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * <p>
 * Commandline tool to send the contents of a (binary) file to a URL.
 * </p>
 */
public class HttpSoapSender extends BaseCommandlineTool {

  private static final int RETURN_BAD_URL = 2;
  private static final int RETURN_SEND_ERROR = 3;
  private static final int RETURN_CANT_READ = 4;
  private static final int RETURN_CANT_WRITE = 5;
  
  private static Pattern SEQ = Pattern.compile("\\$sequence\\$");

  private static final ToolMessages MESSAGES = MessageBundle.getMessages(ToolMessages.class);
  private static final CommonMessages COMMON = MessageBundle.getMessages(CommonMessages.class);
  
  private static final Argument URL_A = new Argument("url","the URL to send the SOAP to.",false);
  private static final Argument FILE_A = new Argument("file","the file that contains the SOAP to send.",false);
  
  private static final FlagWithArgument OUTFILE_FWA = new FlagWithArgument("o","outfile",
      "a file to write the output to (instead of standard out).",true);
    
  private static final Fragments CLINE = new Fragments(new CommandlineFragment[] {
     OUTFILE_FWA,URL_A, FILE_A 
  });
  
  private static final String SYNOPSIS = 
    "send the contents of a file to a URL as a SOAP request and print the response (if any) to the console or a file.";
  
  private static void doSend(URL u, InputStream is, OutputStream os)
    throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
    StreamUtils.copy(bos, is);
    String now = Long.toString(System.currentTimeMillis());
    int c = 1;
    String data = new String(bos.toByteArray());
    Matcher m = SEQ.matcher(data);
    StringBuffer sb = new StringBuffer(8192);
    while (m.find()) {
        m.appendReplacement(sb, now + "-" + c++);
    }
    m.appendTail(sb);
    HttpClient httpClient = new HttpClient();
    PostMethod httpPostMethod = new PostMethod(u.toExternalForm());
    // TODO what about this header?
    //_httpPostMethod.setRequestHeader("SOAPAction", writer.getSoapAction());
    httpPostMethod.setRequestHeader("Content-Type", "text/xml");
    httpPostMethod.setRequestEntity(new StringRequestEntity(sb.toString()));
    httpClient.executeMethod(httpPostMethod);
    String response = httpPostMethod.getResponseBodyAsString();
    if (response != null) {
    	os.write(response.getBytes());
      os.write("\n".getBytes());
    }
  }
  
  public static void main(String[] argv) {
    if (argv.length == 0 || HELP.matches(argv)) {
      ConsoleFormatter.printSynopsis(getProgramName(),SYNOPSIS,new Fragments[] {
        CLINE,HELP});
      System.exit(0);
    } else if (!CLINE.matches(argv)) {
      consoleErr("INVALID COMMANDLINE: Try \"" + getProgramName() + " -h\" for help.");
      System.exit(-1);
    }
    OutputStream os = null;
    if (OUTFILE_FWA.isSet()) {
      String outfile = OUTFILE_FWA.getValue();
      File f = new File(outfile);
      try {
        os = new FileOutputStream(f);
      } catch (FileNotFoundException fnfe) {
        consoleErr(COMMON.msgCannotWriteToFile(outfile));
        System.exit(RETURN_CANT_WRITE);
      }
    } else {
      os = System.out;
    }
            
    URL u = null;
    try {
      u = new URL(URL_A.getValue());
    } catch (MalformedURLException mue) {
      consoleErr(MESSAGES.msgBadUrl(URL_A.getValue(),mue.getMessage()));
      System.exit(RETURN_BAD_URL);
    }
    
    InputStream is = null;
    
    String src = FILE_A.getValue();
    if (src.equals("-")) {
      is = System.in;
    } else {
      File f = new File(src);
      try {
        is = new FileInputStream(f);
      } catch (FileNotFoundException fnfe) {
        consoleErr(COMMON.msgCannotReadFromFile(src));
        System.exit(RETURN_CANT_READ);
      }
    }
    
    initLogging();
    try{
      doSend(u,is,os);
    } catch (IOException ioe) {
      consoleErr(MESSAGES.msgIoErrorOnSend(ioe.getMessage()));
      System.exit(RETURN_SEND_ERROR);
    }
  }
}
