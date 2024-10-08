### PastebinCliUi

Данный модуль отвечает за отправку запросов с "пользовательского" интерфейса на бэкенд. 

Есть 2 режима работы: **обычный** (отправка запросов вручную) и **benchmark** (проверка на нагрузку).

Для запуска **обычного** режима требуется просто запустить проект или запустить .jar файл без аргументов.

Для запуска режима **benchmark** требуется запустить проект или .jar файл с аргументами. Пример:

`java -jar pastebinCliUi.jar --bench c -bt q=120000 --threads 24 --max-text-size 50000 --min-text-size 1`

Информация:
```
usage: benchmark
 -b,--bench <arg>               вид теста (c - только создание, g - только
                                получение, x - и то, и то)
 -bt,--bench-type <arg>         первый аргумент - количество запросов (q)
                                или время тестирования (t), второй -
                                величина (количество или время). Пример:
                                q=10000
 -t,--threads <arg>             количество потоков
    --max-text-size <arg>       максимальный размер текста
    --min-text-size <arg>       минимальный размер текста
    --max-delay <arg>           максимальная задержка между запросами (в
                                мс), необязателен
    --min-delay <arg>           минимальная задержка между запросами (в
                                мс), необязателен
    --percentage-create <arg>   доля создания паст (должна быть больше 0 и
                                меньше 0.5), обязательна для -x
```

Пример тестирования:

```
Время начала: Sun Aug 18 15:53:42 MSK 2024
Время окончания: Sun Aug 18 15:58:22 MSK 2024
Среднее время выполнения запроса: 56 мс
Общее время выполнения: 280 секунд
Всего запросов: 120000
Максимальное время выполнения запроса: 977 мс
Минимальное время выполнения запроса: 7 мс
```