/*
 * (C) Copyright 2009-2013 CNRS.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:

    Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
    Aurelien Lancin (Coati research team, Inria)
    Christian Glacet (LaBRi, Bordeaux)
    David Coudert (Coati research team, Inria)
    Fabien Crequis (Coati research team, Inria)
    Grégory Morel (Coati research team, Inria)
    Issam Tahiri (Coati research team, Inria)
    Julien Fighiera (Aoste research team, Inria)
    Laurent Viennot (Gang research-team, Inria)
    Michel Syska (I3S, University of Nice-Sophia Antipolis)
    Nathann Cohen (LRI, Saclay) 
 */
 
 package grph.algo.sort;

import grph.Grph;
import toools.set.IntSet;

/**
 * 
 * @author lhogie
 *
 *
 */

public abstract class Sorter
{
    public int[] sort(Grph g, IntSet s)
    {
	int[] array = s.toIntArray();
	sortInPlace(g, array);
	return array;
    }

    public void sortInPlace(Grph g, int[] values)
    {
	// Check for empty or null array
	if (values == null || values.length == 0)
	{
	    return;
	}

	sortInPlace(g, values, 0, values.length - 1);
    }

    public void sortInPlace(Grph g, int[] numbers, int low, int high)
    {
	int i = low, j = high;

	// Get the pivot element from the middle of the list
	int pivot = getValue(g, numbers[low + (high - low) / 2]);

	// Divide into two lists
	while (i <= j)
	{
	    // If the current value from the left list is smaller then the pivot
	    // element then get the next element from the left list
	    while (getValue(g, numbers[i]) < pivot)
	    {
		++i;
	    }
	    // If the current value from the right list is larger then the pivot
	    // element then get the next element from the right list
	    while (getValue(g, numbers[j]) > pivot)
	    {
		--j;
	    }

	    // If we have found a values in the left list which is larger then
	    // the pivot element and if we have found a value in the right list
	    // which is smaller then the pivot element then we exchange the
	    // values.
	    // As we are done we can increase i and j
	    if (i <= j)
	    {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
		++i;
		--j;
	    }
	}
	// Recursion
	if (low < j)
	    sortInPlace(g, numbers, low, j);
	if (i < high)
	    sortInPlace(g, numbers, i, high);
    }

    protected abstract int getValue(Grph g, int i);
}
