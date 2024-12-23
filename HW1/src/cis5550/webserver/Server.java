package cis5550.webserver;

import cis5550.tools.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class);
    public static void main(String[] args) {
        int pnum = Integer.parseInt(args[0]);
        String dir = args[1];

        try {
            ServerSocket ss = new ServerSocket(pnum);
            while (true) {

            Socket soc = ss.accept();

            System.out.println("Socket established " + soc.getRemoteSocketAddress());

            StringBuilder builder = new StringBuilder();

            //byte[] buffer = new byte[1024];
            int buf;
            InputStream is = soc.getInputStream();
            while ((buf = is.read()) != -1) {
                builder.append((char) buf);
                int index = builder.indexOf("\r\n\r\n");
                if (index != -1) {
                    String header = builder.toString();
                    BufferedReader reader = new BufferedReader(new StringReader(header));
                    String line = reader.readLine();
                    // System.out.println(line);
                    if (line.isEmpty()) {
                        sendMessage(soc, 400, "Bad Request");
                        builder.setLength(0);
                        reader.close();
                        continue;
                    }

                    String[] headers = line.split(" ");
                    if (headers[0].length() > 7) {
                        sendMessage(soc, 400, "Bad request");
                        builder.setLength(0);
                        reader.close();
                        continue;
                    }
                    if (!headers[2].equals("HTTP/1.1")) {
                        sendMessage(soc, 400, "Bad Request");
                        builder.setLength(0);
                        reader.close();
                        continue;
                    }

//                    int length = 0;
//                    while ((line = reader.readLine()) != null) {
//                        if (line.contains("Content-Length")) {
//                            length = Integer.parseInt(line.substring(16));
//                        }
//                    }
//
//                    int i = 0;
//                    builder.setLength(0);
//                    while (i < length && (buf = is.read()) != -1) {
//                        builder.append((char) buf);
//                        i++;
//                    }
//
//                    String content = builder.toString();
//
//                    PrintWriter writer = new PrintWriter(soc.getOutputStream());
//                    writer.print("HTTP/1.1 200 OK\r\n");
//                    writer.print("Content-Type: text/plain\r\n");
//                    writer.print("Server: Server\r\n");
//                    writer.print("Content-Length: 12\r\n\r\n");
//                    writer.print("Hello World!");
//                    writer.flush();
//
//                    System.out.println(content);
                    dir = dir.replace('\\','/');
                    String filePath = dir+headers[1];
                    Path path = Paths.get(filePath);
                    if(!Files.exists(path)){
                        sendMessage(soc, 404, "Not Found");
                    }
                    if(!Files.isReadable(path)){
                        sendMessage(soc, 403, "Forbidden");
                    }

                    InputStream file = new FileInputStream(filePath);
                    int buffer = file.read();
                    StringBuilder filedata = new StringBuilder();
                    while(buffer != -1){
                        filedata.append((char)buffer);
                        buffer = file.read();
                    }

                    String content = filedata.toString();
                    System.out.println(content);
                    PrintWriter writer = new PrintWriter(soc.getOutputStream());
                    writer.print("HTTP/1.1 200 OK\r\n");
                    writer.print("Content-Length: "+content.length()+"\r\n\r\n");
                    writer.flush();
                    BufferedOutputStream bos = new BufferedOutputStream(soc.getOutputStream());
                    bos.write(content.getBytes());
                    bos.flush();

                    filedata.setLength(0);
                    builder.setLength(0);
                    System.out.println(content);
                }

            }

            is.close();

        }


//            BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));
//            StringBuilder answer = new StringBuilder();
//            int index=-1; && answer.indexOf("\r\n\r\n") == -1
//            while(answer.indexOf("\r\n\r\n") == -1) {
//                answer.append((char)input.read());
//                index = answer.indexOf("\r\n\r\n");
//                System.out.println(answer);
//            }
            // soc.close();
            // ss.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void  sendMessage(Socket soc, int error, String message) throws IOException {
        PrintWriter writer = new PrintWriter(soc.getOutputStream());
        writer.print("HTTP/1.1 "+error+" "+message+"\r\n");
        writer.print("Content-Type: text/plain\r\n");
        writer.print("Content-Length: "+message.length()+"\r\n");
        writer.print("\r\n");
        writer.print(message);
        writer.flush();
        //writer.close();
        // soc.close();
        // ss.close();
        //break;
    }
}
