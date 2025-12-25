import java.util.ArrayList;
import java.util.List;

// Сервис для работы со справочником отделов
public class DepartmentService {
    private final List<Department> departments = new ArrayList<>();

    public DepartmentService() {

        // Начальная инициализация
        departments.add(new Department(1, "Отдел кадров"));
        departments.add(new Department(2, "ИТ-отдел"));
        departments.add(new Department(3, "Бухгалтерия"));
    }

    public List<Department> getAll() {
        return new ArrayList<>(departments);
    }

    public Department findByName(String name) {
        for (Department d : departments) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }
}