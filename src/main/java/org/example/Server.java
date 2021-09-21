package org.example;

import Services.ReceiveMessageSerivice;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Server implements Runnable {

    Thread thread;
    String adress;
    String port;
    String target;
    Server(String adress, String port){
        thread = new Thread(this, "Additional Thread");
        System.out.println("New Thread for server was created " +
                thread);
        this.adress = adress;
        this.port = port;
        this.target = adress+":"+port;
        thread.start();
    }
    @Override
    public void run() {
        try {
            initServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initServer() throws IOException, InterruptedException {


        io.grpc.Server server = ServerBuilder.forPort(Integer.parseInt(port))
                .addService(new ReceiveMessageSerivice())
                .build();
        System.out.println("Server started on adress: " + adress);
        server.start();

        server.awaitTermination();
    }
}
