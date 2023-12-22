public class Animal{
	String name;
	int age;
	
	public Animal(int age){
		this.age = age;
	}
	
	public String toString(){
		return (this.age + " " + this.name);
	}
	
	public static void main(String[] args){
		Animal ani = new Animal(10);
		System.out.println(ani.age);
		System.out.println(ani.name);
		System.out.println(ani);
	}
}