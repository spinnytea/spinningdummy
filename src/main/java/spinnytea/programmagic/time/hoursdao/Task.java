package spinnytea.programmagic.time.hoursdao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "day")
public class Task
implements Comparable<Task>
{
	@Id
	@NonNull
	private Date start;
	private String name;
	@ManyToOne
	@Setter(value = AccessLevel.NONE)
	private Day day;

	public Task(Date start, String name)
	{
		setStart(start);
		setName(name);
	}

	public void setStart(Date start)
	{
		this.start = start;
		day = new Day(start);
	}

	@Override
	public int compareTo(Task t)
	{
		return start.compareTo(t.start);
	}
}
