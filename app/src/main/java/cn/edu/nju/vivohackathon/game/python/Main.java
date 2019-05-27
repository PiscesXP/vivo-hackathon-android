package cn.edu.nju.vivohackathon.game.python;

public class Main {
	public static void main(String[] args) {
		Mining m = Mining.read("data.Mining");
		System.out.println(m == null);
		m.mine(2, 1);
		System.out.println(m.pass());
		m.mine(3, 1);
		System.out.println(m.pass());
	}
}
