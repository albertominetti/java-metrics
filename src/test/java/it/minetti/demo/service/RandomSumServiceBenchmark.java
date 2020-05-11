package it.minetti.demo.service;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigInteger;

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
public class RandomSumServiceBenchmark {

    private RandomSumService service = new RandomSumService();

    @Benchmark
    public BigInteger runBenchmark() {
        return service.run();
    }

    @Benchmark
    @SuppressWarnings("deprecation")
    public BigInteger runClassicBenchmark() {
        return service.runClassic();
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(RandomSumServiceBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

/*
# JMH version: 1.23
# VM version: JDK 11.0.7, Java HotSpot(TM) 64-Bit Server VM, 11.0.7+8-LTS

Benchmark (higher is better)           Mode  Cnt   Score   Error  Units
RandomSumServiceBenchmark.run         thrpt    3  11,042 ± 2,444  ops/s
RandomSumServiceBenchmark.runClassic  thrpt    3  11,025 ± 3,032  ops/s
*/
}