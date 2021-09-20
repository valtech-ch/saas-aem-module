package com.valtech.aem.saas.core.common.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ResourceConsumerTest {

  Resource testInput;
  Resource directChild1, directChild2;
  Resource grandChild1, grandChild2, grandChild3;

  Resource resourceWithParent, parent;
  Resource resourceWithGrandParent, parentNotAMatch, grandParent;
  Resource resourceWithNoParent;

  @BeforeEach
  void setUp(AemContext context) {
    testInput = context.create().resource("/foo");
    directChild1 = context.create().resource("/foo/child1");
    grandChild1 = context.create().resource("/foo/child1/grandchild1");
    directChild2 = context.create().resource("/foo/child2");
    grandChild2 = context.create().resource("/foo/child2/grandchild2");
    grandChild3 = context.create().resource("/foo/child2/grandchild3");

    parent = context.create().resource("/goo",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/parent").build());
    resourceWithParent = context.create().resource("/goo/bar");
    grandParent = context.create().resource("/baz",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/grandparent")
            .build());
    parentNotAMatch = context.create().resource("/baz/qux");
    resourceWithGrandParent = context.create().resource("/baz/qux/quz");
    resourceWithNoParent = context.create().resource("/no-parent-resource");

  }

  @Test
  void testNullArgument() {
    Assertions.assertThrows(NullPointerException.class, () -> new ResourceConsumer(null));
  }

  @Test
  void testGetDirectChildren() {
    assertThat(new ResourceConsumer(testInput).getDirectChildren().count(), is(2L));
  }

  @Test
  void getDescendents() {
    assertThat(new ResourceConsumer(testInput).getDescendents().count(), is(5L));
  }

  @Test
  void testGetParentWithResourceType() {
    assertThat(new ResourceConsumer(resourceWithParent).getParentWithResourceType("res/type/parent").isPresent(),
        is(true));
    assertThat(
        new ResourceConsumer(resourceWithGrandParent).getParentWithResourceType("res/type/grandparent").isPresent(),
        is(true));
    assertThat(
        new ResourceConsumer(resourceWithGrandParent).getParentWithResourceType("res/type/nonexisting").isPresent(),
        is(false));
    assertThat(new ResourceConsumer(resourceWithNoParent).getParentWithResourceType("res/type/grandparent").isPresent(),
        is(false));
  }
}
