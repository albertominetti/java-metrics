package it.minetti.demo.service;

import it.minetti.demo.aop.MetricAndLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static java.math.BigInteger.ONE;

@Slf4j
@Service
public class RandomSumService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final BigInteger MAX_SIZE = BigInteger.valueOf(10_000);
    private static final int LENGTH = 100_000;

    public static final Supplier<BigInteger> randomGen =
            () -> new BigInteger(MAX_SIZE.bitLength(), secureRandom).mod(MAX_SIZE);

    @MetricAndLog
    public BigInteger run() {
        BigInteger total = Stream.generate(randomGen).limit(LENGTH).reduce(ONE, BigInteger::add);
        log.info("BigInteger sum is {}", total);
        return total;
    }

    @MetricAndLog
    public long runLong() {
        long total = ThreadLocalRandom.current().longs(LENGTH).sum();
        log.info("long sum is {}", total);
        return total;
    }

    /**
     * @deprecated because of the custom logging
     * use {@link #run()} instead.
     */
    @Deprecated(since = "you develop enterprise applications")
    public BigInteger runClassic() {
        long start = currentTimeMillis(); // to avoid
        log.debug("method runClassic starts..."); // to avoid

        BigInteger total = Stream.generate(randomGen).limit(LENGTH).reduce(ONE, BigInteger::add);
        log.info("Sum is {}", total);

        long end = currentTimeMillis(); // to avoid
        log.debug("...method runClassic ends [{}ms]", end - start); // to avoid

        return total;
    }
}
