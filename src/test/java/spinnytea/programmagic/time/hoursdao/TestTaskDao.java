package spinnytea.programmagic.time.hoursdao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

public class TestTaskDao
{

	@Test
	public void csv_to_hibernate()
	{
		TaskDao_CSV csv = new TaskDao_CSV();
		TaskDao_Hibernate hib = new TaskDao_Hibernate();

		// verify that all of the csv days will be queried
		assertEquals(TaskDao_CSV.DEFAULT_RESOURCE_FOLDER.listFiles().length, csv.allDays().size());
		// ensure that we have something to test against
		assertNotEquals(0, csv.allDays().size());

		// copy all of the data from the csv files to hibernate
		for(Day day : csv.allDays())
			hib.saveDay(day, csv.loadDay(day));

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
		start.set(2000, Calendar.JANUARY, 1, 8, 0);
		Calendar end = GregorianCalendar.getInstance();
		end.setTimeInMillis(0);
		end.set(2000, Calendar.JANUARY, 1, 9, 0);

		Day day = new Day(start.getTime());

		// create the specific task for testing
		List<Task> tasks = Arrays.asList(new Task(start.getTime(), "TEST_Stuff"), new Task(end.getTime(), null));

		// delete the records for the given day so we start with a clean slate
		dao.deleteDay(day);
		assertEquals(Collections.emptyList(), dao.loadDay(day));

		// save the elements for the day
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));

		// if we do it again, we should still have the same records
		dao.saveDay(day, tasks);
		assertEquals(tasks, dao.loadDay(day));

		// clean up after the test
		dao.deleteDay(day);
		assertEquals(Collections.emptyList(), dao.loadDay(day));
	}
}
