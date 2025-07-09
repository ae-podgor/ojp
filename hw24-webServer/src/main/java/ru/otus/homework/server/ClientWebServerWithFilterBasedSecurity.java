package ru.otus.homework.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.homework.crm.service.DBServiceClient;
import ru.otus.homework.helpers.FileSystemHelper;
import ru.otus.homework.services.TemplateProcessor;
import ru.otus.homework.services.UserAuthService;
import ru.otus.homework.servlet.AuthorizationFilter;
import ru.otus.homework.servlet.ClientsApiServlet;
import ru.otus.homework.servlet.ClientsServlet;
import ru.otus.homework.servlet.LoginServlet;

import java.util.Arrays;

public class ClientWebServerWithFilterBasedSecurity implements ClientWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private final ObjectMapper mapper;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final UserAuthService authService;
    private final DBServiceClient dbServiceClient;

    public ClientWebServerWithFilterBasedSecurity(int port, UserAuthService authService, ObjectMapper mapper,
                                                  TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.authService = authService;
        this.mapper = mapper;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    @Override
    public Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(authService)), "/");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/clients", "/api/clients"));

        server.setHandler(sequence);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor)),
                "/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientsApiServlet(dbServiceClient, mapper)),
                "/api/clients");
        return servletContextHandler;
    }
}
