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

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import bingo.lang.Charsets;
import bingo.lang.Converts;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Func1;
import bingo.lang.Objects;
import bingo.lang.Strings;
import bingo.lang.config.XmlProperties;
import bingo.lang.exceptions.ObjectNotFoundException;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;
import bingo.lang.xml.XmlDocument;
import bingo.lang.xml.XmlElement;

final class MailUtils {
	
	private static final Log log = LogFactory.get(MailUtils.class);

	static final String	INVALID_ENCODING	= "Encoding not accepted: %s";
	static final String	INVALID_RECIPIENT	= "Invalid TO address: %s";
	static final String	INVALID_REPLYTO	  = "Invalid REPLY TO address: %s";
	static final String	INVALID_SENDER	  = "Invalid FROM address: %s";

	static final String	MISSING_SENDER	  = "Email is not valid: missing sender";
	static final String	MISSING_RECIPIENT	= "Email is not valid: missing recipients";
	static final String	MISSING_SUBJECT	  = "Email is not valid: missing subject";
	static final String	MISSING_CONTENT	  = "Email is not valid: missing content body";
	
	static MailConfig loadFromXml(String xmlResourceLocation) {
		Resource res = Resources.getResource(xmlResourceLocation);
		
		if(!res.exists()){
			throw new ObjectNotFoundException("xml resource '{0}' not found",xmlResourceLocation);
		}
		
		XmlDocument doc  = XmlDocument.load(res);
		XmlElement  root = doc.rootElement();

		String host     = root.requiredChildElementText("host");
		int    port     = Converts.toInt(root.childElementText("port"));
		String auth     = root.childElementText("auth");
		String username = root.childElementText("username");
		String password = root.childElementText("password");
		String charset  = root.childElementText("charset");
		boolean debug  = Converts.toBoolean(root.childElementText("debug"));
		int connectionTimeout = Converts.toInt(root.childElementText("connectionTimeout"));
		int socketTimeout = Converts.toInt(root.childElementText("socketTimeout"));

		Recipient from = null;
		
		XmlElement fromElement = root.childElement("from");
		
		if(null != fromElement){
			String name    = null;
			String address = null;
			
			XmlElement addressElement = fromElement.childElement("address");
			
			if(null != addressElement){
				address = addressElement.text();
				name    = fromElement.childElementText("name");
			}else{
				address = fromElement.text();
			}
			
			if(!Strings.isEmpty(address)){
				from = new Recipient(name, address);	
			}
		}
		
		boolean isAuth = !Strings.isEmpty(username) && !Strings.isEmpty(password);
		
		if(!Strings.isEmpty(auth)){
			isAuth = Boolean.valueOf(auth);
		}
		
		XmlElement propsElement = root.childElement("properties");
		
		Map<String, String> properties = (null == propsElement ? new HashMap<String, String>() : XmlProperties.load(propsElement).toMap());
		
		return new MailConfig(host, port, isAuth,debug, username, password, from, Charsets.getOrDefault(charset), connectionTimeout, socketTimeout, properties);
	}
	
