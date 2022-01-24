import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SMTPSkeleton {

    public static void main(String[] args) throws UnknownHostException, IOException {
        String mailServer ="smtp.sendgrid.net";
        InetAddress mailHost = InetAddress.getByName(mailServer);
        InetAddress localHost = InetAddress.getLocalHost();
        //mailHost = localHost;
        Socket smtpSocket = new Socket(mailHost, 25);

        BufferedReader in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        PrintWriter pr = new PrintWriter(smtpSocket.getOutputStream(), true);
        String initialID = in.readLine();
        System.out.println(initialID);
        pr.println("HELO " + localHost.getHostName());
        //pr.flush();
        String welcome = in.readLine();
        System.out.println(welcome);
        // TODO code application logic here

        String sender;
        String receipient;
        String subject = "";
        String mailBody = "";
        String[] receipients;
        receipients = new String[100];

        Scanner scn = new Scanner(System.in);

        System.out.print("From:");
        sender = scn.next();

        System.out.print("To:");
        receipient = scn.next();

        receipients = receipient.split(",");

        System.out.print("Subject:");
        subject = scn.next();

        try (InputStreamReader input = new InputStreamReader(System.in);
                BufferedReader buffer = new BufferedReader(input)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.equals("Goodbye.")) {
                    break;
                }
                mailBody += (line + "\n");
            }
        } catch (Exception e) {
        }

        int noOfReceipients = receipients.length;
        String response;

        for (int i = 0; i < noOfReceipients; i++) {
            pr.println("MAIL FROM:<" + sender + ">");

            response = in.readLine();
            System.out.println(response);

            pr.println("RCPT TO:<" + receipients[i] + ">");
            response = in.readLine();
            System.out.println(response);

            pr.println("DATA");
            response = in.readLine();
            System.out.println(response);

            String mail = "Subject: " + subject + "\n"
                    + "From: " + sender + "\n"
                    + "To: " + receipient + "\n"
                    + "\n"
                    + mailBody
                    + "\r\n"
                    + "."
                    + "\r\n";
            pr.println(mail);
            response = in.readLine();
            System.out.println(response);
        }

        pr.println("QUIT\r\n");
        response = in.readLine();
        System.out.println(response);
    }
}
