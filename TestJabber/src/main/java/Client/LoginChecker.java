package Client;

import Client.Configuration.ClientConfig;
import Common.Results;
import org.jivesoftware.smack.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginChecker extends Thread {
    private static Logger log = Logger.getLogger(LoginChecker.class.getName());

    private int id;
    private Queue<Integer> queue;
    private ClientConfig config;

    LoginChecker(int id, Queue<Integer> queue, ClientConfig config) {
        this.id = id;
        this.queue = queue;
        this.config = config;
    }

    /**
     * Create connection this server
     *
     * @return XMPPConnection
     */
    private XMPPConnection getConnection() {
        ConnectionConfiguration config = new ConnectionConfiguration(this.config.getHost(), 5222, this.config.getServiceName());
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        log.info("Connect to server");
        return new XMPPConnection(config);
    }

    /**
     * Login user on xmpp server
     * NOTICE: expensive operation for server
     *
     * @param connection
     * @throws XMPPException
     */
    private void login(XMPPConnection connection) throws XMPPException {
        connection.connect();
        connection.login("testuser" + id, "pass123");
        log.info("Login in: " + "testuser" + id);
    }


    @Override
    public void run() {
        for (int i = 0; i < config.getMessageNumber(); i++) {
            try {
                XMPPConnection connection = getConnection();
                long startTimestamp = System.currentTimeMillis();
                login(connection);
                long finishTimestamp = System.currentTimeMillis();
                log.info("Success login, send to local instance");
                queue.add((int) (finishTimestamp - startTimestamp));
                connection.disconnect();
                if (config.getSendingDelay() != 0) {
                    Thread.sleep(config.getSendingDelay());
                }
            } catch (Exception ignored) {}
        }
    }
}
