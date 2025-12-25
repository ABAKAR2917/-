import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Репозиторий сотрудников с хранением в файле (сериализация)
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private int nextId = 1;
    private final File storageFile;

    public EmployeeRepository(String fileName) {
        this.storageFile = new File(fileName);
        load();
    }

    // Генерация нового идентификатора
    public int getNextId() {
        return nextId++;
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public Employee findById(int id) {
        for (Employee e : employees) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public void add(Employee employee) {
        employees.add(employee);
    }

    public void update(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee);
                return;
            }
        }
    }

    public void delete(int id) {
        employees.removeIf(e -> e.getId() == id);
    }

    // Сохранение списка сотрудников в файл
    public void save() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(storageFile))) {
            out.writeObject(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Загрузка списка сотрудников из файла
    @SuppressWarnings("unchecked")
    public void load() {
        if (!storageFile.exists()) {
            return;
        }
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(storageFile))) {
            List<Employee> loaded = (List<Employee>) in.readObject();
            employees.clear();
            employees.addAll(loaded);
            int maxId = 0;
            for (Employee e : employees) {
                if (e.getId() > maxId) {
                    maxId = e.getId();
                }
            }
            nextId = maxId + 1;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}