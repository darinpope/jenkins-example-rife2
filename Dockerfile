FROM cgr.dev/chainguard/jdk:openjdk-17.0.6

COPY build/dist/myapp-0.1.0-uber.jar .
EXPOSE 8080

ENTRYPOINT [ "java","-jar","myapp-0.1.0-uber.jar" ]