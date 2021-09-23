package com.valtech.aem.saas.core.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import com.valtech.aem.saas.api.indexing.PathExternalizer;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class PathExternalizerPipelineServiceTest {

  PathExternalizerPipeline testee;

  @Test
  void testGetExternalizedPaths(AemContext context) {
    setUp(context);
    List<String> results = testee.getExternalizedPaths("foo");
    assertThat(results, notNullValue());
    assertThat(results.size(), Is.is(3));
  }

  private void setUp(AemContext context) {
    context.registerService(PathExternalizer.class, newPathExternalizer(1, s -> Arrays.asList("bar", "qux")));
    //this one should be ignored (not registered) due to duplicating rank
    context.registerService(PathExternalizer.class, newPathExternalizer(1, s -> Collections.singletonList("quz")));
    context.registerService(PathExternalizer.class, newPathExternalizer(2, s -> (StringUtils.equals(s, "bar"))
        ? Arrays.asList("bar", "baz")
        : Collections.singletonList(s)));
    testee = context.registerInjectActivateService(new PathExternalizerPipelineService());
  }

  private PathExternalizer newPathExternalizer(int rank, Function<String, List<String>> function) {
    return new PathExternalizer() {

      @Override
      public int getRank() {
        return rank;
      }

      @Override
      public List<String> externalize(String path) {
        return function.apply(path);
      }
    };
  }

}
