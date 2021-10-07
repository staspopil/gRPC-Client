package org.example;

import com.example.grpc.Service;
import com.example.grpc.messageReceiverGrpc;
import com.example.grpc.subscribeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.imageio.ImageTranscoder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Client
{
    private static String name;
    private static String port;
    private static String message;
    private static String topic;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException, InterruptedException {


        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext().build();

        System.out.println("Enter your name");
        name = scanner.nextLine();
        System.out.println("Enter port");
        port = scanner.nextLine();
        System.out.println("Enter Topic");
        topic = scanner.nextLine();
        String clientAdress = InetAddress.getLocalHost().getHostAddress()+":"+port;

        new Server(InetAddress.getLocalHost().getHostAddress(),port);



        //SubscribePart
        subscribeServiceGrpc.subscribeServiceBlockingStub stub1 = subscribeServiceGrpc.newBlockingStub(channel);
        Service.subscribeRequest.Builder subscribeRequestBuilder = Service.subscribeRequest.newBuilder();
        Service.subscribeRequest request1 = subscribeRequestBuilder.setAdress(clientAdress).setTopic(topic).build();
        Iterator<Service.subsribeResponse> response1 = stub1.subscribe(request1);
        System.out.println("U was subscribed on topic: " + topic);
        //Sender Part
        messageReceiverGrpc.messageReceiverBlockingStub stub = messageReceiverGrpc.newBlockingStub(channel);
        Service.receivedMessage.Builder requestBuilder = Service.receivedMessage.newBuilder();

//        if (request1.equals("Succces"))
//        {
//            System.out.println("You was connected to chat, WELkOME");
//        }

        int i = 0;
        List<Service.receivedMessage> a = response1.next().getMessageList();
        for (Service.receivedMessage mes : a)
        {
            System.out.println(mes.getName()+": "+mes.getBody());
        }
//        while (response1.hasNext())
//        {
//
//        }
        i = 0;
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
            request = requestBuilder.setName(name).setBody(message).setTopic(topic).build();
            response = stub.receive(request);
        }
    }
}
