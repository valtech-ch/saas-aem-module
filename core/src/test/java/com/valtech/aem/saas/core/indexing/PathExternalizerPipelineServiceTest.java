package com.valtech.aem.saas.core.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class PathExternalizerPipelineServiceTest {

  PathExternalizerPipeline testee;

  @Test
  void testGetExternalizedPaths(AemContext context) {
    context.registerInjectActivateService(new BarQuxPathExternalizer(),
        ImmutableMap.<String, Object>builder()
            .put("service.ranking", 2)
            .build());
    context.registerInjectActivateService(new BarBazPathExternalizer(),
        ImmutableMap.<String, Object>builder()
            .put("service.ranking", 1)
            .build());
    testee = context.registerInjectActivateService(new PathExternalizerPipelineService());
    List<String> results = testee.getExternalizedPaths("foo");
    assertThat(results, notNullValue());
    assertThat(results.size(), Is.is(3));
  }

  @Test
  void testGetExternalizedPaths_reversedOrder(AemContext context) {
    context.registerInjectActivateService(new BarQuxPathExternalizer(),
        ImmutableMap.<String, Object>builder()
            .put("service.ranking", 1)
            .build());
    context.registerInjectActivateService(new BarBazPathExternalizer(),
        ImmutableMap.<String, Object>builder()
            .put("service.ranking", 2)
            .build());
    testee = context.registerInjectActivateService(new PathExternalizerPipelineService());
    List<String> results = testee.getExternalizedPaths("foo");
    assertThat(results, notNullValue());
    assertThat(results.size(), Is.is(2));
  }

}
