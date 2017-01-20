package net.heteroclinic.jgts.model;
import java.util.ArrayList;
import java.util.List;
/**
 * Copyright 2016 Zhikai Wang <www.heteroclinic.net>. All rights reserved.
 * Science and Technology Promotion License 
 * All third-party licenses are automatically cascaded.
 * Non-commercial usage of this file is not limited.
 * Commercial usage is allowed, given this file is not modified. 
 * Inheritance/overriding/re-factoring is suggested at higher level.
 * This is a good-will software. Users' liability always. 
 */
/**
 * JGTS not necessarily a clone of GTS.
 * <p>
 * {@code Vertex} class is an composition of {@code Point}. 
 * <p>
 * {@code index} is the sequence number in the model's vertices segment.
 * {@code attrib} is reserved.
 * {@code connectedVertices} are all the vertices at immediate reach.
 * {@code connectedEdges} are all the edges with this vertex being one end.
 * {@code connectedTriangles} are all the triangles having this vertex.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class Vertex {
	public Point p = new Point(0.0f, 0.0f, 0.0f);
	public int index;
	public int attrib;

	public List<Integer> connectedVertices = new ArrayList<Integer>();

	public List<Integer> connectedEdges = new ArrayList<Integer>();

	public List<Integer> connectedTriangles = new ArrayList<Integer>();

	public void print() {
		System.out.println(
				"[p.x,p.y,p.z,index,attrib]" + "[" + p.x + "," + p.y + "," + p.z + "," + index + "," + attrib + "]");
		System.out.println("connectedVertices:");
		for (Integer i : connectedVertices) {
			System.out.println(i);
		}
		System.out.println("connectedEdges:");
		for (Integer i : connectedEdges) {
			System.out.println(i);
		}
		System.out.println("connectedTriangles:");
		for (Integer i : connectedTriangles) {
			System.out.println(i);
		}
	}

//	public Vertex() {
//		index = -1;
//		attrib = -1;
//	};

	/**
	 * Constructor of Vertex
	 * @param np the point this vertex is composed of
	 * @param ci index of the vertex in the mesh file
	 * @param attr reserved value
	 */
	public Vertex(Point np, int ci, int attr) {
		attrib = attr;
		index = ci;
		p.x = np.x;
		p.y = np.y;
		p.z = np.z;
	};


	/**
	 * Add an edge the vertex is on.
	 * @param pi index in the mesh file edges segment
	 * @return position in the changed list
	 * @throws Exception
	 * 		We could end here for example a broken mesh file.
	 */
	public int addAnEdgeToList(int pi) throws Exception {
		if (index == -1) {
			throw new RuntimeException("The vertex is not initialized.");
		}
		connectedEdges.add(pi);
		return connectedEdges.size();
	};

	/**
	 * Add a GTSEdge
	 * @param ep a GTSEdge instance
	 * @return list size changed or index for the new one
	 * @throws Exception
	 * 		We could end here for example a broken mesh file.
	 */
	public int addAnEdge(GTSEdge ep) throws Exception {
		if (ep.gp.beg != index && ep.gp.fin != index) {
			throw new Exception("The vertex is not on this edge");
		}

		// addAnEdgeToList
		addAnEdgeToList(ep.index);

		if (ep.gp.beg == index)
			return addAnVertexToList(ep.gp.fin);
		else
			return addAnVertexToList(ep.gp.beg);
	};

	/**
	 * @param pi point index
	 * @return new size of the vertices connected to
	 */
	public int addAnVertexToList(int pi) {
		connectedVertices.add(pi);
		return connectedVertices.size();
	};

	/**
	 * Add a GTSTriangle.
	 * @param pgp a triangle this vertex is on
	 * @return new size or index
	 */
	public int addATriangle(GTSTriangle pgp) {
		return addATriangleToList(pgp.index);
	};

	/**
	 * Add a triangle by index.
	 * @param pi point index to add
	 * @return new size of triangles this vertex belongs to
	 */
	public int addATriangleToList(int pi) {
		connectedTriangles.add(pi);
		return connectedTriangles.size();
	};

	// TODO printf is not tested
	public void printDetails() {

		System.out.printf("\nVertex No. %d ", index);
		System.out.printf("\nConnected vertices:");
		for (Integer i : connectedVertices) {
			System.out.printf("%d,", i);
		}
		System.out.printf("\nConnected edges:");
		for (Integer i : connectedEdges) {
			System.out.printf("%d,", i);
		}
		System.out.printf("\nConnected triangles:");
		for (Integer i : connectedTriangles) {
			System.out.printf("%d,", i);
		}

	};

}
