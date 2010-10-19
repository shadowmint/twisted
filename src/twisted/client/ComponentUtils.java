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

package twisted.client;

import com.google.gwt.dom.client.Element;

/** Utility functions for components. */
public class ComponentUtils {
	
	/** The component container to work with. */
	protected Component root = null;
	
	public ComponentUtils (Component root) {
	  this.root = root;
	}
	
	/** Returns the associated parent component. */
	public Component getParent() {
	  return(root);
	}
	
	/** Returns an asset for the component. */
	public Element getAsset(String id) {
		return(root.getContainer().getAsset(id));
	}
	
	/** Returns a value for the component. */
	public String getValue(String id) {
		return(root.getContainer().getValue(id));
	}
	
	/** Returns the api for a component by id. */
	public ComponentApi getApi(String id) {
	  ComponentApi rtn = null;
	  Component src = root.getContainer().getRegister().getComponent(id);
	  if (src != null)
	    rtn = src.api();
	  return(rtn);
	}
	
	/** Returns the api for a component by type. */
	public ComponentApi getApiByType(String type) {
	  ComponentApi rtn = null;
	  Component src = root.getContainer().getRegister().getComponentByType(type);
	  if (src != null)
	    rtn = src.api();
	  return(rtn);
	}
}
