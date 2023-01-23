package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomWebApplicationServer {

    private final int port;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CustomWebApplicationServer(int port) {
        this.port = port;
    }

    private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[CustomWebApplicationServer] started {} port", port);

            Socket clientSocket;
            logger.info("[CustomWebApplicationServer] waiting for client");

            while ((clientSocket = serverSocket.accept()) != null) {
                logger.info("[CustomWebApplicationServer] client connected!");
                executorService.execute(new ClientRequestHandler(clientSocket));
            }
        }
    }
}
