package mylocal.Server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server {
    private static final int GIVE_SEQ = 2;
    private static final int GIVE_ACK = 1;
    private static final int SET_SEQ = 0;
    private static final int SET_ACK = 1;

    private static final int PORT = 3333;
    private static final int SIZE_BUF = 1024;
    private static byte SEQ;
    private static byte ACK;
    private static final String sendSuccess = "Pack was delivered";
    private static final int MAKING_UP_SPACE_FOR_ACK_SEQ = 2;


    private static final int RAND = 99;
    private static final int RAND_PERCENT = 50;

    private static final boolean FLAG_TRUE = true;
    private static final boolean FLAG_FALSE = false;

    public static void main(String[] args) throws IOException {
        try (DatagramSocket server = new DatagramSocket(PORT, InetAddress.getLocalHost())) {

            while (FLAG_TRUE) {

                byte[] buf = new byte[SIZE_BUF];
                DatagramPacket gotPack = new DatagramPacket(buf, buf.length);
                server.receive(gotPack);
                byte[] giverSeqACK = PackageOutputToConsole.printThePackageToTheConsole(gotPack);
                SEQ = giverSeqACK[SET_SEQ];
                ACK = giverSeqACK[SET_ACK];

                boolean wasReceived = FLAG_TRUE;
                Random rnd80 = new Random();
                int rnd = rnd80.nextInt(RAND);
                if (rnd > RAND_PERCENT) {
                    wasReceived = FLAG_FALSE;
                    System.out.println(FLAG_FALSE);
                }

                if (wasReceived) {
                    byte[] buf2;
                    buf2 = ByteBuffer.allocate(sendSuccess.length() + MAKING_UP_SPACE_FOR_ACK_SEQ).put(sendSuccess.getBytes(StandardCharsets.UTF_8)).array();
                    buf2[buf2.length - GIVE_SEQ] = SEQ;
                    buf2[buf2.length - GIVE_ACK] = ACK;
                    DatagramPacket replyPack = new DatagramPacket(buf2, buf2.length, gotPack.getAddress(), gotPack.getPort());
                    server.send(replyPack);
                }
            }
        }

    }
}
