import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws IOException {

        System.out.println("Serveren er startet og lytter på port 80");

        ServerSocket serverSocket = new ServerSocket(80); // Serverobjektet instansieres

        while (true) {
            Socket socket = serverSocket.accept();                  // Serveren åbner port 80 for forbindelser

            new Thread(new HandleRequest(socket)).start();
        }
    }
}

class HandleRequest implements Runnable {
    private final Socket socket;

    public HandleRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

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

//                    responseStream.writeUTF("<h>Test</h>");
//                    responseStream.writeUTF("<p>Hej med dig! Kelvin er sej!</p>"); // Indholdet i responsen
//                    responseStream.writeUTF("<img src= https://camo.githubusercontent.com/db3d9393cf44727517d7d16725c389d89e9370e6/68747470733a2f2f692e696d67666c69702e636f6d2f346431386b762e6a7067>");

                    StringBuilder contentBuilder = new StringBuilder();
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(getClass().getResource("hjemmeside.html").getFile()));
                        String str;
                        while ((str = in.readLine()) != null) {
                            contentBuilder.append(str);
                        }
                        in.close();
                    } catch (IOException e) {
                    }
                    String content = contentBuilder.toString();
                    responseStream.writeBytes(content);

                    responseStream.flush();

                    // Vi lukker begge streams
                    responsHeaders.close();
                    responseStream.close();
                    System.out.println("Svar er sendt til browseren.");

                }
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Socket closed"))
                System.out.println("Forbindelsen afsluttet efter en succesfuld response-request forløb.");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
