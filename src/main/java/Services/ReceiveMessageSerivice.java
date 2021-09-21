package Services;


import com.example.grpc.Service;
import com.example.grpc.messageReceiverGrpc;
import com.example.grpc.subscribeServiceGrpc;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;

import static java.lang.System.in;

public class ReceiveMessageSerivice extends messageReceiverGrpc.messageReceiverImplBase
{
    @Override
    public void receive(Service.receivedMessage request, StreamObserver<Service.newResponse> responseObserver)
    {
        Service.newResponse.Builder responseBuilder = Service.newResponse.newBuilder();
        Service.newResponse response = responseBuilder.setIsSucces("Succces").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println(request.getName()+": "+request.getBody()+"\n");
    }

}