	static Session createMailSession(final MailConfig config) {
		Properties properties = new Properties(System.getProperties());

		for (Entry<String, String> entry : config.getProperties().entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		
		properties.setProperty(MailConstants.MAIL_TRANSPORT_PROTOCOL, MailConstants.SMTP);
		properties.setProperty(MailConstants.MAIL_HOST, config.getHost());

		if (config.getPort() > 0) {
			properties.setProperty(MailConstants.MAIL_PORT, String.valueOf(config.getPort()));
		}

		if (config.isDebug()) {
			properties.setProperty(MailConstants.MAIL_DEBUG, "true");
		}

        if (config.getSocketTimeout() > 0)
        {
            properties.setProperty(MailConstants.MAIL_SMTP_TIMEOUT, Integer.toString(config.getSocketTimeout()));
        }

        if (config.getConnectionTimeout() > 0)
        {
            properties.setProperty(MailConstants.MAIL_SMTP_CONNECTIONTIMEOUT, Integer.toString(config.getConnectionTimeout()));
        }
        
        Session session = null;
        
        if(!config.isAuth() || Strings.isEmpty(config.getUsername())){
        	session = Session.getDefaultInstance(properties);
        }else{
			properties.setProperty(MailConstants.MAIL_SMTP_AUTH, "true");
        	
    		if (!Strings.isEmpty(config.getUsername())) {
    			properties.setProperty(MailConstants.MAIL_SMTP_USER, config.getUsername());
    		}

			session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(config.getUsername(), config.getPassword());
				}
			});
        }
        
        //try connect to smtp server
        Transport transport = null;
        boolean isDebug = session.getDebug();
        try {
        	session.setDebug(false);
	        transport = session.getTransport();
	        transport.connect();
        } catch (AuthenticationFailedException e){
        	if(null != e.getMessage() && e.getMessage().equals("No authentication mechansims supported by both server and client")){
        		log.warn("No authentication mechansims supported by both server and client, try to using default session");
        		properties.remove(MailConstants.MAIL_SMTP_AUTH);
        		session = Session.getDefaultInstance(properties);
        	}else{
        		throw new MailException("Error authenticating to server : {}",e.getMessage(),e);
        	}
        } catch (Throwable e) {
        	throw new MailException("Error creating transport and trying connect to server : {}",e.getMessage(),e);
        } finally {
        	if(null != transport){
        		try {
	                transport.close();
                } catch (Throwable e) {
                	log.warn("Error closing transport : {}",e.getMessage(),e);
                }
        	}
        }
        session.setDebug(isDebug);
        return session;
	}

	static MimeMessage createMailMessage(final Mailer mailer, final Email email) throws MessagingException {
		final MimeMessageContent content = new MimeMessageContent();
		final MimeMessage message = new MimeMessage(mailer.getSession());
		final String charset = mailer.getConfig().getCharset().name();

		message.setSubject(email.getSubject(), charset);
		message.setFrom(createInternetAddress(Objects.firstNotNull(email.getFrom(),mailer.getFrom()),charset));

		setReplyTo(email, message, charset);
		
		setRecipients(RecipientType.TO, email.getTo(), message, charset);
		
		setRecipients(RecipientType.CC, email.getCc(), message, charset);
		
		//fill multipart structure
		setTextAndHtml(email, content.alternativeMessages,charset);
		
//		setEmbeddedImages(email, messageRoot.multipartRelated);
//		setAttachments(email, messageRoot.multipartRoot);
//		setHeaders(email, message);
		
		message.setContent(content.root);
		
		if(null == email.getSentDate()) {
			message.setSentDate(new Date());	
		}else{
			message.setSentDate(email.getSentDate());
		}

		return message;
	}

	static void error(String message) {
		throw new MailException(message);
	}

	static void error(String message, Object... args) {
		throw new MailException(message, args);
	}
	
	private static void setReplyTo(final Email email,final MimeMessage message,String charset) throws MessagingException {
		final Recipient replyTo = email.getReplyTo();
		if (replyTo != null) {
			message.setReplyTo(new Address[] { createInternetAddress(replyTo, charset) });
		}
	}
	
	private static void setRecipients(final RecipientType type, Enumerable<Recipient> recipients ,final MimeMessage message,final String charset) throws MessagingException {
		if(!recipients.isEmpty()) {
			message.setRecipients(type, 
								  Enumerables.select(recipients, new Func1<Recipient, Address>() {
										public Address apply(bingo.lang.mailing.Recipient input) {
								            return createInternetAddress(input, charset);
							            }
								  })
								  .toArray(new Address[]{})
			);
		}
	}	
	
	private static void setTextAndHtml(final Email email,Multipart alternativeMessage,String charset) throws MessagingException {
		if (email.getText() != null) {
			final MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(email.getText(), charset);
			
			alternativeMessage.addBodyPart(messagePart);
		}
		if (email.getHtml() != null) {
			final MimeBodyPart messagePartHTML = new MimeBodyPart();
			messagePartHTML.setContent(email.getHtml(), "text/html; charset=\"" + charset + "\"");
			alternativeMessage.addBodyPart(messagePartHTML);
		}		
	}

	private static InternetAddress createInternetAddress(Recipient recipient,String charset) throws MailException {
		InternetAddress address = null;
		
		String name  = recipient.getName();
		String email = recipient.getAddress();

		try {
			address = new InternetAddress(email);

			// check name input
			if (Strings.isEmpty(name)) {
				name = email;
			}

			address.setPersonal(name, charset);

			address.validate();
		} catch (AddressException e) {
			throw new MailException(e);
		} catch (UnsupportedEncodingException e){
			throw new MailException(e);
		}
		
		return address;
	}
	
	/**
	 * This class conveniently wraps all necessary mimemessage parts that need to be filled with content, attachments etc. The root is
	 * ultimately sent using JavaMail.<br />
	 * <br />
	 * The constructor creates a new email message constructed from {@link MimeMultipart} as follows:
	 * 
	 * <pre>
	 * - root
	 * 	- related
	 * 		- alternative
	 * 			- mail tekst
	 * 			- mail html tekst
	 * 		- embedded images
	 * 	- attachments
	 * </pre>
	 */	
	private static final class MimeMessageContent {

		private final MimeMultipart root;

		private final MimeMultipart related;

		private final MimeMultipart alternativeMessages;

		private MimeMessageContent() throws MessagingException {
			this.root = new MimeMultipart("mixed");
			this.related = new MimeMultipart("related");
			this.alternativeMessages = new MimeMultipart("alternative");
			
			final MimeBodyPart contentRelated = new MimeBodyPart();
			final MimeBodyPart contentAlternativeMessages = new MimeBodyPart();
			
			// construct mail structure
			root.addBodyPart(contentRelated);
			contentRelated.setContent(related);
			related.addBodyPart(contentAlternativeMessages);
			contentAlternativeMessages.setContent(alternativeMessages);
		}
	}	
}