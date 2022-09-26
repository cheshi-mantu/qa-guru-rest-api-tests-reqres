package tests;


import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    static final String CREATED_USER = "tobias.funke@reqres.in";

    @BeforeAll
    public static void setUp(){

    System.out.println("Dummy Before All method");
    System.out.println("Created " + CREATED_USER);

    }
}