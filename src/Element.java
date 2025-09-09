package HW;

/**
 * Represents a node in the Robin Hood Hashing table. Stores the key, probe
 * length, a reference to the next node, importance value, and word length.
 * Extends {@link RobinHoodHashing}.
 */
public class Element extends RobinHoodHashing {

	/** The character key associated with this element. */
	char key;

	/**
	 * The probe length indicating how far the element is from its initial hash
	 * position.
	 */
	int probeLength;

	/**
	 * A reference to the next element in the hash table (used for chaining or
	 * linked structures).
	 */
	RobinHoodHashing next;

	/** The importance score associated with this element. */
	int importance;

	/** The word length associated with this element. */
	int wl;

	/**
	 * Default constructor for the Element class. Initializes the fields with
	 * default values.
	 */
	public Element() {
		key = ' ';
		probeLength = 0;
		next = null;
		importance = 0;
		wl = 0;
	}
}
