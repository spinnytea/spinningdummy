package spinnytea.programmagic.time.hoursdao;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@EqualsAndHashCode(of = { "year", "dayOfYear" })
@Getter
@NoArgsConstructor
@Setter(value = AccessLevel.NONE)
@ToString(exclude = "key")
public class Day
implements Comparable<Day>
{
	// the key is basically a composite of year and dayOfYear
	@Id
	private String key;
	@NaturalId
	private int year;
	@NaturalId
	private int dayOfYear;

	public Day(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		year = c.get(Calendar.YEAR);
		dayOfYear = c.get(Calendar.DAY_OF_YEAR);
		key = year + "_" + dayOfYear;
	}

	public Day(int year, int dayOfYear)
	{
		this.year = year;
		this.dayOfYear = dayOfYear;
		key = year + "_" + dayOfYear;
	}

	@Override
	public int compareTo(Day day)
	{
		int comp = new Integer(year).compareTo(day.year);
		if(comp != 0)
			return year;
		return new Integer(dayOfYear).compareTo(day.dayOfYear);
	}
}
