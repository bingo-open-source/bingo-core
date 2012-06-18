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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Strings;

public class Email {

	private String subject;
	
	private Recipient from = null;
	
	private Recipient replyTo = null;
	
	private final List<Recipient> to = new ArrayList<Recipient>();
	
	private final List<Recipient> cc = new ArrayList<Recipient>();
	
	private String text;
	
	private String html;
	
	private Date sentDate;
	
	public Email(){
		
	}
	
	public Email(String subject){
		this.subject = subject;
	}
	
	public Email(String to,String subject){
		this.subject = subject;
		this.setTo(to);
	}
	
	public Email(String from,String to,String subject){
		this.setFrom(from);
		this.subject = subject;
		this.setTo(to);
	}
	
	public String getSubject(){
		return subject;
	}
	
	public void setSubject(String subject) {
    	this.subject = subject;
    }

	public Recipient getFrom() {
		return from;
	}
	
	public Recipient getReplyTo() {
    	return replyTo;
    }

	public Enumerable<Recipient> getTo() {
		return Enumerables.of(to);
	}
	
	public Enumerable<Recipient> getCc() {
		return Enumerables.of(cc);
	}
	
	public String getText() {
    	return text;
    }
	
	public String getHtml() {
    	return html;
    }
	
	public boolean isText(){
		return null != text;
	}
	
	public boolean isHtml(){
		return null != html;
	}
	
	public Date getSentDate() {
    	return sentDate;
    }

	public Email setFrom(String address){
		this.from = new Recipient("", address);
		return this;
	}
	
	public Email setFrom(String name,String address){
		this.from = new Recipient(name, address);
		return this;
	}
	
	public Email setReplyTo(String address){
		this.replyTo = new Recipient("", address);
		return this;
	}
	
	public Email setReplyTo(String name,String address){
		this.replyTo = new Recipient(name, address);
		return this;
	}
	
	public Email addTo(String address){
		this.to.add(new Recipient("", address));
		return this;
	}
	
	public Email addTo(String name,String address){
		this.to.add(new Recipient(name, address));
		return this;
	}
	
	public Email setText(String text) {
    	this.text = text;
    	return this;
    }

	public Email setHtml(String html) {
    	this.html = html;
    	return this;
    }

	public Email setSentDate(Date sentDate) {
    	this.sentDate = sentDate;
    	return this;
    }
	
	protected void setTo(String to){
		String[] addresses = Strings.split(to,MailConstants.ADDRESS_SEPERATORS);
		for(String addr : addresses){
			this.addTo(addr);
		}
	}
}
