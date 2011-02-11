package twisted.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;

public class ComponentContainerTests extends GWTTestCase {
    
    @Override
    public String getModuleName() {
        return("twisted.Twisted");
    }
    
    private Element genSampleComponent() {
    	Element rtn = Document.get().createDivElement();
    	rtn.setClassName("style1 style2 Component ComponentId-Sample");
    	
    	Element value = Document.get().createDivElement();
    	value.setClassName("style1 style2 ComponentValue ComponentId-Value1");
    	value.setInnerHTML("This is a value for the internal block.");
    	
    	Element asset = Document.get().createDivElement();
    	asset.setClassName("style1 style2 ComponentAsset ComponentId-Asset1");
    	
    	rtn.appendChild(value);
    	rtn.appendChild(asset);
    	
    	return(rtn);
    }
    
    public void testComponentContainer() {
    	Element e = genSampleComponent();
        ComponentContainer a = new ComponentContainer(e, null);
        assertNotNull(a);
    }
    
    public void testGet() {
    	Element e = genSampleComponent();
        ComponentContainer a = ComponentContainer.get(e, null);
        assertNotNull(a);
    }
    
    public void testGetRootElement() {
    	Element e = genSampleComponent();
        ComponentContainer a = new ComponentContainer(e, null);
        Element target = a.getRootElement();
        assertNotNull(target);
    }
    
    public void testGetType() {
    	Element e = genSampleComponent();
        ComponentContainer a = new ComponentContainer(e, null);
        String type = a.getType();
        assertTrue(type.equals("Sample"));
    }
    
    public void testGetAsset() {
    	Element e = genSampleComponent();
        ComponentContainer a = new ComponentContainer(e, null);
        Element asset = a.getAsset("Asset1");
        assertNotNull(asset);
    }
    
    public void testGetValue() {
    	Element e = genSampleComponent();
        ComponentContainer a = new ComponentContainer(e, null);
        String value = a.getValue("Value1");
        if(!value.equals("This is a value for the internal block."))
	        fail("Failed on getValue(): " + value);
        assertTrue(value.equals("This is a value for the internal block."));
    }
}
