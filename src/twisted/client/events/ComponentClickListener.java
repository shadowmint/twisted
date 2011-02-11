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
import twisted.client.impl.ComponentListener;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/** Allows click listeners to be bound to an element. */
public class ComponentClickListener extends ComponentListener implements HasClickHandlers {

	/** Click listener cache. */
	private static HashMap<Element, ComponentClickListener> localCache = new HashMap<Element, ComponentClickListener>();

	protected ComponentClickListener(Element root) {
		super(root);
	}

	@Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

	/** Returns a cached copy of the click listener for an object. */
	public static ComponentClickListener get(Element root) {
		ComponentClickListener rtn = localCache.get(root);
		if ((rtn == null) && (root != null)) {
			rtn = new ComponentClickListener(root);
			localCache.put(root, rtn);
		}
		return(rtn);
	}

  @Override
  public void fireEvent(GwtEvent<?> event) {
  }
}
