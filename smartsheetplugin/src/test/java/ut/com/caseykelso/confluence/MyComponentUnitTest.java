package ut.com.caseykelso.confluence;

import org.junit.Test;
import com.caseykelso.confluence.api.MyPluginComponent;
import com.caseykelso.confluence.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}