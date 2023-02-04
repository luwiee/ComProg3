package prelim.pexer3;
/**
 * Date: 04/02/2023
 * Name: Lawrence T. Miguel II
 * <p>
 * This class is in compliance to the prelim exercise 3 of  the individual exercises.
 * This is a custom class Citizen that holds the data fields String name, int age.
 */

public class Citizen {
    String name;
    int age;

    public Citizen(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    @Override
    public String toString() {
        return name;
    }
}
