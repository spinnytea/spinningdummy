package spinnytea.programmagic.time.hoursdao;

import spinnytea.tools.HibernateUtils;

import java.util.List;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@Slf4j
public class TaskDaoHibernate
extends TaskDao
{
	@Override
	@SuppressWarnings("unchecked")
	public List<Task> loadDay(@NonNull Day day)
	{
		log.debug("load day: " + day);

		@Cleanup
		Session session = HibernateUtils.openSession();
		// query for the given time range
		return session.createCriteria(Task.class) //
		.add(Restrictions.eq(FIELD_DAY, day)) //
		.addOrder(Order.asc(FIELD_START)) //
		.list();
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

		deleteDay(day);

		try
		{
			@Cleanup
			Session session = HibernateUtils.openSession();
			// now save all the tasks for the given day
			HibernateUtils.crud(session, HibernateUtils.SessionAction.SAVE, day);
			HibernateUtils.crud(session, HibernateUtils.SessionAction.SAVE, tasks);
		}
		catch(HibernateException e)
		{
			log.error("Could not save the tasks", e);
		}
	}

	@Override
	void deleteDay(@NonNull Day day)
	{
		log.debug("delete day: " + day);
		@Cleanup
		Session session = HibernateUtils.openSession();
		// first, remove all the tasks for the given day
		HibernateUtils.crud(session, HibernateUtils.SessionAction.DELETE, loadDay(day));
		HibernateUtils.crud(session, HibernateUtils.SessionAction.DELETE, day);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Day> allDays()
	{
		log.debug("all days");
		@Cleanup
		Session session = HibernateUtils.openSession();
		return session.createCriteria(Day.class).list();
	}

	@Override
	public boolean dayExists(Day day)
	{
		log.debug("day exists: " + day);
		@Cleanup
		Session session = HibernateUtils.openSession();
		return null != session.createCriteria(Day.class) //
		.add(Restrictions.idEq(day.getKey())) //
		.uniqueResult();
	}
}
