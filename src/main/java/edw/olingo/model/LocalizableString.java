package edw.olingo.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Describe the attributes for an localizable string. An localizable string contains two fields:
 * language which the value is written in and the value.
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 0.5
 * @deprecated Not used as it cannot be mapped by olingo
 */
//@Entity
public class LocalizableString  {

    private static final Logger log = Logger.getLogger(LocalizableString.class.getName());
    @Column(name = "language", nullable = false)
    private String language;

    @Id
    @Column(name = "value")
    private String value;

    public LocalizableString() {
    }

    public LocalizableString(String language, String value) {
        this.language = language;
        this.value = value;
    }

    /**
     * @return Language of contained value
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return Value of this string
     */
    public String getTitle() {
        return value;
    }

    public void setTitle(String value) {
        this.value = value;
    }

}
