/** 
 * Copyright 2010 Douglas Linder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twisted.client.impl;

import java.util.HashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;

/** 
 * Converts arbitrary Elements into valid GWT containers.
 * <p>
 * In general HTML elements are not valid containers for GWT events
 * and widgets; this wrapper enables that functionality.
 * <p>
 * Use the twisted.client.events.* classes to handle event bindings 
 * on DOM elements directly; they will call this, but also have event
 * bindings. Cache is part of ComponentFrame now so the event listeners
 * can share elements with events.
 */
public class ComponentFrame extends AbsolutePanel {
	
	/** Click listener cache. */
	private static HashMap<Element, ComponentFrame> cache = new HashMap<Element, ComponentFrame>();
	
	/** Internal element. */
	protected Element root = null;
	
	/** 
	 * Creates a component containers from an element. 
	 * <p>
	 * To avoid memory use consider using get() instead.
	 */
	protected ComponentFrame(Element root) {
		super(root.<com.google.gwt.user.client.Element> cast());
        onAttach();
		this.root = root;
	}
	
	/** Returns the root element. */
	public Element getRootElement() {
		return(root);
	}
	
	/** Returns a cached copy of the click listener for an object. */
	public static ComponentFrame get(Element root) {
		ComponentFrame rtn = cache.get(root);
		if ((rtn == null) && (root != null)) {
			rtn = new ComponentFrame(root);
			cache.put(root, rtn);
		}
		return(rtn);
	}
}
