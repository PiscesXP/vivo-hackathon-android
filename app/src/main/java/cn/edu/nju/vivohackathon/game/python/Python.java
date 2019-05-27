package cn.edu.nju.vivohackathon.game.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Python {
	private String stage;
	private String target;
	private int step;
	private int row, col;
	private int[][] grid;
	private int pylength;
	private Coordinate[] python;

	private Coordinate[] changec;


	private Map<Integer, Integer> word;
	private Map<Integer, Integer> rword;
	private Set<Integer> needs;
	private int sequence;


	//grid ÿһ���Ӧ������
	public static final int empty = 0;
	//�ո���
	public static final int pybody = 1;
	//python ������
	public static final int pyhead = 2;
	//python head

	private Python(String stage, String target, int step) {
		this.stage = stage;
		this.target = target;
		this.step = step;

		changec = new Coordinate[3];
		for (int i = 0; i < 3; i++)
			changec[i] = new Coordinate();

		word = new HashMap<Integer, Integer>();
		rword = new HashMap<Integer, Integer>();
		needs = new HashSet<Integer>();
		sequence = 3;
	}

	private void init_grid(int row, int col) {
		this.row = row >= 3 ? row : 3;
		this.col = col >= 3 ? col : 3;
		grid = new int[row][col];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				grid[i][j] = empty;
	}

	private void init_pylength(int length) {
		this.pylength = length >= 3 ? length : 3;
		//Ĭ��python�տ�ʼ�ڵ�һ�У�����length����С����col
		if (length > col)
			length = col;
		python = new Coordinate[length];
		for (int i = 0; i < length; i++) {
			python[i] = new Coordinate();
			python[i].x = 0;
			python[i].y = i;
			grid[0][i] = pybody;
		}
		grid[0][length - 1] = pyhead;
	}

	private void set_grid(int x, int y, int content) {
		grid[x][y] = rword.get(content);
	}

	private void add_word(int w) {
		if (word.containsValue(w)) return;
		word.put(sequence, w);
		rword.put(w, sequence);
		sequence++;
	}

	private void init_needs(String need) {
		for (int i = 0; i < need.length(); i++) {
			int n = (int)need.charAt(i);
			needs.add(rword.get(n));
		}
	}

	public int get_grid(int x, int y) {
		if (grid[x][y] > 2)
			return word.get(grid[x][y]);
		else
			return grid[x][y];
	}

	//python���ƶ�����
	public static final int left = 0;
	public static final int right = 1;
	public static final int up = 2;
	public static final int down = 3;
	//python�Ƿ�ɹ��ƶ�
	public static final int success = 0;
	public static final int fail = 1;

	public int move(int direction) {
		if (step <= 0) return fail;

		Coordinate head = new Coordinate();
		head.x = python[pylength - 1].x;
		head.y = python[pylength - 1].y;
		switch(direction) {
			case left: {
				changec[1] = head;
				head.y -= 1;
				if (head.y < 0) head.y = col - 1;
				if (grid[head.x][head.y] != pybody) {
					int gxy = grid[head.x][head.y];
					if (gxy != empty) {
						needs.remove(gxy);
					}
				} else return fail;
			}break;
			case right: {
				changec[1] = head;
				head.y += 1;
				if (head.y >= col) head.y = 0;
				if (grid[head.x][head.y] != pybody) {
					int gxy = grid[head.x][head.y];
					if (gxy != empty) {
						needs.remove(gxy);
					}
				} else return fail;
			}break;
			case up: {
				changec[1] = head;
				head.x -= 1;
				if (head.x < 0) head.x = row - 1;
				if (grid[head.x][head.y] != pybody) {
					int gxy = grid[head.x][head.y];
					if (gxy != empty) {
						needs.remove(gxy);
					}
				} else return fail;
			}break;
			case down: {
				changec[1] = head;
				head.x += 1;
				if (head.x >= row) head.x = 0;
				if (grid[head.x][head.y] != pybody) {
					int gxy = grid[head.x][head.y];
					if (gxy != empty) {
						needs.remove(gxy);
					}
				} else return fail;
			}break;
			default: return fail;
		}

		Coordinate tail = python[0];
		changec[0] = tail;
		grid[tail.x][tail.y] = 0;
		for (int i = 0; i < pylength - 1; i++) {
			python[i] = python[i + 1];
		}
		grid[python[pylength - 2].x][python[pylength - 2].y] = pybody;
		python[pylength - 1] = head;
		grid[head.x][head.y] = pyhead;
		changec[2] = head;
		step = step - 1;
		return success;
	}

	//���move�ɹ������øú�����ȡ�ı��λ��
	public Coordinate[] move_change() {
		return changec;
	}

	public String get_stage() {
		return stage;
	}

	public String get_target() {
		return target;
	}

	public boolean fail() {
		return step <= 0;
	}

	public boolean pass() {
		return needs.isEmpty();
	}

	public static Python read(BufferedReader br) {
		Python newgame = null;
		try {
			String stage, target;
			int step;
			System.out.println(111);
			if (br.readLine().equals("@stage")) {
				stage = br.readLine();
				System.out.println(stage);
			} else return null;
			System.out.println(222);
			if (br.readLine().equals("@target")) {
				target = br.readLine();
				System.out.println(target);
			} else return null;
			System.out.println(333);
			if (br.readLine().equals("@step")) {
				step = Integer.parseInt(br.readLine());
				System.out.println(step);
			} else return null;
			System.out.println(444);
			newgame = new Python(stage, target, step);

			if (br.readLine().equals("@grid")) {
				String rc = br.readLine();
				String r = rc.split(",")[0], c = rc.split(",")[1];
				int row = Integer.parseInt(r), col = Integer.parseInt(c);
				newgame.init_grid(row, col);
				for (int i = 0; i < row; i++) {
					String line = br.readLine();
					for (int j = 0; j < col; j++) {
						int ch = (int)line.charAt(j);
						if (ch != '*') {
							newgame.add_word(ch);
							newgame.set_grid(i, j, ch);
						}
					}
				}
			} else return null;
			if (br.readLine().equals("@python")) {
				newgame.init_pylength(Integer.parseInt(br.readLine()));
			} else return null;
			if (br.readLine().equals("@needs")) {
				newgame.init_needs(br.readLine());
			} else return null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newgame;
	}
}
