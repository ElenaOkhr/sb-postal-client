package zapchastidarom.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

//TODO: add sender
public class MailService {

  public static final String GMAIL_HOST = "imap.googlemail.com";
  public static final int DEF_PORT = 993;

  private Session session;
  private String username;
  private String password;
  private String host;
  private int port;

  public MailService(String emailHost, int port, String username, String password) {
    this.username = username;
    this.password = password;
    this.host = emailHost;
    this.port = port;

    session = Session.getDefaultInstance(new Properties());
  }

  public String findLatestAttachment() {
    String file = "";
    try {
      Store store = session.getStore("imaps");
      store.connect(host, port, username, password);

      Folder inbox = store.getFolder("INBOX");
      inbox.open(Folder.READ_WRITE);

      int messageCount = inbox.getMessageCount();
      int topBound = messageCount < 10 ? messageCount : 10;
      Message message;

      for (int i = 0; file.isEmpty() && i < topBound; i++) {
        message = inbox.getMessage(messageCount - i);
        message.setFlags(new Flags(Flag.SEEN), true);

        System.out.println(i + ". " + message.getSentDate() + " from: " +
            message.getFrom()[0] + " subject: " + message.getSubject());

        file = fetchAttachment(message);
      }
    } catch (MessagingException e) {
      System.out.println("Failed to fetch the message " + e.getMessage());
    }
    return file;
  }

  String fetchAttachment(Message message) {
    String file = "";

    try {
      String contentType = message.getContentType();

      if (contentType.contains("multipart")) {
        Multipart content = (Multipart) message.getContent();

        for (int i = 0; i < content.getCount(); i++) {

          MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);

          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            file = downloadFile(part.getFileName(), part);
          }
        }
      }
    } catch (MessagingException | IOException e) {
      System.out.println("Failed to fetch attachment " + e);
    }

    return file;
  }

  String downloadFile(String file, MimeBodyPart attachmentPart) throws IOException, MessagingException {
    if (file.endsWith(".csv")) {
      attachmentPart.saveFile(new File(file));

      System.out.println(file + " is downloaded");
      return file;
    }
    else {
      System.out.println("No suitable attachment in the file");
      return "";
    }
  }
}
