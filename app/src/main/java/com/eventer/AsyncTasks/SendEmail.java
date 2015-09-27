package com.eventer.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eventer.MainActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by rg on 25-Sep-15.
 */
public class SendEmail extends AsyncTask<Void,Void,Void> {

    com.google.api.services.gmail.Gmail mService;
    String token;
    String senderEmail;
    String receiverEmail;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    public SendEmail(String receiverEmail) {
        this.token = MainActivity.token;
        this.senderEmail = MainActivity.mEmail;
        this.receiverEmail = receiverEmail;
    }

    public static MimeMessage createEmail(String to, String from, String subject,
                                          String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        InternetAddress tAddress = new InternetAddress(to);
        InternetAddress fAddress = new InternetAddress(from);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
    }


    public void send() {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();

            MimeMessage mimeMessage = createEmail(receiverEmail, senderEmail, "Invitation subject", "Yes you are invited");
            sendMessage(mService, senderEmail, mimeMessage);

        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
                send();
        } catch (Exception e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
            Log.d("GetUsernameTask::dIB", "IOException");
            e.printStackTrace();
        }
        return null;
    }
}
