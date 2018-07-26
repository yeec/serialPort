package bros.manage.telegraph;

import java.util.concurrent.atomic.AtomicInteger;

public class Test {

	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();
//		String s = "ZCZC345345SOHZCZCETXhhETXNNNN";
//		String str = getTele(s);
//		System.out.println(str);
//		System.out.println(System.currentTimeMillis() - startTime);
		AtomicInteger ai = new AtomicInteger(999999999);
		while(true){
			int a = ai.incrementAndGet();
			System.out.println(a);
		}

	}
	
	public static String getTele(String s){
		
		StringBuilder ss = new StringBuilder();
		boolean flag = true;
		while(flag){
			int a = s.lastIndexOf("ZCZC");
			int c = s.lastIndexOf("SOH");
			if(a!=-1){
				int b = s.lastIndexOf("NNNN");
				s = s.substring(a, b);
				if(s.indexOf("NNNN")==-1){
					ss.append(s);
					ss.append("NNNN");
					s = s + "NNNN";
					flag = false;
					continue;
				}
			}else if(c!=-1){
				int d = s.lastIndexOf("ETX");
				s = s.substring(c, d);
				if(s.indexOf("ETX")==-1){
					s = s + "ETX";
					ss.append(s);
					ss.append("ETX");
					flag = false;
					continue;
				}
			}else{
				s = "錯了";
				flag = false;
				continue;
			}
		}
		return s;
	}

}
