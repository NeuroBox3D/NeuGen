/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/// package's name
package org.neugen.parsers.TXT;

/// imports
import java.util.ArrayList;
import javax.vecmath.Vector4f;


/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTBase {
	private String name;
	private ArrayList<Vector4f> coordinates = new ArrayList<Vector4f>();
	private int id;

	public String getName() {
		return name;
	}

	public ArrayList<Vector4f> getCoordinates() {
		return coordinates;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCoordinates(ArrayList<Vector4f> coordinates) {
		this.coordinates = coordinates;
	}

	public void setId(int id) {
		this.id = id;
	}
}
