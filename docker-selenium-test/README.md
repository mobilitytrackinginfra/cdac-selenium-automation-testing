# Docker Selenium Test Project

This project contains Selenium WebDriver tests using TestNG that can run against a Selenium Grid.

## Project Structure

```
├── src/test/java/com/example/tests/
│   ├── BaseTest.java           # Base test class with WebDriver setup
│   ├── GoogleSearchTest.java   # Google search test cases
│   └── SimpleWebTest.java      # Simple website navigation tests
├── testng.xml                  # TestNG suite configuration
├── pom.xml                     # Maven dependencies and plugins
├── Dockerfile                  # Docker image for running tests
├── docker-compose.yml          # Docker Compose for tests (connects to local Grid)
└── docker-compose-with-grid.yml # Docker Compose with full Grid setup
```

## Prerequisites

- Java 11+
- Maven 3.6+
- Docker & Docker Compose
- Selenium Grid Hub running on your machine (port 4444)

## Running Tests

### Option 1: Run Locally with Maven (requires local Selenium Grid)

First, start your Selenium Grid Hub and Chrome Node, then:

```bash
mvn test
```

Or specify a custom Grid URL:

```bash
mvn test -Dselenium.grid.url=http://localhost:4444/wd/hub
```

### Option 2: Run in Docker (connects to your local Selenium Grid)

Make sure your Selenium Grid is running on the host machine at port 4444.

```bash
# Build and run tests
docker-compose up --build

# View test results in ./target directory
```

### Option 3: Run Everything in Docker (includes Selenium Grid)

This option starts Selenium Hub, Chrome Node, and runs tests - all in Docker:

```bash
# Start everything
docker-compose -f docker-compose-with-grid.yml up --build

# To stop and clean up
docker-compose -f docker-compose-with-grid.yml down
```

## Starting Selenium Grid Manually

If you want to run the Grid on your host machine:

```bash
# Start Hub
docker run -d -p 4444:4444 -p 4442:4442 -p 4443:4443 --name selenium-hub selenium/hub:4.15.0

# Start Chrome Node
docker run -d --shm-size="2g" -e SE_EVENT_BUS_HOST=<YOUR_HOST_IP> -e SE_EVENT_BUS_PUBLISH_PORT=4442 -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 selenium/node-chrome:4.15.0
```

Or use the provided docker-compose-with-grid.yml which sets up everything automatically.

## Test Cases

### GoogleSearchTest
- `testGoogleHomePageLoads`: Verifies Google homepage loads correctly
- `testGoogleSearch`: Tests searching on Google

### SimpleWebTest
- `testNavigateToWebsite`: Navigates to a demo website and verifies content
- `testClickLink`: Tests clicking links and form elements

## View Test Reports

After running tests, find the reports at:
- `target/surefire-reports/` - TestNG XML and HTML reports
- `target/surefire-reports/emailable-report.html` - Summary report

## Configuration

- Grid URL can be set via system property: `-Dselenium.grid.url=http://your-grid:4444/wd/hub`
- Default Grid URL: `http://localhost:4444/wd/hub`

