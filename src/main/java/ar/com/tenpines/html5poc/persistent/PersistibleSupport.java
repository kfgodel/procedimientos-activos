package ar.com.tenpines.html5poc.persistent;

import ar.com.tenpines.orm.api.crud.Identifiable;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Superclase para las entidades persistentes que implementa su ID con un Long.<br>
 * Se redefine el equals y el hashcode para utilizar el ID como referencia para la igualdad
 *
 * @author D. García
 */
@MappedSuperclass
public class PersistibleSupport implements Identifiable {
    private static final Logger LOG = LoggerFactory.getLogger(PersistibleSupport.class);

    public static final String id_FIELD = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Este campo permite detectar modificaciones concurrentes sobre las entidades en forma
     * optimista. Si falla el lockeo, la última transacción en cerrar tendrá una
     * {@link HibernateOptimisticLockingFailureException}, indicando que otra transacción le
     * modificó los datos
     */
    @Version
    private Long persistenceVersion;

    public Long getPersistenceVersion() {
        return persistenceVersion;
    }

    public void setPersistenceVersion(final Long persistenceVersion) {
        this.persistenceVersion = persistenceVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Compara esta entidad con otra considerandola por ID.<br>
     * Serán consideradas iguales sólo si son la misma instancia o si siendo del mismo tipo tienen
     * el mismo ID.<br>
     * Si alguna de las entidades no tiene ID, entonces serán consideradas entidades distintas. (al
     * persistirlas cada una tendrá su ID distinto)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            // Cortamos por el caso más fácil y común
            return true;
        }
        if (obj == null) {
            // Nunca somos iguales a un null
            return false;
        }
        final Long thisId = this.getId();
        if (thisId == null) {
            return false;
        }
        if (!this.getClass().isAssignableFrom(obj.getClass()) && !obj.getClass().isAssignableFrom(this.getClass())){
            // Si no es del mismo tipo asumimos que son tablas distintas y por lo tanto objetos
            // distintos, el id podría coincidir pero son distintas entidades
            LOG.debug("Considerando como distintos a [{}] y [{}] por ser de clases distintas", this, obj);
            return false;
        }
        final PersistibleSupport other = (PersistibleSupport) obj;
        // Finalmente son considerados iguales si tienen el mismo ID y son del mismo tipo
        return thisId.equals(other.getId());
    }

    /**
     * Implementación del hashcode que usa el ID como hashcode de referencia.<br>
     * Esta implementación usa el hashcode tradicional si esta entidad no tiene ID, con lo que hay
     * que ser cuidadosos al agregar entidades sin persistir en colecciones que usen el hashcode.
     * Por ejemplo un HashSet, que puede considerar como distintos a la msima entidad antes y
     * después de persistida
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return this.getId().hashCode();
        } else {
            LOG.debug("Usando Object.hashCode() para un persistible por que no tiene ID");
            return super.hashCode();
        }
    }

}

