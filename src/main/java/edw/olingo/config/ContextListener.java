package edw.olingo.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: miahi
 * Date: 9/1/14
 * Time: 9:33 PM
 */
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DatabaseConfigurator.getInstance().configure();
        // todo maybe others
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
