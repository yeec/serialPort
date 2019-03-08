package bros.manage.entity;

public class ClientConfigs {
	public ClientConfigs(String address, String port, int id, String flag) {
		this.address = address;
		this.port = port;
		this.flag = flag;
		this.id = id;
	}
	public ClientConfigs() {
		
	}
	public String address;
	public String port;
	public String flag;
	public int id;
}
