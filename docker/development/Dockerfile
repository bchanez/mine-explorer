FROM maven:3.9.9-eclipse-temurin-21

RUN apt-get update && apt-get install -y \
  curl \
  git \
  ca-certificates \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app
