package zapchastidarom.mail;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.store.MailFolder;
import com.icegreen.greenmail.store.StoredMessage;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.io.IOException;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


//TODO: write test with greenMail
@Ignore
public class MailServiceTest {

  //@Resource
  //MailService mailService;

  private GreenMail greenMail;
  private Message[] messages;
  private MailService mailService;
  private GreenMailUser greenMailUser;

  String username = "user@name";
  String password = "psswrd";
  String from = "from@user";
  String subject = GreenMailUtil.random();
  String body = GreenMailUtil.random();
  MimeMessage message;

  @Before
  public void setUp() throws MessagingException {

    Security.setProperty("ssl.SocketFactory.provider",
        DummySSLSocketFactory.class.getName());
    greenMail = new GreenMail(ServerSetupTest.IMAPS);
    greenMail.start();

    greenMailUser = greenMail.setUser(username, password);

    message = new MimeMessage((Session) null);
    message.setFrom(new InternetAddress(from));
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(
        username));
    message.setSubject(subject);
    message.setText(body);

    greenMailUser.deliver(message);


    //mailService = new MailService(MailService.GMAIL_HOST, MailService.DEF_PORT, username, password);
  }

  @Test
  public void getLetter() throws MessagingException, IOException {
    Properties props = new Properties();
    Session session = Session.getInstance(props);

    URLName urlName = new URLName("imaps", "host",
        ServerSetupTest.IMAPS.getPort(), null, greenMailUser.getLogin(),
        greenMailUser.getPassword());

    Store store = session.getStore(urlName);
    store.connect();

    Folder folder = store.getFolder("INBOX");
    folder.open(Folder.READ_ONLY);
    Message[] messages = folder.getMessages();
    assertNotNull(messages);
    assertThat(1, equalTo(messages.length));
    assertEquals(subject, messages[0].getSubject());
    assertTrue(String.valueOf(messages[0].getContent())
        .contains(body));
    assertEquals(from, messages[0].getFrom()[0].toString());
  }

  @Test
  public void findLatestAttachment() throws FolderException {

    MailFolder inbox = greenMail.getManagers().getImapHostManager().getInbox(greenMailUser);
    List<StoredMessage> messages = inbox.getMessages();
    if (!messages.isEmpty()) {
      messages.get(0).getMimeMessage();
      System.out.println("good");
    } else {
      System.out.println("empty");;
    }
  }

  @Test
  public void fetchAttachment() {
  }

  @Test
  public void downloadFile() {
  }

  @After
  public void tearDown() throws Exception {
    greenMail.stop();
  }
}