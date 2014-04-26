package spinnytea.time.hoursdao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TaskDao
{
	public static final String FIELD_START = "start";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DAY = "day";

	/** @return a list of all the tasks for a given day; if there are no tasks, then the list will be empty */
	public abstract List<Task> loadDay(Day day);

	/** save the list of tasks for a given day */
	public abstract void saveDay(Day day, List<Task> tasks);

	/** delete all the records for a given day */
	abstract void deleteDay(Day day);

	public abstract List<Day> allDays();

	public abstract boolean dayExists(Day day);

	/**
	 * <ul>
	 * <li>tasks must have at least two items</li>
	 * <li>the last task name must be null</li>
	 * <li>all other task names must not be null</li>
	 * <li>all tasks must be on the given day</li>
	 * </ul>
	 */
	protected boolean validateDay(Day day, List<Task> tasks)
	{
		if(tasks.size() < 2)
			return false;

		// we are going to manipulate this, so it'd be best to create our own local copy
		tasks = new ArrayList<Task>(tasks);
		Collections.sort(tasks);

		// all tasks must be on the given day
		for(Task t : tasks)
		{
			if(!t.getDay().equals(day))
				return false;
		}

		// remove the last one from the list
		// make sure this task has no name
		if(tasks.remove(tasks.size() - 1).getName() != null)
			return false;

		// all other tasks must have a name
		for(Task t : tasks)
		{
			if(t.getName() == null)
				return false;
		}

		// if all the conditions are met, then the data is valid
		return true;
	}
}
