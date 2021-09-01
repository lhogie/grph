/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoni (I3S, Université Cote D'Azur, Saclay) 

*/
 
 
/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 
Aurelien Lancin (Coati research team, Inria)
Christian Glacet (LaBRi, Bordeaux)
David Coudert (Coati research team, Inria)
Fabien Crequis (Coati research team, Inria)
Grégory Morel (Coati research team, Inria)
Issam Tahiri (Coati research team, Inria)
Julien Fighiera (Aoste research team, Inria)
Laurent Viennot (Gang research-team, Inria)
Michel Syska (I3S, Université Cote D'Azur)
Nathann Cohen (LRI, Saclay) 
Julien Deantoin (I3S, Université Cote D'Azur, Saclay) 

*/
 
 package grph.path;


public class SingletonPath extends AbstractPath
{
//    private static final int[] emptyArray = new int[0];
    private final int vertex;

    public SingletonPath(int v)
    {
	this.vertex = v;
    }

    @Override
    public String toString()
    {
	return String.valueOf(vertex);
    }

    @Override
    public int getSource()
    {
	return vertex;
    }

    @Override
    public void setSource(int v)
    {
	throw new IllegalStateException("path can't be changed");
    }

    @Override
    public int getVertexAt(int i)
    {
	if (i != 0)
	    throw new IllegalStateException("out of bounds");

	return vertex;
    }

    @Override
    public AbstractPath clone()
    {
	return new SingletonPath(vertex);
    }

    @Override
    public int getDestination()
    {
	return vertex;
    }

    @Override
    public int[] toVertexArray()
    {
	return new int[] { vertex };
    }

    @Override
    public boolean containsVertex(int someVertex)
    {
	return vertex == someVertex;
    }



    @Override
    public int indexOfVertex(int v)
    {
	return v == this.vertex ? 0 : -1;
    }

    @Override
    public void extend(int e, int v)
    {
	throw new PathNotModifiableException();
    }


    @Override
    public void reverse()
    {
    }

    @Override
    public int getEdgeHeadingToVertexAt(int i)
    {
	throw new IllegalStateException("no edge in a singleton path");
    }

    @Override
    public int getNumberOfVertices()
    {
	return 1;
    }
}
