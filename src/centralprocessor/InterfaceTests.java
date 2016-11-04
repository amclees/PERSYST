package centralprocessor;

public class InterfaceTests {

	public static void main(String[] args) {
		ICommunicationsInterface comm = PERSYSTSession.comm;
		System.out.println("Logging in:");
		comm.login("", "");
		System.out.println("Logged in as " + comm.getUsername());
	}

}
