package Local.Comunicator;

import Client.Configuration.ClientConfig;
import Common.Commands;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Instance of client
 */
public class Client {
    private static Logger log = Logger.getLogger(Client.class.getName());

    private Socket socket;

    public Client(String host, int port) throws IOException {
        log.info("Create new client");
        socket = new Socket(host, port);
    }

    /**
     * activate client
     * make it ready for tests
     *
     * @throws IOException
     */
    public void start() throws IOException {
        log.info("Start client");
        socket.getOutputStream().write(Commands.START_CLIENT);
    }

    /**
     * Send config to client instance
     *
     * @param config
     * @throws IOException
     */
    public void sendConfig(ClientConfig config) throws IOException {
        log.info("Send config");
        socket.getOutputStream().write(Commands.SEND_CONFIG);
        new ObjectOutputStream(socket.getOutputStream()).writeObject(config);
    }

    /**
     * Send client command about tests start
     * Append result of tests in global queue
     *
     * @param successAnswers
     * @throws IOException
     */
    public void startTest(Queue<Long> successAnswers, AtomicInteger counter) throws IOException {
        log.info("Start test");
        InputStream stream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(stream);
        socket.getOutputStream().write(Commands.START_MESSAGE);
        while (!socket.isClosed()) {
            long result = dataInputStream.readInt();
            log.info("Get answer from client: " + result);
            if (result == Commands.FINISH) {
                dataInputStream.close();
                socket.close();
                return;
            }

            counter.incrementAndGet();
            successAnswers.add(result);
        }
    }

    /**
     * Start login test
     *
     * @param successAnswers
     * @throws IOException
     */
    public void startLoginTest(Queue<Long> successAnswers, AtomicInteger counter) throws IOException {
        log.info("Start login test");
        InputStream stream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(stream);
        socket.getOutputStream().write(Commands.START_LOGIN);
        while (!socket.isClosed()) {
            long result = dataInputStream.readInt();
            log.info("Get answer from client: " + result);
            if (result == Commands.FINISH) {
                dataInputStream.close();
                socket.close();
                return;
            }

            counter.incrementAndGet();
            successAnswers.add(result);
        }
    }

    /**
     * Start login test
     *
     * @param successAnswers
     * @throws IOException
     */
    public void startRegisterTest(Queue<Long> successAnswers, AtomicInteger counter) throws IOException {
        log.info("Start register test");
        InputStream stream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(stream);
        socket.getOutputStream().write(Commands.REGISTER_USER);
        while (!socket.isClosed()) {
            long result = dataInputStream.readInt();
            log.info("Get answer from client: " + result);
            if (result == Commands.FINISH) {
                dataInputStream.close();
                socket.close();
                return;
            }

            counter.incrementAndGet();
            successAnswers.add(result);
        }
    }
}
