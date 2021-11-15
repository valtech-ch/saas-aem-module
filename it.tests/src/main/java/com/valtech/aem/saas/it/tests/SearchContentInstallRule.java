package com.valtech.aem.saas.it.tests;

import com.adobe.cq.testing.client.PackageManagerClient;
import com.adobe.cq.testing.client.PackageManagerClient.Package;
import java.io.InputStream;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.util.ResourceUtil;
import org.apache.sling.testing.junit.rules.instance.Instance;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchContentInstallRule extends ExternalResource {

  public static final String PACKAGE_NAME = "saas.ui.content-1.0.0-SNAPSHOT.zip";
  private Logger logger = LoggerFactory.getLogger(SearchContentInstallRule.class);

  private final Instance quickstartRule;

  public SearchContentInstallRule(Instance quickstartRule) {
    this.quickstartRule = quickstartRule;
  }

  private Package contentPackage;

  @Override
  protected void before() throws Throwable {
    PackageManagerClient packageManagerClient = quickstartRule.getAdminClient(PackageManagerClient.class);
    InputStream packageInputStream = ResourceUtil.getResourceAsStream(PACKAGE_NAME);
    logger.info("Uploading package...");
    contentPackage = packageManagerClient.uploadPackage(packageInputStream, "sampleSaasContent");
    logger.info("Installing package...");
    contentPackage.install();
  }

  @Override
  protected void after() {
    if (contentPackage != null) {
      try {
        logger.info("uninstalling package...");
        contentPackage.unInstall();
        contentPackage.delete();
      } catch (ClientException e) {
        logger.error("Could not uninstall package.", e);
      }
    }
  }
}
