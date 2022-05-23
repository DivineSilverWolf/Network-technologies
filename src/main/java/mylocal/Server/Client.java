package mylocal.Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final byte STARTER = 1;
    private static final int SET_SEQ = 2;
    private static final int SET_ACK = 1;
    private static final int GIVE_SEQ = 1;
    private static final int GIVE_ACK = 0;
    private static final int EXIT_STATUS = 0;


    private static final int TIME_OUT = 4000;
    private static byte SEQ = STARTER;
    private static byte ACK = STARTER;
    private static final int SIZE_BUF = 1024;

    private static final String PACKAGE_RESUPPLY_NUMBER = "My try is ";
    private static final String PACKAGE_RESUPPLY_NUMBER_ERR = "the package was not delivered to the server";

    private static final String ERR = "Server not responding ";
    private static final String NUMBER_OF_UNDELIVERED_PACKAGE = "lost package number: ";

    private static final int MAKING_UP_SPACE_FOR_ACK_SEQ = 2;
    private static final int PORT = 3333;

    private static final int STARTER_COUNT = 0;
    private static final int REPEAT_SENDS = 4;


    private static final boolean FLAG_TRUE = true;
    private static final boolean FLAG_FALSE = false;

    public static void main(String[] args) throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        boolean flag;
        try (DatagramSocket client = new DatagramSocket()) {
            Scanner in = new Scanner(System.in);
            String message;
            client.setSoTimeout(TIME_OUT);
            Connector.connect(client, inetAddress, PORT);
            while (true) {

                message = in.nextLine();
                byte[] transition_buffer = message.getBytes(StandardCharsets.UTF_8);
                byte[] dataSend = ByteBuffer.allocate(transition_buffer.length + MAKING_UP_SPACE_FOR_ACK_SEQ).put(transition_buffer).array();
                ACK = (byte) message.length();
                dataSend[dataSend.length - SET_SEQ] = SEQ;
                dataSend[dataSend.length - SET_ACK] = ACK;
                DatagramPacket pktSend = new DatagramPacket(dataSend, dataSend.length, inetAddress, PORT);

                byte[] buf = new byte[SIZE_BUF];
                DatagramPacket reply = new DatagramPacket(buf, buf.length);
                flag = FLAG_TRUE;
                for (int i = STARTER_COUNT; i <= REPEAT_SENDS; i++) {
                    System.out.println(PACKAGE_RESUPPLY_NUMBER + i);
                    try {
                        client.receive(reply);
                        flag = FLAG_FALSE;
                        break;
                    } catch (SocketTimeoutException ignored2) {
                        System.out.println(NUMBER_OF_UNDELIVERED_PACKAGE + i);
                        client.send(pktSend);
                    }
                }
                if (flag) {
                    System.err.println(PACKAGE_RESUPPLY_NUMBER_ERR);
                    System.err.println(ERR);
                    System.exit(EXIT_STATUS);
                }

                byte[] giverSeqACK = PackageOutputToConsole.printThePackageToTheConsole(reply);
                SEQ = giverSeqACK[GIVE_SEQ];
                ACK = giverSeqACK[GIVE_ACK];
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


