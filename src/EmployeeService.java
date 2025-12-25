import java.util.ArrayList;
import java.util.List;

// Бизнес-логика по работе с сотрудниками
public class EmployeeService {
    private final EmployeeRepository repository;
    private final AnalyticsService analyticsService;

    public EmployeeService(EmployeeRepository repository,
                           AnalyticsService analyticsService) {
        this.repository = repository;
        this.analyticsService = analyticsService;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Добавление нового сотрудника
    public Employee createEmployee(String fullName,
                                   String position,
                                   Department department,
                                   double salary,
                                   int experienceYears) {
        int id = repository.getNextId();
        Employee employee = new Employee(id, fullName, position, department, salary, experienceYears);
        repository.add(employee);
        return employee;
    }

    // Обновление данных сотрудника
    public void updateEmployee(Employee employee,
                               String fullName,
                               String position,
                               Department department,
                               double salary,
                               int experienceYears) {
        employee.setFullName(fullName);
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setExperienceYears(experienceYears);
        repository.update(employee);
    }

    public void deleteEmployee(int id) {
        repository.delete(id);
    }

    // Поиск и фильтрация по имени и отделу
    public List<Employee> search(String namePart, String departmentName) {
        List<Employee> result = new ArrayList<>();
        String nameFilter = namePart != null ? namePart.toLowerCase() : "";
        for (Employee e : repository.findAll()) {
            boolean matchesName = nameFilter.isEmpty()
                    || e.getFullName().toLowerCase().contains(nameFilter);
            boolean matchesDept = (departmentName == null || departmentName.isEmpty());
            if (!matchesDept && e.getDepartment() != null) {
                matchesDept = departmentName.equals(e.getDepartment().getName());
            }
            if (matchesName && matchesDept) {
                result.add(e);
            }
        }
        return result;
    }

    // Сохранение данных в файл
    public void save() {
        repository.save();
    }

    // Загрузка данных из файла
    public void load() {
        repository.load();
    }

    // Обучение модели регрессии по текущим данным
    public AnalyticsService.LinearModel trainSalaryModel() {
        return analyticsService.trainSalaryModel(repository.findAll());
    }
}