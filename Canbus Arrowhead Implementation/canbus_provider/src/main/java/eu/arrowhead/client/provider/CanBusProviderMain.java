/*
 *  Copyright (c) 2018 AITIA International Inc.
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.client.provider;

import eu.arrowhead.client.common.ArrowheadClientMain;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.misc.ClientType;
import eu.arrowhead.client.common.misc.TypeSafeProperties;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ServiceConfigurationError;
import java.util.Set;

public class CanBusProviderMain extends ArrowheadClientMain {

  static PublicKey authorizationKey;
  static PrivateKey privateKey;

  private static boolean NEED_AUTH;
  private static boolean NEED_ORCH;
  private static boolean FROM_FILE;
  private static String AUTH_ENTRY_FILE;
  private static String SERVICE_ENTRY_FILE;
  private static String SR_BASE_URI;
  private static String AUTH_URI;

  private ServiceRegistrator sr;

  public static void main(String[] args) {
    new CanBusProviderMain(args);
  }

  /**
   * CanBusProviderMain will be starting the server and register all necessary infromation (ServiceRegistry and AuthRegistry).
   * @param args
   */
  private CanBusProviderMain(String[] args) {
    Set<Class<?>> classes = new HashSet<>(Arrays.asList(CanApiResource.class));
    String[] packages = {"eu.arrowhead.client.common"};
    init(ClientType.PROVIDER, args, classes, packages);
    for (String arg : args) {
      switch (arg) {
        case "-ff":
          FROM_FILE = true;
          break;
        case "-auth":
          NEED_AUTH = true;
          break;
        case "-orch":
          NEED_ORCH = true;
          break;
      }
    }
    if (isSecure && NEED_ORCH) {
      throw new ServiceConfigurationError("The Store registration feature can only be used in insecure mode!");
    }
    setupBaseUris();
    sr = new ServiceRegistrator(SR_BASE_URI);
    sr.createServiceEntriesFromFile(SERVICE_ENTRY_FILE);
    sr.registerAllServices();
    if (NEED_AUTH) {
      AuthorisationRegistrator ar = new AuthorisationRegistrator(AUTH_URI);
      ar.createAuthorisationEntriesFromFile(AUTH_ENTRY_FILE, sr);
      ar.registerAuthorisationEntries();
    }/*
    if (NEED_ORCH) {
      registerToStore();
    }*/
    listenForInput();
  }

  /**
   * This function prepares the URI's that will be needed and also reads in the service entry file and auth entry file.
   */
  private void setupBaseUris(){
    TypeSafeProperties props = Utility.getProp();
    //Service registry address (srAuth)
    String srAddress = props.getProperty("sr_address", "0.0.0.0");
    int srPort = isSecure ? props.getIntProperty("sr_secure_port", 8443) : props.getIntProperty("sr_insecure_port", 8442);
    SR_BASE_URI = Utility.getUri(srAddress, srPort, "serviceregistry", isSecure, false);
    //Auth registry address (arAuth)
    String authAddress = props.getProperty("auth_address", "0.0.0.0");
    int authPort = isSecure ? props.getIntProperty("auth_secure_port", 8445) : props.getIntProperty("auth_insecure_port", 8444);
    AUTH_URI = Utility.getUri(authAddress, authPort, "authorization/mgmt/intracloud", isSecure, false);
    // Read in service entry file from default.conf
    SERVICE_ENTRY_FILE = props.getProperty("sr_entry", "config/serviceList.json");
    // Read in auth entry file from default.conf
    AUTH_ENTRY_FILE = props.getProperty("auth_entry", "config/consumerList.json");
  }

  /**
   * This function will close the server and unregister all services from the ServiceRegistry in Arrowhead.
   */
  @Override
  protected void shutdown() {
    if (server != null) {
      sr.unregisterAllFromServiceRegistry();
      server.shutdownNow();
    }
    System.out.println("Provider Server stopped");
    System.exit(0);
  }

  /* TODO: Fix for application, so it registers all consumers.*/
  /*private void registerToAuthorization() {
    String authAddress = props.getProperty("auth_address", "0.0.0.0");
    int authPort = isSecure ? props.getIntProperty("auth_secure_port", 8445) : props.getIntProperty("auth_insecure_port", 8444);
    String authUri = Utility.getUri(authAddress, authPort, "authorization/mgmt/intracloud", isSecure, false);
    try {
      Utility.sendRequest(authUri, "POST", authEntry);
      System.out.println("Authorization registration is successful!");
    } catch (ArrowheadException e) {
      e.printStackTrace();
      System.out.println("Authorization registration failed!");
    }

  }*/

  /* TODO: Fix for application if needed; Should run on safe anyhow so orch shouldn't be necessary.
  /*private void registerToStore() {
    String orchAddress = props.getProperty("orch_address", "0.0.0.0");
    int orchPort = props.getIntProperty("orch_port", 8440);
    String orchUri = Utility.getUri(orchAddress, orchPort, "orchestrator/mgmt/store", false, false);
    try {
      Utility.sendRequest(orchUri, "POST", storeEntry);
      System.out.println("Store registration is successful!");
    } catch (ArrowheadException e) {
      e.printStackTrace();
      System.out.println("Store registration failed!");
    }
  }*/

}
