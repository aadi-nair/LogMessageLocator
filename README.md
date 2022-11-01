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
   

## Youtube Demo Video: 
- https://youtu.be/9d9NW6DLO2g
