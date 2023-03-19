# YourTopWords [![Build](https://github.com/arguvos/YourTopWords/actions/workflows/build.yml/badge.svg)](https://github.com/arguvos/YourTopWords/actions/workflows/build.yml) [![Test](https://github.com/arguvos/Transcriber/actions/workflows/test.yml/badge.svg)](https://github.com/arguvos/Transcriber/actions/workflows/test.yml)
YourTopWords is a web application designed to identify the most popular words that the user does not know.
The application contains 1000 most popular English words.
The user takes a test for knowledge of words.
After that, the application displays a list of all words that the user does not know and offers to upload them in the format of anki cards.
For anki cards, translation of words into the user's language is supported.

The service has a Backend (Java) and Frontend (React) part.
They communicate via REST API.

The list of words and the list of languages for translation are configured in the configuration file.
To generate the translation, api from the "Context Reverso" service is used.
And for the formation of anki cards, the AnkiTools4j library is used.

![Image](img/1.jpg?raw=true) ![Image](img/2.jpg?raw=true)

How the service works:
1. At startup, the application reads a file with words.
2. For each word, a translation and an example of use are formed. This data is stored in the cache. Several cache types are supported: hashmap, redis, memcache.
3. The user goes to the application website and passes the test, answering which words he knows and which he does not.
4. After the test, the user can display a list of unknown words and download the words along with their translations in anki format.

## Dependencies
YourTopWords service uses:
- java 11
- lombok
- springboot
- junit
- maven
- [AnkiTools4j](https://github.com/imvladikon/AnkiTools4j) 
- react
- [mui](https://mui.com/)



YourTopWords also uses the following services which can be started with docker-compose:
- [redis](https://hub.docker.com/r/bitnami/redis/)
- [memcache](https://hub.docker.com/_/memcached)

## Compile and run
For compile backend project and run test:
```bash
mvn package
```

For compile and run frontend:
```bash
cd frontend
npm install
npm start
```

Dependent services, can be run by docker-compose:
- redis
- memcache

## Configuration
Transcriber used for configuration application.properties file. There is some custom preference:
```
languages=de,ru                                     #supported words for translation
load.translate.at.start=false                       #download translate for words at start application
top.word.file.name=word.txt                         #name of file with words. Optional parametr

anki.dir=anki                                       #name of directory for anki files
cache=hashmap                                       #type of cache. Possible values: hashmap, redis, memcache. Default value: hashmap. Optional parametr

spring.cache.type=redis                             #propertys for redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=my_master_password

memcache.hostname:127.0.0.1                         #propertys for memcache
memcache.port:11211
```