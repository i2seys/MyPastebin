package ru.pastebin.cli.benchmark.bench;

import ru.pastebin.cli.benchmark.BenchType;

import java.util.*;

public abstract class Bench {
    private final List<Long> queriesExecutionTime;
    private long benchTimeMs;
    private final BenchConfiguration benchConfiguration;
    private long benchStartTime;
    private long benchFinishTime;
    private Random rand;

    private final List<List<Long>> threadsQueriesExecutionTime;

    public Iterator<Long> getQueriesExecutionTime() {return queriesExecutionTime.iterator();}

    protected long getBenchTimeMs() {return benchTimeMs;}

    protected BenchConfiguration getBenchConfiguration() {return benchConfiguration;}

    protected long getBenchStartTime() {return benchStartTime;}

    protected long getBenchFinishTime() {return benchFinishTime;}

    public Bench(BenchConfiguration benchConfiguration) {
        this.benchConfiguration = benchConfiguration;
        this.queriesExecutionTime = new LinkedList<>();
        this.threadsQueriesExecutionTime = Collections.synchronizedList(new LinkedList<>());
        this.rand = new Random();
    }

    /**
     * Запуск бенчмарка от начала и до конца (с учётом общего времени).
     * */
    public final void runBench() {
        // Действие перед стартом потоков
        beforeBench();
        // Инициализация потоков
        List<Thread> threadsToRun = new LinkedList<>();
        int benchTimeOrQueriesValue = (int)(benchConfiguration.benchTypePair().value() / benchConfiguration.threads());
        for (int i = 0; i < benchConfiguration.threads(); i++) {
            threadsToRun.add(new Thread(() -> {
                try {
                    runBenchSingleThread(benchTimeOrQueriesValue);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        // Время начала теста
        benchStartTime = now();
        // Запуск потоков
        threadsToRun.forEach(Thread::start);
        // Ожидание окончания работы потоков
        for (Thread bench: threadsToRun) {
            try {
                bench.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Время окончания тестов
        benchFinishTime = now();
        benchTimeMs = benchFinishTime - benchStartTime;
        // Дейтсвие после окончания работы
        afterBench();
        // Сохранение времени выполнения всех потоков
        queriesExecutionTime.addAll(
                threadsQueriesExecutionTime.stream().flatMap(Collection::stream).toList()
        );

        System.out.println("Время начала: " + new Date(benchStartTime));
        System.out.println("Время окончания: " + new Date(benchFinishTime));
        System.out.printf("Среднее время выполнения запроса: %d мс%n",
                queriesExecutionTime.stream().reduce(Long::sum).get() / queriesExecutionTime.size());
        System.out.printf("Общее время выполнения: %d секунд%n", (benchTimeMs) / 1000);
        System.out.printf("Всего запросов: %d%n", queriesExecutionTime.size());
        System.out.printf("Максимальное время выполнения запроса: %d мс%n",
                queriesExecutionTime.stream().max(Long::compare).get());
        System.out.printf("Минимальное время выполнения запроса: %d мс%n",
                queriesExecutionTime.stream().min(Long::compare).get());
    }

    /** Описывает одно действие для bench (получение заметки, создание заметки) */
    protected abstract void singleBenchAction();

    /** Запускается перед началом бенча. Можно переопределять для добавления логики. */
    protected void beforeBench() {}

    /** Запускается после окончания бенча. Можно переопределять для добавления логики. */
    protected void afterBench() {
    }

    private void runBenchSingleThread(int benchTimeOrQueriesValue) throws InterruptedException {
        List<Long> localQueriesExecutionTime = new LinkedList<>();
        boolean hasDelay = benchConfiguration.minDelay() != null && benchConfiguration.maxDelay() != null;
        if (benchConfiguration.benchTypePair().benchType() == BenchType.BENCH_TIME) {
            // Запуск bench с таймером
            while (this.benchStartTime > now() - benchConfiguration.benchTypePair().value()) {
                benchWithTimerAndDelay(localQueriesExecutionTime, hasDelay);
            }
        } else {
            // Запуск bench с количеством.
            for (int i = 0; i < benchTimeOrQueriesValue; i++) {
                benchWithTimerAndDelay(localQueriesExecutionTime, hasDelay);
            }
        }
        threadsQueriesExecutionTime.add(localQueriesExecutionTime);
    }

    private void benchWithTimerAndDelay(List<Long> localQueriesExecutionTime, boolean hasDelay) throws InterruptedException {
        long startTimeLocal = now();
        singleBenchAction();
        localQueriesExecutionTime.add(now() - startTimeLocal);

        if (hasDelay) {
            int threadSleepTimeMs = rand.nextInt(benchConfiguration.minDelay(), benchConfiguration.maxDelay());
            Thread.sleep(threadSleepTimeMs);
        }
    }

    private long now() {
        return System.currentTimeMillis();
    }
}
