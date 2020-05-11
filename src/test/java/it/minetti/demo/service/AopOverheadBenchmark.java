package it.minetti.demo.service;

import io.micrometer.core.annotation.Timed;
import it.minetti.demo.aop.MetricAndLog;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigInteger;
import java.util.Random;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
public class AopOverheadBenchmark {

    private final Service service = new Service();

    @Benchmark
    public BigInteger baseline() {
        return service.run();
    }

    @Benchmark
    public BigInteger withTimedAnnotation() {
        return service.runWithTimedAnnotation();
    }

    @Benchmark
    public BigInteger runWithMetricAndLogAnnotation() {
        // log level to OFF to avoid too much time in IO operations
        return service.runWithMetricAndLogAnnotation();
    }

    private static class Service {
        private static final Random random = new Random();

        public BigInteger run() {
            return new BigInteger(500, 9, random);
        }

        @Timed
        public BigInteger runWithTimedAnnotation() {
            return run();
        }

        @MetricAndLog
        public BigInteger runWithMetricAndLogAnnotation() {
            return run();
        }
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(AopOverheadBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
/*
# JMH version: 1.23
# VM version: JDK 11.0.7, Java HotSpot(TM) 64-Bit Server VM, 11.0.7+8-LTS

Benchmark (higher is better)                         Mode  Cnt    Score   Error  Units
AopOverheadBenchmark.baseline                       thrpt    5  152,309 ± 9,479  ops/s
AopOverheadBenchmark.runWithMetricAndLogAnnotation  thrpt    5  153,064 ± 8,669  ops/s
AopOverheadBenchmark.withTimedAnnotation            thrpt    5  147,554 ± 5,747  ops/s
 */
}
