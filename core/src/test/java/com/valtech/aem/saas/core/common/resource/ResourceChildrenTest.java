package com.valtech.aem.saas.core.common.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ResourceChildrenTest {

  Resource testInput;
  Resource directChild1, directChild2;
  Resource grandChild1, grandChild2, grandChild3;

  @BeforeEach
  void setUp(AemContext context) {
    testInput = context.create().resource("/foo");
    directChild1 = context.create().resource("/foo/child1");
    grandChild1 = context.create().resource("/foo/child1/grandchild1");
    directChild2 = context.create().resource("/foo/child2");
    grandChild2 = context.create().resource("/foo/child2/grandchild2");
    grandChild3 = context.create().resource("/foo/child2/grandchild3");

  }

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new ResourceChildren(null));
  }

  @Test
  void testGetDirectChildren() {
    assertThat(new ResourceChildren(testInput).getDirectChildren().count(), is(2L));
  }

  @Test
  void getDescendents() {
    assertThat(new ResourceChildren(testInput).getDescendents().count(), is(5L));
  }
}
