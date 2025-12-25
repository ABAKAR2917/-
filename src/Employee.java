// Класс сотрудника
public class Employee extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String position;
    private Department department;
    private double salary;
    private int experienceYears;

    public Employee(int id,
                    String fullName,
                    String position,
                    Department department,
                    double salary,
                    int experienceYears) {
        super(id);
        this.fullName = fullName;
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.experienceYears = experienceYears;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
}