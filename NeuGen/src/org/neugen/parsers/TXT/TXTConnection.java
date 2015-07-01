/// package's name
package org.neugen.parsers.TXT;

/**
 *
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public class TXTConnection extends TXTBase {

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getFrom_loc() {
		return from_loc;
	}

	public void setFrom_loc(int from_loc) {
		this.from_loc = from_loc;
	}

	public int getTo_loc() {
		return to_loc;
	}

	public void setTo_loc(int to_loc) {
		this.to_loc = to_loc;
	}
	private String from;
	private String to;
	private int from_loc;
	private int to_loc;
}
