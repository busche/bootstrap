package de.ismll.bootstrap;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCmdlineParsing.class, TestConversions.class, TestInstantiations.class, TestOO.class, TestHelpScreen.class })
public class AllTests {

} 