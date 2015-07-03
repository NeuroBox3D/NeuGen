/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/// package's name
package org.neugen.parsers.TXT;

/// imports

import javax.vecmath.Point3f;


/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTSynapse {
	/// private members
	
	private String from;

	public Point3f getFrom_point_end() {
		return from_point_end;
	}

	public void setFrom_point_end(Point3f from_point_end) {
		this.from_point_end = from_point_end;
	}

	public Point3f getFrom_point_start() {
		return from_point_start;
	}

	public void setFrom_point_start(Point3f from_point_start) {
		this.from_point_start = from_point_start;
	}

	public Point3f getTo_point_end() {
		return to_point_end;
	}

	public void setTo_point_end(Point3f to_point_end) {
		this.to_point_end = to_point_end;
	}

	public Point3f getTo_point_start() {
		return to_point_start;
	}

	public void setTo_point_start(Point3f to_point_start) {
		this.to_point_start = to_point_start;
	}
	private String to;
	private float from_loc;
	private float to_loc;
	private Point3f from_point_end;
	private Point3f from_point_start;
	private Point3f to_point_end;
	private Point3f to_point_start;

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the from_loc
	 */
	public float getFrom_loc() {
		return from_loc;
	}

	/**
	 * @param from_loc the from_loc to set
	 */
	public void setFrom_loc(float from_loc) {
		this.from_loc = from_loc;
	}

	/**
	 * @return the to_loc
	 */
	public float getTo_loc() {
		return to_loc;
	}

	/**
	 * @param to_loc the to_loc to set
	 */
	public void setTo_loc(float to_loc) {
		this.to_loc = to_loc;
	}

}
