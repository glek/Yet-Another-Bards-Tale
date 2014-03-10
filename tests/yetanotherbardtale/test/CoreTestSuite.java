package yetanotherbardtale.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
 
@RunWith (Suite.class)
@SuiteClasses (
       {
        CommandWordsTest.class,
        CreatureTest.class,
        GameEntityTest.class,
        RoomTest.class,
        EventFactoryTest.class,
        EnemyTest.class,
        PlayerTest.class
        }
    )

/**
 * This class is used to run all test suites at once.
 * If your IDE supports JUnit, you can run this class as a JUnit
 * test case, and it will run all of the unit tests automatically
 * @author Andrew O'Hara (zalpha314)
 * @version 0.1.1
 */
public class CoreTestSuite {}