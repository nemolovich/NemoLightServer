/**
 * Implementation of Map object from Java.
 * 
 * @author Brian GOHIER
 * @date 18/07/2014
 * @see http://docs.oracle.com/javase/7/docs/api/java/util/Map.html
 */

/**
 * Default constructor.
 */
function Map() {

	Map.doc = function () {
		return " * Implementation of Map object from Java.<br/>\n" +
				" * <br/>\n" +
				" * <i>@author</i> Brian GOHIER<br/>\n" +
				" * <i>@date</i> 18/07/2014<br/>\n" +
				" * <i>@see</i> <a href='http://docs.oracle.com/javase/7/docs/api/java/util/Map.html'>Java Doc</a><br/>\n";
	};


	/**
	 * {number} : Size of map.
	 */
	var nbElms = 0;
	/**
	 * {[Object]} : The map content.
	 */
	var elements = [];

	/**
	 * Removes all of the mappings from this map.
	 */
	this.clear = function () {
		nbElms = 0;
		elements = [];
	};

	/**
	 * Returns <code>true</code> if this map contains a mapping for the
	 * specified key.
	 * 
	 * @param key
	 *            {Object} - The key whose presence in this map is to be tested.
	 * @return {boolean} - <code>true</code> if the key has been found,
	 *         <code>false</code> otherwise.
	 */
	this.containsKey = function (key) {
		return this.indexOf(key) > -1;
	};

	/**
	 * Returns the index of the element having the specified key.
	 * 
	 * @param key
	 *            {Object} - The key whose presence in this map is to be tested.
	 * @return {number} - The index of the element if it has been found,
	 *         <code>-1</code> otherwise.
	 */
	this.indexOf = function (key) {
		var result = -1;
		for (var i = 0; i < nbElms; i++) {
			if (elements[i].key === key) {
				result = i;
				break;
			}
		}
		return result;
	};

	/**
	 * Returns <code>true</code> if this map maps one or more keys to the
	 * specified value.
	 * 
	 * @param value
	 *            {Object} - The value whose presence in this map is to be
	 *            tested.
	 * @return {boolean} - <code>true</code> if the value has been found,
	 *         <code>false</code> otherwise.
	 */
	this.containsValue = function (value) {
		var result = false;
		for (var i = 0; i < nbElms; i++) {
			if (elements[i].value === value) {
				result = true;
				break;
			}
		}
		return result;
	};

	/**
	 * Compares the specified object with this map for equality. Returns
	 * <code>true</code> if the given object is also a map and the two maps
	 * represent the same mappings. More formally, two maps <code>m1</code>
	 * and <code>m2</code> represent the same mappings if
	 * <code>m1.entrySet().equals(m2.entrySet())</code>. This ensures that
	 * the equals method works properly across different implementations of the
	 * Map interface.
	 * 
	 * @param map
	 *            {Object} - The object to be compared for equality with this
	 *            map.
	 * @return {boolean} - <code>true</code> if the specified object is equal
	 *         to this map, <code>false</code>.
	 */
	this.equals = function (map) {
		var result = true;
		var keys = map.keySet();
		if (typeof (map) === typeof (this) && nbElms === map.size()) {
			for (var i = 0; i < nbElms; i++) {
				if (elements[i].key !== keys[i]
						|| elements[i].value !== map.get(keys[i])) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
		}
		return result;
	};

	/**
	 * Returns the value to which the specified key is mapped, or
	 * <code>undefined</code> if this map contains no mapping for the key.
	 * More formally, if this map contains a mapping from a key <code>k</code>
	 * to a value <cdoe>v</code> such that <code>(key===undefined ?
	 * k===undefined : key.equals(k))</code>, then this method returns <code>v</code>;
	 * otherwise it returns <code>udnefined</code>. (There can be at most one
	 * such mapping.)
	 * 
	 * If this map permits <code>undefined</code> values, then a return value
	 * of <code>undefined</code> does not necessarily indicate that the map
	 * contains no mapping for the key; it's also possible that the map
	 * explicitly maps the key to <code>undefined</code>. The containsKey
	 * operation may be used to distinguish these two cases.
	 * 
	 * @param key
	 *            {Object} - The key whose associated value is to be returned.
	 * @return {Object} - The value to which the specified key is mapped, or
	 *         <code>undefined</code> if this map contains no mapping for the
	 *         key.
	 * 
	 */
	this.get = function (key) {
		var result = undefined;
		for (var i = 0; i < nbElms; i++) {
			if (elements[i].key === key) {
				result = elements[i].value;
				break;
			}
		}
		return result;
	};

	/**
	 * Returns informations about this map.
	 * 
	 * @return {string} - The informations about this map.
	 */
	this.getInfos = function () {
		var result = "<b>Map</b>:{<br/>\n";
		result += "\tsize: " + nbElms + "<br/>\n";
		result += "\tdata: " + this.toString() + "<br/>\n";
		result += "}<br/>\n";
		return result;
	};

	/**
	 * Returns <code>true</code> if this map contains no key-value mappings.
	 * 
	 * @return {boolean} - <code>true</code> if this map contains no key-value
	 *         mappings.
	 */
	this.isEmpty = function () {
		return nbElms === 0;
	};

	/**
	 * Returns a Set view of the keys contained in this map. The set is backed
	 * by the map, so changes to the map are reflected in the set, and
	 * vice-versa.
	 * 
	 * @return {[Object]} - A set view of the keys contained in this map.
	 */
	this.keySet = function () {
		var result = [];
		for (var i = 0; i < nbElms; i++) {
			result[i] = elements[i].key;
		}
		return result;
	};

	/**
	 * Associates the specified value with the specified key in this map
	 * (optional operation). If the map previously contained a mapping for the
	 * key, the old value is replaced by the specified value. (A map
	 * <code>m</code> is said to contain a mapping for a key <code>k</code>
	 * if and only if </code>m.containsKey(k)</code> would return <code>true</code>.)
	 * 
	 * @param key
	 *            {Object} - The key with which the specified value is to be
	 *            associated.
	 * @param value
	 *            {Object} - The value to be associated with the specified key.
	 * 
	 * @return {Object} - The previous value associated with <code>key</code>,
	 *         or <code>undefined</code> if there was no mapping for key. (A
	 *         <code>udnefined</code> return can also indicate that the map
	 *         previously associated <code>undefined</code> with <code>key</code>,
	 *         if the implementation supports <code>undefined</code> values.)
	 */
	this.put = function (key, value) {
		var result = undefined;
		if (key !== undefined) {
			var index;
			if (!this.containsKey(key)) {
				index = nbElms;
				result = value;
				nbElms++;
			} else {
				index = this.indexOf(key);
				result = elements[index].value;
			}
			elements[index] = new Object();
			elements[index].key = key;
			elements[index].value = value;
		}
		return result;
	};

	/**
	 * Copies all of the mappings from the specified map to this map. The effect
	 * of this call is equivalent to that of calling <code>clear()</code> then
	 * <code>put(k, v)</code> on this map once for each mapping from key
	 * <code>k</code> to value </code>v</code> in the specified map.
	 * 
	 * @param map
	 *            {Object} - The mappings to be stored in this map.
	 */
	this.putAll = function (map) {
		nbElms = map.size();
		var keys = map.keySet();
		for (var i = 0; i < nbElms; i++) {
			var value = map.get(keys[i]);
			elements[i] = new Object();
			elements[i].key = keys[i];
			elements[i].value = value;
		}
	};

	/**
	 * Removes the mapping for a key from this map if it is present (optional
	 * operation). More formally, if this map contains a mapping from key
	 * <code>k</code> to value <code>v</code> such that
	 * <code>(key===undefined  ? k=== undefined : key.equals(k))</code>, that
	 * mapping is removed. (The map can contain at most one such mapping.)
	 * 
	 * Returns the value to which this map previously associated the key, or
	 * <code>undefined</code> if the map contained no mapping for the key.
	 * 
	 * If this map permits <code>udnefined</code> values, then a return value
	 * of <code>undefined</code> does not necessarily indicate that the map
	 * contained no mapping for the key; it's also possible that the map
	 * explicitly mapped the key to <code>udnefined</code>.
	 * 
	 * The map will not contain a mapping for the specified key once the call
	 * returns.
	 * 
	 * @param key
	 *            {Object} - The key whose mapping is to be removed from the
	 *            map.
	 * @return {Object} - The previous value associated with key, or
	 *         <code>undefined</code> if there was no mapping for key.
	 */
	this.remove = function (key) {
		var result = undefined;
		var found = false;
		for (var i = 0; i < nbElms; i++) {
			if (elements[i].key === key) {
				result = elements[i].value;
				found = true;
			}
			if (found) {
				if (i < nbElms - 1) {
					elements[i] = elements[i + 1];
				}
			}
		}
		if (found) {
			nbElms--;
		}
		return result;
	};

	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * @return {number} - The number of key-value mappings in this map.
	 */
	this.size = function () {
		return nbElms;
	};

	/**
	 * Returns a string version of the Map.
	 * 
	 * @return {string} - The map as string format.
	 */
	this.toString = function () {
		var result = "[ ";
		var isFirst = true;
		for (var i = 0; i < nbElms; i++) {
			var elm = elements[i];
			var isString = (typeof (elm.value) === 'string');
			if (isFirst) {
				isFirst = false;
			} else {
				result += ", ";
			}
			result += elm.key + " = " + (isString ? '"' : '') + elm.value
					+ (isString ? '"' : '');
		}
		result += " ]";
		return result;
	};

	/**
	 * Returns a Collection view of the values contained in this map. The
	 * collection is backed by the map, so changes to the map are reflected in
	 * the collection, and vice-versa.
	 * 
	 * @return {[Object]} - A collection view of the values contained in this
	 *         map.
	 * 
	 */
	this.values = function () {
		var result = [];
		for (var i = 0; i < nbElms; i++) {
			result[i] = elements[i].value;
		}
		return result;
	};
}
new Map();