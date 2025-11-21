package com.appiancs.swiftmtconverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.appiancorp.suiteapi.expression.annotations.Category;

/**
 * Custom category annotation for SWIFT Message functions.
 * This annotation groups all SWIFT message conversion functions
 * together in the Appian Expression Editor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Category("swiftMessageCategory")
public @interface SwiftMessageCategory {
}
