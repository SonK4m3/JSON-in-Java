package Object;

public class Student {
	String name;
	String id;
	int score;
	
	public Student() {
		
	}
	
	public Student(String name, String id, int score) {
		this.name = name;
		this.id = id;
		this.score = score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public Student createStudent() {
		Student stu = new Student();
		
		stu.setName("Son");
		stu.setId("B20DCVT311");
		stu.setScore(9);
		
		return stu;
	}
	
	public void print() {
		System.out.println("name: " + name);
		System.out.println("id: " + id);
		System.out.println("score: " + score);
	}
	
}
