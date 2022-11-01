# Homework 2 (Aditya Nair - anair38)

This project uses gRPC and REST API to query log files in an AWS S3 Bucket.


## Functionality
This project consists of a gRPC client, server and 2 AWS Lambdas.

### Components
1. **HashingLambda.py**: Uses python to implement a Lambda that is triggered when
   a new log file is added to the ```S3 bucket```(log.file.generator)
   When triggered HashingLambda hashes the contents of the LogFile into a HashMap
    ```
    <timstampInMicroSeconds, log>
    Eg:-
       A log message like the following:
       19:49:17.441 [scala-execution-.........
       will be hashed as
       <71357441: '19:49:17.441 [scala-execution-.........'
   ```
   This is done so that any input timestamp can be easily compared with the keys while using binary search.
   The result is saved the bucket under the folder ```hash_file/```

2. **RestLambda.py** : Also implemented in python, uses ```AWS API Gateway``` as trigger, so that it can be called over https.
   Accepts a ```POST``` request with the following parameters:
   - **startTime**: start of the time interval in ```hh:MM:ss.SSS```
   - **timeInterval**: the duration of the time interval in ```hh:MM:ss```
   - **pattern**: the regex pattern to look for in the log message
   
   Once these params are received the function calculates startTime and endTime and 
   looks for them in hash.pickle using ```Binary Search Algorithm```.
   The ```bin_search``` function is a modified version of ```Binary Search Algorithm``` that not 
   just looks for exact match but checks if the timestamp could exist between two timestamps.
   The log messages found in the interval are then matched against ```pattern``` and returned as count.
3. **GrpcClient.scala** : uses protobuf to serialize user provided ( ```application.conf``` ) arguments
   and rpc ```fetchLogsForInterval``` function on the local rpc server
4. **LogGrpcServer.scala** : contains ```fetchLogsForInterval``` function, that when invoked 
   unmarshalls the arguments from protobuf and uses ```java.net.Http```
   to perform a POST call with the arguments as json body to ```RestLambda.py```.
   The results returned from the lambda is the marshalled back to protobuff and returned to the client.


## Dependencies
* Scala 3.1.3
* SBT 1.7.2
* slf4j-api 1.7.5
* typesafe config 1.3.3
* scalapb-runtime-grpc

## How to Run this project
   1. Clone this repository
      ```console
      git clone https://github.com/aadi-nair/LogMessageLocator
      cd LogMessageLocator
      ```
   2. Clean and compile the project to generate scala classes from protobuf```src/main/protobuf/logfetcher.proto```:
      ```console
      sbt clean compile
      ```   
      scala files are generated under ```target/scala-3.1.3/src_managed/main/scalapb/logfetcher/```

   3. Deploy the AWS Lambda code as instructed in the Youtube Video.
   4. Edit program arguments under ```/src/main/resources/application.conf```
   5. Run the scala project:
         ```console
         sbt run
         ```
         You'll be prompted to choose between two scala files:
         ```console
         [info] loading settings for project root from build.sbt ...
         [info] set current project to LogMessageLocator (in build file:/Users/aditya/Documents/CS441/LogMessageLocator/)
         [info] compiling 7 Scala sources to /Users/aditya/Documents/CS441/LogMessageLocator/target/scala-3.1.3/classes ...
      
         Multiple main classes detected. Select one to run:
         [1] GrpcClient
         [2] LogGrpcServer

         Enter number: 

         ```
         Run ```LogGrpcServer``` first, then run ```GrpcClient``` \
      
         Output from running ```LogGrpcServer```:
         ```
         17:21:48.803 [grpc-nio-boss-ELG-1-1] DEBUG io.netty.channel.DefaultChannelId - -Dio.netty.machineId: 38:f9:d3:ff:fe:59:da:d5 (auto-detected)
         17:21:48.855 [grpc-nio-boss-ELG-1-1] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.allocator.type: pooled
         17:21:48.855 [grpc-nio-boss-ELG-1-1] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.threadLocalDirectBufferSize: 0
         17:21:48.856 [grpc-nio-boss-ELG-1-1] DEBUG io.netty.buffer.ByteBufUtil - -Dio.netty.maxThreadLocalCharBufferSize: 16384
         17:21:48.901 [sbt-bg-threads-1] INFO  LogGrpcServer - Server started, listening on 50051
         ```

        Warning: if you get the following error while running the project
        ```
        LogFetcherGrpc is already defined as object LogFetcherGrpc in ./LogMessageLocator/target/scala-3.1.3/src_managed/main/scalapb/logfetcher/LogFetcherGrpc.scala
        object LogFetcherGrpc {
        ```
      
        locate ```target/scala-3.1.3/src_managed/main/``` right click on ```main/``` and select:
        ```
        Mark Directory as > Unmark as Sources Root
       ```

    

## Youtube Demo Video: 
Youtube video demonstrating the steps to run and deploy this project
- https://youtu.be/9d9NW6DLO2g
