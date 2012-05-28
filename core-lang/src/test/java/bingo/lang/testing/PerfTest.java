package bingo.lang.testing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class PerfTest {
	@Test
	public void testPerf(){
		final List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("22");
		list.add("3");
		RunnableGroup rg = new RunnableGroup("group1", 2);
		
		rg.add("item1",new Runnable() {
			public void run() {
				list.size();
			}
		}, 123);
		
		Runnable run = new Runnable() {
			public void run() {
				list.getClass();
			}
		};
		RunnableGroup group = new RunnableGroup("group1.1", 5);
		group.add("123456",run, 1000000);
		rg.add(group);
		
		rg.add("hahah", new Runnable() {
			public void run() {
				list.get(1);
			}
		}, 321);

		Perf.run("Array.get(native)", rg);
	}
}
