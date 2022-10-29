import json
import os
import pickle
import boto3
import logging
logger = logging.getLogger()
logger.setLevel(logging.INFO)

BUCKET_NAME = "log.file.processor"

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
            timestamp = line.split(" ")[0]
            message = line.split(" - ")[-1]
            hash[timestamp] = message
    
    output_file = pickleKeyPrefix + fileName + '.pickle'
    serialized = pickle.dumps(hash)
    s3_client.put_object(Bucket=bucket, Key=output_file, Body=serialized)
    logger.info("Saved map pickle file to: " + output_file)

    return {
        "statusCode": 200,
    }
