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

import twisted.client.ComponentContainer;

/**
 * Factory for creating component instances.
 * <p>
 * The component factory is the key to actually getting components
 * onto the webpage; it translates a ComponentContainer into a
 * component instance.
 * <p>
 * This is required because the reflection API is not available.
 * It is also useful for making the code splitting work properly,
 * if that is required.
 * <p>
 * A good implementation will use async callbacks to create component
 * instances.
 */
public abstract class ComponentFactory {
	
	/** 
	 * Returns a component for a given root element. 
	 * <p>
	 * This function may include async callbacks, so it
	 * should return a Component via the componentCreated()
	 * call on root.getRegister(); which is why the return
	 * is null.
	 * <p>
	 * Calls to this function are also deferred via Timeout,
	 * so errors cannot propagate upwards.
	 */
	public abstract void createComponent(ComponentContainer root, String requestId);
}
