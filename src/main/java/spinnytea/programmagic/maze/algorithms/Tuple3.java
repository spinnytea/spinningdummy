package spinnytea.programmagic.maze.algorithms;

/** @deprecated this class should be more specific */
@Deprecated
class Tuple3<S1, S2, S3>
{
	private final S1 _1;
	private final S2 _2;
	private final S3 _3;

	public Tuple3(S1 s1, S2 s2, S3 s3)
	{
		_1 = s1;
		_2 = s2;
		_3 = s3;
	}

	public S1 _1()
	{
		return _1;
	}

	public S2 _2()
	{
		return _2;
	}

	public S3 _3()
	{
		return _3;
	}
}
