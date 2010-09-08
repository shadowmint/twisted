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

package twisted.client.events;

import java.util.HashMap;

import twisted.client.impl.ComponentFrame;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

/** Allows key listeners to be bound to an element. */
public class ComponentLoadListener extends ComponentFrame implements HasLoadHandlers {

	/** Click listener cache. */
	private static HashMap<Element, ComponentLoadListener> localCache = new HashMap<Element, ComponentLoadListener>();

	protected ComponentLoadListener(Element root) {
		super(root);
	}

	/** Returns a cached copy of the click listener for an object. */
	public static ComponentLoadListener get(Element root) {
		ComponentLoadListener rtn = localCache.get(root);
		if ((rtn == null) && (root != null)) {
			rtn = new ComponentLoadListener(root);
			localCache.put(root, rtn);
		}
		return(rtn);
	}

	@Override
	public HandlerRegistration addLoadHandler(LoadHandler handler) {
        return addDomHandler(handler, LoadEvent.getType());
	}
}
