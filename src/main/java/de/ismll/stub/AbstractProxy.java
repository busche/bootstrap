package de.ismll.stub;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The AbstractProxy can be subclassed by Bootstrap Factory Objects (classes with only purpose to provide a convert method from some object to some other object) to provide simple get/set functionality for an interface.
 * 
 * A usual implementation / usage will be as follows:
 * <ol>
 * <li>Define an abstract class (extending this class), being called XXXFactory, containing a static XXXFactory convert() method. T will, most likely, be an interface, or abstract class, which could otherwise not be handled by Bootstrap. The convert() method will parse the input object, and return an implementation object of an appropriate type.
 * <li>Usage classes (Objects called by Bootstrap) will define a field of type XXXFactory annotated by a Parameter annotation. Bootstrap will handle the conversion, s.t. calling getTarget() will return an appropriately parsed object of target type T.
 * </ol>
 * 
 * See http://tech4research.wordpress.com/2013/05/06/indirect-support-for-interfaces-in-bootstrap/
 * 
 * @author Andre Busche
 *
 * @param <T> the target object to provide. Usually an interface, or abstract class
 */
public abstract class AbstractProxy<T> {

	protected Logger log = LogManager.getLogger(getClass());

	private T target;

	protected AbstractProxy(T target) {
		this.target = target;
	}

	protected AbstractProxy() {
		
	}
	
	public T getTarget() {
		if (target == null) {
			log.warn("No proxy target given. Returning null ... (should crash soon...)");
		}
		return target;
	}
	
	/**
	 * Sets the new proxy Target and returns the old one (or null).
	 * 
	 * @param newTarget the new target
	 * @return the old proxy target
	 */
	public T setTarget(T newTarget) {
		T ret = this.target;
		this.target = newTarget;
		return ret;		
	}
}
