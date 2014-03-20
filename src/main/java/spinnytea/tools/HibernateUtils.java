package spinnytea.tools;

import java.util.Collection;

import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtils
{
	private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

	public static Session openSession()
	throws HibernateException
	{
		return openSession(null);
	}

	/** @param pathtoconfig if null, it will use the <code>hibernate.cfg.xml</code> it finds non the classpath */
	public static Session openSession(String pathtoconfig)
	throws HibernateException
	{
		AnnotationConfiguration config = new AnnotationConfiguration() //
			.addAnnotatedClass(spinnytea.programmagic.time.hoursdao.Day.class) //
			.addAnnotatedClass(spinnytea.programmagic.time.hoursdao.Task.class);

		if(pathtoconfig != null)
			config.configure(pathtoconfig);
		else
			config.configure();

		SessionFactory sessionFactory = config.buildSessionFactory();
		return sessionFactory.openSession();
	}

	/** perform generic operations on {@link Entity} objects. */
	public static <T> void crud(Session session, HibernateUtils.SessionAction sessionAction, T t)
	throws HibernateException
	{
		if(t == null)
			return;

		Transaction tx = null;
		try
		{
			// start a transaction
			tx = session.beginTransaction();

			// perform the selected action on the item
			switch(sessionAction)
			{
			case UPDATE:
				session.update(t);
				break;
			case SAVE:
				session.save(t);
				break;
			case DELETE:
				logger.info("Deleting obj: {}", t);
				session.delete(t);
				break;
			}

			// commit the transaction
			tx.commit();
		}
		catch(HibernateException e)
		{
			// if there is a problem, then rollback the transaction
			if(tx != null)
				tx.rollback();
			throw e;
		}
	}

	/** perform generic operations on a collection of {@link Entity} objects. */
	public static <T> void crud(Session session, HibernateUtils.SessionAction sessionAction, Collection<T> list)
	throws HibernateException
	{
		// if there isn't anything to do, then just return
		if(list == null || list.isEmpty())
			return;

		Transaction tx = null;
		try
		{
			// start a single transaction
			tx = session.beginTransaction();

			// perform the selected action on all of the items
			switch(sessionAction)
			{
			case UPDATE:
				for(T t : list)
					session.update(t);
				break;
			case SAVE:
				for(T t : list)
					session.save(t);
				break;
			case DELETE:
				logger.info("Deleting {} objs", list.size());
				for(T t : list)
					session.delete(t);
				break;
			}

			// commit the transaction
			tx.commit();
		}
		catch(HibernateException e)
		{
			// if there is a problem, then rollback the transaction
			if(tx != null)
				tx.rollback();
			throw e;
		}
	}

	public static enum SessionAction
	{
		SAVE,
		UPDATE,
		DELETE;
	}
}