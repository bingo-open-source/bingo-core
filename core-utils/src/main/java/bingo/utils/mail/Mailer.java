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
package bingo.utils.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import bingo.lang.Assert;
import bingo.lang.Objects;
import bingo.lang.StopWatch;
import bingo.lang.Strings;
import bingo.lang.exceptions.NotFoundException;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class Mailer {
	
	private static final Log log = LogFactory.get(Mailer.class);
	
	private static final Mailer mailer;
	
	static {
		MailConfig config = null;
		try {
	        config = MailConfig.loadFromXml("classpath:/mail.conf.xml");
        } catch (NotFoundException e) {
        }
        
		try {
	        config = MailConfig.loadFromXml("classpath:/config/mail.conf.xml");
        } catch (NotFoundException e) {
        }
        
        if(null == config){
        	mailer = null;
        }else{
        	mailer = new Mailer(config);
        }
	}
	
	public static Mailer get() {
		Assert.notNull(mailer,"there is no default mailer config has been found");
		return mailer;
	}

	private final MailConfig config;
	private final Session    session;
	
	private Recipient from;
	
	public Mailer(String host) {
		this.config  = new MailConfig(host, 0);
		this.session = Utils.createMailSession(config);
		this.check();
	}
	
	public Mailer(String host,int port) {
		this.config = new MailConfig(host, port);
		this.session = Utils.createMailSession(config);
		this.check();
	}
	
	public Mailer(String host,String username,String password) {
		this.config = new MailConfig(host,0,username,password);
		this.session = Utils.createMailSession(config);
		this.check();
	}	
	
	public Mailer(String host,int port,String username,String password) {
		this.config = new MailConfig(host, port,username,password);
		this.session = Utils.createMailSession(config);
		this.check();
	}
	
	public Mailer(MailConfig config) {
		this.config = config;
		this.session = Utils.createMailSession(config);
		this.check();
	}
	
	public MailConfig getConfig() {
    	return config;
    }
	
	public Mailer setFrom(String address){
		this.from = new Recipient("", address);
		return this;
	}
	
	public Mailer setDebug(boolean debug){
		this.session.setDebug(debug);
		return this;
	}
	
	public Mailer setFrom(String name,String address){
		this.from = new Recipient(name, address);
		return this;
	}
	
	public Recipient getFrom() {
    	return null != from ? from : config.getFrom();
    }

	public boolean isDebug() {
    	return session.getDebug() || config.isDebug();
    }
	
	public Email sendText(String to,String subject,String text) throws MailException {
		Email email = new Email(to,subject).setText(text);
		
		send(email);
		
		return email;		
	}
	
	public Email sendText(String from,String to,String subject,String text) throws MailException {
		Email email = new Email(from,to,subject).setText(text);
		
		send(email);
		
		return email;
	}
	
	public Email sendHtml(String to,String subject,String html) throws MailException {
		Email email = new Email(to,subject).setHtml(html);
		
		send(email);
		
		return email;
	}
	
	public Email sendHtml(String from,String to,String subject,String html) throws MailException {
		Email email = new Email(from,to,subject).setHtml(html);
		
		send(email);
		
		return email;
	}
	
	public void send(Email email) throws MailException {
		validate(email);
		
		StopWatch sw = StopWatch.startNew();
		
		try {
	        send(Utils.createMailMessage(this, email));
        } catch (MessagingException e) {
        	throw new MailException("Error creating mail message : {0}",e.getMessage(),e);
        }
		
		log.debug("Sent an email [{}] used {}ms",sw.stop().getElapsedMilliseconds());
	}
	
	protected void send(MimeMessage message) throws MailException {
		Transport transport = null;
		try {
	        transport = session.getTransport();
	        transport.connect();
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
        } catch (Throwable e) {
        	throw new MailException("Error sending email : {0}",e.getMessage(),e);
        } finally {
        	if(null != transport){
        		try {
	                transport.close();
                } catch (Throwable e) {
                	log.warn("Error closing transport : {0}",e.getMessage(),e);
                }
        	}
        }
	}
	
	protected Session getSession(){
		return session;
	}
	
	private void check() {
		Assert.notEmpty(config.getHost(),"'host' cannot be empty");
	}
	
	private void validate(Email email) {
		if(Strings.isEmpty(email.getSubject())){
			Utils.error(Utils.MISSING_SUBJECT);
		}
		
		Recipient from = Objects.firstNotNull(email.getFrom(),getFrom());
		
		if(null == from || Strings.isEmpty(from.getAddress())){
			Utils.error(Utils.MISSING_SENDER);
		}
		
		if(email.getTo().isEmpty()){
			Utils.error(Utils.MISSING_RECIPIENT);
		}
	}
}