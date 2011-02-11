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

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Element;

import twisted.client.impl.ComponentFrame;
import twisted.client.impl.ComponentQuery;
import twisted.client.ComponentRegister;

/** 
 * Converts arbitrary Elements into valid GWT containers.
 * <p>
 * This wrapper assumes that a component is in the form:
 * <pre>
 * 		&lt;div class="Component ComponentId-[Component Name]"&gt;
 * 			...
 * 			&lt;div class="ComponentValue ComponentId-[Value Name]"/&gt;
 * 			&lt;div class="ComponentAsset ComponentId-[Asset Name]"/&gt;
 * 		&lt;/div&gt;
 * </pre>
 * Note the type of element is actually irrelevant; divs are used
 * purely as an example.
 * <p>
 * The ComponentId-[VALUE] requires the VALUE field to be a single character
 * only sequence, eg. ComponentId-LoginSubmitButton; these ids are used to
 * allow the component direct access to the important elements of the 
 * component so that it can render itself and find it's dependencies.
 */
public class ComponentContainer extends ComponentFrame {
	
	/** Register this component is associated with. */
	private ComponentRegister register = null;
	
	/** Element type, because we do actually cache this. */
	private String type = null;
	
	/** Cached element to classname mappings. */
	private HashMap<String, ArrayList<Element>> elements = new HashMap<String, ArrayList<Element>>();
	
	/** Set of created element/panel instances. */
	private static HashMap<Element, ComponentContainer> instances = new HashMap<Element, ComponentContainer>();
	
	/** 
	 * Creates a component containers from an element. 
	 * <p>
	 * To avoid memory use consider using get() instead.
	 */
	protected ComponentContainer(Element root, ComponentRegister parent) {
		super(root);
		this.register = parent;
	}
	
	/** Returns the component type */
	public String getType() {
		if (type == null) {
			if (root != null) 
				type = getComponentId(root.getClassName());
			if (type == null) {
				ComponentLog.trace("Found component with no defined type id.");
				type = "";
			}
		}
		return(type);
	}
	
	private String getComponentId(String classname) {
		String rtn = null;
		if (classname != null) {
	 		String[] classes = classname.split(" ");
	 		for (int i = 0; i < classes.length; ++i) {
	 			if (classes[i].startsWith("ComponentId-")) { 
	 				rtn = classes[i].replaceFirst("ComponentId-", "");
	 				break;
	 			}
	 		}
		}
		return(rtn);
	}
	
	/** Returns an instance for the given element. */
	public static ComponentContainer get(Element root, ComponentRegister register) {
		ComponentContainer rtn = instances.get(root);
		if (rtn == null) {
			rtn = new ComponentContainer(root, register);
			instances.put(root, rtn);
		}
		return(rtn);
	}
	
	/** Returns the register. */
	public ComponentRegister getRegister() {
		return(register);
	}
	
	/** Returns an asset by name. */
	public Element getAsset(String id) {
		return(getAsset(id, root));
	}
	
	/** Returns assets by name. */
	public ArrayList<Element> getAssets(String id, int limit) {
		return(getAssets(id, limit, root));
	}
	
	/** Returns an asset by name, specifying base node. */
	public Element getAsset(String id, Element root) {
		ArrayList<Element> set = getAssets(id, 1, root);
		Element rtn = null;
		if (set.size() > 0)
			rtn = set.get(0);
		return(rtn);
	}
	
	/** Returns assets by name, specifying base node. */
	public ArrayList<Element> getAssets(String id, int limit, Element root) {
		ArrayList<Element> rtn = getElements("ComponentAsset", "ComponentId-"+id, limit, root);
		return(rtn);
	}
	
	/** Sets the a specific value, by name. */
	public void setValue(String id, String  value) {
		if ((id != null) && (value != null)) {
			ArrayList<Element> set = getElements("ComponentValue", "ComponentId-"+id, 1, root);
			for (Element e : set) {
				e.setInnerHTML(value);
			}
		}
	}
	
	/** Shortcut to get an element by ID. */
	public static Element getElementById(String id, Element parent) {
	  Element rtn = null; 
		if ((id != null) && (parent != null)) {
		  ComponentQuery q = ComponentQuery.query(id, parent, ComponentQuery.QueryType.ID);
		  if (q.getLength() > 0)
		    rtn = q.getItem(0);
		}
		return(rtn);
	}
	
	/** 
	 * Sets the id for a component. 
	 * <p>
	 * This is specifically public static so it can be used to
	 * process template component blocks before passing them to
	 * the component register.
	 * <p>
	 * It's hard to find a specific target inside the block, since it
	 * won't already have a unique id; so we use the specified class name
	 * to find the element to set the id of; the first element found it
	 * modified only.
	 * <p>
	 * To do that, do something like:<br/>
	 * e.setInnerHtml("... &lt; div class='Component ComponentId-xxx' &gt; ...");<br/>
	 * Element root = ComponentContainer.injectComponentId("ComponentId-xxx", "instanceName", e);
	 */
	public static Element injectComponentId(String target, String id, Element root) {
	  Element rtn = null;
		if (id != null) {
		  ComponentQuery q = ComponentQuery.query(target, root);
		  if (q.getLength() > 0) {
  		  rtn = q.getItem(0);
  		  rtn.setId(id);
		  }
		}
		return(null);
	}
	
