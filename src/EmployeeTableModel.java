import javax.swing.table.AbstractTableModel;
import java.util.List;

// Модель таблицы для отображения сотрудников
public class EmployeeTableModel extends AbstractTableModel {
    private final String[] columns = {
            "ID", "ФИО", "Должность", "Отдел", "Оклад", "Стаж, лет"
    };

    private List<Employee> employees;

    public EmployeeTableModel(List<Employee> employees) {
        this.employees = employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < employees.size()) {
            return employees.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return employees == null ? 0 : employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee e = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return e.getId();
            case 1:
                return e.getFullName();
            case 2:
                return e.getPosition();
            case 3:
                return e.getDepartment() != null ? e.getDepartment().getName() : "";
            case 4:
                return e.getSalary();
            case 5:
                return e.getExperienceYears();
            default:
                return "";
        }
    }
}