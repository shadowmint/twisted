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

import java.util.ArrayList;
import java.util.HashMap;

import twisted.client.events.handlers.RightClickHandler;
import twisted.client.impl.ComponentFrame;
import twisted.client.impl.ComponentListener;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/** Allows key listeners to be bound to an element. */
public class ComponentRightClickListener extends ComponentListener implements HasHandlers {

	/** Click listener cache. */
	private static HashMap<Element, ComponentRightClickListener> localCache = new HashMap<Element, ComponentRightClickListener>();

	/** Set of click listeners for this object. */
	private ArrayList<RightClickHandler> handlers = new ArrayList<RightClickHandler>();

	protected ComponentRightClickListener(Element root) {
		super(root);
	  bindEventHandler(this, root);
	}

	/** Returns a cached copy of the click listener for an object. */
	public static ComponentRightClickListener get(Element root) {
		ComponentRightClickListener rtn = localCache.get(root);
		if ((rtn == null) && (root != null)) {
			rtn = new ComponentRightClickListener(root);
			localCache.put(root, rtn);
		}
		return(rtn);
	}

	public HandlerRegistration addRightClickHandler(final RightClickHandler handler) {
	  HandlerRegistration rtn = new HandlerRegistration() {
      @Override
      public void removeHandler() {
        handlers.remove(handler);
      }
	  };
	  handlers.add(handler);
	  return(rtn);
	}

  /** Handles right click events. */
  private void handleEvent(NativeEvent event) {
    for(RightClickHandler handler : handlers) {
      handler.onRightClick(event);
    }
  }

  /** Binds the right click event handler. */
  private native void bindEventHandler(ComponentRightClickListener self, Element target) /*-{
    target.oncontextmenu = function(event) {
      self.@twisted.client.events.ComponentRightClickListener::handleEvent(Lcom/google/gwt/dom/client/NativeEvent;)(event);
      return(false);
    };
  }-*/;

  @Override
  public void fireEvent(GwtEvent<?> event) {
  }
}
