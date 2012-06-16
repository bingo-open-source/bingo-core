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

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import static org.junit.Assert.*;

public class MailerTest {

	@Test
	public void testDefaultMailer() throws Exception {
		Wiser wiser = new Wiser();
		wiser.start();
		
		Email sentMail = Mailer.get().sendText("from@mail.com", "to@mail.com", "Hello",  "Hello Text");
		
		assertEquals("Hello", sentMail.getSubject());
		
		wiser.stop();
		
		assertEquals(1, wiser.getMessages().size());
		
		WiserMessage receivedEmail = wiser.getMessages().get(0);
		
		MimeMessage message = receivedEmail.getMimeMessage();
		
		assertEquals(sentMail.getSubject(), message.getSubject());
		assertEquals(sentMail.getFrom().getAddress(), ((InternetAddress)message.getFrom()[0]).getAddress());
		assertEquals(sentMail.getTo().first().getAddress(), ((InternetAddress)message.getRecipients(RecipientType.TO)[0]).getAddress());
	}
}
