# Service Monitoring Tool

This is a small status monitoring tool that checks the status of different services.

###### Important note
The tool does not offer an interface for user interaction. The system can only be operated via REST calls.

## How to start
To create a JAR file, run `./gradlew clean shadowJar`.

Generated JAR file can be found on `./build/libs/rex-svc-monitor-1.0.0-SNAPSHOT-fat.jar`

To start the tool, run `java -jar .\rex-svc-monitor-1.0.0-SNAPSHOT-fat.jar`.

The console will show on which port the system is deployed. Wait for a message like this one:

> Deploying in port 8080

Example:

![Port](assets/deploy-port.png)

## REST calls
The tool can be operated via these REST endpoints:

- HTTP GET: `localhost:8080/services` -> Returns all services and their status.
- HTTP POST: `localhost:8080/services` -> Add a new service to check with name and URL.
- HTTP DELETE: `localhost:8080/services/{id}` -> Removes the service with the specified id.

Only the HTTP POST endpoint needs to have request body. The POST endpoint needs to be supplied with the name and URL of the service in JSON format:

```
{
  "name": "kry",
  "url": "kry.se"
}
```

### Examples

###### HTTP POST

![POST](assets/post.png)

###### HTTP GET

![GET](/assets/get.png)

###### HTTP DELETE

![DELETE](/assets/delete.png)

## Background Polling
As a last feature, the tool polls all services saved in the system and update them of their current status.
Background polling happens every one minute.

## Support
For questions/clarifications, please contact me at rexwynnohay@gmail.com
