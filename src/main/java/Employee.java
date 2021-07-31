public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    public void setAttribute(String attributeName, String attributeValue){
        switch (attributeName){
            case "id":
                this.id = Long.parseLong(attributeValue, 10);
                break;
            case "firstName":
                this.firstName = attributeValue;
                break;
            case "lastName":
                this.lastName = attributeValue;
                break;
            case "country":
                this.country = attributeValue;
                break;
            case "age":
                this.age = Integer.parseInt(attributeValue);
                break;
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                '}';
    }
}