aws cloudformation create-stack --region us-east-1 --stack-name kindlepublishingservice-createtables --template-body file://configurations/tables.template.yml --capabilities CAPABILITY_IAM
aws dynamodb batch-write-item --request-items file://configurations/PublishingStatusData.json