	/** 
	 * Sets the a specific value, by name.
	 * <p>
	 * This is specifically public static so it can be used to
	 * process template component blocks before passing them to
	 * the component register.
	 * <p>
	 * To do that, do something like:<br/>
	 * e.setInnerHtml(content);<br/>
	 * Element root = ComponentContainer.injectComponentId(id, e);<br/>
	 * ComponentContainer.injectComponentValue(id, value, root);
	 */
	public static void injectComponentValue(String id, String  value, Element root) {
		if ((id != null) && (value != null)) {
			ArrayList<Element> set = getElementsNoCache("ComponentValue", "ComponentId-"+id, 1, root);
			for (Element e : set) {
				e.setInnerHTML(value);
			}
		}
	}
	
	/** 
	 * Sets the a specific asset, by name.
	 * <p>
	 * This is specifically public static so it can be used to
	 * process template component blocks before passing them to
	 * the component register.
	 * <p>
	 * Note that this will replace the Asset in the template, not
	 * set the asset param as a child element. The class values
	 * 'ComponentAsset' and 'ComponentId-[id]' are added if they
	 * are not present in the asset.
	 * <p>
	 * To do that, do something like:<br/>
	 * e.setInnerHtml(content);<br/>
	 * Element root = ComponentContainer.injectComponentId(id, e);<br/>
	 * ComponentContainer.injectComponentAsset(id, value, root);
	 */
	public static void injectComponentAsset(String id, Element asset, Element root) {
		if ((id != null) && (asset != null)) {
		  if (!asset.getClassName().contains("ComponentAsset"))
  		  asset.addClassName("ComponentAsset");
		  if (!asset.getClassName().contains("ComponentId-"+id))
  		  asset.addClassName("ComponentId-"+id);
			ArrayList<Element> set = getElementsNoCache("ComponentAsset", "ComponentId-"+id, 1, root);
			for (Element e : set) {
			  if (e.getParentElement() != null) {
  			  e.getParentElement().appendChild(asset);
  			  e.getParentElement().removeChild(e);
			  }
			}
		}
	}
	
	/** Returns a value by name. */
	public String getValue(String id) {
		return(getValue(id, root));
	}
	
	/** Returns values by name. */
	public ArrayList<String> getValues(String id, int limit) {
		return(getValues(id, limit, root));
	}
	
	/** Returns a value by name, specifying base node. */
	public String getValue(String id, Element root) {
		ArrayList<String> set = getValues(id, 1, root);
		String rtn = null;
		if (set.size() > 0) 
			rtn = set.get(0);
		return(rtn);
	}
	
	/** Returns values by name, specifying base node. */
	public ArrayList<String> getValues(String id, int limit, Element root) {
		ArrayList<String> rtn = new ArrayList<String>();
		if (id != null) {
			ArrayList<Element> set = getElements("ComponentValue", "ComponentId-"+id, limit, root);
			for (Element e : set) {
				rtn.add(e.getInnerHTML());
			}
		}
		return(rtn);
	}
	
	/** 
	 * Returns elements which are not the child of any other component matching classname.
	 * <p>
	 * The elements returned are ones under the node 'root' provided. To search the entire
	 * component this.root should be passed as 'root'.
	 */
	private ArrayList<Element> getElements(String type, String id, int limit, Element root) {
		ArrayList<Element> rtn = elements.get(type + "-" + id);
		if ((rtn == null) || (rtn.size() > limit)) {
			rtn = getElementsNoCache(type, id, limit, root);
			elements.put(type + "-" + id, rtn);
		}
		return(rtn);
	}
	
	/** 
	 * Returns elements which are not the child of any other component matching classname.
	 * <p>
	 * The results are not cached.
	 * @type The class type, eg. ComponentAsset
	 * @id The component id to match, eg. ComponentId-Panel
	 */
	private static ArrayList<Element> getElementsNoCache(String type, String id, int limit, Element root) {
		ArrayList<Element> rtn = new ArrayList<Element>();
		ComponentQuery ids = ComponentQuery.query(id, root);
		ComponentQuery types = ComponentQuery.query(type, root);
		int count = ids.getLength();
		int found = 0;
		for (int i = 0; (found < limit) && (i < count); ++i) {
			Element e = ids.getItem(i);
			if (types.contains(e))
  			if (getParentComponent(e) == root) 
  				rtn.add(e);
		}
		return(rtn);
	}
	
	/** Finds the parent component of an element. */
	private static Element getParentComponent(Element e) {
		Element rtn = null;
		Element parent = e.getParentElement();
		loop: while ((parent != null) && (rtn == null)) {
			String classname = parent.getClassName();
			if (classname != null) {
				if(classname.contains("Component")) {
					String set[] = classname.split(" ");
					for (String s : set) {
						if (s.equals("Component")) {
							rtn = parent;
							break loop;
						}
					}
				}
			}
			parent = parent.getParentElement();
		}
		return(rtn);
	}
}
