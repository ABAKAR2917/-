import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Главное окно приложения
public class HRMainFrame extends JFrame {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final EmployeeTableModel tableModel;
    private JTable employeeTable;
    private JTextField tfName;
    private JTextField tfPosition;
    private JComboBox<Department> cbDepartment;
    private JTextField tfSalary;
    private JTextField tfExperience;
    private JTextField tfSearchName;
    private JComboBox<Department> cbSearchDepartment;

    public HRMainFrame() {
        this.departmentService = new DepartmentService();
        EmployeeRepository repository = new EmployeeRepository("employees.dat");
        AnalyticsService analyticsService = new AnalyticsService();
        this.employeeService = new EmployeeService(repository, analyticsService);
        tableModel = new EmployeeTableModel(employeeService.getAllEmployees());
        initUI();
    }

    // Инициализация компонентов интерфейса
    private void initUI() {
        setTitle("Система управления персоналом");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Панель поиска (верх)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Поиск по ФИО:"));
        tfSearchName = new JTextField(20);
        searchPanel.add(tfSearchName);
        searchPanel.add(new JLabel("Отдел:"));
        cbSearchDepartment = new JComboBox<>();
        cbSearchDepartment.addItem(null); // пункт "все отделы"
        for (Department d : departmentService.getAll()) {
            cbSearchDepartment.addItem(d);
        }
        searchPanel.add(cbSearchDepartment);
        JButton btnSearch = new JButton("Найти");
        btnSearch.addActionListener(this::onSearch);
        searchPanel.add(btnSearch);
        JButton btnClearFilter = new JButton("Сброс");
        btnClearFilter.addActionListener(e -> refreshTable());
        searchPanel.add(btnClearFilter);
        add(searchPanel, BorderLayout.NORTH);

        // Таблица сотрудников (центр)
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getSelectionModel().addListSelectionListener(e -> onTableSelection());
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Нижняя панель: форма + кнопки
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Форма ввода данных сотрудника
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Сотрудник"));
        formPanel.add(new JLabel("ФИО:"));
        tfName = new JTextField();
        formPanel.add(tfName);
        formPanel.add(new JLabel("Должность:"));
        tfPosition = new JTextField();
        formPanel.add(tfPosition);
        formPanel.add(new JLabel("Отдел:"));
        cbDepartment = new JComboBox<>();
        for (Department d : departmentService.getAll()) {
            cbDepartment.addItem(d);
        }
        formPanel.add(cbDepartment);
        formPanel.add(new JLabel("Оклад:"));
        tfSalary = new JTextField();
        formPanel.add(tfSalary);
        formPanel.add(new JLabel("Стаж (лет):"));
        tfExperience = new JTextField();
        formPanel.add(tfExperience);
        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Кнопки управления
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Добавить");
        btnAdd.addActionListener(this::onAdd);
        buttonsPanel.add(btnAdd);
        JButton btnUpdate = new JButton("Изменить");
        btnUpdate.addActionListener(this::onUpdate);
        buttonsPanel.add(btnUpdate);
        JButton btnDelete = new JButton("Удалить");
        btnDelete.addActionListener(this::onDelete);
        buttonsPanel.add(btnDelete);
        JButton btnSave = new JButton("Сохранить в файл");
        btnSave.addActionListener(e -> {
            employeeService.save();
            JOptionPane.showMessageDialog(this, "Данные сохранены");
        });
        buttonsPanel.add(btnSave);
        JButton btnLoad = new JButton("Загрузить из файла");
        btnLoad.addActionListener(e -> {
            employeeService.load();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Данные загружены");
        });
        buttonsPanel.add(btnLoad);
        JButton btnForecast = new JButton("Прогноз оклада");
        btnForecast.addActionListener(this::onForecast);
        buttonsPanel.add(btnForecast);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Обновление таблицы всеми данными
    private void refreshTable() {
        tableModel.setEmployees(employeeService.getAllEmployees());
        tfSearchName.setText("");
        cbSearchDepartment.setSelectedIndex(0);
    }

    // Обработка поиска
    private void onSearch(ActionEvent event) {
        String namePart = tfSearchName.getText().trim();
        Department dept = (Department) cbSearchDepartment.getSelectedItem();
        String deptName = (dept != null) ? dept.getName() : null;
        List<Employee> found = employeeService.search(namePart, deptName);
        tableModel.setEmployees(found);
    }

    // Добавление нового сотрудника
    private void onAdd(ActionEvent event) {
        try {
            String name = tfName.getText().trim();
            String position = tfPosition.getText().trim();
            Department department = (Department) cbDepartment.getSelectedItem();
            double salary = Double.parseDouble(tfSalary.getText().trim());
            int experience = Integer.parseInt(tfExperience.getText().trim());
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите ФИО");
                return;
            }
            employeeService.createEmployee(name, position, department, salary, experience);
            refreshTable();
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Проверьте числовые значения оклада и стажа");
        }
    }

    // Изменение выбранного сотрудника
    private void onUpdate(ActionEvent event) {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите сотрудника в таблице");
            return;
        }
        Employee employee = tableModel.getEmployeeAt(row);
        if (employee == null) {
            return;
        }
        try {
            String name = tfName.getText().trim();
            String position = tfPosition.getText().trim();
            Department department = (Department) cbDepartment.getSelectedItem();
            double salary = Double.parseDouble(tfSalary.getText().trim());
            int experience = Integer.parseInt(tfExperience.getText().trim());
            employeeService.updateEmployee(employee, name, position, department, salary, experience);
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Проверьте числовые значения оклада и стажа");
        }
    }

    // Удаление выбранного сотрудника
    private void onDelete(ActionEvent event) {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите сотрудника в таблице");
            return;
        }
        Employee employee = tableModel.getEmployeeAt(row);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Удалить сотрудника " + employee.getFullName() + "?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            employeeService.deleteEmployee(employee.getId());
            refreshTable();
            clearForm();
        }
    }

    // Прогноз оклада по стажу
    private void onForecast(ActionEvent event) {
        String input = JOptionPane.showInputDialog(
                this,
                "Введите стаж (лет) для прогноза оклада:"
        );
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        try {
            double experience = Double.parseDouble(input.trim());
            AnalyticsService.LinearModel model = employeeService.trainSalaryModel();
            double salary = model.predict(experience);
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Прогноз оклада при стаже %.1f лет: %.2f", experience, salary)
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите корректное число");
        }
    }

    // Заполнение формы при выборе строки в таблице
    private void onTableSelection() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        Employee employee = tableModel.getEmployeeAt(row);
        if (employee == null) {
            return;
        }
        tfName.setText(employee.getFullName());
        tfPosition.setText(employee.getPosition());
        tfSalary.setText(String.valueOf(employee.getSalary()));
        tfExperience.setText(String.valueOf(employee.getExperienceYears()));
        Department empDept = employee.getDepartment();
        if (empDept != null) {
            for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                Department d = cbDepartment.getItemAt(i);
                if (d.getName().equals(empDept.getName())) {
                    cbDepartment.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // Очистка полей формы
    private void clearForm() {
        tfName.setText("");
        tfPosition.setText("");
        tfSalary.setText("");
        tfExperience.setText("");
        if (cbDepartment.getItemCount() > 0) {
            cbDepartment.setSelectedIndex(0);
        }
    }
}