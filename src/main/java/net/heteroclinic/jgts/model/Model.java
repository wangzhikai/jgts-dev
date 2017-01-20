package net.heteroclinic.jgts.model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
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
 * {@code Model} class is a data type representing a GTS mesh.
 * <p>
 * A model should have information of the following three, geometry (vertices),
 * topology (edges), completeness and orientation (triangles).
 * To make a model render-able, we need the following steps:
 * <blockquote><pre>
 *     //read the model from file by URL or absolute file path eg.
 *     model.initFromFile(TestGTSModelBunnyNEWT.class.getClassLoader(), fn);
 *     //to use a fixed camera for different models, we fit the model to a bounding box.
 *     model.OBJ_SIZE = 10f;
 *     model.calculateScale();
 *     //re-center the model, so scaling is proper, see discussion in tutorial TODO 
 *     model.offSet();
 *     //calculate the normals of the triangles, also the normal of the vertices
 *     model.calculateTrianglesNormals();
 * </pre></blockquote><p> 
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class Model {
	public static final int BORDER_VERTEX = 1;
	public static final int COMMON_VERTEX = 2;
	public static final int VALID_EDGE = 3;
	public static final int VALID_TRIANGLE = 4;
	public static final int INVALID = -1;

	protected float scale1, scale2, scale3;
	protected float scale;

	protected int verticesCount;
	public int getVerticesCount() {
		return verticesCount;
	}
	protected int verticesCountOrigin = 0;
	protected int verticesLimit;
	
	protected int edgesCount;
	public int getEdgesCount() {
		return edgesCount;
	}
	protected int edgesCountOrigin = 0;
	protected int edgesLimit;

	protected int trianglesCount;
	public int getTrianglesCount() {
		return trianglesCount;
	}
	protected int trianglesCountOrigin = 0;
	protected int trianglesLimit;
	
	protected float xmin, xmax, ymin, ymax, zmin, zmax;
	protected float edgemin, edgemax;

	// Vector [] verticesNomals;
	// float [] verticesNomalsHeap;
	// Vector [][] trianlgesNomals;
	protected Vertex[] vertices;
	protected GTSEdge[] edges;
	protected GTSTriangle[] triangles;

	/**
	 * Print a small size mesh file.
	 */
	public void print() {
		System.out.println("vertices: " + vertices.length);
		for (Vertex v : vertices) {
			v.print();
		}
		System.out.println("edges:" + edges.length);
		for (GTSEdge e : edges) {
			e.print();
		}

		System.out.println("triangles:" + triangles.length);
		for (GTSTriangle t : triangles) {
			t.print();
		}
	}
	
	/**
	 * Show the bounding geometry
	 */
	public void printBounding() {
		System.out.println("xmin,xmax: " + xmin + "," + xmax);
		System.out.println("ymin,ymax: " + ymin + "," + ymax);
		System.out.println("zmin,zmax: " + zmin + "," + zmax);
	}

	/**
	 * Load a file in the resource folder by a ClassLoader to get the URL. 
	 * @param cl the ClassLoader that can provide a absolute URL
	 * @param fn xxxx.xxx file name, no path 
	 */
	public void initFromFile(final ClassLoader cl, final String fn) {
		URL fileURL = cl.getResource(new File(fn).getName());
		initFromFile(fileURL);
	}

	/**
	 * Load a file in the resource folder by a xxxx.xxx name string. 
	 * @param fn xxxx.xxx file name, no path
	 */
	public void initFromFile(final String fn) {
		URL fileURL = Model.class.getClassLoader().getResource(new File(fn).getName());
		initFromFile(fileURL);
	}

	/**
	 * @param fileURL URL is better way representing a absolute path on different operating systems
	 */
	public void initFromFile(URL fileURL) {
		// GLfloat xmin,xmax,ymin,ymax,zmin,zmax;
		// xmin = xmax= ymin= ymax= zmin= zmax = 0;
		float x = 0.0f, y = 0.0f, z = 0.0f;
		int v1 = 0, v2 = 0;
		// GTSEdge tmpep = new GTSEdge (new Edge(0,0), -1, -1);
		Edge tmpep = new Edge(0, 0);
		Triangle tmpt = new Triangle(0, 0, 0);
		Point tmpp = new Point(0.0f, 0.0f, 0.0f);

		// URL fileURL = Model.class.getClassLoader().getResource(new
		// File(fn).getName());

		FileInputStream fis = null;
		BufferedReader br = null;
		try {

			fis = new FileInputStream(new File(fileURL.toURI()));

			// Construct BufferedReader from InputStreamReader
			br = new BufferedReader(new InputStreamReader(fis));

			// Read the first line, to get totalVertices, totalEdges,
			// totalTriangles
			String line = br.readLine();

			if (null != line) {
				String strFirstLine = line;
				String[] indices = strFirstLine.split("\\s");
				try {
					this.verticesCount = Integer.parseInt(indices[0]);
					this.edgesCount = Integer.parseInt(indices[1]);
					this.trianglesCount = Integer.parseInt(indices[2]);

				} catch (Exception e) {
					System.out.println("Fail to parse header.");
					// throw again to goto disaster handle.
					throw new Exception("Fail to parse header.");
				}
			} else {
				System.out.println("Fail to read the first line.");
				throw new Exception("Fail to read the first line.");
			}
			// vertex (x,y,z); edge (beginVertexIndex, endVertexIndex); triangle
			// (edgeOneIndex,edgeTwoIndex,edgeThreeIndex)
			// End read the first line

			this.vertices = new Vertex[this.verticesCount];
			this.edges = new GTSEdge[this.edgesCount];
			this.triangles = new GTSTriangle[this.trianglesCount];

			{
				// Read the vertices (geometry)
				int count = 0;
				for (int i = 0; i < this.verticesCount; i++) {
					try {
						line = br.readLine();
						String[] arr = line.split("\\s");
						x = Float.parseFloat(arr[0]);
						count++;
						y = Float.parseFloat(arr[1]);
						count++;
						z = Float.parseFloat(arr[2]);
						count++;
						tmpp.x = x;
						tmpp.y = y;
						tmpp.z = z;
						// mp->addAVertex(tmpp);
						addAVertexToVlist(tmpp, i + 1, COMMON_VERTEX);
						xmin = (xmin < x) ? xmin : x;
						ymin = (ymin < y) ? ymin : y;
						zmin = (zmin < z) ? zmin : z;
						xmax = (xmax > x) ? xmax : x;
						ymax = (ymax > y) ? ymax : y;
						zmax = (zmax > z) ? zmax : z;
					} catch (Exception e) {
						System.out.println("Fail to parse vertex at row " + (i));
						// throw again to goto disaster handle.
						throw new Exception("Fail to parse vertex at row " + (i));
					}
				}
				if (count != this.verticesCount * 3) {
					throw new Exception("Read vertices ");
				}
				// Read the vertices (geometry)
			}

			{
				// TO-DO metaphorically read the edges the same way
				int count = 0;
				for (int i = 0; i < this.edgesCount; i++) {
					try {
						line = br.readLine();
						String[] arr = line.split("\\s");
						v1 = Integer.parseInt(arr[0]);
						count++;
						v2 = Integer.parseInt(arr[1]);
						count++;

						tmpep.beg = v1;
						tmpep.fin = v2;
						addAnEdge(tmpep, i + 1, VALID_EDGE);

						// TODO where is tmpe1 computed it is the current edges'
						// length, is it used later.
						// if (minedge == 0.0)
						// minedge = tmpe1;
						//
						// minedge = (minedge < tmpe1 )?minedge:tmpe1;
						// maxedge = (maxedge > tmpe1 )?maxedge:tmpe1;

					} catch (Exception e) {
						System.out.println("Fail to parse edge at edge row " + (i));
						// throw again to goto disaster handle.
						throw new Exception("Fail to parse edge at edge row  " + (i));
					}
				}
				if (count != this.edgesCount * 2) {
					throw new Exception("Read edges ");
				}
			}

			{
				// TO-DO metaphorically read the triangles
				int count = 0;
				for (int i = 0; i < this.trianglesCount; i++) {
					try {
						line = br.readLine();
						String[] arr = line.split("\\s");
						tmpt.e1 = Integer.parseInt(arr[0]);
						count++;
						tmpt.e2 = Integer.parseInt(arr[1]);
						count++;
						tmpt.e3 = Integer.parseInt(arr[2]);
						addATriangle(tmpt, i + 1, VALID_TRIANGLE);
						count++;

					} catch (Exception e) {
						System.out.println("Fail to parse triangle at triangle row " + (i));
						// throw again to goto disaster handle.
						throw new Exception("Fail to parse triangle at triangle row  " + (i));
					}
				}
				if (count != this.trianglesCount * 3) {
					throw new Exception("Read triangles ");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception eat) {
			eat.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @param p
	 * @param ci
	 * @param attr
	 * @return
	 */
	protected int addAVertexToVlist(Point p, final int ci, final int attr) {
		vertices[this.verticesCountOrigin] = new Vertex(new Point(p.x, p.y, p.z), ci, attr);
		return ++verticesCountOrigin;
	};

	/**
	 * 
	 * @param pgp
	 * @param nindex
	 * @param nattrib
	 * @return
	 * @throws Exception
	 */
	protected int addAnEdge(Edge pgp, int nindex, int nattrib) throws Exception {
		GTSEdge edge = new GTSEdge(new Edge(pgp.beg, pgp.fin), nindex, nattrib);
		this.edges[this.edgesCountOrigin] = edge;
		vertices[(pgp.beg) - 1].addAnEdge(edge);
		vertices[(pgp.fin) - 1].addAnEdge(edge);
		return ++edgesCountOrigin;
	}

	/**
	 * Adding a triangle to the model.
	 * <p>
	 * Referring to model cube.gts
	 * We know the triangle formed by v5, v6 and v7 is facing us. So the right
	 * hand rule follows. Namely, v7, v6 and v5 are sequenced counter-clock-wise
	 * -- CCW. Let's check the mesh file, we have edges 14 (6,5), 15 (7,6) and
	 * 17 (7,5). We have exactly triangle No. 11 (15,14,17) has all these three
	 * edges. So it is telling us, if a triangle is facing us, the three edges
	 * are in a sequence of counter-clock-wise. So in the three-tuple
	 * representing a triangle, from the first edge we get point p1 which is not
	 * shared with next edge, the other point is p2. The second edge has a point
	 * not shared with the beginning edge p3. We take vector p2p3 as vec1,
	 * vector p2p1 as vec2, the cross product of vec1 and vec2 is the normal of
	 * the triangle.
	 * @param pgp
	 * @param nindex
	 * @param nattrib
	 * @return
	 */
	protected int addATriangle(Triangle pgp, int nindex, int nattrib) {
		// addATriangleToTlist(GTStriangle * pgt,int nindex,int nattrib);
		GTSTriangle triangle = new GTSTriangle(new Triangle(pgp.e1, pgp.e2, pgp.e3), nindex, nattrib);
		this.triangles[this.trianglesCountOrigin] = triangle;

		// modeltriangle * tmpp = addATriangleToTlist(pgp,nindex,nattrib );
		int v1, v2, v3;
		v1 = v2 = v3 = -1;
		// if pgp->e1
		if ((edges[pgp.e1 - 1]).gp.beg == (edges[pgp.e2 - 1]).gp.beg)
			v1 = (edges[pgp.e1 - 1]).gp.beg;
		else {
			if ((edges[pgp.e1 - 1]).gp.beg == (edges[pgp.e2 - 1]).gp.fin)
				v1 = (edges[pgp.e1 - 1]).gp.beg;
			else
				v1 = (edges[pgp.e1 - 1]).gp.fin;
		}

		if (v1 == (edges[pgp.e2 - 1]).gp.beg)
			v2 = (edges[pgp.e2 - 1]).gp.fin;
		else
			v2 = (edges[pgp.e2 - 1]).gp.beg;

		if (v2 == (edges[pgp.e3 - 1]).gp.beg)
			v3 = (edges[pgp.e3 - 1]).gp.fin;
		else
			v3 = (edges[pgp.e3 - 1]).gp.beg;
		vertices[v1 - 1].addATriangle(triangle);
		vertices[v2 - 1].addATriangle(triangle);
		vertices[v3 - 1].addATriangle(triangle);
		return ++trianglesCountOrigin;
	};

	public float OBJ_SIZE;

	/**
	 * Decide the scale to fit the model to a bounding box.
	 * <p>
	 * The bounding box is a cube of OBJ_SIZE. So scale the largest dimension to OBJ_SIZE
	 * together with other dimensions.
	 */
	public void calculateScale() {
		scale1 = OBJ_SIZE / (xmax - xmin);
		scale2 = OBJ_SIZE / (ymax - ymin);
		scale3 = OBJ_SIZE / (zmax - zmin);
		scale = (((OBJ_SIZE / (xmax - xmin) < OBJ_SIZE / (ymax - ymin)) ? OBJ_SIZE / (xmax - xmin)
				: OBJ_SIZE / (ymax - ymin)) < OBJ_SIZE / (zmax - zmin))
						? ((OBJ_SIZE / (xmax - xmin) < OBJ_SIZE / (ymax - ymin)) ? OBJ_SIZE / (xmax - xmin)
								: OBJ_SIZE / (ymax - ymin))
						: OBJ_SIZE / (zmax - zmin);

	}

	/**
	 * 
	 */
	public void offSetWithoutRepositioning() {
		// float tmpxmin = xmin,tmpymin = ymin,tmpzmin = zmin;
		// float tmpxmax = xmax,tmpymax = ymax,tmpzmax = zmax;
		//
		// tmpxmin *= scale; tmpymin *= scale; tmpzmin *= scale;
		// tmpxmax *= scale; tmpymax *= scale; tmpzmax *= scale;
		// //printf("\nscale:%f\n",scale);
		//// float offsetx = (tmpxmin + tmpxmax)/2.0f;
		//// float offsety = (tmpymin + tmpymax)/2.0f;
		//// float offsetz = (tmpzmin + tmpzmax)/2.0f;

		// edgemin *= scale;
		// edgemax *= scale;
		int i = 0;
		// vertices[iv1-1]->p.
		try {
			for (i = 0; i < verticesCount; i++) {
				vertices[i].p.x *= scale;
				// vertices[i].p.x -= offsetx;
				vertices[i].p.y *= scale;
				// vertices[i].p.y -= offsety;
				vertices[i].p.z *= scale;
				// vertices[i].p.z -= offsetz;
			}
		} catch (Exception e) {
			System.out.println("i = " + i);
		}
	}

	/**
	 * Change the vertices' geometry according to the scale calculated.
	 */
	public void offSet() {
		float tmpxmin = xmin, tmpymin = ymin, tmpzmin = zmin;
		float tmpxmax = xmax, tmpymax = ymax, tmpzmax = zmax;

		tmpxmin *= scale;
		tmpymin *= scale;
		tmpzmin *= scale;
		tmpxmax *= scale;
		tmpymax *= scale;
		tmpzmax *= scale;
		// //printf("\nscale:%f\n",scale);
		// float offsetx = (tmpxmin + tmpxmax)/2.0f;
		// float offsety = (tmpymin + tmpymax)/2.0f;
		// float offsetz = (tmpzmin + tmpzmax)/2.0f;

		float offsetx = 0f;
		float offsety = 0f;
		float offsetz = 0f;
		xmin = tmpxmin - offsetx;
		xmax = tmpxmax - offsetx;
		ymin = tmpymin - offsety;
		ymax = tmpymax - offsety;
		zmin = tmpzmin - offsetz;
		zmax = tmpzmax - offsetz;

		edgemin *= scale;
		edgemax *= scale;
		int i = 0;
		// vertices[iv1-1]->p.
		try {
			for (i = 0; i < verticesCount; i++) {
				vertices[i].p.x *= scale;
				vertices[i].p.x -= offsetx;
				vertices[i].p.y *= scale;
				vertices[i].p.y -= offsety;
				vertices[i].p.z *= scale;
				vertices[i].p.z -= offsetz;
			}
		} catch (Exception e) {
			System.out.println("i = " + i);
		}
	}

	public Vector[] trianlgesNomals;
	public Vector[] verticesNomals;

	/**
	 * Calculate the triangles and vertices normals.
	 * <p>
	 * @see #addATriangle(Triangle, int, int) 
	 * We can compute the all triangles'
	 * normals. But mostly, we need the normal of every vertex for
	 * rendering especially we are passing the GPU a strip. In our
	 * approach, we simply sum the normals of the triangles a vertex is on,
	 * then take a normalization of the result. One better approach is to
	 * weigh in the area of each triangle. It is a trivia task. Finally,
	 * the triangulated surface model is an approximation of the real
	 * object we want to render. The quality of the model is rather
	 * dependent on the granularity.
	 */
	public void calculateTrianglesNormals() {

		Point v1 = new Point(0.0f, 0.0f, 0.0f), v2 = new Point(0.0f, 0.0f, 0.0f), v3 = new Point(0.0f, 0.0f, 0.0f);
		Vector vc1 = new Vector(0.0f, 0.0f, 0.0f), vc2 = new Vector(0.0f, 0.0f, 0.0f),
				Norm = new Vector(0.0f, 0.0f, 0.0f);

		if (trianlgesNomals == null || trianlgesNomals.length != trianglesCount) {
			trianlgesNomals = new Vector[trianglesCount];
		}
		if (verticesNomals == null || verticesNomals.length != this.verticesCount) {
			verticesNomals = new Vector[verticesCount];
		}

		int i = 0;
		float t = 1.0f;
		for (i = 0; i < verticesCount; i++) {
			verticesNomals[i] = new Vector(0.0f, 0.0f, 0.0f);
		}

		GTSTriangle pt;
		int iv1 = -1, iv2 = -1, iv3 = -1;
		for (i = 0; i < trianglesCount; i++) {
			trianlgesNomals[i] = new Vector(0.0f, 0.0f, 0.0f);
			pt = triangles[i];

			if (edges[pt.triangle.e1 - 1].gp.beg != edges[pt.triangle.e2 - 1].gp.beg
					&& edges[pt.triangle.e1 - 1].gp.beg != edges[pt.triangle.e2 - 1].gp.fin) {
				iv1 = edges[pt.triangle.e1 - 1].gp.beg;
				iv2 = edges[pt.triangle.e1 - 1].gp.fin;
			} else {
				iv1 = edges[pt.triangle.e1 - 1].gp.fin;
				iv2 = edges[pt.triangle.e1 - 1].gp.beg;

			}

			if (edges[pt.triangle.e2 - 1].gp.beg != edges[pt.triangle.e1 - 1].gp.beg
					&& edges[pt.triangle.e2 - 1].gp.beg != edges[pt.triangle.e1 - 1].gp.fin) {
				iv3 = edges[pt.triangle.e2 - 1].gp.beg;
			} else {
				iv3 = edges[pt.triangle.e2 - 1].gp.fin;
			}

			v1.x = vertices[iv1 - 1].p.x;
			v1.y = vertices[iv1 - 1].p.y;
			v1.z = vertices[iv1 - 1].p.z;
			v2.x = vertices[iv2 - 1].p.x;
			v2.y = vertices[iv2 - 1].p.y;
			v2.z = vertices[iv2 - 1].p.z;
			v3.x = vertices[iv3 - 1].p.x;
			v3.y = vertices[iv3 - 1].p.y;
			v3.z = vertices[iv3 - 1].p.z;

			vc1.x = v1.x - v2.x;
			vc1.y = v1.y - v2.y;
			vc1.z = v1.z - v2.z;
			vc2.x = v1.x - v3.x;
			vc2.y = v1.y - v3.y;
			vc2.z = v1.z - v3.z;
			// float t = crossVector(&vc2,&vc1,&Norm);
			t = MathBasics.crossVector(vc1, vc2, Norm);
			if (t > 0) {
				// Norm already normalized
				trianlgesNomals[i].x = Norm.x;
				trianlgesNomals[i].y = Norm.y;
				trianlgesNomals[i].z = Norm.z;

				verticesNomals[iv1 - 1].x += Norm.x;
				verticesNomals[iv1 - 1].y += Norm.y;
				verticesNomals[iv1 - 1].z += Norm.z;
				verticesNomals[iv2 - 1].x += Norm.x;
				verticesNomals[iv2 - 1].y += Norm.y;
				verticesNomals[iv2 - 1].z += Norm.z;
				verticesNomals[iv3 - 1].x += Norm.x;
				verticesNomals[iv3 - 1].y += Norm.y;
				verticesNomals[iv3 - 1].z += Norm.z;

			} else {
				trianlgesNomals[i].x = 0.0f;
				trianlgesNomals[i].y = 0.0f;
				trianlgesNomals[i].z = 0.0f;

				verticesNomals[iv1 - 1].x = 0.0f;
				verticesNomals[iv1 - 1].y = 0.0f;
				verticesNomals[iv1 - 1].z = 0.0f;
				verticesNomals[iv2 - 1].x = 0.0f;
				verticesNomals[iv2 - 1].y = 0.0f;
				verticesNomals[iv2 - 1].z = 0.0f;
				verticesNomals[iv3 - 1].x = 0.0f;
				verticesNomals[iv3 - 1].y = 0.0f;
				verticesNomals[iv3 - 1].z = 0.0f;

			}
		}
		float l = 0.0f;
		Vector tmpv = null;
		for (i = 0; i < verticesCount; i++) {
			// verticesNomals[i] = new Vector;
			tmpv = verticesNomals[i];
			l = MathBasics.Length((tmpv));
			if (l > 0.0) {
				verticesNomals[i].x /= l;
				verticesNomals[i].y /= l;
				verticesNomals[i].z /= l;
			}
		}
	}
}
