package spinnytea;

import spinnytea.clone._2048.Test_2048;
import spinnytea.programmagic.time.hoursdao.TestTaskDao;
import spinnytea.tools.TestEncryption;
import spinnytea.tools.TestID;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = { TestID.class, TestEncryption.class, TestTaskDao.class, Test_2048.class })
public class TestSuite {}
