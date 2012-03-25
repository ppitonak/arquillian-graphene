package org.jboss.arquillian.graphene.javascript;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface JavaScript {
    
    String value() default "";
    
    Class<? extends ExecutionResolver> methodResolver() default DefaultExecutionResolver.class;
}
