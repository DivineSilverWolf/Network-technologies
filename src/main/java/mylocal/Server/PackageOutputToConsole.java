package mylocal.Server;

import java.net.DatagramPacket;

public class PackageOutputToConsole {
    private static final int STARTER_SUBSTRING = 0;
    private static final int MAKING_UP_SPACE_FOR_ACK_SEQ = 2;
    private static final int GIVE_SEQ = 1;
    private static final int GIVE_ACK = 2;
    private static final int SET_SEQ = 0;
    private static final int SET_ACK = 1;
    private static final int BUFFER_ACK_SEQ = 2;
    private static final String SEQ_EQUALS = ", SEQ=";
    private static final String ACK_EQUALS = ", ACK=";
    public static byte[] printThePackageToTheConsole(DatagramPacket reply){
        byte SEQ;
        byte ACK;
        byte[] buf2 = reply.getData();
        String answer = new String(buf2);
        answer = answer.substring(STARTER_SUBSTRING, reply.getLength() - MAKING_UP_SPACE_FOR_ACK_SEQ);
        SEQ = buf2[reply.getLength() - GIVE_SEQ];
        ACK = buf2[reply.getLength() - GIVE_ACK];
        System.out.println(answer + SEQ_EQUALS + SEQ + ACK_EQUALS + ACK);
        byte[] returnerSeqAck=new byte[BUFFER_ACK_SEQ];
        returnerSeqAck[SET_SEQ]=SEQ;
        returnerSeqAck[SET_ACK]=ACK;
        return returnerSeqAck;
    }
}
