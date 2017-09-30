package com.tugo.app.locationtracker;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class EmbeddedWebServer
{
  private final String staticPrefix = "/static";

  private final int port;
  private Server server;
  private String staticResourceDirectory = "static";

  public EmbeddedWebServer(int port)
  {
    this.port = port;
  }

  private ServletHolder newStaticServlet()
  {
    ServletHolder sh = new ServletHolder(new StaticServlet());
    sh.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
    return sh;
  }

  void start() throws Exception
  {
    String[] packages = new String[] { "com.tugo.app.locationtracker.resources" };
    ResourceConfig config = new PackagesResourceConfig(packages);
    ServletHolder servlet = new ServletHolder(new ServletContainer(config));
    servlet.setInitParameter("jersey.config.server.provider.packages",
        "com.tugo.app.locationtracker.resources");

    server = new Server(port);
    ServletContextHandler context = new ServletContextHandler(server, "/*");
    context.addServlet(servlet, "/*");
    try {
      server.start();
      server.join();
    } finally {
      server.destroy();
    }
  }

  private class StaticServlet extends DefaultServlet
  {
    private static final long serialVersionUID = 1L;

    @Override
    public Resource getResource(String pathInContext)
    {
      if (pathInContext.startsWith(staticPrefix + "/")) {
        pathInContext = pathInContext.substring(staticPrefix.length());
      }
      Resource r = super.getResource(pathInContext);

      if (r == null && !pathInContext.endsWith("/") && !pathInContext.endsWith(".gz")
        && !pathInContext.endsWith(".zip") && !pathInContext.endsWith(".json")) {
        r = super.getResource("/index.html");
      }
      return r;
    }
  }

}
