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

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "key")
@Setter(value = AccessLevel.NONE)
public class Day
{
	@Id
	private String key;
	private int year;
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
	public String toString()
	{
		return key;
	}
}
