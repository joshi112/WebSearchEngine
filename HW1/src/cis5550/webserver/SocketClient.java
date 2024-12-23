package cis5550.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

//    public SocketClient(String ip, int port) throws IOException {
//        clientSocket = new Socket(ip, port);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    }
//
//    public String sendMessage(String msg) throws IOException {
//        //out.println(msg);
//        out.print("GET / HTTP/1.1\r\n");
//        out.print("Content-Length: 0\r\n\r\n");
//        String resp = in.readLine();
//        return resp;
//    }
//
//    public void stopConnection() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//    }
//    public static void main(String[] args) throws IOException, InterruptedException {
//        SocketClient client = new SocketClient("127.0.0.1", 8000);
//        client.sendMessage("hi server\r\n");
//    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Client written by Apurva Sunil Modak");
            System.exit(1);
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            //out.print("GET /file1.txt HTTP/1.1\r\nContent-Length: 5\r\nHost: localhost\r\n\r\nHello");
            out.print("GET /file1.txt HTTP/1.1\r\nContent-Length: 0\r\n\r\n");
            out.flush();
//            out.print("GET /file2.txt HTTP/1.1\r\nContent-Length: 14\r\nHost: localhost\r\n\r\nThis is a test");
//            out.flush();
//            out.print("GET /file3.txt HTTP/1.1\r\nContent-Length: 0\r\nHost: localhost\r\n\r\n");
//            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
