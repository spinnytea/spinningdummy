package spinnytea.programmagic.time;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Clock
extends JPanel
implements ActionListener
{
	private static final long serialVersionUID = -7007713141372025267L;

	// used for drawing
	private final Timer timer;
	private final ClockOptions options;
	private final double size2;
	private final Polygon hourHand;
	private final Polygon minuteHand;
	private final Polygon secondHand;

	// used by the action listener to determine if it is worth the time to repaint
	private Calendar cal = Calendar.getInstance();
	private int prevDrawSecond = 0;

	public Clock(ClockOptions options)
	{
		setPreferredSize(new Dimension(options.size, options.size));
		this.options = options;
		size2 = options.size / 2.0;
		timer = new Timer(50, this);
		timer.start();

		hourHand = new Polygon( // created inline
		new int[] { (int) (size2 * -1 / 12), 0, (int) (size2 * 7 / 12), 0 }, // xs
		new int[] { 0, (int) (size2 * 0.75 / 12), 0, (int) (size2 * -0.75 / 12) }, // ys
		4);
		minuteHand = new Polygon( // created inline
		new int[] { (int) (size2 * -1 / 12), 0, (int) (size2 * 11 / 12), 0 }, // xs
		new int[] { 0, (int) (size2 * 0.5 / 12), 0, (int) (size2 * -0.5 / 12) }, // ys
		4);

		// the second hand is square-ish
		int x1 = (int) (size2 * -3 / 12);
		int x2 = (int) (size2 * -1 / 12);
		int x3 = (int) (size2 * 11 / 12);
		int y1 = (int) Math.max(size2 * 0.2 / 12, 2.0);
		int y2 = (int) Math.max(size2 * 0.1 / 12, 1.0);
		int s2h = (int) (size2 * 0.3 / 12);
		secondHand = new Polygon( // created inline
		new int[] { x1, x2, x2, -s2h, 0, s2h, x3, x3, s2h, 0, -s2h, x2, x2, x1 }, // xs
		new int[] { y1, y1, y2, y2, s2h, y2, y2, -y2, -y2, -s2h, -y2, -y2, -y1, -y1 }, // ys
		14);

	}

	public void pause(boolean pause)
	{
		if(pause)
			timer.stop();
		else
			timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		cal = Calendar.getInstance();
		if(prevDrawSecond != cal.get(Calendar.SECOND))
			repaint();
	}

	@Override
	protected void paintComponent(Graphics _g)
	{
		prevDrawSecond = cal.get(Calendar.SECOND);

		super.paintComponent(_g);
		Graphics2D g = (Graphics2D) _g;

		// make it pretty
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(options.showClock_Face && options.colorClock_Face != null)
		{
			// draw the background of the clock face
			g.setColor(options.colorClock_Face);
			g.fillOval(1, 1, options.size - 3, options.size - 3);
		}

		if((options.fillFrom != null && options.colorFillFrom != null) || (options.fillUntil != null && options.colorFillUntil != null))
		{
			// this fill is in degrees starting from the right, and going counter-clockwise

			// times are in degrees, and in a very odd orientation
			int from = (int) (90 - (360 * (options.fillFrom.get(Calendar.HOUR_OF_DAY) + options.fillFrom.get(Calendar.MINUTE) / 60.0) / options.hoursOnFace));
			int now = (int) (90 - (360 * (cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0) / options.hoursOnFace));
			int until = (int) (90 - (360 * (options.fillUntil.get(Calendar.HOUR_OF_DAY) + options.fillUntil.get(Calendar.MINUTE) / 60.0) / options.hoursOnFace));

			if(options.fillFrom != null && options.colorFillFrom != null)
			{
				g.setColor(options.colorFillFrom);
				g.fillArc(1, 1, options.size - 3, options.size - 3, from, now - from);
			}

			if(options.fillUntil != null && options.colorFillUntil != null)
			{
				g.setColor(options.colorFillUntil);
				g.fillArc(1, 1, options.size - 3, options.size - 3, now, until - now);
			}
		}

		if(options.colorClock_Lines != null)
		{
			g.setColor(options.colorClock_Lines);

			// draw the thick ring around the edge of the clock
			g.setStroke(new BasicStroke(4f));
			g.drawOval(2, 2, options.size - 5, options.size - 5);

			g.setStroke(new BasicStroke(1f));

			// only need to loop if there are any ticks to draw
			if(options.showTicks_Hour || options.showTicks_Minute)
			{
				// draw the tick marks
				for(int i = 1; i <= 60; i++)
				{
					// if length doesn't get changed, then nothing will be drawn for this tick
					double length = -1.0;

					// set the length depending on which tick and the options
					if(options.showTicks_Hour && (i % 5 == 0))
					{
						// make 3,6,9,12 larger than the others
						length = (options.showTicks_HourLarger && (i % 15 == 0)) ? 9 : 10;
					}
					else if(options.showTicks_Minute)
					{
						// this will be called for hours if showTicks_Hour is false
						// wouldn't want gaps where the hours are supposed to be
						length = 10.5;
					}

					// if we have a length, then draw the tick mark
					if(length > 0.0)
					{
						double angle = Math.PI * 2.0 * i / 60.0 - Math.PI / 2;

						int x1 = (int) (Math.cos(angle) * 11.0 / 12.0 * size2 + size2);
						int y1 = (int) (Math.sin(angle) * 11.0 / 12.0 * size2 + size2);

						int x2 = (int) (Math.cos(angle) * length / 12.0 * size2 + size2);
						int y2 = (int) (Math.sin(angle) * length / 12.0 * size2 + size2);

						g.drawLine(x1, y1, x2, y2);
					}
				}
			}
		}

		// filling in Polygons does not require a stroke
		// therefore, we don't need to ensure that it is any particular value for this part

		if(options.showHand_Hour && options.colorHand_Hour != null)
		{
			// hour hand affected my hours and minutes
			drawHand(g, hourHand, options.colorHand_Hour, Math.PI * 2.0 * (cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0)
			/ options.hoursOnFace - Math.PI / 2);
		}
		if(options.showHand_MinuteAnalog && options.colorHand_MinuteAnalog != null)
		{
			// minute hand affected by minutes and seconds
			drawHand(g, minuteHand, options.colorHand_MinuteAnalog, Math.PI * 2.0 * (cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) / 60.0) / 60.0
			- Math.PI / 2);
		}
		if(options.showHand_MinuteDigit && options.colorHand_MinuteDigit != null)
		{
			// minute hand affected by minutes
			drawHand(g, minuteHand, options.colorHand_MinuteDigit, Math.PI * 2.0 * cal.get(Calendar.MINUTE) / 60.0 - Math.PI / 2);
		}

		if(options.showHand_Second && options.colorHand_Second != null)
		{
			// second hand affected by seconds
			// it it were to be affected by milliseconds, it would need to update quite a bit more
			// fact is, I might consider removing this, so it only needs to update for the minutes
			drawHand(g, secondHand, options.colorHand_Second, Math.PI * 2.0 * cal.get(Calendar.SECOND) / 60.0 - Math.PI / 2);
		}
	}

	/** translate, draw, un-translate */
	private void drawHand(Graphics2D g, Polygon hand, Color c, double angle)
	{
		g.setColor(c);
		g.translate(size2, size2);
		g.rotate(angle);
		g.fillPolygon(hand);
		g.rotate(-angle);
		g.translate(-size2, -size2);
	}

	/** Configuration data for the clock */
	public static class ClockOptions
	{
		/** color of the clock-face */
		public Color colorClock_Face = Color.white;

		/** color of outline and tick marks */
		public Color colorClock_Lines = Color.black;

		/** color the "fill From" background */
		public Color colorFillFrom = new Color(48, 32, 255, 80);

		/** color the "fill Until" background */
		public Color colorFillUntil = new Color(255, 32, 48, 80);

		/** when visible, what color is the hour hand */
		public Color colorHand_Hour = Color.darkGray;

		/** when visible, what color is the minute hand that ticks every minute */
		public Color colorHand_MinuteDigit = Color.black;

		/** when visible, what color is the minute hand that keeps up with the seconds */
		public Color colorHand_MinuteAnalog = Color.lightGray;

		/** when visible, what color is the seconds hand */
		public Color colorHand_Second = Color.red;

		/** fill a section of the clock, from the specified time, until now (days are ignored; 24 hr clock) */
		public Calendar fillFrom = null;

		/** fill a section of the clock, from now, until the specified time (days are ignored; 24 hr clock) */
		public Calendar fillUntil = null;

		/** draw a background on the clock face, or just leave it transparent */
		public boolean showClock_Face = true;

		/** show the hour hand */
		public boolean showHand_Hour = true;

		/** show the minute hand that ticks every minute */
		public boolean showHand_MinuteDigit = true;

		/** show the minute hand the keeps up with the seconds */
		public boolean showHand_MinuteAnalog = true;

		/** show the seconds hand */
		public boolean showHand_Second = false;

		/** show tick marks the represent hours */
		public boolean showTicks_Hour = true;

		/** if true, then 3,6,9,12 positions will be larger than 1,2,4,5,7,8,10,11; if false, then all will be the same size */
		public boolean showTicks_HourLarger = true;

		/** show tick marks to denote the minutes */
		public boolean showTicks_Minute = false;

		/** Now, every clock-face I have ever seen has had 12 hours. But the rules of math don't require this to be so */
		public double hoursOnFace = 12.0;

		/** How big should the clock be? */
		private final int size;

		public ClockOptions(int size)
		{
			this.size = size;
		}

		public ClockOptions presetMinimal()
		{
			showClock_Face = true;
			showHand_Hour = true;
			showHand_MinuteDigit = false;
			showHand_MinuteAnalog = true;
			showHand_Second = false;
			showTicks_Hour = false;
			showTicks_HourLarger = true;
			showTicks_Minute = false;
			return this;
		}

		public ClockOptions presetNormal()
		{
			showClock_Face = true;
			showHand_Hour = true;
			showHand_MinuteDigit = true;
			showHand_MinuteAnalog = false;
			showHand_Second = true;
			showTicks_Hour = true;
			showTicks_HourLarger = true;
			showTicks_Minute = false;
			return this;
		}

		public ClockOptions presetFull()
		{
			showClock_Face = true;
			showHand_Hour = true;
			showHand_MinuteDigit = true;
			showHand_MinuteAnalog = true;
			showHand_Second = true;
			showTicks_Hour = true;
			showTicks_HourLarger = true;
			showTicks_Minute = true;
			return this;
		}
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.setContentPane(new Clock(new ClockOptions(200).presetFull()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
