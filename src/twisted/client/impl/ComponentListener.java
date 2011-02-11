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


import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/** Generic listener interface. */
public class ComponentListener {

  /** ComponentFrame for this listener. */
  private ComponentFrame frame = null;

  /** Creates an instance from an element. */
  protected ComponentListener(Element e) {
    frame = ComponentFrame.get(e);
  }

	/** Binds a dom handler */
	protected <H extends EventHandler> HandlerRegistration addDomHandler(H handler, DomEvent.Type<H> type) {
	  return(frame.addDomHandler(handler, type));
	}

	/** Fire event dummy function. */
	protected void firEvent(GwtEvent<?> event) {
	}
}
