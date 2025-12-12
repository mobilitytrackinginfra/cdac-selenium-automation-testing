# --- Dockerfile ---
# Base image with Maven + JDK
FROM maven:3.9.6-eclipse-temurin-17

# Install dependencies for Chrome
USER root
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    wget \
    gnupg \
    unzip \
    curl \
    fontconfig \
    fonts-liberation \
    libnss3 \
    libxss1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libgtk-3-0 \
    libgbm1 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    xdg-utils \
    && rm -rf /var/lib/apt/lists/*

# Install Google Chrome Stable
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Verify Chrome installation
RUN google-chrome --version

# Create app directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml ./

# Download dependencies (cache layer)
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Create directories for reports
RUN mkdir -p /app/target/surefire-reports /app/target/test-output

# Environment variables
ENV MAVEN_OPTS="-Xmx512m"

# Selenium Manager (included in Selenium 4.6+) will automatically download
# the correct ChromeDriver version, so no manual ChromeDriver installation needed!

# Create non-root user for running tests
RUN useradd -m -d /home/tester tester \
    && chown -R tester:tester /app

USER tester

# Default command: run maven tests
ENTRYPOINT ["mvn", "test"]
