import com.sun.net.httpserver.HttpServer;

import javax.sound.sampled.Port;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(80);
            System.out.println("Serveren er startet og lytter på port 80");
            Socket socket = serverSocket.accept();
            DataInputStream requestStream = new DataInputStream(socket.getInputStream());
            DataOutputStream responseStream = new DataOutputStream(socket.getOutputStream());
            // we read characters from the client via input stream on the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // we get character output stream to client (for headers)
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            // get binary output stream to client (for requested data)
            BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
            while (true) {
                String input = in.readLine();
                System.out.println("" + input);
                if (input.isEmpty()) {System.out.println("Request modtaget");
                out.println("HTTP/1.1 200 OK");
                    out.println(); // blank line between headers and content, very important !
                    out.flush();
                    System.out.println("Svar sendes nu");
                    //dataOut.write();
                }
            }





        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
