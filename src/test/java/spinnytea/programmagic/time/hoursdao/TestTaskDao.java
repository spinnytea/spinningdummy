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
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class TestTaskDao
{
	private static final Day dummyTestDay = new Day(2000, 2);

	@BeforeClass
	public static void before()
	{
		File folder = new File("exports/hours");
		if(!folder.exists())
		{
			log.info("Hours folder doesn't exists. It will be deleted upon exit.");
			assertTrue(folder.mkdir());
			folder.deleteOnExit();
		}

		File db = new File("exports/hours.h2.db");
		if(!db.exists())
		{
			log.info("Hours database doesn't exists. It will be deleted upon exit.");
			db.deleteOnExit();
		}
	}

	@Test
	public void csv_to_hibernate()
	{
		TaskDaoCSV csv = new TaskDaoCSV();
		// ensure that we have something to test against
		if(csv.allDays().size() == 0)
			try
			{
				log.info("Creating dummy test day");
				@Cleanup
				PrintStream ps = new PrintStream(new File("exports/hours/" + dummyTestDay.getKey()));

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(0);
				cal.set(Calendar.YEAR, dummyTestDay.getYear());
				cal.set(Calendar.DAY_OF_YEAR, dummyTestDay.getDayOfYear());
				cal.set(Calendar.HOUR, 9);

				ps.println(cal.getTime());
				cal.set(Calendar.HOUR, 8);
				ps.println(cal.getTime());
				ps.println("TEST_Stuff");
			}
			catch(Exception e)
			{
				assertTrue(false);
			}
		assertNotEquals(0, csv.allDays().size());

		TaskDaoHibernate hib = new TaskDaoHibernate();

		// verify that all of the csv days will be queried
		assertNotNull(TaskDaoCSV.DEFAULT_RESOURCE_FOLDER.listFiles());
		//noinspection ConstantConditions
		assertEquals(TaskDaoCSV.DEFAULT_RESOURCE_FOLDER.listFiles().length, csv.allDays().size());

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
			log.info("Deleting dummy test day");
			csv.deleteDay(dummyTestDay);
			hib.deleteDay(dummyTestDay);
		}

		assertFalse(csv.dayExists(dummyTestDay));
		assertFalse(hib.dayExists(dummyTestDay));
	}

	@Test
	public void crud()
	{
		interfaceExhaustive(new TaskDaoHibernate());
		interfaceExhaustive(new TaskDaoCSV());
	}

	private void interfaceExhaustive(TaskDao dao)
	{
		// setup a specific date for testing
		Calendar start = GregorianCalendar.getInstance();
		start.setTimeInMillis(0);
		start.set(dummyTestDay.getYear(), Calendar.JANUARY, 1, 8, 0);
		Calendar end = GregorianCalendar.getInstance();
		end.setTimeInMillis(0);
		end.set(dummyTestDay.getYear(), Calendar.JANUARY, 1, 9, 0);

		Day day = new Day(start.getTime());

		assertFalse(dao.validateDay(day, Arrays.asList(new Task(start.getTime(), "TEST_Stuff"))));
		assertFalse(dao.validateDay(day, Arrays.asList(new Task(end.getTime(), null))));

		// create the specific task for testing
		List<Task> tasks = Arrays.asList(new Task(start.getTime(), "TEST_Stuff"), new Task(end.getTime(), null));
		assertTrue(dao.validateDay(day, tasks));

		// delete the records for the given day so we start with a clean slate
		dao.deleteDay(day);
		assertEquals(Collections.EMPTY_LIST, dao.loadDay(day));
		assertFalse(dao.dayExists(day));
		assertFalse(dao.allDays().contains(day));

		// save the elements for the day
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));
		assertTrue(dao.dayExists(day));
		assertTrue(dao.allDays().contains(day));

		// if we do it again, we should still have the same records
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));
		assertTrue(dao.dayExists(day));
		assertTrue(dao.allDays().contains(day));

		// clean up after the test
		dao.deleteDay(day);
		assertEquals(Collections.EMPTY_LIST, dao.loadDay(day));
		assertFalse(dao.dayExists(day));
		assertFalse(dao.allDays().contains(day));
	}
}
