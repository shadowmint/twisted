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

package twisted.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Common base for all component services which are independent servlets.
 * <p>
 * ...
 */
@SuppressWarnings("serial")
public abstract class ComponentServlet extends HttpServlet implements ComponentService {
	
	@Override 
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doRequest(req, resp);
	}
	
	@Override 
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		doRequest(req, resp);
	}
	
	/** Handles the service requests. */
	protected abstract void doRequest(HttpServletRequest req, HttpServletResponse resp);
}