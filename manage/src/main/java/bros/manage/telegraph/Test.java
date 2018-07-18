package bros.manage.telegraph;

public class Test {

	public static void main(String[] args) {
		String s = "SOH345345SOHZCZCETXhhETXNNN";
		String str = getTele(s);
		System.out.println(str);
		

	}
	
	public static String getTele(String s){
		boolean flag = true;
		while(flag){
			int a = s.lastIndexOf("ZCZC");
			int c = s.lastIndexOf("SOH");
			if(a!=-1){
				int b = s.lastIndexOf("NNNN");
				s = s.substring(a, b);
				if(s.indexOf("NNNN")==-1){
					s = s + "NNNN";
					flag = false;
					continue;
				}
			}else if(c!=-1){
				int d = s.lastIndexOf("ETX");
				s = s.substring(c, d);
				if(s.indexOf("ETX")==-1){
					s = s + "ETX";
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
