package spinnytea;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import spinnytea.clone._2048.Test_2048;
import spinnytea.tools.TestID;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = { TestID.class, Test_2048.class })
public class TestSuite
{
}
