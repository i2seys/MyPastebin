# Pet-project MyPastebin
Pet-проект Pastebin с использованием kafka и микросервисов.

Для корректной работы требуется:
1) Запустить kafka (https://kafka.apache.org/quickstart): например, как показано ниже (запускать в двух разных консолях). Топики создадутся автоматически.
```
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```
```
$ bin/kafka-server-start.sh config/server.properties
```
2) Запустить микросервисы pasteMicroservice и hashMicroservice (при ошибке подключения к Kafka в application.properties файлах обоих проектов требуется поменять ip на ip устройства, на котором запущена kafka.
3) Запустить pastebinCliUi (в режиме простого пользователя или в режиме тестирования).

**TODO**: 
- управлять микросервисами через docker/k8s
- добавить тесты