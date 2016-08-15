package singleton;

public enum SingletonDemo4 {
	INSTANCE {
		@Override
		public String getName() {
			return "Hello Ace!";
		}
	};
	
	public abstract String getName();
}
