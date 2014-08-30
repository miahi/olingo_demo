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
package edw.olingo.pojo;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Describes the vocabulary term entity. An vocabulary term contains the term and an namespace used to uniquely identify this term in InforMEA portal as a source.
 * This namespace may be an URL, ex: http://ozone.unep.org.
 * <br />
 * TODO: Will we accept terms outside Global Vocabulary?
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 0.5
 */
public class VocabularyTerm {

    private static final Logger log = Logger.getLogger(VocabularyTerm.class.getName());
    /**
     * If terms are from Global Vocabulary set namespace to this value
     */
    public static final String UNEP_NAMESPACE = "http://www.unep.org";
    private String term;
    private String namespace;

    public VocabularyTerm(String term, String namespace) {
        this.term = term;
        this.namespace = namespace;
    }

    /**
     * @return Term in English
     */
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Namespaces are used in InforMEA context to differentiate between Global Vocabulary and MEA node local Vocabulary.
     * Normally keywords used to tag the decisions come from the Global Vocabulary, but in case that these cannot be
     * matched we can keep these in another namespace and figure out what to do with them. So namespaces acts more like
     * source of the keyword.
     * @return Namespace in which term is located, example: http://ozone.unep.org etc.
     */
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}
