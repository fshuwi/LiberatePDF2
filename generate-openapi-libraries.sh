docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i /local/microservice/build/tmp/kapt3/classes/main/META-INF/swagger/liberatepdf2-microservice-0.1.yml -g java -o /local/openapi/java --additional-properties=apiPackage=de.debuglevel.liberatepdf2.api
docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate -i /local/microservice/build/tmp/kapt3/classes/main/META-INF/swagger/liberatepdf2-microservice-0.1.yml -g kotlin -o /local/openapi/kotlin --additional-properties=packageName=de.debuglevel.liberatepdf2.client