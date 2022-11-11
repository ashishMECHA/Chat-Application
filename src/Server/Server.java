package Server;
import java.net.*;
import java.io.*;

public class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server(){

       try{
           server = new ServerSocket(7777);
           System.out.println("Server is ready to accept connection");
           System.out.println("waiting...");
           socket = server.accept();

           br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(socket.getOutputStream());

           startReading();
           startWriting();


       } catch(Exception e) {
           e.printStackTrace();
       }
    }

    public void startReading(){
//This thread will keep reading data

        Runnable r1=()->{
            System.out.println("Reader started");

            try {
                while (true) {
                    String message = br.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + message);

                }
            } catch(Exception e) {
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }


    public void startWriting(){
//This thread will take data from user and send it to the client
        Runnable r2=()->{
            System.out.println("Writer started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            } catch(Exception e) {
                System.out.println("Connection Closed");
        }
        };
        new Thread(r2).start();
    }









    public static void main(String[] args) {
        System.out.println("This is server.....");
        new Server();
    }
}
