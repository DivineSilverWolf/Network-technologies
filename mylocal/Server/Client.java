package mylocal.Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final byte STARTER=1;
    private static final int SET_SEQ =2;
    private static final int SET_ACK =1;
    private static final int GIVE_SEQ =1;
    private static final int GIVE_ACK =2;
    private static final int EXIT_STATUS=0;



    private static final int TIME_OUT=4000;
    private static byte SEQ = STARTER;
    private static byte ACK = STARTER;
    private static final int SIZE_BUF = 1024;

    private static final String PACKAGE_RESUPPLY_NUMBER = "My try is ";
    private static final String PACKAGE_RESUPPLY_NUMBER_ERR = "Last try is ";

    private static final String ERR="Server not responding ";
    private static final String NUMBER_OF_UNDELIVERED_PACKAGE="lost package number: ";

    private static final int MAKING_UP_SPACE_FOR_ACK_SEQ=2;
    private static final int PORT=3333;

    private static final int STARTER_COUNT=0;
    private static final int STARTER_SUBSTRING=0;
    private static final int REPEAT_SENDS=3;

    private static final String SEQ_EQUALS =", SEQ=";
    private static final String ACK_EQUALS = ", ACK=";
    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        DatagramSocket client = new DatagramSocket();
        Scanner in = new Scanner(System.in);
        String message;
        client.setSoTimeout(TIME_OUT);
        while (true) {

            message = in.nextLine();
            byte[] dataSend = ByteBuffer.allocate(message.length() + MAKING_UP_SPACE_FOR_ACK_SEQ).put(message.getBytes(StandardCharsets.UTF_8)).array();
            ACK = (byte) message.length();
            dataSend[dataSend.length - SET_SEQ] = SEQ;
            dataSend[dataSend.length - SET_ACK] = ACK;
            DatagramPacket pktSend = new DatagramPacket(dataSend, dataSend.length, inetAddress, PORT);
            client.send(pktSend);

            byte[] buf = new byte[SIZE_BUF];
            DatagramPacket reply = new DatagramPacket(buf, buf.length);
                for (int i =  STARTER_COUNT; i <= REPEAT_SENDS; i++) {
                    System.out.println(PACKAGE_RESUPPLY_NUMBER + i);
                    try {
                        client.receive(reply);
                        break;
                    } catch (SocketTimeoutException ignored2) {
                        System.out.println(NUMBER_OF_UNDELIVERED_PACKAGE + i);
                        client.send(pktSend);
                    }
                    if (i == REPEAT_SENDS) {
                        System.out.println(PACKAGE_RESUPPLY_NUMBER_ERR + i);
                        System.err.println(ERR);
                        System.exit(EXIT_STATUS);
                    }
                }


            byte[] buf2;
            buf2 = reply.getData();
            String answer = new String(buf2);
            answer = answer.substring(STARTER_SUBSTRING, reply.getLength() - MAKING_UP_SPACE_FOR_ACK_SEQ);
            SEQ = buf2[reply.getLength() - GIVE_SEQ];
            ACK = buf2[reply.getLength() - GIVE_ACK];
            System.out.println(answer + SEQ_EQUALS + SEQ + ACK_EQUALS + ACK);
        }
    }
}


