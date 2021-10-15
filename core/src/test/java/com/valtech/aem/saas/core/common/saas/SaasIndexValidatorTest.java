package com.valtech.aem.saas.core.common.saas;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

class SaasIndexValidatorTest {

  @Test
  void testValidate() {
    SaasIndexValidator saasIndexValidator = SaasIndexValidator.getInstance();
    assertThrows(IllegalArgumentException.class, () -> saasIndexValidator.validate(null));
    assertThrows(IllegalArgumentException.class, () -> saasIndexValidator.validate(""));
    assertDoesNotThrow(() -> saasIndexValidator.validate("foo"));
  }

  @Test
  void testSingleton() {
    SaasIndexValidator instance1 = SaasIndexValidator.getInstance();
    SaasIndexValidator instance2 = SaasIndexValidator.getInstance();
    assertThat(instance1, Is.is(instance2));
  }

}
