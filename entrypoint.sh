#!/bin/zsh
export $(cat .env | xargs)
java -jar build/libs/KindlePublishingService.jar
