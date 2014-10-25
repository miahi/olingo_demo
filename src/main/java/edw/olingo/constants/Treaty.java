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
package edw.olingo.constants;

import edw.olingo.InvalidValueException;

import java.util.ArrayList;
import java.util.List;

/**
 * List of treaties implemented. InforMEA portal will use one of these predefined constants
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 0.5
 */
public enum Treaty {
    AARHUS("aarhus"),
    ABIDJAN("abidjan"),
    AEWA("aewa"),
    ANTIGUA("antigua"),
    APIA("apia"),
    ASCOBANS("ascobans"),
    BAMAKO("bamako"),
    BARCELONA("barcelona"),
    BARCELONA_SPA("barc-spa"),
    BASEL("basel"),
    CARTAGENA("cartagena"),
    CARTAGENA_CONV("cartagena-conv"),
    CBD("cbd"),
    CITES("cites"),
    CMS("cms"),
    DUMPING_PROTOCOL("dumping"),
    EUROBATS("eurobats"),
    ESPOO("espoo"),
    HAZARDOUS("hazardous"),
    JEDDAH("jeddah"),
    KUWAIT("kuwait"),
    KYIVSEA("kyivsea"),
    KYOTO("kyoto"),
    LAND_BASED("land-based"),
    LRTAP("lrtap"),
    MONTREAL("montreal"),
    NAGOYA("nagoya"),
    NAIROBI("nairobi"),
    NOUMEA("noumea"),
    OFFSHORE("offshore"),
    PLANT_TREATY("plant-treaty"),
    PREVENTION_EMERGENCY("preventionemergency"),
    PROTOCOL_WATER_HEALTH("protocolwaterhealth"),
    RAMSAR("ramsar"),
    ROTTERDAM("rotterdam"),
    STOCKHOLM("stockholm"),
    UNCCD("unccd"),
    UNEP("unep"),
    UNFCCC("unfccc"),
    VIENNA("vienna"),
    WATER_CONVENTION("waterconvention"),
    WHC("whc");

    private String name;

    private Treaty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    //TODO Remove this and override valueof
    public static Treaty getTreaty(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch(Exception ex) {
            if("plant-treaty".equalsIgnoreCase(value)) {
                return PLANT_TREATY;
            } else if("cartagena-conv".equalsIgnoreCase(value)) {
                return CARTAGENA_CONV;
            } else if("barc-spa".equalsIgnoreCase(value)) {
                return BARCELONA_SPA;
            } else if("land-based".equalsIgnoreCase(value)) {
                return LAND_BASED;
            }
            throw new InvalidValueException(
                    String.format(
                            "Unknown treaty value. Invalid value '%s' for  <treaty> property. Treaties are enumerated values and %s cannot is not among them", value, value),
                    ex);
        }
    }

}
