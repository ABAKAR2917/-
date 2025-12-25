import java.util.List;

// Сервис с простым математическим анализом (линейная регрессия)
public class AnalyticsService {

    // Обучение модели оклад = a + b * стаж
    public LinearModel trainSalaryModel(List<Employee> employees) {
        if (employees.size() < 2) {

            // Недостаточно данных для регрессии
            return new LinearModel(0, 0);
        }
        double sumX = 0;
        double sumY = 0;
        for (Employee e : employees) {
            sumX += e.getExperienceYears();
            sumY += e.getSalary();
        }
        int n = employees.size();
        double meanX = sumX / n;
        double meanY = sumY / n;
        double sxy = 0;
        double sxx = 0;
        for (Employee e : employees) {
            double dx = e.getExperienceYears() - meanX;
            double dy = e.getSalary() - meanY;
            sxy += dx * dy;
            sxx += dx * dx;
        }
        if (sxx == 0) {

            // У всех работников стаж одинаковый, значит используем средний оклад
            return new LinearModel(meanY, 0);
        }
        double b = sxy / sxx; // наклон
        double a = meanY - b * meanX; // свободный член
        return new LinearModel(a, b);
    }

    // Простая модель линейной регрессии
    public static class LinearModel {
        private final double intercept; // a
        private final double slope; // b

        public LinearModel(double intercept, double slope) {
            this.intercept = intercept;
            this.slope = slope;
        }

        public double predict(double x) {
            return intercept + slope * x;
        }

        public double getIntercept() {
            return intercept;
        }

        public double getSlope() {
            return slope;
        }
    }
}