package bingo.lang.cloning;

public class TransientTest
{
	protected transient String		tr1		= "x";
	protected transient String[]	a		= new String[] { "1", "2" };
	protected String				nontr	= "y";
	protected transient int			i		= 5;							// can't null this
}
