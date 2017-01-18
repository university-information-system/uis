package at.ac.tuwien.inso.clock;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.time.Clock;
import java.time.Instant;
import java.util.TimeZone;

import static org.mockito.Mockito.when;

/**
 * Thanks to http://blog.poznachowski.pl/2016/06/15/mockbean-spring-boot-missing-ingredient/
 */
public class FixedClockListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        FixedClock classFixedClock = AnnotationUtils.findAnnotation(testContext.getTestClass(), FixedClock.class);
        if (classFixedClock == null) {
            return;
        }
        mockClock(testContext, classFixedClock);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        FixedClock methodFixedClock = AnnotationUtils.findAnnotation(testContext.getTestMethod(), FixedClock.class);
        if (methodFixedClock == null) {
            return;
        }
        verifyClassAnnotation(testContext);
        mockClock(testContext, methodFixedClock);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        FixedClock methodFixedClock = AnnotationUtils.findAnnotation(testContext.getTestMethod(), FixedClock.class);
        if (methodFixedClock == null) {
            return;
        }
        verifyClassAnnotation(testContext);

        FixedClock classFixedClock = AnnotationUtils.findAnnotation(testContext.getTestClass(), FixedClock.class);
        mockClock(testContext, classFixedClock);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        FixedClock annotation = AnnotationUtils.findAnnotation(testContext.getTestClass(), FixedClock.class);
        if (annotation == null) {
            return;
        }
        //reset(testContext.getApplicationContext().getBean(Clock.class));
    }

    private void verifyClassAnnotation(TestContext testContext) {
        FixedClock classAnnotation = AnnotationUtils.findAnnotation(testContext.getTestClass(), FixedClock.class);
        if (classAnnotation == null) {
            throw new IllegalStateException("@FixedClock class level clock is missing.");
        }
    }

    private void mockClock(TestContext testContext, FixedClock fixedClock) {
        Instant instant = Instant.parse(fixedClock.value());
        Clock mockedClock = testContext.getApplicationContext().getBean(Clock.class);
        when(mockedClock.instant()).thenReturn(instant);
        when(mockedClock.getZone()).thenReturn(TimeZone.getDefault().toZoneId());
    }
}
