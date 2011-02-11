package twisted.client.sample;

import twisted.client.Component;
import twisted.client.ComponentApi;
import twisted.client.ComponentContainer;

public class SampleA extends Component {

	public SampleA(ComponentContainer root) {
		super(root);
	}

	@Override
	public ComponentApi api() {
		return null;
	}

	@Override
	public void init() {
		requireComponent("IdSetToB");
		Component anyInstance = getContainer().getRegister().getComponentByType("SampleC");
		requireComponentInstance(anyInstance);
		requireValue("Value1");
		requireAsset("Asset1");
	}
	
	@Override
	public void run() {
		complete();
	}
}
