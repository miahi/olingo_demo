package edw.olingo.service;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
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
 */
public class JPAExtensionImpl implements JPAEdmExtension {
    @Override
    public void extendWithOperation(JPAEdmSchemaView jpaEdmSchemaView) {
    }

    @Override
    public void extendJPAEdmSchema(JPAEdmSchemaView jpaEdmSchemaView) {
		for (EntityType e : jpaEdmSchemaView.getEdmSchema().getEntityTypes()) {
			switch (e.getName()) {
			case "MeetingTitle":
			case "DecisionTitle":
			case "NationalPlanTitle":
			case "MeetingDescription":
			case "DecisionLongTitle":
			case "CountryReportTitle":
				FullQualifiedName baseType = new FullQualifiedName("informea", "LocalizableString");
				e.setBaseType(baseType);
			}
		}

        // Workaround for Olingo multiplicity bug.
        // The JPA OneToMany relationship is translated as 1 to * for some entities and as 1 to 1 for others.
        // This makes olingo throw "Requested entity could not be found" errors when accessing the 1:1 entities.
        // Workaround: if 1:1 multiplicity is found, it forces * on the child
        // Child is determined by the class prefix (the parent entity name must be a prefix of the child element entity)
        for(Association a : jpaEdmSchemaView.getEdmSchema().getAssociations()) {
            if(a.getEnd1().getMultiplicity() == EdmMultiplicity.ONE && a.getEnd2().getMultiplicity() == EdmMultiplicity.ONE ) {
                System.out.println("Fixed multiplicity for: " + a.getName() + " " + a.getEnd1().getRole() + "(" + a.getEnd1().getMultiplicity() +")  " +
                        a.getEnd2().getRole() + "(" + a.getEnd2().getMultiplicity() + ")");

                if(a.getEnd1().getRole().startsWith(a.getEnd2().getRole())){
                    a.getEnd1().setMultiplicity(EdmMultiplicity.MANY);
                } else if(a.getEnd2().getRole().startsWith(a.getEnd1().getRole())){
                    a.getEnd2().setMultiplicity(EdmMultiplicity.MANY);
                }
            }
        }

    }

    @Override
    public InputStream getJPAEdmMappingModelStream() {
        return null;
    }
}
