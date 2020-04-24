package it.minetti.demo.aop;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MetricsAspect {

    private static final Logger log = LoggerFactory.getLogger(MetricsAspect.class);

    @Autowired
    private MeterRegistry registry;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void isARestController() {
        //empty because it is just an expression to match a join-point
    }

    @Pointcut("execution(public * *(..))")
    public void isPublicMethod() {
        //empty because it is just an expression to match a join-point
    }

    @Pointcut("isARestController() && isPublicMethod()")
    public void isRestEndPoint() {
        //empty because it is just an expression to match a join-point
    }

    @Around("isRestEndPoint() || @annotation(it.minetti.demo.aop.MetricAndLog)")
    public Object timeSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
        log.trace("Around start {}", joinPoint);
        Signature signature = joinPoint.getStaticPart().getSignature();
        Logger declaringLog = LoggerFactory.getLogger(signature.getDeclaringType());
        String methodName = signature.getName();
        declaringLog.debug("method {} starts...", methodName);
        Timer.Sample sample = Timer.start(registry);
        String exceptionClass = "none";

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            exceptionClass = ex.getClass().getSimpleName();
            declaringLog.debug("...stop with error");
            throw ex;
        } finally {
            try {

                long durationNs = sample.stop(
                        Timer.builder(signature.getDeclaringTypeName() + "." + methodName)
                                .description(methodName)
                                .tags("exception", exceptionClass)
                                .publishPercentileHistogram(true)
                                .publishPercentiles(0.05, 0.5, 0.95)
                                .register(registry)
                );

                declaringLog.debug("...method {} ends [{}ns]", methodName, durationNs);
            } catch (Exception e) {
                log.trace("Exception during metrics", e);
            }
            log.trace("Around end {}", joinPoint);
        }
    }

}