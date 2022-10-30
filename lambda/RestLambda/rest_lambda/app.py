import json
import os, re
import pickle
import boto3
import math
import logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

LOCAL_FILE_PATH = 'hash_file/hash.pickle' 
BUCKET_NAME  = "log.file.processor"

def toMicroseconds(timestamp):
    timestamp = timestamp.replace(".", ":")
    indice = 2
    inMicro = 0
    split_timestamp = timestamp.split(":")
    for i,part in enumerate(split_timestamp[:-1] if len(split_timestamp) >3 else split_timestamp):
        inMicro += (int(part) * round(math.pow(60, (indice - i))) * 1000)
    
    if(len(split_timestamp) == 3):
        return inMicro
    return inMicro + int(timestamp.split(":")[-1])

def bin_search(arr, x): 
    low = 0
    high = len(arr) - 1
    mid = 0

    while low <= high:	
        mid = (high + low) // 2
        if arr[mid] < x and arr[mid + 1] > x:
            return mid + 1
        if arr[mid] < x:
            low = mid + 1
        elif arr[mid] > x:
            high = mid - 1
        else:
            return mid

    return -1


def lambda_handler(event, context):

    logger.info('hashing lambda event {}'.format(event))
    accessKeyId = os.environ["ACCESS_KEY_ID"]
    secretKeyId = os.environ["SECRET_KEY_ID"]

    if event["httpMethod"] == "GET":
        queryParams = event["queryStringParameters"]
        if all(k in queryParams for k in ("startTime","timeInterval", "pattern")):
            startTime = queryParams["startTime"]
            timeInterval = queryParams["timeInterval"]
            pattern = queryParams["pattern"]
            logger.info('startTime: {} timeInterval: {} pattern: {}'.format(startTime,timeInterval, pattern))

            s3_client = boto3.client('s3', aws_access_key_id=accessKeyId, aws_secret_access_key=secretKeyId)
            response = s3_client.get_object(Bucket=BUCKET_NAME, Key=LOCAL_FILE_PATH)
            hash_data = pickle.loads(response['Body'].read())
          
            startTimeInMicro = toMicroseconds(startTime)
            endTimeInMicro = startTimeInMicro + toMicroseconds(timeInterval)

            logTimestampsInMicro = list(hash_data.keys())
            if startTimeInMicro > logTimestampsInMicro[-1] or endTimeInMicro < logTimestampsInMicro[0]:
                return {
                    "statusCode": 404,
                    "message": "No logs for given time interval"
                }
            startIndex = bin_search(logTimestampsInMicro,startTimeInMicro)
            startIndex = startIndex if startIndex!=-1 else 0

            endIndex = bin_search(logTimestampsInMicro,endTimeInMicro)
            endIndex = endIndex if endIndex!=-1 else len(logTimestampsInMicro)

            logger.info("{} Log Messages found".format((endIndex-startIndex)))
   
            matches = []
            for i in range(startIndex, endIndex):
                log = hash_data[logTimestampsInMicro[i]]
                search_res = re.search(pattern, log)
                if search_res:
                    matches.append(log)
            
            log.info("{} log messages found".format(len(matches)))

            # if matches:
            return {
                "statusCode": 200,
                "body": json.dumps({'count':len(matches),'matches': matches})
            } 
            # else:
            #     return {
            #         "statusCode": 200,
            #         "body": json.dumps({'count':len(matches),'matches': matches})

            #         # "message": "No logs for given time interval"
            #     }
        else:
            return {
                "statusCode": 400,
                "message": "please enter all three parameters"
            }
    else:
        logger.info("Invalid route accessed")
        return {
            "statusCode": 400,
            "message": "invalid route"
        }