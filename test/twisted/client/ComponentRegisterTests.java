package twisted.client;

import twisted.client.sample.SampleFactory;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;

public class ComponentRegisterTests extends GWTTestCase {
    
    @Override
    public String getModuleName() {
        ComponentLog.setDebugLevel(ComponentLog.DEBUG_ALL);
        return("twisted.Twisted");
    }
    
    public Element pageContent() {
    	// Bring on multi-line strings. :/
    	String data = " <div class=\"Component ComponentId-SampleA\" id=\"IdSetToA\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> <div class=\"ComponentValue ComponentId-Value1\">Value</div> </div> <div class=\"Component ComponentId-SampleB\" id=\"IdSetToB\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> <div class=\"ComponentValue ComponentId-Value1\">Value</div> </div> <div class=\"Component ComponentId-SampleC\" id=\"IdSetToC\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> <div class=\"ComponentValue ComponentId-Value1\">Value</div> </div> ";
    	Element rtn = Document.get().getBody();
    	rtn.setInnerHTML("");
    	Element block = Document.get().createDivElement();
    	block.setInnerHTML(data);
    	rtn.appendChild(block);
    	return(rtn);
    }
    
    public void testParse() {
    	SampleFactory f = new SampleFactory();
    	final ComponentRegister r = new ComponentRegister(f);
    	r.setTimeout(2);
    	Element root = pageContent();
    	Timer t = new Timer() {
			@Override
			public void run() {
				finishTest();
			}
    	};
    	r.setTimeout(t, 2000);
    	delayTestFinish(10000);
    	r.parse(root);
    }
    
    public Element pageBadContent() {
    	// Bring on multi-line strings. :/
    	String data = " <div class=\"Component ComponentId-SampleA\" id=\"IdSetToA\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> <div class=\"ComponentValue ComponentId-Value1\">Value</div> </div> <div class=\"Component ComponentId-SampleB\" id=\"IdSetToB\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> <div class=\"ComponentValue ComponentId-Value1\">Value</div> </div> <div class=\"Component ComponentId-SampleC\" id=\"IdSetToC\"> <div class=\"ComponentAsset ComponentId-Asset1\"/> </div> ";
    	Element rtn = Document.get().getBody();
    	rtn.setInnerHTML("");
    	Element block = Document.get().createDivElement();
    	block.setInnerHTML(data);
    	rtn.appendChild(block);
    	return(rtn);
    }
    
    public void testParseFailed() {
    	ComponentLog.setDebugLevel(ComponentLog.DEBUG_ALL);
    	SampleFactory f = new SampleFactory();
    	final ComponentRegister r = new ComponentRegister(f);
    	Element root = pageBadContent();
    	Timer t = new Timer() {
			@Override
			public void run() {
				finishTest();
			}
    	};
    	r.setTimeout(t, 2000);
    	r.parse(root);
    	delayTestFinish(10000);
    }
    
}
