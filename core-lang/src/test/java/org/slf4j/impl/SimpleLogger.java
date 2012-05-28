/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.slf4j.impl;

import java.util.Date;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import bingo.lang.Dates;

/**
 * A simple (and direct) implementation that logs messages of level INFO or
 * higher on the console (<code>System.err<code>).
 * 
 * <p>The output includes the relative time in milliseconds, thread
 * name, the level, logger name, and the message followed by the line
 * separator for the host.  In log4j terms it amounts to the "%r [%t]
 * %level %logger - %m%n" pattern. </p>
 * 
 * <p>Sample output follows.</p>
<pre>
176 [main] INFO examples.Sort - Populating an array of 2 elements in reverse order.
225 [main] INFO examples.SortAlgo - Entered the sort method.
304 [main] INFO examples.SortAlgo - Dump of integer array:
317 [main] INFO examples.SortAlgo - Element [0] = 0
331 [main] INFO examples.SortAlgo - Element [1] = 1
343 [main] INFO examples.Sort - The next log statement should be an error message.
346 [main] ERROR examples.SortAlgo - Tried to dump an uninitialized array.
        at org.log4j.examples.SortAlgo.dump(SortAlgo.java:58)
        at org.log4j.examples.Sort.main(Sort.java:64)
467 [main] INFO  examples.Sort - Exiting main method.
</pre>
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class SimpleLogger extends MarkerIgnoringBase {

  private static final long serialVersionUID = -6560244151660620173L;

  /**
   * Mark the time when this class gets loaded into memory.
   */
  public static final String LINE_SEPARATOR = System
      .getProperty("line.separator");
  
  public static String TRACE_STR = "TRACE";
  public static String DEBUG_STR = "DEBUG";
  public static String INFO_STR  = "INFO";
  public static String WARN_STR  = "WARN";
  public static String ERROR_STR = "ERROR";

  /**
   * Package access allows only {@link SimpleLoggerFactory} to instantiate
   * SimpleLogger instances.
   */
  SimpleLogger(String name) {
    this.name = name;
  }

  /**
   * Always returns false.
   * 
   * @return always false
   */
  public boolean isTraceEnabled() {
    return SimpleContext.get().isTraceEnabled;
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the TRACE
   * level.
   */
  public void trace(String msg) {
	  SimpleContext.get().reset();
	  if(isTraceEnabled()){
		  log(TRACE_STR,msg,null);  
	  }
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the TRACE
   * level.
   */
  public void trace(String format, Object param1) {
    // NOP
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the TRACE
   * level.
   */
  public void trace(String format, Object param1, Object param2) {
    // NOP
  }

  public void trace(String format, Object[] argArray) {
	  SimpleContext.get().reset();
	  if(isTraceEnabled()){
		  formatAndLog(TRACE_STR, format, argArray);  
	  }
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the TRACE
   * level.
   */
  public void trace(String msg, Throwable t) {
	  SimpleContext.get().reset();
	  if(isTraceEnabled()){
		  log(TRACE_STR, msg, t);  
	  }
  }

  /**
   * Always returns false.
   * 
   * @return always false
   */
  public boolean isDebugEnabled() {
	  return SimpleContext.get().isDebugEnabled;
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the DEBUG
   * level.
   */
  public void debug(String msg) {
	  SimpleContext.get().reset();
	  if(isDebugEnabled()){
		  log(DEBUG_STR,msg,null);  
	  }
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the DEBUG
   * level.
   */
  public void debug(String format, Object param1) {
    // NOP
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the DEBUG
   * level.
   */
  public void debug(String format, Object param1, Object param2) {
    // NOP
  }

  public void debug(String format, Object[] argArray) {
	  SimpleContext.get().reset();
	  if(isDebugEnabled()){
		  formatAndLog(DEBUG_STR, format, argArray);  
	  }
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for the DEBUG
   * level.
   */
  public void debug(String msg, Throwable t) {
	  SimpleContext.get().reset();
	  if(isDebugEnabled()){
		  log(DEBUG_STR, msg, t);  
	  }
  }

  /**
   * This is our internal implementation for logging regular (non-parameterized)
   * log messages.
   * 
   * @param level
   * @param message
   * @param t
   */
  private void log(String level, String message, Throwable t) {
    StringBuffer buf = new StringBuffer();

    buf.append("[");
    buf.append(Dates.format(new Date(System.currentTimeMillis()),"HH:mm:ss.SSS"));
    buf.append("] ");
    
    buf.append("[");
    buf.append(Thread.currentThread().getName());
    buf.append("] ");

    buf.append(level);
    buf.append(" ");

    buf.append(name);
    buf.append(" - ");

    buf.append(message);

    buf.append(LINE_SEPARATOR);

    System.out.print(buf.toString());

    SimpleContext.get().reset().log(message,t);
    
    if (t != null) {
      t.printStackTrace(System.err);
    }
    
    System.out.flush();
  }

  /**
   * For formatted messages, first substitute arguments and then log.
   * 
   * @param level
   * @param format
   * @param param1
   * @param param2
   */
  private void formatAndLog(String level, String format, Object arg1,
      Object arg2) {
    FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
    log(level, tp.getMessage(), tp.getThrowable());
  }

  /**
   * For formatted messages, first substitute arguments and then log.
   * 
   * @param level
   * @param format
   * @param argArray
   */
  private void formatAndLog(String level, String format, Object[] argArray) {
    FormattingTuple tp = MessageFormatter.arrayFormat(format, argArray);
    log(level, tp.getMessage(), tp.getThrowable());
  }

  /**
   * Always returns true.
   */
  public boolean isInfoEnabled() {
	  return SimpleContext.get().isInfoEnabled;
  }

  /**
   * A simple implementation which always logs messages of level INFO according
   * to the format outlined above.
   */
  public void info(String msg) {
	  SimpleContext.get().reset();
	  if(isInfoEnabled()){
		  log(INFO_STR, msg, null);	  
	  }
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  public void info(String format, Object arg) {
	  SimpleContext.get().reset();
	  if(isInfoEnabled()){
		  formatAndLog(INFO_STR, format, arg, null);	  
	  }
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  public void info(String format, Object arg1, Object arg2) {
	  SimpleContext.get().reset();
	  if(isInfoEnabled()){
		  formatAndLog(INFO_STR, format, arg1, arg2);	  
	  }
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * INFO according to the format outlined above.
   */
  public void info(String format, Object[] argArray) {
	  SimpleContext.get().reset();
	  if(isInfoEnabled()){
		  formatAndLog(INFO_STR, format, argArray);	  
	  }
  }

  /**
   * Log a message of level INFO, including an exception.
   */
  public void info(String msg, Throwable t) {
	  SimpleContext.get().reset();
	  if(isInfoEnabled()){
		  log(INFO_STR, msg, t);	  
	  }
    
  }

  /**
   * Always returns true.
   */
  public boolean isWarnEnabled() {
	  return SimpleContext.get().isWarnEnabled;
  }

  /**
   * A simple implementation which always logs messages of level WARN according
   * to the format outlined above.
   */
  public void warn(String msg) {
	  SimpleContext.get().reset();
	  if(isWarnEnabled()){
		  log(WARN_STR, msg, null);	  
	  }
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  public void warn(String format, Object arg) {
    formatAndLog(WARN_STR, format, arg, null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  public void warn(String format, Object arg1, Object arg2) {
    formatAndLog(WARN_STR, format, arg1, arg2);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * WARN according to the format outlined above.
   */
  public void warn(String format, Object[] argArray) {
	  SimpleContext.get().reset();
	  if(isWarnEnabled()){
		  formatAndLog(WARN_STR, format, argArray);	  
	  }
  }

  /**
   * Log a message of level WARN, including an exception.
   */
  public void warn(String msg, Throwable t) {
	  SimpleContext.get().reset();
	  if(isWarnEnabled()){
		  log(WARN_STR, msg, t);	  
	  }
  }

  /**
   * Always returns true.
   */
  public boolean isErrorEnabled() {
	  return SimpleContext.get().isErrorEanbled;
  }

  /**
   * A simple implementation which always logs messages of level ERROR according
   * to the format outlined above.
   */
  public void error(String msg) {
	  SimpleContext.get().reset();
	  if(isErrorEnabled()){
		  log(ERROR_STR, msg, null);	  
	  }
  }

  /**
   * Perform single parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  public void error(String format, Object arg) {
    formatAndLog(ERROR_STR, format, arg, null);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  public void error(String format, Object arg1, Object arg2) {
    formatAndLog(ERROR_STR, format, arg1, arg2);
  }

  /**
   * Perform double parameter substitution before logging the message of level
   * ERROR according to the format outlined above.
   */
  public void error(String format, Object[] argArray) {
	  SimpleContext.get().reset();
	  if(isErrorEnabled()){
		  formatAndLog(ERROR_STR, format, argArray);	  
	  }
  }

  /**
   * Log a message of level ERROR, including an exception.
   */
  public void error(String msg, Throwable t) {
	  SimpleContext.get().reset();
	  if(isErrorEnabled()){
		  log(ERROR_STR, msg, t);	  
	  }
  }
}
