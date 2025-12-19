# Use Maven with JDK 11 as base image
FROM maven:3.9.5-eclipse-temurin-11

# Set working directory
WORKDIR /app

# Copy pom.xml first (for better caching of dependencies)
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
COPY testng.xml .

ENV SELENIUM_GRID_URL=http://host.docker.internal:4444/wd/hub

# Default command to run tests
# The selenium.grid.url will be passed as an environment variable
CMD sh -c "mvn test -Dselenium.grid.url=$SELENIUM_GRID_URL"

