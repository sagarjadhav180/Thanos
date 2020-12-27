package call;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import common.BaseClass;

public class InvocationCount implements IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation arg0, Class arg1, Constructor arg2, Method arg3) {
		// TODO Auto-generated method stub
		
		String num = System.getProperty("count");
//		 int count = Integer.parseInt(num);
		
		 try {
			  if ("uploadCalls".equals(arg3.getName())) {
		            ((ITestAnnotation) arg0).setInvocationCount(100);
		        }			 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		
	}

}
