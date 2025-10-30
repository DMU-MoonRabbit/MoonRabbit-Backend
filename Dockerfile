FROM eclipse-temurin:21-jdk
VOLUME /tmp

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libleptonica-dev \
    tesseract-ocr-eng \
    tesseract-ocr-kor \
    && rm -rf /var/lib/apt/lists/*
    
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
