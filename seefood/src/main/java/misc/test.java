package misc;

public class test {

	public static void main(String[] args) {
		 String val = "<html><body background=\"red\">This is a test file.<hr></body></html>";
	        String tagPattern = "<{1}[^>]{1,}>{1}";
	 
	        System.out.println("Before:" + val);
	        System.out.println("After :" + val.replaceAll(tagPattern, ""));

	}

}
