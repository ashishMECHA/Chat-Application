package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roberto",Font.PLAIN,20);


    public Client(){
        try{
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            CreateGUI();
            handleEvents();


            startReading();
//            startWriting();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void CreateGUI(){
        this.setTitle("Client Messenger END");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);  // center your window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Code for Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);  // To center the heading
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        this.setLayout(new BorderLayout());

        //Adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("key released " + e.getKeyCode());
                if(e.getKeyCode() == 10){
//                    System.out.println("Enter key pressed");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }



    public void startReading(){
//This thread will keep reading data
        Runnable r1=()->{
            System.out.println("Reader started");
            try {
            while(true){

                    String message = br.readLine();
                    if(message.equals("exit")){
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
//                    System.out.println("Server : "+ message);
                messageArea.append("Server : "+ message+"\n");
            }

            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }


    public void startWriting(){
//This thread will take data from user and send it to the client
        Runnable r2=()->{
            System.out.println("Writer started");
            try{
            while(!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };
        new Thread(r2).start();
    }














    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
