package grph;

import java.util.Random;

import it.unimi.dsi.fastutil.ints.IntSet;

public interface GrphIntSet extends IntSet
{
	int getGreatest();
	int pickRandomElement(Random r);
}
