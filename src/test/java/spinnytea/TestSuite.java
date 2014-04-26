package spinnytea;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import spinnytea.clone._2048.Test_2048;
import spinnytea.maze.TestAlgorithms;
import spinnytea.maze.callforhelp.TestDisjointSets;
import spinnytea.puzzle.square.TestBoard;
import spinnytea.puzzle.square.TestTransform;
import spinnytea.time.hoursdao.TestTaskDao;
import spinnytea.tools.TestEncryption;
import spinnytea.tools.TestID;
import spinnytea.tools.TestRandomAccessCollection;
import spinnytea.tools.TestServer;

@SuppressWarnings("EmptyClass")
@RunWith(Suite.class)
@Suite.SuiteClasses(value = { TestID.class, TestEncryption.class, TestRandomAccessCollection.class, TestServer.class, //
TestTaskDao.class, //
TestAlgorithms.class, TestDisjointSets.class, //
Test_2048.class, TestBoard.class, TestTransform.class })
public class TestSuite
{
}
