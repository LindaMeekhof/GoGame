package client;
/**
 * Represents the possibilities of stones in the game GO. 
 * There are three possibilities: Stone.w, Stone.b and Stone._
 * Stone.b is a black stone.
 * Stone.w is a white stone
 * Stone._ is no stone on the field
 * Final exercise module 1 Nedap University.
 * 
 * @author Linda.Meekhof
 *
 */
public enum Stone {

	__, b, w;
	
/**
 * Returns the other color stone. Probably useful for AI strategy.
 */
/*@ensures this == Stone.w ==> \result == Stone.b;
 *@ensures this == Stone.b ==> \result == Stone.w;
 *@ensures this == Stone._ ==> \result == Stone._;
 */
	public Stone other() {
	    if (this == b) {
            return w;
        } else if (this == w) {
            return b;
        } else {
            return __;
        }
    }
	
	//ToString ??
	public String toString() {
		if (this.equals(Stone.__)) {
			return "+";
		} else if (this.equals(Stone.b)) {
			return "b";
		} else if (this.equals(Stone.w)) {
			return "w";
		}
		return null;
	}
}