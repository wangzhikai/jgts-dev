package net.heteroclinic.jgts.model.joglext;

import com.jogamp.opengl.GL2;

import net.heteroclinic.computergraphics.derivedfromopengl.GLBasket;
import net.heteroclinic.jgts.model.GTSEdge;
import net.heteroclinic.jgts.model.GTSTriangle;
import net.heteroclinic.jgts.model.Model;
import net.heteroclinic.jgts.model.Vertex;

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
 * {@code DrawableModel} provides the rendering methods of models.
 * <p>
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class DrawableModel extends Model {
	/**
	 * Draw the model as a mesh.
	 * @param gl GL2 or above
	 */
	public void drawByEdges(GL2 gl) {
		// float scale1,scale2,scale3;
		// scale1 = OBJ_SIZE/(xmax - xmin);
		// scale2 = OBJ_SIZE/(ymax - ymin);
		// scale3 = OBJ_SIZE/(zmax - zmin);
		// float scale =
		// (
		// ((OBJ_SIZE/(xmax - xmin)<OBJ_SIZE/(ymax - ymin))
		// ?OBJ_SIZE/(xmax - xmin):OBJ_SIZE/(ymax - ymin))<OBJ_SIZE/(zmax -
		// zmin)
		// )
		// ?
		// ((OBJ_SIZE/(xmax - xmin)<OBJ_SIZE/(ymax - ymin))
		// ?OBJ_SIZE/(xmax - xmin):OBJ_SIZE/(ymax - ymin)):OBJ_SIZE/(zmax -
		// zmin);
		gl.glPushMatrix();
		// gl.glScalef(scale,scale,scale);
		gl.glTranslatef(-(xmin + xmax) / 2, -(ymin + ymax) / 2, -(zmin + zmax) / 2);

		gl.glPointSize((float) 1.0);
		gl.glBegin(GL2.GL_LINES);
		int i = 0;
		// modeledge * mlp;
		// Vertex * vp;
		for (i = 0; i < edgesCount; i++) {
			// glVertex3f(vertices[i]->p.x,vertices[i]->p.y,vertices[i]->p.z);
			// edges[i]
			GTSEdge mlp = edges[i];
			Vertex vp = vertices[mlp.gp.beg - 1];
			gl.glVertex3f(vp.p.x, vp.p.y, vp.p.z);
			vp = vertices[mlp.gp.fin - 1];
			gl.glVertex3f(vp.p.x, vp.p.y, vp.p.z);
		}
		gl.glEnd();
		gl.glPopMatrix();
	}

	/**
	 * Draw the model with OpenGL smooth shading.
	 * @param gl GL2 or above
	 */
	public void drawSmoothShading(GL2 gl) {
		// printf("\n draw bunny smooth shading");

		// float scale1,scale2,scale3;
		// scale1 = OBJ_SIZE/(xmax - xmin);
		// scale2 = OBJ_SIZE/(ymax - ymin);
		// scale3 = OBJ_SIZE/(zmax - zmin);
		// float scale =
		// (
		// ((OBJ_SIZE/(xmax - xmin)<OBJ_SIZE/(ymax - ymin))
		// ?OBJ_SIZE/(xmax - xmin):OBJ_SIZE/(ymax - ymin))<OBJ_SIZE/(zmax -
		// zmin)
		// )
		// ?
		// ((OBJ_SIZE/(xmax - xmin)<OBJ_SIZE/(ymax - ymin))
		// ?OBJ_SIZE/(xmax - xmin):OBJ_SIZE/(ymax - ymin)):OBJ_SIZE/(zmax -
		// zmin);
		gl.glPushMatrix();
		// gl.glScalef(scale,scale,scale);
		// glScalef(1.0,1.0,1.0);
		gl.glTranslatef(-(xmin + xmax) / 2, -(ymin + ymax) / 2, -(zmin + zmax) / 2);
		GTSTriangle pt = null;
		int iv1 = -1, iv2 = -1, iv3 = -1;
		// printf("\n %d triangle count",trianglesCount);
		gl.glDisable(GL2.GL_CULL_FACE);
		for (int i = 0; i < trianglesCount; i++) {
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

			gl.glBegin(GL2.GL_TRIANGLES);
			// glNormal3f(Norm.x*scale,Norm.y*scale,Norm.z*scale);
			// glScalef(scale,scale,scale);
			// glNormal3f((trianlgesNomals[i])->x*scale,(trianlgesNomals[i])->y*scale,(trianlgesNomals[i])->z*scale);
			// //glNormal3fv((GLfloat *)trianlgesNomals[i]);
			gl.glNormal3f((verticesNomals[iv1 - 1]).x * scale, (verticesNomals[iv1 - 1]).y * scale,
					(verticesNomals[iv1 - 1]).z * scale);
			// gl.glVertex3fv((GLfloat *)&vertices[iv1-1].p);
			GLBasket.glVertex3fv(gl, vertices[iv1 - 1].p);
			gl.glNormal3f((verticesNomals[iv2 - 1]).x * scale, (verticesNomals[iv2 - 1]).y * scale,
					(verticesNomals[iv2 - 1]).z * scale);
			// gl.glVertex3fv((GLfloat *)&vertices[iv2-1].p);
			GLBasket.glVertex3fv(gl, vertices[iv2 - 1].p);
			gl.glNormal3f((verticesNomals[iv3 - 1]).x * scale, (verticesNomals[iv3 - 1]).y * scale,
					(verticesNomals[iv3 - 1]).z * scale);
			// gl.glVertex3fv((GLfloat *)&vertices[iv3-1].p);
			GLBasket.glVertex3fv(gl, vertices[iv3 - 1].p);
			gl.glEnd();
		}

		gl.glPopMatrix();
	}

}
