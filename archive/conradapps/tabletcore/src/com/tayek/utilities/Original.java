package com.tayek.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
class Foo {
    
}
public class Original {
    private static final Map<String,Class<?>> ENTRY_POINTS=new HashMap<String,Class<?>>();
    static {
        //ENTRY_POINTS.put("foo",Foo.class);
        //ENTRY_POINTS.put("bar",Bar.class);
        //ENTRY_POINTS.put("baz",Baz.class);
    }
    public static void main(final String[] args) throws Exception {
        if(args.length<1) {
            // throw exception, not enough args
        }
        final Class<?> entryPoint=ENTRY_POINTS.get(args[0]);
        if(entryPoint==null) {
            // throw exception, entry point doesn't exist
        }
        final String[] argsCopy=args.length>1?Arrays.copyOfRange(args,1,args.length):new String[0];
        entryPoint.getMethod("main",String[].class).invoke(null,(Object)argsCopy);
    }
}
