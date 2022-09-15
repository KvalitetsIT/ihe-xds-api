![Build Status](https://github.com/KvalitetsIT/ihe-xds-api/workflows/CICD/badge.svg)
# ihe-xds-api

Template repository showing how to be a good Java Spring Boot citizen in a k8s cluster.

## A good citizen

Below is a set of recommendations for being a good service. The recommendations are not tied to a specific language or 
framework.

1. Configuration through environment variables.
2. Expose readiness endpoint
3. Expose endpoint that Prometheus can scrape
4. Be stateless
5. Support multiple instances
6. Always be in a releasable state
7. Automate build and deployment.

Some of above recommendations are heavily inspired by [https://12factor.net/](https://12factor.net/). It is recommended 
read [https://12factor.net/](https://12factor.net/) for more inspiration and further details. Some points go 
further than just being a good service and also touches areas like operations.

## Getting started

Run `./setup.sh GIT_REPOSITORY_NAME`.

Above does a search/replace in relevant files. 

## Endpoints

### Service

The service is listening for connections on port 8080.

Spring boot actuator is listening for connections on port 8081. This is used as prometheus scrape endpoint and health monitoring. 

Prometheus scrape endpoint: `http://localhost:8081/actuator/prometheus`  
Health URL that can be used for readiness probe: `http://localhost:8081/actuator/health`

### Documentation

Documentation of the API is build as a separate Docker image. Documentation is build using Swagger. The documentation 
image is post-fixed with `-documentation`. The file `documentation/docker/compose/docker-compose.yml` contains a  setup 
that starts both the service and documentation image. The documentation can be accessed at `http://localhost/test` 
and the service can be called through the Swagger UI. 

In the docker-compose setup is also an example on how to set custom endpoints for the Swagger documentation service.

## Configuration

| Environment variable | Description                                                                                                                                                                       | Required |
|----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| LOG_LEVEL            | Log Level for applikation  log. Defaults to INFO.                                                                                                                                 | No       |
| LOG_LEVEL_FRAMEWORK  | Log level for framework. Defaults to INFO.                                                                                                                                        | No       |
| CORRELATION_ID       | HTTP header to take correlation id from. Used to correlate log messages. Defaults to "x-request-id".                                                                              | No       |
| redis.host           | Name for the redis host. Defaults t0 "redis"                                                                                                                                      | No       |
| redis.port           | The port that Redis is running on. Defaults to "6973"                                                                                                                             | No       |
| redis.host           | Redis objects time to live. Defaults to 86400000 miliseconds / 1 day                                                                                                              | No       |
| type.code.codes      | 	Example: 53576-5;74468-0;74465-6;11502-2;56446-8                                                                                                                                 | No       |
| type.code.names      | 	Example: Personal health monitoring report Document;Questionnaire Form Definition Document;Questionnaire Response Document;LABORATORY REPORT.TOTAL;Appointment Summary Document; | No       |
| type.code.scheme     | 	Example: 2.16.840.1.113883.6.1                                                                                                                                                   | No       |
| format.code.codes    | 	Example: urn:ad:dk:medcom:phmr:full;urn:ad:dk:medcom:qfdd:full                                                                                                                   | No       |
| format.code.names    | 	Example: DK PHMR schema;DK QFDD schema;DK QRD schema;                                                                                                                            | No       |
| format.code.scheme   | 	Example:  1.2.208.184.100.10                                                                                                                                                     | No       |
| event.code.scheme.codes    | 	Example: 1.2.208.176.2.1;1.2.208.176.2.4;                                                                                                                                        | No       |
| event.code.scheme.names     | 	Example: NPU;Episode of care label;                                                                                                                                              | No       |
| event.code.scheme     | 	Example: 2.16.840.1.113883.6.1                                                                                                                                                   | No       |
| healthcarefacilitytype.code.codes | 	Example: N/A;264372000;20078004;554221000005108;554031000005103;                                                                                                                 | No       |
| healthcarefacilitytype.code.names  | 	Example: N/A;apotek;behandlingscenter for stofmisbrugere;bosted;diætistklinik;ergoterapiklinik;                                                                                  | No       |
| healthcarefacilitytype.code.scheme   | 	Example: 2.16.840.1.113883.6.96                                                                                                                                                  | No       |
| practicesetting.code.codes     | 	Example: N/A;408443003;394577000;                                                                                                                                                | No       |
| practicesetting.code.names    | 	Example: N/A;almen medicin;anæstesiologi;arbejdsmedicin;børne- og ungdomspsykiatri;                                                                                              | No       |
| practicesetting.code.scheme   | 	Example: 2.16.840.1.113883.6.96                                                                                                                                                  | No       |
| class.code.codes    | 	Example: 001                                                                                                                                                                     | No       |
| class.code.names    | 	Example: Clinical report;                                                                                                                                                        | No       |
| class.code.scheme     | 	Example: 1.2.208.184.100.9                                                                                                                                                       | No       |
| object.type.codes   | 	Example: STABLE                                                                                                                                                                  | No       |
| object.type.names   | 	Example: Stable                                                                                                                                                                  | No       |
