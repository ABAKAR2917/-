// Класс отдела
public class Department extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;

    public Department(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Используется для отображения в ComboBox
    @Override
    public String toString() {
        return name;
    }
}