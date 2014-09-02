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
 */
@Entity
public class LocalizableString  {

    private static final Logger log = Logger.getLogger(LocalizableString.class.getName());
    @Column(name = "language", nullable = false)
    private String language;

    @Id
    @Column(name = "id")
    private String id;

    public LocalizableString() {
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
