package com.github.maly7;

import com.github.maly7.support.WithAuthenticatedUserSecurityContextFactory;
import com.github.maly7.support.WithAuthenticatedUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthenticatedUserSecurityContextFactory.class)
public @interface WithAuthenticatedUser {
    String username() default "test-user";
}
