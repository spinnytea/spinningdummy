package spinnytea.programmagic.time;

import spinnytea.programmagic.time.Clock.ClockOptions;
import spinnytea.programmagic.time.hoursdao.Day;
import spinnytea.programmagic.time.hoursdao.Task;
import spinnytea.programmagic.time.hoursdao.TaskDao;
import spinnytea.programmagic.time.hoursdao.TaskDaoCSV;
import spinnytea.tools.GridLayout2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// XXX add preset buttons for projects
// XXX add a comment field to explain what I did
public class Hours
extends JPanel
{
	private static final long serialVersionUID = -4165420249552888745L;

	private static final SimpleDateFormat format_title = new SimpleDateFormat("EE MMMM dd, yyyy");
	private static final SimpleDateFormat format_input = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat format_day = new SimpleDateFormat("dd EE");

	// GUI handles
	private final JFrame parent;
	private final JPanel datePanel;
	private final Clock clock;
	private final ClockOptions clockOptions;
	private JPanel grid;

	// data
	private Day currentDay;
	private final TaskDao dao;
	private Date endTime;
	private final ArrayList<Task> dates = new ArrayList<Task>();

	public Hours(JFrame parent)
	{
		this.parent = parent;

		clockOptions = new ClockOptions(100);
		clockOptions.colorHand_MinuteAnalog = Color.gray;
		clockOptions.colorHand_Hour = Color.black;
		clockOptions.fillFrom = Calendar.getInstance();
		clockOptions.fillUntil = Calendar.getInstance();
		clockOptions.hoursOnFace = ClockOptions.HoursOnFace.TwentyFour;

		setLayout(new BorderLayout());
		clock = new Clock(clockOptions);
		add(clock, BorderLayout.EAST);

		datePanel = new JPanel(new BorderLayout());
		add(datePanel, BorderLayout.CENTER);

		dao = new TaskDaoCSV();
		setDate(new Day(new Date()));

		ArrayList<Day> days = new ArrayList<Day>(dao.allDays());
		Collections.sort(days, Collections.reverseOrder());
		final JList<Day> dateList = new JList<Day>(days.toArray(new Day[days.size()]));
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setBorder(BorderFactory.createLoweredBevelBorder());
		dateList.setSelectedValue(currentDay, true);
		dateList.setCellRenderer(new ListCellRenderer<Day>()
		{
			DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
			Calendar cal = Calendar.getInstance();

			@Override
			public Component getListCellRendererComponent(JList<? extends Day> list, Day day, int index, boolean isSelected, boolean cellHasFocus)
			{
				cal.set(Calendar.YEAR, day.getYear());
				cal.set(Calendar.DAY_OF_YEAR, day.getDayOfYear());
				return dlcr.getListCellRendererComponent(dateList, format_day.format(cal.getTime()), index, isSelected, cellHasFocus);
			}
		});

		dateList.addListSelectionListener(new ListSelectionListener()
		{
			Day previousLoad = null;

			@Override
			public void valueChanged(ListSelectionEvent evt)
			{
				Day day = dateList.getSelectedValue();
				if(day == null)
					return;

				// if we have already loaded this, then just return
				if(previousLoad == day)
					return;

				// load it
				previousLoad = day;
				setDate(day);
			}
		});
		add(new JScrollPane(dateList), BorderLayout.WEST);

		Button newRoundedTime = new Button("Add New Rounded Time");
		newRoundedTime.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addDate(new Task(roundMinutes(getNewDate()), "TODO"));
				buildDatePane();
			}
		});
		Button newTime = new Button("Add New Time");
		newTime.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addDate(new Task(getNewDate(), "TODO"));
				buildDatePane();
			}
		});
		JPanel times = new JPanel(new GridLayout(0, 2));
		times.add(newRoundedTime);
		times.add(newTime);
		add(times, BorderLayout.SOUTH);
	}

	private void addDate(Task t)
	{
		int idx = 0;
		while(idx < dates.size() && dates.get(idx).getStart().getTime() < t.getStart().getTime())
			idx++;
		dates.add(idx, t);
	}

	public void setDate(Day day)
	{
		currentDay = day;

		if(dao.dayExists(day))
			loadModel();
		else
		{
			// new model
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, day.getYear());
			cal.set(Calendar.DAY_OF_YEAR, day.getDayOfYear());
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			//noinspection MagicNumber
			cal.set(Calendar.HOUR_OF_DAY, 8);
			dates.add(new Task(cal.getTime(), "Start"));
			clockOptions.fillFrom.setTimeInMillis(dates.get(0).getStart().getTime());

			//noinspection MagicNumber
			cal.set(Calendar.HOUR_OF_DAY, 17);
			endTime = cal.getTime();
			clockOptions.fillUntil.setTimeInMillis(endTime.getTime());

			saveModel();
		}

		parent.setTitle(format_title.format(new Date()));
		buildDatePane();
	}

	private Date getNewDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, currentDay.getDayOfYear());
		return cal.getTime();
	}

	private Date parseTime(String text)
	{
		String[] strings = text.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentDay.getYear());
		cal.set(Calendar.DAY_OF_YEAR, currentDay.getDayOfYear());
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strings[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(strings[1]));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/** use the model to update the GUI */
	private void buildDatePane()
	{
		datePanel.removeAll();

		grid = new JPanel(new GridLayout2(0, 2));
		datePanel.add(grid, BorderLayout.NORTH);
		JTextPane summary = new JTextPane();
		summary.setBorder(BorderFactory.createLoweredBevelBorder());
		summary.setContentType("text/html");
		summary.setEditable(false);
		datePanel.add(summary, BorderLayout.SOUTH);

		// this is just to make sure that it repaints the center when it isn't filled
		JPanel filler = new JPanel();
		// since we collapsed the times, the frame is now too small
		// this will help expand it; the sizes are rather arbitrary (it just worked for me)
		//noinspection MagicNumber
		filler.setPreferredSize(new Dimension(130, 0));
		datePanel.add(filler, BorderLayout.CENTER);

		Map<String, Duration> labelDuration = new TreeMap<String, Duration>();

		final ActionListener update = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateModel();
			}
		};

		Task prev = null;
		Collections.sort(dates);
		for(Task t : dates)
		{
			grid.add(new JTextField(t.getName())
			{
				private static final long serialVersionUID = 1L;

				{
					addActionListener(update);
				}
			});
			grid.add(new JTextField(format_input.format(t.getStart()))
			{
				private static final long serialVersionUID = 1L;

				{
					addActionListener(update);
					setHorizontalAlignment(JTextField.RIGHT);
				}
			});

			if(prev != null)
				labelDuration.put(prev.getName(), new Duration(prev.getStart(), t.getStart(), labelDuration.get(prev.getName())));

			prev = t;
		}
		assert prev != null;
		labelDuration.put(prev.getName(), new Duration(prev.getStart(), endTime, labelDuration.get(prev.getName())));
		grid.add(new JLabel("End______"));
		grid.add(new JTextField(format_input.format(endTime))
		{
			private static final long serialVersionUID = 1L;

			{
				addActionListener(update);
				setHorizontalAlignment(JTextField.RIGHT);
			}
		});

		StringBuilder summaryText = new StringBuilder("<html><table border=0 cellpadding=0 width=\"100%\">");
		for(String label : labelDuration.keySet())
			summaryText.append("<tr><td>").append(label).append("</td><td align=\"right\">").append(labelDuration.get(label)).append("</td></tr>");
		summaryText.append("</table></html>");
		summary.setText(summaryText.toString());

		// refresh the display
		parent.invalidate();
