/* Copyright 2011 UNEP (http://www.unep.org)
 * This file is part of InforMEA Toolkit project.
 * InforMEA Toolkit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * InforMEA Toolkit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * InforMEA Toolkit. If not, see http://www.gnu.org/licenses/.
 */
package edw.olingo.config;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Dynamically configure Hibernate at runtime
 * <br />
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 1.3.3
 */
public class DatabaseConfigurator {
    private static final Logger log = Logger.getLogger(DatabaseConfigurator.class.getName());

    private static DatabaseConfigurator _instance;

    // The entity manager configured with this class
    private EntityManagerFactory factory = null;


    private DatabaseConfigurator() {}


    /**
     * @return Singleton instance for this class
     */
    public static DatabaseConfigurator getInstance() {
        if(_instance == null) {
            _instance = new DatabaseConfigurator();
            if(!_instance.isConfigured()){
                _instance.configure();
            }
        }
        return _instance;
    }



    public void configure() {

        Configuration cfg = Configuration.getInstance();
//
//        //Preferences.userRoot().getBoolean(USE_PATH_PREFIX, false);
//        Configuration hc = new Configuration();
//
//        // Use C3P0 connection pooling instead of Tomcat DBCP - which cannot be configured at runtime dynamically
//        hc.setProperty("connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
//        hc.setProperty("hibernate.c3p0.acquire_increment", "1");
//        hc.setProperty("hibernate.c3p0.idle_test_period", "100");
//        hc.setProperty("hibernate.c3p0.max_size", "30");
//        hc.setProperty("hibernate.c3p0.max_statements", "0");
//        hc.setProperty("hibernate.c3p0.min_size", "1");
//        hc.setProperty("hibernate.c3p0.timeout", "100");
//        hc.setProperty("hibernate.c3p0.testConnectionOnCheckout", "true"); // Very important to validate connections - for ITPGRFA fails if false since it's remote DB.
//
//        // Database connection configuration
//        DatabaseConfiguration dbCfg = cfg.getDatabaseConfiguration();
//        hc.setProperty("hibernate.connection.driver_class", dbCfg.getJDBCDriver());
//        hc.setProperty("hibernate.connection.url", dbCfg.getJDBCUrl());
//        hc.setProperty("hibernate.connection.username", dbCfg.getUser());
//        hc.setProperty("hibernate.connection.password", dbCfg.getPassword());
//        hc.setProperty("hibernate.connection.zeroDateTimeBehavior", "convertToNull");
//
//        hc.setProperty("hibernate.dialect", dbCfg.getHibernateDialect());
//
//        // Cache configuration - Second level
//        // log.info("Secondary cache is disabled - Dynamic enabling not implemented.");
//        // hc.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
//        /*
//        hc.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.SingletonEhCacheProvider");
//        hc.setProperty("hibernate.cache.use_query_cache", "true");
//        hc.setProperty("hibernate.cache.use_second_level_cache", "true");
//        hc.setProperty("hibernate.cache.provider_configuration", "/ehcache.xml");
//         */
//
//        hc.setProperty("hibernate.current_session_context_class", "thread");
//        hc.setProperty("hibernate.show_sql", "true");
//        hc.setProperty("zeroDateTimeBehavior", "convertToNull");
//
//        if(cfg.isUseContacts()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.ContactTreaty.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.Contact.class);
//        }
//
//        if(cfg.isUseCountryProfiles()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.CountryProfile.class);
//        }
//
//        if(cfg.isUseCountryReports()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.CountryReport.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.CountryReportTitle.class);
//        }
//
//        if(cfg.isUseDecisions()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.DBDecisionDocument.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.DecisionKeyword.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.DecisionContent.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.DecisionSummary.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.DecisionLongTitle.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.DecisionTitle.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.Decision.class);
//        }
//
//        if(cfg.isUseMeetings()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.MeetingDescription.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.MeetingTitle.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.Meeting.class);
//        }
//
//        if(cfg.isUseNationalPlans()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.NationalPlanTitle.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.NationalPlan.class);
//        }
//
//        if(cfg.isUseSites()) {
//            hc.addAnnotatedClass(org.informea.odata.data.db.Site.class);
//            hc.addAnnotatedClass(org.informea.odata.data.db.SiteName.class);
//        }
//        sessionFactory = hc.buildSessionFactory();


        EntityManagerFactory managerFactory = null;
        Map<String, String> persistenceMap = new HashMap<String, String>();

        DatabaseConfiguration dbCfg = cfg.getDatabaseConfiguration();

//        persistenceMap.put("javax.persistence.jdbc.url", dbCfg.getJDBCUrl());
//        persistenceMap.put("javax.persistence.jdbc.user", dbCfg.getUser());
//        persistenceMap.put("javax.persistence.jdbc.password", dbCfg.getPassword());
//        persistenceMap.put("javax.persistence.jdbc.driver", dbCfg.getJDBCDriver());
        persistenceMap.put("javax.persistence.jdbc.url", "jdbc:mysql://192.168.37.140/informea?zeroDateTimeBehavior=convertToNull");
        persistenceMap.put("javax.persistence.jdbc.user", "miahi");
        persistenceMap.put("javax.persistence.jdbc.password", "miahi");
        persistenceMap.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

        persistenceMap.put("eclipselink.ddl-generation.output-mode", "database");
        persistenceMap.put("eclipselink.logging.level", "ALL");     // todo: remove in PROD
        persistenceMap.put("eclipselink.orm.throw.exceptions", "true");

        factory = Persistence.createEntityManagerFactory(Configuration.PUNIT_NAME, persistenceMap);


    }


    public boolean isConfigured() {
        return factory != null;
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }
}
