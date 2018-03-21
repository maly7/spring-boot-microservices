package io.echoseven.kryption;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ActiveProfiles
@SpringBootTest
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthIntegrationTest {

    @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles")
    String[] profiles() default {"local", "integration"};

    @AliasFor(annotation = SpringBootTest.class, attribute = "webEnvironment")
    WebEnvironment webEnv() default WebEnvironment.RANDOM_PORT;

    @AliasFor(annotation = SpringBootTest.class, attribute = "classes")
    Class<?>[] classes() default {AuthenticationApp.class, TestJwtConfig.class};
}
