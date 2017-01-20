package net.heteroclinic.jgts.model;
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
 * {@code MathBasics} class gives some basic math functions used in JGTS.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
//TODO resolve the TODO
public class MathBasics {
	/**
	 * Calculate the length of a given vector, namely the second norm.
	 * @param v the given vector
	 * @return length
	 */
	public static float Length (final Vector v) {
		return v.length();
	}

	/**
	 * Calculate the cross-product of two vectors. As the cross-product
	 * definition the sequence of v and w matter.
	 * @param v the first vector
	 * @param w the second vector
	 * @return Vector as the cross product
	 */
	public static Vector crossVector (Vector v, Vector w)
	{
		Vector crossVector = new Vector(0.0f,0.0f,0.0f);
		float cosinTheta, sinTheta, crossVectorLength;
		// dot production v.w
		float vw = v.x*w.x + v.y*w.y + v.z*w.z;

		cosinTheta = vw / (Length((v))*Length((w)));
		sinTheta = (float) Math.sqrt(1-cosinTheta*cosinTheta);
		crossVectorLength = Length((v))*Length((w))*sinTheta;
		
		// TODO if crossVectorLength is almost zero, throw exception
		
		// Normalize it
		crossVector.x = (v.y*w.z - v.z*w.y) / crossVectorLength;
		crossVector.y = (v.z*w.x - v.x*w.z) / crossVectorLength;
		crossVector.z = (v.x*w.y - v.y*w.x) / crossVectorLength;


		return crossVector;
	}
	
	/**
	 * Calculate the cross-product of two vectors. 
	 * <p>
	 * As the cross-product definition the sequence of v and w matter. 
	 * The result is assigned to {@code crossVector}. It returns the length of 
	 * the result vector.
	 * @param v the first vector
	 * @param w the second vector
	 * @param crossVector the result vector
	 * @return length of result vector
	 */
	public static float crossVector (Vector v, Vector w, Vector crossVector)
	{
		float cosinTheta, sinTheta, crossVectorLength;
		// dot production v->w
		float vw = v.x*w.x + v.y*w.y + v.z*w.z;

		cosinTheta = vw / (Length((v))*Length((w)));
		sinTheta = (float) Math.sqrt(1-cosinTheta*cosinTheta);
		crossVectorLength = Length((v))*Length((w))*sinTheta;
		
		// Normalize it
		crossVector.x = (v.y*w.z - v.z*w.y) / crossVectorLength;
		crossVector.y = (v.z*w.x - v.x*w.z) / crossVectorLength;
		crossVector.z = (v.x*w.y - v.y*w.x) / crossVectorLength;

		//crossVector->x = (v->y*w->z - v->z*w->y) ;
		//crossVector->y = (v->z*w->x - v->x*w->z) ;
		//crossVector->z = (v->x*w->y - v->y*w->x) ;

		return crossVectorLength;
	}
}
