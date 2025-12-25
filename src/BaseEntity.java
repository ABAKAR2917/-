import java.io.Serializable;

// Базовый класс для сущностей с идентификатором
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}