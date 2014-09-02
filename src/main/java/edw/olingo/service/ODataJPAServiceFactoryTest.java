package edw.olingo.service;

import edw.olingo.config.Configuration;
import edw.olingo.config.DatabaseConfigurator;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.ref.factory.JPAEntityManagerFactory;

public class ODataJPAServiceFactoryTest extends ODataJPAServiceFactory {


	@Override
	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = getODataJPAContext();

//		oDataJPAContext.setEntityManagerFactory(JPAEntityManagerFactory.getEntityManagerFactory(Configuration.PUNIT_NAME));
		oDataJPAContext.setEntityManagerFactory(DatabaseConfigurator.getInstance().getFactory());
		oDataJPAContext.setPersistenceUnitName(Configuration.PUNIT_NAME);
		oDataJPAContext.setPageSize(Configuration.PAGE_SIZE);
//        oDataJPAContext.setDefaultNaming(true);
		setDetailErrors(true);
        oDataJPAContext.setJPAEdmExtension(new JPAExtensionImpl());

        oDataJPAContext.setJPAEdmMappingModel("jpa_edm_mapping.xml");
		return oDataJPAContext;

	}

    public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
        T callback;

        if (callbackInterface.isAssignableFrom(MyDebugCallback.class)) {
            callback = (T) new MyDebugCallback();
        } else {
            callback = (T) super.getCallback(callbackInterface);
        }

        return callback;
    }

}
