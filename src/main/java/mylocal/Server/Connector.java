package mylocal.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Connector {
    private static final byte STARTER = 0;
    private static final int STARTER_COUNT = 0;
    private static final int REPEAT_SENDS = 10;
    private static final int MAKING_UP_SPACE_FOR_ACK_SEQ = 2;
    private static byte SEQ = STARTER;
    private static byte ACK = STARTER;
    private static final int SET_SEQ = 2;
    private static final int SET_ACK = 1;
    private static final boolean FLAG_TRUE = true;
    private static final boolean FLAG_FALSE = false;
    private static final int SIZE_BUF = 1024;
    private static String ERR = "Не удалось установить соединение с сервером";
    private static String HELLO_SERVER = "";
    private static String FINISH = "Успех!";

    public static void connect(DatagramSocket client, InetAddress inetAddress, int port) throws IOException {
        String message = HELLO_SERVER;
        byte[] transition_buffer = message.getBytes(StandardCharsets.UTF_8);
        byte[] dataSend = ByteBuffer.allocate(transition_buffer.length + MAKING_UP_SPACE_FOR_ACK_SEQ).put(transition_buffer).array();
        ACK = (byte) message.length();
        dataSend[dataSend.length - SET_SEQ] = SEQ;
        dataSend[dataSend.length - SET_ACK] = ACK;
        DatagramPacket pktSend = new DatagramPacket(dataSend, dataSend.length, inetAddress, port);
        boolean flag = FLAG_TRUE;


        byte[] buf = new byte[SIZE_BUF];
        DatagramPacket reply = new DatagramPacket(buf, buf.length);
        for (int i = STARTER_COUNT; i <= REPEAT_SENDS; i++) {
            try {
                client.receive(reply);
                flag = FLAG_FALSE;
                break;
            } catch (SocketTimeoutException ignored2) {
                client.send(pktSend);
            }
        }
        if (flag) {
            throw new IOException(ERR);
        } else {
            System.out.println(FINISH);
        }
    }
}
