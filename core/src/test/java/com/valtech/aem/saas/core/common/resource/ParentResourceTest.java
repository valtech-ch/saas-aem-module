package com.valtech.aem.saas.core.common.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ParentResourceTest {

  Resource resourceWithParent, parent;

  Resource resourceWithGrandParent, parentNotAMatch, grandParent;

  Resource resourceWithNoParent;

  @BeforeEach
  void setUp(AemContext context) {
    parent = context.create().resource("/foo",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/parent").build());
    resourceWithParent = context.create().resource("/foo/bar");
    grandParent = context.create().resource("/baz",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/grandparent")
            .build());
    parentNotAMatch = context.create().resource("/baz/qux");
    resourceWithGrandParent = context.create().resource("/baz/qux/quz");
    resourceWithNoParent = context.create().resource("/no-parent-resource");
  }

  @Test
  void testGetParentWithResourceType() {
    assertThat(new ParentResource(resourceWithParent).getParentWithResourceType("res/type/parent").isPresent(),
        is(true));
    assertThat(
        new ParentResource(resourceWithGrandParent).getParentWithResourceType("res/type/grandparent").isPresent(),
        is(true));
    assertThat(
        new ParentResource(resourceWithGrandParent).getParentWithResourceType("res/type/nonexisting").isPresent(),
        is(false));
    assertThat(new ParentResource(resourceWithNoParent).getParentWithResourceType("res/type/grandparent").isPresent(),
        is(false));
  }
}
