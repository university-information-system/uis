package at.ac.tuwien.inso.clock;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;

import java.lang.annotation.*;
import java.time.Clock;

@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MockBean(value = Clock.class, reset = MockReset.NONE)
public @interface FixedClock {

    String value() default "2000-01-01T10:00:00Z";

}
