package edw.olingo.service;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.*;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: miahi
 * Date: 8/25/14
 * Time: 9:48 PM
 * @deprecated
 */
public class JPAExtensionImpl implements JPAEdmExtension {
    @Override
    public void extendWithOperation(JPAEdmSchemaView jpaEdmSchemaView) {
    }

    @Override
    public void extendJPAEdmSchema(JPAEdmSchemaView jpaEdmSchemaView) {
//        //To change body of implemented methods use File | Settings | File Templates.
//
//        jpaEdmSchemaView.getEdmSchema().getEntityTypes().add(getLocalizableString());
        System.out.println("HERE1");
//        for(EntityType et : jpaEdmSchemaView.getEdmSchema().getEntityTypes() ){
//            System.out.println(et.getName());
//            if(et.getName().equals("MeetingTitle")){
//                et.setBaseType(new FullQualifiedName("persistence_unit","LocalizableString"));
//                et.setProperties(new ArrayList<Property>());
//                et.setKey(null);
//                et.setNavigationProperties(new ArrayList<NavigationProperty>());
//            }
//        }

    }

    private EntityType getLocalizableString(){
    // todo add key
        EntityType entityType = new EntityType();

        List<Property> properties = new ArrayList<Property>();
        SimpleProperty property = new SimpleProperty();

        property.setName("language");
        property.setType(EdmSimpleTypeKind.String);
        properties.add(property);

        property = new SimpleProperty();
        property.setName("value");
        property.setType(EdmSimpleTypeKind.String);
        properties.add(property);

        entityType.setName("LocalizableString");
        entityType.setProperties(properties);



        return entityType;
    }

    @Override
    public InputStream getJPAEdmMappingModelStream() {
        return null;
    }
}
