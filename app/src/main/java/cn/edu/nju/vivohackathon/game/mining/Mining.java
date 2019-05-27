package cn.edu.nju.vivohackathon.game.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class Mining {

    private int radius;
    private int energy;
    private String target;
    int diameter;
    private int[][] grid;

    private Map<Integer, String> substance;
    //substance�������ʣ�ÿ������ӳ�䵽һ����������
    private Map<String, Integer> rsubstance;
    //substance��ӳ��
    private Map<Integer, Integer> hardness;
    //Ӳ��
    private Set<Integer> needs;
    //��Ҫ�ռ�����Ŀ
    private int sequence;
    //�������У��ɳ����Լ�����

    ///loss_energy ���صĵ�ǰ����״̬
    public static final int Vigorous = 0x0;
    //��ʣ������
    public static final int Exhausted = 0x1;
    //�����ľ�
    public static final int Overdraft = 0x2;
    //����͸֧���쳣

    //grid״̬���ջ��߲����ã���������ʴ�1��ʼ���
    public static final int empty = 0;
    public static final int unavailable = -1;


    public Mining(int energy, String target) {
        this.energy = energy;
        this.target = target;
        substance = new HashMap<Integer, String>();
        rsubstance = new HashMap<String, Integer>();
        hardness = new HashMap<Integer, Integer>();
        needs = new HashSet<Integer>();
        sequence = 1;
    }

    //��ʼ������������radius����뾶
    private void init_grid(int radius) {
        this.radius = radius;
        diameter = 2 * radius - 1;
        grid = new int[diameter][diameter];
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                if (j + Math.abs((radius - 1) - i) >= diameter)
                    grid[i][j] = unavailable;
                else
                    grid[i][j] = empty;
            }
        }

    }

    private void add_substance(String s, int hard) {
        if (substance.containsValue(s)) return;
        substance.put(sequence, s);
        rsubstance.put(s, sequence);
        hardness.put(sequence, hard);
        sequence++;
    }

    private boolean minable(int x, int y) {
        boolean able = false;
        if (!is_available(x - 1, y - 1)) return true;
        else able |= (grid[x - 1][y - 1] == empty);
        if (!is_available(x - 1, y)) return true;
        else able |= (grid[x - 1][y] == empty);
        if (!is_available(x, y - 1)) return true;
        else able |= (grid[x][y - 1] == empty);
        if (!is_available(x, y + 1)) return true;
        else able |= (grid[x][y + 1] == empty);
        if (!is_available(x + 1, y)) return true;
        else able |= (grid[x + 1][y] == empty);
        if (!is_available(x + 1, y + 1)) return true;
        else able |= (grid[x + 1][y + 1] == empty);
        return able;
    }

    //�ھ�ɹ�����ʧ��
    public static final int success = 0;
    public static final int fail = 1;

    public int mine(int x, int y) {
        if (grid[x][y] == empty) return fail;
        if (minable(x, y)) {
            int content = grid[x][y];
            int cost = hardness.get(content);
            if (cost > energy) {
                return fail;
            } else {
                energy -= cost;
                needs.remove(grid[x][y]);
                grid[x][y] = empty;
                return success;
            }
        } else {
            return fail;
        }
    }

    public boolean is_available(int x, int y) {
        if (x < 0 || x >= diameter) return false;
        if (y < 0 || y >= diameter) return false;
        if (grid[x][y] == unavailable) return false;
        return true;
    }

    private void init_needs(String[] need) {
        for (String i : need) {
            needs.add(rsubstance.get(i));
        }
    }

    private void set_grid(int x, int y, String content) {
        grid[x][y] = rsubstance.get(content);
    }

    public String get_grid(int x, int y) {
        if (grid[x][y] == empty)
            return null;
        else
            return substance.get(grid[x][y]);
    }

    public boolean fail() {
        return energy <= 0;
    }

    public boolean pass() {
        return needs.isEmpty();
    }

    public static Mining read(BufferedReader br) {
        Mining newgame = null;
        try {
            int energy;
            String target;
            if (br.readLine().equals("@energy")) {
                energy = Integer.parseInt(br.readLine());
            } else return null;
            if (br.readLine().equals("@target")) {
                target = br.readLine();
            } else return null;
            newgame = new Mining(energy, target);
            if (br.readLine().equals("@radius")) {
                newgame.init_grid(Integer.parseInt(br.readLine()));
            } else return null;
            if (br.readLine().equals("@substance")) {
                int ntup = Integer.parseInt(br.readLine());
                for (int i = 0; i < ntup; i++) {
                    String[] tup = br.readLine().split(",");
                    newgame.add_substance(tup[3], Integer.parseInt(tup[2]));
                    newgame.set_grid(Integer.parseInt(tup[0]), Integer.parseInt(tup[1]), tup[3]);
                }
            } else return null;
            if (br.readLine().equals("@needs")) {
                newgame.init_needs(br.readLine().split(","));
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newgame;
    }

}
