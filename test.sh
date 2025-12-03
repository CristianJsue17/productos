# test.sh
#!/bin/bash
docker run --rm \
  -v $(pwd):/app \
  -w /app \
  maven:3.9.5-eclipse-temurin-21 \
  mvn test