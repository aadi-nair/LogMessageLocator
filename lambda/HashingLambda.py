import json
import os
import pickle
from time import time
import boto3
import logging
import math
logger = logging.getLogger()
logger.setLevel(logging.INFO)



def toMicroseconds(timestamp):
    timestamp = timestamp.replace(".", ":")
    indice = 2
    inMicro = 0
    for i,part in enumerate(timestamp.split(":")[:-1]):
        inMicro += (int(part) * round(math.pow(60, (indice - i))) * 1000)
    
    return inMicro + int(timestamp.split(":")[-1])


def lambda_handler(event, context):

    logger.info('hashing lambda event {}'.format(event))
    accessKeyId= os.environ["ACCESS_KEY_ID"]
    secretKeyId = os.environ["SECRET_KEY_ID"]

    s3_obj = event["Records"][0]["s3"]
    pickleKeyPrefix = 'hash_file/'
    bucket = s3_obj['bucket']['name']
    key = s3_obj['object']['key']
    fileName = key.split('/')[-1]

    logger.info("Bucket: " + bucket)
    logger.info("Key: " + key)

    LOCAL_FILE_PATH = '/tmp/' + fileName
    s3_client = boto3.client('s3', aws_access_key_id=accessKeyId, aws_secret_access_key=secretKeyId)
    s3_client.download_file(bucket, key, LOCAL_FILE_PATH)
    logger.info("Downloaded file to: " + LOCAL_FILE_PATH)

    hash = {}
    with open(LOCAL_FILE_PATH, 'r') as f:
        data = f.read()
        for line in data.splitlines():
            hash[toMicroseconds(line.split(" ")[0])] = line

    output_file = pickleKeyPrefix + 'hash.pickle'
    serialized = pickle.dumps(hash)
    s3_client.put_object(Bucket=bucket, Key=output_file, Body=serialized)
    logger.info("Saved map pickle file to: " + output_file)

    return {
        "statusCode": 200,
    }

