package twisted.client.sample;

import twisted.client.Component;
import twisted.client.ComponentApi;
import twisted.client.ComponentContainer;

public class SampleB extends Component {

	public SampleB(ComponentContainer root) {
		super(root);
	}

	@Override
	public ComponentApi api() {
		return null;
	}

	@Override
	public void init() {
		requireValue("Value1");
		requireAsset("Asset1");
	}
	
	@Override
	public void run() {
		complete();
	}
}
