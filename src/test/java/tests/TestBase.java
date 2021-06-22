package tests;


import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    @BeforeAll
    public static void setUp(){
    System.out.println("Dummy Before All method");
    }
}