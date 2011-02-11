package twisted.client.sample;

import twisted.client.Component;
import twisted.client.ComponentContainer;
import twisted.client.ComponentFactory;

public class SampleFactory extends ComponentFactory {

	@Override
	public void createComponent(ComponentContainer root, String requestId) {
		Component c;
		if(root.getType().equals("SampleA")) {
			c = new SampleA(root);
			root.getRegister().componentCreated(c, requestId);
		}
		else if(root.getType().equals("SampleB")) {
			c = new SampleB(root);
			root.getRegister().componentCreated(c, requestId);
		}
		else if(root.getType().equals("SampleC")) {
			c = new SampleC(root);
			root.getRegister().componentCreated(c, requestId);
		}
	}
}
