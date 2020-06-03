package com.ab.unittesting.basic;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.*;

import static com.ab.unittesting.basic.CoffeeType.*;
import static org.junit.Assert.*;

/**
 * @author Arpit Bhardwaj
 */
public class CafeTest {

    public static final int NO_MILK = 0;
    public static final int ESPRESSO_BEANS = Espresso.getRequiredBeans();
    public static final int NO_BEANS = 0;
    private Cafe cafe;
    //Constructor will be called before each test method
    /*public CafeTest() {
        System.out.println("Constructor");
    }*/

    //will be called before each test method
    @Before
    public void setUp() throws Exception {
        cafe = new Cafe();
    }

    //will be called after each test method
    /*@After
    public void tearDown() throws Exception {
        System.out.println("After");
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.out.println("BeforeClass");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        System.out.println("AfterClass");
    }*/

    @Test
    public void canBrewEspresso(){
        //Given Clause (Pre Condition)
        WithBeans();

        //When Clause (Behaviour)
        Coffee coffee = cafe.brew(Espresso);

        //Then Clause (Post Condition)
        MatcherAssert.assertThat(coffee, Matchers.hasProperty("bean",Matchers.equalTo(ESPRESSO_BEANS)));
        Assert.assertEquals("Wrong Coffee Type", Espresso, coffee.getType());
        Assert.assertEquals("Wrong amount of milk", NO_MILK, coffee.getMilk());
        Assert.assertEquals("Wrong number of beans", ESPRESSO_BEANS, coffee.getBean());
    }

    @Test
    public void canBrewLatte(){
        //Given Clause (Pre Condition)
        WithBeans();
        cafe.reStockMilk(Latte.getRequiredMilk());
        //When Clause (Behaviour)
        Coffee coffee = cafe.brew(Latte);

        //Then Clause (Post Condition)
        Assert.assertEquals("Wrong Coffee Type", Latte, coffee.getType());
        Assert.assertEquals("Wrong amount of milk",227, coffee.getMilk());
        Assert.assertEquals("Wrong number of beans",7, coffee.getBean());
    }

    @Test
    public void brewingEspressoConsumesBeans(){
        //Given Clause (Pre Condition)
        WithBeans();

        //When Clause (Behaviour)
        Coffee coffee = cafe.brew(Espresso);

        //Then Clause (Post Condition)
        Assert.assertEquals(NO_MILK, cafe.getBeanInStock());
        //leads to failure of test
        //Assert.assertEquals(1, cafe.getBeanInStock());
    }

    //Then Clause (Post Condition)
    //@Test //leads to error or exception in code under test
    @Test(expected = IllegalStateException.class)
    public void lattesRequiresMilk(){
        //Given Clause (Pre Condition)
        WithBeans();

        //When Clause (Behaviour)
        Coffee coffee = cafe.brew(Latte);
    }
    @Test
    public void mustRestockMilk(){
        //Given Clause (Pre Condition)
        //Cafe cafe = new Cafe();

        //When Clause (Behaviour)
        cafe.reStockMilk(NO_MILK);
    }

    @Test
    public void mustRestockBeans(){
        //Given Clause (Pre Condition)
        //Cafe cafe = new Cafe();

        //When Clause (Behaviour)
        cafe.reStockBeans(NO_BEANS);
    }

    /*private Cafe cafeWithBeans() {
        Cafe cafe = new Cafe();
        cafe.reStockBeans(ESPRESSO_BEANS);
        return cafe;
    }*/

    private void WithBeans() {
        cafe.reStockBeans(ESPRESSO_BEANS);
    }
}