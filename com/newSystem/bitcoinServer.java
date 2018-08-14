package com.newSystem;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class bitcoinServer extends Thread {
    public void run() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(9999), 0);
            httpServer.createContext("/", new Handler());
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }
    public class  Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            StringBuilder response = new StringBuilder();
            Map <String,String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            String method = params.get("method");
            switch (Integer.valueOf(method)) {
                case 1:
                    // method:1 --> send_to_address
                    String address = params.get("address");
                    String pid = params.get("pid");
                    response.append(MainFrame.bitcoinJSONRPCClient.send_to_address(address, pid));
                    break;
                case 2:
                    // method:2 --> track_product
                    String pid_to_track = params.get("pid");
                    response.append(MainFrame.bitcoinJSONRPCClient.track_product(pid_to_track));
                    break;
            }
            writeResponse(httpExchange, response.toString());
        }
    }
    // url 뒤 parameter들을 파싱 하기 위한 함수들. (ex. ip주소:port/?method=1&address=~~~&pid=~~)
    static public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1)
                result.put(pair[0], pair[1]);
            else
                result.put(pair[0], "");
        }
        return result;
    }
    static public void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
