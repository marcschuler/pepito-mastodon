# Mastodon Client for Pépito API

A unofficial mastodon client for the [Pépito-API](https://github.com/Clement87/Pepito-API?). Written in Java with Spring Boot and BigBone.

Currently used for the account [@PepitoTheCat@mastodon.social](https://mastodon.social/@PepitoTheCat).

## Build

You can use the Dockerfile to generate a docker image. 
There is an example [docker-compose](https://github.com/marcschuler/pepito-mastodon/blob/master/docker-compose.yml) file.

## Configuration

| Key                          | Application YAML             | Docker ENV                 | Example value                            |
|------------------------------|------------------------------|----------------------------|------------------------------------------|
| Pêpito API Url               | pepito-api.url               | PEPITPAPI_URL              | https://api.thecatdoor.com/sse/v1/events |
| Pêpito API Heartbeat Timeout | pepito-api.heartbeat-timeout | PEPITPAPI_HEARTBEATTIMEOUT | 22                                       |
| Mastodon Instance            | mastodon.instance            | MASTODON_INSTANCE          | mastodon.social                          |
| Mastodn Account Token        | mastodon.token               | MASTODON_TOKEN             | abcdefg...                               |

## TODO

- [ ] (More) unit Tests
- [ ] Public Docker Image (via Github Actions)
- [ ] When API support is available, add the video files

## License

This work is dual-licensed under MIT and WTFPL.
You can choose between one of them if you use this work.
