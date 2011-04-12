/**
 * Copyright 2008 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.mail;

import eu.debooy.doosutils.DoosConstants;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author Marco de Booy
 *
 * The MailMan does exactly what you expect; it delivers an email message. The
 * message text is expected to contain HTML tags as well as UTF-8 characters.
 * This is configured by default. <br/><i>Note: support for mail messages with
 * attachments is not yet implemented. </i> <br/>
 */
// @TODO Implement attachments.
public class MailMan {
  public static void sendMail(String from, String to, String subject,
                              String messageText) throws MessagingException {
    Properties  properties  = new Properties();
    properties.put("mail.smtp.host", DoosConstants.SMTP_HOST);

    Session     session     = Session.getDefaultInstance(properties, null);
    MimeMessage message     = new MimeMessage(session);
    message.addFrom(new Address[] { new InternetAddress(from) });
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setSubject(subject);
    message.setContent(messageText, "text/html; charset=utf-8");

    Transport.send(message);
  }
}
