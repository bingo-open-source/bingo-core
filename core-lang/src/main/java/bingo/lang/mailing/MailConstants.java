/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang.mailing;

final class MailConstants {
	public static final String	MAIL_DEBUG	                = "mail.debug";
	public static final String	MAIL_HOST	                = "mail.smtp.host";
	public static final String	MAIL_PORT	                = "mail.smtp.port";
	public static final String	MAIL_SMTP_FROM	            = "mail.smtp.from";
	public static final String	MAIL_SMTP_AUTH	            = "mail.smtp.auth";
	public static final String	MAIL_SMTP_USER	            = "mail.smtp.user";
	public static final String	MAIL_SMTP_PASSWORD	        = "mail.smtp.password";
	public static final String	MAIL_SMTP_CONNECTIONTIMEOUT	= "mail.smtp.connectiontimeout";
	public static final String	MAIL_SMTP_TIMEOUT	        = "mail.smtp.timeout";
	public static final String	MAIL_TRANSPORT_PROTOCOL	    = "mail.transport.protocol";

	public static final String	SMTP	                    = "smtp";

	public static final String	TEXT_HTML	                = "text/html";
	public static final String	TEXT_PLAIN	                = "text/plain";
	
	static final char[] ADDRESS_SEPERATORS = new char[]{',',';'};
}