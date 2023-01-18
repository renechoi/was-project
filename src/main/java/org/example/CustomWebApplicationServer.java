package org.example;

import org.example.calculator.domain.Calculator;
import org.example.calculator.domain.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CustomWebApplicationServer {

    private final int port;

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
                try(InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                    HttpRequest httpRequest = new HttpRequest(bufferedReader);

                    if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
                        QueryStrings queryStrings = httpRequest.getQueryString();

                        int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
                        String operator = queryStrings.getValue("operator");
                        int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

                        int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
                        byte[] body = String.valueOf(result).getBytes();

                        HttpResponse response = new HttpResponse(dataOutputStream);
                        response.response200Header("application/json", body.length);
                        response.responseBody(body);
                    }
                }
            }

        }
    }
}
