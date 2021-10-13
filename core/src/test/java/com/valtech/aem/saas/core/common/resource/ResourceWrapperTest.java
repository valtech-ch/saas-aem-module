package com.valtech.aem.saas.core.common.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ResourceWrapperTest {

  Resource resourceWithParent, parent;
  Resource resourceWithGrandParent, parentNotAMatch, grandParent;
  Resource resourceWithNoParent;

  Resource testInput;
  Resource directChild1, directChild2;
  Resource grandChild1, grandChild2, grandChild3;

  @BeforeEach
  void setUp(AemContext context) {
    parent = context.create().resource("/goo",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/parent").build());
    resourceWithParent = context.create().resource("/goo/bar");
    grandParent = context.create().resource("/baz",
        ImmutableMap.<String, String>builder().put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "res/type/grandparent")
            .build());
    parentNotAMatch = context.create().resource("/baz/qux");
    resourceWithGrandParent = context.create().resource("/baz/qux/quz");
    resourceWithNoParent = context.create().resource("/no-parent-resource");

    testInput = context.create().resource("/foo");
    directChild1 = context.create().resource("/foo/child1");
    grandChild1 = context.create().resource("/foo/child1/grandchild1");
    directChild2 = context.create().resource("/foo/child2");
    grandChild2 = context.create().resource("/foo/child2/grandchild2");
    grandChild3 = context.create().resource("/foo/child2/grandchild3");
  }

  @Test
  void testGetDirectChildren() {
    ResourceWrapper resourceWrapper = testInput.adaptTo(ResourceWrapper.class);
    testAdaptTo(resourceWrapper);
    assertThat(resourceWrapper.getDirectChildren().count(), is(2L));
  }

  @Test
  void getDescendents() {
    ResourceWrapper resourceWrapper = testInput.adaptTo(ResourceWrapper.class);
    testAdaptTo(resourceWrapper);
    assertThat(resourceWrapper.getDescendents().count(), is(5L));
  }

  @Test
  void testGetParentWithResourceType() {
    testGetParentWithResourceType(resourceWithParent, "res/type/parent", true);
    testGetParentWithResourceType(resourceWithGrandParent, "res/type/grandparent", true);
    testGetParentWithResourceType(resourceWithGrandParent, "res/type/nonexisting", false);
    testGetParentWithResourceType(resourceWithNoParent, "res/type/grandparent", false);
  }

  private void testGetParentWithResourceType(Resource resource, String resType, boolean isPresent) {
    ResourceWrapper resourceWrapper = resource.adaptTo(ResourceWrapper.class);
    testAdaptTo(resourceWrapper);
    assertThat(resourceWrapper.getParentWithResourceType(resType).isPresent(), is(isPresent));
  }

  private void testAdaptTo(ResourceWrapper resourceWrapper) {
    assertThat(resourceWrapper, notNullValue());
    assertThat(resourceWrapper, instanceOf(ResourceWrapper.class));
  }
}
