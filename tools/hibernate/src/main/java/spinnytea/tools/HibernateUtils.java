package spinnytea.tools;

import java.util.Collection;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HibernateUtils
{
	private static SessionFactory sessionFactory = null;

	public static Session openSession()
	throws HibernateException
	{
		if(sessionFactory == null)
		{
			Configuration config = new Configuration().configure();
			ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(registry);
		}
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
				log.info("Deleting obj: {}", t);
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
				log.info("Deleting {} objs", list.size());
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
		DELETE
	}
}
