package org.example;

import com.example.grpc.Service;
import com.example.grpc.messageReceiverGrpc;
import com.example.grpc.subscribeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Scanner;

public class Client
{
    private static String name;
    private static String port;
    private static String message;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException, InterruptedException {


        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext().build();

        System.out.println("Enter your name");
        name = scanner.nextLine();
        System.out.println("Enter port");
        port = scanner.nextLine();
        String clientAdress = InetAddress.getLocalHost().getHostAddress()+":"+port;

        new Server(InetAddress.getLocalHost().getHostAddress(),port);



        //SubscribePart
        subscribeServiceGrpc.subscribeServiceBlockingStub stub1 = subscribeServiceGrpc.newBlockingStub(channel);
        Service.subscribeRequest.Builder subscribeRequestBuilder = Service.subscribeRequest.newBuilder();
        Service.subscribeRequest request1 = subscribeRequestBuilder.setAdress(clientAdress).build();
        Service.subsribeResponse response1 = stub1.subscribe(request1);

        //Sender Part
        messageReceiverGrpc.messageReceiverBlockingStub stub = messageReceiverGrpc.newBlockingStub(channel);
        Service.receivedMessage.Builder requestBuilder = Service.receivedMessage.newBuilder();

        if (request1.equals("Succces"))
        {
            System.out.println("You was connected to chat, WELkOME");
        }

        Service.receivedMessage request;
        Service.newResponse response;

        while (true)
        {
            message = scanner.nextLine();
            if (message.equals("exit"))
            {
                System.out.println("Bye bye...");
                channel.shutdown();
            }
            request = requestBuilder.setName(name).setBody(message).build();
            response = stub.receive(request);
        }
    }
}
