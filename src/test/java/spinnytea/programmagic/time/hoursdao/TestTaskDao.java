package spinnytea.programmagic.time.hoursdao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Cleanup;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTaskDao
{
	private static final Logger logger = LoggerFactory.getLogger(TestTaskDao.class);
	private static final Day dummyTestDay = new Day(2000, 2);

	@BeforeClass
	public static void before()
	{
		File folder = new File("exports/hours");
		if(!folder.exists())
		{
			logger.info("Hours folder doesn't exists. It will be deleted upon exit.");
			assertTrue(folder.mkdir());
			folder.deleteOnExit();
		}

		File db = new File("exports/hours.h2.db");
		if(!db.exists())
		{
			logger.info("Hours database doesn't exists. It will be deleted upon exit.");
			db.deleteOnExit();
		}
	}

	@Test
	public void csv_to_hibernate()
	{
		TaskDao_CSV csv = new TaskDao_CSV();
		// ensure that we have something to test against
		if(csv.allDays().size() == 0)
			try
			{
				logger.info("Creating dummy test day");
				@Cleanup
				// FIXME generate this from dummyTestDay
				PrintStream ps = new PrintStream(new File("exports/hours/2000_2"));
				ps.print("Sat Jan 02 09:00:00 EST 2000\nSat Jan 02 08:00:00 EST 2000\nTEST_Stuff\n");
			}
			catch(Exception e)
			{
				assertTrue(false);
			}
		assertNotEquals(0, csv.allDays().size());

		TaskDao_Hibernate hib = new TaskDao_Hibernate();

		// verify that all of the csv days will be queried
		assertNotNull(TaskDao_CSV.DEFAULT_RESOURCE_FOLDER.listFiles());
		//noinspection ConstantConditions
		assertEquals(TaskDao_CSV.DEFAULT_RESOURCE_FOLDER.listFiles().length, csv.allDays().size());

		// copy all of the data from the csv files to hibernate
		for(Day day : csv.allDays())
		{
			hib.saveDay(day, csv.loadDay(day));
			assertTrue(csv.dayExists(day));
			assertTrue(hib.dayExists(day));
		}

		// verify that all the days from csv have been copied over
		assertTrue(hib.allDays().containsAll(csv.allDays()));

		// ensure that the data loads properly
		for(Day day : csv.allDays())
		{
			List<Task> csv_day = csv.loadDay(day);
			List<Task> hib_day = hib.loadDay(day);
			assertEquals(csv_day.size(), hib_day.size());
			Collections.sort(csv_day);
			Collections.sort(hib_day);
			assertEquals(csv_day, hib_day);
		}

		if(csv.dayExists(dummyTestDay))
		{
			logger.info("Deleting dummy test day");
			csv.deleteDay(dummyTestDay);
			hib.deleteDay(dummyTestDay);
		}

		assertFalse(csv.dayExists(dummyTestDay));
		assertFalse(hib.dayExists(dummyTestDay));
	}

	@Test
	public void crud()
	{
		crud(new TaskDao_Hibernate());
		crud(new TaskDao_CSV());
	}

	private void crud(TaskDao dao)
	{
		// setup a specific date for testing
		Calendar start = GregorianCalendar.getInstance();
		start.setTimeInMillis(0);
		start.set(dummyTestDay.getYear(), Calendar.JANUARY, 1, 8, 0);
		Calendar end = GregorianCalendar.getInstance();
		end.setTimeInMillis(0);
		end.set(dummyTestDay.getYear(), Calendar.JANUARY, 1, 9, 0);

		Day day = new Day(start.getTime());

		// create the specific task for testing
		List<Task> tasks = Arrays.asList(new Task(start.getTime(), "TEST_Stuff"), new Task(end.getTime(), null));

		// delete the records for the given day so we start with a clean slate
		dao.deleteDay(day);
		assertEquals(Collections.EMPTY_LIST, dao.loadDay(day));
		assertFalse(dao.dayExists(day));

		// save the elements for the day
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));
		assertTrue(dao.dayExists(day));

		// if we do it again, we should still have the same records
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));
		assertTrue(dao.dayExists(day));

		// clean up after the test
		dao.deleteDay(day);
		assertEquals(Collections.EMPTY_LIST, dao.loadDay(day));
		assertFalse(dao.dayExists(day));
	}
}