//		parent.pack();
		parent.repaint();
	}

	/** use the GUI to update the data within the model */
	private void updateModel()
	{
		// this type cannot be weakened, the order must be maintained because it will be added to dates
		//noinspection TypeMayBeWeakened
		List<Task> tempDates = new ArrayList<Task>();
		// for things that belong in dates
		for(int i = 0; i < grid.getComponentCount() - 2; i += 2)
		{
			JTextField label = (JTextField) grid.getComponent(i);
			JTextField date = (JTextField) grid.getComponent(i + 1);
			// skip if either the date or the label are empty
			if(label.getText().trim().length() == 0 || date.getText().trim().length() == 0)
				continue;

			tempDates.add(new Task(parseTime(date.getText()), label.getText()));
		}
		if(tempDates.size() == 0)
			tempDates.add(new Task(getNewDate(), "Start"));
		endTime = parseTime(((JTextField) grid.getComponent(grid.getComponentCount() - 1)).getText());
		clockOptions.fillUntil.setTime(endTime);
		dates.clear();
		dates.addAll(tempDates);
		clockOptions.fillFrom.setTimeInMillis(dates.get(0).getStart().getTime());
		saveModel();
		buildDatePane();
	}

	/** round the date to the nearest 15 minutes */
	@SuppressWarnings("deprecation")
	public Date roundMinutes(Date d)
	{
		//noinspection MagicNumber
		d.setMinutes((int) (d.getMinutes() / 15.0 + 0.5) * 15);
		return d;
	}

	private void saveModel()
	{
		List<Task> tasks = new ArrayList<Task>(dates);
		tasks.add(new Task(endTime, null));
		dao.saveDay(currentDay, tasks);
	}

	private void loadModel()
	{
		// load the model from a file
		List<Task> tasks = new ArrayList<Task>(dao.loadDay(currentDay));
		Collections.sort(tasks);

		endTime = tasks.remove(tasks.size() - 1).getStart();
		dates.clear();
		dates.addAll(tasks);

		clockOptions.fillUntil.setTimeInMillis(endTime.getTime());
		clockOptions.fillFrom.setTimeInMillis(dates.get(0).getStart().getTime());
	}

	@SuppressWarnings("MagicNumber")
	private static final class Duration
	{
		private int minutes;
		private int hours;

		@SuppressWarnings("deprecation")
		public Duration(Date start, Date end, Duration previous)
		{
			minutes = end.getMinutes() - start.getMinutes();
			hours = end.getHours() - start.getHours();
			if(minutes < 0)
			{
				minutes += 60;
				hours--;
			}
			if(previous != null)
			{
				minutes += previous.minutes;
				hours += previous.hours;
			}
			if(minutes >= 60)
			{
				minutes -= 60;
				hours++;
			}
		}

		@Override
		public String toString()
		{
			return "" + (hours + (minutes / 60.0));
//			return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes;
		}
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		final Hours hours = new Hours(frame);
		frame.setContentPane(hours);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter()
		{
			//
			// This uses Iconify
			// We don't want to use on Focus because we want it to update so long as we can see it
			//
			@Override
			public void windowIconified(WindowEvent e)
			{
				hours.clock.pause();
			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
				hours.clock.unpause();
			}
		});
	}
}
