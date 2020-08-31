import com.sun.net.httpserver.HttpServer;

import javax.sound.sampled.Port;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class Server {

    public static void main(String[] args) {

        try {

            System.out.println("Serveren er startet og lytter på port 80");

            ServerSocket serverSocket = new ServerSocket(80); // Serverobjektet instansieres
            Socket socket = serverSocket.accept();                  // Serveren åbner port 80 for forbindelser

            // Vi læser en stream med bogstaver fra browserens request in igennem socketen
            BufferedReader request = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Vi skriver headers som respons vha. dette PrintWriter objekt
            PrintWriter responsHeaders = new PrintWriter(socket.getOutputStream());

            // Vi skriver HTML som respons vha. dette DataOutputStream objekt
            DataOutputStream responseStream = new DataOutputStream(socket.getOutputStream());

            // Vi indlæser alle linjer i den indkomne requests
            while (true) {

                String input = request.readLine();
                System.out.println("" + input);

                // En blank linje i HTTP er det, der slutter requesten
                if (input.isEmpty()) {

                    System.out.println("Request modtaget, svar sendes nu.");

                    // Vi konstruerer og sender en HTTP response linje for linje
                    responsHeaders.println("HTTP/1.1 200 OK"); // HTTP Status kode
                    responsHeaders.println("Content-Type: text/html; charset=utf-8"); // Typen af svaret er text eller html
                    responsHeaders.println(); // Blank line between headers and content, very important !
                    responsHeaders.flush();

                    responseStream.writeUTF("Hej med dig! Kelvin er sej!"); // Indholdet i responsen
                    responseStream.flush();

                    // Vi lukker begge streams
                    responsHeaders.close();
                    responseStream.close();
                    System.out.println("Svar er sendt til browseren.");

                }
            }
        }
        catch (SocketException e) {
            if ( e.getMessage().equals("Socket closed"))
            System.out.println("Forbindelsen afsluttet efter en succesfuld response-request forløb.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
