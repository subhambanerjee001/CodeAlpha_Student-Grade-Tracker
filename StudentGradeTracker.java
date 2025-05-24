import java.util.*;
class Student{
    String name;
    ArrayList<Integer> grades;

    Student(String name){
        this.name = name;
        this.grades = new ArrayList<>();
    }
     void addGrade(int grade){
        grades.add(grade);
    }

    double getAverage() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (int grade : grades) sum += grade;
        return (double) sum / grades.size();
    }

     int getLowest() {
        int min = Integer.MAX_VALUE;
        for (int grade : grades) min = Math.min(min, grade);
        return min;
    }
    
     int getHighest() {
        int max = Integer.MIN_VALUE;
        for (int grade : grades) max = Math.max(max, grade);
        return max;
    }

     void printSummary() {
        System.out.println("Student: " + name);
        System.out.println("Grades: " + grades);
        System.out.printf("Average: %.2f, Highest: %d, Lowest: %d\n",
                          getAverage(), getHighest(), getLowest());
    }

}
   

public class StudentGradeTracker{
    public static void main(String[] args) {
              Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        System.out.println("Enter The  number of students You want to print the data:");
        int n = sc.nextInt();
        sc.nextLine(); 

        for (int i = 0; i < n; i++) {
            System.out.print("Enter the name of the student: ");
            String name = sc.nextLine();
            Student student = new Student(name);

            System.out.print("Enter the no.of grades you want to enter : ");
            int g = sc.nextInt();

            for (int j = 0; j < g; j++) {
                System.out.print("Enter grade " + (j + 1) + ": ");
                int grade = sc.nextInt();
                student.addGrade(grade);
            }
            sc.nextLine(); 
            students.add(student);
        }

        System.out.println("\n... Student Summary Report ...");
        for (Student student : students) {
            student.printSummary();
            System.out.println("................................");
        }
    }
}