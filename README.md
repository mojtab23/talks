# Appsmith Backend Takehome Exercise For Mojtaba Zarezadeh


## Objectives:

### Retrieves a list of talks planned for any given data range
- detailed: returns a list of talks
``` bash
curl -X GET --location "http://localhost:8080/talks/search?title=First&description=the&speakerId=u_1&planedStartFrom=1970-01-01T00:00:00.000-00:00&planedStartTo=1970-01-02T00:00:00.000-00:00&planedEndFrom=1971-01-01T00:10:00.000-00:00&planedEndTo=1971-01-01T00:40:00.000-00:00&startedFrom=2022-01-05T16:00:00.000-00:00&startedTo=2022-01-05T16:30:00.000-00:00&endedFrom=2022-01-05T16:30:00.000-00:00&endedTo=2022-01-05T17:00:00.000-00:00" \
-H "Accept: application/json"
```

- short: returns a page of talks
```bash
curl -X GET --location "http://localhost:8080/talks?from=1970-01-01T00:00:00.000-00:00&to=1970-01-02T00:00:00.000-00:00" \
    -H "Accept: application/json"
```

### Lists talks planned by a given speaker

A page of talks from the speaker:
```bash
curl -X GET --location "http://localhost:8080/talks/speaker/u_1" \
    -H "Accept: application/json"
```
`u_1` is a speaker ID (User ID)


### Registers an attendee for a talk

The user with ID `u_3` that has a `ATTENDEE` role subscribes to the talk with ID `t_1`

```bash
curl -X POST --location "http://localhost:8080/talks/t_1/subscribers" \
    -H "Content-Type: text/plain" \
    -d "u_3"
```

## How to Build

```bash
./gradlew build
```

### Testing

- Start a mongodb with docker compose:


```bash
docker-compose up
```

- Run the test:

```bash
./gradlew test
```

### Running

```bash 
java -jar build/libs/talks-0.0.1-SNAPSHOT.jar
```
