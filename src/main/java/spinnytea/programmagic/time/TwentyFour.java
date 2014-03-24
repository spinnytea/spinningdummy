package spinnytea.programmagic.time;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

/**
 * This class name is unconventional. I know. It doesn't expalin what is going on. But it contains something that I feel is exceptional and unique.
 * <p/>
 * But seriosuly. It's just a clock. It's a 24-hour clock with day on the top and night on the bottom. That's about it.
 */
public class TwentyFour
extends JPanel
implements ActionListener
{
	private static final long serialVersionUID = -7007713141372025267L;
	private static final boolean drawSeconds = false;

	// used for drawing
	private final Timer timer;
	private int size;
	private double size2;
	private final Color clockFaceColor = Color.black;
	private final Color clockSecondTone = Color.lightGray;

	// used by the action listener to determine if it is worth the time to repaint
	private Calendar cal = Calendar.getInstance();
	private int prevDrawSecond = 0;

	// used for sunrise/sunset
	private SunriseSunsetCalculator ssc;

	// create the hands here
	// the clock isn't redrawn often enough for this to really matter
	final Shape hourHand;
	final Shape minuteHand;
	final Shape secondHand;

	public TwentyFour(Location location, int size)
	{
		if(location != null)
			ssc = new SunriseSunsetCalculator(location, TimeZone.getDefault());
		else
			ssc = null;
		this.size = size;
		setPreferredSize(new Dimension(size, size));
		size2 = size / 2.0;

		double hw = 0.25;
		hourHand = new Polygon( // created inline
		new int[] { (int) (size2 * -2.5 / 12), (int) (size2 * -2.5 / 12), (int) (size2 * 6.25 / 12), (int) (size2 * 6.25 / 12) }, // xs
		new int[] { (int) (size2 * -hw / 12), (int) (size2 * hw / 12), (int) (size2 * hw / 12), (int) (size2 * -hw / 12) }, // ys
		4);
		minuteHand = new Polygon( // created inline
		new int[] { (int) (size2 * -2.5 / 12), (int) (size2 * -2.5 / 12), (int) (size2 * 9 / 12), (int) (size2 * 9 / 12) }, // xs
		new int[] { (int) (size2 * -hw / 12), (int) (size2 * hw / 12), (int) (size2 * hw / 12), (int) (size2 * -hw / 12) }, // ys
		4);
		if(drawSeconds)
			secondHand = new Ellipse2D.Double(size2 * 9.7 / 12, size2 * -0.1 / 12, size2 * 0.2 / 12, size2 * 0.2 / 12);
		else
			secondHand = null;

		// if we are showing the seconds, update basically 60 times a second
		// if the highest granularity is minutes, then only update 20 times a second
		timer = new Timer(drawSeconds ? 16 : 50, this);
		timer.start();
	}

	@Override
	public void validate()
	{
		super.validate();
		Dimension d = getSize();
		size = Math.min(d.width, d.height);
		size2 = size / 2.0;
		repaint();
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
		if(prevDrawSecond != cal.get(Calendar.SECOND) || drawSeconds)
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

		// draw the background of the clock face
		g.setColor(Color.white);
		g.fillOval(1, 1, size - 3, size - 3);

		g.setColor(clockFaceColor);

		// draw the thick ring around the edge of the clock
		int clockBorderWidth = size / 40;
		g.setStroke(new BasicStroke(clockBorderWidth * 2f));
		g.drawOval(clockBorderWidth, clockBorderWidth, size - 2 * clockBorderWidth - 1, size - 2 * clockBorderWidth - 1);

		g.setStroke(new BasicStroke(1f));
		g.setFont(g.getFont().deriveFont((float) (size2 / 16.0)));

		// draw the minutes
		for(int i = 0; i < 60; i++)
		{
			double angle = Math.PI * 2.0 * i / 60.0 - Math.PI / 2;

			if(i % 5 == 0)
			{
				// centers of the text
				int x = (int) (Math.cos(angle) * 9.75 / 12.0 * size2 + size2);
				int y = (int) (Math.sin(angle) * 9.75 / 12.0 * size2 + size2);

				String text = (i < 10 ? "0" : "") + i;
				FontMetrics fm = g.getFontMetrics();
				g.drawString(text, x - (int) (fm.getStringBounds(text, g).getWidth() / 2.0), y + fm.getAscent() / 2);
			}
			else
			{
				int x1 = (int) (Math.cos(angle) * 10.0 / 12.0 * size2 + size2);
				int y1 = (int) (Math.sin(angle) * 10.0 / 12.0 * size2 + size2);

				int x2 = (int) (Math.cos(angle) * 9.5 / 12.0 * size2 + size2);
				int y2 = (int) (Math.sin(angle) * 9.5 / 12.0 * size2 + size2);

				g.drawLine(x1, y1, x2, y2);
			}
		}

		g.setStroke(new BasicStroke(3f));
		// draw the hours
		for(int i = 0; i < 24; i++)
		{
			double angle = Math.PI * 2.0 * i / 24.0 + Math.PI;

			// move the center and rotate
			g.translate(size2, size2);
			g.rotate(angle);
			g.setFont(g.getFont().deriveFont((float) (size2 / 10.0)));

			// centers of the text
			int y = (int) (-7.25 / 12.0 * size2);
			int y2 = (int) (-7 / 12.0 * size2);
			int y3 = (int) (-6.75 / 12.0 * size2);

			String text = (i < 10 ? "0" : "") + i;
			if(i == 0)
			{
				text = "\u263D";
				g.setFont(g.getFont().deriveFont((float) (size2 / 5.0)));
			}
			if(i == 12)
			{
				text = "\u263C";
				g.setFont(g.getFont().deriveFont((float) (size2 / 5.0)));
			}

			g.setColor(clockFaceColor);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(text, 0 - (int) (fm.getStringBounds(text, g).getWidth() / 2.0), y);

			g.setColor(clockSecondTone);
			g.drawLine(0, y2, 0, y3);

			// unrotate, and move back
			g.rotate(-angle);
			g.translate(-size2, -size2);
		}
		// fill in the sunset hours
		{
			int top = (int) ((12.0 - 6.75) / 12.0 * size2);
			int width = (int) (6.75 * 2.0 / 12.0 * size2);

			if(ssc != null)
			{
				Calendar sunrise = ssc.getOfficialSunriseCalendarForDate(cal);
				Calendar sunset = ssc.getOfficialSunsetCalendarForDate(cal);

				// trial and error adjustment
				double sunsetAngle = 270 - 360.0 * (sunset.get(Calendar.HOUR_OF_DAY) + sunset.get(Calendar.MINUTE) / 60.0) / 24;
				double sunriseAngle = 270 - 360.0 * (sunrise.get(Calendar.HOUR_OF_DAY) + sunrise.get(Calendar.MINUTE) / 60.0) / 24;

				// trial and error adjustment
				g.fillArc(top, top, width, width, (int) sunriseAngle, 360 + (int) (sunsetAngle - sunriseAngle));
			}
			else
			{
				// if there is no location, then fill 6am to 6pm
				g.fillArc(top, top, width, width, 180, 180);
			}
		}

		// filling in Polygons does not require a stroke
		// therefore, we don't need to ensure that it is any particular value for this part

		g.setColor(clockFaceColor);
		// hour hand affected my hours and minutes
		drawHand(g, hourHand, Math.PI * 2.0 * (cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0) / 24 + Math.PI / 2);
		// minute hand affected by minutes and seconds and milliseconds (although, there probably aren't enough pixels for the milliseconds to matter)
		drawHand(g, minuteHand, Math.PI * 2.0 * (cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) / 60.0 + cal.get(Calendar.MILLISECOND) / 60000.0) / 60.0 - Math.PI / 2);
		if(drawSeconds)
			// second hand affected by seconds and milliseconds (although, there probably aren't enough pixels for the milliseconds to matter)
			drawHand(g, secondHand, Math.PI * 2.0 * (cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND) / 1000.0) / 60.0 - Math.PI / 2);

		g.fillOval((int) (size2 - size2 / 18), (int) (size2 - size2 / 18), (int) (size2 / 9), (int) (size2 / 9));
	}

	/** translate, draw, un-translate */
	private void drawHand(Graphics2D g, Shape hand, double angle)
	{
		g.translate(size2, size2);
		g.rotate(angle);
		g.fill(hand);
		g.rotate(-angle);
		g.translate(-size2, -size2);
	}

	public static void main(String[] args)
	{
		// location is centered at the Inner Harbor in Baltimore, MD
		final TwentyFour tf = new TwentyFour(new Location(39.285743, -76.610297), 512);
		JFrame frame = new JFrame()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate()
			{
				super.validate();
				tf.validate();
			}
		};
		frame.setContentPane(tf);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
