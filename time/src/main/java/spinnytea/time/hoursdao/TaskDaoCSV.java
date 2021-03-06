package spinnytea.time.hoursdao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskDaoCSV
extends TaskDao
{
	public static final File DEFAULT_RESOURCE_FOLDER = new File("exports/hours");

	private final File resourceFolder;

	public TaskDaoCSV()
	{
		this(DEFAULT_RESOURCE_FOLDER);
	}

	public TaskDaoCSV(File resourceFolder)
	{
		this.resourceFolder = resourceFolder;
		resourceFolder.mkdirs();
	}

	private File getFile(Day day)
	{
		return new File(resourceFolder, day.getYear() + "_" + day.getDayOfYear());
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<Task> loadDay(@NonNull Day day)
	{
		log.debug("load day: " + day);

		List<Task> dates = new ArrayList<Task>();

		if(getFile(day).exists())
		{
			try
			{
				@Cleanup
				Scanner s = new Scanner(getFile(day));

				dates.add(new Task(new Date(s.nextLine()), null));
				while(s.hasNext())
					dates.add(new Task(new Date(s.nextLine()), s.nextLine()));

				// they *should* already be in order
				Collections.sort(dates);
			}
			catch(FileNotFoundException e)
			{
				// this shouldn't happen
			}
			catch(NoSuchElementException e)
			{
				if(dates.isEmpty())
					log.info("day is empty");
				else
				{
					log.error("corrupt file: " + day);
					dates.clear();
				}
			}
			catch(Throwable t)
			{
				log.error("Failed to load the data: " + day, t);
				dates.clear();
			}
		}

		// if the file for this day doesn't exist, then there is nothing to add to the list

		return dates;
	}

	@Override
	public void saveDay(@NonNull Day day, @NonNull List<Task> tasks)
	{
		log.debug("save day: " + day);
		if(!validateDay(day, tasks))
		{
			log.error("tasks are invalid; they cannot be saved");
			return;
		}

		try
		{
			@Cleanup
			PrintStream ps = new PrintStream(getFile(day));

			tasks = new ArrayList<Task>(tasks);
			Collections.sort(tasks);
			ps.println(tasks.remove(tasks.size() - 1).getStart());
			for(Task t : tasks)
			{
				ps.println(t.getStart());
				ps.println(t.getName());
			}
		}
		catch(Throwable t)
		{
			log.error("Failed to save the data: " + day, t);
		}
	}

	@Override
	void deleteDay(@NonNull Day day)
	{
		log.debug("delete day: " + day);

		// delete the file
		getFile(day).delete();
	}

	@Override
	public List<Day> allDays()
	{
		log.debug("all days");

		List<Day> days = new ArrayList<Day>();

		File[] files = resourceFolder.listFiles();
		if(files != null)
			for(File f : files)
			{
				String[] s = f.getName().split("_");
				if(s.length == 2)
					try
					{
						days.add(new Day(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
					}
					catch(Exception e)
					{
						// not a valid filename, just keep going
					}
			}
		return days;
	}

	@Override
	public boolean dayExists(Day day)
	{
		log.debug("day exists: " + day);
		return getFile(day).exists();
	}
}
