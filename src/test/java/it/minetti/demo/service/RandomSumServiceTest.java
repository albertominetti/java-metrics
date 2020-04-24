package it.minetti.demo.service;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

class RandomSumServiceTest {

    private RandomSumService service = new RandomSumService();

    @Test
    void run() {
        BigInteger total = service.run();
        assertThat(total, is(greaterThanOrEqualTo(ONE)));
    }

    @Test
    @SuppressWarnings("deprecation")
    void runClassic() {
        BigInteger total = service.runClassic();
        assertThat(total, is(greaterThanOrEqualTo(ONE)));
    }

}