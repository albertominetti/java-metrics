package it.minetti.demo;

import it.minetti.demo.service.RandomSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class OnDemandController {

    @Autowired
    RandomSumService randomSumService;

    Random random = new Random();

    @GetMapping("/run")
    public void run() {
        randomSumService.run();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/hi")
    public String hi() {
        switch (random.nextInt(5)) {
            case 1:
                throw new IllegalArgumentException("hi");
            case 2:
                throw new UnsupportedOperationException("hi");
            default:
                return "Hi!";
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(IllegalArgumentException ee) {
        // nothing
    }


    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void handle(UnsupportedOperationException ee) {
        // nothing
    }
}
