# Geo Finder

## Dependencies
- Java 21
- Spring Boot 3.3.0

## How to run
- Clone the repository
- Run the application with the command `./mvnw spring-boot:run` or directly from your IDE
- Default port is 9001

## Endpoints
- The endpoints are documented using OpenAPI 3.1 and can be accessed at [this link.](https://app.swaggerhub.com/apis/PEKKARANTAAHO86/accenture-geo-app/1.0.0#/)
- The endpoints are also available in the `src/main/resources/openapi.yaml` file

## Usage
- The application communicates with a third-party API documented [here](https://gitlab.com/restcountries/restcountries) to get country information.
- The application has three endpoints:
  - `/api/v1/geoanalyzer` which fires off an asynchronous job.
  - `/api/v1/geoanalyzer/processed` which allows to check the status of the job or get the result if the job is done.
  - `/api/v1/geoanalyzer/resetJob` which resets the job status after a successful job.
- The result is a list of countries ordered by population density in descending order and the Asian country with most non-same region bordering countries.
- The response is in a JSON format, for more details, check the endpoints section.

## Flow
- The application can be in one of the following states:
  - `Job running` - The application is running a job.
  - `Job finished` - The application successfully finished the job, to reset back to Idle state, the user needs to invoke `/api/v1/geoanalyzer/resetJob`.
  - `Job failed` - The initialized job failed, you can immediately start a new one without manually resetting.
  - `Idle` - The application is ready to accept new jobs.
- There can only be one job running at a time.
