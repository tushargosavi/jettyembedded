package com.tugo.app.locationtracker;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class Main
{
  public static void main(String[] args) throws Exception
  {
    Options options = new Options();
    options.addOption("address", true, "Address DTGateway should listen to, specified in arg as [<ip-address>:]<port>.");
    options.addOption("saveaddress", false, "Saves the address in configuration so that it will be used as default in the next run");
    options.addOption("service", false, "Tell DTGateway it's running as a service");
    options.addOption("findport", false, "Find an available port to listen to if the specified port is not available and write the listen address to path specified by environment variable DT_GATEWAY_ADDRESS_FILE");

    CommandLineParser parser = new BasicParser();
    CommandLine line = parser.parse(options, args);

    String overrideAddr = null;
    if (line.hasOption("address")) {
      overrideAddr = line.getOptionValue("address");
    }

    EmbeddedWebServer embeddedWebServer = new EmbeddedWebServer(8080);
    embeddedWebServer.start();
  }
}